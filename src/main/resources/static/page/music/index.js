/**
 *@Description
 *@auther  rubin
 *@create  2019-09-18 19:29
 *@version: 1.0.0
 */

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
            $('#we_share_doc_music').get(0).addEventListener('ended', function () {
                var $check = $('.layui-nav-item.check')
                if ($check.nextAll().length != 0) {
                    $check.next().find('a').get(0).click()
                }
            })
            $('#we_share_doc_music').get(0).addEventListener('pause', function () {
                $('.record-img').removeClass('layui-anim-rotate').removeClass('layui-anim-loop')
            })
            $('#we_share_doc_music').get(0).addEventListener('play', function () {
                $('.record-img').addClass('layui-anim-rotate').addClass('layui-anim-loop')
            })
            $(document).keyup(function (e) {
                // 兼容FF和IE和Opera
                var theEvent = e || window.event,
                    code = theEvent.keyCode || theEvent.which || theEvent.charCode
                if (code == 32) {
                    // 空格执行查询
                    var musicObj = $('#we_share_doc_music').get(0)
                    if (musicObj.paused) {
                        musicObj.play()
                        return
                    }
                    musicObj.pause()
                }
            })
            $('#we_share_doc_music').keyup(function (e) {
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
            this.loadMusic(filePath, fileName, fileType)
        },
        loadMusic: function (filePath, fileName, fileType) {
            $('.music-name').text('').text(fileName.substring(0, fileName.lastIndexOf('.')))
            fileService.getFiles({
                filePath: filePath,
                fileType: fileType,
                fileName: fileName
            }, function (data) {
                var musicListStr = ''
                layui.each(data, function (index, item) {
                    if (item.isChecked == 1) {
                        musicListStr += '<li class="layui-nav-item check">'
                        $('#we_share_doc_music').attr('src', encodeURI(item.fileShowPath)).get(0).play()
                    } else {
                        musicListStr += '<li class="layui-nav-item">'
                    }
                    musicListStr += '<a href="javascript:;" data-path="'
                    musicListStr += item.fileShowPath
                    musicListStr += '" data-name="'
                    musicListStr += item.fileName
                    musicListStr += '" onclick="page.showMe(this)">'
                    musicListStr += item.fileName.substring(0, item.fileName.lastIndexOf('.'))
                    musicListStr += '</a>'
                    musicListStr += '</li>'
                })
                $('.music-list').empty().append(musicListStr)
            }, function (error) {
                panUtil.showErrorMsg(error)
            })
        },
        showMe: function (obj) {
            var filePath = $(obj).data('path'),
                fileName = $(obj).data('name')
            $('.music-name').text('').text(fileName.substring(0, fileName.lastIndexOf('.')))
            $('#we_share_doc_music').attr('src', encodeURI(filePath)).get(0).play()
            $(obj).parent('li').addClass('check').siblings().removeClass('check')
        }
    }

    $(function () {
        page.start()
    })
})