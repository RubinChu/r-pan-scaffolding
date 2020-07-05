/**
 * Created by rubin on 2020/6/4.
 */

'use strict'

import cookie from 'vue-cookie'

const LOGIN_TOKEN = 'login_token',
    EMPTY_STR = ''

export function setToken(token) {
    cookie.set(LOGIN_TOKEN, token)
}

export function getToken() {
    let token = cookie.get(LOGIN_TOKEN)
    if (token) {
        return token
    }
    return EMPTY_STR
}

export function clearToken() {
    cookie.delete(LOGIN_TOKEN)
}
