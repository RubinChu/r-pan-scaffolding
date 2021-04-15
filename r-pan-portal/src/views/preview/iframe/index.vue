<template>
    <div>
        <pan-header/>
        <div class="iframe-content">
            <iframe class="iframe" :src="showPath" frameborder="0"/>
        </div>
    </div>
</template>

<script>

    import PanHeader from '../../../components/header'
    import panUtil from '../../../utils/common'

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
                let _this = this,
                    fileId = _this.$route.params.fileId
                _this.showPath = panUtil.getPreviewUrl(fileId)
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
