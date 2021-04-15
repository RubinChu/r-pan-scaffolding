<template>
    <div class="pan-main-operating-content">
        <div class="pan-main-button-content">
            <upload-button v-if="uploadButtonFlag" size="medium" :round-flag=true :parent-id="parentId" @refresh="refresh"/>
            <create-folder-button v-if="createFolderButtonFlag" size="medium" :round-flag=true :parent-id="parentId" @refresh="refresh"/>
            <download-button v-if="downloadButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection"/>
            <delete-button v-if="deleteButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection" :parent-id="parentId" @refresh="refresh"/>
            <rename-button v-if="renameButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection" @refresh="refresh"/>
            <share-button v-if="shareButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection"/>
            <copy-button v-if="copyButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection" :parent-id="parentId" @refresh="refresh"/>
            <transfer-button v-if="transferButtonFlag" size="medium" :round-flag=true :multiple-selection="multipleSelection" :parent-id="parentId" @refresh="refresh"/>
        </div>

    </div>
</template>

<script>

    import UploadButton from '../buttons/upload-button'
    import CreateFolderButton from '../buttons/create-folder-button'
    import DownloadButton from '../buttons/download-button'
    import DeleteButton from '../buttons/delete-button'
    import RenameButton from '../buttons/rename-button'
    import ShareButton from '../buttons/share-button'
    import CopyButton from '../buttons/copy-button'
    import TransferButton from '../buttons/transfer-button'

    export default {
        name: 'FileButtonGroup',
        components: {
            UploadButton,
            CreateFolderButton,
            DownloadButton,
            DeleteButton,
            RenameButton,
            ShareButton,
            CopyButton,
            TransferButton
        },
        props: {
            parentId: String,
            multipleSelection: Array,
            buttonArray: Array
        },
        data() {
            return {
                uploadButtonFlag: false,
                createFolderButtonFlag: false,
                downloadButtonFlag: false,
                deleteButtonFlag: false,
                renameButtonFlag: false,
                shareButtonFlag: false,
                copyButtonFlag: false,
                transferButtonFlag: false
            }
        },
        methods: {
            refresh(fileList) {
                this.$emit('refresh', fileList)
            },
            initButton() {
                let _this = this
                if (_this.buttonArray) {
                    _this.buttonArray.forEach(item => {
                        switch (item) {
                            case 'upload':
                                _this.uploadButtonFlag = true
                                break
                            case 'createFolder':
                                _this.createFolderButtonFlag = true
                                break
                            case 'download':
                                _this.downloadButtonFlag = true
                                break
                            case 'delete':
                                _this.deleteButtonFlag = true
                                break
                            case 'rename':
                                _this.renameButtonFlag = true
                                break
                            case 'share':
                                _this.shareButtonFlag = true
                                break
                            case 'copy':
                                _this.copyButtonFlag = true
                                break
                            case 'transfer':
                                _this.transferButtonFlag = true
                                break
                            default:
                                break
                        }
                    })
                }
            }
        },
        computed: {},
        mounted() {
            this.initButton()
        },
        watch: {}
    }
</script>

<style>

    .pan-main-operating-content {
        width: 100%;
        padding: 20px 0 0 13px;
        height: 60px;
    }

    .pan-main-operating-content .pan-main-button-content {
        display: inline-block;
    }

</style>