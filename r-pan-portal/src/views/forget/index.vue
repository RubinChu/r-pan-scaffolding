/**
* Created by rubin on 2020/6/5.
*/

<template>
    <div class="content">
        <div class="steps-content">
            <el-steps :active="currentStep" direction="vertical" finish-status="success"
                      :process-status="processStatus">
                <el-step title="校验用户名"></el-step>
                <el-step title="校验密保答案"></el-step>
                <el-step title="修改密码"></el-step>
            </el-steps>
        </div>
        <div class="form" v-if="currentStep === 0">
            <h2>R Pan 校验用户名</h2>
            <div class="forget-form-content">
                <div class="forget-form-item">
                    <label>
                        <span>用户名</span>
                        <input type="text" v-model="username" @keyup.enter="goStepTwo" ref="username"/>
                    </label>
                </div>
            </div>
            <el-button type="warning" class="submit" :loading="false" @click="goStepTwo" round>下一步</el-button>
            <div class="login-link-content">
                <el-link type="primary" @click="goLogin">去登陆</el-link>
            </div>
        </div>
        <div class="form" v-if="currentStep === 1">
            <h2>R Pan 校验密保答案</h2>
            <div class="forget-form-content">
                <div class="forget-form-item">
                    <label>
                        <span>您的密保问题为</span>
                        <div class="question-content">{{question}}</div>
                    </label>
                    <label>
                        <span>您的密保答案为</span>
                        <input type="text" v-model="answer" @keyup.enter="goStepThree" ref="answer"/>
                    </label>
                </div>
            </div>
            <el-button type="warning" class="submit" :loading="loading" @click="goStepThree" round>下一步</el-button>
            <div class="login-link-content">
                <el-link type="primary" @click="goLogin">去登陆</el-link>
            </div>
        </div>
        <div class="form" v-if="currentStep === 2">
            <h2>R Pan 重置密码</h2>
            <div class="forget-form-content">
                <div class="forget-form-item">
                    <label>
                        <span>新密码</span>
                        <input type="password" v-model="newPassword" @keyup.enter="resetPassword" ref="newPassword"/>
                    </label>
                    <label>
                        <span>确认密码</span>
                        <input type="password" v-model="reNewPassword" @keyup.enter="resetPassword"/>
                    </label>
                </div>
            </div>
            <el-button type="warning" class="submit" :loading="false" @click="resetPassword" round>提 交</el-button>
            <div class="login-link-content">
                <el-link type="primary" @click="goLogin">去登陆</el-link>
            </div>
        </div>
    </div>
</template>

<script>

    import userService from '../../api/user'
    import panUtil from '../../utils/common'

    export default {
        data() {
            return {
                currentStep: 0,
                username: '',
                question: '',
                answer: '',
                newPassword: '',
                reNewPassword: '',
                token: '',
                processStatus: 'process',
                loading: false
            }
        },
        components: {},
        methods: {
            goLogin() {
                this.$router.push({name: 'Login'})
            },
            goStepOne() {
                let _this = this
                _this.loading = true
                _this.currentStep = 0
                _this.username = ''
                _this.question = ''
                _this.answer = ''
                _this.newPassword = ''
                _this.reNewPassword = ''
                _this.token = ''
                _this.processStatus = 'process'
                _this.$nextTick(function () {
                    _this.$refs.username.focus()
                })
                _this.loading = false
            },
            goStepTwo() {
                let _this = this
                if (!_this.username) {
                    _this.$message.error('请输入要修改密码的用户名')
                    return
                }
                _this.loading = true
                userService.checkUsername({
                    username: _this.username
                }, res => {
                    _this.question = res.data
                    _this.currentStep = 1
                    _this.$nextTick(function () {
                        _this.$refs.answer.focus()
                        _this.loading = false
                    })
                }, res => {
                    _this.$message.error(res.message)
                    _this.loading = false
                })
            },
            goStepThree() {
                let _this = this
                if (!_this.answer) {
                    _this.$message.error('请输入密保答案')
                    return
                }
                _this.loading = true
                userService.checkAnswer({
                    username: _this.username,
                    question: _this.question,
                    answer: _this.answer
                }, res => {
                    _this.token = res.data
                    _this.currentStep = 2
                    _this.$nextTick(function () {
                        _this.$refs.newPassword.focus()
                        _this.loading = false
                    })
                }, res => {
                    _this.$message.error(res.message)
                    _this.loading = false
                })
            },
            resetPassword() {
                let _this = this
                if (!_this.token) {
                    _this.$message.error('由于您长时间未操作，将返回步骤一重新申请密码重置')
                    _this.goStepOne()
                    return
                }
                if (_this.checkPassword()) {
                    _this.loading = true
                    userService.resetPassword({
                        username: _this.username,
                        newPassword: _this.newPassword,
                        token: _this.token
                    }, () => {
                        _this.loading = false
                        _this.processStatus = 'success'
                        _this.$message.success('密码重置成功！即将跳转至登录页面')
                        setTimeout(_this.goLogin, 1000)
                    }, res => {
                        if (res.status === 2) {
                            _this.loading = false
                            _this.$message.error('由于您长时间未操作，将返回步骤一重新申请密码重置')
                            _this.goStepOne()
                        } else {
                            _this.loading = false
                            _this.$message.error(res.message)
                        }
                    })
                }
            },
            checkPassword() {
                if (!panUtil.checkPassword(this.newPassword)) {
                    this.$message.error('请输入8-16位的密码')
                    return false
                }
                if (!this.reNewPassword || this.newPassword !== this.reNewPassword) {
                    this.$message.error('两次密码输入不一致')
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

    .img__text h2 {
        margin-bottom: 10px;
        font-weight: normal;
    }

    .img__text p {
        font-size: 14px;
        line-height: 1.5;
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
        display: table;
    }

    .content .steps-content {
        padding: 20px 0 20px 10px;
        display: table-cell;
        vertical-align: middle;
    }

    .login-link-content {
        text-align: right;
    }

    .forget-form-content {
        width: 100%;
        height: 300px;
        display: table;
    }

    .forget-form-content .forget-form-item {
        display: table-cell;
        vertical-align: middle;
    }

    .forget-form-content .forget-form-item label {
        height: 90px;
    }

    .forget-form-content .forget-form-item .question-content {
        margin-top: 15px;
    }
</style>
