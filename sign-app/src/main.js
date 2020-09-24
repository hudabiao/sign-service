import Vue from 'vue'
import ELEMENT from 'element-ui';
import axios from 'axios';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import router from './router';
import config from './config.js';
// import 'element-ui/lib/theme-chalk/display.css';

axios.defaults.baseURL = config.url.server_url;
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    // 登录失效，跳转到登录页面
    if(error.response.status === 401){
        window.location.href = config.url.client_url;
    }
    return Promise.reject(error);
});
Vue.use(ELEMENT);
Vue.config.productionTip = false;
Vue.prototype.$axios = axios;
Vue.prototype.config = config;

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
