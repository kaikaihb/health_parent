var vue = new Vue({
    el: '#app',
    data: {
        autoUpload: true,//自动上传
        imageUrl: null,//模型数据，用于上传图片完成后图片预览
        activeName: 'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize: 10,
            total: 100,
            queryString: null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData: [],//添加表单窗口中检查组列表数据
        checkgroupIds: [],//添加表单窗口中检查组复选框对应id
        dialogFormVisible: false//控制添加窗口显示/隐藏
    },
    created() {
        this.findPage();
    },
    methods: {
        //文件上传成功后的钩子，response为服务端返回的值，file为当前上传的文件封装成的js对象
        handleAvatarSuccess(response, file) {
            this.imageUrl = "https://health-1256102088.cos.ap-guangzhou.myqcloud.com/" + response.data;
            this.$message({
                message: response.message,
                type: response.flag ? 'success' : 'error'
            });
            this.formData.img = response.data;
        },
        //上传图片之前执行
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG) {
                this.$message.error('上传套餐图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传套餐图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },
        //添加
        handleAdd() {
            axios.post(
                '/health_web/setmeal/add.do?checkgroupIds=' + this.checkitemIds, this.formData
            ).then((response) => {
                this.dialogFormVisible = false;
                this.$message({
                    message: response.data.message,
                    type: response.data.flag ? 'success' : "error"
                }).finally(() => {
                    this.findPage();
                });
            });
        },
        //分页查询
        findPage() {
            var param = {
                currentPage:this.pagination.currentPage,
                pageSize:this.pagination.pageSize,
                queryString:this.pagination.queryString
            };
            axios.post('/health_web/setmeal/findPage.do',param).then((response)=>{
                this.dataList = response.data.rows;
                this.pagination.total = response.data.total;
            });
        },
        // 重置表单
        resetForm() {
            this.formData = {};
            this.activeName = 'first';
            this.checkgroupIds = [];
            this.imageUrl = null;
        },
        // 弹出添加窗口
        handleCreate() {
            this.resetForm();
            this.dialogFormVisible = true;
            axios.get('/health_web/checkgroup/findAll.do').then((response) => {
                if (response.data.flag) {
                    this.tableData = response.data.data;
                } else {
                    this.$message.error(response.data.message);
                }
            });
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        }
    }
})