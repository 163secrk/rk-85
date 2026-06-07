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
        <div class="card mb-20 customer-profile">
          <div class="card-header">
            <span class="card-title">
              <i class="el-icon-user-solid" style="margin-right: 8px; color: #409EFF;"></i>客户画像
            </span>
          </div>
          <div class="profile-stats">
            <div class="stat-item">
              <div class="stat-icon icon-viewing">
                <i class="el-icon-location-outline"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ viewingCount }}</div>
                <div class="stat-label">带看次数</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-follow">
                <i class="el-icon-phone-outline"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ followUpCount }}</div>
                <div class="stat-label">跟进记录</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-type">
                <i class="el-icon-home"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value text-primary">{{ customerProfile.preferredType || '-' }}</div>
                <div class="stat-label">意向户型</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon icon-budget">
                <i class="el-icon-money"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value text-success">{{ customerProfile.budget || '-' }}</div>
                <div class="stat-label">预算区间</div>
              </div>
            </div>
          </div>
          <div class="profile-section">
            <div class="section-title">最近带看房源</div>
            <div class="recent-property" v-if="recentViewing">
              <div class="property-info">
                <span class="property-title">{{ recentViewing.propertyTitle }}</span>
                <span class="property-time">{{ recentViewing.viewingTime | formatDate('MM-DD HH:mm') }}</span>
              </div>
              <div class="property-meta">
                <el-tag size="mini" :type="getSatisfactionTagType(recentViewing.satisfaction)">{{ recentViewing.satisfaction }}</el-tag>
                <span class="property-agent">带看：{{ recentViewing.agentName }}</span>
              </div>
            </div>
            <div class="no-data" v-else>暂无带看记录</div>
          </div>
          <div class="profile-section">
            <div class="section-title">客户关注关键词</div>
            <div class="tag-cloud">
              <span 
                v-for="(tag, index) in keywords" 
                :key="index"
                class="keyword-tag"
                :class="'tag-size-' + tag.size"
                :style="{ color: tag.color, borderColor: tag.color + '60', backgroundColor: tag.color + '15' }"
              >
                {{ tag.name }}
              </span>
            </div>
          </div>
        </div>

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
  computed: {
    viewingCount() {
      return this.viewingRecords.length
    },
    followUpCount() {
      return this.followUpRecords.length
    },
    customerProfile() {
      return {
        preferredType: this.customer.preferredType || '-',
        budget: this.customer.budget || '-',
        preferredArea: this.customer.preferredArea || '-'
      }
    },
    recentViewing() {
      if (!this.viewingRecords.length) return null
      return [...this.viewingRecords].sort((a, b) => {
        return new Date(b.viewingTime) - new Date(a.viewingTime)
      })[0]
    },
    keywords() {
      const keywordMap = {}
      const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#606266']
      const extractKeywords = (text) => {
        if (!text) return
        const patterns = [
          '学区房', '地铁房', '精装修', '毛坯', '南北通透', '采光好', '楼层佳',
          '位置好', '交通便利', '配套齐全', '学区', '地铁', '公园', '医院',
          '商场', '学校', '车位', '电梯', '洋房', '别墅', '高层', '低层',
          '朝南', '朝北', '朝东', '朝西', '满五唯一', '满二', '免税', '刚需',
          '改善', '投资', '自住', '婚房', '养老', '学区', '落户', '升值空间'
        ]
        patterns.forEach(pattern => {
          if (text.includes(pattern)) {
            keywordMap[pattern] = (keywordMap[pattern] || 0) + 1
          }
        })
      }
      this.followUpRecords.forEach(record => {
        extractKeywords(record.followContent)
        extractKeywords(record.followResult)
      })
      this.viewingRecords.forEach(record => {
        extractKeywords(record.propertyTitle)
      })
      if (this.customer.preferredType) {
        keywordMap[this.customer.preferredType] = (keywordMap[this.customer.preferredType] || 0) + 2
      }
      if (this.customer.preferredArea) {
        keywordMap[this.customer.preferredArea] = (keywordMap[this.customer.preferredArea] || 0) + 2
      }
      if (this.customer.source) {
        keywordMap[this.customer.source] = (keywordMap[this.customer.source] || 0) + 1
      }
      const keywords = Object.keys(keywordMap).map(name => ({
        name,
        count: keywordMap[name]
      }))
      if (keywords.length === 0) {
        return ['学区房', '地铁房', '精装修', '南北通透', '采光好', '交通便利', '配套齐全', '楼层佳'].map((name, i) => ({
          name,
          size: Math.floor(Math.random() * 3) + 1,
          color: colors[i % colors.length]
        }))
      }
      const maxCount = Math.max(...keywords.map(k => k.count))
      return keywords
        .sort((a, b) => b.count - a.count)
        .slice(0, 12)
        .map((k, i) => ({
          name: k.name,
          size: maxCount <= 1 ? 2 : Math.max(1, Math.ceil((k.count / maxCount) * 3)),
          color: colors[i % colors.length]
        }))
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
    },
    getSatisfactionTagType(satisfaction) {
      const map = {
        '非常满意': 'success',
        '满意': 'primary',
        '一般': 'warning',
        '不满意': 'danger'
      }
      return map[satisfaction] || 'info'
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

.customer-profile .profile-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 15px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  border-radius: 8px;
}

.customer-profile .stat-item {
  display: flex;
  align-items: center;
  flex: 1;
}

.customer-profile .stat-item:not(:last-child) {
  border-right: 1px dashed #dcdfe6;
  margin-right: 15px;
  padding-right: 15px;
}

.customer-profile .stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #fff;
  font-size: 20px;
}

.customer-profile .icon-viewing {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.customer-profile .icon-follow {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.customer-profile .icon-type {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.customer-profile .icon-budget {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.customer-profile .stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  line-height: 1.2;
}

.customer-profile .stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.customer-profile .profile-section {
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px dashed #ebeef5;
}

.customer-profile .profile-section:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.customer-profile .section-title {
  font-size: 14px;
  font-weight: 600;
  color: #606266;
  margin-bottom: 10px;
  padding-left: 8px;
  border-left: 3px solid #409EFF;
}

.customer-profile .recent-property {
  background: #f5f7fa;
  padding: 12px 15px;
  border-radius: 6px;
}

.customer-profile .property-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.customer-profile .property-title {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.customer-profile .property-time {
  font-size: 12px;
  color: #909399;
}

.customer-profile .property-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.customer-profile .property-agent {
  font-size: 12px;
  color: #606266;
}

.customer-profile .no-data {
  color: #c0c4cc;
  font-size: 13px;
  text-align: center;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 6px;
}

.customer-profile .tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.customer-profile .keyword-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  border: 1px solid;
  font-size: 12px;
  cursor: default;
  transition: all 0.3s;
}

.customer-profile .keyword-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.customer-profile .tag-size-1 {
  font-size: 11px;
  padding: 3px 10px;
}

.customer-profile .tag-size-2 {
  font-size: 13px;
  padding: 5px 14px;
  font-weight: 500;
}

.customer-profile .tag-size-3 {
  font-size: 15px;
  padding: 6px 16px;
  font-weight: 600;
}
</style>
