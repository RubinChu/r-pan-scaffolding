<template>
    <div class="pan-header-content">
        <div class="pan-header-content-wrapper">
            <div class="pan-title-font-content">
                <span class="pan-title-font" @click="goHome">R Pan</span>
            </div>
            <div class="pan-user-info-content">
                <el-dropdown @command="handleCommand">
                          <span class="pan-user-info">
                            欢迎您,{{ username }}
                              <i class="el-icon-arrow-down el-icon--right"/>
                          </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
                        <el-dropdown-item command="exit">退出登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>
        <el-dialog
                title="修改密码"
                :visible.sync="changePasswordDialogVisible"
                @opened="focusPasswordInput"
                @closed="resetChangePasswordForm"
                width="30%"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-form label-width="100px" :rules="changePasswordRules" ref="changePasswordForm"
                         :model="changePasswordForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="旧密码" prop="password">
                        <el-input type="password"
                                  show-password
                                  ref="password"
                                  @keyup.enter.native="doChangePassword"
                                  v-model="changePasswordForm.password" autocomplete="off"/>
                    </el-form-item>
                    <el-form-item label="新密码" prop="newPassword">
                        <el-input type="password"
                                  show-password
                                  @keyup.enter.native="doChangePassword"
                                  v-model="changePasswordForm.newPassword" autocomplete="off"/>
                    </el-form-item>
                    <el-form-item label="确认密码" prop="reNewPassword">
                        <el-input type="password"
                                  show-password
                                  @keyup.enter.native="doChangePassword"
                                  v-model="changePasswordForm.reNewPassword" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="changePasswordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doChangePassword" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import userService from '../../api/user'
    import { clearToken } from '../../utils/cookie'

    export default {
        name: "PanHeader",
        data() {
            let checkReNewPassword = (rule, value, callback) => {
                if (value !== this.changePasswordForm.newPassword) {
                    return callback(new Error('两次密码不一致'));
                } else {
                    callback()
                }
            }
            return {
                username: '',
                changePasswordDialogVisible: false,
                changePasswordForm: {
                    password: '',
                    newPassword: '',
                    reNewPassword: ''
                },
                loading: false,
                changePasswordRules: {
                    password: [
                        {required: true, message: '请输入旧密码', trigger: 'blur'},
                        {min: 8, max: 16, message: '请输入8-16位的密码', trigger: 'blur'}
                    ],
                    newPassword: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {min: 8, max: 16, message: '请输入8-16位的密码', trigger: 'blur'}
                    ],
                    reNewPassword: [
                        {required: true, message: '请输入确认密码', trigger: 'blur'},
                        {validator: checkReNewPassword, trigger: 'blur'}
                    ]
                },
            }
        },
        methods: {
            initUserInfo() {
                let _this = this
                userService.info(res => {
                    _this.username = res.data.username
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            handleCommand(command) {
                if (command === 'changePassword') {
                    this.changePasswordDialogVisible = true
                } else if (command === 'exit') {
                    this.exit()
                }
            },
            doChangePassword() {
                let _this = this
                _this.$refs.changePasswordForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        userService.changePassword({
                            password: _this.changePasswordForm.password,
                            newPassword: _this.changePasswordForm.newPassword
                        }, () => {
                            _this.loading = false
                            _this.changePasswordDialogVisible = false
                            _this.$notify({
                                title: '成功',
                                message: '密码修改成功,即将跳转至登录页面',
                                type: 'success'
                            })
                            setTimeout(() => _this.goLogin(), 1000)
                        }, res => {
                            _this.$message.error(res.message)
                            _this.loading = false
                        })
                    }
                })
            },
            exit() {
                let _this = this
                _this.$confirm('确定要退出登录吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    userService.exit(() => {
                        _this.goLogin()
                    }, res => {
                        _this.$message.error(res.message)
                    })

                })
            },
            goLogin() {
                clearToken()
                this.$router.push({name: 'Login'})
            },
            goHome() {
                window.location.href = '/'
            },
            focusPasswordInput() {
                this.$refs.password.focus()
            },
            resetChangePasswordForm() {
                this.$refs.changePasswordForm.resetFields()
            }
        },
        computed: {},
        mounted() {
            this.initUserInfo()
        },
        watch: {
        }
    }
</script>

<style>

    .pan-header-content {
        top: 0;
        left: 0;
        width: 100%;
        z-index: 41;
        position: fixed;
    }

    .pan-header-content .pan-header-content-wrapper {
        height: 62px;
        line-height: 62px;
        position: relative;
        background: #fff;
        box-shadow: 0 2px 6px 0 rgba(0, 0, 0, .05);
        -webkit-transition: background 1s ease;
        -moz-transition: background 1s ease;
        -o-transition: background 1s ease;
        transition: background 1s ease;
    }

    .pan-header-content .pan-header-content-wrapper .pan-title-font-content {
        display: inline-block;
        position: absolute;
        left: 40px;
    }

    .pan-header-content-wrapper .pan-title-font-content .pan-title-font {
        font-size: 40px;
        font-weight: bolder;
        cursor: pointer;
        color: #F56C6C;
    }

    .pan-header-content-wrapper .pan-user-info-content {
        display: inline-block;
        position: absolute;
        right: 40px;
    }

    .pan-header-content-wrapper .pan-user-info-content .pan-user-info {
        color: #409EFF;
    }

</style>
