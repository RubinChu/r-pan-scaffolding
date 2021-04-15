<template>
    <div class="pan-nav-content-wrapper">
        <div class="pan-nav-content">
            <ul class="pan-nav-file">
                <li class="pan-nav-file-all">
                    <a @click="openFilePage" :class="{'checked': activeModule === 'files'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon el-icon-document-copy"/>
                            <span>全部文件</span>
                        </span>
                    </a>
                </li>
                <li class="pan-nav-file-pic">
                    <a @click="openImgPage" :class="{'checked': activeModule === 'imgs'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon"/>
                            <span>图片</span>
                        </span>
                    </a>
                </li>
                <li class="pan-nav-file-doc">
                    <a @click="openDocPage" :class="{'checked': activeModule === 'docs'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon"/>
                            <span>文档</span>
                        </span>
                    </a>
                </li>
                <li class="pan-nav-file-video">
                    <a @click="openVideoPage" :class="{'checked': activeModule === 'videos'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon"/>
                            <span>视频</span>
                        </span>
                    </a>
                </li>
                <li class="pan-nav-file-music">
                    <a @click="openMusicPage" :class="{'checked': activeModule === 'musics'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon"/>
                            <span>音乐</span>
                        </span>
                    </a>
                </li>
            </ul>
            <ul class="pan-nav-share">
                <li class="pan-nav-share-li">
                    <a @click="openSharePage" :class="{'checked': activeModule === 'share'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon el-icon-share"/>
                            <span>我的分享</span>
                        </span>
                    </a>
                </li>
            </ul>
            <ul class="pan-nav-recycle">
                <li class="pan-nav-recycle-li">
                    <a @click="openRecyclePage" :class="{'checked': activeModule === 'recycles'}" href="javascript:void(0);">
                        <span class="text">
                            <span class="icon el-icon-delete"/>
                            <span>回收站</span>
                        </span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'PanNavbar',
        components:{},
        props:[],
        data () {
            return {
                activeModule: 'files'
            }
        },
        methods: {
            init() {
              this.openFilePage()
            },
            openFilePage() {
                this.activeModule = 'files'
                this.$router.push({name: 'Files'})
            },
            openImgPage() {
                this.activeModule = 'imgs'
                this.$router.push({name: 'Imgs'})
            },
            openDocPage() {
                this.activeModule = 'docs'
                this.$router.push({name: 'Docs'})
            },
            openVideoPage() {
                this.activeModule = 'videos'
                this.$router.push({name: 'Videos'})
            },
            openMusicPage() {
                this.activeModule = 'musics'
                this.$router.push({name: 'Musics'})
            },
            openSharePage() {
                this.activeModule = 'share'
                this.$router.push({name: 'Shares'})
            },
            openRecyclePage() {
                this.activeModule = 'recycles'
                this.$router.push({name: 'Recycles'})
            }
        },
        computed:{

        },
        mounted() {
            this.init()
        },
        watch:{

        }
    }
</script>
<style scoped>

    .checked {
        background: rgba(0, 0, 0, .05);
    }

    .checked span {
        color: #09AAFF;
    }

    ul {
        list-style: none;
        padding-inline-start: 0px;
        display: block;
    }

    li {
        display: list-item;
        text-align: -webkit-match-parent;
        list-style: none;
    }

    .pan-content .pan-nav-content-wrapper {
        background-color: #fff;
    }

    .pan-content .pan-nav-content-wrapper .pan-nav-content {
        width: 194px;
        height: 100%;
        top: 62px;
        left: 0;
        bottom: 0;
        z-index: 9;
        position: absolute;
    }

    .pan-content .pan-nav-content-wrapper .pan-nav-content ul a {
        height: 38px;
        line-height: 38px;
        display: block;
        position: relative;
        padding: 0 0 0 15px;
        font-size: 14px;
        text-decoration: none;
        zoom: 1;
    }

    .pan-content .pan-nav-content-wrapper .pan-nav-content ul .icon {
        font-weight: bold;
        font-size: 16px;
        position: absolute;
        top: 12px;
        left: 10px;
        cursor: pointer;
    }

    .pan-content .pan-nav-content-wrapper .pan-nav-content ul .text {
        color: black;
        font-weight: bold;
        font-size: 14px;
        cursor: pointer;
        height: 38px;
        line-height: 38px;
        position: relative;
        display: block;
        width: 115px;
        padding-left: 38px;
    }

</style>