/**
* Created by rubin on 2020/7/5.
*/

<template>
    <div>
        <pan-header/>
        <div class="pan-video-content">
            <div class="video-name-content">
                <p class="video-name">{{ videoName }}</p>
            </div>
            <el-divider></el-divider>
            <el-row>
                <el-col :span="18">
                    <!--视频播放容器-->
                    <div class="video-content">
                        <video id="r_pan_video_player" :src="videoShowPath" autoplay="true" controls="true"/>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="video-list-content">
                        <el-menu class="video-list"
                                 :default-active="activeIndex"
                                 @select="selectVideo">
                            <el-menu-item v-for="(item, index) in videoList" :key="index" :index="item.fileId">
                                <i class="fa fa-video-camera"></i>
                                <span slot="title">{{ item.filename }}</span>
                            </el-menu-item>
                        </el-menu>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>

    import PanHeader from '../../../components/header'
    import fileService from '../../../api/file'
    import panUtil from '../../../utils/common'

    export default {
        name: 'Video',
        data() {
            return {
                videoName: '',
                activeIndex: '',
                videoShowPath: '',
                videoList: []
            }
        },
        components: { PanHeader },
        methods: {
            init() {
                let _this = this
                fileService.list({
                    parentId: _this.$route.params.parentId,
                    fileTypes: '9'
                }, function (res) {
                    if (res.status === 0) {
                        _this.renderVideoList(res.data)
                        _this.listenVideoPlayer()
                    } else {
                        _this.$message.error(res.message)
                    }
                }, function (err) {
                    _this.$message.error(err)
                })
            },
            selectVideo(index) {
                this.activeIndex = index
                this.videoList.some(item => {
                    if (item.fileId === index) {
                        this.videoName = item.fileName
                        this.videoShowPath = panUtil.getPreviewUrl(item.fileId)
                        return true
                    }
                })
            },
            renderVideoList(dataList) {
                let _this = this
                _this.videoList = new Array()
                dataList.forEach((item, index) => {
                    item.filename = item.filename.substring(0, item.filename.lastIndexOf('.'))
                    if (item.filename.length > 15) {
                        item.filename = item.filename.substring(0, 16) + '...'
                    }
                    if (item.fileId === _this.$route.params.fileId) {
                        _this.videoName = item.filename
                        _this.videoShowPath = panUtil.getPreviewUrl(item.fileId)
                    }
                    _this.videoList.push(item)
                    _this.activeIndex = _this.$route.params.fileId
                })
            },
            listenVideoPlayer() {
                let _this = this
                document.getElementById('r_pan_video_player').addEventListener('ended', () => {
                    _this.selectNext(_this.activeIndex)
                }, false)
            },
            selectNext(currentFileId) {
                let i
                this.videoList.some((item, index) => {
                    if (item.fileId === currentFileId) {
                        i = index
                        return true
                    }
                })
                if (i === this.videoList.length - 1) {
                    return
                }
                let item = this.videoList[++i]
                this.videoName = item.filename
                this.videoShowPath = panUtil.getPreviewUrl(item.fileId)
                this.activeIndex = item.fileId
            }
        },
        mounted() {
            //确保页面元素加载完成之后调用
            this.init()
        },
        watch: {}
    }
</script>

<style scoped>

    .pan-video-content {
        width: 100%;
        margin-top: 62px;
        display: block;
    }

    .pan-video-content .video-name-content {
        display: block;
        width: 100%;
        text-align: center;
        padding: 10px 0 0 0;
    }

    .pan-video-content .video-name-content .video-name {
        color: #409EFF;
        font-size: 35px;
        font-weight: bold;
        font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    }

    .pan-video-content .video-content {
        width: 100%;
        height: 100px;
        display: block;
        text-align: center;
    }

    .pan-video-content .video-content #r_pan_video_player {
        display: inline-block;
        width: 100%;
        height: 500px;
        line-height: 500px;
    }

    .pan-video-content .video-list-content {
        display: block;
        margin: 0 auto;
        width: 250px;
        height: 500px;
        line-height: 500px;
        overflow: hidden;
    }

    .pan-video-content .video-list-content .video-list {
        width: 100%;
        height: 100%;
        overflow: scroll;
    }

    .pan-video-content .video-list i {
        margin-right: 15px;
    }

</style>
