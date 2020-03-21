/**
 * Created by rubin on 2019/9/7.
 * version 1.0.0
 * 首页请求处理js
 */

'use strict';

var fileService = {

    // 获取文件列表
    getFiles: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/file/getFiles',
            data    : receiverInfo,
            method  : 'GET',
            success : resolve,
            error   : reject
        })
    },
    // 创建文件夹
    createDirectory: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/file/createDirectory',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    },
    // 修改文件名
    updateFileName: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/file/updateFileName',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    },
    // 删除文件(批量)
    deleteFile: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/file/deleteFile',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    },
    // 下载文件
    downloadFile: function (receiverInfo) {
        var url = '/file/downloadFile'
        var form = $('<form></form>').attr('action', url).attr('method', 'post')
        form.append($('<input/>').attr('type', 'hidden').attr('name', 'filePath').attr('value', receiverInfo.filePath))
        form.append($('<input/>').attr('type', 'hidden').attr('name', 'fileNames').attr('value', receiverInfo.fileNames))
        form.appendTo('body').submit().remove()
    },
    // 下载文件
    getFolders: function (resolve, reject) {
        panUtil.request({
            url     : '/file/getFolders',
            data    : {},
            method  : 'GET',
            success : resolve,
            error   : reject
        })
    },
    // 下载文件
    transferFile: function (receiverInfo, resolve, reject) {
        panUtil.request({
            url     : '/file/transferFile',
            data    : receiverInfo,
            method  : 'POST',
            success : resolve,
            error   : reject
        })
    }

}