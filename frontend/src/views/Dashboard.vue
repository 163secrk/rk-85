<template>
  <div class="page-container">
    <div class="stats-grid">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card card-1">
            <div class="stat-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalCustomers }}</div>
              <div class="stat-label">客户总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card card-2">
            <div class="stat-icon">
              <i class="el-icon-house"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalProperties }}</div>
              <div class="stat-label">房源总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card card-3">
            <div class="stat-icon">
              <i class="el-icon-date"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalViewingRecords }}</div>
              <div class="stat-label">带看记录</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card card-4">
            <div class="stat-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalSubscriptions }}</div>
              <div class="stat-label">签约总数</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">客户分布</span>
          </div>
          <div ref="customerChart" style="height: 300px;"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">房源类型分布</span>
          </div>
          <div ref="propertyChart" style="height: 300px;"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">近期带看记录</span>
          </div>
          <el-table :data="recentViewings" style="width: 100%">
            <el-table-column prop="customerName" label="客户姓名" width="100"></el-table-column>
            <el-table-column prop="propertyTitle" label="房源" show-overflow-tooltip></el-table-column>
            <el-table-column prop="viewingTime" label="带看时间" width="160">
              <template slot-scope="scope">
                {{ scope.row.viewingTime | formatDate('MM-DD HH:mm') }}
              </template>
            </el-table-column>
            <el-table-column prop="satisfaction" label="满意度" width="80">
              <template slot-scope="scope">
                <span :class="getSatisfactionClass(scope.row.satisfaction)">{{ scope.row.satisfaction }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="card">
          <div class="card-header">
            <span class="card-title">待签约合同</span>
          </div>
          <el-table :data="pendingSubscriptions" style="width: 100%">
            <el-table-column prop="subscriptionNo" label="合同编号" width="140"></el-table-column>
            <el-table-column prop="customerName" label="客户" width="80"></el-table-column>
            <el-table-column prop="propertyTitle" label="房源" show-overflow-tooltip></el-table-column>
            <el-table-column prop="totalPrice" label="总价(万)" width="80">
              <template slot-scope="scope">
                {{ scope.row.totalPrice }}万
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag size="mini" type="warning">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalCustomers: 0,
        totalProperties: 0,
        totalViewingRecords: 0,
        totalSubscriptions: 0,
        totalVRPanoramas: 0,
        pendingSubscriptions: 0,
        completedSubscriptions: 0,
        activeCustomers: 0
      },
      recentViewings: [],
      pendingSubscriptions: []
    }
  },
  mounted() {
    this.loadStats()
    this.loadRecentViewings()
    this.loadPendingSubscriptions()
  },
  methods: {
    async loadStats() {
      const res = await this.$axios.get('/dashboard/stats')
      if (res.code === 200) {
        this.stats = res.data
        this.initCharts()
      }
    },
    async loadRecentViewings() {
      const res = await this.$axios.get('/viewing-records')
      if (res.code === 200) {
        this.recentViewings = res.data.slice(0, 5)
      }
    },
    async loadPendingSubscriptions() {
      const res = await this.$axios.get('/subscriptions/search', {
        params: { status: '待签约' }
      })
      if (res.code === 200) {
        this.pendingSubscriptions = res.data
      }
    },
    initCharts() {
      this.$nextTick(() => {
        this.initCustomerChart()
        this.initPropertyChart()
      })
    },
    initCustomerChart() {
      const chart = echarts.init(this.$refs.customerChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: '0', left: 'center' },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['50%', '40%'],
          avoidLabelOverlap: false,
          itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
          label: { show: false },
          data: [
            { value: 8, name: '跟进中' },
            { value: 5, name: '意向客户' },
            { value: 3, name: '已成交' },
            { value: 2, name: '已流失' }
          ],
          color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C']
        }]
      })
    },
    initPropertyChart() {
      const chart = echarts.init(this.$refs.propertyChart)
      chart.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: ['朝阳区', '海淀区', '通州区', '大兴区', '昌平区', '石景山区'] },
        yAxis: { type: 'value' },
        series: [{
          type: 'bar',
          data: [3, 1, 1, 1, 1, 1],
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      })
    },
    getSatisfactionClass(satisfaction) {
      const map = {
        '非常满意': 'tag-success',
        '满意': 'tag-info',
        '一般': 'tag-warning',
        '不满意': 'tag-danger'
      }
      return map[satisfaction] || 'tag-info'
    }
  }
}
</script>

<style scoped>
.stats-grid {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-1 {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-2 {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-3 {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-4 {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-icon {
  font-size: 48px;
  margin-right: 20px;
  opacity: 0.8;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
