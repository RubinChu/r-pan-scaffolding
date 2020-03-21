/**
 *@Description
 *@auther  rubin
 *@create  2019-09-29 11:24
 *@version 1.0.0
 */

var page = {
    start: function () {
        this.onLoad()
        this.bindEvent()
    },
    onLoad: function () {
        panUtil.initUserName()
    },
    bindEvent: function () {

    }
}

$(function () {
    page.start()
})