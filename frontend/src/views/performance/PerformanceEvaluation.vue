<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">业绩考核评级</div>
      <div>
        <el-date-picker
          v-model="selectedMonth"
          type="month"
          placeholder="选择月份"
          value-format="YYYY-MM"
          size="small"
          style="margin-right: 10px;">
        </el-date-picker>
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
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
        <el-table-column label="业绩指标" width="350">
          <template slot-scope="scope">
            <div class="metrics-grid">
              <div class="metric-item">
                <span class="metric-label">带看量:</span>
                <span class="metric-value">{{ scope.row.viewingCount || 0 }}</span>
              </div>
              <div class="metric-item">
                <span class="metric-label">成交量:</span>
                <span class="metric-value">{{ scope.row.dealCount || 0 }}</span>
              </div>
              <div class="metric-item">
                <span class="metric-label">成交额:</span>
                <span class="metric-value text-success">{{ scope.row.dealAmount || 0 }}万</span>
              </div>
              <div class="metric-item">
                <span class="metric-label">转化率:</span>
                <span class="metric-value text-primary">{{ scope.row.conversionRate || 0 }}%</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="目标完成率" width="250">
          <template slot-scope="scope">
            <div class="completion-rates">
              <div class="rate-item">
                <span class="rate-label">带看</span>
                <el-progress
                  :percentage="Math.min(scope.row.viewingCompletionRate || 0, 100)"
                  :stroke-width="8"
                  size="small"
                  :color="getProgressColor(scope.row.viewingCompletionRate)">
                </el-progress>
              </div>
              <div class="rate-item">
                <span class="rate-label">成交</span>
                <el-progress
                  :percentage="Math.min(scope.row.dealCompletionRate || 0, 100)"
                  :stroke-width="8"
                  size="small"
                  :color="getProgressColor(scope.row.dealCompletionRate)">
                </el-progress>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评分评级" width="180">
          <template slot-scope="scope">
            <div v-if="scope.row.score !== null" class="score-display">
              <div class="score-circle" :class="getScoreCircleClass(scope.row.score)">
                {{ scope.row.score }}
              </div>
              <el-tag :type="getGradeType(scope.row.grade)" size="medium" class="grade-tag">
                {{ scope.row.grade || '未评级' }}
              </el-tag>
            </div>
            <span v-else class="text-muted">未评分</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="openEvaluateDialog(scope.row)">
              {{ scope.row.score !== null ? '重评' : '评分' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      :title="evaluateTitle"
      :visible.sync="evaluateDialogVisible"
      width="550px">
      <el-form :model="evaluateForm" label-width="100px">
        <el-form-item label="经纪人">
          <span class="form-value">{{ evaluateForm.agentName }}</span>
        </el-form-item>
        <el-form-item label="考核月份">
          <span class="form-value">{{ evaluateForm.month }}</span>
        </el-form-item>
        <el-form-item label="综合评分">
          <el-slider
            v-model="evaluateForm.score"
            :min="0"
            :max="100"
            :step="1"
            show-input
            :input-size="50">
          </el-slider>
        </el-form-item>
        <el-form-item label="评级">
          <el-radio-group v-model="evaluateForm.grade">
            <el-radio label="S" border>S级 (95-100)</el-radio>
            <el-radio label="A" border>A级 (85-94)</el-radio>
            <el-radio label="B" border>B级 (70-84)</el-radio>
            <el-radio label="C" border>C级 (60-69)</el-radio>
            <el-radio label="D" border>D级 (0-59)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="评价人">
          <el-input v-model="evaluateForm.evaluator" placeholder="请输入评价人姓名"></el-input>
        </el-form-item>
        <el-form-item label="评价说明">
          <el-input
            v-model="evaluateForm.evaluationRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入评价说明和改进建议"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="evaluateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEvaluate">确认提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PerformanceEvaluation',
  data() {
    return {
      selectedMonth: this.getCurrentMonth(),
      tableData: [],
      evaluateDialogVisible: false,
      evaluateForm: {
        id: null,
        agentId: null,
        agentName: '',
        month: '',
        score: 80,
        grade: 'B',
        evaluator: '',
        evaluationRemark: ''
      }
    }
  },
  computed: {
    evaluateTitle() {
      return this.evaluateForm.id ? '重新评级' : '业绩评级'
    }
  },
  watch: {
    'evaluateForm.score': function(newScore) {
      this.evaluateForm.grade = this.calculateGrade(newScore)
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    calculateGrade(score) {
      const s = parseFloat(score) || 0
      if (s >= 95) return 'S'
      if (s >= 85) return 'A'
      if (s >= 70) return 'B'
      if (s >= 60) return 'C'
      return 'D'
    },
    getCurrentMonth() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },
    async loadData() {
      const res = await this.$axios.get(`/performance/monthly/${this.selectedMonth}`)
      if (res.code === 200) {
        this.tableData = res.data
      }
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
    getScoreCircleClass(score) {
      const s = parseFloat(score) || 0
      if (s >= 90) return 'circle-excellent'
      if (s >= 80) return 'circle-good'
      if (s >= 60) return 'circle-pass'
      return 'circle-fail'
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
    },
    openEvaluateDialog(row) {
      this.evaluateForm = {
        id: null,
        agentId: row.agentId,
        agentName: row.agentName,
        month: this.selectedMonth,
        score: row.score || 80,
        grade: row.grade || 'B',
        evaluator: row.evaluator || '',
        evaluationRemark: row.evaluationRemark || ''
      }
      this.evaluateDialogVisible = true
    },
    async submitEvaluate() {
      if (!this.evaluateForm.evaluator) {
        this.$message.warning('请输入评价人姓名')
        return
      }
      
      const res = await this.$axios.get(`/performance-assessments/agent/${this.evaluateForm.agentId}/month/${this.selectedMonth}`)
      if (res.code !== 200 || !res.data) {
        this.$message.error('未找到对应的业绩记录')
        return
      }
      
      const assessmentId = res.data.id
      
      const params = new URLSearchParams()
      params.append('id', assessmentId)
      params.append('score', this.evaluateForm.score)
      params.append('grade', this.evaluateForm.grade)
      params.append('evaluator', this.evaluateForm.evaluator)
      params.append('evaluationRemark', this.evaluateForm.evaluationRemark)
      
      const result = await this.$axios.post('/performance-assessments/evaluate', params)
      if (result.code === 200) {
        this.$message.success('评分成功')
        this.evaluateDialogVisible = false
        this.loadData()
      } else {
        this.$message.error(result.message)
      }
    }
  }
}
</script>

<style scoped>
.metrics-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}

.metric-item {
  display: flex;
  align-items: center;
  font-size: 13px;
}

.metric-label {
  color: #909399;
  margin-right: 6px;
  min-width: 50px;
}

.metric-value {
  font-weight: 600;
  color: #303133;
}

.completion-rates {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.rate-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.rate-label {
  min-width: 30px;
  font-size: 12px;
  color: #909399;
}

.score-display {
  display: flex;
  align-items: center;
  gap: 12px;
}

.score-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 700;
  color: #fff;
}

.circle-excellent {
  background: linear-gradient(135deg, #F56C6C 0%, #c23a3a 100%);
}

.circle-good {
  background: linear-gradient(135deg, #E6A23C 0%, #b88230 100%);
}

.circle-pass {
  background: linear-gradient(135deg, #409EFF 0%, #337ecc 100%);
}

.circle-fail {
  background: linear-gradient(135deg, #909399 0%, #73767a 100%);
}

.grade-tag {
  font-size: 14px;
  padding: 4px 12px;
}

.form-value {
  color: #303133;
  font-weight: 500;
}

.text-success {
  color: #67C23A;
}

.text-primary {
  color: #409EFF;
}

.text-muted {
  color: #c0c4cc;
  font-size: 13px;
}
</style>
