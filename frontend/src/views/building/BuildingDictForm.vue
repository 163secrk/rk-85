<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑楼盘' : '新增楼盘' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼盘名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入楼盘名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参考均价" prop="averagePrice">
              <el-input v-model="form.averagePrice" placeholder="请输入参考均价（元/㎡）"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="城市" prop="city">
              <el-input v-model="form.city" placeholder="请输入城市"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区域" prop="district">
              <el-input v-model="form.district" placeholder="请输入区域"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="详细地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入详细地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="建筑类型">
              <el-select v-model="form.buildingType" placeholder="请选择建筑类型">
                <el-option label="板楼" value="板楼"></el-option>
                <el-option label="塔楼" value="塔楼"></el-option>
                <el-option label="板塔结合" value="板塔结合"></el-option>
                <el-option label="平房" value="平房"></el-option>
                <el-option label="别墅" value="别墅"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="建成年代">
              <el-input v-model="form.buildingYear" placeholder="如：2015"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="总户数">
              <el-input v-model="form.totalHouses" placeholder="请输入总户数"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="车位数">
              <el-input v-model="form.parkingSpace" placeholder="请输入车位数"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="开发商">
              <el-input v-model="form.developer" placeholder="请输入开发商"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="物业公司">
              <el-input v-model="form.propertyCompany" placeholder="请输入物业公司"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="容积率">
              <el-input v-model="form.plotRatio" placeholder="如：2.5"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="绿化率">
              <el-input v-model="form.greeningRate" placeholder="如：30%"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="封面图片">
              <el-input v-model="form.imageUrl" placeholder="请输入图片URL"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="周边配套">
          <el-input v-model="form.supportingFacilities" placeholder="如：学校,医院,超市,公园"></el-input>
        </el-form-item>
        <el-form-item label="楼盘描述">
          <el-input type="textarea" v-model="form.description" :rows="3" placeholder="请输入楼盘描述"></el-input>
        </el-form-item>
        <el-form-item class="text-center">
          <el-button type="primary" @click="handleSubmit" :loading="submitting">保存</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BuildingDictForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      form: {
        id: null,
        name: '',
        city: '北京',
        district: '',
        address: '',
        buildingType: '板楼',
        buildingYear: '',
        developer: '',
        propertyCompany: '',
        totalHouses: '',
        plotRatio: '',
        greeningRate: '',
        parkingSpace: '',
        supportingFacilities: '',
        description: '',
        imageUrl: '',
        averagePrice: ''
      },
      rules: {
        name: [{ required: true, message: '请输入楼盘名称', trigger: 'blur' }],
        city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
        district: [{ required: true, message: '请输入区域', trigger: 'blur' }],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    const id = this.$route.params.id
    if (id) {
      this.isEdit = true
      this.loadDetail(id)
    }
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/building-dicts/${id}`)
      if (res.code === 200) {
        this.form = { ...this.form, ...res.data }
      }
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const res = this.isEdit
              ? await this.$axios.put('/building-dicts', this.form)
              : await this.$axios.post('/building-dicts', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/building-dicts')
            } else {
              this.$message.error(res.message)
            }
          } finally {
            this.submitting = false
          }
        }
      })
    },
    goBack() {
      this.$router.push('/building-dicts')
    }
  }
}
</script>
