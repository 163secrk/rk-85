<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">跟进记录</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增跟进</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户姓名">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户" filterable clearable>
            <el-option v-for="customer in customerList" :key="customer.id" :label="customer.name" :value="customer.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="跟进方式">
          <el-select v-model="searchForm.followType" placeholder="请选择方式" clearable>
            <el-option label="电话跟进" value="电话跟进"></el-option>
            <el-option label="微信跟进" value="微信跟进"></el-option>
            <el-option label="上门拜访" value="上门拜访"></el-option>
            <el-option label="短信提醒" value="短信提醒"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="经纪人">
          <el-input v-model="searchForm.agentName" placeholder="请输入经纪人" clearable></el-input>
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
        <el-table-column prop="followType" label="跟进方式" width="100">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.followType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="followTime" label="跟进时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.followTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="followContent" label="跟进内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="followResult" label="结果" width="100"></el-table-column>
        <el-table-column prop="nextFollowTime" label="下次跟进" width="160">
          <template slot-scope="scope">
            {{ scope.row.nextFollowTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="agentName" label="跟进人" width="90"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row.id)" class="text-danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FollowUpRecordList',
  data() {
    return {
      tableData: [],
      customerList: [],
      searchForm: {
        customerId: null,
        agentName: '',
        followType: ''
      }
    }
  },
  mounted() {
    this.loadCustomers()
    this.loadData()
  },
  methods: {
    async loadCustomers() {
      const res = await this.$axios.get('/customers')
      if (res.code === 200) {
        this.customerList = res.data
      }
    },
    async loadData() {
      const res = await this.$axios.get('/follow-up-records')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const res = await this.$axios.get('/follow-up-records/search', { params: this.searchForm })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        customerId: null,
        agentName: '',
        followType: ''
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/follow-up-records/add')
    },
    handleDelete(id) {
      this.$confirm('确定要删除该跟进记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/follow-up-records/${id}`)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(res.message)
        }
      }).catch(() => {})
    }
  }
}
</script>
