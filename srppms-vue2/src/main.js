import Vue from 'vue'
import App from './App'
import ElementUI, {Table, TableColumn} from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import store from './store'
import '@/method/global'
import Axios from '@/method/axios'
import * as echarts from 'echarts'
import "@/assets/css/main.css"
import 'default-passive-events'

//注册方法
Vue.prototype.$http = Axios
Vue.prototype.$echarts = echarts
Vue.config.productionTip = false
Vue.use(ElementUI)


//注册组件
Vue.component("RightToolbar", () => import("@/components/utils/RightToolbar"))
Vue.component("printTable", () => import("@/components/utils/printTable"))


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app")

// 获取组件的props
const TableProps = Table.props
const TableColumnProps = TableColumn.props

// 修改默认props
// 全局el-table设置
TableProps.border.default = true // 边框
// 全局el-table-column设置
TableColumnProps.align.default = 'center' // 居中
TableColumnProps.showOverflowTooltip.default = true // 文本溢出

TableProps.border = {type: Boolean, default: true} // 边框
