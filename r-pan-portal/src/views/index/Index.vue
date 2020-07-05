/**
* Created by rubin on 2020/6/4.
*/

<template>
    <div class="pan-content">
        <pan-header @callback="initBreadcrumbs"/>
        <div class="pan-nav-content-wrapper">
            <div class="pan-nav-content">
                <ul class="pan-nav-file">
                    <li class="pan-nav-file-all">
                        <a @click="loadAllFile" :class="{'checked': allFileFlag}" href="javascript:void(0);">
                            <span class="text">
                                <span class="icon el-icon-document-copy"/>
                                <span>全部文件</span>
                            </span>
                        </a>
                    </li>
                    <li class="pan-nav-file-pic">
                        <a @click="loadImgFile" :class="{'checked': imgFileFlag}" href="javascript:void(0);">
                            <span class="text">
                                <span class="icon"/>
                                <span>图片</span>
                            </span>
                        </a>
                    </li>
                    <li class="pan-nav-file-doc">
                        <a @click="loadDocFile" :class="{'checked': docFileFlag}" href="javascript:void(0);">
                            <span class="text">
                                <span class="icon"/>
                                <span>文档</span>
                            </span>
                        </a>
                    </li>
                    <li class="pan-nav-file-video">
                        <a @click="loadVideoFile" :class="{'checked': videoFileFlag}" href="javascript:void(0);">
                            <span class="text">
                                <span class="icon"/>
                                <span>视频</span>
                            </span>
                        </a>
                    </li>
                    <li class="pan-nav-file-music">
                        <a @click="loadMusicFile" :class="{'checked': musicFileFlag}" href="javascript:void(0);">
                            <span class="text">
                                <span class="icon"/>
                                <span>音乐</span>
                            </span>
                        </a>
                    </li>
                </ul>
                <!--                <ul class="pan-nav-share">-->
                <!--                    <li class="pan-nav-share-li">-->
                <!--                        <a class="" href="javascript:void(0);">-->
                <!--                            <span class="text">-->
                <!--                                <span class="icon el-icon-share"/>-->
                <!--                                <span>我的分享</span>-->
                <!--                            </span>-->
                <!--                        </a>-->
                <!--                    </li>-->
                <!--                </ul>-->
                <!--                <ul class="pan-nav-recycle">-->
                <!--                    <li class="pan-nav-recycle-li">-->
                <!--                        <a class="" href="javascript:void(0);">-->
                <!--                            <span class="text">-->
                <!--                                <span class="icon el-icon-delete"/>-->
                <!--                                <span>回收站</span>-->
                <!--                            </span>-->
                <!--                        </a>-->
                <!--                    </li>-->
                <!--                </ul>-->
            </div>
        </div>
        <div class="pan-main-content-wrapper">
            <div class="pan-main-content">
                <div class="pan-main-operating-content">
                    <div class="pan-main-button-content">
                        <el-upload
                                class="upload-content"
                                :headers="uploadHeaders"
                                :action="uploadUrl"
                                :data="uploadData"
                                :show-file-list="false"
                                :before-upload="beforeUpload"
                                :on-progress="uploadProcess"
                                :on-success="uploadSuccess">
                            <el-button type="primary" size="medium" round>
                                上传<i class="el-icon-upload el-icon--right"/>
                            </el-button>
                        </el-upload>
                        <el-button type="success" size="medium" round @click="createDirectoryDialogVisible = true">
                            新建文件夹<i class="el-icon-folder-add el-icon--right"/>
                        </el-button>
                        <el-button type="info" size="medium" round @click="downloadMultipleFile" :loading="downloadLoading">
                            下载<i class="el-icon-download el-icon--right"/>
                        </el-button>
                        <el-button type="danger" size="medium" round @click="deleteMultipleFile">
                            删除<i class="el-icon-delete el-icon--right"/>
                        </el-button>
                        <el-button size="medium" round @click="copyMultipleFile">
                            复制到
                        </el-button>
                        <el-button size="medium" round @click="transferMultipleFile">
                            移动到
                        </el-button>
                    </div>
                    <div class="pan-main-search-content">
                        <el-input class="pan-main-search" v-model="searchKey" placeholder="请输入内容" clearable size="small"
                                  @keyup.enter.native="doSearch">
                            <i slot="prefix" class="el-input__icon el-icon-search"></i>
                        </el-input>
                    </div>
                </div>
                <div class="pan-main-breadcrumb-content">
                    <el-breadcrumb v-if="backFlag" style="display: inline-block;">
                        <el-breadcrumb-item>
                            <a class="breadcrumb-item-a" @click="goBack()" href="#">返回</a>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                    <el-divider v-if="backFlag" direction="vertical" style="vertical-align: top !important;"/>
                    <el-breadcrumb separator-class="el-icon-arrow-right" style="display: inline-block;">
                        <el-breadcrumb-item v-for="item in breadcrumbs">
                            <a class="breadcrumb-item-a" @click="goToThis(item.id)" href="#">{{item.name}}</a>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                </div>
                <div class="pan-main-file-list-content">
                    <el-table
                            ref="fileTable"
                            :data="tableData"
                            height="100%"
                            tooltip-effect="dark"
                            style="width: 100%"
                            @selection-change="handleSelectionChange"
                            @cell-mouse-enter="showOperation"
                            @cell-mouse-leave="hiddenOperation"
                    >
                        <el-table-column
                                type="selection"
                                width="55">
                        </el-table-column>
                        <el-table-column
                                label="文件名"
                                prop="fileName"
                                sortable
                                show-overflow-tooltip
                                min-width="750">
                            <template slot-scope="scope">
                                <div @click="show(scope.row)" class="file-name-content">
                                    <i :class="getFileFontElement(scope.row.type)"
                                       style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                                    <span style="cursor:pointer;">{{scope.row.fileName}}</span>
                                </div>
                                <div v-show="scope.row.fileId === operationShowFileId" class="file-operation-content">
                                    <el-tooltip class="item" effect="light" content="下载" placement="top">
                                        <el-button size="small" icon="el-icon-download" circle @click="downloadSingleFile(scope.row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="light" content="重命名" placement="top">
                                        <el-button size="small" icon="el-icon-edit" circle @click="renameSingleFile(scope.row)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="light" content="删除" placement="top">
                                        <el-button size="small" icon="el-icon-delete" circle
                                                   @click="deleteSingleFile(scope.row.fileId)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="light" content="复制到" placement="top">
                                        <el-button size="small" icon="el-icon-document-copy" circle @click="copySingleFile(scope.row.fileId)"></el-button>
                                    </el-tooltip>
                                    <el-tooltip class="item" effect="light" content="转移到" placement="top">
                                        <el-button size="small" icon="el-icon-position" circle @click="transferSingleFile(scope.row.fileId)"></el-button>
                                    </el-tooltip>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="fileSize"
                                sortable
                                label="大小"
                                min-width="120"
                                align="center"
                                :formatter="formatFileSize">
                        </el-table-column>
                        <el-table-column
                                prop="updateTime"
                                sortable
                                align="center"
                                label="修改日期"
                                min-width="240"
                                :formatter="formatUpdateTime">
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>
        <el-dialog
                title="新建文件夹"
                :visible.sync="createDirectoryDialogVisible"
                width="30%"
                :center="true">
            <div>
                <el-form label-width="100px" :rules="createDirectoryRules" ref="createDirectoryForm"
                         :model="createDirectoryForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="文件夹名称" prop="directoryName">
                        <el-input type="text"
                                  ref="directoryName"
                                  @keyup.enter.native="doCreateDirectory"
                                  v-model="createDirectoryForm.directoryName" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="createDirectoryDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doCreateDirectory" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                :show-close="false"
                :visible.sync="uploadProcessDialogVisible"
                :center="true">
            <div>
                <el-progress :percentage="percent"/>
            </div>
        </el-dialog>
        <el-dialog
                title="文件重命名"
                :visible.sync="renameDialogVisible"
                width="30%"
                :center="true">
            <div>
                <el-form label-width="100px" :rules="renameRules" ref="renameForm"
                         :model="renameForm"
                         status-icon
                         @submit.native.prevent>
                    <el-form-item label="文件名称" prop="fileName">
                        <el-input type="text"
                                  ref="fileName"
                                  @keyup.enter.native="doRenameFile"
                                  v-model="renameForm.fileName" autocomplete="off"/>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="renameDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doRenameFile" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                :title="treeTitle"
                :visible.sync="treeDialogVisible"
                width="30%"
                :center="true">
            <div>
                <el-tree
                        :data="treeData"
                        empty-text="暂无文件夹数据"
                        default-expand-all
                        highlight-current
                        check-on-click-node
                        :expand-on-click-node="false"
                        ref="tree">
                    <span class="custom-tree-node" slot-scope="{ node, data }">
                        <i class="fa fa-folder-o" style="margin-right: 15px; font-size: 20px; cursor: pointer;"/>
                        <span>{{ node.label }}</span>
                    </span>
                </el-tree>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="treeDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="doChoseTreeNodeCallBack" :loading="loading">确 定</el-button>
            </span>
        </el-dialog>
        <el-image-viewer
                v-if="showViewer"
                :on-close="closeShowViewer"
                :url-list="[imgUrl]"/>
    </div>
