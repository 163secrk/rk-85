<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">认购签约管理</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增签约</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="客户姓名">
          <el-select v-model="searchForm.customerId" placeholder="请选择客户" filterable clearable>
            <el-option v-for="customer in customerList" :key="customer.id" :label="customer.name" :value="customer.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待签约" value="待签约"></el-option>
            <el-option label="已签约" value="已签约"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="签约日期">
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
        <el-table-column prop="subscriptionNo" label="合同编号" width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="100"></el-table-column>
        <el-table-column prop="propertyTitle" label="房源名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="totalPrice" label="总价(万)" width="100">
          <template slot-scope="scope">
            <span class="text-danger font-bold">{{ scope.row.totalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="depositAmount" label="定金(万)" width="90"></el-table-column>
        <el-table-column prop="paymentMethod" label="付款方式" width="100"></el-table-column>
        <el-table-column prop="signDate" label="签约日期" width="160">
          <template slot-scope="scope">
            {{ scope.row.signDate | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="agentName" label="经办人" width="90"></el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="goToDetail(scope.row.id)">详情</el-button>
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
  name: 'SubscriptionList',
  data() {
    return {
      tableData: [],
      customerList: [],
      searchForm: {
        customerId: null,
        status: '',
        dateRange: []
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
      const res = await this.$axios.get('/subscriptions')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {
        status: this.searchForm.status
      }
      if (this.searchForm.customerId) {
        params.customerId = this.searchForm.customerId
      }
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      const res = await this.$axios.get('/subscriptions/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        customerId: null,
        status: '',
        dateRange: []
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/subscriptions/add')
    },
    goToEdit(id) {
      this.$router.push(`/subscriptions/edit/${id}`)
    },
    goToDetail(id) {
      this.$router.push(`/subscriptions/detail/${id}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该签约记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/subscriptions/${id}`)
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
        '已签约': 'success',
        '待签约': 'warning',
        '已取消': 'danger',
        '已完成': 'success'
      }
      return map[status] || 'info'
    }
  }
}
</script>

<style scoped>
.font-bold {
  font-weight: bold;
}
</style>
