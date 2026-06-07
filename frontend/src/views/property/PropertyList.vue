<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">房源管理</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增房源</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="房源名称">
          <el-input v-model="searchForm.title" placeholder="请输入房源名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="在售" value="在售"></el-option>
            <el-option label="已售" value="已售"></el-option>
            <el-option label="已租" value="已租"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="住宅" value="住宅"></el-option>
            <el-option label="公寓" value="公寓"></el-option>
            <el-option label="别墅" value="别墅"></el-option>
            <el-option label="商铺" value="商铺"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="区域">
          <el-input v-model="searchForm.district" placeholder="请输入区域" clearable></el-input>
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
        <el-table-column label="图片" width="100">
          <template slot-scope="scope">
            <img :src="scope.row.imageUrl" width="80" height="60" style="object-fit: cover; border-radius: 4px;">
          </template>
        </el-table-column>
        <el-table-column prop="propertyNo" label="房源编号" width="110"></el-table-column>
        <el-table-column prop="title" label="房源名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="type" label="类型" width="70"></el-table-column>
        <el-table-column prop="area" label="面积" width="80"></el-table-column>
        <el-table-column prop="rooms" label="户型" width="80">
          <template slot-scope="scope">
            {{ scope.row.rooms }}室{{ scope.row.halls }}厅
          </template>
        </el-table-column>
        <el-table-column prop="price" label="总价(万)" width="90">
          <template slot-scope="scope">
            <span class="text-danger font-bold">{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="district" label="区域" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="70">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '在售' ? 'success' : 'danger'" size="mini">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-view" @click="goToDetail(scope.row.id)">详情</el-button>
            <el-button size="mini" type="text" icon="el-icon-picture-outline" @click="viewVR(scope.row.id)">VR</el-button>
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
  name: 'PropertyList',
  data() {
    return {
      tableData: [],
      searchForm: {
        title: '',
        status: '',
        type: '',
        district: '',
        minPrice: null,
        maxPrice: null
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await this.$axios.get('/properties')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.title && this.searchForm.title.trim()) params.title = this.searchForm.title.trim()
      if (this.searchForm.status) params.status = this.searchForm.status
      if (this.searchForm.type) params.type = this.searchForm.type
      if (this.searchForm.district && this.searchForm.district.trim()) params.district = this.searchForm.district.trim()
      if (this.searchForm.minPrice !== null) params.minPrice = this.searchForm.minPrice
      if (this.searchForm.maxPrice !== null) params.maxPrice = this.searchForm.maxPrice
      const res = await this.$axios.get('/properties/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        title: '',
        status: '',
        type: '',
        district: '',
        minPrice: null,
        maxPrice: null
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/properties/add')
    },
    goToEdit(id) {
      this.$router.push(`/properties/edit/${id}`)
    },
    goToDetail(id) {
      this.$router.push(`/properties/detail/${id}`)
    },
    viewVR(propertyId) {
      this.$router.push(`/vr-panoramas?propertyId=${propertyId}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该房源吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/properties/${id}`)
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

<style scoped>
.font-bold {
  font-weight: bold;
}
</style>
