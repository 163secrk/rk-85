<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑带看记录' : '新增带看记录' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择客户" prop="customerId">
              <el-select v-model="form.customerId" placeholder="请选择客户" filterable @change="onCustomerChange">
                <el-option v-for="customer in customerList" :key="customer.id" :label="customer.name + ' - ' + customer.phone" :value="customer.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选择房源" prop="propertyId">
              <el-select v-model="form.propertyId" placeholder="请选择房源" filterable @change="onPropertyChange">
                <el-option v-for="property in propertyList" :key="property.id" :label="property.title" :value="property.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="带看时间" prop="viewingTime">
              <el-date-picker
                v-model="form.viewingTime"
                type="datetime"
                placeholder="请选择带看时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="带看时长(分钟)">
              <el-input-number v-model="form.viewingDuration" :min="1" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="满意度">
              <el-select v-model="form.satisfaction" placeholder="请选择满意度">
                <el-option label="非常满意" value="非常满意"></el-option>
                <el-option label="满意" value="满意"></el-option>
                <el-option label="一般" value="一般"></el-option>
                <el-option label="不满意" value="不满意"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经纪人">
              <el-input v-model="form.agentName" placeholder="请输入经纪人姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="待跟进" value="待跟进"></el-option>
            <el-option label="已完成" value="已完成"></el-option>
            <el-option label="已取消" value="已取消"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客户反馈">
          <el-input type="textarea" v-model="form.customerFeedback" :rows="3" placeholder="请输入客户反馈"></el-input>
        </el-form-item>
        <el-form-item label="跟进计划">
          <el-input type="textarea" v-model="form.followUpPlan" :rows="2" placeholder="请输入后续跟进计划"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.remark" :rows="2" placeholder="请输入备注"></el-input>
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
  name: 'ViewingRecordForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      customerList: [],
      propertyList: [],
      form: {
        id: null,
        customerId: null,
        customerName: '',
        customerPhone: '',
        propertyId: null,
        propertyTitle: '',
        propertyAddress: '',
        viewingTime: '',
        viewingDuration: 60,
        satisfaction: '',
        agentName: '',
        customerFeedback: '',
        followUpPlan: '',
        status: '待跟进',
        remark: ''
      },
      rules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        propertyId: [{ required: true, message: '请选择房源', trigger: 'change' }],
        viewingTime: [{ required: true, message: '请选择带看时间', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.loadCustomers()
    this.loadProperties()
    const id = this.$route.params.id
    if (id) {
      this.isEdit = true
      this.loadDetail(id)
    }
  },
  methods: {
    async loadCustomers() {
      const res = await this.$axios.get('/customers')
      if (res.code === 200) {
        this.customerList = res.data
      }
    },
    async loadProperties() {
      const res = await this.$axios.get('/properties')
      if (res.code === 200) {
        this.propertyList = res.data
      }
    },
    onCustomerChange(customerId) {
      const customer = this.customerList.find(c => c.id === customerId)
      if (customer) {
        this.form.customerName = customer.name
        this.form.customerPhone = customer.phone
      }
    },
    onPropertyChange(propertyId) {
      const property = this.propertyList.find(p => p.id === propertyId)
      if (property) {
        this.form.propertyTitle = property.title
        this.form.propertyAddress = property.address
      }
    },
    async loadDetail(id) {
      const res = await this.$axios.get(`/viewing-records/${id}`)
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
              ? await this.$axios.put('/viewing-records', this.form)
              : await this.$axios.post('/viewing-records', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/viewing-records')
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
      this.$router.push('/viewing-records')
    }
  }
}
</script>
