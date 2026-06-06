<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">客户详情</div>
      <div>
        <el-button icon="el-icon-edit" type="primary" @click="goToEdit">编辑</el-button>
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">基本信息</span>
          </div>
          <div class="info-item">
            <span class="label">姓名：</span>
            <span class="value">{{ customer.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">电话：</span>
            <span class="value">{{ customer.phone }}</span>
          </div>
          <div class="info-item">
            <span class="label">性别：</span>
            <span class="value">{{ customer.gender }}</span>
          </div>
          <div class="info-item">
            <span class="label">年龄：</span>
            <span class="value">{{ customer.age }}</span>
          </div>
          <div class="info-item">
            <span class="label">身份证：</span>
            <span class="value">{{ customer.idCard }}</span>
          </div>
          <div class="info-item">
            <span class="label">地址：</span>
            <span class="value">{{ customer.address }}</span>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">购房需求</span>
          </div>
          <div class="info-item">
            <span class="label">预算：</span>
            <span class="value text-primary">{{ customer.budget }}</span>
          </div>
          <div class="info-item">
            <span class="label">意向区域：</span>
            <span class="value">{{ customer.preferredArea }}</span>
          </div>
          <div class="info-item">
            <span class="label">意向户型：</span>
            <span class="value">{{ customer.preferredType }}</span>
          </div>
          <div class="info-item">
            <span class="label">客户来源：</span>
            <span class="value">{{ customer.source }}</span>
          </div>
          <div class="info-item">
            <span class="label">客户等级：</span>
            <el-tag size="mini">{{ customer.level }}</el-tag>
          </div>
          <div class="info-item">
            <span class="label">状态：</span>
            <el-tag :type="customer.status === '已成交' ? 'success' : 'primary'" size="mini">{{ customer.status }}</el-tag>
          </div>
        </div>
      </el-col>

      <el-col :span="16">
        <div class="card mb-20">
          <div class="card-header flex justify-between items-center">
            <span class="card-title">带看记录</span>
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="addViewing">新增带看</el-button>
          </div>
          <el-table :data="viewingRecords" border stripe size="small">
            <el-table-column prop="propertyTitle" label="房源"></el-table-column>
            <el-table-column prop="viewingTime" label="带看时间" width="150">
              <template slot-scope="scope">
                {{ scope.row.viewingTime | formatDate('MM-DD HH:mm') }}
              </template>
            </el-table-column>
            <el-table-column prop="viewingDuration" label="时长(分钟)" width="100"></el-table-column>
            <el-table-column prop="satisfaction" label="满意度" width="90">
              <template slot-scope="scope">
                <span :class="getSatisfactionClass(scope.row.satisfaction)">{{ scope.row.satisfaction }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="agentName" label="经纪人" width="90"></el-table-column>
          </el-table>
        </div>

        <div class="card mb-20">
          <div class="card-header flex justify-between items-center">
            <span class="card-title">跟进记录</span>
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="addFollowUp">新增跟进</el-button>
          </div>
          <el-table :data="followUpRecords" border stripe size="small">
            <el-table-column prop="followType" label="跟进方式" width="100"></el-table-column>
            <el-table-column prop="followTime" label="跟进时间" width="150">
              <template slot-scope="scope">
                {{ scope.row.followTime | formatDate('MM-DD HH:mm') }}
              </template>
            </el-table-column>
            <el-table-column prop="followContent" label="跟进内容"></el-table-column>
            <el-table-column prop="followResult" label="结果" width="100"></el-table-column>
            <el-table-column prop="agentName" label="跟进人" width="90"></el-table-column>
          </el-table>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">认购记录</span>
          </div>
          <el-table :data="subscriptions" border stripe size="small">
            <el-table-column prop="subscriptionNo" label="合同编号" width="140"></el-table-column>
            <el-table-column prop="propertyTitle" label="房源"></el-table-column>
            <el-table-column prop="totalPrice" label="总价(万)" width="90"></el-table-column>
            <el-table-column prop="signDate" label="签约日期" width="150">
              <template slot-scope="scope">
                {{ scope.row.signDate | formatDate }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="90">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="mini">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'CustomerDetail',
  data() {
    return {
      customer: {},
      viewingRecords: [],
      followUpRecords: [],
      subscriptions: []
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadDetail(id)
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/customers/${id}`)
      if (res.code === 200) {
        this.customer = res.data
        this.loadViewingRecords(id)
        this.loadFollowUpRecords(id)
        this.loadSubscriptions(id)
      }
    },
    async loadViewingRecords(customerId) {
      const res = await this.$axios.get(`/viewing-records/customer/${customerId}`)
      if (res.code === 200) {
        this.viewingRecords = res.data
      }
    },
    async loadFollowUpRecords(customerId) {
      const res = await this.$axios.get(`/follow-up-records/customer/${customerId}`)
      if (res.code === 200) {
        this.followUpRecords = res.data
      }
    },
    async loadSubscriptions(customerId) {
      const res = await this.$axios.get(`/subscriptions/customer/${customerId}`)
      if (res.code === 200) {
        this.subscriptions = res.data
      }
    },
    addViewing() {
      this.$router.push('/viewing-records/add')
    },
    addFollowUp() {
      this.$router.push('/follow-up-records/add')
    },
    goToEdit() {
      this.$router.push(`/customers/edit/${this.customer.id}`)
    },
    goBack() {
      this.$router.push('/customers')
    },
    getStatusType(status) {
      const map = {
        '已签约': 'success',
        '待签约': 'warning',
        '已取消': 'danger'
      }
      return map[status] || 'info'
    },
    getSatisfactionClass(satisfaction) {
      const map = {
        '非常满意': 'tag-success',
        '满意': 'tag-info',
        '一般': 'tag-warning',
        '不满意': 'tag-danger'
      }
      return map[satisfaction] || 'tag-info'
    }
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.info-item {
  display: flex;
  padding: 8px 0;
  border-bottom: 1px dashed #ebeef5;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  width: 80px;
  color: #909399;
}

.value {
  flex: 1;
  color: #303133;
}
</style>
