<template>
  <el-container class="main-container">
    <el-aside :width="sidebarCollapsed ? '64px' : '220px'" class="sidebar">
      <div class="logo">
        <i class="el-icon-office-building"></i>
        <span v-show="!sidebarCollapsed" class="logo-text">房产管理系统</span>
      </div>
      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        :collapse="sidebarCollapsed"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF">
        <el-menu-item index="/dashboard">
          <i class="el-icon-s-data"></i>
          <span slot="title">数据概览</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <i class="el-icon-user"></i>
          <span slot="title">客户管理</span>
        </el-menu-item>
        <el-menu-item index="/properties">
          <i class="el-icon-house"></i>
          <span slot="title">房源管理</span>
        </el-menu-item>
        <el-menu-item index="/viewing-records">
          <i class="el-icon-date"></i>
          <span slot="title">带看记录</span>
        </el-menu-item>
        <el-menu-item index="/follow-up-records">
          <i class="el-icon-phone-outline"></i>
          <span slot="title">跟进记录</span>
        </el-menu-item>
        <el-menu-item index="/vr-panoramas">
          <i class="el-icon-picture-outline"></i>
          <span slot="title">VR全景</span>
        </el-menu-item>
        <el-menu-item index="/subscriptions">
          <i class="el-icon-document"></i>
          <span slot="title">认购签约</span>
        </el-menu-item>
        <el-menu-item index="/contracts">
          <i class="el-icon-s-contract"></i>
          <span slot="title">交易合同</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <i class="el-icon-fold" @click="toggleSidebar"></i>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">
              {{ item }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <i class="el-icon-user-solid"></i>
              {{ user.name }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'MainLayout',
  computed: {
    ...mapGetters(['sidebarCollapsed', 'user']),
    breadcrumbs() {
      const path = this.$route.path
      const breadcrumbMap = {
        '/dashboard': ['数据概览'],
        '/customers': ['客户管理', '客户列表'],
        '/customers/add': ['客户管理', '新增客户'],
        '/properties': ['房源管理', '房源列表'],
        '/properties/add': ['房源管理', '新增房源'],
        '/viewing-records': ['带看记录', '记录列表'],
        '/viewing-records/add': ['带看记录', '新增记录'],
        '/follow-up-records': ['跟进记录', '记录列表'],
        '/follow-up-records/add': ['跟进记录', '新增记录'],
        '/vr-panoramas': ['VR全景', '全景列表'],
        '/vr-panoramas/add': ['VR全景', '新增全景'],
        '/subscriptions': ['认购签约', '签约列表'],
        '/subscriptions/add': ['认购签约', '新增签约'],
        '/contracts': ['交易合同', '合同列表'],
        '/contracts/add': ['交易合同', '新增合同']
      }
      if (breadcrumbMap[path]) {
        return breadcrumbMap[path]
      }
      if (path.startsWith('/contracts/edit/')) {
        return ['交易合同', '编辑合同']
      }
      if (path.startsWith('/contracts/detail/')) {
        return ['交易合同', '合同详情']
      }
      if (path.startsWith('/customers/edit/') || path.startsWith('/customers/detail/')) {
        return ['客户管理', '客户详情']
      }
      if (path.startsWith('/properties/edit/') || path.startsWith('/properties/detail/')) {
        return ['房源管理', '房源详情']
      }
      if (path.startsWith('/subscriptions/edit/') || path.startsWith('/subscriptions/detail/')) {
        return ['认购签约', '签约详情']
      }
      if (path.startsWith('/viewing-records/edit/')) {
        return ['带看记录', '编辑记录']
      }
      if (path.startsWith('/vr-panoramas/edit/')) {
        return ['VR全景', '编辑全景']
      }
      if (path.startsWith('/vr-panoramas/view/')) {
        return ['VR全景', '全景播放']
      }
      return ['首页']
    }
  },
  methods: {
    ...mapActions(['toggleSidebar'])
  }
}
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.sidebar {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b2f3a;
}

.logo i {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  white-space: nowrap;
}

.sidebar-menu {
  border-right: none;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-left i {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
  color: #606266;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #606266;
}

.user-info i {
  margin-right: 5px;
}

.main-content {
  background-color: #f0f2f5;
  overflow-y: auto;
  padding: 0;
}
</style>
