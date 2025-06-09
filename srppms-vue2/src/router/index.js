import Vue from 'vue'
import Router from 'vue-router'
import Axios from "axios";
import store from "@/store";
import permission from "@/method/permission";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {updateButton} from "@/method/button";
import cache from "@/method/cache";

Vue.use(Router)

//静态路由
export const routes = [
  {//登录
    path: '/',
    name: 'login',
    component: () => import('@/components/Login'),
    meta: {
      title: "登录",
      keepAlive: true,// 需要被缓存
    }
  },
  {//注册
    path: '/register',
    name: 'register',
    component: () => import('@/components/Register'),
    meta: {
      title: "注册",
      keepAlive: true,// 需要被缓存
    }
  },
];

//定义新建路由方法
const createRouter = () =>
  new Router({
    mode: "history",
    routes: routes,
  });

//异常路由
export const errorRouter = [
  // {//放在最后
  //   name:"404",
  //   path: '*',
  //   component: () => import('@/components/error/404'),
  // }
]
//新建路由
const router = createRouter()

//后端获取路由
export function getRouter() {
  updateButton()
  Axios.post("/menu/getList/asideMenus").then((res) => {
    permission.mutations.setMenuList(permission.state, res)
    setRouter(res)
  });
}

//路由的装载
export function setRouter(list) {
  //防止list为空报错
  if (list === undefined || list === null) return;

  //重置路由函数
  resetRouter()

  const Home = {
    path: '/Home',
    component: () => import('@/components/Home'),
    redirect: '/',
    children: []
  }
  const menus = []
  list.forEach(menu => {
    if (menu.children !== null) {
      menu.children.forEach(child => {
        menus.push(child)
      })
    }
    menus.push(menu)
  })

  menus.filter(item => item.component !== null)
    .forEach(item => {
    const data = {
      path: item.url,
      name: item["comName"],
      component: (resolve) => require([`@/views${item.component}`], resolve),
      meta: {
        title: item.name,
        keepAlive: true,// 需要被缓存
        parentName: item["parentName"]
      },
    }
      Home.children.push(data)
  })

  //添加动态路由
  router.addRoute(Home)
  //添加异常路由
  errorRouter.forEach(item=> {
    router.addRoute(item)
  })
}

//重置路由函数
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher;
}

/** 使用router钩子函数来处理 */
router.beforeEach((to, from, next) => {
  //进度条
  NProgress.start();
  if (to.matched.length === 0) {
    //刷新路由
    getRouter()
  }
  if (to.path === '/register' && from.path === '/') {
    next();
    return;
  }
  //动态标题
  to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
  const token = cache.session.get('token')
  if (to.path !== '/' && !token) next({path: '/'})
  else {
    //为头皮屑添加元素
    store.dispatch('app/routerChange', to)
    //路由跳转
    next()
  }
})

router.afterEach(() => {
  NProgress.done()
})

// 防止连续点击多次路由报错
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
// push
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
Router.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}


export default router;
