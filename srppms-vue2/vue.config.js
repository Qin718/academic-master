// Vue CLI 工程配置
const {defineConfig} = require('@vue/cli-service')

// 获取本机IP模块
const address = require('address')
// 自动获取本机IP，失败时默认使用本地回环地址
const host = address.ip() || '127.0.0.1'

module.exports = defineConfig({
    // 关闭代码保存时自动lint检查
    lintOnSave: false,
    
    // 转译node_modules中的依赖
    transpileDependencies: true,

    // 开发服务器配置
    devServer: {
        // 绑定到本机IP地址，支持局域网访问
        host: host,
        
        // 开发服务器端口
        port: 8080,
        
        // 自动打开浏览器
        open: true,

        // API代理配置
        proxy: {
            '/api': {
                // 后端服务地址（需与实际后端地址一致）
                target: "http://127.0.0.1:8080",
                
                // 允许跨域请求
                changeOrigin: true,
                
                // 路径重写规则：去除/api前缀
                pathRewrite: {
                    '^/api': ""
                }
            }
        }
    }
})
// const { defineConfig } = require('@vue/cli-service');
// const address = require('address');
// const host = address.ip() || '127.0.0.1'; // 获取本地 IP 地址，或者使用 '127.0.0.1'

// module.exports = defineConfig({
//     lintOnSave: false,  // 禁用代码检查
//     transpileDependencies: true,  // 确保依赖被转译
//     devServer: {
//         host: '192.168.56.1',  // 绑定到本地网络 IP 地址
//         port: 8080,             // 设置服务端口为 8080
//         open: true,             // 自动打开浏览器
//         server: {
//             type: 'https',  // 使用 HTTPS 协议
//         },
//         allowedHosts: "all",    // 允许所有 Host 请求，解决 Invalid Host Header 问题
//         proxy: {
//             '/api': {
//                 target: "http://127.0.0.1:8080",  // 设置代理到本地 8080 服务
//                 changeOrigin: true,  // 允许跨域
//                 pathRewrite: {
//                     '^/api': ""   // 重写路径，去掉 /api 前缀
//                 }
//             }
//         },
//         client: {
//             webSocketURL: `wss://${host}:8080/ws`,  // 使用加密的 WebSocket 协议
//         },
//     },
// });

