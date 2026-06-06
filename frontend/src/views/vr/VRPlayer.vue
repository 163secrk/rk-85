<template>
  <div class="vr-player-container">
    <div class="vr-header">
      <el-button icon="el-icon-arrow-left" @click="goBack" plain>返回</el-button>
      <div class="vr-title">
        <span class="current-scene">{{ currentVR ? currentVR.name : 'VR全景浏览' }}</span>
        <span class="property-name">{{ currentVR ? currentVR.propertyTitle : '' }}</span>
      </div>
      <div class="vr-info">
        <el-tag type="info" size="small"><i class="el-icon-view"></i> {{ currentVR ? currentVR.viewCount : 0 }}</el-tag>
      </div>
    </div>

    <div class="vr-content">
      <div class="vr-main">
        <div class="vr-display">
          <div class="vr-image-wrapper">
            <img
              v-if="currentVR"
              :src="currentVR.panoramaUrl"
              alt="VR全景"
              class="vr-image"
              @mousedown="startDrag"
              @mousemove="onDrag"
              @mouseup="stopDrag"
              @mouseleave="stopDrag"
              @touchstart="startDrag"
              @touchmove="onDrag"
              @touchend="stopDrag"
              :style="imageStyle"
              draggable="false"
            >
            <div v-else class="vr-loading">
              <i class="el-icon-loading"></i>
              <span>加载中...</span>
            </div>
          </div>
          <div class="vr-controls">
            <el-button-group>
              <el-button icon="el-icon-zoom-in" circle @click="zoomIn"></el-button>
              <el-button icon="el-icon-zoom-out" circle @click="zoomOut"></el-button>
              <el-button icon="el-icon-refresh-left" circle @click="resetView"></el-button>
            </el-button-group>
            <div class="vr-hint">
              <i class="el-icon-sort"></i>
              拖动查看360°全景
            </div>
          </div>
        </div>
      </div>

      <div class="vr-sidebar">
        <div class="sidebar-title">场景切换</div>
        <div class="scene-list">
          <div
            v-for="vr in vrList"
            :key="vr.id"
            class="scene-item"
            :class="{ active: currentVR && currentVR.id === vr.id }"
            @click="switchScene(vr)"
          >
            <img :src="vr.thumbnailUrl" alt="" class="scene-thumb">
            <div class="scene-info">
              <div class="scene-name">{{ vr.name }}</div>
              <div class="scene-views"><i class="el-icon-view"></i> {{ vr.viewCount }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="vr-description" v-if="currentVR">
      <el-descriptions :column="3" border>
        <el-descriptions-item label="所属房源">
          {{ currentVR.propertyTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="场景名称">
          {{ currentVR.name }}
        </el-descriptions-item>
        <el-descriptions-item label="浏览次数">
          {{ currentVR.viewCount }}
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="3">
          {{ currentVR.description }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VRPlayer',
  data() {
    return {
      currentVR: null,
      vrList: [],
      scale: 1,
      rotateX: 0,
      rotateY: 0,
      isDragging: false,
      startX: 0,
      startY: 0,
      startRotateX: 0,
      startRotateY: 0
    }
  },
  computed: {
    imageStyle() {
      return {
        transform: `scale(${this.scale}) rotateY(${this.rotateY}deg) rotateX(${this.rotateX}deg)`,
        cursor: this.isDragging ? 'grabbing' : 'grab'
      }
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadVR(id)
  },
  methods: {
    async loadVR(id) {
      const res = await this.$axios.get(`/vr-panoramas/${id}`)
      if (res.code === 200) {
        this.currentVR = res.data
        this.loadVRList(res.data.propertyId)
        this.incrementViewCount(id)
      }
    },
    async loadVRList(propertyId) {
      const res = await this.$axios.get(`/vr-panoramas/property/${propertyId}`)
      if (res.code === 200) {
        this.vrList = res.data
      }
    },
    async incrementViewCount(id) {
      await this.$axios.post(`/vr-panoramas/${id}/view`)
    },
    switchScene(vr) {
      this.currentVR = vr
      this.rotateX = 0
      this.rotateY = 0
      this.scale = 1
      this.incrementViewCount(vr.id)
    },
    startDrag(e) {
      this.isDragging = true
      const event = e.touches ? e.touches[0] : e
      this.startX = event.clientX
      this.startY = event.clientY
      this.startRotateX = this.rotateX
      this.startRotateY = this.rotateY
    },
    onDrag(e) {
      if (!this.isDragging) return
      const event = e.touches ? e.touches[0] : e
      const deltaX = event.clientX - this.startX
      const deltaY = event.clientY - this.startY
      this.rotateY = this.startRotateY + deltaX * 0.5
      this.rotateX = this.startRotateX - deltaY * 0.5
      this.rotateX = Math.max(-90, Math.min(90, this.rotateX))
    },
    stopDrag() {
      this.isDragging = false
    },
    zoomIn() {
      this.scale = Math.min(3, this.scale + 0.2)
    },
    zoomOut() {
      this.scale = Math.max(0.5, this.scale - 0.2)
    },
    resetView() {
      this.scale = 1
      this.rotateX = 0
      this.rotateY = 0
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.vr-player-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #1a1a1a;
}

.vr-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 20px;
  background: #2d2d2d;
  border-bottom: 1px solid #404040;
}

.vr-title {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
}

.current-scene {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
}

.property-name {
  color: #909399;
  font-size: 14px;
}

.vr-info {
  min-width: 100px;
  text-align: right;
}

.vr-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.vr-main {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.vr-display {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(circle, #2d2d2d 0%, #1a1a1a 100%);
}

.vr-image-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.vr-image {
  max-width: 90%;
  max-height: 90%;
  object-fit: contain;
  transition: transform 0.1s ease-out;
  user-select: none;
  border-radius: 8px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
}

.vr-loading {
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  font-size: 18px;
}

.vr-loading i {
  font-size: 36px;
  animation: rotating 2s linear infinite;
}

@keyframes rotating {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.vr-controls {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.vr-hint {
  color: rgba(255, 255, 255, 0.7);
  font-size: 13px;
  background: rgba(0, 0, 0, 0.5);
  padding: 8px 15px;
  border-radius: 20px;
}

.vr-sidebar {
  width: 280px;
  background: #2d2d2d;
  border-left: 1px solid #404040;
  padding: 15px;
  overflow-y: auto;
}

.sidebar-title {
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #404040;
}

.scene-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.scene-item {
  display: flex;
  gap: 10px;
  padding: 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.scene-item:hover {
  background: #3d3d3d;
}

.scene-item.active {
  background: #3d3d3d;
  border-color: #409EFF;
}

.scene-thumb {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}

.scene-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
}

.scene-name {
  color: #fff;
  font-size: 14px;
}

.scene-views {
  color: #909399;
  font-size: 12px;
}

.vr-description {
  background: #2d2d2d;
  padding: 20px;
  border-top: 1px solid #404040;
  color: #fff;
}

::v-deep .el-descriptions__label,
::v-deep .el-descriptions__content {
  color: #fff !important;
  background: #3d3d3d !important;
  border-color: #404040 !important;
}

::v-deep .el-button--text {
  color: #fff !important;
}

::v-deep .el-button.is-plain {
  color: #fff !important;
  background: rgba(255, 255, 255, 0.1) !important;
  border-color: rgba(255, 255, 255, 0.2) !important;
}
</style>
