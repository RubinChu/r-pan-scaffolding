/**
* Created by rubin on 2020/7/5.
*/

<template>
    <div>
        <pan-header/>
        <div class="pan-music-content">
            <div class="music-name-content">
                <p class="music-name">{{musicName}}</p>
            </div>
            <el-divider></el-divider>
            <el-row>
                <el-col :span="18">
                    <div class="record-img-content">
                        <img class="record-img" src="@/assets/imgs/record.png">
                    </div>
                    <!--音频播放容器-->
                    <div class="music-content">
                        <audio id="r_pan_music_player" :src="musicShowPath" autoplay="true" controls="true"/>
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="music-list-content">
                        <el-menu class="music-list"
                                 :default-active="activeIndex"
                                 @select="selectMusic">
                            <el-menu-item v-for="(item, index) in musicList" :key="index" :index="item.fileId">
                                <i class="fa fa-music"></i>
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
        name: 'Music',
        data() {
            return {
                musicName: '',
                musicList: [],
                musicShowPath: '',
                activeIndex: '0'
            }
        },
        components: { PanHeader },
        methods: {
            init() {
                let _this = this
                fileService.list({
                    parentId: _this.$route.params.parentId,
                    fileTypes: '8'
                }, res => {
                    _this.renderMusicList(res.data)
                    _this.listenMusicPlayer()
                }, res => {
                    _this.$message.error(res.message)
                })
            },
            selectMusic(index, indexPath) {
                this.activeIndex = index
                this.musicList.some(item => {
                    if (item.fileId === index) {
                        this.musicName = item.filename
                        this.musicShowPath = panUtil.getPreviewUrl(item.fileId)
                        return true
                    }
                })
            },
            renderMusicList(dataList) {
                let _this = this
                _this.musicList = new Array()
                dataList.forEach((item, index) => {
                    item.filename = item.filename.substring(0, item.filename.lastIndexOf('.'))
                    if (item.filename.length > 15) {
                        item.filename = item.filename.substring(0, 16) + '...'
                    }
                    if (item.fileId === _this.$route.params.fileId) {
                        _this.musicName = item.filename
                        _this.musicShowPath = panUtil.getPreviewUrl(item.fileId)
                    }
                    _this.musicList.push(item)
                    _this.activeIndex = _this.$route.params.fileId
                })
            },
            selectNext(currentFileId) {
                let i
                this.musicList.some((item, index) => {
                    if (item.fileId === currentFileId) {
                        i = index
                        return true
                    }
                })
                if (i === this.musicList.length - 1) {
                    return
                }
                let item = this.musicList[++i]
                this.musicName = item.filename
                this.musicShowPath = panUtil.getPreviewUrl(item.fileId)
                this.activeIndex = item.fileId
            },
            listenMusicPlayer() {
                let _this = this
                document.getElementById('r_pan_music_player').addEventListener('ended', () => {
                    _this.selectNext(_this.activeIndex)
                }, false)
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
    .pan-music-content {
        width: 100%;
        margin-top: 62px;
        display: block;
    }

    .pan-music-content .music-name-content {
        display: block;
        width: 100%;
        text-align: center;
        padding: 10px 0 0 0;
    }

    .pan-music-content .music-name-content .music-name {
        color: #409EFF;
        font-size: 35px;
        font-weight: bold;
        font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
    }

    .pan-music-content .record-img-content {
        display: inline-block;
        width: 100%;
        height: 100%;
        margin-top: 80px;
        text-align: center;
    }

    .pan-music-content .record-img-content .record-img {
        width: 300px;
    }

    .pan-music-content .music-content {
        width: 100%;
        height: 100px;
        display: block;
        margin-top: 60px;
        text-align: center;
        position: relative;
    }

    .pan-music-content .music-content #r_pan_music_player {
        display: inline-block;
        width: 100%;
        position: absolute;
        bottom: 10px;
        left: 0;
    }

    .pan-music-content .music-list-content {
        display: block;
        margin: 0 auto;
        width: 250px;
        height: 500px;
        line-height: 500px;
        overflow: hidden;
    }

    .pan-music-content .music-list-content .music-list {
        width: 100%;
        height: 100%;
        overflow: scroll;
    }

    .pan-music-content .music-list i {
        margin-right: 15px;
    }

</style>
