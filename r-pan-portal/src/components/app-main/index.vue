<template>
    <div class="pan-main-content-wrapper">
        <div class="pan-main-content">
            <router-view/>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'PanAppMain',
        components:{},
        props:[],
        data () {
            return {

            }
        },
        methods: {

        },
        computed:{

        },
        mounted() {

        },
        watch:{

        }
    }
</script>
<style scoped>

    .pan-main-content-wrapper .pan-main-content {
        top: 62px;
        left: 194px;
        bottom: 0;
        right: 0;
        z-index: 1;
        background: #fff;
        position: absolute;
    }

</style>