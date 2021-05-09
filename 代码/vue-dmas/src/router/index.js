import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/features/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'Home',
      component: () => import('@/views/home'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    path: '/member',
    component: Layout,
    redirect: '/member/manage',
    meta: { title: '团队', icon: 'team' },
    children: [
      {
        path: 'manage',
        name: 'Member',
        component: () => import('@/views/member/manage/index'),
        meta: { title: '信息管理', icon: 'info-member' }
      },
      {
        path: 'upload',
        name: 'UploadMember',
        component: () => import('@/views/member/uploadMember/index'),
        meta: { title: '新增队员', icon: 'add-member' }
      }
    ]
  },

  {
    path: '/contest',
    component: Layout,
    redirect: '/contest/manage',
    name: 'Contest',
    meta: {
      title: '比赛', icon: 'contest'
    },
    children: [
      {
        path: 'manage',
        component: () => import('@/views/contest/manage/index'),
        name: 'Manage',
        meta: { title: '数据管理', icon: 'manage' }
      },
      {
        path: 'upload',
        component: () => import('@/views/contest/uploadContest/index'),
        name: 'UploadContest',
        meta: { title: '数据上传', icon: 'upload' }
      },
      {
        path: 'detail',
        component: () => import('@/views/contest/detail/index'),
        name: 'ContestDetail',
        hidden: true
      }
    ]
  },

  {
    path: '/analysis',
    component: Layout,
    redirect: '/analysis/member',
    meta: { title: '数据分析', icon: 'analysis' },
    children: [
      {
        path: 'member',
        component: () => import('@/views/analysis/member/index'),
        name: 'AnalysisMember',
        meta: { title: '队员', icon: 'member' }
      },
      {
        path: 'team',
        component: () => import('@/views/analysis/team/index'),
        name: 'AnalysisTeam',
        meta: { title: '团队', icon: 'team' }
      },
      {
        path: '/member/nowcoder/detail',
        name: 'AnalysisMemberNowCoderDetail',
        component: () => import('@/views/analysis/member/detail/nowcoderDetail/index'),
        hidden: true
      },
      {
        path: 'member/exam/detail',
        name: 'AnalysisMemberExamDetail',
        component: () => import('@/views/analysis/member/detail/examDetail/index'),
        hidden: true
      },
      {
        path: 'member/detail',
        name: 'AnalysisMemberDetail',
        component: () => import('@/views/analysis/member/detail/analysisDetail/index'),
        hidden: true
      }
    ]
  },

  {
    path: '/award/detail',
    component: Layout,
    redirect: '/award/detail',
    children: [
      {
        path: 'detail',
        component: () => import('@/views/award/detail/index'),
        name: 'AwardDetail',
        meta: { title: '历年获奖', icon: 'award' }
      }
    ]
  },

  {
    path: '/award/upload',
    component: Layout,
    redirect: '/award/upload',
    children: [
      {
        path: 'upload',
        component: () => import('@/views/award/upload/index'),
        name: 'AwardUpload',
        meta: { title: '再添奖项', icon: 'upload-prize' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://www.cnblogs.com/zeyfra/',
        meta: { title: '友情链接', icon: 'link' }
      }
    ]
  },

  // 404页面必须放到最后
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
