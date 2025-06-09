import Vue from "vue";
import pako from "pako";
import {exportToExcel} from "../excel/Export2Excel";
import {closeGlobalLoading, globalLoading} from "../loading";
import "@/method/button"
import cache from "@/method/cache";


/** 压缩  */
Vue.prototype.$zip = function (str) {
  const binaryString = pako.gzip(encodeURIComponent(str), {to: 'string'})
  return btoa(binaryString);
};

/** 解压缩  */
Vue.prototype.$unzip = function (b64Data) {
  const strData1 = atob(b64Data);
  // 将二进制字符串转换为字符数数组
  const charData = strData1.split("").map(function (x) {
    return x.charCodeAt(0);
  });
  // 把数字数组转换成字节数组
  const binData = new Uint8Array(charData);
  // 解压
  const data = pako.inflate(binData);
  //将解压缩完成的的byteArray转换回ascii字符串:
  const strData2 = new TextDecoder("utf-8").decode(data);//大数据使用此方法
  //const strData   = String.fromCharCode.apply(null, new Uint16Array(data));//数据不是很多可以使用此方法
  return decodeURIComponent(strData2);
};

/** 分页方法 */
Vue.prototype.$getPageInfo = function (pageSize, pageNum) {
  return {
    "pageSize": pageSize, "pageNum": pageNum
  }
};

/** 后端返回的分页信息 */
Vue.prototype.$cutPageDataList = function (response) {
  const data = {
    "dataList": [],
    "totalPage": 0
  }
  if (response !== null && response !== undefined) {
    data['dataList'] = response.list
    data['totalPage'] = response.total
  }
  return data;
};

/** 字符串转JSON */
Vue.prototype.$StrToJson = function (res) {
  const type = [];
  res.forEach(r => type.push(eval('(' + r["dict"] + ')')))
  return type;
};

/** 赋值对象 */
Vue.prototype.$CopyObject = function (res) {
  const copyObject = {};
  for (const i in res) {
    copyObject[i] = res[i]
  }
  return copyObject;
};

/** 下载文件 */
Vue.prototype.$DownloadFile = function (name, stream) {
  const bstr = atob(stream);
  let n = bstr.length;
  const u8arr = new Uint8Array(n)
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n)
  }
  const blob = new Blob([u8arr], {type: 'application/vnd.ms-excel'})
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = URL.createObjectURL(blob)
  link.setAttribute('download', name)
  document.body.appendChild(link)
  link.click()
  this.loading = false
};

/** 获取分页尺寸 */
Vue.prototype.$getPageSize = function () {
  return eval(cache.session.get("pageSize"))
};

/** 表格数据写入excel */
Vue.prototype.$exportExcel = function (res) {
  const tHeader = res["headers"]; // 导出的excel表头名信息
  const filterVal = res["names"]; // 导出的excel表头字段名，需要导出表格字段名
  const data = res["list"].map(v => filterVal.map(j => v[j]));
  exportToExcel(tHeader, data, res["fileName"]); // 导出的表格名称，根据需要自己命名
};

/** 设置全局设置 */
Vue.prototype.$setGlobalConfig = function (res) {
  for (let key in res) {
    //页号
    if (key === "pageSize") {
      const page = []
      res["pageSize"].forEach(item => {
        page.push(Number(item.value))
      })
      page.sort(function (a, b) {
        return a - b;
      });
        cache.session.set("pageSize", "[" + page + "]");
    }
    //hot
    if (key === "hot") {
      const value = res["hot"][0]["value"]
        cache.session.set("hot", value);
    }
  }
};

/** 全局loading */
Vue.prototype.$GlobalLoading = function (b, options) {
  if (b) {
    globalLoading(options)
  } else {
    closeGlobalLoading()
  }
};

/** 浏览器 */
Vue.prototype.$getBrowser = function () {
  const userAgent = window.navigator.userAgent;
  const isIE = userAgent.indexOf("MSIE ") > -1 || userAgent.indexOf("Trident/") > -1;
  const isEdge = userAgent.indexOf("Edg/") > -1;
  const isChrome = userAgent.indexOf("Chrome/") > -1;
  const isFirefox = userAgent.indexOf("Firefox/") > -1;
  const isSafari = userAgent.indexOf("Safari/") > -1;
  let Browser;
  if (isIE) {
    Browser = "Internet Explorer";
  } else if (isEdge) {
    Browser = "Microsoft Edge";
  } else if (isChrome) {
    Browser = "Google Chrome";
  } else if (isFirefox) {
    Browser = "Mozilla Firefox";
  } else if (isSafari) {
    Browser = "Apple Safari";
  } else {
    Browser = "Unknown Browser";
  }
  return Browser;
};

/** 屏幕分辨率 */
Vue.prototype.$getResolution = function () {
  const screenWidth = window.screen.width;
  const screenHeight = window.screen.height;
  return `${screenWidth} x ${screenHeight}`
};

/** 颜色深度 */
Vue.prototype.$getColorDepth = function () {
  return window.screen.colorDepth
};

/** 操作系统 */
Vue.prototype.$getOS = function () {
  const userAgent = window.navigator.userAgent;
  const platform = window.navigator.platform;
  const os = {
    android: /Android/.test(userAgent),
    ios: /iPhone|iPad|iPod/.test(userAgent),
    macos: /Mac/.test(platform),
    windows: /Win/.test(platform),
    linux: /Linux/.test(platform)
  };

  if (os.android) {
    return "Android";
  } else if (os.ios) {
    return "iOS";
  } else if (os.macos) {
    return "macOS";
  } else if (os.windows) {
    return "Windows";
  } else if (os.linux) {
    return "Linux";
  } else {
    return "Unknown Operating System";
  }
};

/** 获取本机的IP地址 */
Vue.prototype.$getIp = function () {
  return window.location.hostname;
};

/** 封装上传文件方法 */
Vue.prototype.$UploadFile = function (url,files) {
  cache.session.set("UploadUrl", url);
  let formData = new FormData();

  if(files.length === undefined) {
    formData.append('files', files)
  }else {
    files.forEach(item => {
      formData.append("files", item.raw);
    });
  }
  return this.$http.post(url, formData, {headers: {"Content-Type": "multipart/form-data",}});
};

/** 封装获取表头的方法 */
Vue.prototype.$getProp = function (url) {
  const data = JSON.parse(cache.session.get(url))
  if (data !== null && data.length !== 0) {
    return data;
  }
  const list = []
  this.$http.post(url).then((res) => {
    if (res === null || res === undefined) {
      return [];
    }
    res = res.sort(function (a, b) {
      return a.index - b.index;
    });
    res.forEach(item => {
      item["width"] = item.label.length * 50
      list.push(item)
    })
      cache.session.set(url, JSON.stringify(res))
  })
  return list;
};
