<template>
    <div class="download-button-content">
        <el-button v-if="roundFlag" type="danger" :size="size" round @click="deleteFile">
            删除<i class="el-icon-delete el-icon--right"/>
        </el-button>
        <el-button v-if="circleFlag" type="danger" :size="size" circle @click="deleteFile">
            <i class="el-icon-delete"/>
        </el-button>
    </div>
</template>

<script>

    import fileService from '../../../api/file'

    export default {
        name: 'DeleteButton',
        components: {},
        props: {
            roundFlag: Boolean,
            circleFlag: Boolean,
            size: String,
            multipleSelection: Array,
            parentId: String,
            item: Object
        },
        data() {
            return {}
        },
        methods: {
            deleteFile() {
                let _this = this
                if (_this.item) {
                    _this.doDeleteFile(_this.item.fileId)
                    return
                }
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => fileIdArr.push(item.fileId))
                    _this.doDeleteFile(fileIdArr.join('__,__'))
                    return
                }
                _this.$message.error('请选择要删除的文件')
            },
            doDeleteFile(fileIds) {
                let _this = this
                _this.$confirm('文件删除后将保存在回收站，您确定这样做吗？', '删除文件', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    fileService.delete({
                        fileIds: fileIds,
                        parentId: _this.parentId
                    }, res => {
                        _this.$message.success('删除成功')
                        _this.$emit('refresh', res.data)
                    }, res => {
                        _this.$message.error(res.message)
                    })
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
    .download-button-content {
        display: inline-block;
        margin-right: 10px;
    }
</style>