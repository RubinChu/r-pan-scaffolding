<template>
    <div class="upload-button-content">
        <el-button v-if="roundFlag" type="primary" id="uploadButton" :size="size" round>
            上传<i class="el-icon-upload el-icon--right"/>
        </el-button>
        <el-button v-if="circleFlag" size="size" id="uploadButton" circle>
            <i class="el-icon-upload"/>
        </el-button>
        <el-dialog
                title="文件上传"
                :show-close=false
                :close-on-click-modal=false
                :close-on-press-escape=false
                :visible.sync="uploadProcessDialogVisible"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div
                    v-loading="loading"
                    element-loading-text="文件解析中，请稍候。。。"
                    class="upload-process-content">
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
    import plupload from 'plupload'
    import {MD5} from '../../md5'
    import fileService from '../../../api/file'

    export default {
        name: 'uploadButton',
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
                fileList: [],
                // 详细文档地址：https://blog.csdn.net/sunwork888/article/details/100976725
                fileOptions: {
                    browse_button: 'uploadButton',
                    url: panUtil.getUrlPrefix() + '/file/upload',
                    filters: {
                        max_file_size: panUtil.getMaxFileSize(),
                        prevent_duplicates: false
                    },
                    headers: {
                        Authorization: getToken()
                    },
                    multipart: true,
                    chunk_size: panUtil.getChunkSize(),
                    multi_selection: true,
                    unique_names: false,
                    file_data_name: 'file',
                    flash_swf_url: 'script/Moxie.swf',
                    silverlight_xap_url: 'script/Moxie.xap'
                },
                uploader: undefined,
                loading: false
            }
        },
        methods: {
            initUploader() {
                // 实例化一个plupload上传对象
                this.uploader = new plupload.Uploader(this.fileOptions)
                this.uploader.init()
                // 绑定进队列
                this.uploader.bind("FilesAdded", this.filesAdded)
                // 绑定进度
                this.uploader.bind("UploadProgress", this.uploadProgress)
                // 上传之前
                this.uploader.bind("BeforeUpload", this.beforeUpload)
                // 上传成功监听
                this.uploader.bind("FileUploaded", this.fileUploaded)
                // 上传全部完成调用
                this.uploader.bind("UploadComplete", this.uploadComplete)
                // 上传出错调用
                this.uploader.bind("Error", this.uploadError)
            },
            filesAdded(uploader, files) {
                let _this = this
                _this.loading = true
                _this.uploadProcessDialogVisible = true
                files.forEach((f) => {
                    MD5(f.getNative(), (e, md5) => {
                        f['md5'] = md5
                        let md5FileItem = _this.getFileItem(f.name)
                        md5FileItem.md5 = md5
                        fileService.secUpload({
                            name: f.name,
                            md5: md5,
                            parentId: _this.parentId
                        }, res => {
                            if (res.status === 0) {
                                f['status'] = 5
                                md5FileItem.percent = 100
                                md5FileItem.status = 'success'
                                md5FileItem.done = true
                            }
                            if (_this.checkAllReadyToUpload()) {
                                _this.loading = false
                                uploader.refresh()
                                uploader.start()
                            }
                        }, res => {
                            if (_this.checkAllReadyToUpload()) {
                                _this.loading = false
                                uploader.refresh()
                                uploader.start()
                            }
                        })
                    })
                    let fileItem = {
                        md5: undefined,
                        filename: f.name,
                        percent: 0,
                        status: undefined,
                        done: false
                    }
                    _this.fileList.push(fileItem)
                })
            },
            uploadProgress(uploader, file) {
                let fileItem = this.getFileItem(file.name)
                fileItem.percent = Math.floor(file.percent)
            },
            getFileItem(filename) {
                for (let i = 0, iLength = this.fileList.length; i < iLength; i++) {
                    if (this.fileList[i].filename === filename) {
                        return this.fileList[i]
                    }
                }
            },
            beforeUpload(uploader, file) {
                uploader.setOption('multipart_params', {'size': file.size, 'md5': file.md5, 'parentId': this.parentId})
            },
            fileUploaded(uploader, file, responseObject) {
                let fileItem = this.getFileItem(file.name),
                    res = JSON.parse(responseObject['response'])
                if (res.status === 0) {
                    fileItem.status = 'success'
                } else {
                    fileItem.status = 'exception'
                }
                fileItem.done = true
                uploader.removeFile(file)
            },
            uploadComplete(uploader, files) {
                this.$message.success('上传完成')
                this.$emit('refresh', undefined)
                this.uploadProcessDialogVisible = false
                this.fileList = new Array()
                uploader.refresh()
            },
            uploadError(uploader, errObject) {
                if (errObject.code === -600) {
                    this.$message.error('文件' + errObject.file.name + '大小超过了3个G的限制！')
                } else {
                    this.$message.error(errObject.message)
                }
                uploader.stop()
                uploader.refresh()
            },
            checkAllReadyToUpload() {
                for (let i = 0, iLength = this.fileList.length; i < iLength; i++) {
                    if (!this.fileList[i].md5) {
                        return false
                    }
                }
                return true
            }
        },
        computed: {},
        mounted() {
            this.initUploader()
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