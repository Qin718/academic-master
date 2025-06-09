import Vue from "vue";
import Axios from "axios";

export const button = []


//更新按钮集合
export function updateButton() {
  Axios.post("/menu/getButton").then((res) => {
    if (res !== null && res !== undefined) {
      res.forEach(item => button.push(item))
    }
  });
}

Vue.directive("button", {
  bind(el, binding) {
    const value = binding.value
    button.forEach(item => {
      if (item.url === value) {
        // 图标
        const i = el.querySelector('i')
        if(i === null){
          const icon = document.createElement("i");
          icon.setAttribute("class", `el-icon-` + item.icon);
          if(el.children.length === 0){
            el.appendChild(icon)
          }else {
            el.children[0].before(icon)
          }
        }
        // 文字
        const span_ = el.querySelector('span')
        if(span_ !== null){
          span_.innerHTML = item.name
        }else {
          const span = document.createElement("span");
          span.innerHTML = item.name
          el.appendChild(span)
        }
        if (item.isShow !== '显示') {
          el.style.display = 'none'
        } else {
          el.style.display = ""
        }
      }
    })
  }
})
