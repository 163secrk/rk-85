<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑房源' : '新增房源' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房源编号" prop="propertyNo">
              <el-input v-model="form.propertyNo" placeholder="请输入房源编号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入房源名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="房源类型">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option label="住宅" value="住宅"></el-option>
                <el-option label="公寓" value="公寓"></el-option>
                <el-option label="别墅" value="别墅"></el-option>
                <el-option label="商铺" value="商铺"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="面积(㎡)">
              <el-input v-model="form.area" placeholder="请输入面积"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="朝向">
              <el-input v-model="form.orientation" placeholder="如：南北通透"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="室">
              <el-input-number v-model="form.rooms" :min="1" :max="10"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="厅">
              <el-input-number v-model="form.halls" :min="0" :max="5"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="卫">
              <el-input-number v-model="form.bathrooms" :min="0" :max="5"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="楼层">
              <el-input v-model="form.floor" placeholder="如：5/28"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="总价(万)" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价(元/㎡)">
              <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="装修">
              <el-select v-model="form.decoration" placeholder="请选择装修">
                <el-option label="毛坯" value="毛坯"></el-option>
                <el-option label="简装" value="简装"></el-option>
                <el-option label="精装修" value="精装修"></el-option>
                <el-option label="豪华装修" value="豪华装修"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="城市">
              <el-input v-model="form.city" placeholder="请输入城市"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="区域">
              <el-input v-model="form.district" placeholder="请输入区域"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="在售" value="在售"></el-option>
                <el-option label="已售" value="已售"></el-option>
                <el-option label="已租" value="已租"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址">
          <el-input v-model="form.address" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item label="房源描述">
          <el-input type="textarea" v-model="form.description" :rows="3" placeholder="请输入房源描述"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房源特色">
              <el-input v-model="form.features" placeholder="如：近地铁,学区房"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="配套设施">
              <el-input v-model="form.supportingFacilities" placeholder="如：学校,医院,超市"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片">
          <el-input v-model="form.imageUrl" placeholder="请输入图片URL"></el-input>
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
  name: 'PropertyForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      form: {
        id: null,
        title: '',
        propertyNo: '',
        type: '住宅',
        area: '',
        address: '',
        city: '北京',
        district: '',
        price: null,
        unitPrice: null,
        rooms: 2,
        halls: 1,
        bathrooms: 1,
        floor: '',
        totalFloor: '',
        orientation: '',
        decoration: '精装修',
        buildingType: '',
        buildingYear: '',
        description: '',
        features: '',
        supportingFacilities: '',
        status: '在售',
        imageUrl: ''
      },
      rules: {
        title: [{ required: true, message: '请输入房源名称', trigger: 'blur' }],
        propertyNo: [{ required: true, message: '请输入房源编号', trigger: 'blur' }],
        price: [{ required: true, message: '请输入总价', trigger: 'blur' }]
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
      const res = await this.$axios.get(`/properties/${id}`)
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
              ? await this.$axios.put('/properties', this.form)
              : await this.$axios.post('/properties', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/properties')
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
      this.$router.push('/properties')
    }
  }
}
</script>
