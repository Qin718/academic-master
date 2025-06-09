import Vue from "vue";

let loading = null;
let loadingNum=0;
export function startLoading() {
  if(loadingNum === 0){
    loading =  Vue.prototype.$loading({
      lock: true,
      text: '拼命加载中...',
      background:'rgba(255,255,255,0.5)',
      target: document.querySelector('.loading-area')//设置加载动画区域
    })
  }
  loadingNum++;
}
export function endLoading() {
  loadingNum--;
  if (loadingNum <= 0 && loading) {
    loadingNum = 0;
    loading.close();
  }
}

export function globalLoading(options) {
  loading = Vue.prototype.$loading(options)
  loadingNum++;
}

export function closeGlobalLoading() {
  endLoading()
}
