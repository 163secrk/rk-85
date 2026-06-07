<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">VR全景管理</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增全景</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="所属房源">
          <el-select v-model="searchForm.propertyId" placeholder="请选择房源" filterable clearable>
            <el-option v-for="property in propertyList" :key="property.id" :label="property.title" :value="property.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="全景名称">
          <el-input v-model="searchForm.name" placeholder="请输入全景名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="启用"></el-option>
            <el-option label="禁用" value="禁用"></el-option>
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
        <el-table-column label="缩略图" width="100">
          <template slot-scope="scope">
            <img :src="scope.row.thumbnailUrl" width="80" height="60" style="object-fit: cover; border-radius: 4px;">
          </template>
        </el-table-column>
        <el-table-column prop="propertyTitle" label="所属房源" width="150" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="全景名称" width="120"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80">
          <template slot-scope="scope">
            <span class="text-primary"><i class="el-icon-view"></i> {{ scope.row.viewCount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="70"></el-table-column>
        <el-table-column prop="status" label="状态" width="70">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '启用' ? 'success' : 'danger'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-video-play" @click="playVR(scope.row.id)">播放</el-button>
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
  name: 'VRPanoramaList',
  data() {
    return {
      tableData: [],
      propertyList: [],
      searchForm: {
        propertyId: null,
        name: '',
        status: ''
      }
    }
  },
  mounted() {
    this.loadProperties()
    this.loadData()
    const propertyId = this.$route.query.propertyId
    if (propertyId) {
      this.searchForm.propertyId = Number(propertyId)
      this.search()
    }
  },
  methods: {
    async loadProperties() {
      const res = await this.$axios.get('/properties')
      if (res.code === 200) {
        this.propertyList = res.data
      }
    },
    async loadData() {
      const res = await this.$axios.get('/vr-panoramas')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.propertyId) params.propertyId = this.searchForm.propertyId
      if (this.searchForm.name && this.searchForm.name.trim()) params.name = this.searchForm.name.trim()
      if (this.searchForm.status) params.status = this.searchForm.status
      const res = await this.$axios.get('/vr-panoramas/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        propertyId: null,
        name: '',
        status: ''
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/vr-panoramas/add')
    },
    goToEdit(id) {
      this.$router.push(`/vr-panoramas/edit/${id}`)
    },
    playVR(id) {
      this.$router.push(`/vr-panoramas/view/${id}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该VR全景吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/vr-panoramas/${id}`)
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
