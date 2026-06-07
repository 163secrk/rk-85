<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">业绩排名</div>
      <div>
        <el-date-picker
          v-model="selectedMonth"
          type="month"
          placeholder="选择月份"
          value-format="YYYY-MM"
          size="small"
          style="margin-right: 10px;">
        </el-date-picker>
        <el-button type="primary" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-data-line" style="margin-right: 8px; color: #409EFF;"></i>
              业绩指标对比
            </span>
            <el-radio-group v-model="chartType" size="small" @change="handleChartTypeChange">
              <el-radio-button label="bar">柱状图</el-radio-button>
              <el-radio-button label="radar">雷达图</el-radio-button>
            </el-radio-group>
          </div>
          <div :class="['chart-container', chartType === 'radar' ? 'chart-radar' : '']">
            <div v-show="chartType === 'bar'">
              <div ref="barChart" class="chart"></div>
              <div class="chart-legend">
                <span class="legend-item"><span class="legend-color color-viewing"></span>带看量</span>
                <span class="legend-item"><span class="legend-color color-deal"></span>成交量</span>
                <span class="legend-item"><span class="legend-color color-amount"></span>成交总额(万)</span>
                <span class="legend-item"><span class="legend-color color-conversion"></span>转化率(%)</span>
              </div>
            </div>
            <div v-show="chartType === 'radar'">
              <div ref="radarChart" class="chart"></div>
              <div class="radar-selector">
                <span class="selector-label">选择经纪人:</span>
                <el-checkbox-group v-model="selectedAgents" @change="updateRadarChart">
                  <el-checkbox
                    v-for="(name, index) in agentNames"
                    :key="name"
                    :label="name"
                    :style="{ color: getRadarColor(index) }">
                    {{ name }}
                  </el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-trophy" style="margin-right: 8px; color: #E6A23C;"></i>
              成交金额排名
            </span>
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in rankingByAmount"
              :key="item.agentId"
              class="ranking-item"
              :class="'rank-' + (index + 1)">
              <div class="rank-badge">
                <span v-if="index < 3" class="medal">{{ ['🥇', '🥈', '🥉'][index] }}</span>
                <span v-else class="rank-number">{{ index + 1 }}</span>
              </div>
              <div class="rank-info">
                <div class="agent-name">{{ item.agentName }}</div>
                <div class="rank-meta">
                  <span>成交: {{ item.dealCount || 0 }}单</span>
                  <span class="divider">|</span>
                  <span>带看: {{ item.viewingCount || 0 }}次</span>
                </div>
              </div>
              <div class="rank-amount">
                <div class="amount-value">{{ item.dealAmount || 0 }}<span class="amount-unit">万</span></div>
                <el-progress
                  :percentage="getAmountPercentage(item.dealAmount)"
                  :stroke-width="6"
                  :show-text="false"
                  :color="getProgressColor(item.dealAmountCompletionRate)">
                </el-progress>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-medal" style="margin-right: 8px; color: #F56C6C;"></i>
              综合评分排名
            </span>
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in rankingByScore"
              :key="item.agentId"
              class="ranking-item"
              :class="'rank-' + (index + 1)">
              <div class="rank-badge">
                <span v-if="index < 3" class="medal">{{ ['🥇', '🥈', '🥉'][index] }}</span>
                <span v-else class="rank-number">{{ index + 1 }}</span>
              </div>
              <div class="rank-info">
                <div class="agent-name">
                  {{ item.agentName }}
                  <el-tag :type="getGradeType(item.grade)" size="mini" style="margin-left: 8px;">
                    {{ item.grade || '未评级' }}
                  </el-tag>
                </div>
                <div class="rank-meta">
                  <span>完成率: {{ getOverallCompletionRate(item) }}%</span>
                </div>
              </div>
              <div class="rank-score">
                <div class="score-ring" :style="getScoreRingStyle(item.score)">
                  <span class="score-text">{{ item.score || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <div class="card">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-s-grid" style="margin-right: 8px; color: #67C23A;"></i>
              详细排名表
            </span>
          </div>
          <el-table :data="performanceList" border stripe>
            <el-table-column prop="rank" label="排名" width="70" align="center">
              <template slot-scope="scope">
                <el-tag :type="getRankType(scope.row.rank)" size="mini">{{ scope.row.rank }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="agentName" label="经纪人" width="100"></el-table-column>
            <el-table-column prop="viewingCount" label="带看量" width="90" align="center">
              <template slot-scope="scope">
                <span class="highlight">{{ scope.row.viewingCount || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dealCount" label="成交量" width="90" align="center">
              <template slot-scope="scope">
                <span class="highlight">{{ scope.row.dealCount || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dealAmount" label="成交总额(万)" width="120" align="center">
              <template slot-scope="scope">
                <span class="text-success highlight">{{ scope.row.dealAmount || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="客户转化率" width="120" align="center">
              <template slot-scope="scope">
                <span class="text-primary highlight">{{ scope.row.conversionRate || 0 }}%</span>
              </template>
            </el-table-column>
            <el-table-column label="综合完成率" width="150" align="center">
              <template slot-scope="scope">
                <el-progress
                  :percentage="getOverallCompletionRate(scope.row)"
                  :stroke-width="8"
                  :color="getProgressColor(getOverallCompletionRate(scope.row))">
                </el-progress>
              </template>
            </el-table-column>
            <el-table-column label="评分" width="100" align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.score !== null" class="score-text-inline" :class="getScoreClass(scope.row.score)">
                  {{ scope.row.score }}
                </span>
                <span v-else class="text-muted">-</span>
              </template>
            </el-table-column>
            <el-table-column label="评级" width="80" align="center">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.grade" :type="getGradeType(scope.row.grade)" size="mini">
                  {{ scope.row.grade }}
                </el-tag>
                <span v-else class="text-muted">-</span>
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

const radarColors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9b59b6']

export default {
  name: 'PerformanceRanking',
  data() {
    return {
      selectedMonth: this.getCurrentMonth(),
      chartType: 'bar',
      agentNames: [],
      selectedAgents: [],
      performanceList: [],
      barChart: null,
      radarChart: null,
      chartData: {
        agentNames: [],
        viewingCounts: [],
        dealCounts: [],
        dealAmounts: [],
        conversionRates: []
      },
      radarData: {
        indicator: [],
        seriesData: []
      }
    }
  },
  computed: {
    rankingByAmount() {
      return [...this.performanceList].sort((a, b) => {
        const amountA = parseFloat(a.dealAmount) || 0
        const amountB = parseFloat(b.dealAmount) || 0
        return amountB - amountA
      })
    },
    rankingByScore() {
      return [...this.performanceList]
        .filter(item => item.score != null)
        .sort((a, b) => {
          const scoreA = parseFloat(a.score) || 0
          const scoreB = parseFloat(b.score) || 0
          return scoreB - scoreA
        })
    },
    maxDealAmount() {
      if (this.performanceList.length === 0) return 1
      return Math.max(...this.performanceList.map(p => parseFloat(p.dealAmount) || 0), 1)
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.barChart) this.barChart.dispose()
    if (this.radarChart) this.radarChart.dispose()
  },
  methods: {
    getCurrentMonth() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },
    async loadData() {
      const res = await this.$axios.get(`/performance/ranking/chart/${this.selectedMonth}`)
      if (res.code === 200) {
        this.chartData = res.data
        this.performanceList = res.data.performanceList || []
        this.agentNames = res.data.agentNames || []
        this.selectedAgents = this.agentNames.slice(0, 4)
        this.$nextTick(() => {
          this.initBarChart()
          this.loadRadarData()
        })
      }
    },
    async loadRadarData() {
      const res = await this.$axios.get(`/performance/radar/chart/${this.selectedMonth}`)
      if (res.code === 200) {
        this.radarData = res.data
        this.$nextTick(() => {
          this.initRadarChart()
        })
      }
    },
    handleResize() {
      if (this.barChart) this.barChart.resize()
      if (this.radarChart) this.radarChart.resize()
    },
    handleChartTypeChange() {
      this.$nextTick(() => {
        if (this.chartType === 'bar') {
          if (!this.barChart) this.initBarChart()
          else this.barChart.resize()
        } else {
          if (!this.radarChart) this.initRadarChart()
          else this.radarChart.resize()
        }
      })
    },
    initBarChart() {
      if (!this.$refs.barChart) return
      
      if (this.barChart) this.barChart.dispose()
      
      this.barChart = echarts.init(this.$refs.barChart)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['带看量', '成交量', '成交总额(万)', '转化率(%)'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '60px',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.chartData.agentNames,
          axisLabel: {
            interval: 0,
            rotate: 0
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '数量/金额',
            position: 'left',
            axisLabel: {
              formatter: '{value}'
            }
          },
          {
            type: 'value',
            name: '转化率(%)',
            position: 'right',
            axisLabel: {
              formatter: '{value}%'
            },
            max: 100
          }
        ],
        series: [
          {
            name: '带看量',
            type: 'bar',
            data: this.chartData.viewingCounts,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ])
            },
            barWidth: '18%'
          },
          {
            name: '成交量',
            type: 'bar',
            data: this.chartData.dealCounts,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#43e97b' },
                { offset: 1, color: '#38f9d7' }
              ])
            },
            barWidth: '18%'
          },
          {
            name: '成交总额(万)',
            type: 'bar',
            data: this.chartData.dealAmounts,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#f6d365' },
                { offset: 1, color: '#fda085' }
              ])
            },
            barWidth: '18%'
          },
          {
            name: '转化率(%)',
            type: 'line',
            yAxisIndex: 1,
            data: this.chartData.conversionRates,
            smooth: true,
            symbol: 'circle',
            symbolSize: 8,
            lineStyle: {
              width: 3,
              color: '#F56C6C'
            },
            itemStyle: {
              color: '#F56C6C'
            }
          }
        ]
      }
      
      this.barChart.setOption(option)
    },
    initRadarChart() {
      if (!this.$refs.radarChart) return
      
      if (this.radarChart) this.radarChart.dispose()
      
      this.radarChart = echarts.init(this.$refs.radarChart)
      this.updateRadarChart()
    },
    updateRadarChart() {
      if (!this.radarChart) return
      
      const filteredData = this.radarData.seriesData.filter(item => 
        this.selectedAgents.includes(item.name)
      )
      
      const series = filteredData.map((item, index) => ({
        value: item.value,
        name: item.name,
        symbol: 'circle',
        symbolSize: 6,
        lineStyle: {
          width: 2,
          color: radarColors[index % radarColors.length]
        },
        itemStyle: {
          color: radarColors[index % radarColors.length]
        },
        areaStyle: {
          color: radarColors[index % radarColors.length],
          opacity: 0.15
        }
      }))
      
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          data: filteredData.map(d => d.name),
          bottom: 10,
          textStyle: {
            fontSize: 12
          }
        },
        radar: {
          indicator: this.radarData.indicator,
          center: ['50%', '45%'],
          radius: '60%',
          splitNumber: 5,
          axisName: {
            color: '#333',
            fontSize: 13
          },
          splitArea: {
            areaStyle: {
              color: ['rgba(64, 158, 255, 0.05)', 'rgba(64, 158, 255, 0.1)']
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            }
          }
        },
        series: [{
          type: 'radar',
          data: series
        }]
      }
      
      this.radarChart.setOption(option)
    },
    getRadarColor(index) {
      return radarColors[index % radarColors.length]
    },
    getRankType(rank) {
      if (rank === 1) return 'danger'
      if (rank === 2) return 'warning'
      if (rank === 3) return 'primary'
      return 'info'
    },
    getGradeType(grade) {
      const map = { 'S': 'danger', 'A': 'warning', 'B': 'primary', 'C': 'success', 'D': 'info' }
      return map[grade] || 'info'
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
    getOverallCompletionRate(item) {
      const viewingRate = parseFloat(item.viewingCompletionRate) || 0
      const dealRate = parseFloat(item.dealCompletionRate) || 0
      const amountRate = parseFloat(item.dealAmountCompletionRate) || 0
      const conversionRate = parseFloat(item.conversionCompletionRate) || 0
      return Math.round((viewingRate + dealRate + amountRate + conversionRate) / 4)
    },
    getAmountPercentage(amount) {
      const val = parseFloat(amount) || 0
      return Math.min(Math.round((val / this.maxDealAmount) * 100), 100)
    },
    getScoreRingStyle(score) {
      const s = parseFloat(score) || 0
      const percent = s
      let color = '#909399'
      if (s >= 90) color = '#F56C6C'
      else if (s >= 80) color = '#E6A23C'
      else if (s >= 60) color = '#409EFF'
      
      return {
        background: `conic-gradient(${color} ${percent * 3.6}deg, #ebeef5 0deg)`
      }
    },
    getScoreClass(score) {
      const s = parseFloat(score) || 0
      if (s >= 90) return 'score-excellent'
      if (s >= 80) return 'score-good'
      if (s >= 60) return 'score-pass'
      return 'score-fail'
    }
  }
}
</script>

