import Vue from 'vue'

import 'normalize.css/normalize.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss'

import App from './App'
import store from './store'
import router from './router'

import '@/icons'
import '@/permission'

// 引入 echarts 核心模块，核心模块提供了 echarts 使用必须要的接口。
import * as echarts from 'echarts/core'

import { BarChart, LineChart, RadarChart, PieChart, ScatterChart } from 'echarts/charts'

// 引入提示框，标题，直角坐标系组件，组件后缀都为 Component
import { TitleComponent, TooltipComponent, GridComponent,
  LegendComponent, DatasetComponent, TransformComponent,
  ToolboxComponent, DataZoomComponent } from 'echarts/components'

// // 引入 Canvas 渲染器，注意引入 CanvasRenderer 或者 SVGRenderer 是必须的一步
import {
  CanvasRenderer
} from 'echarts/renderers'

// 注册组件
echarts.use(
  [TitleComponent, TooltipComponent, GridComponent, CanvasRenderer,
    PieChart, LegendComponent, DatasetComponent, RadarChart, BarChart,
    TransformComponent, LineChart, ToolboxComponent, DataZoomComponent, ScatterChart]
)

Vue.prototype.$echarts = echarts

Vue.use(ElementUI, {
  size: 'medium'
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
