<template>
    <div>
        <div class="share-button-content">
            <div class="cancel-button-content">
                <el-button type="danger" size="medium" round @click="cancelShares">
                    取消分享<i class="el-icon-switch-button el-icon--right"/>
                </el-button>
            </div>
        </div>
        <div class="share-list-content">
            <el-table
                    ref="shareTable"
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
                        label="分享名称"
                        prop="shareName"
                        sortable
                        show-overflow-tooltip
                        min-width="750">
                    <template slot-scope="scope">
                        <div class="share-name-content">
                            <i class="el-icon-share"
                               style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                            <span style="cursor:pointer;">{{ scope.row.shareName }}</span>
                        </div>
                        <div v-show="scope.row.shareId === operationShowShareId" class="share-operation-content">
                            <el-tooltip class="item" effect="light" content="复制链接" placement="top">
                                <el-button type="success" size="small" circle @click="copy(scope.row)">
                                    <i class="el-icon-link"/>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="取消分享" placement="top">
                                <el-button type="danger" size="small" circle @click="cancelShare(scope.row)">
                                    <i class="el-icon-switch-button"/>
                                </el-button>
                            </el-tooltip>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createTime"
                        sortable
                        align="center"
                        label="分享时间"
                        min-width="240"
                        :formatter="formatShareCreateTime">
                </el-table-column>
                <el-table-column
                        prop="shareStatus"
                        sortable
                        label="分享状态"
                        min-width="240"
                        align="center"
                        :formatter="formatShareStatus">
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>

    import shareService from '../../../api/share'
    import panUtil from '../../../utils/common'

    export default {
        name: "Shares",
        components: {},
        props: {},
        data() {
            return {
                tableData: [],
                tableHeight: window.innerHeight - 200,
                multipleSelection: [],
                operationShowShareId: ''
            }
        },
        methods: {
            cancelShares() {
                let _this = this
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let shareIdArr = new Array()
                    _this.multipleSelection.forEach(item => {
                        shareIdArr.push(item.shareId)
                    })
                    _this.doCancelShares(shareIdArr.join('__,__'))
                    return
                }
                _this.$message.error('请选择要取消的分享')
            },
            doCancelShares(shareIds) {
                let _this = this
                _this.$confirm('分享取消后将不可恢复，您确定这样做吗？', '取消分享', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    shareService.cancelShare({
                        shareIds: shareIds
                    }, () => {
                        _this.$message.success('取消分享成功')
                        _this.loadTableData()
                    }, res => {
                        _this.$message.error(res.message)
                    })
                })
            },
            cancelShare(row) {
                this.doCancelShares(row.shareId)
            },
            handleSelectionChange(multipleSelection) {
                this.multipleSelection = multipleSelection
            },
            showOperation(row, column, cell, event) {
                this.operationShowShareId = row.shareId
            },
            hiddenOperation(row, column, cell, event) {
                this.operationShowShareId = ''
            },
            formatShareStatus(row, column, cellValue, index) {
                if (row.shareStatus === 1) {
                    return '有分享文件被删除'
                } else if (row.shareDayType === 0) {
                    return '永久有效'
                } else {
                    return panUtil.formatTime(new Date(row.shareEndTime)) + '到期'
                }
            },
            formatShareCreateTime(row, column, cellValue, index) {
                return panUtil.formatTime(new Date(row.createTime))
            },
            loadTableData() {
                let _this = this
                shareService.getShares(res => {
                    _this.tableData = res.data
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            copy(row) {
                let shareMessage = '链接：' + row.shareUrl + '\n提取码：' + row.shareCode + '\n赶快分享给小伙伴吧！'
                this.$clipboard(shareMessage)
                this.$message.success('复制成功')
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

    .share-button-content {
        display: inline-block;
    }

    .share-button-content .cancel-button-content {
        margin: 20px 0 0 30px;
    }

    .share-list-content {
        width: 100%;
        height: 100%;
        margin-top: 20px;
    }

    .share-operation-content {
        display: inline-block;
        position: absolute;
        right: 100px;
        top: 8px;
    }

</style>