/**
 * Created by rubin on 2019/9/7.
 * version: 1.0.1
 */

'use strict';
var page = {
    start: function () {
        this.onLoad()
        this.bindEvent()
    },
    onLoad: function () {

    },
    bindEvent: function () {
        $('#login_btn').click(function () {
            var username = $('#username').val(),
                password = $('#password').val()
            if (!username) {
                panUtil.showErrorMsg('用户名不能为空')
                return
            }
            if (!password) {
                panUtil.showErrorMsg('密码不能为空')
                return
            }
            layer.load()
            userService.login({
                username: username,
                password: password
            }, function (res) {
                layer.closeAll('loading')
                panUtil.goHome()
            }, function (error) {
                layer.closeAll('loading')
                panUtil.showErrorMsg(error)
            })
        })
        
        $('#username').keyup(function (e) {
            // 兼容FF和IE和Opera
            var theEvent = e || window.event,
                code = theEvent.keyCode || theEvent.which || theEvent.charCode
            if (code == 13) {
                //回车执行查询
                $('#login_btn').click()
            }
        })

        $('#password').keyup(function (e) {
            // 兼容FF和IE和Opera
            var theEvent = e || window.event,
                code = theEvent.keyCode || theEvent.which || theEvent.charCode
            if (code == 13) {
                //回车执行查询
                $('#login_btn').click()
            }
        })
    },
}

$(function () {
    page.start()
})