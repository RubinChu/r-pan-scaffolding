/**
 * Created by rubin on 2020/6/26.
 */

'use strict'

import http from '../../utils/http'

let recycleService = {
    recycles: function (resolve, reject) {
        http({
            url: '/recycles',
            params: {},
            method: 'get'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    restoreRecycle: function (data, resolve, reject) {
        http({
            url: '/recycle/restore',
            data: data,
            method: 'put'
        }).then(res => resolve(res)).catch(err => reject(err))
    },
    deleteRecycle: function (data, resolve, reject) {
        http({
            url: '/recycle',
            data: data,
            method: 'delete'
        }).then(res => resolve(res)).catch(err => reject(err))
    }
}

export default recycleService
