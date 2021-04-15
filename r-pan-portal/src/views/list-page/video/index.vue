<template>
    <div>
        <div class="button-search-content">
            <file-button-group :button-array="buttonArray" :parent-id="parentId" :multiple-selection="multipleSelection" @refresh="refresh"/>
            <search @refresh="searchList" :file-types="fileTypes"/>
        </div>
        <bread-crumb :breadCrumbs="breadCrumbs" :normal="normal" :category-name="categoryName" :file-types="fileTypes" @refresh="refresh" @reload="reloadBreadCrumbs"/>
        <file-table :table-data="tableData" :parent-id="parentId" :file-types="fileTypes" @refresh="refresh" @handleSelectionChange="handleSelectionChange"/>
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
        name: "Videos",
        components: { FileButtonGroup, Search, BreadCrumb, FileTable },
        props: [],
        data() {
            return {
                breadCrumbs: [],
                normal: false,
                tableData: [],
                parentId: '',
                defaultParentId: '-1',
                multipleSelection: [],
                fileTypes: '9',
                categoryName: '分类：视频',
                buttonArray: ['upload', 'download', 'delete', 'rename', 'copy', 'transfer']
            }
        },
        methods: {
            initBreadCrumb() {
                let _this = this
                userService.info(res => {
                    let item = {
                        id: '-1',
                        name: _this.categoryName
                    }
                    _this.breadCrumbs = new Array()
                    _this.breadCrumbs.push(item)
                    _this.parentId = res.data.rootFileId
                    _this.loadVideoData()
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            loadVideoData() {
                let _this = this
                fileService.list({
                    parentId: _this.defaultParentId,
                    fileTypes: _this.fileTypes
                }, res => {
                    _this.tableData = res.data
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            refresh(fileList) {
                this.loadVideoData()
            },
            reloadBreadCrumbs(newBreadCrumbs) {
                this.breadCrumbs = newBreadCrumbs
            },
            searchList(fileList, searchKey) {
                this.breadCrumbs = new Array()
                let item = {
                    id: '-1',
                    name: '搜索:' + searchKey
                }
                this.breadCrumbs.push(item)
                this.tableData  = fileList
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