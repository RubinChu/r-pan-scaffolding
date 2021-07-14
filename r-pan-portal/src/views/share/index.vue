<template>
    <div v-loading="pageLoading" element-loading-text="加载中..." class="pan-share-content">
        <div class="pan-share-header-content">
            <div class="pan-share-header-content-wrapper">
                <div class="pan-share-header-title-font-content">
                    <span class="pan-share-header-title-font" @click="goHome">R Pan</span>
                </div>
                <div v-if="loginFlag" class="pan-share-header-user-info-content">
                    <el-link :underline=false type="success" class="pan-share-username">
                        欢迎您,{{ username }}
                    </el-link>
                    <el-link :underline=false type="success" class="pan-share-exit-button" @click="exit">
                        退出
                    </el-link>
                </div>
                <div v-if="!loginFlag" class="pan-share-header-button-content">
                    <el-link :underline=false type="primary" class="pan-share-login-button" @click="login">
                        登录
                    </el-link>
                    <el-link :underline=false type="primary" class="pan-share-register-button" href="/register"
                             target="_blank">
                        注册
                    </el-link>
                </div>
            </div>
        </div>
        <div class="pan-share-list-content">
            <div class="pan-share-list-wrapper">
                <el-card shadow="always" class="pan-share-list-card">
                    <div v-if="!shareCancelFlag" slot="header" class="pan-share-list-card-header">
                        <div class="pan-share-list-card-header-share-info-content">
                            <span class="pan-share-list-card-header-share-info">{{ shareCodeHeader }}</span>
                            <div class="pan-share-list-card-header-time">
                                <span class="pan-share-list-card-header-create-date"><i class="el-icon-time"/>分享时间：{{ shareDate }}</span>
                                <span class="pan-share-list-card-header-expire-date"><i class="el-icon-time"/>失效时间：{{ shareExpireDate }}</span>
                            </div>
                        </div>
                        <div class="pan-share-list-card-button-group">
                            <el-button type="success" size="medium" round @click="saveFiles(undefined)">保存到我的R盘<i
                                    class="el-icon-document-copy el-icon--right"/></el-button>
                            <el-button type="info" size="medium" round @click="downloadFile">下载<i
                                    class="el-icon-download el-icon--right"/></el-button>
                        </div>
                    </div>
                    <div v-if="shareCancelFlag" class="pan-share-list-card-error-message">
                        <span>Sorry,您来晚啦~ 该分享已到期或已失效~</span>
                    </div>
                    <div v-if="!shareCancelFlag" class="pan-share-list-card-operate-content">
                        <div class="pan-share-list-card-operate-bread-crumb">
                            <el-breadcrumb separator-class="el-icon-arrow-right">
                                <el-breadcrumb-item v-for="(item, index) in breadCrumbs" :key="index">
                                    <a class="breadcrumb-item-a" @click="goToThis(item.id)" href="#">{{ item.name }}</a>
                                </el-breadcrumb-item>
                            </el-breadcrumb>
                        </div>
                        <el-divider></el-divider>
                    </div>
                    <div v-if="!shareCancelFlag" class="pan-share-list">
                        <el-table
                                ref="fileTable"
                                :data="tableData"
                                :height="tableHeight"
                                tooltip-effect="dark"
                                style="width: 100%"
                                @selection-change="handleSelectionChange"
                                @cell-mouse-enter="showOperation"
                                @cell-mouse-leave="hiddenOperation"
                        >
                            <el-table-column
                                    type="selection"
                                    width="55">
                            </el-table-column>
                            <el-table-column
                                    label="文件名"
                                    prop="filename"
                                    sortable
                                    show-overflow-tooltip
                                    min-width="750">
                                <template slot-scope="scope">
                                    <div @click="clickFilename(scope.row)" class="file-name-content">
                                        <i :class="getFileFontElement(scope.row.fileType)"
                                           style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                                        <span style="cursor:pointer;">{{ scope.row.filename }}</span>
                                    </div>
                                    <div v-show="scope.row.fileId === operationShowFileId"
                                         class="file-operation-content">
                                        <el-tooltip class="item" effect="light" content="保存到我的R盘" placement="top">
                                            <el-button type="success" icon="el-icon-document-copy" size="small" circle @click="saveFiles(scope.row)"/>
                                        </el-tooltip>
                                        <el-tooltip class="item" effect="light" content="下载" placement="top">
                                            <el-button type="info" icon="el-icon-download" size="small" circle @click="doDownload(scope.row)"/>
                                        </el-tooltip>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="fileSizeDesc"
                                    sortable
                                    label="大小"
                                    min-width="120"
                                    align="center"
                                    :formatter="formatFileSize">
                            </el-table-column>
                            <el-table-column
                                    prop="updateTime"
                                    sortable
                                    align="center"
                                    label="修改日期"
                                    min-width="240"
                                    :formatter="formatUpdateTime">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-card>
            </div>
        </div>
        <el-dialog
                title="欢迎登录"
                :visible.sync="loginDialogVisible"
                @opened="focusInput('username')"
                @closed="resetForm('loginForm')"
                width="30%"
                :append-to-body=true
                :modal-append-to-body=false
                :center="true">
            <div>
                <el-form label-width="100px" :rules="loginRules" ref="loginForm"
                         :model="loginForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="用户名" prop="username">
                        <el-input type="text"
                                  ref="username"
                                  @keyup.enter.native="doLogin"
                                  v-model="loginForm.username" autocomplete="off"/>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                        <el-input type="password"
                                  show-password
                                  @keyup.enter.native="doLogin"
                                  v-model="loginForm.password" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="loginDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doLogin" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                :visible.sync="shareCodeDialogVisible"
                @opened="focusInput('shareCode')"
                @closed="resetForm('shareCodeForm')"
                width="30%"
                :fullscreen=true
                :append-to-body=true
                :modal-append-to-body=false
                :close-on-click-modal=false
                :close-on-press-escape=false
                :show-close=false
        >
            <div class="pan-share-code-content">
                <div class="pan-share-code-wrapper">
                    <el-card :header="shareCodeHeader" shadow="always" class="pan-share-code-card">
                        <el-form
                                class="pan-share-code-form"
                                label-width="100px"
                                :inline=true
                                :rules="shareCodeFormRules"
                                ref="shareCodeForm"
                                :model="shareCodeForm"
                                status-icon
                                @submit.native.prevent>
                            <el-form-item label="提取码" prop="shareCode">
                                <el-input type="text"
                                          ref="shareCode"
                                          @keyup.enter.native="doCheckShareCode"
                                          v-model="shareCodeForm.shareCode" autocomplete="off"/>
                            </el-form-item>
                            <el-form-item>
                                <el-button type="primary" :loading="loading" @click="doCheckShareCode">确 定</el-button>
                            </el-form-item>
                        </el-form>
                    </el-card>
                </div>
            </div>
        </el-dialog>
        <el-dialog
                title="复制文件"
                :visible.sync="treeDialogVisible"
                @open="loadTreeData"
                @closed="resetTreeData"
                width="30%"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-tree
                        :data="treeData"
                        empty-text="暂无文件夹数据"
                        default-expand-all
                        highlight-current
                        check-on-click-node
                        :expand-on-click-node="false"
                        ref="tree">
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                        <i class="fa fa-folder-o" style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                        <span>{{ node.label }}</span>
                    </span>
                </el-tree>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="treeDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doChoseTreeNodeCallBack" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import panUtil from '../../utils/common'
    import userService from '../../api/user'
    import fileService from '../../api/file'
    import {clearToken, setToken, setShareToken, getShareToken, getToken, clearShareToken} from '../../utils/cookie'
    import shareService from '../../api/share'

    export default {
        name: "Share",
        components: {},
        props: {},
        data() {
            const checkUsername = (rule, value, callback) => {
                    if (!panUtil.checkUsername(value)) {
                        callback('请输入6-16位只包含数字和字母的用户名')
                        return
                    }
                    callback()
                },
                checkPassword = (rule, value, callback) => {
                    if (!panUtil.checkPassword(value)) {
                        callback('请输入8-16位的密码')
                        return
                    }
                    callback()
                }
            return {
                loginDialogVisible: false,
                loginRules: {
                    username: [
                        {validator: checkUsername, trigger: 'blur'}
                    ],
                    password: [
                        {validator: checkPassword, trigger: 'blur'}
                    ]
                },
                shareCodeFormRules: {
                    shareCode: [
                        {required: true, message: '请输入提取码', trigger: 'blur'}
                    ]
                },
                loginForm: {
                    username: '',
                    password: ''
                },
                loading: false,
                username: '',
                loginFlag: false,
                shareCodeDialogVisible: false,
                shareCodeForm: {
                    shareCode: ''
                },
                shareCodeHeader: '',
                shareCancelFlag: false,
                tableData: [],
                tableHeight: window.innerHeight - 300,
                multipleSelection: [],
                operationShowFileId: '',
                pageLoading: true,
                shareDate: '',
                shareExpireDate: '',
                breadCrumbs: [{
                    id: '-1',
                    name: '全部文件'
                }],
                treeData: [],
                treeDialogVisible: false,
                item: undefined
            }
        },
        methods: {
            loadShareInfo() {
                let _this = this
                shareService.getShareDetail(res => {
                    if (res.status === 0) {
                        _this.refreshShareInfo(res.data)
                    } else if (res.status === 4) {
                        _this.openShareCodePage()
                    } else {
                        _this.openShareExpirePage()
                    }
                })
            },
            refreshShareInfo(data) {
                let username = data.shareUserInfoVO.username,
                    shareName = data.shareName
                this.shareCodeHeader = username + '的分享：' + shareName
                this.shareDate = panUtil.formatTime(new Date(data.createTime))
                if (data.shareDay === 0) {
                    this.shareExpireDate = '永久有效'
                } else {
                    this.shareExpireDate = panUtil.formatTime(new Date(data.shareEndTime))
                }
                this.tableData = data.rPanUserFileVOList
            },
            openShareCodePage() {
                let _this = this
                shareService.getSimpleShareDetail({
                    shareId: _this.getShareId()
                }, res => {
                    if (res.status === 0) {
                        this.shareCodeDialogVisible = true
                        _this.shareCodeHeader = res.data.shareUserInfoVO.username + '的分享：' + res.data.shareName
                    } else {
                        _this.shareCodeDialogVisible = false
                        _this.openShareExpirePage()
                    }
                })
            },
            openShareExpirePage() {
                this.shareCancelFlag = true
            },
            loadUserInfo() {
                let _this = this
                userService.infoWithoutPageJump(res => {
                    if (res.status === 0) {
                        _this.username = res.data.username
                        _this.loginFlag = true
                    } else {
                        _this.username = ''
                        _this.loginFlag = false
                    }
                })
            },
            login() {
                this.loginDialogVisible = true
            },
            exit() {
                let _this = this
                _this.$confirm('确定要退出登录吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    userService.exit(() => {
                        clearToken()
                        _this.loginFlag = false
                        _this.username = ''
                    }, res => {
                        _this.$message.error(res.message)
                    })
                })
            },
            focusInput(ref) {
                this.$refs[ref].focus()
            },
            resetForm(ref) {
                this.$refs[ref].resetFields()
            },
            doLogin() {
                let _this = this
                _this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        userService.login({
                            username: _this.loginForm.username,
                            password: _this.loginForm.password
                        }, res => {
                            _this.loading = false
                            setToken(res.data)
                            _this.loginDialogVisible = false
                            _this.loadUserInfo()
                        }, res => {
                            _this.loading = false
                            _this.$message.error(res.message)
                        })
                    }
                })
            },
            goHome() {
                window.location.href = '/'
            },
            doCheckShareCode() {
                let _this = this
                _this.$refs.shareCodeForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        shareService.checkShareCode({
                            shareId: _this.getShareId(),
                            shareCode: _this.shareCodeForm.shareCode
                        }, res => {
                            if (res.status === 0) {
                                _this.loading = false
                                setShareToken(res.data)
                                _this.shareCodeDialogVisible = false
                                _this.loadShareInfo()
                            } else {
                                _this.loading = false
                                _this.$message.error(res.message)
                            }
                        })
                    }
                })
            },
            handleSelectionChange(multipleSelection) {
                this.multipleSelection = multipleSelection
            },
            showOperation(row, column, cell, event) {
                this.operationShowFileId = row.fileId
            },
            hiddenOperation(row, column, cell, event) {
                this.operationShowFileId = ''
            },
            clickFilename(row) {
                let _this = this
                if (row.folderFlag === 1) {
                    _this.goInFolder(row)
                }
            },
            goInFolder(row) {
                this.breadCrumbs.push({
                    id: row.fileId,
                    name: row.filename
                })
                this.reloadTableData(row.fileId)
            },
            reloadTableData(parentId) {
                let _this = this
                shareService.getShareFiles({
                    parentId: parentId
                }, res => {
                    if (res.status === 0) {
                        _this.tableData = res.data
                    } else {
                        window.location.reload()
                    }
                })
            },
            getFileFontElement(type) {
                let tagStr = 'fa fa-file'
                switch (type) {
                    case 0:
                        tagStr = 'fa fa-folder-o'
                        break
                    case 2:
                        tagStr = 'fa fa-file-archive-o'
                        break
                    case 3:
                        tagStr = 'fa fa-file-excel-o'
                        break
                    case 4:
                        tagStr = 'fa fa-file-word-o'
                        break
                    case 5:
                        tagStr = 'fa fa-file-pdf-o'
                        break
                    case 6:
                        tagStr = 'fa fa-file-text-o'
                        break
                    case 7:
                        tagStr = 'fa fa-file-image-o'
                        break
                    case 8:
                        tagStr = 'fa fa-file-audio-o'
                        break
                    case 9:
                        tagStr = 'fa fa-file-video-o'
                        break
                    case 10:
                        tagStr = 'fa fa-file-powerpoint-o'
                        break
                    case 11:
                        tagStr = 'fa fa-file-code-o'
                        break
                    default:
                        break
                }
                return tagStr
            },
            formatFileSize(row, column, cellValue, index) {
                if (row.fileSizeDesc) {
                    return row.fileSizeDesc
                }
                return '--'
            },
            formatUpdateTime(row, column, cellValue, index) {
                return panUtil.formatTime(new Date(row.updateTime))
            },
            goToThis(id) {
                let _this = this
                if (id === '-1') {
                    _this.breadCrumbs = [{
                        id: '-1',
                        name: '全部文件'
                    }]
                    _this.loadShareInfo()
                } else {
                    let newBreadCrumbs = new Array()
                    _this.breadCrumbs.some(item => {
                        newBreadCrumbs.push(item)
                        if (item.id === id) {
                            return true
                        }
                    })
                    _this.breadCrumbs = newBreadCrumbs
                    _this.reloadTableData(id)
                }
            },
            getShareId() {
                return this.$route.params.shareId
            },
            downloadFile() {
                let _this = this
                if (!_this.multipleSelection || _this.multipleSelection.length === 0) {
                    _this.$message.error('请选择要下载的文件')
                    return
                }
                for (let i = 0, iLength = _this.multipleSelection.length; i < iLength; i++) {
                    if (_this.multipleSelection[i].folderFlag === 1) {
                        _this.$message.error('文件夹暂不支持下载')
                        return
                    }
                }
                _this.doDownLoads(_this.multipleSelection)
            },
            doDownLoads(items, i) {
                let _this = this
                if (!i) {
                    i = 0
                }
                if (items.length === i) {
                    return
                }
                setTimeout(function () {
                    _this.doDownload(items[i]);
                    i++
                    _this.doDownLoads(items, i)
                }, 500);
            },
            doDownload(item) {
                let _this = this
                if (item.folderFlag === 1) {
                    _this.$message.error('文件夹暂不支持下载')
                    return
                }
                userService.infoWithoutPageJump(res => {
                    if (res.status === 0) {
                        shareService.getSimpleShareDetail({
                            shareId: _this.getShareId()
                        }, res => {
                            if (res.status === 0) {
                                let url = panUtil.getUrlPrefix() + '/share/file/download?fileId=' + item.fileId + '&shareToken=' + getShareToken() + '&authorization=' + getToken(),
                                    filename = item.filename,
                                    link = document.createElement('a')
                                link.style.display = 'none'
                                link.href = url
                                link.setAttribute('download', filename)
                                document.body.appendChild(link)
                                link.click()
                                document.body.removeChild(link)
                            } else {
                                window.location.reload()
                            }
                        })
                    } else {
                        _this.loadUserInfo()
                        _this.login()
                    }
                })
            },
            resetTreeData() {
                this.treeData = new Array()
                this.item = undefined
            },
            loadTreeData() {
                let _this = this
                fileService.getFolderTree(res => {
                    _this.treeData = res.data
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            doChoseTreeNodeCallBack() {
                this.loading = true
                let checkNode = this.$refs.tree.getCurrentNode()
                if (!checkNode) {
                    this.$message.error('请选择文件夹')
                    this.loading = false
                    return
                }
                this.doSaveFiles(checkNode.id)
            },
            saveFiles(item) {
                let _this = this
                if (item) {
                    _this.item = item
                } else if (!_this.multipleSelection || _this.multipleSelection.length === 0) {
                    _this.$message.error('请选择要保存的文件')
                    return
                }
                userService.infoWithoutPageJump(res => {
                    if (res.status === 0) {
                        _this.treeDialogVisible = true
                    } else {
                        _this.login()
                    }
                })
            },
            doSaveFiles(targetParentId) {
                let _this = this
                let fileIds = ''
                if (_this.item) {
                    fileIds = _this.item.fileId
                } else {
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => {
                        fileIdArr.push(item.fileId)
                    })
                    fileIds = fileIdArr.join('__,__')
                }
                shareService.saveShareFiles({
                    fileIds: fileIds,
                    targetParentId: targetParentId
                }, res => {
                    if (res.status === 0) {
                        _this.$message.success('保存成功')
                        _this.treeDialogVisible = false
                    } else if (res.status === 10) {
                        _this.treeDialogVisible = false
                        _this.loadUserInfo()
                        _this.login()
                    } else {
                        _this.$message.error(res.message)
                    }
                    _this.loading = false
                })
            }
        },
        computed: {},
        mounted() {
            clearShareToken()
            this.loadShareInfo()
            this.loadUserInfo()
            this.pageLoading = false
        },
        watch: {}
    }
