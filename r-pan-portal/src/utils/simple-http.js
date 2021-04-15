/**
 * Created by rubin on 2020/6/4.
 */

'use strict'

import axios from 'axios'
import { clearToken, getToken, getShareToken, clearShareToken } from './cookie'
import { Message } from 'element-ui'
import panUtil from './common'

const simpleHttp = axios.create({
        baseURL: panUtil.getUrlPrefix(),
        timeout: 1000 * 30,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    })

simpleHttp.interceptors.request.use(config => {
    if (config.data) {
        config.data = JSON.stringify(config.data)
    }
    let token = getToken(),
        shareToken = getShareToken()
    if (token) {
        config.headers['Authorization'] = token
    }
    if (shareToken) {
        config.headers['Share-Token'] = shareToken
    }
    return config
}, error => {
    Message.error('请求失败')
    console.error(error)
    return
})

simpleHttp.interceptors.response.use(res => {
    return res.data
}, error => {
    Message.error('请求失败')
    console.error(error)
    return
})

export default simpleHttp
