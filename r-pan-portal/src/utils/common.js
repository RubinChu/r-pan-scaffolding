/**
 * Created by rubin on 2020/6/6.
 */

'use strict';

let panUtil = {
    checkUsername: function (username) {
        return !!username && /^[0-9A-Za-z]{6,16}$/.test(username)
    },
    checkPassword: function (password) {
        return !!password && password.length >= 8 && password.length <= 16
    },
    formatNumber: function (n) {
        n = n.toString()
        return n[1] ? n : '0' + n
    },
    formatTime: function (date) {
        const year = date.getFullYear()
        const month = date.getMonth() + 1
        const day = date.getDate()
        const hour = date.getHours()
        const minute = date.getMinutes()
        const second = date.getSeconds()
        return [year, month, day].map(this.formatNumber).join('-') + ' ' + [hour, minute, second].map(this.formatNumber).join(':')
    },
    // TODO modify to service access path
    getUrlPrefix: function () {
        return 'http://XXXXXXX/api'
    }
}

export default panUtil
