<template>
    <div class="rename-button-content">
        <el-button v-if="roundFlag" type="warning" :size="size" round @click="renameFile">
            重命名<i class="el-icon-edit el-icon--right"/>
        </el-button>
        <el-button v-if="circleFlag" type="warning" :size="size" circle @click="renameFile">
            <i class="el-icon-edit"/>
        </el-button>
        <el-dialog
                title="文件重命名"
                :visible.sync="renameDialogVisible"
                width="30%"
                @opened="focusInput('filename')"
                @closed="resetForm('renameForm')"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-form label-width="100px" :rules="renameRules" ref="renameForm"
                         :model="renameForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="文件名称" prop="filename">
                        <el-input type="text"
                                  ref="filename"
                                  @keyup.enter.native="doRenameFile"
                                  v-model="renameForm.filename" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="renameDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doRenameFile" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import fileService from '../../../api/file'

    export default {
        name: 'RenameButton',
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
                renameRules: {
                    filename: [
                        {required: true, message: '请输入新文件名称', trigger: 'blur'}
                    ]
                },
                renameForm: {
                    fileId: '',
                    filename: ''
                },
                renameDialogVisible: false,
                loading: false
            }
        },
        methods: {
            renameFile() {
                let _this = this
                if (_this.item) {
                    _this.renameForm.fileId = _this.item.fileId
                    _this.renameForm.filename = _this.item.filename
                    _this.renameDialogVisible = true
                    return
                }
                if (!_this.multipleSelection || _this.multipleSelection.length == 0) {
                    _this.$message.error('请选择要重命名的文件')
                    return
                }
                if (_this.multipleSelection.length > 1) {
                    _this.$message.error('请选择一个文件进行重命名')
                    return
                }
                let item = _this.multipleSelection[0]
                _this.renameForm.fileId = item.fileId
                _this.renameForm.filename = item.filename
                _this.renameDialogVisible = true
            },
            doRenameFile() {
                let _this = this
                _this.$refs.renameForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        fileService.update({
                            fileId: _this.renameForm.fileId,
                            newFilename: _this.renameForm.filename
                        }, res => {
                            _this.loading = false
                            _this.renameDialogVisible = false
                            _this.$message.success('重命名成功')
                            _this.$emit('refresh', res.data)
                        }, res => {
                            _this.$message.error(res.message)
                            _this.loading = false
                        })
                    }
                })
            },
            focusInput(refName) {
                this.$refs[refName].focus()
            },
            resetForm(refName) {
                this.$refs[refName].resetFields()
            }
        },
        computed: {},
        mounted() {
        },
        watch: {}
    }
</script>

<style>
    .rename-button-content {
        display: inline-block;
        margin-right: 10px;
    }
</style>