import router from "../../router";
import Axios from "axios";
import {endLoading, startLoading} from "../loading"
import {message} from "../message"
import cache from "@/method/cache";

//loading白名单
const loadingWhiteList = [
  "/menu/getList/asideMenus", //侧边菜单
  "/menu/getButton", //获取按钮
  "/user/userImage", //头部头像
]

//默认路径
Axios.defaults.baseURL = 'http://localhost:8888/'

/** 请求拦截器 */
Axios.interceptors.request.use(config => AxiosRequest(config), error => Promise.reject(error))

/** 响应拦截器 */
Axios.interceptors.response.use((res) => AxiosSuccess(res), (res) => AxiosError(res));

/** 请求拦截器 */
export const AxiosRequest  = (config) => {
  if(!loadingWhiteList.includes(config.url)) {
    startLoading()
  }
  // 请求头加载token
  const token = cache.session.get('token')
  const tokenName = cache.session.get('tokenName')
  config.headers[tokenName] = token
  // 请求头加载请求类型
  const uploadUrl = cache.session.get("UploadUrl");
  if (config.url !== uploadUrl) {
    config.headers['Content-Type'] = "application/json" // 请求头带上Content-Type->参数转为json格式
  }
  //请求头携带自定义数据
  config.headers["ip"] = cache.session.get('Ip')
  config.headers["Browser"] = cache.session.get('Browser')
  config.headers["Resolution"] = cache.session.get('Resolution')
  config.headers["colorDepth"] = cache.session.get('colorDepth')
  config.headers["OS"] = cache.session.get('OS')
  return config;
}

/** 相应拦截器-成功 */
export const AxiosSuccess = (res)=>{
  endLoading()
  const response = res.data
  //文本提示
  if (response.message) {
    message({type: response.type, message: response.message});
  }
  //特殊情况
  if (response.code === 11012) {   //token无效
    cache.local.remove("auto");
    router.push("/").then(r => {})
    return;
  }
  if (response.code === 11013) {   //权限不足
    //只弹出提示，不做特殊处理
  }
  return response.data;
}

/** 相应拦截器-失败 */
export const AxiosError = (res)=>{
  endLoading()
  if(res.response === undefined){
    return;
  }
  const response = res.response.data
  if (response.status === 504 ) {
    message.error({message: '服务器请求超时( ╯□╰ )'});
  } else
  if (response.status === 404) {
    const path = Axios.defaults.baseURL + response.path
    const massage = "请求 '"+path+"' 出现错误!"
    message.error({message: massage});
  } else
  if (response.status === 403) {
    message.error({message: '权限不足，请联系管理员'});
  } else
  if (response.status === 500) {
    message.error({message: '服务器遇到错误，无法完成请求。'});
  } else
  if (response.message) {
    message({type: response.type, message: response.message});
  } else {
    message({message: '该错误触及盲区(●ˇ∀ˇ●)'});
  }
}

export default Axios;
