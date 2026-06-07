<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">楼盘字典</div>
      <el-button type="primary" icon="el-icon-plus" @click="goToAdd">新增楼盘</el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="楼盘名称">
          <el-input v-model="searchForm.name" placeholder="请输入楼盘名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="城市">
          <el-input v-model="searchForm.city" placeholder="请输入城市" clearable></el-input>
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
        <el-table-column prop="name" label="楼盘名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="city" label="城市" width="80"></el-table-column>
        <el-table-column prop="district" label="区域" width="80"></el-table-column>
        <el-table-column prop="address" label="地址" show-overflow-tooltip></el-table-column>
        <el-table-column prop="buildingType" label="建筑类型" width="90"></el-table-column>
        <el-table-column prop="buildingYear" label="建成年代" width="90"></el-table-column>
        <el-table-column prop="averagePrice" label="参考均价" width="100">
          <template slot-scope="scope">
            <span class="text-danger font-bold">{{ scope.row.averagePrice || '暂无' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
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
  name: 'BuildingDictList',
  data() {
    return {
      tableData: [],
      searchForm: {
        name: '',
        city: '',
        district: ''
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await this.$axios.get('/building-dicts')
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    async search() {
      const params = {}
      if (this.searchForm.name && this.searchForm.name.trim()) params.name = this.searchForm.name.trim()
      if (this.searchForm.city && this.searchForm.city.trim()) params.city = this.searchForm.city.trim()
      if (this.searchForm.district && this.searchForm.district.trim()) params.district = this.searchForm.district.trim()
      const res = await this.$axios.get('/building-dicts/search', { params })
      if (res.code === 200) {
        this.tableData = res.data
      }
    },
    reset() {
      this.searchForm = {
        name: '',
        city: '',
        district: ''
      }
      this.loadData()
    },
    goToAdd() {
      this.$router.push('/building-dicts/add')
    },
    goToEdit(id) {
      this.$router.push(`/building-dicts/edit/${id}`)
    },
    goToDetail(id) {
      this.$router.push(`/building-dicts/detail/${id}`)
    },
    handleDelete(id) {
      this.$confirm('确定要删除该楼盘吗？删除后关联的房源不会被删除，但会取消关联。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.delete(`/building-dicts/${id}`)
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
