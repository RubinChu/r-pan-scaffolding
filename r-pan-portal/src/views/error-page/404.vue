<template>
    <div class="wscn-http404-container">
        <div class="wscn-http404">
            <div class="pic-404">
                <img class="pic-404-parent" src="@/assets/imgs/404.png" alt="404">
                <img class="pic-404-child left" src="@/assets/imgs/404_cloud.png" alt="404">
                <img class="pic-404-child mid" src="@/assets/imgs/404_cloud.png" alt="404">
                <img class="pic-404-child right" src="@/assets/imgs/404_cloud.png" alt="404">
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: '404'
    }
</script>

<style>
    .wscn-http404-container {
        transform: translate(-50%, -50%);
        position: absolute;
        top: 40%;
        left: 50%;
    }
    .wscn-http404-container .wscn-http404 {
        position: relative;
        width: 1200px;
        padding: 0 50px;
        overflow: hidden;
    }
    .wscn-http404-container .wscn-http404 .pic-404 {
        position: relative;
        float: left;
        width: 600px;
        overflow: hidden;
    }
    .wscn-http404-container .wscn-http404 .pic-404 .pic-404-parent {
        width: 100%;
    }
    .wscn-http404-container .wscn-http404 .pic-404 .pic-404-child {
        position: absolute;
    }
    .wscn-http404-container .wscn-http404 .pic-404 .pic-404-child.left {
        width: 80px;
        top: 17px;
        left: 220px;
        opacity: 0;
        animation-name: cloudLeft;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
    }
    .wscn-http404-container .wscn-http404 .pic-404 .pic-404-child.mid {
        width: 46px;
        top: 10px;
        left: 420px;
        opacity: 0;
        animation-name: cloudMid;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1.2s;
    }
    .wscn-http404-container .wscn-http404 .pic-404 .pic-404-child.right {
        width: 62px;
        top: 100px;
        left: 500px;
        opacity: 0;
        animation-name: cloudRight;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
    }
</style>
