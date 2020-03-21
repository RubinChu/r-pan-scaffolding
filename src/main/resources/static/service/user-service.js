/**
 * Created by rubin on 2019/9/7.
 * version 1.0.0
 * 首页请求处理js
 */

'use strict';

var userService = {

    // 用户注册
    register: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/user/register',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    },
    // 用户登录
    login: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/user/login',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    },
    // 用户退出登录
    exit: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/user/exit',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    }

}