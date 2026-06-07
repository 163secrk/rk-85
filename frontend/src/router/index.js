import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/dashboard',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      }
    ]
  },
  {
    path: '/customers',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'CustomerList',
        component: () => import('@/views/customer/CustomerList.vue')
      },
      {
        path: 'add',
        name: 'CustomerAdd',
        component: () => import('@/views/customer/CustomerForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'CustomerEdit',
        component: () => import('@/views/customer/CustomerForm.vue')
      },
      {
        path: 'detail/:id',
        name: 'CustomerDetail',
        component: () => import('@/views/customer/CustomerDetail.vue')
      }
    ]
  },
  {
    path: '/properties',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'PropertyList',
        component: () => import('@/views/property/PropertyList.vue')
      },
      {
        path: 'add',
        name: 'PropertyAdd',
        component: () => import('@/views/property/PropertyForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'PropertyEdit',
        component: () => import('@/views/property/PropertyForm.vue')
      },
      {
        path: 'detail/:id',
        name: 'PropertyDetail',
        component: () => import('@/views/property/PropertyDetail.vue')
      }
    ]
  },
  {
    path: '/viewing-records',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'ViewingRecordList',
        component: () => import('@/views/viewing/ViewingRecordList.vue')
      },
      {
        path: 'add',
        name: 'ViewingRecordAdd',
        component: () => import('@/views/viewing/ViewingRecordForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'ViewingRecordEdit',
        component: () => import('@/views/viewing/ViewingRecordForm.vue')
      }
    ]
  },
  {
    path: '/vr-panoramas',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'VRPanoramaList',
        component: () => import('@/views/vr/VRPanoramaList.vue')
      },
      {
        path: 'add',
        name: 'VRPanoramaAdd',
        component: () => import('@/views/vr/VRPanoramaForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'VRPanoramaEdit',
        component: () => import('@/views/vr/VRPanoramaForm.vue')
      },
      {
        path: 'view/:id',
        name: 'VRPlayer',
        component: () => import('@/views/vr/VRPlayer.vue')
      }
    ]
  },
  {
    path: '/subscriptions',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'SubscriptionList',
        component: () => import('@/views/subscription/SubscriptionList.vue')
      },
      {
        path: 'add',
        name: 'SubscriptionAdd',
        component: () => import('@/views/subscription/SubscriptionForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'SubscriptionEdit',
        component: () => import('@/views/subscription/SubscriptionForm.vue')
      },
      {
        path: 'detail/:id',
        name: 'SubscriptionDetail',
        component: () => import('@/views/subscription/SubscriptionDetail.vue')
      }
    ]
  },
  {
    path: '/follow-up-records',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'FollowUpRecordList',
        component: () => import('@/views/followup/FollowUpRecordList.vue')
      },
      {
        path: 'add',
        name: 'FollowUpRecordAdd',
        component: () => import('@/views/followup/FollowUpRecordForm.vue')
      }
    ]
  },
  {
    path: '/contracts',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'ContractList',
        component: () => import('@/views/contract/ContractList.vue')
      },
      {
        path: 'add',
        name: 'ContractAdd',
        component: () => import('@/views/contract/ContractForm.vue')
      },
      {
        path: 'edit/:id',
        name: 'ContractEdit',
        component: () => import('@/views/contract/ContractForm.vue')
      },
      {
        path: 'detail/:id',
        name: 'ContractDetail',
        component: () => import('@/views/contract/ContractDetail.vue')
      }
    ]
  },
  {
    path: '/performance',
    component: () => import('@/layout/MainLayout.vue'),
    children: [
      {
        path: 'stats',
        name: 'PerformanceStats',
        component: () => import('@/views/performance/PerformanceStats.vue')
      },
      {
        path: 'targets',
        name: 'MonthlyTargetSetting',
        component: () => import('@/views/performance/MonthlyTargetSetting.vue')
      },
      {
        path: 'evaluation',
        name: 'PerformanceEvaluation',
        component: () => import('@/views/performance/PerformanceEvaluation.vue')
      },
      {
        path: 'ranking',
        name: 'PerformanceRanking',
        component: () => import('@/views/performance/PerformanceRanking.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
