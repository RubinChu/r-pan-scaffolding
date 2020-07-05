<template>
    <div>
        <pan-header/>
        <div class="iframe-content">
            <iframe class="iframe" :src="showPath" frameborder="0"/>
        </div>
    </div>
</template>

<script>
    import PanHeader from "../../components/header/PanHeader";
    import fileService from "../../api/file/file-service";

    export default {
        name: "Iframe",
        components: { PanHeader },
        data() {
            return {
                showPath: ''
            }
        },
        methods: {
            init() {
                let fileId = this.$route.params.fileId,
                    _this = this
                fileService.detail({fileId: fileId}, function (res) {
                    if (res.status === 0) {
                        _this.showPath = res.data.showPath
                    } else {
                        _this.$message.error(res.message)
                    }
                }, function (err) {
                    _this.$message.error(err)
                })
            }
        },
        computed: {},
        mounted() {
            this.init()
        }
    }
</script>

<style scoped>
    .iframe-content {
        width: 100%;
        margin-top: 62px;
        display: block;
        text-align: center;
    }
    .iframe {
        width: 100%;
        height: 1200px;
    }
</style>
