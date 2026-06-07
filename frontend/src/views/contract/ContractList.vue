<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">交易合同管理</div>
      <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">从认购记录生成合同</el-button>
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
            <el-option label="草稿" value="草稿"></el-option>
            <el-option label="已签署" value="已签署"></el-option>
            <el-option label="已备案" value="已备案"></el-option>
            <el-option label="已完结" value="已完结"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建日期">
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
        <el-table-column prop="contractNo" label="合同编号" width="180"></el-table-column>
        <el-table-column prop="customerName" label="买方姓名" width="100"></el-table-column>
        <el-table-column prop="sellerName" label="卖方姓名" width="100"></el-table-column>
        <el-table-column prop="propertyTitle" label="房源名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="totalPrice" label="成交价(万)" width="110">
          <template slot-scope="scope">
            <span class="text-danger font-bold">{{ scope.row.totalPrice }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="付款方式" width="100"></el-table-column>
        <el-table-column prop="deliveryDate" label="交房日期" width="160">
          <template slot-scope="scope">
            {{ scope.row.deliveryDate | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.createTime | formatDate }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="goToDetail(scope.row.id)">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-edit" @click="goToEdit(scope.row.id)" :disabled="scope.row.status !== '草稿'">编辑</el-button>
            <el-dropdown size="mini" @command="(status) => handleStatusChange(scope.row.id, status)" :disabled="scope.row.status === '已完结'">
              <el-button type="text" icon="el-icon-setting">状态流转</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="scope.row.status === '草稿'" command="已签署" icon="el-icon-edit-outline">签署合同</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.status === '已签署'" command="已备案" icon="el-icon-upload2">合同备案</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.status === '已备案'" command="已完结" icon="el-icon-circle-check">完结合同</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row.id)" class="text-danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="选择认购记录生成合同" :visible.sync="createDialogVisible" width="800px">
      <el-table :data="subscriptionList" border stripe height="400">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="subscriptionNo" label="认购编号" width="150"></el-table-column>
        <el-table-column prop="customerName" label="客户姓名" width="100"></el-table-column>
        <el-table-column prop="propertyTitle" label="房源名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="totalPrice" label="总价(万)" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="createFromSubscription(scope.row.id)">生成合同</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'ContractList',
  data() {
    return {
      tableData: [],
      customerList: [],
      subscriptionList: [],
      createDialogVisible: false,
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
      const res = await this.$axios.get('/contracts')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async loadSubscriptions() {
      const res = await this.$axios.get('/subscriptions')
      if (res.code === 200) {
        this.subscriptionList = res.data.filter(s => s.status === '已签约')
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.customerId) params.customerId = this.searchForm.customerId
      if (this.searchForm.status) params.status = this.searchForm.status
      if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
        params.startDate = this.searchForm.dateRange[0]
        params.endDate = this.searchForm.dateRange[1]
      }
      const res = await this.$axios.get('/contracts/search', { params })
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
    showCreateDialog() {
      this.loadSubscriptions()
      this.createDialogVisible = true
    },
    async createFromSubscription(subscriptionId) {
      const res = await this.$axios.post(`/contracts/create-from-subscription/${subscriptionId}`)
      if (res.code === 200) {
        this.$message.success('合同创建成功')
        this.createDialogVisible = false
        this.loadData()
        this.$router.push(`/contracts/detail/${res.data.id}`)
      } else {
        this.$message.error(res.message)
      }
    },
    goToEdit(id) {
      this.$router.push(`/contracts/edit/${id}`)
    },
    goToDetail(id) {
      this.$router.push(`/contracts/detail/${id}`)
    },
    async handleStatusChange(id, status) {
      this.$confirm(`确定要将合同状态更新为"${status}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.put(`/contracts/${id}/status?status=${status}`)
        if (res.code === 200) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(res.message)
        }
      }).catch(() => {})
    },
    handleDelete(id) {
      this.$confirm('确定要删除该合同吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/contracts/${id}`)
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
        '草稿': 'info',
        '已签署': 'primary',
        '已备案': 'warning',
        '已完结': 'success'
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
