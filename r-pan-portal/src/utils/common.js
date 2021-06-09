/**
 * Created by rubin on 2020/6/6.
 */


'use strict';

import { getToken } from './cookie'

let panUtil = {
    checkUsername(username) {
        return !!username && /^[0-9A-Za-z]{6,16}$/.test(username)
    },
    checkPassword(password) {
        return !!password && password.length >= 8 && password.length <= 16
    },
    formatNumber(n) {
        n = n.toString()
        return n[1] ? n : '0' + n
    },
    formatTime(date) {
        const year = date.getFullYear()
        const month = date.getMonth() + 1
        const day = date.getDate()
        const hour = date.getHours()
        const minute = date.getMinutes()
        const second = date.getSeconds()
        return [year, month, day].map(this.formatNumber).join('-') + ' ' + [hour, minute, second].map(this.formatNumber).join(':')
    },
    // TODO Your file preview uri prefix. example for dev:http://127.0.0.1:7000/preview?fileId=111&authorization=xx15456xwwx
    getPreviewUrl(fileId) {
        return 'http(s)://api-domain:port/preview?fileId=' + fileId + '&authorization=' + getToken()
    },
    // TODO Your http uri prefix. example for dev:http://127.0.0.1:7000/
    getUrlPrefix() {
        return 'http(s)://api-domain:port'
    }
}

export default panUtil
