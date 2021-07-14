<template>
    <div class="download-button-content">
        <el-button v-if="roundFlag" type="info" :size="size" round @click="downloadFile" :loading="loading">
            下载<i class="el-icon-download el-icon--right"/>
        </el-button>
        <el-button v-if="circleFlag" type="info" :size="size" circle @click="downloadFile" :loading="loading">
            <i class="el-icon-download"/>
        </el-button>
    </div>
</template>

<script>

    import panUtil from '../../../utils/common'
    import {getToken} from '../../../utils/cookie'

    export default {
        name: 'DownLoadButton',
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
                loading: false
            }
        },
        methods: {
            downloadFile() {
                let _this = this
                if (!_this.item && (!_this.multipleSelection || _this.multipleSelection.length === 0)) {
                    _this.$message.error('请选择要下载的文件')
                    return
                }
                if (!_this.item) {
                    for (let i = 0, iLength = _this.multipleSelection.length; i < iLength; i++) {
                        if (_this.multipleSelection[i].folderFlag === 1) {
                            _this.$message.error('文件夹暂不支持下载')
                            return
                        }
                    }
                    _this.doDownLoads(_this.multipleSelection)
                }
                if (_this.item) {
                    if (_this.item.folderFlag === 1) {
                        _this.$message.error('文件夹暂不支持下载')
                        return
                    }
                    _this.doDownload(_this.item)
                }
            },
            doDownload(item) {
                let url = panUtil.getUrlPrefix() + '/file/download?fileId=' + item.fileId + '&authorization=' + getToken(),
                    filename = item.filename,
                    link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', filename)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
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
            }
        },
        computed: {},
        mounted() {
        },
        watch: {}
    }
</script>

<style>

    .download-button-content {
        display: inline-block;
        margin-right: 10px;
    }

</style>