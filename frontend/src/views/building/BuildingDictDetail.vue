<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">楼盘详情</div>
      <div>
        <el-button icon="el-icon-edit" type="primary" @click="goToEdit">编辑</el-button>
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
    </div>

    <div v-if="detail.buildingDict">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="card mb-20">
            <img :src="detail.buildingDict.imageUrl" class="building-image" alt="楼盘图片">
            <div class="building-title">{{ detail.buildingDict.name }}</div>
            <div class="building-price">
              <span class="price">¥{{ detail.buildingDict.averagePrice || '暂无' }}</span>
              <span class="unit" v-if="detail.buildingDict.averagePrice">元/㎡</span>
            </div>
            <div class="building-basic">
              <span>{{ detail.buildingDict.buildingType || '暂无' }}</span>
              <span>{{ detail.buildingDict.buildingYear || '暂无' }}年建</span>
            </div>
          </div>

          <div class="card mb-20">
            <div class="card-header">
              <span class="card-title">价格走势</span>
            </div>
            <div class="price-stats">
              <div class="price-item">
                <div class="price-label">历史成交均价</div>
                <div class="price-value">¥{{ formatPrice(detail.historicalAveragePrice) }}</div>
                <div class="price-sub">元/㎡</div>
              </div>
              <div class="price-item">
                <div class="price-label">当前在售均价</div>
                <div class="price-value">¥{{ formatPrice(detail.currentAveragePrice) }}</div>
                <div class="price-sub">元/㎡</div>
              </div>
            </div>
            <div class="count-stats">
              <div class="count-item">
                <span class="count-number text-success">{{ detail.onSaleCount || 0 }}</span>
                <span class="count-label">套在售</span>
              </div>
              <div class="count-item">
                <span class="count-number text-danger">{{ detail.soldCount || 0 }}</span>
                <span class="count-label">套已售</span>
              </div>
            </div>
          </div>

          <div class="card">
            <div class="card-header">
              <span class="card-title">基本信息</span>
            </div>
            <div class="info-item">
              <span class="label">所在城市：</span>
              <span class="value">{{ detail.buildingDict.city }}</span>
            </div>
            <div class="info-item">
              <span class="label">所在区域：</span>
              <span class="value">{{ detail.buildingDict.district }}</span>
            </div>
            <div class="info-item">
              <span class="label">详细地址：</span>
              <span class="value">{{ detail.buildingDict.address }}</span>
            </div>
            <div class="info-item">
              <span class="label">建筑类型：</span>
              <span class="value">{{ detail.buildingDict.buildingType || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">建成年代：</span>
              <span class="value">{{ detail.buildingDict.buildingYear || '暂无' }}年</span>
            </div>
            <div class="info-item">
              <span class="label">开 发 商：</span>
              <span class="value">{{ detail.buildingDict.developer || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">物业公司：</span>
              <span class="value">{{ detail.buildingDict.propertyCompany || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">总 户 数：</span>
              <span class="value">{{ detail.buildingDict.totalHouses || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">容 积 率：</span>
              <span class="value">{{ detail.buildingDict.plotRatio || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">绿 化 率：</span>
              <span class="value">{{ detail.buildingDict.greeningRate || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="label">车 位 数：</span>
              <span class="value">{{ detail.buildingDict.parkingSpace || '暂无' }}</span>
            </div>
          </div>
        </el-col>

        <el-col :span="16">
          <div class="card mb-20">
            <div class="card-header">
              <span class="card-title">楼盘描述</span>
            </div>
            <p class="description">{{ detail.buildingDict.description || '暂无描述' }}</p>
          </div>

          <div class="card mb-20">
            <div class="card-header">
              <span class="card-title">周边配套</span>
            </div>
            <div class="facilities" v-if="detail.supportingFacilityList && detail.supportingFacilityList.length > 0">
              <el-tag v-for="item in detail.supportingFacilityList" :key="item" size="small">{{ item }}</el-tag>
            </div>
            <p v-else class="text-muted">暂无配套信息</p>
          </div>

          <div class="card mb-20">
            <div class="card-header">
              <span class="card-title">在售房源 ({{ detail.onSaleCount || 0 }}套)</span>
            </div>
            <div v-if="detail.onSaleProperties && detail.onSaleProperties.length > 0">
              <div class="property-list">
                <div class="property-item" v-for="item in detail.onSaleProperties" :key="item.id" @click="goToPropertyDetail(item.id)">
                  <img :src="item.imageUrl" class="property-thumb" alt="">
                  <div class="property-info">
                    <div class="property-name">{{ item.title }}</div>
                    <div class="property-desc">
                      <span>{{ item.rooms }}室{{ item.halls }}厅</span>
                      <span>{{ item.area }}㎡</span>
                      <span>{{ item.orientation }}</span>
                      <span>{{ item.floor }}/{{ item.totalFloor }}层</span>
                    </div>
                    <div class="property-tags">
                      <el-tag v-for="tag in getTags(item.features)" :key="tag" size="mini" type="info">{{ tag }}</el-tag>
                    </div>
                  </div>
                  <div class="property-price">
                    <div class="total-price">¥{{ item.price }}万</div>
                    <div class="unit-price">{{ item.unitPrice }}元/㎡</div>
                  </div>
                </div>
              </div>
            </div>
            <p v-else class="text-muted">暂无在售房源</p>
          </div>

          <div class="card" v-if="detail.soldProperties && detail.soldProperties.length > 0">
            <div class="card-header">
              <span class="card-title">历史成交 ({{ detail.soldCount || 0 }}套)</span>
            </div>
            <div class="property-list">
              <div class="property-item sold" v-for="item in detail.soldProperties" :key="item.id" @click="goToPropertyDetail(item.id)">
                <img :src="item.imageUrl" class="property-thumb" alt="">
                <div class="property-info">
                  <div class="property-name">{{ item.title }}</div>
                  <div class="property-desc">
                    <span>{{ item.rooms }}室{{ item.halls }}厅</span>
                    <span>{{ item.area }}㎡</span>
                    <span>{{ item.orientation }}</span>
                  </div>
                </div>
                <div class="property-price">
                  <div class="total-price">¥{{ item.price }}万</div>
                  <div class="unit-price">{{ item.unitPrice }}元/㎡</div>
                  <el-tag size="mini" type="danger" style="margin-top: 5px;">已成交</el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BuildingDictDetail',
  data() {
    return {
      detail: {}
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadDetail(id)
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/building-dicts/${id}/detail`)
      if (res.code === 200) {
        this.detail = res.data
      }
    },
    formatPrice(price) {
      if (!price || price === 0) return '暂无'
      return Math.round(price)
    },
    getTags(features) {
      if (!features) return []
      return features.split(',').map(s => s.trim()).filter(s => s)
    },
    goToPropertyDetail(id) {
      this.$router.push(`/properties/detail/${id}`)
    },
    goToEdit() {
      this.$router.push(`/building-dicts/edit/${this.detail.buildingDict.id}`)
    },
    goBack() {
      this.$router.push('/building-dicts')
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

.building-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 15px;
}

.building-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.building-price {
  margin-bottom: 10px;
}

.price {
  font-size: 28px;
  font-weight: bold;
  color: #f56c6c;
  margin-right: 5px;
}

.unit {
  color: #909399;
  font-size: 14px;
}

.building-basic {
  display: flex;
  gap: 20px;
  color: #606266;
}

.price-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.price-item {
  flex: 1;
  text-align: center;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.price-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 5px;
}

.price-value {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}

.price-sub {
  color: #909399;
  font-size: 12px;
}

.count-stats {
  display: flex;
  gap: 20px;
  justify-content: center;
}

.count-item {
  display: flex;
  align-items: baseline;
  gap: 5px;
}

.count-number {
  font-size: 24px;
  font-weight: bold;
}

.count-label {
  color: #606266;
  font-size: 14px;
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
  width: 100px;
  color: #909399;
  flex-shrink: 0;
}

.value {
  flex: 1;
  color: #303133;
}

.description {
  color: #606266;
  line-height: 1.8;
}

.facilities {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.property-list {
  max-height: 500px;
  overflow-y: auto;
}

.property-item {
  display: flex;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
  cursor: pointer;
  transition: background-color 0.2s;
}

.property-item:hover {
  background-color: #f5f7fa;
  padding-left: 10px;
  padding-right: 10px;
  margin: 0 -10px;
  border-radius: 4px;
}

.property-item:last-child {
  border-bottom: none;
}

.property-thumb {
  width: 120px;
  height: 90px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}

.property-info {
  flex: 1;
  min-width: 0;
}

.property-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.property-desc {
  display: flex;
  gap: 15px;
  color: #606266;
  font-size: 13px;
  margin-bottom: 8px;
}

.property-tags {
  display: flex;
  gap: 5px;
  flex-wrap: wrap;
}

.property-price {
  text-align: right;
  flex-shrink: 0;
  min-width: 100px;
}

.total-price {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 3px;
}

.unit-price {
  color: #909399;
  font-size: 12px;
}

.property-item.sold .property-name {
  color: #909399;
  text-decoration: line-through;
}

.text-muted {
  color: #909399;
  text-align: center;
  padding: 20px;
}

.text-success {
  color: #67c23a;
}

.text-danger {
  color: #f56c6c;
}
</style>
