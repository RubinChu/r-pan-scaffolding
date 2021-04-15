/**
 * Created by rubin on 2020/6/26.
 */

'use strict'

import http from '../../utils/http'
import simpleHttp from '../../utils/simple-http'

let shareService = {
    getShareDetail: function (resolve) {
        simpleHttp({
            url: '/share',
            params: {},
            method: 'get'
        }).then(res => resolve(res))
    },
    createShare: function (data, resolve, reject) {
        http({
            url: '/share',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    cancelShare: function (data, resolve, reject) {
        http({
            url: '/share',
            data: data,
            method: 'delete'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    checkShareCode: function (data, resolve) {
        simpleHttp({
            url: '/share/code/check',
            data: data,
            method: 'post'
        }).then(res => resolve(res))
    },
    getShareFiles: function (params, resolve) {
        simpleHttp({
            url: '/share/file/list',
            params: params,
            method: 'get'
        }).then(res => resolve(res))
    },
    saveShareFiles: function (data, resolve) {
        simpleHttp({
            url: '/share/save',
            data: data,
            method: 'post'
        }).then(res => resolve(res))
    },
    getShares: function (resolve, reject) {
        http({
            url: '/shares',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    getSimpleShareDetail: function (params, resolve) {
        simpleHttp({
            url: '/share/simple',
            params: params,
            method: 'get'
        }).then(res => resolve(res))
    }
}

export default shareService
