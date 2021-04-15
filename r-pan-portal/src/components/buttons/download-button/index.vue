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
                if (!_this.item && _this.multipleSelection.length > 1) {
                    _this.$message.error('请选择一个文件进行下载')
                    return
                }
                let item = _this.item ? _this.item : _this.multipleSelection[0]
                if (item.folderFlag === 1) {
                    _this.$message.error('文件夹暂不支持下载')
                    return
                }
                _this.doDownload(item)
            },
            doDownload(item) {
                let url = panUtil.getUrlPrefix() + '/file/download?fileId=' + item.fileId + '&token=' + getToken(),
                    filename = item.filename,
                    link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', filename)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
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