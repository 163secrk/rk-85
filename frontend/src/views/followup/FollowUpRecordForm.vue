<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">新增跟进记录</div>
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
            <el-form-item label="跟进方式" prop="followType">
              <el-select v-model="form.followType" placeholder="请选择方式">
                <el-option label="电话跟进" value="电话跟进"></el-option>
                <el-option label="微信跟进" value="微信跟进"></el-option>
                <el-option label="上门拜访" value="上门拜访"></el-option>
                <el-option label="短信提醒" value="短信提醒"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="跟进时间" prop="followTime">
              <el-date-picker
                v-model="form.followTime"
                type="datetime"
                placeholder="请选择跟进时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次跟进时间">
              <el-date-picker
                v-model="form.nextFollowTime"
                type="datetime"
                placeholder="请选择下次跟进时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="经纪人">
          <el-input v-model="form.agentName" placeholder="请输入经纪人姓名"></el-input>
        </el-form-item>
        <el-form-item label="跟进内容" prop="followContent">
          <el-input type="textarea" v-model="form.followContent" :rows="4" placeholder="请输入跟进内容"></el-input>
        </el-form-item>
        <el-form-item label="跟进结果">
          <el-input type="textarea" v-model="form.followResult" :rows="2" placeholder="请输入跟进结果"></el-input>
        </el-form-item>
        <el-form-item label="后续计划">
          <el-input type="textarea" v-model="form.nextFollowPlan" :rows="2" placeholder="请输入后续跟进计划"></el-input>
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
  name: 'FollowUpRecordForm',
  data() {
    return {
      submitting: false,
      customerList: [],
      form: {
        id: null,
        customerId: null,
        customerName: '',
        followType: '',
        followTime: '',
        followContent: '',
        followResult: '',
        nextFollowPlan: '',
        nextFollowTime: '',
        agentName: '',
        status: '已完成',
        remark: ''
      },
      rules: {
        customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        followType: [{ required: true, message: '请选择跟进方式', trigger: 'change' }],
        followTime: [{ required: true, message: '请选择跟进时间', trigger: 'change' }],
        followContent: [{ required: true, message: '请输入跟进内容', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadCustomers()
  },
  methods: {
    async loadCustomers() {
      const res = await this.$axios.get('/customers')
      if (res.code === 200) {
        this.customerList = res.data
      }
    },
    onCustomerChange(customerId) {
      const customer = this.customerList.find(c => c.id === customerId)
      if (customer) {
        this.form.customerName = customer.name
      }
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const res = await this.$axios.post('/follow-up-records', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/follow-up-records')
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
      this.$router.push('/follow-up-records')
    }
  }
}
</script>
