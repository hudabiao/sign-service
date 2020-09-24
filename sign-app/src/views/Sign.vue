<template>
    <div class="sign-main">
        <canvas @touchstart="ontouchstart" @touchmove="ontouchmove" @touchend="ontouchend" @mousedown="onmousedown"
                @mousemove="onmousemove" @mouseup="onmouseup">
        </canvas>
        <div id="sign-btn-container">
            <el-button type="primary" @click="saveSign">确定</el-button>
            <el-button @click="clear">清除</el-button>
        </div>
    </div>

</template>
<style scoped>
    .sign-main {
        width: 100%;
        height: 100%;
    }

    /* 	canvas {
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    } */
    #sign-btn-container {
        position: fixed;
        bottom: 10px;
        width: 100%;
    }
</style>
<script>
    var viewWidth = window.innerWidth || document.documentElement.clientWidth;
    var viewHeight = window.innerHeight || document.documentElement.clientHeight;
    window.addEventListener("onorientationchange" in window ? "orientationchange" : "resize", function () {
        if (window.orientation === 180 || window.orientation === 0) {
            console.log("竖屏");
            var canvas = document.querySelector("canvas");
            canvas.width = viewWidth;
            canvas.height = viewHeight;
        }
        if (window.orientation === 90 || window.orientation === -90) {
            console.log("横屏");
            var canvas = document.querySelector("canvas");
            canvas.width = viewHeight;
            canvas.height = viewWidth;
        }
    }, false);
    export default {
        name: 'Sign',
        data() {
            return {
                x: 0,
                y: 0,
                mousedown: false,
                loginId: '',
                signed: false
            }
        },
        methods: {
            clear(){
                let canvas = document.querySelector("canvas");
                let cxt = canvas.getContext("2d");
                cxt.clearRect(0, 0, canvas.width, canvas.height);
            },
            draw: function (startX, startY, endX, endY, ctx) {
                this.signed = true;
                ctx.beginPath();
                ctx.globalAlpha = 1;
                ctx.lineWidth = 5;
                ctx.strokeStyle = "#000";
                ctx.moveTo(startX, startY);
                ctx.lineTo(endX, endY);
                ctx.closePath();
                ctx.stroke();
            },
            setStartPoint(clientX, clientY) {
                let canvas = document.querySelector("canvas");
                this.x = clientX - canvas.offsetLeft;
                this.y = clientY - canvas.offsetTop;
                this.mousedown = true;
            },
            onmousedown(event) {
                this.setStartPoint(event.clientX, event.clientY);
            },
            move: function (clientX, clientY) {
                if (this.mousedown) {
                    let canvas = document.querySelector("canvas");
                    let x1 = clientX - canvas.offsetLeft;
                    let y1 = clientY - canvas.offsetTop;
                    this.draw(this.x, this.y, x1, y1, canvas.getContext("2d"));
                    this.x = x1;
                    this.y = y1;
                }
            },
            onmousemove(event) {
                this.move(event.clientX, event.clientY);
            },
            onmouseup() {
                this.mousedown = false;
            },
            ontouchstart: function (e) {
                this.setStartPoint(e.touches[0].clientX, e.touches[0].clientY);
            },
            ontouchmove: function (e) {
                this.move(e.touches[0].clientX, e.touches[0].clientY);
            },
            ontouchend: function () {
                this.onmouseup();
            },
            saveSign: function () {
                let imgData = document.querySelector("canvas").toDataURL();
                if(!this.signed){
                    this.$message.warning('您还未签名！');
                    return;
                }
                let _this = this;
                let loginId = _this.loginId;
                let contractId = _this.contractId;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.$axios.post("/contract/sign", {
                    base64ImgData: imgData,
                    loginId: loginId,
                    contractId: contractId
                }).then(function () {
                    loading.close();
                    _this.$message.success('签名成功');
                    // 返回到查看界面
                    _this.$router.push({
                        name: "About",
                        params: {
                            loginId: loginId,
                            contractId: contractId,
                            status: '02'
                        }
                    })
                }).catch(function (e) {
                    loading.close();
                    _this.$message.error("签名失败");
                });
            }
        },
        mounted: function () {
            this.$message('建议横屏签字');
            this.loginId = this.$route.params.loginId;
            this.contractId = this.$route.params.contractId;
            this.$axios.defaults.headers.common['token'] = localStorage.getItem("token");
            let canvas = document.querySelector("canvas");
            let viewWidth = window.innerWidth || document.documentElement.clientWidth;
            let viewHeight = window.innerHeight || document.documentElement.clientHeight;
            canvas.width = viewWidth;
            canvas.height = viewHeight;
            document.body.addEventListener('touchmove', prevent, {passive: false});
        }
    }
</script>
