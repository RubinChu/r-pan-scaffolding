/**
 * Created by rubin on 2019/9/13.
 * version: 1.0.0
 */

'use strict';
var page
layui.use('element', function () {
    var element = layui.element
    page = {
        start: function () {
            this.onLoad()
            this.bindEvent()
        },
        onLoad: function () {
            panUtil.initUserName()
            this.initPage()
        },
        bindEvent: function () {
            $('#we_share_doc_video').get(0).addEventListener('ended', function () {
                var $check = $('.layui-nav-item.check')
                if ($check.nextAll().length != 0) {
                    $check.next().find('a').get(0).click()
                }
            })
            $(document).keyup(function (e) {
                // 兼容FF和IE和Opera
                var theEvent = e || window.event,
                    code = theEvent.keyCode || theEvent.which || theEvent.charCode
                if (code == 32) {
                    // 空格执行查询
                    var videoObj = $('#we_share_doc_video').get(0)
                    if (videoObj.paused) {
                        videoObj.play()
                        return
                    }
                    videoObj.pause()
                }
            })
            $('#we_share_doc_video').keyup(function (e) {
                // 兼容FF和IE和Opera
                var theEvent = e || window.event,
                    code = theEvent.keyCode || theEvent.which || theEvent.charCode
                if (code == 32) {
                    // 空格执行查询
                    var videoObj = $('#we_share_doc_video').get(0)
                    if (videoObj.paused) {
                        videoObj.play()
                        return
                    }
                    videoObj.pause()
                }
            })
        },
        initPage: function () {
            var filePath = panUtil.getUrlParam('filePath'),
                fileName = panUtil.getUrlParam('fileName'),
                fileType = panUtil.getUrlParam('fileType')
            this.loadVideo(filePath, fileName, fileType)
        },
        loadVideo: function (filePath, fileName, fileType) {
            $('.video-name').text(fileName.substring(0, fileName.lastIndexOf('.')))
            fileService.getFiles({
                filePath: filePath,
                fileType: fileType,
                fileName: fileName
            }, function (data) {
                var videoListStr = ''
                layui.each(data, function (index, item) {
                    if (item.isChecked == 1) {
                        videoListStr += '<li class="layui-nav-item check">'
                        $('#we_share_doc_video').attr('src', encodeURI(item.fileShowPath)).get(0).play()
                    } else {
                        videoListStr += '<li class="layui-nav-item">'
                    }
                    videoListStr += '<a href="javascript:;" data-path="'
                    videoListStr += item.fileShowPath
                    videoListStr += '" data-name="'
                    videoListStr += item.fileName
                    videoListStr += '" onclick="page.showMe(this)">'
                    videoListStr += item.fileName.substring(0, item.fileName.lastIndexOf('.'))
                    videoListStr += '</a>'
                    videoListStr += '</li>'
                })
                $('.video-list').empty().append(videoListStr)
            }, function (error) {
                panUtil.showErrorMsg(error)
            })
        },
        showMe: function (obj) {
            var filePath = $(obj).data('path'),
                fileName = $(obj).data('name')
            $('.video-name').text('').text(fileName.substring(0, fileName.lastIndexOf('.')))
            $('#we_share_doc_video').attr('src', encodeURI(filePath)).get(0).play()
            $(obj).parent('li').addClass('check').siblings().removeClass('check')
        }
    }

    $(function () {
        page.start()
    })
})
