/**
 *@Description
 *@auther  rubin
 *@create  2019-09-29 11:24
 *@version 1.0.0
 */

var page

layui.use('code', function () {
    page = {
        start: function () {
            this.onLoad()
            this.bindEvent()
        },
        onLoad: function () {
            panUtil.initUserName()
            this.initCode()
        },
        bindEvent: function () {
            
        },
        initCode: function () {
            var domStr = '<pre class="layui-code code-text">'
            $.get(panUtil.getUrlParam('fileShowPath'), function (data, status) {
                if ('success' == status) {
                    domStr += data
                } else {
                    domStr += '获取静态资源失败'
                }
                domStr += '</pre>'
                $('.code-text-content').empty().html(domStr)
                layui.code({
                    elem: '.layui-code.code-text',
                    title: panUtil.getUrlParam('fileName'),
                    encode: false,
                    about: false
                })
            })
        }
    }

    $(function () {
        page.start()
    })
})
