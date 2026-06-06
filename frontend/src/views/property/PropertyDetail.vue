<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">房源详情</div>
      <div>
        <el-button icon="el-icon-picture-outline" type="success" @click="viewVR">VR看房</el-button>
        <el-button icon="el-icon-edit" type="primary" @click="goToEdit">编辑</el-button>
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
    </div>

    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card mb-20">
          <img :src="property.imageUrl" class="property-image" alt="房源图片">
          <div class="property-title">{{ property.title }}</div>
          <div class="property-price">
            <span class="price">¥{{ property.price }}万</span>
            <span class="unit-price">{{ property.unitPrice }}元/㎡</span>
          </div>
          <div class="property-basic">
            <span>{{ property.rooms }}室{{ property.halls }}厅</span>
            <span>{{ property.area }}㎡</span>
            <span>{{ property.orientation }}</span>
          </div>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">基本信息</span>
          </div>
          <div class="info-item">
            <span class="label">房源编号：</span>
            <span class="value">{{ property.propertyNo }}</span>
          </div>
          <div class="info-item">
            <span class="label">房源类型：</span>
            <span class="value">{{ property.type }}</span>
          </div>
          <div class="info-item">
            <span class="label">所在楼层：</span>
            <span class="value">{{ property.floor }}/{{ property.totalFloor }}层</span>
          </div>
          <div class="info-item">
            <span class="label">装修情况：</span>
            <span class="value">{{ property.decoration }}</span>
          </div>
          <div class="info-item">
            <span class="label">建筑年代：</span>
            <span class="value">{{ property.buildingYear }}年</span>
          </div>
          <div class="info-item">
            <span class="label">房源状态：</span>
            <el-tag :type="property.status === '在售' ? 'success' : 'danger'" size="mini">{{ property.status }}</el-tag>
          </div>
        </div>
      </el-col>

      <el-col :span="16">
        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">房源描述</span>
          </div>
          <p class="description">{{ property.description }}</p>
        </div>

        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">房源位置</span>
          </div>
          <div class="info-item">
            <span class="label">所在城市：</span>
            <span class="value">{{ property.city }}</span>
          </div>
          <div class="info-item">
            <span class="label">所在区域：</span>
            <span class="value">{{ property.district }}</span>
          </div>
          <div class="info-item">
            <span class="label">详细地址：</span>
            <span class="value">{{ property.address }}</span>
          </div>
        </div>

        <div class="card mb-20">
          <div class="card-header">
            <span class="card-title">VR全景</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8" v-for="vr in vrList" :key="vr.id">
              <div class="vr-item" @click="playVR(vr.id)">
                <img :src="vr.thumbnailUrl" class="vr-thumb" alt="">
                <div class="vr-info">
                  <div class="vr-name">{{ vr.name }}</div>
                  <div class="vr-views">
                    <i class="el-icon-view"></i> {{ vr.viewCount }}
                  </div>
                </div>
                <div class="vr-play">
                  <i class="el-icon-video-play"></i>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="card">
          <div class="card-header">
            <span class="card-title">配套设施</span>
          </div>
          <div class="facilities">
            <el-tag v-for="item in facilities" :key="item" size="small">{{ item }}</el-tag>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'PropertyDetail',
  data() {
    return {
      property: {},
      vrList: []
    }
  },
  computed: {
    facilities() {
      return this.property.supportingFacilities ? this.property.supportingFacilities.split(',') : []
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadDetail(id)
    this.loadVRList(id)
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/properties/${id}`)
      if (res.code === 200) {
        this.property = res.data
      }
    },
    async loadVRList(propertyId) {
      const res = await this.$axios.get(`/vr-panoramas/property/${propertyId}`)
      if (res.code === 200) {
        this.vrList = res.data
      }
    },
    playVR(id) {
      this.$router.push(`/vr-panoramas/view/${id}`)
    },
    viewVR() {
      if (this.vrList.length > 0) {
        this.playVR(this.vrList[0].id)
      } else {
        this.$message.warning('该房源暂无VR全景')
      }
    },
    goToEdit() {
      this.$router.push(`/properties/edit/${this.property.id}`)
    },
    goBack() {
      this.$router.push('/properties')
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

.property-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 15px;
}

.property-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.property-price {
  margin-bottom: 10px;
}

.price {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
  margin-right: 10px;
}

.unit-price {
  color: #909399;
  font-size: 14px;
}

.property-basic {
  display: flex;
  gap: 20px;
  color: #606266;
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
}

.value {
  flex: 1;
  color: #303133;
}

.description {
  color: #606266;
  line-height: 1.8;
}

.vr-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 15px;
  border-radius: 4px;
  overflow: hidden;
}

.vr-thumb {
  width: 100%;
  height: 120px;
  object-fit: cover;
  display: block;
}

.vr-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0,0,0,0.7));
  color: #fff;
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.vr-name {
  font-size: 14px;
}

.vr-views {
  font-size: 12px;
  opacity: 0.9;
}

.vr-play {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px;
  height: 50px;
  background: rgba(0,0,0,0.5);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
}

.facilities {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