<style scoped>
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

.chart-container {
  padding: 10px 0;
}

.chart {
  width: 100%;
  height: 400px;
}

.chart-radar .chart {
  height: 450px;
}

.chart-legend {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 10px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #606266;
}

.legend-color {
  display: inline-block;
  width: 16px;
  height: 16px;
  border-radius: 3px;
  margin-right: 6px;
}

.color-viewing {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.color-deal {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.color-amount {
  background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
}

.color-conversion {
  background: #F56C6C;
}

.radar-selector {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  margin-top: 10px;
}

.selector-label {
  font-weight: 500;
  color: #606266;
}

.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ranking-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  transition: all 0.3s;
}

.ranking-item:hover {
  transform: translateX(5px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.ranking-item.rank-1 {
  border-color: #f56c6c;
  background: linear-gradient(90deg, #fff1f0 0%, #fff 30%);
}

.ranking-item.rank-2 {
  border-color: #e6a23c;
  background: linear-gradient(90deg, #fdf6ec 0%, #fff 30%);
}

.ranking-item.rank-3 {
  border-color: #409eff;
  background: linear-gradient(90deg, #ecf5ff 0%, #fff 30%);
}

.rank-badge {
  width: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.medal {
  font-size: 28px;
}

.rank-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #606266;
}

.rank-info {
  flex: 1;
  padding: 0 15px;
}

.agent-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.rank-meta {
  font-size: 12px;
  color: #909399;
}

.rank-meta .divider {
  margin: 0 8px;
  color: #dcdfe6;
}

.rank-amount {
  text-align: right;
  min-width: 120px;
}

.amount-value {
  font-size: 20px;
  font-weight: 700;
  color: #67c23a;
}

.amount-unit {
  font-size: 14px;
  font-weight: 400;
  margin-left: 2px;
}

.rank-score {
  min-width: 80px;
  display: flex;
  justify-content: center;
}

.score-ring {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.score-ring::before {
  content: '';
  position: absolute;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #fff;
}

.score-text {
  position: relative;
  font-size: 16px;
  font-weight: 700;
  color: #303133;
  z-index: 1;
}

.highlight {
  font-weight: 600;
  font-size: 14px;
}

.text-success {
  color: #67C23A;
}

.text-primary {
  color: #409EFF;
}

.text-muted {
  color: #c0c4cc;
}

.score-text-inline {
  font-size: 18px;
  font-weight: 700;
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
</style>
