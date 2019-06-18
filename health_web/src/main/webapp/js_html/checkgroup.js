var vue = new Vue({
    el: '#app',
    data: {
        activeName: 'first',//添加/编辑窗口Tab标签名称
        pagination: {//分页相关属性
            currentPage: 1,
            pageSize: 10,
            total: 100,
            queryString: null,
        },
        dataList: [],//列表数据
        formData: {},//表单数据
        tableData: [],//新增和编辑表单中对应的检查项列表数据
        checkitemIds: [],//新增和编辑表单中检查项对应的复选框，基于双向绑定可以进行回显和数据提交
        dialogFormVisible: false,//控制添加窗口显示/隐藏
        dialogFormVisible4Edit: false//控制编辑窗口显示/隐藏
    },
    created() {
        this.findPage();
    },
    methods: {
        //编辑
        handleEdit() {
            axios.post(
                '/health_web/checkgroup/edit.do?checkitemIds=' + this.checkitemIds,
                this.formData
            ).then((response) => {
                this.dialogFormVisible4Edit = false;
                this.$message({
                    message: response.data.message,
                    type: response.data.flag ? 'success' : 'error'
                });
            }).finally(() => {
                this.findPage();
            })
        },
        //添加
        handleAdd() {
            axios.post(
                '/health_web/checkgroup/add.do?checkitemIds=' + this.checkitemIds, this.formData
            ).then((response) => {
                this.$message({
                    message: response.data.message,
                    type: response.data.flag ? 'success' : 'error'
                });
            }).finally(() => {
                this.findPage();
            })
        },
        //分页查询
        findPage() {
            var param = {
                currentPage: this.pagination.currentPage,
                pageSize: this.pagination.pageSize,
                queryString: this.pagination.queryString,
            };
            axios.post('/health_web/checkgroup/findPage.do', param).then((response) => {
                this.dataList = response.data.rows;
                this.pagination.total = response.data.total;
            });
        },
        // 重置表单
        resetForm() {
            this.formData = {};
        },
        // 弹出添加窗口
        handleCreate() {
            this.resetForm();
            this.dialogFormVisible = true;
            this.activeName = 'first';
            this.checkitemIds = [];
            axios.get('/health_web/checkitem/findAll.do').then((response) => {
                if (response.data.flag) {
                    this.tableData = response.data.data;
                } else {
                    this.$message({
                        message: response.data.message,
                        type: 'error'
                    });
                }
            });
        },
        // 弹出编辑窗口
        handleUpdate(row) {
            axios.get('/health_web/checkgroup/findById.do?id=' + row.id).then((response) => {
                if (response.data.flag) {
                    this.dialogFormVisible4Edit = true;
                    this.activeName = 'first';
                    this.formData = response.data.data;
                    axios.get('/health_web/checkitem/findAll.do').then((response) => {
                        if (response.data.flag) {
                            this.tableData = response.data.data;
                            axios.get('/health_web/checkitem/findCheckItemIdsByCheckGroupId.do?id=' + row.id).then((response) => {
                                this.checkitemIds = response.data.data;
                            });
                        } else {
                            this.$message.error(response.data.message);
                        }
                    });
                } else {
                    this.$message.error("获取数据失败，请刷新当前页面");
                }
            })
        },
        //切换页码
        handleCurrentChange(currentPage) {
            this.pagination.currentPage = currentPage;
            this.findPage();
        },
        // 删除
        handleDelete(row) {
            axios.get('/health_web/checkgroup/delete.do?id=' + row.id).then((response) => {
                this.$message({
                    message: response.data.message,
                    type: response.data.flag ? 'success' : 'error'
                });
            }).finally(() => {
                this.findPage();
            });
        }
    }
})