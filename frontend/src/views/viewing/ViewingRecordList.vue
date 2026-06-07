<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">带看记录</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增带看</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户姓名">
          <el-input v-model="searchForm.customerName" placeholder="请输入客户姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="经纪人">
          <el-input v-model="searchForm.agentName" placeholder="请输入经纪人" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="待跟进" value="待跟进"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="带看时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="100"></el-table-column>
        <el-table-column prop="customerPhone" label="客户电话" width="130"></el-table-column>
        <el-table-column prop="propertyTitle" label="带看房源" show-overflow-tooltip></el-table-column>
        <el-table-column prop="viewingTime" label="带看时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.viewingTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="viewingDuration" label="时长(分钟)" width="100"></el-table-column>
        <el-table-column prop="satisfaction" label="满意度" width="90">
          <template slot-scope="scope">
            <span :class="getSatisfactionClass(scope.row.satisfaction)">{{ scope.row.satisfaction }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="agentName" label="经纪人" width="90"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="goToEdit(scope.row.id)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row.id)" class="text-danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewingRecordList',
  data() {
    return {
      tableData: [],
      searchForm: {
        customerName: '',
        agentName: '',
        status: '',
        dateRange: []
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await this.$axios.get('/viewing-records')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.customerName && this.searchForm.customerName.trim()) params.customerName = this.searchForm.customerName.trim()
      if (this.searchForm.agentName && this.searchForm.agentName.trim()) params.agentName = this.searchForm.agentName.trim()
      if (this.searchForm.status) params.status = this.searchForm.status
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startTime = this.searchForm.dateRange[0]
        params.endTime = this.searchForm.dateRange[1]
      }
      const res = await this.$axios.get('/viewing-records/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        customerName: '',
        agentName: '',
        status: '',
        dateRange: []
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/viewing-records/add')
    },
    goToEdit(id) {
      this.$router.push(`/viewing-records/edit/${id}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该带看记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/viewing-records/${id}`)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(res.message)
        }
      }).catch(() => {})
    },
    getStatusType(status) {
      const map = {
        '已完成': 'success',
        '待跟进': 'warning',
        '已取消': 'danger'
      }
      return map[status] || 'info'
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
