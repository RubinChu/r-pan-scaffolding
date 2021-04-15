/**
* Created by rubin on 2020/6/5.
*/

<template>
    <div class="content">
        <div class="sub-cont">
            <div class="img">
                <div class="img__text m--in">
                    <h2>已有帐号？</h2>
                    <p>有帐号就登录吧，好久不见了！</p>
                </div>
                <div class="img__btn" @click="goLogin">
                    <span class="m--in">去 登 录</span>
                </div>
            </div>
            <div class="form sign-up">
                <h2>R Pan 注册</h2>
                <label>
                    <span>用户名</span>
                    <input type="text" v-model="registerForm.username" @keyup.enter="doRegister" ref="username"/>
                </label>
                <label>
                    <span>密码</span>
                    <input type="password" v-model="registerForm.password" @keyup.enter="doRegister"/>
                </label>
                <label>
                    <span>确认密码</span>
                    <input type="password" v-model="registerForm.rePassword" @keyup.enter="doRegister"/>
                </label>
                <label>
                    <span>密保问题</span>
                    <input type="text" v-model="registerForm.question" @keyup.enter="doRegister"/>
                </label>
                <label>
                    <span>密保答案</span>
                    <input type="text" v-model="registerForm.answer" @keyup.enter="doRegister"/>
                </label>
                <el-button type="warning" class="submit" :loading="loading" @click="doRegister" round>注 册</el-button>
            </div>
        </div>
    </div>
</template>

<script>

    import panUtil from '../../utils/common'
    import userService from '../../api/user'

    export default {
        data() {
            return {
                loading: false,
                registerForm: {
                    username: '',
                    password: '',
                    rePassword: '',
                    question: '',
                    answer: ''
                }
            }
        },
        components: {},
        methods: {
            goLogin() {
                this.$router.push({name: 'Login'})
            },
            doRegister() {
                let _this = this
                if (_this.checkRegisterForm()) {
                    _this.loading = true
                    userService.register(this.registerForm, () => {
                        _this.$message.success('注册成功！即将跳转至登录页面')
                        setTimeout(_this.goLogin, 1000)
                        _this.loading = false
                    }, res => {
                        _this.$message.error(res.message)
                        _this.loading = false
                    })
                }
            },
            checkRegisterForm() {
                if (!panUtil.checkUsername(this.registerForm.username)) {
                    this.$message.error('请输入6-16位只包含数字和字母的用户名')
                    return false
                }
                if (!panUtil.checkPassword(this.registerForm.password)) {
                    this.$message.error('请输入8-16位的密码')
                    return false
                }
                if (!this.registerForm.rePassword || this.registerForm.password !== this.registerForm.rePassword) {
                    this.$message.error('两次密码输入不一致')
                    return false
                }
                if (!this.registerForm.question) {
                    this.$message.error('请输入密保问题')
                    return false
                }
                if (!this.registerForm.answer) {
                    this.$message.error('请输入密保答案')
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

    .content {
        height: 600px;
    }

    .content .sub-cont {
        -webkit-transform: translate3d(-640px, 0, 0);
        transform: translate3d(-640px, 0, 0);
    }

    .content .img:before {
        -webkit-transform: translate3d(640px, 0, 0);
        transform: translate3d(640px, 0, 0);
    }

</style>