</template>

<script>
    import fileService from "../../api/file/file-service";
    import panUtil from "../../utils/common";
    import { getToken } from "../../utils/cookie";
    import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
    import PanHeader from "../../components/header/PanHeader";

    export default {
        components: { ElImageViewer, PanHeader },
        data() {
            return {
                loading: false,
                tableData: [],
                multipleSelection: [],
                breadcrumbs: [{
                    id: '',
                    name: ''
                }],
                createDirectoryDialogVisible: false,
                createDirectoryForm: {
                    directoryName: ''
                },
                createDirectoryRules: {
                    directoryName: [
                        {required: true, message: '请输入文件夹名称', trigger: 'blur'}
                    ]
                },
                uploadUrl: panUtil.getUrlPrefix() + 'file/upload',
                uploadProcessDialogVisible: false,
                percent: 0,
                downloadLoading: false,
                renameDialogVisible: false,
                renameForm: {
                    fileId: '',
                    fileName: ''
                },
                renameRules: {
                    fileName: [
                        {required: true, message: '请输入新文件名称', trigger: 'blur'}
                    ]
                },
                treeData: [],
                treeTitle: '',
                treeDialogVisible: false,
                searchKey: '',
                allFileFlag: true,
                imgFileFlag: false,
                docFileFlag: false,
                videoFileFlag: false,
                musicFileFlag: false,
                fileTypes: '-1',
                backFlag: true,
                showViewer: false,
                imgUrl: '',
                operationShowFileId: '',
                toBeCopiedFileIds: '',
                toBeTransferredFileIds: '',
                defaultParentId: ''
            }
        },
        methods: {
            initBreadcrumbs(rootFileId, rootFileName) {
                let rootItem = {
                    id: rootFileId,
                    name: rootFileName
                }
                this.breadcrumbs.length = 0
                this.breadcrumbs.push(rootItem)
                this.loadTableData()
                this.defaultParentId = rootFileId
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            loadTableData() {
                let _this = this
                fileService.list({
                    parentId: _this.getParentId(),
                    fileTypes: _this.fileTypes
                }, function (res) {
                    if (res.status == 0) {
                        _this.tableData = res.data
                    } else (
                        _this.$message.error(res.message)
                    )
                }, function (err) {
                    _this.$message.error(err)
                })
            },
            getParentId() {
                return [...this.breadcrumbs].pop().id
            },
            formatUpdateTime(row, column, cellValue, index) {
                return panUtil.formatTime(new Date(row.updateTime))
            },
            formatFileSize(row, column, cellValue, index) {
                if (row.fileSize) {
                    return row.fileSize
                }
                return '--'
            },
            getFileFontElement(type) {
                let tagStr = 'fa fa-file'
                switch (type) {
                    case 0:
                        tagStr = 'fa fa-folder-o'
                        break
                    case 2:
                        tagStr = 'fa fa-file-archive-o'
                        break
                    case 3:
                        tagStr = 'fa fa-file-excel-o'
                        break
                    case 4:
                        tagStr = 'fa fa-file-word-o'
                        break
                    case 5:
                        tagStr = 'fa fa-file-pdf-o'
                        break
                    case 6:
                        tagStr = 'fa fa-file-text-o'
                        break
                    case 7:
                        tagStr = 'fa fa-file-image-o'
                        break
                    case 8:
                        tagStr = 'fa fa-file-audio-o'
                        break
                    case 9:
                        tagStr = 'fa fa-file-video-o'
                        break
                    case 10:
                        tagStr = 'fa fa-file-powerpoint-o'
                        break
                    case 11:
                        tagStr = 'fa fa-file-code-o'
                        break
                    default:
                        break
                }
                return tagStr
            },
            show(row) {
                let _this = this
                switch (row.type) {
                    case 0:
                        _this.goIn(row)
                        break
                    case 3:
                    case 4:
                    case 10:
                        _this.showOffice(row)
                        break
                    case 5:
                    case 6:
                        _this.showIframe(row)
                        break
                    case 7:
                        _this.showImg(row)
                        break
                    case 8:
                        _this.showMusic(row)
                        break
                    case 9:
                        _this.showVideo(row)
                        break
                    case 11:
                        _this.showCode(row)
                    default:
                        break
                }
            },
            goIn(row) {
                let _this = this
                fileService.getBreadcrumbs({
                    fileId: row.fileId
                }, function (res) {
                    if (res.status === 0) {
                        _this.breadcrumbs = res.data
                        _this.loadTableData()
                    } else {
                        _this.$message.error(res.message)
                    }
                }, function (err) {
                    _this.$message.error(err)
                })
            },
            showOffice(row) {
                const { href } = this.$router.resolve({
                    path: '/office',
                    name: "Office",
                    params: {
                        fileId: row.fileId
                    }
                })
                window.open(href, '_blank')
            },
            showIframe(row) {
                const { href } = this.$router.resolve({
                    path: '/iframe',
                    name: "Iframe",
                    params: {
                        fileId: row.fileId
                    }
                })
                window.open(href, '_blank')
            },
            showImg(row) {
                this.imgUrl = row.showPath
                this.showViewer = true
            },
            showMusic(row) {
                const { href } = this.$router.resolve({
                    path: '/music',
                    name: "Music",
                    params: {
                        fileId: row.fileId,
                        parentId: row.parentId
                    }
                })
                window.open(href, '_blank')
            },
            showVideo(row) {
                const { href } = this.$router.resolve({
                    path: '/video',
                    name: "Video",
                    params: {
                        fileId: row.fileId,
                        parentId: row.parentId
                    }
                })
                window.open(href, '_blank')
            },
            showCode(row) {
                const { href } = this.$router.resolve({
                    path: '/code',
                    name: "Code",
                    params: {
                        fileId: row.fileId
                    }
                })
                window.open(href, '_blank')
            },
            doCreateDirectory() {
                let _this = this
                _this.$refs.createDirectoryForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        let parentId = _this.getParentId()
                        if (parentId === '-1') {
                            parentId = _this.defaultParentId
                        }
                        fileService.createDirectory({
                            parentId: parentId,
                            directoryName: _this.createDirectoryForm.directoryName
                        }, function (res) {
                            if (res.status === 0) {
                                _this.loading = false
                                _this.createDirectoryDialogVisible = false
                                _this.$message.success('新建成功')
                                _this.reloadFileList(res.data)
                            } else {
                                _this.$message.error(res.message)
                                _this.loading = false
                            }
                        }, function (err) {
                            _this.$message.error(err)
                            _this.loading = false
                        })
                    }
                })
            },
            goToThis(id) {
                let newBreadcrumbs = new Array(),
                    _this = this
                if (_this.allFileFlag) {
                    _this.breadcrumbs.some(item => {
                        newBreadcrumbs.push(item)
                        if (item.id == id) {
                            return true
                        }
                    })
                    _this.breadcrumbs = newBreadcrumbs
                    _this.loadTableData()
                }
            },
            deleteMultipleFile() {
                let _this = this
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => fileIdArr.push(item.fileId))
                    _this.doDeleteFiles(fileIdArr.join('__,__'))
                    return
                }
                _this.$message.error('请选择要删除的文件')
            },
            deleteSingleFile(fileId) {
                this.doDeleteFiles(fileId)
            },
            doDeleteFiles(fileIds) {
                let _this = this
                _this.$msgbox.confirm('文件删除后将不可恢复，您确定这样做吗？', '删除文件', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    fileService.delete({
                            parentId: _this.getParentId(),
                            fileIds: fileIds
                        },
                        function (res) {
                            if (res.status == 0) {
                                _this.$message.success('删除成功')
                                _this.reloadFileList(res.data)
                            } else (
                                _this.$message.error(res.message)
                            )
                        }, function (err) {
                            _this.$message.error(err)
                        })
                })
            },
            beforeUpload(file) {
                const limitFlag = file.size / 1024 / 1024 < 3072
                if (!limitFlag) {
                    this.$message.error('上传文件大小大小不能超过3072MB')
                    return limitFlag
                }
                this.uploadProcessDialogVisible = true
            },
            uploadProcess(event, file, fileList) {
                this.percent = Math.floor(event.percent)
            },
            uploadSuccess(res, file, fileList) {
                if (res.status === 0) {
                    this.$message.success('上传成功')
                    this.uploadProcessDialogVisible = false
                    this.percent = 0
                    this.reloadFileList(res.data)
                } else {
                    this.$message.error('上传失败')
                    this.uploadProcessDialogVisible = false
                    this.percent = 0
                }
            },
            downloadMultipleFile() {
                let _this = this,
                    url = '',
                    fileName = ''
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    _this.downloadLoading = true
                    if (_this.multipleSelection.length == 1 && _this.multipleSelection[0].type != 0) {
                        url = panUtil.getUrlPrefix() + '/file/download?fileId=' + _this.multipleSelection[0].fileId + '&token=' + getToken()
                        fileName = _this.multipleSelection[0].fileName
                        _this.doDownload(url, fileName)
                        _this.downloadLoading = false
                        return
                    }
                    let fileIdArr = new Array()
                    _this.multipleSelection.forEach(item => fileIdArr.push(item.fileId))
                    url = panUtil.getUrlPrefix() + '/file/zip/download?fileIds=' + fileIdArr.join('__,__') + '&token=' + getToken()
                    fileName = 'data.zip'
                    _this.doDownload(url, fileName)
                    _this.downloadLoading = false
                    return
                }
                _this.$message.error('请选择要下载的文件')
            },
            downloadSingleFile(row) {
                let _this = this,
                    url = '',
                    fileName = ''
                if (row.type != 0) {
                    url = panUtil.getUrlPrefix() + '/file/download?fileId=' + row.fileId + '&token=' + getToken()
                    fileName = row.fileName
                    _this.doDownload(url, fileName)
                    return
                }
                url = panUtil.getUrlPrefix() + '/file/zip/download?fileIds=' + row.fileId + '&token=' + getToken()
                fileName = 'data.zip'
                _this.doDownload(url, fileName)
            },
            doDownload(url, fileName) {
                let link = document.createElement('a')
                link.style.display = 'none'
                link.href = url
                link.setAttribute('download', fileName)
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
            },
            renameSingleFile(row) {
                this.renameForm.fileId = row.fileId
                this.renameForm.fileName = row.fileName
                this.renameDialogVisible = true
            },
            doRenameFile() {
                let _this = this
                _this.$refs.renameForm.validate((valid) => {
                    if (valid) {
                        _this.loading = true
                        fileService.update({
                            fileId: _this.renameForm.fileId,
                            newFileName: _this.renameForm.fileName
                        }, function (res) {
                            if (res.status === 0) {
                                _this.loading = false
                                _this.renameDialogVisible = false
                                _this.$message.success('重命名成功')
                                _this.reloadFileList(res.data)
                            } else {
                                _this.$message.error(res.message)
                                _this.loading = false
                            }
                        }, function (err) {
                            _this.$message.error(err)
                            _this.loading = false
                        })
                    }
                })
            },
            goBack() {
                if (this.breadcrumbs.length > 1) {
                    this.breadcrumbs.splice(this.breadcrumbs.length - 1, 1)
                    this.loadTableData()
                }
            },
            getCheckNode() {
                return this.$refs.tree.getCurrentNode()
            },
            doChoseTreeNodeCallBack() {
                if (!this.getCheckNode()) {
                    this.$message.error('请选择文件夹')
                    return
                }
                if (this.treeTitle === '复制文件') {
                    this.doCopyFile()
                } else if (this.treeTitle === '转移文件') {
                    this.doTransferFile()
                }
            },
            copyMultipleFile() {
                let _this = this
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let checkedFileIdArr = new Array()
                    _this.multipleSelection.forEach(item => checkedFileIdArr.push(item.fileId))
                    _this.toBeCopiedFileIds = checkedFileIdArr.join('__,__')
                    _this.treeTitle = '复制文件'
                    _this.treeDialogVisible = true
                    return
                }
                _this.$message.error('请选择要复制的文件')
            },
            copySingleFile(fileId) {
                this.toBeCopiedFileIds = fileId
                this.treeTitle = '复制文件'
                this.treeDialogVisible = true
            },
            doCopyFile() {
                let _this = this
                _this.loading = true
                fileService.copy({
                    fileIds: _this.toBeCopiedFileIds,
                    parentId: _this.getParentId(),
                    targetParentId: _this.getCheckNode().id
                }, function (res) {
                    if (res.status === 0) {
                        _this.loading = false
                        _this.treeDialogVisible = false
                        _this.$message.success('文件复制成功')
                        _this.reloadFileList(res.data)
                    } else {
                        _this.$message.error(res.message)
                        _this.loading = false
                    }
                }, function (err) {
                    _this.$message.error(err)
                    _this.loading = false
                })
            },
            transferMultipleFile() {
                let _this = this
                if (_this.multipleSelection && _this.multipleSelection.length > 0) {
                    let checkedFileIdArr = new Array()
                    _this.multipleSelection.forEach(item => checkedFileIdArr.push(item.fileId))
                    _this.toBeTransferredFileIds = checkedFileIdArr.join('__,__')
                    _this.treeTitle = '转移文件'
                    _this.treeDialogVisible = true
                    return
                }
                _this.$message.error('请选择要转移的文件')
            },
            transferSingleFile(fileId) {
                this.toBeTransferredFileIds = fileId
                this.treeTitle = '转移文件'
                this.treeDialogVisible = true
            },
            doTransferFile() {
                let _this = this
                _this.loading = true
                fileService.transfer({
                    fileIds: _this.toBeTransferredFileIds,
                    parentId: _this.getParentId(),
                    targetParentId: _this.getCheckNode().id
                }, function (res) {
                    if (res.status === 0) {
                        _this.loading = false
                        _this.treeDialogVisible = false
                        _this.$message.success('文件转移成功')
                        _this.reloadFileList(res.data)
                    } else {
                        _this.$message.error(res.message)
                        _this.loading = false
                    }
                }, function (err) {
                    _this.$message.error(err)
                    _this.loading = false
                })
            },
            doSearch() {
                let _this = this
                if (_this.searchKey) {
                    _this.breadcrumbs = _this.breadcrumbs.splice(0, 1)
                    let item = {
                        type: 'search',
                        name: '搜索：' + _this.searchKey
                    }
                    _this.breadcrumbs.push(item)
                    fileService.search({
                        keyword: _this.searchKey
                    }, function (res) {
                        if (res.status === 0) {
                            _this.tableData = res.data
                        } else {
                            _this.$message.error(res.message)
                        }
                    }, function (err) {
                        _this.$message.error(err)
                    })
                }
            },
            loadAllFile() {
                this.backFlag = true
                this.allFileFlag = true
                this.imgFileFlag = false
                this.docFileFlag = false
                this.videoFileFlag = false
                this.musicFileFlag = false
                this.fileTypes = '-1'
                this.breadcrumbs = this.breadcrumbs.splice(0, 1)
                this.loadTableData()
            },
            loadImgFile() {
                this.backFlag = false
                this.allFileFlag = false
                this.imgFileFlag = true
                this.docFileFlag = false
                this.videoFileFlag = false
                this.musicFileFlag = false
                this.fileTypes = '7'
                this.breadcrumbs = this.breadcrumbs.splice(0, 1)
                let item = {
                    id: '-1',
                    name: '分类：图片'
                }
                this.breadcrumbs.push(item)
                this.loadTableData()

            },
            loadDocFile() {
                this.backFlag = false
                this.allFileFlag = false
                this.imgFileFlag = false
                this.docFileFlag = true
                this.videoFileFlag = false
                this.musicFileFlag = false
                this.fileTypes = '3__,__4__,__5__,__6__,__10__,__11'
                this.breadcrumbs = this.breadcrumbs.splice(0, 1)
                let item = {
                    id: '-1',
                    name: '分类：文档'
                }
                this.breadcrumbs.push(item)
                this.loadTableData()
            },
            loadVideoFile() {
                this.backFlag = false
                this.allFileFlag = false
                this.imgFileFlag = false
                this.docFileFlag = false
                this.videoFileFlag = true
                this.musicFileFlag = false
                this.fileTypes = '9'
                this.breadcrumbs = this.breadcrumbs.splice(0, 1)
                let item = {
                    id: '-1',
                    name: '分类：视频'
                }
                this.breadcrumbs.push(item)
                this.loadTableData()
            },
            loadMusicFile() {
                this.backFlag = false
                this.allFileFlag = false
                this.imgFileFlag = false
                this.docFileFlag = false
                this.videoFileFlag = false
                this.musicFileFlag = true
                this.fileTypes = '8'
                this.breadcrumbs = this.breadcrumbs.splice(0, 1)
                let item = {
                    id: '-1',
                    name: '分类：音乐'
                }
                this.breadcrumbs.push(item)
                this.loadTableData()
            },
            closeShowViewer() {
                this.showViewer = false
            },
            showOperation(row, column, cell, event) {
                this.operationShowFileId = row.fileId
            },
            hiddenOperation(row, column, cell, event) {
                this.operationShowFileId = ''
            },
            reloadFileList(fileList) {
                if (this.allFileFlag) {
                    this.tableData = fileList
                    return
                }
                if (this.imgFileFlag) {
                    this.loadImgFile()
                    return
                }
                if (this.docFileFlag) {
                    this.loadDocFile()
                    return
                }
                if (this.videoFileFlag) {
                    this.loadVideoFile()
                    return
                }
                if (this.musicFileFlag) {
                    this.loadMusicFile()
                    return
                }
            }
        },
        computed: {
            uploadData() {
                let parentId = this.getParentId()
                if (parentId === '-1') {
                    parentId = this.defaultParentId
                }
                return {
                    parentId: parentId
                }
            },
            uploadHeaders() {
                return {
                    Authorization: getToken()
                }
            },
        },
        mounted() {

        },
        watch: {
            createDirectoryDialogVisible: function (newValue, oldValue) {
                if (newValue) {
                    this.$nextTick(() => {
                        this.$refs.createDirectoryForm.resetFields()
                        this.$refs.directoryName.focus()
                    })
                }
            },
            renameDialogVisible: function (newValue, oldValue) {
                if (oldValue) {
                    this.$nextTick(() => {
                        this.$refs.renameForm.resetFields()
                        this.$refs.fileName.focus()
                    })
                }
                if (newValue) {
                    this.$nextTick(() => {
                        this.$refs.fileName.focus()
                    })
                }
            },
            treeDialogVisible: function (newValue, oldValue) {
                let _this = this
                if (newValue) {
                    fileService.getDirectoryTree({}, function (res) {
                        if (res.status === 0) {
                            _this.treeData = res.data
                        } else {
                            _this.$message.error(res.message)
                        }
                    }, function (err) {
                        _this.$message.error(err)
                    })
                }
            }
        }
    }
