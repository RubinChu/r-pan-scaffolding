<template>
    <div class="pan-main-breadcrumb-content">
        <el-breadcrumb style="display: inline-block;">
            <el-breadcrumb-item>
                <a class="breadcrumb-item-a" @click="goBack" href="#">返回</a>
            </el-breadcrumb-item>
        </el-breadcrumb>
        <el-divider direction="vertical" style="vertical-align: top !important;"/>
        <el-breadcrumb separator-class="el-icon-arrow-right" style="display: inline-block;">
            <el-breadcrumb-item v-for="(item, index) in breadCrumbs" :key="index">
                <a class="breadcrumb-item-a" @click="goToThis(item.id)" href="#">{{ item.name }}</a>
            </el-breadcrumb-item>
        </el-breadcrumb>
    </div>
</template>

<script>

    import fileService from '../../api/file'
    import userService from '../../api/user'

    export default {
        name: 'BreadCrumb',
        components: {},
        props: {
            breadCrumbs: Array,
            normal: Boolean,
            fileTypes: String,
            categoryName: String
        },
        data() {
            return {
            }
        },
        methods: {
            reload() {
                let _this = this
                userService.info(res => {
                    let item = {
                        id: res.data.rootFileId,
                        name: res.data.rootFilename
                    }
                    let newBreadCrumbs = new Array()
                    newBreadCrumbs.push(item)
                   _this.doReload(newBreadCrumbs)
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            doReload(breadCrumbs) {
                this.$emit('reload', breadCrumbs)
                this.refresh([...breadCrumbs].pop().id)
            },
            goBack() {
                let _this = this
                if (_this.normal) {
                    if (_this.breadCrumbs.length > 1) {
                        let newBreadCrumbs = [..._this.breadCrumbs]
                        newBreadCrumbs.splice(newBreadCrumbs.length - 1, 1)
                        _this.doReload(newBreadCrumbs)
                    }
                } else if (_this.fileTypes === '-1') {
                    _this.reload()
                } else {
                    let newBreadCrumbs = new Array(),
                        item = {
                            id: '-1',
                            name: _this.categoryName
                        }
                    newBreadCrumbs.push(item)
                    _this.doReload(newBreadCrumbs)
                }
            },
            goToThis(id) {
                let newBreadCrumbs = new Array(),
                    _this = this
                if (_this.normal) {
                    _this.breadCrumbs.some(item => {
                        newBreadCrumbs.push(item)
                        if (item.id == id) {
                            return true
                        }
                    })
                    _this.doReload(newBreadCrumbs)
                }
            },
            refresh(id) {
                let _this = this
                fileService.list({
                    parentId: id,
                    fileTypes: _this.fileTypes
                }, res => {
                    _this.$emit('refresh', res.data)
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

    .pan-main-breadcrumb-content {
        width: 100%;
        padding: 10px 0 0 25px;
    }

    .breadcrumb-item-a {
        cursor: pointer !important;
        color: #409EFF !important;
    }

</style>