// import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

// Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/sign/:loginId/:contractId',
        name: 'Sign',
        component: () => import('../views/Sign.vue')
    }, {
        path: '/home',
        name: 'Home',
        component: Home
    },
    {
        path: '/about/:loginId/:contractId/:status',
        name: 'About',
        component: () => import('../views/About.vue')
    },
    {
        path: '/userContract',
        name: 'UserContract',
        component: () => import('../views/UserContract.vue')
    }
];

const router = new VueRouter({
    routes
});

export default router
