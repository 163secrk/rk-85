<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑VR全景' : '新增VR全景' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属房源" prop="propertyId">
              <el-select v-model="form.propertyId" placeholder="请选择房源" filterable @change="onPropertyChange">
                <el-option v-for="property in propertyList" :key="property.id" :label="property.title" :value="property.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="全景名称" prop="name">
              <el-input v-model="form.name" placeholder="如：客厅全景"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="form.description" :rows="2" placeholder="请输入全景描述"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="缩略图URL" prop="thumbnailUrl">
              <el-input v-model="form.thumbnailUrl" placeholder="请输入缩略图URL"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="全景图URL" prop="panoramaUrl">
              <el-input v-model="form.panoramaUrl" placeholder="请输入全景图URL"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序">
              <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="启用" value="启用"></el-option>
                <el-option label="禁用" value="禁用"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
  name: 'VRPanoramaForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      propertyList: [],
      form: {
        id: null,
        propertyId: null,
        propertyTitle: '',
        name: '',
        description: '',
        thumbnailUrl: '',
        panoramaUrl: '',
        panoramaList: '',
        initialView: '',
        hotspots: '',
        sortOrder: 0,
        status: '启用'
      },
      rules: {
        propertyId: [{ required: true, message: '请选择房源', trigger: 'change' }],
        name: [{ required: true, message: '请输入全景名称', trigger: 'blur' }],
        thumbnailUrl: [{ required: true, message: '请输入缩略图URL', trigger: 'blur' }],
        panoramaUrl: [{ required: true, message: '请输入全景图URL', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadProperties()
    const id = this.$route.params.id
    if (id) {
      this.isEdit = true
      this.loadDetail(id)
    }
  },
  methods: {
    async loadProperties() {
      const res = await this.$axios.get('/properties')
      if (res.code === 200) {
        this.propertyList = res.data
      }
    },
    onPropertyChange(propertyId) {
      const property = this.propertyList.find(p => p.id === propertyId)
      if (property) {
        this.form.propertyTitle = property.title
      }
    },
    async loadDetail(id) {
      const res = await this.$axios.get(`/vr-panoramas/${id}`)
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
              ? await this.$axios.put('/vr-panoramas', this.form)
              : await this.$axios.post('/vr-panoramas', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/vr-panoramas')
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
      this.$router.push('/vr-panoramas')
    }
  }
}
</script>
