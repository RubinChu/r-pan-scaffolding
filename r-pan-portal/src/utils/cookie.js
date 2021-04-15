/**
 * Created by rubin on 2020/6/4.
 */

'use strict'

import cookie from 'vue-cookie'

const LOGIN_TOKEN = 'login_token',
    SHARE_TOKEN = 'share_token',
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

export function setShareToken(token) {
    cookie.set(SHARE_TOKEN, token)
}

export function getShareToken() {
    let token = cookie.get(SHARE_TOKEN)
    if (token) {
        return token
    }
    return EMPTY_STR
}

export function clearShareToken() {
    cookie.delete(SHARE_TOKEN)
}
