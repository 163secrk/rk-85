<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">认购签约详情</div>
      <div>
        <el-button icon="el-icon-edit" type="primary" @click="goToEdit">编辑</el-button>
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">合同信息</span>
          </div>
          <div class="info-item">
            <span class="label">合同编号：</span>
            <span class="value text-primary font-bold">{{ subscription.subscriptionNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">合同状态：</span>
            <el-tag :type="getStatusType(subscription.status)" size="mini">{{ subscription.status }}</el-tag>
          </div>
          <div class="info-item">
            <span class="label">签约日期：</span>
            <span class="value">{{ subscription.signDate | formatDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">交房日期：</span>
            <span class="value">{{ subscription.deliveryDate | formatDate }}</span>
          </div>
          <div class="info-item">
            <span class="label">经办人：</span>
            <span class="value">{{ subscription.agentName }}</span>
          </div>
        </div>

        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">客户信息</span>
          </div>
          <div class="info-item">
            <span class="label">客户姓名：</span>
            <span class="value">{{ subscription.customerName }}</span>
          </div>
          <div class="info-item">
            <span class="label">联系电话：</span>
            <span class="value">{{ subscription.customerPhone }}</span>
          </div>
          <div class="info-item">
            <span class="label">身份证号：</span>
            <span class="value">{{ subscription.customerIdCard }}</span>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">房源信息</span>
          </div>
          <div class="info-item">
            <span class="label">房源名称：</span>
            <span class="value">{{ subscription.propertyTitle }}</span>
          </div>
          <div class="info-item">
            <span class="label">房源地址：</span>
            <span class="value">{{ subscription.propertyAddress }}</span>
          </div>
          <div class="info-item">
            <span class="label">房屋面积：</span>
            <span class="value">{{ subscription.area }} ㎡</span>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card mb-20 price-card">
          <div class="card-header">
            <span class="card-title">价格信息</span>
          </div>
          <div class="price-item total">
            <span class="label">房屋总价：</span>
            <span class="value">¥ {{ subscription.totalPrice }} 万元</span>
          </div>
          <div class="price-item">
            <span class="label">单价：</span>
            <span class="value">{{ subscription.unitPrice }} 元/㎡</span>
          </div>
          <div class="price-item">
            <span class="label">定金：</span>
            <span class="value">¥ {{ subscription.depositAmount }} 万元</span>
          </div>
          <div class="price-item">
            <span class="label">首付：</span>
            <span class="value">¥ {{ subscription.firstPayment }} 万元</span>
          </div>
          <div class="price-item">
            <span class="label">剩余房款：</span>
            <span class="value">¥ {{ subscription.remainingAmount }} 万元</span>
          </div>
          <div class="price-item">
            <span class="label">付款方式：</span>
            <span class="value">{{ subscription.paymentMethod }}</span>
          </div>
          <div class="price-item">
            <span class="label">贷款年限：</span>
            <span class="value">{{ subscription.paymentPeriod }} 年</span>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">备注信息</span>
          </div>
          <p class="remark">{{ subscription.remark || '暂无备注' }}</p>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'SubscriptionDetail',
  data() {
    return {
      subscription: {}
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadDetail(id)
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/subscriptions/${id}`)
      if (res.code === 200) {
        this.subscription = res.data
      }
    },
    goToEdit() {
      this.$router.push(`/subscriptions/edit/${this.subscription.id}`)
    },
    goBack() {
      this.$router.push('/subscriptions')
    },
    getStatusType(status) {
      const map = {
        '已签约': 'success',
        '待签约': 'warning',
        '已取消': 'danger',
        '已完成': 'success'
      }
      return map[status] || 'info'
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
  padding: 10px 0;
  border-bottom: 1px dashed #ebeef5;
}

.info-item:last-child {
  border-bottom: none;
}

.label {
  width: 100px;
  color: #909399;
  flex-shrink: 0;
}

.value {
  flex: 1;
  color: #303133;
}

.font-bold {
  font-weight: bold;
}

.price-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.price-card .card-header {
  border-bottom-color: rgba(255, 255, 255, 0.2);
}

.price-card .card-title {
  color: #fff;
}

.price-item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px dashed rgba(255, 255, 255, 0.2);
}

.price-item:last-child {
  border-bottom: none;
}

.price-item.total {
  padding: 15px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.price-item .label {
  width: 100px;
  color: rgba(255, 255, 255, 0.8);
  flex-shrink: 0;
}

.price-item .value {
  flex: 1;
  color: #fff;
  font-weight: 500;
}

.price-item.total .value {
  font-size: 22px;
  font-weight: bold;
}

.remark {
  color: #606266;
  line-height: 1.8;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
</style>
