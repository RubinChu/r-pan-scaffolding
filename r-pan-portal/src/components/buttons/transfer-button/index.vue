<template>
    <div class="transfer-button-content">
        <el-button v-if="roundFlag" :size="size" round @click="transferFile">
            移动到
        </el-button>
        <el-button v-if="circleFlag" icon="el-icon-position" :size="size" circle @click="transferFile">
        </el-button>
        <el-dialog
                title="移动文件"
                :visible.sync="treeDialogVisible"
                @open="loadTreeData"
                @closed="resetTreeData"
                width="30%"
                :append-to-body=true
                :modal-append-to-body=false
                :center=true>
            <div>
                <el-tree
                        :data="treeData"
                        empty-text="暂无文件夹数据"
                        default-expand-all
                        highlight-current
                        check-on-click-node
                        :expand-on-click-node="false"
                        ref="tree">
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                        <i class="fa fa-folder-o" style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                        <span>{{ node.label }}</span>
                    </span>
                </el-tree>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="treeDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doChoseTreeNodeCallBack" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>

    import fileService from '../../../api/file'

    export default {
        name: 'TransferButton',
        components: {},
        props: {
            roundFlag: Boolean,
            circleFlag: Boolean,
            size: String,
            parentId: String,
            multipleSelection: Array,
            item: Object
        },
        data() {
            return {
                treeDialogVisible: false,
                treeData: [],
                loading: false
            }
        },
        methods: {
            transferFile() {
                let _this = this
                if (!_this.item && (!_this.multipleSelection || _this.multipleSelection.length == 0)) {
                    _this.$message.error('请选择要移动的文件')
                    return
                }
                _this.treeDialogVisible = true
            },
            doTransferFile(targetParentId) {
                let _this = this,
                    fileIds = ''
                if (_this.item) {
                    fileIds = _this.item.fileId
                } else {
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => fileIdArr.push(item.fileId))
                    fileIds = fileIdArr.join('__,__')
                }
                fileService.transfer({
                    fileIds: fileIds,
                    parentId: _this.parentId,
                    targetParentId: targetParentId
                }, res => {
                    _this.loading = false
                    _this.treeDialogVisible = false
                    _this.$message.success('文件移动成功')
                    _this.$emit('refresh', res.data)
                }, res => {
                    _this.loading = false
                    _this.$message.error(res.message)
                })
            },
            doChoseTreeNodeCallBack() {
                this.loading = true
                let checkNode = this.$refs.tree.getCurrentNode()
                if (!checkNode) {
                    this.$message.error('请选择文件夹')
                    this.loading = false
                    return
                }
                this.doTransferFile(checkNode.id)
            },
            resetTreeData() {
                this.treeData = new Array()
            },
            loadTreeData() {
                let _this = this
                fileService.getFolderTree(res => {
                    _this.treeData = res.data
                }, res => {
                    _this.$message.error(res.message)
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

    .transfer-button-content {
        display: inline-block;
        margin-right: 10px;
    }

</style>