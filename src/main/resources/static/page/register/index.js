/**
 * Created by rubin on 2019/9/7.
 * version: 1.0.0
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
        $('#register_btn').click(function () {
            var username = $('#username').val(),
                password = $('#password').val(),
                confirmPassword = $('#confirm_password').val()
            if (!username) {
                panUtil.showErrorMsg('用户名不能为空')
                return
            }
            if (!password) {
                panUtil.showErrorMsg('密码不能为空')
                return
            }
            if (!confirmPassword) {
                panUtil.showErrorMsg('确认密码不能为空')
                return
            }
            if (confirmPassword !== password) {
                panUtil.showErrorMsg('两次密码输入不一致')
                return
            }
            layer.load()
            userService.register({
                username: username,
                password: password
            }, function () {
                layer.closeAll('loading')
                panUtil.showSuccessMsg('注册成功')
                $('#username').val('')
                $('#password').val('')
                $('#confirm_password').val('')
            }, function (error) {
                layer.closeAll('loading')
                panUtil.showErrorMsg(error)
            })
        })
    }
}

$(function () {
    page.start()
})