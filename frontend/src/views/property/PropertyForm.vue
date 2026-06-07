<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑房源' : '新增房源' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="所属楼盘" prop="buildingDictId">
              <el-select
                v-model="form.buildingDictId"
                placeholder="请选择所属楼盘（可选）"
                filterable
                remote
                clearable
                :remote-method="remoteSearchBuilding"
                :loading="buildingLoading"
                style="width: 100%"
                @change="onBuildingChange">
                <el-option
                  v-for="item in buildingOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                  <span style="float: left;">{{ item.name }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px;">{{ item.district }} - {{ item.address }}</span>
                </el-option>
              </el-select>
              <div v-if="form.buildingName" style="margin-top: 5px; color: #409EFF; font-size: 12px;">
                <i class="el-icon-info"></i> 已选择楼盘：{{ form.buildingName }}，城市、区域、地址已自动填充
              </div>
            </el-form-item>
          </el-col>
        </el-row>
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
            <el-form-item label="房源类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option label="住宅" value="住宅"></el-option>
                <el-option label="公寓" value="公寓"></el-option>
                <el-option label="别墅" value="别墅"></el-option>
                <el-option label="商铺" value="商铺"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="面积(㎡)" prop="area">
              <el-input v-model="form.area" placeholder="请输入面积"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="朝向" prop="orientation">
              <el-input v-model="form.orientation" placeholder="如：南北通透"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="室" prop="rooms">
              <el-input-number v-model="form.rooms" :min="1" :max="10"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="厅" prop="halls">
              <el-input-number v-model="form.halls" :min="0" :max="5"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="卫" prop="bathrooms">
              <el-input-number v-model="form.bathrooms" :min="0" :max="5"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="楼层" prop="floor">
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
            <el-form-item label="单价(元/㎡)" prop="unitPrice">
              <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="装修" prop="decoration">
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
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="在售" value="在售"></el-option>
                <el-option label="已售" value="已售"></el-option>
                <el-option label="已租" value="已租"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细地址" prop="address">
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
      buildingLoading: false,
      buildingOptions: [],
      selectedBuilding: null,
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
        imageUrl: '',
        buildingDictId: null,
        buildingName: ''
      },
      rules: {
        title: [{ required: true, message: '请输入房源名称', trigger: 'blur' }],
        propertyNo: [{ required: true, message: '请输入房源编号', trigger: 'blur' }],
        type: [{ required: true, message: '请选择房源类型', trigger: 'change' }],
        area: [{ required: true, message: '请输入面积', trigger: 'blur' }],
        orientation: [{ required: true, message: '请输入朝向', trigger: 'blur' }],
        rooms: [{ required: true, message: '请输入室数', trigger: 'blur' }],
        halls: [{ required: true, message: '请输入厅数', trigger: 'blur' }],
        bathrooms: [{ required: true, message: '请输入卫数', trigger: 'blur' }],
        floor: [{ required: true, message: '请输入楼层', trigger: 'blur' }],
        price: [{ required: true, message: '请输入总价', trigger: 'blur' }],
        unitPrice: [{ required: true, message: '请输入单价', trigger: 'blur' }],
        decoration: [{ required: true, message: '请选择装修', trigger: 'change' }],
        city: [{ required: true, message: '请输入城市', trigger: 'blur' }],
        district: [{ required: true, message: '请输入区域', trigger: 'blur' }],
        address: [{ required: true, message: '请输入详细地址', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
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
    async remoteSearchBuilding(query) {
      if (query && query.trim()) {
        this.buildingLoading = true
        try {
          const res = await this.$axios.get('/building-dicts/search', { params: { name: query } })
          if (res.code === 200) {
            this.buildingOptions = res.data
          }
        } finally {
          this.buildingLoading = false
        }
      } else {
        this.buildingOptions = []
      }
    },
    async onBuildingChange(buildingId) {
      if (buildingId) {
        const building = this.buildingOptions.find(b => b.id === buildingId)
        if (building) {
          this.selectedBuilding = building
          this.form.city = building.city
          this.form.district = building.district
          this.form.address = building.address
          this.form.buildingName = building.name
          if (building.buildingType) {
            this.form.buildingType = building.buildingType
          }
          if (building.buildingYear) {
            this.form.buildingYear = building.buildingYear
          }
          if (building.supportingFacilities) {
            this.form.supportingFacilities = building.supportingFacilities
          }
          this.$message.success('已自动填充楼盘信息')
        }
      } else {
        this.selectedBuilding = null
        this.form.buildingName = ''
      }
    },
    async loadDetail(id) {
      const res = await this.$axios.get(`/properties/${id}`)
      if (res.code === 200) {
        this.form = { ...this.form, ...res.data }
        if (this.form.buildingDictId) {
          const buildingRes = await this.$axios.get(`/building-dicts/${this.form.buildingDictId}`)
          if (buildingRes.code === 200) {
            this.buildingOptions = [buildingRes.data]
            this.selectedBuilding = buildingRes.data
          }
        }
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
