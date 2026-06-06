<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑客户' : '新增客户' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number v-model="form.age" :min="1" :max="120"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户来源" prop="source">
              <el-select v-model="form.source" placeholder="请选择来源">
                <el-option label="线上咨询" value="线上咨询"></el-option>
                <el-option label="朋友介绍" value="朋友介绍"></el-option>
                <el-option label="门店来访" value="门店来访"></el-option>
                <el-option label="电话咨询" value="电话咨询"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="购房预算" prop="budget">
              <el-input v-model="form.budget" placeholder="例如：300万"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="意向区域" prop="preferredArea">
              <el-input v-model="form.preferredArea" placeholder="请输入意向区域"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="意向户型" prop="preferredType">
              <el-input v-model="form.preferredType" placeholder="例如：三居"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户等级" prop="level">
              <el-select v-model="form.level" placeholder="请选择等级">
                <el-option label="A类" value="A类"></el-option>
                <el-option label="B类" value="B类"></el-option>
                <el-option label="C类" value="C类"></el-option>
                <el-option label="D类" value="D类"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="客户状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="跟进中" value="跟进中"></el-option>
            <el-option label="意向客户" value="意向客户"></el-option>
            <el-option label="已成交" value="已成交"></el-option>
            <el-option label="已流失" value="已流失"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.remark" :rows="3" placeholder="请输入备注"></el-input>
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
  name: 'CustomerForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      form: {
        id: null,
        name: '',
        phone: '',
        gender: '男',
        age: 30,
        idCard: '',
        address: '',
        budget: '',
        preferredArea: '',
        preferredType: '',
        status: '跟进中',
        source: '线上咨询',
        level: 'B类',
        remark: ''
      },
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'blur' },
          { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }
        ],
        address: [{ required: true, message: '请输入地址', trigger: 'blur' }],
        budget: [{ required: true, message: '请输入购房预算', trigger: 'blur' }],
        preferredArea: [{ required: true, message: '请输入意向区域', trigger: 'blur' }],
        preferredType: [{ required: true, message: '请输入意向户型', trigger: 'blur' }],
        source: [{ required: true, message: '请选择客户来源', trigger: 'change' }],
        level: [{ required: true, message: '请选择客户等级', trigger: 'change' }],
        status: [{ required: true, message: '请选择客户状态', trigger: 'change' }]
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
      const res = await this.$axios.get(`/customers/${id}`)
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
              ? await this.$axios.put('/customers', this.form)
              : await this.$axios.post('/customers', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/customers')
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
      this.$router.push('/customers')
    }
  }
}
</script>
