<template>
    <div>
        <div class="button-search-content">
            <file-button-group :button-array="buttonArray" :parent-id="parentId" :multiple-selection="multipleSelection"
                               @refresh="refresh"/>
            <search @refresh="searchList" :file-types="fileTypes"/>
        </div>
        <bread-crumb :bread-crumbs="breadCrumbs" :normal="normal" :file-types="fileTypes" @refresh="refresh"
                     @reload="reloadBreadCrumbs"/>
        <file-table :table-data="tableData" :parent-id="parentId" :file-types="fileTypes" @refresh="refresh"
                    @goInFolder="reloadBreadCrumbs" @handleSelectionChange="handleSelectionChange"/>
    </div>
</template>

<script>

    import FileButtonGroup from '../../../components/file-button-group/'
    import Search from '../../../components/search'
    import BreadCrumb from '../../../components/breadcrumb'
    import FileTable from '../../../components/file-table'
    import userService from '../../../api/user'
    import fileService from '../../../api/file'

    export default {
        name: 'Files',
        components: {FileButtonGroup, Search, BreadCrumb, FileTable},
        props: [],
        data() {
            return {
                breadCrumbs: [],
                normal: true,
                tableData: [],
                parentId: '',
                defaultParentId: '',
                multipleSelection: [],
                fileTypes: '-1',
                buttonArray: ['upload', 'createFolder', 'download', 'delete', 'rename', 'share', 'copy', 'transfer']
            }
        },
        methods: {
            initBreadCrumb() {
                let _this = this
                userService.info(res => {
                    let item = {
                        id: res.data.rootFileId,
                        name: res.data.rootFilename
                    }
                    _this.breadCrumbs = new Array()
                    _this.breadCrumbs.push(item)
                    _this.parentId = res.data.rootFileId
                    _this.defaultParentId = res.data.rootFileId
                    _this.loadTableData()
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            loadTableData() {
                let _this = this
                fileService.list({
                    parentId: _this.parentId,
                    fileTypes: _this.fileTypes
                }, res => {
                    _this.refresh(res.data)
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            refresh(fileList) {
                if (fileList) {
                    this.tableData = fileList
                } else {
                    this.loadTableData()
                }
            },
            reloadBreadCrumbs(newBreadCrumbs) {
                this.normal = true
                this.breadCrumbs = newBreadCrumbs
                let id = [...newBreadCrumbs].pop().id
                if (id === '-1') {
                    this.parentId = this.defaultParentId
                    return
                }
                this.parentId = id
            },
            searchList(fileList, searchKey) {
                this.normal = false
                this.breadCrumbs = new Array()
                let item = {
                    id: '-1',
                    name: '搜索:' + searchKey
                }
                this.breadCrumbs.push(item)
                this.refresh(fileList)
            },
            handleSelectionChange(multipleSelection) {
                this.multipleSelection = multipleSelection
            }
        },
        computed: {},
        mounted() {
            this.initBreadCrumb()
        },
        watch: {}
    }
</script>

<style scoped>

    .button-search-content {
        display: flex;
        width: 100%;
    }

</style>