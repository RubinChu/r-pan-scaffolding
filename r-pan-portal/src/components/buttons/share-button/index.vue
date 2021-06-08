<template>
    <div class="share-button-content">
        <el-button class="share-button" v-if="roundFlag" :size="size" round @click="shareFile">
            分享<i class="el-icon-share el-icon--right"/>
        </el-button>
        <el-button class="share-button" v-if="circleFlag" icon="el-icon-share" :size="size" circle @click="shareFile">
        </el-button>
        <el-dialog
                :title="shareTitle"
                :visible.sync="shareDialogVisible"
                @opened="focusInput('shareName')"
                @closed="resetForm('shareForm')"
                width="30%"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <div v-show="step === 1">
                    <el-form label-width="100px" :rules="shareFileRules" ref="shareForm"
                             :model="shareFileForm"
                             status-icon
                             @submit.native.prevent>
                        <el-form-item label="分享名称" prop="shareName">
                            <el-input type="text"
                                      ref="shareName"
                                      v-model="shareFileForm.shareName" autocomplete="off"/>
                        </el-form-item>
                        <el-form-item label="分享类型">
                            <el-radio-group v-model="shareFileForm.shareType">
                                <el-radio disabled label="0">有提取码</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="分享有效期">
                            <el-select v-model="shareFileForm.shareDayType">
                                <el-option label="永久有效" value="0"></el-option>
                                <el-option label="7天有效" value="1"></el-option>
                                <el-option label="30天有效" value="2"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-form>
                </div>
                <div v-show="step === 2">
                    <el-form label-width="100px"
                             status-icon
                             @submit.native.prevent>
                        <el-form-item label="分享链接" prop="shareUrl">
                            <el-link :underline=false type="primary"><span>{{ shareResultForm.shareUrl }}</span>
                            </el-link>
                        </el-form-item>
                        <el-form-item label="提取码">
                            <el-link :underline=false type="success"><span>{{ shareResultForm.shareCode }}</span>
                            </el-link>
                        </el-form-item>
                        <div class="share-result-button-content">
                            <el-button type="primary" class="share-result-copy-button" @click="copy">
                                点击复制<i class="el-icon-document-copy el-icon--right"></i>
                            </el-button>
                        </div>
                    </el-form>
                </div>
            </div>
            <span v-show="step === 1" slot="footer" class="dialog-footer">
                <el-button @click="shareDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doShareFile" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import shareService from "../../../api/share";

    export default {
        name: "ShareButton",
        components: {},
        props: {
            roundFlag: Boolean,
            circleFlag: Boolean,
            size: String,
            multipleSelection: Array,
            item: Object
        },
        data() {
            return {
                shareDialogVisible: false,
                loading: false,
                step: 1,
                shareFileForm: {
                    shareName: '',
                    shareType: '0',
                    shareDayType: '0'
                },
                shareFileRules: {
                    shareName: [
                        {required: true, message: '请输入分享名称', trigger: 'blur'}
                    ]
                },
                shareTitle: '',
                shareResultForm: {
                    shareUrl: '',
                    shareCode: ''
                }
            }
        },
        methods: {
            shareFile() {
                if (!this.item && (!this.multipleSelection || this.multipleSelection.length === 0)) {
                    this.$message.error('请选择要分享的文件')
                    return
                }
                if (this.item) {
                    this.shareTitle = '分享文件（' + this.handleFilename(this.item.filename) + ')'
                } else {
                    if (this.multipleSelection.length === 1) {
                        this.shareTitle = '分享文件（' + this.handleFilename(this.multipleSelection[0].filename) + ')'
                    } else {
                        this.shareTitle = '分享文件（' + this.handleFilename(this.multipleSelection[0].filename) + '等)'
                    }
                }
                this.shareDialogVisible = true
            },
            focusInput(refName) {
                this.$refs[refName].focus()
            },
            resetForm(refName) {
                this.$refs[refName].resetFields()
                this.step = 1
                this.shareTitle = ''
                this.shareResultForm = {
                    shareUrl: '',
                    shareCode: ''
                }
            },
            doShareFile() {
                let _this = this,
                    shareFileIdArr = new Array()
                _this.loading = true
                if (_this.item) {
                    shareFileIdArr.push(_this.item.fileId)
                } else {
                    _this.multipleSelection.forEach(item => {
                        shareFileIdArr.push(item.fileId)
                    })
                }
                shareService.createShare({
                    shareName: _this.shareFileForm.shareName,
                    shareType: parseInt(_this.shareFileForm.shareType),
                    shareDayType: parseInt(_this.shareFileForm.shareDayType),
                    shareFileIds: shareFileIdArr.join('__,__')
                }, res => {
                    _this.loading = false
                    _this.shareTitle = '恭喜你！分享成功！'
                    _this.shareResultForm.shareUrl = res.data.shareUrl
                    _this.shareResultForm.shareCode = res.data.shareCode
                    _this.step = 2
                }, res => {
                    _this.loading = false
                    _this.$message.error(res.message)
                })
            },
            handleFilename(filename) {
                if (filename.length > 10) {
                    filename = filename.substring(0, 11) + '...'
                }
                return filename
            },
            copy() {
                let shareMessage = '链接：' + this.shareResultForm.shareUrl + '\n提取码：' + this.shareResultForm.shareCode + '\n赶快分享给小伙伴吧！'
                this.$clipboard(shareMessage)
                this.$message.success('复制成功')
            }
        },
        computed: {},
        mounted() {
        },
        watch: {}
    }
</script>

<style>

    .share-button-content {
        display: inline-block;
        margin-right: 10px;
    }

    .share-button-content .share-button {
        background-color: #F2F6FC;
    }

    .share-result-button-content {
        width: 100%;
        height: 10px;
        line-height: 30px;
        text-align: right;
        padding: 0 10px 20px 0;
    }

</style>