<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">{{ isEdit ? '编辑合同' : '新增合同' }}</div>
      <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    </div>

    <div class="form-container">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="form-section-title">
              <i class="el-icon-user"></i> 买方信息
            </div>
          </el-col>
          <el-col :span="12">
            <div class="form-section-title">
              <i class="el-icon-user"></i> 卖方信息
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="买方姓名" prop="customerName">
              <el-input v-model="form.customerName" placeholder="请输入买方姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卖方姓名" prop="sellerName">
              <el-input v-model="form.sellerName" placeholder="请输入卖方姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="customerPhone">
              <el-input v-model="form.customerPhone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="sellerPhone">
              <el-input v-model="form.sellerPhone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号" prop="customerIdCard">
              <el-input v-model="form.customerIdCard" placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="sellerIdCard">
              <el-input v-model="form.sellerIdCard" placeholder="请输入身份证号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系地址">
              <el-input v-model="form.customerAddress" placeholder="请输入联系地址"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系地址">
              <el-input v-model="form.sellerAddress" placeholder="请输入联系地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <div class="form-section-title">
              <i class="el-icon-house"></i> 房源信息
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房源名称" prop="propertyTitle">
              <el-input v-model="form.propertyTitle" placeholder="请输入房源名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房源编号">
              <el-input v-model="form.propertyNo" placeholder="请输入房源编号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="房源地址" prop="propertyAddress">
              <el-input v-model="form.propertyAddress" placeholder="请输入房源地址"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="房屋类型">
              <el-select v-model="form.propertyType" placeholder="请选择房屋类型">
                <el-option label="住宅" value="住宅"></el-option>
                <el-option label="公寓" value="公寓"></el-option>
                <el-option label="别墅" value="别墅"></el-option>
                <el-option label="商铺" value="商铺"></el-option>
                <el-option label="写字楼" value="写字楼"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="面积(㎡)">
              <el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="户型">
              <el-row :gutter="10">
                <el-col :span="8">
                  <el-input-number v-model="form.rooms" :min="0" placeholder="室" style="width: 100%"></el-input-number>
                </el-col>
                <el-col :span="8">
                  <el-input-number v-model="form.halls" :min="0" placeholder="厅" style="width: 100%"></el-input-number>
                </el-col>
                <el-col :span="8">
                  <el-input-number v-model="form.bathrooms" :min="0" placeholder="卫" style="width: 100%"></el-input-number>
                </el-col>
              </el-row>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="楼层">
              <el-input v-model="form.floor" placeholder="如：3/18层"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="朝向">
              <el-select v-model="form.orientation" placeholder="请选择朝向">
                <el-option label="东" value="东"></el-option>
                <el-option label="南" value="南"></el-option>
                <el-option label="西" value="西"></el-option>
                <el-option label="北" value="北"></el-option>
                <el-option label="南北" value="南北"></el-option>
                <el-option label="东西" value="东西"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="装修">
              <el-select v-model="form.decoration" placeholder="请选择装修">
                <el-option label="毛坯" value="毛坯"></el-option>
                <el-option label="简装" value="简装"></el-option>
                <el-option label="精装" value="精装"></el-option>
                <el-option label="豪装" value="豪装"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <div class="form-section-title">
              <i class="el-icon-money"></i> 价格与付款信息
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="单价(元/㎡)">
              <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="成交价(万)" prop="totalPrice">
              <el-input-number v-model="form.totalPrice" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="定金(万)" prop="depositAmount">
              <el-input-number v-model="form.depositAmount" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="首付(万)">
              <el-input-number v-model="form.firstPayment" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="剩余房款(万)">
              <el-input-number v-model="form.remainingAmount" :min="0" :precision="2" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
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
          <el-col :span="8">
            <el-form-item label="贷款年限(年)">
              <el-input-number v-model="form.paymentPeriod" :min="0" :max="30" style="width: 100%"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="签署日期" prop="signDate">
              <el-date-picker
                v-model="form.signDate"
                type="datetime"
                placeholder="请选择签署日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交房日期" prop="deliveryDate">
              <el-date-picker
                v-model="form.deliveryDate"
                type="datetime"
                placeholder="请选择交房日期"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="width: 100%"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合同状态">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="草稿" value="草稿"></el-option>
                <el-option label="已签署" value="已签署"></el-option>
                <el-option label="已备案" value="已备案"></el-option>
                <el-option label="已完结" value="已完结"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经办人">
              <el-input v-model="form.agentName" placeholder="请输入经办人姓名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

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
  name: 'ContractForm',
  data() {
    return {
      isEdit: false,
      submitting: false,
      form: {
        id: null,
        contractNo: '',
        subscriptionId: null,
        subscriptionNo: '',
        customerId: null,
        customerName: '',
        customerPhone: '',
        customerIdCard: '',
        customerAddress: '',
        sellerName: '',
        sellerPhone: '',
        sellerIdCard: '',
        sellerAddress: '',
        propertyId: null,
        propertyTitle: '',
        propertyAddress: '',
        propertyNo: '',
        propertyType: '',
        area: null,
        rooms: null,
        halls: null,
        bathrooms: null,
        floor: '',
        orientation: '',
        decoration: '',
        totalPrice: null,
        unitPrice: null,
        depositAmount: null,
        firstPayment: null,
        remainingAmount: null,
        paymentMethod: '',
        paymentPeriod: 30,
        signDate: '',
        deliveryDate: '',
        status: '草稿',
        agentName: '',
        remark: ''
      },
      rules: {
        customerName: [{ required: true, message: '请输入买方姓名', trigger: 'blur' }],
        customerPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        customerIdCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        sellerName: [{ required: true, message: '请输入卖方姓名', trigger: 'blur' }],
        sellerPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        sellerIdCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
        propertyTitle: [{ required: true, message: '请输入房源名称', trigger: 'blur' }],
        propertyAddress: [{ required: true, message: '请输入房源地址', trigger: 'blur' }],
        totalPrice: [{ required: true, message: '请输入成交价', trigger: 'blur' }],
        depositAmount: [{ required: true, message: '请输入定金', trigger: 'blur' }],
        paymentMethod: [{ required: true, message: '请选择付款方式', trigger: 'change' }],
        signDate: [{ required: true, message: '请选择签署日期', trigger: 'change' }],
        deliveryDate: [{ required: true, message: '请选择交房日期', trigger: 'change' }]
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
      const res = await this.$axios.get(`/contracts/${id}`)
      if (res.code === 200) {
        if (res.data.status !== '草稿') {
          this.$message.warning('非草稿状态的合同不可编辑')
          this.$router.push(`/contracts/detail/${id}`)
          return
        }
        this.form = { ...this.form, ...res.data }
      }
    },
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.submitting = true
          try {
            const res = this.isEdit
              ? await this.$axios.put('/contracts', this.form)
              : await this.$axios.post('/contracts', this.form)
            if (res.code === 200) {
              this.$message.success('保存成功')
              this.$router.push('/contracts')
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
      this.$router.push('/contracts')
    }
  }
}
</script>

<style scoped>
.form-section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  padding: 15px 0;
  border-bottom: 2px solid #409EFF;
  margin-bottom: 20px;
}

.form-section-title i {
  color: #409EFF;
  margin-right: 5px;
}
</style>
