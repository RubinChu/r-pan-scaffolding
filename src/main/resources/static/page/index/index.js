/**
 * Created by rubin on 2019/9/7.
 * version: 1.0.1
 */

'use strict';
var page

layui.config({
    base: '/plugin/layui/layui_ext/'
}).use(['element', 'upload', 'dtree'], function () {
    var element = layui.element,
        upload = layui.upload,
        dtree = layui.dtree
    page = {
        // 页面缓存对象
        data: {
            progress: new Object()
        },
        // 页面启动入口
        start: function () {
            this.onLoad()
            this.bindEvent()
        },
        // 页面初始化
        onLoad: function () {
            panUtil.initUserName()
            this.loadFileList()
            this.initUploader()
            this.initTreeComponent()
        },
        // 事件绑定
        bindEvent: function () {
            var _this = this
            $('#previous_btn').click(function () {
                var breadcrumbText = $('#breadcrumb_label').html()
                if (breadcrumbText == '/') {
                    panUtil.showErrorMsg('没有上一级目录了')
                    return
                }
                breadcrumbText = breadcrumbText.substring(0, breadcrumbText.lastIndexOf('/'))
                if (!breadcrumbText) {
                    breadcrumbText = '/'
                }
                $('#breadcrumb_label').html('').html(breadcrumbText)
                _this.loadFileList()
            })
            $('#check_all_btn').click(function () {
                $('.file-list-item-content').each(function () {
                    if (!$(this).hasClass('check')) {
                        $(this).addClass('check')
                    }
                })
                $(this).addClass('hide')
                $('#recheck_all_btn').removeClass('hide')
            })
            $('#recheck_all_btn').click(function () {
                $('.file-list-item-content').each(function () {
                    if ($(this).hasClass('check')) {
                        $(this).removeClass('check')
                    }
                })
                $(this).addClass('hide')
                $('#check_all_btn').removeClass('hide')
            })
            $('#download_btn').on('mouseover', function () {
                layer.tips('文件打包可能比较耗时<br/>点击下载之后请勿操作页面!', this, {
                    tips: 1
                })
            })
            $('#download_btn').on('mouseout', function () {
                layer.closeAll('tips')
            })
            $('#download_btn').click(function () {
                var $checkDom = $('.file-list-item-content.check')
                if ($checkDom.length == 0) {
                    panUtil.showErrorMsg('请选择要下载的文件')
                    return
                }
                var fileNameArr = new Array()
                $checkDom.each(function () {
                    fileNameArr.push($(this).find('.file-list-item-file-name').attr('title'))
                })
                fileService.downloadFile({
                    filePath: $('#breadcrumb_label').html(),
                    fileNames: fileNameArr.join('__,__')
                })
                $('#check_all_btn').removeClass('hide')
                $('#recheck_all_btn').addClass('hide')
                _this.loadFileList()
            })
            $('#new_folder_btn').click(function () {
                layer.prompt({
                    formType: 0,
                    value: '',
                    title: '文件夹名称',
                    success: function (layero, index) {
                        var $elem = $(layero).find('input[type="text"]')
                        $elem.on('keydown', function (e) {
                            if (13 == e.which) {
                                _this.submitNewFolder(index, $elem.get(0))
                            }
                        })
                    }
                }, function (value, index, elem) {
                    _this.submitNewFolder(index, elem)
                })
            })
            $('#rename_btn').click(function () {
                var $checkDom = $('.file-list-item-content.check')
                if ($checkDom.length == 0) {
                    panUtil.showErrorMsg('请选择要重命名的文件')
                    return
                }
                if ($checkDom.length > 1) {
                    panUtil.showErrorMsg('请选择一个要重命名的文件')
                    return
                }
                var oldName = $checkDom.find('.file-list-item-file-name').attr('title')
                layer.prompt({
                    formType: 0,
                    value: oldName,
                    title: '重命名',
                    success: function (layero, index) {
                        var $elem = $(layero).find('input[type="text"]')
                        $elem.on('keydown', function (e) {
                            if (13 == e.which) {
                                _this.submitRename(index, $elem.get(0), oldName)
                            }
                        })
                    }
                }, function (value, index, elem) {
                    _this.submitRename(index, elem, oldName)
                })
            })
            $('#delete_btn').click(function () {
                var $checkDom = $('.file-list-item-content.check')
                if ($checkDom.length == 0) {
                    panUtil.showErrorMsg('请选择要删除的文件')
                    return
                }
                var fileNameArr = new Array()
                $checkDom.each(function () {
                    fileNameArr.push($(this).find('.file-list-item-file-name').attr('title'))
                })
                layer.confirm('文件删除后将不可恢复,你确定要这样做吗?', function (index) {
                    fileService.deleteFile({
                        filePath: $('#breadcrumb_label').html(),
                        fileNames: fileNameArr.join('__,__')
                    }, function (data) {
                        layer.close(index)
                        _this.reloadFileListDom(data)
                    }, function (error) {
                        panUtil.showErrorMsg(error)
                    })
                })
            })
            $('#refresh_btn').click(function () {
                layer.load()
                _this.loadFileList()
            })
            $('#transfer_file_btn').click(function () {
                var $checkDom = $('.file-list-item-content.check')
                if ($checkDom.length == 0) {
                    panUtil.showErrorMsg('请选择要转移的文件')
                    return
                }
                fileService.getFolders(function (data) {
                    dtree.reload(_this.data.dTreeObj, {
                        data: data
                    })
                    layer.open({
                        type: 1,
                        title: '文件转移',
                        content: $('#file_transfer_folder_content'),
                        area: ['500px', '500px'],
                        btn: ['确认转移', '取消'],
                        btnAlign: 'c',
                        closeBtn: 1,
                        shadeClose: true,
                        success: function (layero, index) {

                        },
                        yes: function (index, layero) {
                            layer.load()
                            var targetFolder = dtree.getNowParam(_this.data.dTreeObj).parentId,
                                filePath = $('#breadcrumb_label').html(),
                                fileNameArr = new Array()
                            if (!targetFolder) {
                                panUtil.showErrorMsg('请选择转移目录')
                                layer.closeAll('loading')
                                return
                            }
                            $checkDom.each(function () {
                                fileNameArr.push($(this).find('.file-list-item-file-name').attr('title'))
                            })
                            fileService.transferFile({
                                filePath: filePath,
                                fileNames: fileNameArr.join('__,__'),
                                targetFolder: targetFolder
                            }, function (data) {
                                layer.closeAll()
                                panUtil.showSuccessMsg('转移成功')
                                _this.reloadFileListDom(data)
                            }, function (error) {
                                layer.closeAll('loading')
                                panUtil.showErrorMsg(error)
                            })
                        },
                        end: function () {
                            dtree.reload(_this.data.dTreeObj, {
                                data: new Array()
                            })
                        }
                    })
                }, function (error) {
                    panUtil.showErrorMsg(error)
                })

            })
        },
        // 加载文件列表
        loadFileList: function () {
            var _this = this
            fileService.getFiles({
                filePath: $('#breadcrumb_label').html()
            }, function (data) {
                _this.reloadFileListDom(data)
            }, function (error) {
                layer.closeAll('loading')
                panUtil.showErrorMsg('获取文件列表失败')
            })
        },
        // 渲染文件列表DOM
        reloadFileListDom: function (data) {
            var domStr = '', _this = this
            layui.each(data, function (index, item) {
                var fileNameWithoutTag = item.fileName.substring(0, item.fileName.lastIndexOf('.'))
                domStr += '<div class="file-list-item-content" data-type="'
                domStr += item.type
                domStr += '" onclick="page.checkThis(this)">'
                domStr += '<div class="file-list-item-file-icon-content">'
                domStr += _this.getFileIcon(item.type)
                domStr += '</div>'
                domStr += '<div class="file-list-item-file-name-content">'
                domStr += '<span class="file-list-item-file-name" title="'
                domStr += item.fileName
                domStr += '" data-type="'
                domStr += item.type
                domStr += '" data-path="'
                domStr += item.fileShowPath
                domStr += '" onclick="page.showMe(this)">'
                domStr += fileNameWithoutTag.length > 15 ? fileNameWithoutTag.substring(0, 16) + '...' : item.fileName
                domStr += '</div>'
                domStr += '</div>'
            })
            $('.file-list-content').empty().append(domStr)
            this.checkSelectAll()
            layer.closeAll('loading')
        },
        // 根据文件后缀获取文件类型
        getFileIcon: function (type) {
            var tagStr = '<a href="#" class="fa fa-file"/>'
            switch (type) {
                case 0:
                    tagStr = '<a href="#" class="fa fa-folder" ondblclick="page.goin(this)"/>'
                    break
                case 2:
                    tagStr = '<a href="#" class="fa fa-file-archive-o"/>'
                    break
                case 3:
                    tagStr = '<a href="#" class="fa fa-file-excel-o"/>'
                    break
                case 4:
                    tagStr = '<a href="#" class="fa fa-file-word-o"/>'
                    break
                case 5:
                    tagStr = '<a href="#" class="fa fa-file-pdf-o"/>'
                    break
                case 6:
                    tagStr = '<a href="#" class="fa fa-file-text-o"/>'
                    break
                case 7:
                    tagStr = '<a href="#" class="fa fa-file-image-o"/>'
                    break
                case 8:
                    tagStr = '<a href="#" class="fa fa-file-audio-o"/>'
                    break
                case 9:
                    tagStr = '<a href="#" class="fa fa-file-video-o"/>'
                    break
                case 10:
                    tagStr = '<a href="#" class="fa fa-file-powerpoint-o"/>'
                    break
                case 11:
                    tagStr = '<a href="#" class="fa fa-file-code-o"/>'
                    break
                default:
                    break
            }
            return tagStr
        },
        // 确认提交创建新文件夹
        submitNewFolder: function(index, elem) {
            layer.load()
            var value = $(elem).val(),
                folderName = $.trim(value),
                _this = this
            if (!folderName) {
                panUtil.showErrorMsg('请输入文件夹名称')
                $(elem).val('')
                layer.closeAll('loading')
                return
            }
            fileService.createDirectory({
                filePath: $('#breadcrumb_label').html(),
                folderName: folderName
            }, function (data) {
                _this.reloadFileListDom(data)
                layer.closeAll()
            }, function (error) {
                layer.closeAll('loading')
                panUtil.showErrorMsg(error)
            })
        },
        // 确认提交新文件夹名称
        submitRename: function(index, elem, oldName) {
            layer.load()
            var value = $(elem).val(),
                folderName = $.trim(value),
                _this = this
            if (!folderName) {
                panUtil.showErrorMsg('请输入新名称')
                $(elem).val(oldName)
                layer.closeAll('loading')
                return
            }
            fileService.updateFileName({
                filePath: $('#breadcrumb_label').html(),
                oldName: oldName,
                newName: value
            }, function (data) {
                _this.reloadFileListDom(data)
                layer.closeAll()
            }, function (error) {
                layer.closeAll('loading')
                panUtil.showErrorMsg(error)
            })
        },
        showMe: function(obj) {
            // 阻止事件冒泡
            event.stopPropagation()
            var type = $(obj).data('type'),
                _this = this
            switch (type) {
                case 3:
                    _this.showOffice(obj)
                    break
                case 4:
                    _this.showOffice(obj)
                    break
                case 10:
                    _this.showOffice(obj)
                    break
                case 5:
                    _this.showPdf(obj)
                    break
                case 6:
                    _this.showTxt(obj)
                    break
                case 7:
                    _this.showImg(obj)
                    break
                case 8:
                    _this.showSound(obj)
                    break
                case 9:
                    _this.showVideo(obj)
                    break
                case 11:
                    _this.showCode(obj)
                    break
                default:
                    break
            }
        },
        // 选中点击事件
        checkThis: function (obj) {
            if ($(obj).hasClass('check')) {
                $(obj).removeClass('check')
            } else {
                $(obj).addClass('check')
            }
            this.checkSelectAll()
        },
        // 跳转至iframe页面
        goIframePage: function(obj, isOffice) {
            var fileShowPath = $(obj).data('path'),
                url = '/iframe?fileShowPath='
            if (isOffice) {
                fileShowPath = 'https://view.officeapps.live.com/op/view.aspx?src=' + fileShowPath
            }
            url += fileShowPath
            window.open(encodeURI(url))
        },
        // office文档预览
        showOffice: function(obj) {
            this.goIframePage(obj, true)
        },
        // txt文档预览
        showTxt: function(obj) {
            this.goIframePage(obj)
        },
        // 音频文件预览
        showSound: function(obj) {
            var fileName = $(obj).attr('title'),
                filePath = $('#breadcrumb_label').html(),
                fileType = $(obj).data('type'),
                url = '/music?filePath=' + filePath + '&fileName=' + fileName + '&fileType=' + fileType
            window.open(encodeURI(url))
        },
        // pdf文件预览
        showPdf: function(obj) {
            this.goIframePage(obj)
        },
        // 视频文件预览
        showVideo: function(obj) {
            var fileName = $(obj).attr('title'),
                filePath = $('#breadcrumb_label').html(),
                fileType = $(obj).data('type'),
                url = '/video?filePath=' + filePath + '&fileName=' + fileName + '&fileType=' + fileType
            window.open(encodeURI(url))
        },
        // 代码文件预览
        showCode: function(obj) {
            var fileShowPath = $(obj).data('path'),
                fileName = $(obj).attr('title'),
                url = '/code?fileShowPath='
            url += fileShowPath
            url += '&fileName='
            url+= fileName
            window.open(encodeURI(url))
        },
        // 展示图片层
        showImg: function (obj) {
            var _this = this
            fileService.getFiles({
                filePath: $('#breadcrumb_label').html(),
                fileType: $(obj).data('type'),
                fileName: $(obj).parent().parent().find('.file-list-item-file-name').attr('title')
            }, function (data) {
                var photos = {
                        //相册标题
                        'title': '',
                        //相册id
                        'id': 123,
                        //初始显示的图片序号，默认0
                        'start': 0,
                        //相册包含的图片，数组格式
                        'data': []
                    },
                    photoObj
                layui.each(data, function (index, item) {
                    photoObj = new Object()
                    photoObj.alt = item.fileName
                    photoObj.pid = index
                    photoObj.src = item.fileShowPath
                    photoObj.thumb = item.fileShowPath
                    photos.data.push(photoObj)
                    if (item.isChecked == 1) {
                        photos.start = index
                    }
                })
                layer.photos({
                    photos: photos
                    //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
                    ,anim: 5
                })
            }, function (error) {
                panUtil.showErrorMsg(error)
            })
        },
        // 检查是否全选中
        checkSelectAll: function () {
            var flag = true
            if ($('.file-list-item-content').length <= 0) {
                flag = false
            }
            $('.file-list-item-content').each(function () {
                if (!$(this).hasClass('check')) {
                    flag = false
                    return false
                }
            })
            if (flag) {
                $('#check_all_btn').addClass('hide')
                $('#recheck_all_btn').removeClass('hide')
            } else {
                $('#check_all_btn').removeClass('hide')
                $('#recheck_all_btn').addClass('hide')
            }
        },
        // 双击文件夹事件
        goin: function (obj) {
            var breadcrumbText = $('#breadcrumb_label').html(),
                folderName = $(obj).parent().parent().find('.file-list-item-file-name').attr('title')
            $('#breadcrumb_label').html('').html((breadcrumbText == '/' ? '' : breadcrumbText) + '/' + folderName)
            this.loadFileList()
        },
        // 初始化上传组件
        initUploader: function () {
            var _this = this
            upload.render({
                elem: '#upload_btn'
                , url: '/file/uploadFile'
                , data: {
                    filePath: function () {
                        return $('#breadcrumb_label').html()
                    }
                }
                , headers: {}
                , accept: 'file'
                , auto: true
                , field: 'file'
                , xhr: function (fun) {
                    var that = this
                    //绑定监听
                    that.onprogress = fun
                    //使用闭包实现监听绑定
                    return function () {
                        //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
                        var xhr = $.ajaxSettings.xhr()
                        //判断监听函数是否为函数
                        if (typeof that.onprogress !== 'function')
                            return xhr
                        //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
                        if (that.onprogress && xhr.upload) {
                            xhr.upload.onprogress = that.onprogress
                        }
                        return xhr
                    }
                }
                //上传进度回调 value进度值
                , progress: function (index, value) {
                    // 单独上传 设置页面进度条
                    // element.progress('file_upload_progress', value + '%')

                    // 批量上传打开
                    index = parseInt(index.substring(index.lastIndexOf('-')))
                    _this.data.progress[index] = value
                    _this.reloadProgress()
                }
                , size: 0
                , multiple: true
                , drag: false
                , choose: function (obj) {
                    _this.data.chooseNum = obj.chooseNum
                }
                , before: function (obj) {
                    layer.open({
                        type: 1,
                        title: '上传进度',
                        //这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
                        content: $('#file_upload_progress_content'),
                        area: ['490px', '100px'],
                        closeBtn: 0
                    })
                }
                //上传后的回调
                , done: function (res, index, upload) {
                    if (res.status === 10) {
                        window.location.href = '/login'
                    }
                    //关闭进度条
                    // layer.closeAll()
                    // element.progress('file_upload_progress', '0%')
                    // if (res.status == 0) {
                    //     panUtil.showSuccessMsg('上传成功')
                    //     _this.reloadFileListDom(res.data)
                    // } else {
                    //     panUtil.showErrorMsg(res.message)
                    // }
                }
                //当文件全部被提交后，才触发
                , allDone: function(obj){
                    //关闭进度条
                    layer.closeAll()
                    _this.data.progress = new Array()
                    element.progress('file_upload_progress', '0%')
                    if (obj.total == obj.successful) {
                        panUtil.showSuccessMsg('上传成功')
                        _this.loadFileList()
                        return
                    }
                    panUtil.showErrorMsg('上传完成. 上传文件总个数: ' + obj.total + ' 文件上传失败个数: ' + obj.aborted)
                }
                , error: function (index, upload) {
                    //关闭
                    layer.closeAll()
                    // _this.data.progress = new Array()
                    element.progress('file_upload_progress', '0%')
                    panUtil.showErrorMsg('上传失败')
                }
            })
        },
        // 初始化树形组件
        initTreeComponent: function () {
            this.data.dTreeObj = dtree.render({
                elem: '#file_transfer_folder',
                obj: $('#file_transfer_folder'),
                initLevel: 7,
                type: 'all',
                none: '无数据',
                data: new Array(),
                icon: '0',
                line: true
            })
        },
        // 刷新进度条
        reloadProgress: function () {
            var _this = this,
                total = 0,
                iLength = 0
            Object.keys(_this.data.progress).forEach(function(){
                iLength++
            })
            if (iLength == _this.data.chooseNum) {
                Object.keys(_this.data.progress).forEach(function(key){
                    total += _this.data.progress[key]
                })
                var value = Math.floor(total / iLength)
                //设置页面进度条
                element.progress('file_upload_progress', value + '%')
            }

        }
    }

    $(function () {
        page.start()
    })
})