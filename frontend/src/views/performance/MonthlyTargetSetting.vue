<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">月度目标设置</div>
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
        <el-button type="success" icon="el-icon-plus" @click="showBatchDialog" style="margin-left: 10px;">批量设置</el-button>
      </div>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="agentName" label="经纪人" width="100"></el-table-column>
        <el-table-column prop="position" label="职位" width="110"></el-table-column>
        <el-table-column prop="department" label="部门" width="110"></el-table-column>
        <el-table-column label="带看量目标" width="180">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.viewingTarget"
              :min="0"
              size="small"
              @change="handleTargetChange(scope.row)">
            </el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="成交量目标" width="180">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.dealTarget"
              :min="0"
              size="small"
              @change="handleTargetChange(scope.row)">
            </el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="成交总额目标(万)" width="200">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.dealAmountTarget"
              :min="0"
              :precision="2"
              size="small"
              @change="handleTargetChange(scope.row)">
            </el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="转化率目标(%)" width="200">
          <template slot-scope="scope">
            <el-input-number
              v-model="scope.row.conversionRateTarget"
              :min="0"
              :max="100"
              :precision="2"
              size="small"
              @change="handleTargetChange(scope.row)">
            </el-input-number>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-check"
              :disabled="!scope.row._dirty"
              @click="saveTarget(scope.row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      title="批量设置月度目标"
      :visible.sync="batchDialogVisible"
      width="600px">
      <el-form :model="batchForm" label-width="120px">
        <el-form-item label="适用月份">
          <el-date-picker
            v-model="batchForm.month"
            type="month"
            placeholder="选择月份"
            value-format="YYYY-MM"
            style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="选择经纪人">
          <el-select
            v-model="batchForm.agentIds"
            multiple
            placeholder="请选择经纪人"
            style="width: 100%;">
            <el-option
              v-for="agent in agents"
              :key="agent.id"
              :label="agent.name"
              :value="agent.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="带看量目标">
          <el-input-number
            v-model="batchForm.viewingTarget"
            :min="0"
            style="width: 100%;">
          </el-input-number>
        </el-form-item>
        <el-form-item label="成交量目标">
          <el-input-number
            v-model="batchForm.dealTarget"
            :min="0"
            style="width: 100%;">
          </el-input-number>
        </el-form-item>
        <el-form-item label="成交总额目标(万)">
          <el-input-number
            v-model="batchForm.dealAmountTarget"
            :min="0"
            :precision="2"
            style="width: 100%;">
          </el-input-number>
        </el-form-item>
        <el-form-item label="转化率目标(%)">
          <el-input-number
            v-model="batchForm.conversionRateTarget"
            :min="0"
            :max="100"
            :precision="2"
            style="width: 100%;">
          </el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="batchSave">确认保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MonthlyTargetSetting',
  data() {
    return {
      selectedMonth: this.getCurrentMonth(),
      tableData: [],
      agents: [],
      batchDialogVisible: false,
      batchForm: {
        month: this.getCurrentMonth(),
        agentIds: [],
        viewingTarget: 20,
        dealTarget: 4,
        dealAmountTarget: 400,
        conversionRateTarget: 20
      }
    }
  },
  mounted() {
    this.loadAgents()
    this.loadData()
  },
  methods: {
    getCurrentMonth() {
      const now = new Date()
      return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`
    },
    async loadAgents() {
      const res = await this.$axios.get('/agents')
      if (res.code === 200) {
        this.agents = res.data
      }
    },
    async loadData() {
      const res = await this.$axios.get(`/monthly-targets/month/${this.selectedMonth}`)
      if (res.code === 200) {
        const existingTargets = res.data || []
        const targetMap = {}
        existingTargets.forEach(t => {
          targetMap[t.agentId] = t
        })
        
        const agentRes = await this.$axios.get('/agents')
        if (agentRes.code === 200) {
          this.tableData = agentRes.data.map(agent => {
            const existing = targetMap[agent.id]
            return {
              id: existing ? existing.id : null,
              agentId: agent.id,
              agentName: agent.name,
              position: agent.position,
              department: agent.department,
              viewingTarget: existing ? existing.viewingTarget : 0,
              dealTarget: existing ? existing.dealTarget : 0,
              dealAmountTarget: existing ? existing.dealAmountTarget : 0,
              conversionRateTarget: existing ? existing.conversionRateTarget : 0,
              _dirty: false
            }
          })
        }
      }
    },
    handleTargetChange(row) {
      row._dirty = true
    },
    async saveTarget(row) {
      const data = {
        id: row.id,
        agentId: row.agentId,
        agentName: row.agentName,
        month: this.selectedMonth,
        viewingTarget: row.viewingTarget,
        dealTarget: row.dealTarget,
        dealAmountTarget: row.dealAmountTarget,
        conversionRateTarget: row.conversionRateTarget
      }
      
      const res = row.id
        ? await this.$axios.put('/monthly-targets', data)
        : await this.$axios.post('/monthly-targets', data)
      
      if (res.code === 200) {
        this.$message.success('保存成功')
        row.id = res.data.id
        row._dirty = false
      } else {
        this.$message.error(res.message)
      }
    },
    showBatchDialog() {
      this.batchForm.month = this.selectedMonth
      this.batchForm.agentIds = []
      this.batchDialogVisible = true
    },
    async batchSave() {
      if (!this.batchForm.month) {
        this.$message.warning('请选择月份')
        return
      }
      if (this.batchForm.agentIds.length === 0) {
        this.$message.warning('请选择经纪人')
        return
      }
      
      const targets = this.batchForm.agentIds.map(agentId => {
        const agent = this.agents.find(a => a.id === agentId)
        return {
          agentId,
          agentName: agent ? agent.name : '',
          month: this.batchForm.month,
          viewingTarget: this.batchForm.viewingTarget,
          dealTarget: this.batchForm.dealTarget,
          dealAmountTarget: this.batchForm.dealAmountTarget,
          conversionRateTarget: this.batchForm.conversionRateTarget
        }
      })
      
      const res = await this.$axios.post('/monthly-targets/batch', targets)
      if (res.code === 200) {
        this.$message.success('批量设置成功')
        this.batchDialogVisible = false
        this.loadData()
      } else {
        this.$message.error(res.message)
      }
    }
  }
}
</script>

<style scoped>
</style>
