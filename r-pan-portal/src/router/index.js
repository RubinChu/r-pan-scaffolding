import Vue from 'vue'
import VueRouter from 'vue-router'
import Index from '../views/index/Index'
import Login from '../views/login/Login'
import Register from '../views/register/Register'
import Forget from '../views/forget/Forget'
import Code from "../views/code/Code";
import Office from "../views/office/Office";
import Iframe from "../views/iframe/Iframe";
import Music from "../views/music/Music";
import Video from "../views/video/Video";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from "../utils/cookie"

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Index',
        component: Index
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/forget',
        name: 'Forget',
        component: Forget
    },
    {
        path: '/code/:fileId',
        name: 'Code',
        component: Code
    },
    {
        path: '/office/:fileId',
        name: 'Office',
        component: Office
    },
    {
        path: '/iframe/:fileId',
        name: 'Iframe',
        component: Iframe
    },
    {
        path: '/music/:parentId/:fileId',
        name: 'Music',
        component: Music
    },
    {
        path: '/video/:parentId/:fileId',
        name: 'Video',
        component: Video
    },
    {
        path: '/404',
        component: () => import('@/views/error-page/404')
    },
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

const whiteList = ['/login', '/register', '/forget']

router.beforeEach((to, from, next) => {
    NProgress.start()
    const hasToken = getToken()
    if (hasToken) {
        if (whiteList.indexOf(to.path) !== -1) {
            next({name: 'Index'})
        } else {
            next()
        }
    } else {
        if (whiteList.indexOf(to.path) !== -1) {
            next()
        } else {
            next({name: 'Login'})
        }
    }
    NProgress.done()
})

router.afterEach(() => {
    NProgress.done()
})

export default router
