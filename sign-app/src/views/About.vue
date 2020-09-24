<template>
    <div id="about-main">
        <el-pagination layout="prev, pager, next" :page-count="total"
                       :page-size="1"
                       @current-change="viewPdfByPage"
                       :hide-on-single-page="true"
                       :current-page="current"
                       :total="total">
        </el-pagination>
        <pdf :src="pdfUrl" v-for="i in numPages" :key="i" :page="i"></pdf>
        <div id="btn-contaner" v-show="role==='02'">
            <el-button v-show="status==='01'" id="sign-btn" type="primary" @click="toSignView" icon="el-icon-edit">签名

            </el-button>
            <el-button v-show="status==='02'" id="download-btn" type="primary" @click="download"
                       icon="el-icon-download">下载

            </el-button>
        </div>
    </div>
</template>

<style>
    #about-main {
        width: 100%;
        height: 100%;
    }

    #btn-contaner {
        position: fixed;
        bottom: 1.25rem;
        text-align: center;
        width: 100%;
    }

    .el-pagination {
        position: fixed;
        width: 100%;
        z-index: 1;
    }
</style>

<script>
    import pdf from 'vue-pdf';

    export default {
        components: {
            pdf
        },
        data() {
            return {
                pdfUrl: "",
                role: localStorage.getItem("role"),
                loginId: "",
                contractId: "",
                name: "",
                status: "",
                numPages: 0,
                total: 1,
                current: 1,
                contractList: []
            }
        },
        methods: {
            toSignView() {
                let loginId = this.loginId;
                let contractId = this.contractId;
                this.$router.push({
                    name: "Sign",
                    params: {
                        loginId: loginId,
                        contractId: contractId
                    }
                })
            },
            download(){
                let loginId = this.loginId;
                let contractId = this.contractId;
                let name = this.name;
                let status = this.status;
                let downloadUrl = this.config.url.server_url + "/file/download";
                downloadUrl += "?loginId=" + loginId;
                downloadUrl += "&contractId=" + contractId;
                downloadUrl += "&status=" + status;
                downloadUrl += "&name=" + name;
                downloadUrl += "&token=" + localStorage.getItem("token");
                window.open(downloadUrl, "_blank");
            },
            viewPdfByPage(page){
                let loginId = this.loginId;
                let contractId = this.contractList[page - 1].id;
                let status = this.contractList[page - 1].status;
                this.name = this.contractList[page - 1].name;
                this.status = status;
                this.contractId = contractId;
                this.current = page;
                this.viewPdf(loginId, contractId, status);
            },
            viewPdf(loginId, contractId, status){
                let pdfUrl = this.config.url.server_url + "/file/preview";
                pdfUrl += "?loginId=" + loginId;
                pdfUrl += "&contractId=" + contractId;
                pdfUrl += "&status=" + status;
                pdfUrl += "&token=" + localStorage.getItem("token");
                const loading = this.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
                this.pdfUrl = pdf.createLoadingTask(pdfUrl);
                loading.close();
                this.pdfUrl.then(pdf => {
                    this.numPages = pdf.numPages;
                });
            }
        },
        mounted: function () {
            this.$axios.defaults.headers.common['token'] = localStorage.getItem("token");
            let loginId = this.$route.params.loginId;
            let contractId = this.$route.params.contractId;
            let status = this.$route.params.status;
            this.status = status;
            this.loginId = loginId;
            this.contractId = contractId;
            let _this = this;
            this.$axios.get("/contract/" + loginId).then(function (res) {
                let contractList = res.data;
                _this.total = contractList.length;
                _this.contractList = contractList;
                let page = 1;
                if(contractId !== '0'){
                    page = contractList.findIndex(function (contract) {
                        return contract.id === contractId
                    })+1;
                }
                _this.viewPdfByPage(page);
            });
            document.body.removeEventListener('touchmove', prevent);
        }
    };
</script>
