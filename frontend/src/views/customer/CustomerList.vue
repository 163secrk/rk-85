<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">客户管理</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增客户</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="姓名">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="searchForm.phone" placeholder="请输入电话" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="跟进中" value="跟进中"></el-option>
            <el-option label="意向客户" value="意向客户"></el-option>
            <el-option label="已成交" value="已成交"></el-option>
            <el-option label="已流失" value="已流失"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select v-model="searchForm.level" placeholder="请选择等级" clearable>
            <el-option label="A类" value="A类"></el-option>
            <el-option label="B类" value="B类"></el-option>
            <el-option label="C类" value="C类"></el-option>
            <el-option label="D类" value="D类"></el-option>
          </el-select>
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
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="phone" label="电话" width="130"></el-table-column>
        <el-table-column prop="gender" label="性别" width="60"></el-table-column>
        <el-table-column prop="age" label="年龄" width="60"></el-table-column>
        <el-table-column prop="budget" label="预算" width="100"></el-table-column>
        <el-table-column prop="preferredArea" label="意向区域" width="100"></el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="等级" width="70">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.level)" size="mini">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ scope.row.createTime | formatDate }}
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
  name: 'CustomerList',
  data() {
    return {
      tableData: [],
      searchForm: {
        name: '',
        phone: '',
        status: '',
        level: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await this.$axios.get('/customers')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.name && this.searchForm.name.trim()) params.name = this.searchForm.name.trim()
      if (this.searchForm.phone && this.searchForm.phone.trim()) params.phone = this.searchForm.phone.trim()
      if (this.searchForm.status) params.status = this.searchForm.status
      if (this.searchForm.level) params.level = this.searchForm.level
      const res = await this.$axios.get('/customers/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        name: '',
        phone: '',
        status: '',
        level: ''
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/customers/add')
    },
    goToEdit(id) {
      this.$router.push(`/customers/edit/${id}`)
    },
    goToDetail(id) {
      this.$router.push(`/customers/detail/${id}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该客户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/customers/${id}`)
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
        '跟进中': 'primary',
        '意向客户': 'success',
        '已成交': 'success',
        '已流失': 'danger'
      }
      return map[status] || 'info'
    },
    getLevelType(level) {
      const map = {
        'A类': 'danger',
        'B类': 'warning',
        'C类': 'primary',
        'D类': 'info'
      }
      return map[level] || 'info'
    }
  }
}
</script>
