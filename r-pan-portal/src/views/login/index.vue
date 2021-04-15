/**
* Created by rubin on 2020/6/4.
*/

<template>
    <div class="login-content">
        <div class="content">
            <div class="form sign-in">
                <h2>R Pan 登录</h2>
                <label>
                    <span>用户名</span>
                    <input type="text" @keyup.enter="doLogin" v-model="loginForm.username" ref="username"/>
                </label>
                <label>
                    <span>密码</span>
                    <input type="password" @keyup.enter="doLogin" v-model="loginForm.password"/>
                </label>
                <p class="forgot-pass">
                    <a href="javascript:" @click="goForget">
                        忘记密码？
                    </a>
                </p>
                <el-button type="warning" class="submit" :loading="loading" @click="doLogin" round>登 录</el-button>
            </div>
            <div class="sub-cont">
                <div class="img">
                    <div class="img__text m--up">
                        <h2>还未注册？</h2>
                        <p>立即注册，享受独有空间！</p>
                    </div>
                    <div class="img__btn" @click="goRegister">
                        <span class="m--up">去 注 册</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    import panUtil from '../../utils/common'
    import userService from '../../api/user'
    import { setToken } from '../../utils/cookie'

    export default {
        data() {
            return {
                loginForm: {
                    username: '',
                    password: ''
                },
                loading: false
            }
        },
        components: {},
        methods: {
            goRegister() {
                this.$router.push({name: 'Register'})
            },
            goForget() {
                this.$router.push({name: 'Forget'})
            },
            doLogin() {
                let _this = this
                if (_this.checkLoginForm()) {
                    _this.loading = true
                    userService.login(_this.loginForm, res => {
                        setToken(res.data)
                        _this.$router.push({name: 'Index'})
                    }, res => {
                        _this.$message.error(res.message)
                        _this.loading = false
                    })
                }
            },
            checkLoginForm() {
                if (!panUtil.checkUsername(this.loginForm.username)) {
                    this.$message.error('请输入6-16位只包含数字和字母的用户名')
                    return false
                }
                if (!panUtil.checkPassword(this.loginForm.password)) {
                    this.$message.error('请输入8-16位的密码')
                    return false
                }
                return true
            }
        },
        mounted() {
            this.$refs.username.focus()
        },
        watch: {}
    }
</script>

<style scoped>

    *, *:before, *:after {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
    }

    input {
        border: none;
        outline: none;
        background: none;
        font-family: 'Open Sans', Helvetica, Arial, sans-serif;
    }

    .content {
        overflow: hidden;
        position: absolute;
        left: 50%;
        top: 50%;
        width: 900px;
        height: 550px;
        margin: -300px 0 0 -450px;
        background: #fff;
    }

    .form {
        position: relative;
        width: 640px;
        height: 100%;
        transition: -webkit-transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
        padding: 50px 30px 0;
    }

    .sub-cont {
        overflow: hidden;
        position: absolute;
        left: 640px;
        top: 0;
        width: 900px;
        height: 100%;
        padding-left: 260px;
        background: #fff;
        transition: -webkit-transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
    }

    button {
        display: block;
        margin: 0 auto;
        width: 260px;
        height: 36px;
        border-radius: 30px;
        color: #fff;
        font-size: 15px;
        cursor: pointer;
    }

    .img {
        overflow: hidden;
        z-index: 2;
        position: absolute;
        left: 0;
        top: 0;
        width: 260px;
        height: 100%;
        padding-top: 360px;
    }

    .img:before {
        content: '';
        position: absolute;
        right: 0;
        top: 0;
        width: 900px;
        height: 100%;
        background-image: url(../../assets/imgs/bg.jpg);
        background-size: cover;
        transition: -webkit-transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
    }

    .img:after {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.6);
    }

    .img__text {
        z-index: 2;
        position: absolute;
        left: 0;
        top: 50px;
        width: 100%;
        padding: 0 20px;
        text-align: center;
        color: #fff;
        transition: -webkit-transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out;
        transition: transform 0.6s ease-in-out, -webkit-transform 0.6s ease-in-out;
    }

    .img__text h2 {
        margin-bottom: 10px;
        font-weight: normal;
    }

    .img__text p {
        font-size: 14px;
        line-height: 1.5;
    }

    .img__btn {
        overflow: hidden;
        z-index: 2;
        position: relative;
        width: 100px;
        height: 36px;
        margin: 0 auto;
        background: transparent;
        color: #fff;
        text-transform: uppercase;
        font-size: 15px;
        cursor: pointer;
    }

    .img__btn:after {
        content: '';
        z-index: 2;
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        border: 2px solid #fff;
        border-radius: 30px;
    }

    .img__btn span {
        position: absolute;
        left: 0;
        top: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 100%;
        transition: -webkit-transform 0.6s;
        transition: transform 0.6s;
        transition: transform 0.6s, -webkit-transform 0.6s;
    }

    h2 {
        width: 100%;
        font-size: 26px;
        text-align: center;
    }

    label {
        display: block;
        width: 260px;
        margin: 25px auto 0;
        text-align: center;
    }

    label span {
        font-size: 12px;
        color: #909399;
        text-transform: uppercase;
    }

    input {
        display: block;
        width: 100%;
        margin-top: 5px;
        padding-bottom: 5px;
        font-size: 16px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.4);
        text-align: center;
    }

    .submit {
        margin-top: 40px !important;
    }

    .forgot-pass {
        margin-top: 15px;
        text-align: center;
        font-size: 12px;
        color: #cfcfcf;
    }

    .forgot-pass a {
        color: #cfcfcf;
    }

</style>
