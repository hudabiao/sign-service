<template>
    <div class="main-container">
        <el-row type="flex" justify="center">
            <el-col :xs="24" :sm="20" :md="16" :lg="12" :xl="8">
                <el-form class="user-form" ref="userform" :model="usermodel"  label-width="0" :rules="rules">
                    <el-form-item prop="username">
                        <el-input v-model="usermodel.username" placeholder="请输入名称"></el-input>
                    </el-form-item>
                    <el-form-item prop="phone">
                        <el-input v-model="usermodel.phone" placeholder="请输入手机号码"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-upload ref="uploadComponet"
                                   action="#"
                                   :limit="5"
                                   accept="application/pdf"
                                   :auto-upload="false">
                            <el-button slot="trigger" size="small">上传合同</el-button>
                        </el-upload>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="saveContract">保存</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </div>
</template>
<style scoped>
    .main-container {
        margin-left: 10px;
        margin-right: 10px;
        height: 500px;
        position: relative;
        top: 50%;
        margin-top: -250px;
    }

    .user-form {
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        padding: 50px 25px;
    }
</style>
<script>
    export default {
        name: 'Login',
        data() {
            return {
                usermodel:{
                    username: '',
                    phone: ''
                },
                rules:{
                    username:[
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { min: 2, max: 5, message: '长度在2到5个字符', trigger: 'blur' }
                    ],
                    phone:[
                        { required: true, message: '请输入手机号码', trigger: 'blur' },
                        { pattern:'^[0-9]{11}$' , message: '请输入正确的手机号码', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            saveContract() {
                let _this = this;
                let formData = new FormData();
                let files = this.$refs.uploadComponet.uploadFiles;
                if (!files || files.length === 0) {
                    _this.$message.warning('请上传合同！');
                    return;
                }
                let usermodel = this.usermodel;
                formData.append('username', usermodel.username);
                formData.append('loginId', usermodel.phone);
                formData.append('phone', usermodel.phone);
                files.forEach(function (file) {
                    formData.append('files', file.raw);
                })
                this.$refs.userform.validate((valid) => {
                    if (valid) {
                        _this.$axios.post("/user", formData, {
                            'Content-Type': 'multipart/form-data'
                        }).then(function () {
                            _this.$message.success('保存成功');
                            _this.$router.push({
                                name: "Home"
                            })
                        }).catch(function (error) {
                            _this.$message.error(error.response.data.message);
                        });
                    } else {
                        console.log('error submit!!');
                    }
                });
            }
        },
        mounted: function () {

        }
    }
</script>
