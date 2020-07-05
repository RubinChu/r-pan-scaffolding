/**
 * Created by rubin on 2020/6/26.
 */

'use strict';

import http from "../../utils/http";

let fileService = {
    list: function (params, resolve, reject) {
        http({
            url: '/file/list',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    createDirectory: function (data, resolve, reject) {
        http({
            url: '/file/directory/create',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    update: function (data, resolve, reject) {
        http({
            url: '/file/update',
            data: data,
            method: 'put'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    delete: function (data, resolve, reject) {
        http({
            url: '/file/delete',
            data: data,
            method: 'delete'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    download: function (data, resolve, reject) {
        http({
            url: '/file/download',
            params: data,
            method: 'post',
            timeout: 60000,
            responseType: 'blob'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    getDirectoryTree: function (params, resolve, reject) {
        http({
            url: '/file/directory/tree',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    transfer: function (data, resolve, reject) {
        http({
            url: '/file/transfer',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    copy: function (data, resolve, reject) {
        http({
            url: '/file/copy',
            data: data,
            method: 'post'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    search: function (params, resolve, reject) {
        http({
            url: '/file/search',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    getBreadcrumbs: function (params, resolve, reject) {
        http({
            url: '/file/breadcrumbs',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    detail: function (params, resolve, reject) {
        http({
            url: '/file/detail',
            params: params,
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    }
}

export default fileService
