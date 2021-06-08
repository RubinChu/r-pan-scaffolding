import Vue from 'vue'
import VueRouter from 'vue-router'
import Docs from '../views/list-page/doc'
import Files from '../views/list-page/file'
import Imgs from '../views/list-page/img'
import Musics from '../views/list-page/music'
import Recycles from '../views/list-page/recycle'
import Shares from '../views/list-page/share'
import Videos from '../views/list-page/video'
import Layout from '../layout'
import Login from '../views/login'
import Register from '../views/register'
import Forget from '../views/forget'
import Share from '../views/share'
import PreviewCode from '../views/preview/code'
import PreviewOffice from '../views/preview/office'
import PreviewIframe from '../views/preview/iframe'
import PreviewMusic from '../views/preview/music'
import PreviewVideo from '../views/preview/video'
import Error404 from '../views/error-page/404'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken} from "../utils/cookie"

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Index',
        component: Layout,
        children: [
            {
                path: '/docs',
                name: 'Docs',
                component: Docs
            },
            {
                path: '/files',
                name: 'Files',
                component: Files
            },
            {
                path: '/imgs',
                name: 'Imgs',
                component: Imgs
            },
            {
                path: '/musics',
                name: 'Musics',
                component: Musics
            },
            {
                path: '/recycles',
                name: 'Recycles',
                component: Recycles
            },
            {
                path: '/shares',
                name: 'Shares',
                component: Shares
            },
            {
                path: '/videos',
                name: 'Videos',
                component: Videos
            }
        ]
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
        path: '/share/:shareId',
        name: 'Share',
        component: Share
    },
    {
        path: '/preview/code/:fileId',
        name: 'PreviewCode',
        component: PreviewCode
    },
    {
        path: '/preview/office/:fileId',
        name: 'PreviewOffice',
        component: PreviewOffice
    },
    {
        path: '/preview/iframe/:fileId',
        name: 'PreviewIframe',
        component: PreviewIframe
    },
    {
        path: '/preview/music/:parentId/:fileId',
        name: 'PreviewMusic',
        component: PreviewMusic
    },
    {
        path: '/preview/video/:parentId/:fileId',
        name: 'PreviewVideo',
        component: PreviewVideo
    },
    {
        path: '/404',
        component: Error404
    },
    {path: '*', redirect: '/404', hidden: true}
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

const toIndexPageList = ['Login', 'Register', 'Forget'],
    whiteList = ['Login', 'Register', 'Forget', 'Share']

router.beforeEach((to, from, next) => {
    NProgress.start()
    const hasToken = getToken()
    if (hasToken) {
        if (toIndexPageList.indexOf(to.name) !== -1) {
            next({name: 'Index'})
        } else {
            next()
        }
    } else {
        if (whiteList.indexOf(to.name) !== -1) {
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