</script>

<style>

    .pan-share-content {
        overflow: hidden;
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        min-width: 1103px;
        background: #f7f7f7;
        transition: background 1s ease;
        font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
        width: 100%;
        height: 100%;
    }

    .pan-share-content .pan-share-header-content {
        top: 0;
        left: 0;
        width: 100%;
        z-index: 41;
        position: fixed;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper {
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

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-title-font-content {
        display: inline-block;
        position: absolute;
        left: 40px;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-title-font-content .pan-share-header-title-font {
        font-size: 40px;
        font-weight: bolder;
        cursor: pointer;
        color: #F56C6C;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-user-info-content {
        display: inline-block;
        position: absolute;
        right: 100px;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-user-info-content .pan-share-username {
        margin-right: 20px;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-button-content {
        display: inline-block;
        position: absolute;
        right: 100px;
    }

    .pan-share-content .pan-share-header-content .pan-share-header-content-wrapper .pan-share-header-button-content .pan-share-login-button {
        margin-right: 20px;
    }

    .pan-share-code-content {
        width: 100%;
        height: 300px;
    }

    .pan-share-code-content .pan-share-code-wrapper {
        height: 300px;
        width: 450px;
        margin: 0 auto;
        position: relative;
        top: 50%;
    }

    .pan-share-code-content .pan-share-code-wrapper .pan-share-code-card {
        width: 100%;
        height: 100%;
        border-radius: 30px;
    }

    .pan-share-code-content .pan-share-code-wrapper .pan-share-code-card .pan-share-code-form {
        position: absolute;
        top: 40%;
    }

    .pan-share-content .pan-share-list-content {
        width: 100%;
        margin-top: 82px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper {
        width: 80%;
        margin: 0 auto;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card {
        width: 100%;
        border-radius: 30px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header {
        padding: 10px 20px 0 0;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-button-group {
        display: inline-block;
        float: right;
        margin-right: 20px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-header-share-info-content {
        display: inline-block;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-header-share-info-content .pan-share-list-card-header-share-info {
        color: #67C23A;
        font-size: 18px;
        margin-right: 10px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-header-share-info-content .pan-share-list-card-header-time {
        margin-top: 10px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-header-share-info-content .pan-share-list-card-header-time .pan-share-list-card-header-create-date {
        color: #909399;
        margin-right: 15px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-header .pan-share-list-card-header-share-info-content .pan-share-list-card-header-time .pan-share-list-card-header-expire-date {
        color: #F56C6C;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-error-message {
        width: 100%;
        height: 300px;
        padding-top: 50px;
        text-align: center;
        color: #F56C6C;
        font-size: 30px;
    }

    .pan-share-content .pan-share-list-content .pan-share-list-wrapper .pan-share-list-card .pan-share-list-card-operate-content {
        height: 30px;
        line-height: 30px;
        padding: 0 30px 0 0;
    }

    span {
        font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    }

    .breadcrumb-item-a {
        cursor: pointer !important;
        color: #409EFF !important;
    }

</style>