</script>

<style>
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

    div {
        display: block;
    }

    .pan-content {
        overflow: hidden;
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        min-width: 1103px;
        background: #f7f7f7;
        transition: background 1s ease;
        font: 12px/1.5 "Microsoft YaHei", arial, SimSun, "宋体";
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

    .pan-content .pan-main-content-wrapper .pan-main-content {
        top: 62px;
        left: 194px;
        bottom: 0;
        right: 0;
        z-index: 1;
        background: #fff;
        position: absolute;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content {
        width: 100%;
        padding: 20px 0 0 13px;
        height: 60px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content .pan-main-button-content {
        display: inline-block;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content .pan-main-button-content .upload-content {
        display: inline-block;
        margin-right: 10px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content .pan-main-search-content {
        display: inline-block;
        position: absolute;
        right: 35px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content .pan-main-search-content .pan-main-search {
        width: 250px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-operating-content .pan-main-search-content input {
        border-radius: 30px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-breadcrumb-content {
        width: 100%;
        padding: 10px 0 0 25px;
    }

    .pan-content .pan-main-content-wrapper .pan-main-content .pan-main-file-list-content {
        width: 100%;
        height: 100%;
        margin-top: 20px;
    }

    .breadcrumb-item-a {
        cursor: pointer !important;
        color: #409EFF !important;
    }

    .file-name-content {
        display: inline-block;
    }

    .file-name-content:hover {
        color: #409EFF;
    }

    .file-operation-content {
        display: inline-block;
        position: absolute;
        right: 100px;
    }

    .el-divider {
        vertical-align: top !important;
    }
</style>
