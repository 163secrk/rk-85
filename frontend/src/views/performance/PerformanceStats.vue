<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">业绩统计</div>
      <div>
        <el-date-picker
          v-model="selectedMonth"
          type="month"
          placeholder="选择月份"
          value-format="YYYY-MM"
          size="small"
          style="margin-right: 10px;">
        </el-date-picker>
        <el-button type="primary" icon="el-icon-refresh" @click="loadData">刷新数据</el-button>
      </div>
    </div>

    <div class="summary-cards">
      <div class="summary-card card-blue">
        <div class="card-icon"><i class="el-icon-location-outline"></i></div>
        <div class="card-info">
          <div class="card-value">{{ summary.totalViewing }}</div>
          <div class="card-label">总带看量</div>
        </div>
      </div>
      <div class="summary-card card-green">
        <div class="card-icon"><i class="el-icon-document-checked"></i></div>
        <div class="card-info">
          <div class="card-value">{{ summary.totalDeal }}</div>
          <div class="card-label">总成交量</div>
        </div>
      </div>
      <div class="summary-card card-orange">
        <div class="card-icon"><i class="el-icon-money"></i></div>
        <div class="card-info">
          <div class="card-value">{{ summary.totalAmount }}万</div>
          <div class="card-label">成交总额</div>
        </div>
      </div>
      <div class="summary-card card-purple">
        <div class="card-icon"><i class="el-icon-pie-chart"></i></div>
        <div class="card-info">
          <div class="card-value">{{ summary.avgConversion }}%</div>
          <div class="card-label">平均转化率</div>
        </div>
      </div>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="rank" label="排名" width="70" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRankType(scope.row.rank)" size="mini">{{ scope.row.rank }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="agentName" label="经纪人" width="100"></el-table-column>
        <el-table-column label="带看量" width="140" align="center">
          <template slot-scope="scope">
            <div class="stat-cell">
              <div class="stat-main">{{ scope.row.viewingCount || 0 }}</div>
              <div class="stat-sub">目标: {{ scope.row.viewingTarget || 0 }}</div>
              <el-progress
                :percentage="Math.min(scope.row.viewingCompletionRate || 0, 100)"
                :stroke-width="6"
                :color="getProgressColor(scope.row.viewingCompletionRate)">
              </el-progress>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="成交量" width="140" align="center">
          <template slot-scope="scope">
            <div class="stat-cell">
              <div class="stat-main">{{ scope.row.dealCount || 0 }}</div>
              <div class="stat-sub">目标: {{ scope.row.dealTarget || 0 }}</div>
              <el-progress
                :percentage="Math.min(scope.row.dealCompletionRate || 0, 100)"
                :stroke-width="6"
                :color="getProgressColor(scope.row.dealCompletionRate)">
              </el-progress>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="成交总额(万)" width="160" align="center">
          <template slot-scope="scope">
            <div class="stat-cell">
              <div class="stat-main text-success">{{ scope.row.dealAmount || 0 }}</div>
              <div class="stat-sub">目标: {{ scope.row.dealAmountTarget || 0 }}</div>
              <el-progress
                :percentage="Math.min(scope.row.dealAmountCompletionRate || 0, 100)"
                :stroke-width="6"
                :color="getProgressColor(scope.row.dealAmountCompletionRate)">
              </el-progress>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="客户转化率" width="160" align="center">
          <template slot-scope="scope">
            <div class="stat-cell">
              <div class="stat-main text-primary">{{ scope.row.conversionRate || 0 }}%</div>
              <div class="stat-sub">目标: {{ scope.row.conversionRateTarget || 0 }}%</div>
              <div class="conversion-info">
                <span>成交客户: {{ scope.row.dealCustomers || 0 }}</span>
                <span class="divider">/</span>
                <span>总客户: {{ scope.row.totalCustomers || 0 }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="综合评分" width="120" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.score !== null">
              <div class="score-value" :class="getScoreClass(scope.row.score)">{{ scope.row.score }}</div>
              <el-tag :type="getGradeType(scope.row.grade)" size="mini">{{ scope.row.grade || '未评级' }}</el-tag>
            </div>
            <span v-else class="text-muted">未评分</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PerformanceStats',
  data() {
    return {
      selectedMonth: this.getCurrentMonth(),
      tableData: [],
      summary: {
        totalViewing: 0,
        totalDeal: 0,
        totalAmount: 0,
        avgConversion: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    getCurrentMonth() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },
    async loadData() {
      const res = await this.$axios.get(`/performance/monthly/${this.selectedMonth}`)
      if (res.code === 200) {
        this.tableData = res.data
        this.calculateSummary()
      }
    },
    calculateSummary() {
      this.summary.totalViewing = this.tableData.reduce((sum, item) => sum + (item.viewingCount || 0), 0)
      this.summary.totalDeal = this.tableData.reduce((sum, item) => sum + (item.dealCount || 0), 0)
      this.summary.totalAmount = this.tableData.reduce((sum, item) => sum + (parseFloat(item.dealAmount) || 0), 0)
      const validConversion = this.tableData.filter(item => item.conversionRate != null)
      this.summary.avgConversion = validConversion.length > 0
        ? (validConversion.reduce((sum, item) => sum + parseFloat(item.conversionRate), 0) / validConversion.length).toFixed(1)
        : 0
    },
    getRankType(rank) {
      if (rank === 1) return 'danger'
      if (rank === 2) return 'warning'
      if (rank === 3) return 'primary'
      return 'info'
    },
    getProgressColor(rate) {
      const r = parseFloat(rate) || 0
      if (r >= 100) return '#67C23A'
      if (r >= 80) return '#409EFF'
      if (r >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    getScoreClass(score) {
      const s = parseFloat(score) || 0
      if (s >= 90) return 'score-excellent'
      if (s >= 80) return 'score-good'
      if (s >= 60) return 'score-pass'
      return 'score-fail'
    },
    getGradeType(grade) {
      const map = {
        'S': 'danger',
        'A': 'warning',
        'B': 'primary',
        'C': 'success',
        'D': 'info'
      }
      return map[grade] || 'info'
    }
  }
}
</script>

<style scoped>
.summary-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.summary-card {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.summary-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
  margin-right: 16px;
}

.card-blue .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-green .card-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-orange .card-icon {
  background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
}

.card-purple .card-icon {
  background: linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%);
}

.card-info {
  flex: 1;
}

.card-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.card-label {
  font-size: 14px;
  color: #909399;
  margin-top: 4px;
}

.stat-cell {
  padding: 4px 0;
}

.stat-main {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
}

.stat-sub {
  font-size: 12px;
  color: #909399;
  margin-bottom: 6px;
}

.conversion-info {
  font-size: 11px;
  color: #909399;
  margin-top: 4px;
}

.conversion-info .divider {
  margin: 0 4px;
  color: #dcdfe6;
}

.score-value {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 4px;
}

.score-excellent {
  color: #F56C6C;
}

.score-good {
  color: #E6A23C;
}

.score-pass {
  color: #409EFF;
}

.score-fail {
  color: #909399;
}

.text-muted {
  color: #c0c4cc;
  font-size: 13px;
}

.text-success {
  color: #67C23A;
}

.text-primary {
  color: #409EFF;
}
</style>
