/**
 * Created by rubin on 2018/10/24.
 * 公用js文件
 * version: 1.0.2
 */

'use strict';

var panUtil = {

    // 网络请求
    request: function (param) {
        var _this = this
        $.ajax({
            type: param.method || 'get',
            url: param.url || '',
            dataType: param.type || 'json',
            data: param.data || '',
            success: function (res) {
                // 请求成功
                if (0 === res.status) {
                    typeof param.success === 'function' && param.success(res.data, res.message)
                }
                // 需要强制登陆
                else if (10 === res.status) {
                    _this.goLogin()
                }
                // 请求数据错误
                else if (1 === res.status) {
                    typeof param.error === 'function' && param.error(res.message)
                }

            },
            error: function (err) {
                typeof param.error === 'function' && param.error(err.statusText)
            }
        })
    },
    // 强制登录方法，登陆成功后跳转回之前页面
    goLogin: function () {
        window.localStorage.clear()
        window.location.href = '/login'
    },
    // 跳回主页
    goHome: function () {
        window.location.href = '/'
    },
    // 字段的验证，支持非空、手机、邮箱的判断
    validate: function (value, type) {
        var value = $.trim(value)
        // 非空验证
        if ('require' === type) {
            //将value强转为boolean类型的值
            return !!value
        }
        // 手机号验证
        if ('phone' === type) {
            return /^1\d{10}$/.test(value)
        }
        // 邮箱验证
        if ('email' === type) {
            return /^(\w)+(\.\w+)*@(\w)+((\.\w{2,3}){1,3})$/.test(value)
        }
    },
    //获取url参数
    getUrlParam: function (name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)');
        var result = window.location.search.substr(1).match(reg);
        return result ? decodeURIComponent(result[2]) : null;
    },
    // 错误提示
    showErrorMsg: function (msg) {
        layer.msg(msg, {
            icon: 2,
            time: 2000 //2秒关闭（如果不配置，默认是3秒）
        })
    },
    // 成功提示
    showSuccessMsg: function (msg) {
        layer.msg(msg, {
            icon: 1,
            time: 2000 //2秒关闭（如果不配置，默认是3秒）
        })
    },
    exit: function () {
        var _this = this
        _this.request({
            url: '/user/exit',
            method: 'POST',
            success: function () {
                _this.goLogin()
            },
            error: function (error) {
                _this.showErrorMsg(error)
            }
        })
    },
    initUserName: function () {
        var _this = this
        _this.request({
            url: '/user/getUsername',
            method: 'GET',
            success: function (data) {
                $('.r-pan-username-font').text('').text('欢迎您,' + data)
            },
            error: function (error) {
                _this.showErrorMsg(error)
            }
        })
    }
}