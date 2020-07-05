/**
 * Created by rubin on 2020/6/6.
 */

'use strict';

import http from "../../utils/http";

let userService = {
    login: function (data, resolve, reject) {
        http({
            url: '/user/login',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    register: function (data, resolve, reject) {
        http({
            url: '/user/register',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    info: function (resolve, reject) {
        http({
            url: '/user/info',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    checkUsername: function (data, resolve, reject) {
        http({
            url: '/user/username/check',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    checkAnswer: function (data, resolve, reject) {
        http({
            url: '/user/answer/check',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    resetPassword: function (data, resolve, reject) {
        http({
            url: '/user/password/reset',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    changePassword: function (data, resolve, reject) {
        http({
            url: '/user/password/change',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    exit: function (resolve, reject) {
        http({
            url: '/user/exit',
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    }
}

export default userService
