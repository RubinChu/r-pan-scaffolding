<template>
    <div class="create-folder-button-content">
        <el-button v-if="roundFlag" type="success" :size="size" round @click="createFolderDialogVisible = true">
            新建文件夹<i class="el-icon-folder-add el-icon--right"/>
        </el-button>
        <el-button v-if="circleFlag" type="success" :size="size" circle @click="createFolderDialogVisible = true">
            <i class="el-icon-folder-add"/>
        </el-button>
        <el-dialog
                title="新建文件夹"
                :visible.sync="createFolderDialogVisible"
                width="30%"
                @opened="focusInput('folderName')"
                @closed="resetForm('createFolderForm')"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-form label-width="100px" :rules="createFolderRules" ref="createFolderForm"
                         :model="createFolderForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="文件夹名称" prop="folderName">
                        <el-input type="text"
                                  ref="folderName"
                                  @keyup.enter.native="doCreateFolder"
                                  v-model="createFolderForm.folderName" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="createFolderDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doCreateFolder" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import fileService from '../../../api/file'

    export default {
        name: 'CreateFolder',
        components: {},
        props: {
            roundFlag: Boolean,
            circleFlag: Boolean,
            size: String,
            parentId: String
        },
        data() {
            return {
                createFolderDialogVisible: false,
                createFolderRules: {
                    folderName: [
                        {required: true, message: '请输入文件夹名称', trigger: 'blur'}
                    ]
                },
                createFolderForm: {
                    folderName: ''
                },
                loading: false
            }
        },
        methods: {
            focusInput(refName) {
                this.$refs[refName].focus()
            },
            resetForm(refName) {
                this.$refs[refName].resetFields()
            },
            doCreateFolder() {
                let _this = this
                _this.$refs.createFolderForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        fileService.createFolder({
                            parentId: _this.parentId,
                            folderName: _this.createFolderForm.folderName
                        }, res => {
                            _this.loading = false
                            _this.createFolderDialogVisible = false
                            _this.$message.success('新建成功')
                            _this.$emit('refresh', res.data)
                        }, res => {
                            _this.$message.error(res.message)
                            _this.loading = false
                        })
                    }
                })
            }
        },
        computed: {},
        mounted() {
        },
        watch: {}
    }
</script>

<style>

    .create-folder-button-content {
        display: inline-block;
        margin-right: 10px;
    }

</style>