<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑签约' : '新增签约' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
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
            <el-form-item label="总价(万)" prop="totalPrice">
              <el-input-number v-model="form.totalPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单价(元/㎡)">
              <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="面积(㎡)">
              <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="定金(万)" prop="depositAmount">
              <el-input-number v-model="form.depositAmount" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="首付(万)">
              <el-input-number v-model="form.firstPayment" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="剩余房款(万)">
              <el-input-number v-model="form.remainingAmount" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式" prop="paymentMethod">
              <el-select v-model="form.paymentMethod" placeholder="请选择付款方式">
                <el-option label="一次性付款" value="一次性付款"></el-option>
                <el-option label="商业贷款" value="商业贷款"></el-option>
                <el-option label="公积金贷款" value="公积金贷款"></el-option>
                <el-option label="组合贷款" value="组合贷款"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="贷款年限(年)">
              <el-input-number v-model="form.paymentPeriod" :min="0" :max="30" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="签约日期" prop="signDate">
              <el-date-picker
                v-model="form.signDate"
                type="datetime"
                placeholder="请选择签约日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交房日期">
              <el-date-picker
                v-model="form.deliveryDate"
                type="datetime"
                placeholder="请选择交房日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同状态">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="待签约" value="待签约"></el-option>
                <el-option label="已签约" value="已签约"></el-option>
                <el-option label="已取消" value="已取消"></el-option>
                <el-option label="已完成" value="已完成"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="经办人">
          <el-input v-model="form.agentName" placeholder="请输入经办人姓名"></el-input>
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
  name: 'SubscriptionForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      customerList: [],
      propertyList: [],
      form: {
        id: null,
        subscriptionNo: '',
        customerId: null,
        customerName: '',
        customerPhone: '',
        customerIdCard: '',
        propertyId: null,
        propertyTitle: '',
        propertyAddress: '',
        totalPrice: null,
        unitPrice: null,
        area: null,
        depositAmount: null,
        firstPayment: null,
        remainingAmount: null,
        paymentMethod: '',
        paymentPeriod: 30,
        signDate: '',
        deliveryDate: '',
        status: '待签约',
        agentName: '',
        remark: ''
      },
      rules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        propertyId: [{ required: true, message: '请选择房源', trigger: 'change' }],
        totalPrice: [{ required: true, message: '请输入总价', trigger: 'blur' }],
        depositAmount: [{ required: true, message: '请输入定金', trigger: 'blur' }],
        paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }],
        signDate: [{ required: true, message: '请选择签约日期', trigger: 'change' }]
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
        this.form.customerIdCard = customer.idCard
      }
    },
    onPropertyChange(propertyId) {
      const property = this.propertyList.find(p => p.id === propertyId)
      if (property) {
        this.form.propertyTitle = property.title
        this.form.propertyAddress = property.address
        if (property.price) {
          this.form.totalPrice = property.price
        }
        if (property.unitPrice) {
          this.form.unitPrice = property.unitPrice
        }
      }
    },
    async loadDetail(id) {
      const res = await this.$axios.get(`/subscriptions/${id}`)
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
              ? await this.$axios.put('/subscriptions', this.form)
              : await this.$axios.post('/subscriptions', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/subscriptions')
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
      this.$router.push('/subscriptions')
    }
  }
}
</script>
