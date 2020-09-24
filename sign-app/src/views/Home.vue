<template>
    <div class="home">
        <div>
            <div style="display: inline-block;float: left;margin-bottom: 10px;">
                <el-button @click="addUserAndContract">新增合同</el-button>
            </div>
            <div style="display: inline-block;float: right;margin-bottom: 10px;">
                <el-input placeholder="搜索姓名" v-model="searchText" class="input-with-select">
                    <el-button @click="loadData(1)" slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>
        <el-table border :data="tableData">
            <el-table-column prop="username" label="姓名">
            </el-table-column>
            <el-table-column prop="loginId" label="账号">
            </el-table-column>
            <el-table-column prop="" :formatter="getContractSize" label="合同数">
            </el-table-column>
            <el-table-column prop="createDatetime" label="创建时间">
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button @click="lookContract(scope.row)" type="text" size="small">查看合同</el-button>
                    <el-button @click="addContract(scope.row)" type="text" size="small">增加合同</el-button>
                    <el-button @click="delUser(scope.row)" type="text" size="small">删除学员</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination layout="prev, pager, next" :page-count="page.totalPages" :current-page="page.currentPage"
                       :page-size="10"
                       @current-change="loadData" :total="page.total">
        </el-pagination>
        <el-dialog :title="username" :visible.sync="showContract" width="70%" :close-on-click-modal="false">
            <el-table :data="contractList">
                <el-table-column property="name" label="名称"></el-table-column>
                <el-table-column prop="status" :formatter="getStatus" label="签名状态"></el-table-column>
                <el-table-column prop="createDatetime" width="180" label="创建时间"></el-table-column>
                <el-table-column prop="updateDatetime" width="180" label="更新时间"></el-table-column>
                <el-table-column label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button @click="viewContract(scope.row)" type="text" size="small">查看合同</el-button>
                        <el-button @click="download(scope.row)" type="text" size="small">下载</el-button>
                        <el-button @click="delContract(scope.row)" type="text" size="small">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-dialog>
        <el-dialog :title="username" :visible.sync="showAddContract" :close-on-click-modal="false">
            <el-form :model="form">
                <el-form-item>
                    <el-upload ref="uploadComponet"
                               action="#"
                               :limit="5"
                               accept="application/pdf"
                               :auto-upload="false">
                        <el-button slot="trigger" size="small">上传合同</el-button>
                    </el-upload>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="showAddContract = false">取 消</el-button>
                <el-button type="primary" @click="saveAddContract">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<style scoped>
    .home {
        padding: 10px 20px;
    }

</style>
<script>
    export default {
        name: 'Home',
        data() {
            return {
                username: '',
                loginId: '',
                userId: '',
                tableData: [],
                dialogImageUrl: '',
                dialogVisible: false,
                imgVisible: false,
                disabled: false,
                uploadUrl: "",
                headers: {
                    token: localStorage.getItem("token")
                },
                uploadStatus: false,
                fileData: {},
                fileList: [],
                page: {
                    totalPages: 0,
                    currentPage: 0,
                    total: 100
                },
                searchText: '',
                showContract: false,
                showAddContract: false,
                contractList: []
            }
        },
        methods: {
            download(row) {
                let loginId = this.loginId;
                let name = row.name;
                let status = row.status;
                let contractId = row.id;
                let downloadUrl = this.config.url.server_url + "/file/download";
                downloadUrl += "?loginId=" + loginId;
                downloadUrl += "&contractId=" + contractId;
                downloadUrl += "&status=" + status;
                downloadUrl += "&name=" + name;
                downloadUrl += "&token=" + localStorage.getItem("token");
                window.open(downloadUrl, "_blank");
            },
            handleClose(){
                this.$refs.uploadComponet.clearFiles();
            },
            addContract(row){
                this.username = row.username;
                this.loginId = row.loginId;
                this.userId = row.id;
                this.showAddContract = true;
                this.$refs.uploadComponet.clearFiles();
            },
            saveAddContract(){
                let _this = this;
                let formData = new FormData();
                let files = this.$refs.uploadComponet.uploadFiles;
                formData.append('loginId', this.loginId);
                formData.append('userId', this.userId);
                if (!files || files.length === 0) {
                    _this.$message.warning('请上传合同！');
                    return;
                }
                files.forEach(function (file) {
                    formData.append('files', file.raw);
                })
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                _this.$axios.post("/contract", formData, {
                    'Content-Type': 'multipart/form-data'
                }).then(function () {
                    loading.close();
                    _this.$message.success('保存成功');
                    _this.showAddContract = false;
                    _this.loadData();
                }).catch(function (error) {
                    loading.close();
                    _this.$message.error(error.response.data.message);
                });
            },
            delUser(row){
                let id = row.id;
                let loginId = row.loginId;
                let _this = this;
                var url = "/user/{id}/{loginId}";
                url = url.replace("{id}", id);
                url = url.replace("{loginId}", loginId);
                this.$confirm('此操作将删除该用户及合同, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    _this.$axios.delete(url).then(function () {
                        loading.close();
                        _this.$message.success('删除成功');
                        _this.loadData();
                    }).catch(function (error) {
                        loading.close();
                        _this.$message.error(error.response.data.message);
                    });
                });
            },
            delContract(row){
                let id = row.id;
                let loginId = this.loginId;
                let _this = this;
                var url = "/contract/{id}/{loginId}";
                url = url.replace("{id}", id);
                url = url.replace("{loginId}", loginId);
                if (row.status === '02') {
                    this.$message.warning('该合同已经签名了，不能删除！');
                    return;
                }
                this.$confirm('此操作将删除合同, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    const loading = this.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                    _this.$axios.delete(url).then(function () {
                        loading.close();
                        _this.showContract = false;
                        _this.$message.success('删除成功');
                        _this.loadData();
                    }).catch(function (error) {
                        loading.close();
                        _this.$message.error(error.response.data.message);
                    });
                });
            },
            getStatus(row, column, cellValue, index) {
                let val = ""
                if (cellValue === '01') {
                    val = "未签名"
                }
                if (cellValue === '02') {
                    val = "已签名"
                }
                return val;
            },
            getContractSize(row){
                return row.contractList.length;
            },
            loadData(page) {
                if (!page) {
                    page = 1;
                }
                page--;
                let _this = this;
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.$axios.get("/user/page", {
                    params: {
                        page: page,
                        size: 10,
                        entity: {
                            username: _this.searchText
                        }
                    }
                }).then(function (result) {
                    loading.close();
                    _this.page.totalPages = result.data.data.totalPages;
                    _this.page.currentPage = result.data.data.number + 1;
                    _this.page.total = result.data.data.totalElements;
                    _this.tableData = result.data.data.content;
                });
            },
            lookContract(row) {
                let contractList = row.contractList;
                this.username = row.username;
                this.loginId = row.loginId;
                this.showContract = true;
                this.contractList = contractList;
            },
            viewContract(row){
                let loginId = this.loginId;
                let status = row.status;
                let contractId = row.id;
                this.$router.push({
                    name: "About",
                    params: {
                        loginId: loginId,
                        contractId: contractId,
                        status: status
                    }
                })
            },
            addUserAndContract: function () {
                this.$router.push({
                    name: "UserContract"
                })
            }
        },
        mounted: function () {
            this.$axios.defaults.headers.common['token'] = localStorage.getItem("token");
            this.loadData();
            this.uploadUrl = this.config.url.server_url + "/file/upload"
        }
    }
</script>
