<template>
    <div>
        <div class="recycle-button-content">
            <div class="restore-button-content">
                <el-button type="success" size="medium" round @click="restoreRecycle">
                    还原<i class="el-icon-refresh-left el-icon--right"/>
                </el-button>
            </div>
            <div class="clean-button-content">
                <el-button type="danger" size="medium" round @click="cleanRecycle">
                    清空回收站<i class="el-icon-delete el-icon--right"/>
                </el-button>
            </div>
        </div>
        <div class="recycle-file-list-content">
            <el-table
                    ref="recycleTable"
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
                        prop="fileName"
                        sortable
                        show-overflow-tooltip
                        min-width="750">
                    <template slot-scope="scope">
                        <div class="file-name-content">
                            <i :class="getFileFontElement(scope.row.fileType)"
                               style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                            <span style="cursor:pointer;">{{ scope.row.filename }}</span>
                        </div>
                        <div v-show="scope.row.fileId === operationShowFileId" class="file-operation-content">
                            <el-tooltip class="item" effect="light" content="还原" placement="top">
                                <el-button type="success" size="small" circle @click="restoreOneRecycleFile(scope.row)">
                                    <i class="el-icon-refresh-left"/>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="彻底删除" placement="top">
                                <el-button type="danger" size="small" circle @click="deleteOneRecycleFile(scope.row)">
                                    <i class="el-icon-delete"/>
                                </el-button>
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
                        label="删除日期"
                        min-width="240"
                        :formatter="formatUpdateTime">
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>

    import recycleService from '../../../api/recycle'
    import panUtil from '../../../utils/common'

    export default {
        name: "Recycle",
        components: {},
        props: {},
        data() {
            return {
                tableData: [],
                tableHeight: window.innerHeight - 200,
                multipleSelection: [],
                operationShowFileId: ''
            }
        },
        methods: {
            cleanRecycle() {
                let _this = this
                if (_this.tableData && _this.tableData.length > 0) {
                    let fileIdArr = new Array()
                    _this.tableData.forEach(item => {
                        fileIdArr.push(item.fileId)
                    })
                    _this.doDeleteRecycle(fileIdArr.join('__,__'))
                }
            },
            doDeleteRecycle(fileIds) {
                let _this = this
                _this.$confirm('文件删除后将不可恢复，您确定这样做吗？', '删除文件', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    recycleService.deleteRecycle({
                        fileIds: fileIds
                    }, res => {
                        _this.$message.success('删除成功')
                        _this.tableData = res.data
                    }, res => {
                        _this.$message.error(res.message)
                    })
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
            restoreOneRecycleFile(row) {
                this.doRestoreRecycle(row.fileId)
            },
            deleteOneRecycleFile(row) {
                this.doDeleteRecycle(row.fileId)
            },
            restoreRecycle() {
                let _this = this
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => {
                        fileIdArr.push(item.fileId)
                    })
                    _this.doRestoreRecycle(fileIdArr.join('__,__'))
                    return
                }
                _this.$message.error('请选择要还原的文件')
            },
            doRestoreRecycle(fileIds) {
                let _this = this
                recycleService.restoreRecycle({
                    fileIds: fileIds
                }, res => {
                    _this.$message.success('文件还原成功')
                    _this.tableData = res.data
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            loadTableData() {
                let _this = this
                recycleService.recycles(res => {
                    _this.tableData = res.data
                }, res => {
                    _this.$message.error(res.message)
                })
            }
        },
        computed: {},
        mounted() {
            this.loadTableData()
        },
        watch: {}
    }
</script>

<style scoped>

    .recycle-button-content {
        display: inline-block;
    }

    .recycle-button-content .restore-button-content {
        margin: 20px 0 0 30px;
    }

    .recycle-button-content .clean-button-content {
        position: absolute;
        top: 20px;
        right: 30px;
    }

    .recycle-file-list-content {
        width: 100%;
        height: 100%;
        margin-top: 20px;
    }

    .file-operation-content {
        display: inline-block;
        position: absolute;
        right: 100px;
        top: 8px;
    }

</style>