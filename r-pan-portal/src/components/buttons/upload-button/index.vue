<template>
    <div class="upload-button-content">
        <el-upload
                :headers="uploadHeaders"
                :action="uploadUrl"
                :data="uploadData"
                :show-file-list="false"
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
                title="上传中..."
                :show-close=false
                :close-on-click-modal=false
                :close-on-press-escape=false
                :visible.sync="uploadProcessDialogVisible"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-progress :percentage="percent"/>
            </div>
        </el-dialog>
    </div>
</template>

<script>

    import { getToken } from '../../../utils/cookie'
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
                percent: 0
            }
        },
        methods: {
            beforeUpload(file) {
                const limitFlag = file.size / 1024 / 1024 < 3072
                if (!limitFlag) {
                    this.$message.error('上传文件大小大小不能超过3072MB')
                    return limitFlag
                }
                this.uploadProcessDialogVisible = true
            },
            uploadProcess(event, file, fileList) {
                this.percent = Math.floor(event.percent)
            },
            uploadSuccess(res, file, fileList) {
                if (res.status === 0) {
                    this.$message.success('上传成功')
                    this.uploadProcessDialogVisible = false
                    this.percent = 0
                    this.$emit('refresh', res.data)
                } else {
                    this.$message.error('上传失败')
                    this.uploadProcessDialogVisible = false
                    this.percent = 0
                }
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
        mounted() {},
        watch: {}
    }
</script>

<style>
    .upload-button-content{
        display: inline-block;
        margin-right: 10px;
    }
</style>