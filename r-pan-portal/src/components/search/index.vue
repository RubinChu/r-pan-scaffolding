<template>
    <div class="pan-main-search-content">
        <el-autocomplete
                class="pan-main-search"
                v-model="searchKey"
                placeholder="请输入内容"
                size="small"
                @keyup.enter.native="doSearch"
                :fetch-suggestions="querySearchHistory"
                @select="handleSelect"
        >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
        </el-autocomplete>
    </div>
</template>

<script>

    import userService from '../../api/user'
    import fileSearch from '../../api/file'

    export default {
        name: 'Search',
        components: {},
        props: {
            fileTypes: String
        },
        data() {
            return {
                searchKey: ''
            }
        },
        methods: {
            querySearchHistory(queryString, cb) {
                let _this = this
                userService.searchHistories(res => {
                    cb(res.data)
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            doSearch() {
                let _this = this
                fileSearch.search({
                    keyword: _this.searchKey,
                    fileTypes: _this.fileTypes
                }, res => {
                    _this.$emit('refresh', res.data, _this.searchKey)
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            handleSelect(item) {
                this.searchKey = item.value
                this.doSearch()
            }
        },
        computed: {},
        mounted() {

        },
        watch: {}
    }
</script>

<style>

    .pan-main-search-content {
        position: absolute;
        right: 35px;
        height: 60px;
        padding: 20px 0 0 13px;
    }

    .pan-main-search-content .pan-main-search {
        width: 250px;
    }

    .pan-main-search-content .pan-main-search input {
        border-radius: 30px;
    }

</style>