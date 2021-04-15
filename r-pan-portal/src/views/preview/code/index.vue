<template>
    <div>
        <pan-header/>
        <div class="code-text-content">
            <pre class="layui-code code-text">
                {{codeContent}}
            </pre>
        </div>
    </div>
</template>

<script>

    import PanHeader from '../../../components/header'
    import fileService from '../../../api/file'
    import axios from 'axios'
    import panUtil from '../../../utils/common'

    export default {
        name: "Code",
        components: { PanHeader },
        data() {
            return {
                codeContent: ''
            }
        },
        methods: {
            init() {
                let fileId = this.$route.params.fileId,
                    _this = this
                fileService.detail({fileId: fileId}, function (res) {
                    if (res.status === 0) {
                        _this.renderCode(res.data.fileId, res.data.filename)
                    } else {
                        _this.codeContent = '获取资源失败'
                    }
                }, function (err) {
                    _this.codeContent = '获取资源失败'
                })
            },
            renderCode(fileId, fileName) {
                let _this = this
                axios.get(panUtil.getPreviewUrl(fileId)).then(res => {
                    if (res.status === 200) {
                        _this.codeContent = res.data
                        layui.use('code', function () {
                            layui.code({
                                elem: '.layui-code.code-text',
                                title: fileName,
                                encode: false,
                                about: false
                            })
                        })
                    } else {
                        _this.codeContent = '获取资源失败'
                    }
                }).catch(err => {
                    _this.codeContent = '获取资源失败'
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

    .code-text-content {
        width: 100%;
        margin-top: 62px;
        display: block;
        text-align: center;
    }

    .layui-code.code-text {
        width: 60%;
        display: inline-block;
        text-align: left;
        overflow: auto;
    }

</style>
