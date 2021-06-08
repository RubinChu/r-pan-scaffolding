<template>
    <div class="upload-button-content">
        <el-upload
                ref="upload"
                :headers="uploadHeaders"
                :action="uploadUrl"
                :data="uploadData"
                :show-file-list="false"
                :multiple="true"
                :limit="6"
                :on-exceed="onExceed"
                :before-upload="beforeUpload"
                :on-progress="uploadProcess"
                :on-success="uploadSuccess">
            <el-button v-if="roundFlag" type="primary" :size="size" round>
                上传<i class="el-icon-upload el-icon--right"/>
            </el-button>
            <el-button v-if="circleFlag" size="size" circle>
                <i class="el-icon-upload"/>
            </el-button>
        </el-upload>
        <el-dialog
                title="文件上传"
                :show-close=false
                :close-on-click-modal=false
                :close-on-press-escape=false
                :visible.sync="uploadProcessDialogVisible"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div class="upload-process-content">
                <div v-for="file in fileList" class="upload-process-item-content">
                    <div class="upload-item-file-name">
                        <span>{{file.filename}}</span>
                    </div>
                    <div>
                        <el-progress type="dashboard" :percentage="file.percent" :color="colors" :status="file.status"/>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import {getToken} from '../../../utils/cookie'
    import panUtil from '../../../utils/common'

    export default {
        name: 'UploadButton',
        components: {},
        props: {
            roundFlag: Boolean,
            circleFlag: Boolean,
            size: String,
            parentId: String
        },
        data() {
            return {
                uploadProcessDialogVisible: false,
                uploadUrl: panUtil.getUrlPrefix() + '/file/upload',
                percent: 80,
                colors: [
                    {color: '#909399', percentage: 30},
                    {color: '#e6a23c', percentage: 70},
                    {color: '#67c23a', percentage: 100}
                ],
                fileList: []
            }
        },
        methods: {
            beforeUpload(file) {
                let fileItem = {
                    filename: file.name,
                    percent: 0,
                    status: undefined,
                    done: false
                }
                this.fileList.push(fileItem)
                const limitFlag = file.size / 1024 / 1024 < 3072
                if (!limitFlag) {
                    this.$message.error('上传文件大小大小不能超过3072MB')
                    fileItem.status = 'exception'
                    fileItem.done = true
                    return limitFlag
                }
                this.uploadProcessDialogVisible = true
            },
            uploadProcess(event, file, fileList) {
                let fileItem = this.getFileItem(file.name)
                fileItem.percent = Math.floor(event.percent)
            },
            uploadSuccess(res, file, fileList) {
                let fileItem = this.getFileItem(file.name)
                if (res.status === 0) {
                    fileItem.status = 'success'
                } else {
                    fileItem.status = 'exception'
                }
                fileItem.done = true
                if (this.checkAllDone()) {
                    this.$message.success('上传完成')
                    this.$emit('refresh', undefined)
                    this.uploadProcessDialogVisible = false
                    this.fileList = new Array()
                    this.$refs.upload.clearFiles()
                }
            },
            onExceed(files, fileList) {
                this.$message.error('文件最多支持上传6个')
            },
            getFileItem(filename) {
                for (let i = 0, iLength = this.fileList.length; i < iLength; i++) {
                    if (this.fileList[i].filename === filename) {
                        return this.fileList[i]
                    }
                }
            },
            checkAllDone() {
                for (let i = 0, iLength = this.fileList.length; i < iLength; i++) {
                    if (!this.fileList[i].done) {
                        return false
                    }
                }
                return true
            }
        },
        computed: {
            uploadHeaders() {
                return {
                    Authorization: getToken()
                }
            },
            uploadData() {
                return {
                    parentId: this.parentId
                }
            }
        },
        mounted() {
        },
        watch: {}
    }
</script>

<style>
    .upload-button-content {
        display: inline-block;
        margin-right: 10px;
    }

    .upload-process-content {
        width: 600px;
        height: 100%;
        display: block;
        text-align: center;
        margin: 0 auto;
    }

    .upload-process-content .upload-process-item-content {
        width: 180px;
        height: 200px;
        margin: 0 10px 10px 10px;
        display: inline-block;
    }

    .upload-process-content .upload-process-item-content .upload-item-file-name {
        margin-bottom: 10px;
    }
</style>