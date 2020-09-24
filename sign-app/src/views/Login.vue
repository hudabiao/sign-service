<template>
    <div class="main-container">
        <div class="login-container">
            <div class="logo">
                <img src="../../src/assets/logo.jpg">
            </div>
            <div class="login-form">
                <el-form class="login" ref="form" :model="user" label-width="0">
                    <el-form-item>
                        <div class="my-title">登录签订合同</div>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="user.loginId"placeholder="登录账号为开课手机号"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-input v-model="user.password" show-password placeholder="默认密码为手机号后6位"></el-input>
                    </el-form-item>
                    <el-form-item v-show="loginFail">
                        <el-alert :title="failMsg" type="error"></el-alert>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="login">登录</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>
<style scoped>
    .my-title{
        font-size: x-large;
        font-weight: bold;
    }
    .main-container{
        height: 100%;
        background-image: url("../../src/assets/bg.jpg");
    }
    .login-container{
        background-color: #ffffff;
        border-radius: 5px;
    }

    @media screen and (max-width: 1000px) {
        .login-container{
            width: calc(100% - 20px);
            margin-left: 10px;
            margin-right: 10px;
            position: absolute;
            top: 50%;
            margin-top: -187px;
        }
        .logo{
            width: 100%;
        }
        .logo img{
            width: 100%;
            height: 100%;
        }
        .login-form{
            padding: 5px;
        }
    }

    @media screen and (min-width: 1000px) {
        .login-container{
            height: 500px;
            width: 1000px;
            position: absolute;
            top: 50%;
            left: 50%;
            margin-top: -250px;
            margin-left: -500px;
        }
        .logo{
            vertical-align: top;
            height: 100%;
            width: 500px;
            display: inline-block;
        }
        .logo img{
            position: absolute;
            top: 50%;
            left: 0;
            margin-top: -122px;
        }
        .login-form{
            vertical-align: top;
            display: inline-block;
            width: 500px;
            height: 500px;
        }
        .login {
            position: relative;
            top: 100px;
            padding:50px 25px;
        }
    }
</style>
<script>
    export default {
        name: 'Login',
        data() {
            return {
                user: {
                    loginId: '',
                    password: ''
                },
                loginFail: false,
                failMsg: ""
            }
        },
        methods: {
            login() {
                let url = "/login";
                let user = this.user;
                let _this = this;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.$axios.post(url, user).then(function (result) {
                    loading.close();
                    var resultData = result.data;
                    if (resultData.code === '01') {
                        let loginId = resultData.data.loginId;
                        localStorage.setItem("token", resultData.data.token);
                        localStorage.setItem("username", resultData.data.username);
                        localStorage.setItem("role", resultData.data.role);
                        if (resultData.data.role === '01') {
                            _this.$router.replace({
                                name: "Home"
                            })
                        } else {
                            _this.$router.replace({
                                name: "About",
                                params: {
                                    loginId: loginId,
                                    contractId: '0',
                                    status: '0'
                                }
                            })
                        }
                    }
                    if (resultData.code === '02') {
                        _this.$message.error(resultData.msg);
                    }
                }).catch(function (e) {
                    loading.close();
                    _this.$message.error("出错了！请联系管理员");
                })
            }
        },
        mounted: function () {

        }
    }
</script>
