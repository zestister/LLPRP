<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'

//用户搜索时选中的类型
const type = ref('')
//用户搜索时选中的结果状态
const state = ref('')
//列表数据模型
const Sentence = ref([
{
    "id": 0,
    "sentence": '',
    "sentenceType": '',
    "createUser": '',
    "createTime": '',
    "state": ''
    }
])
const responseData =ref([
    {
        "id": 0,
        "word": "今天",
        "tags": "",
        "sentenceId": 0,
        "createUser": 0
    }
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(5)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    sentencelist()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    sentencelist()
}
//获取语句历史记录
import { sentenceListService, sentenceDeleteService ,sentenceGetService} from '@/api/sentence.js'
const sentencelist = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        type: type.value ? type.value : null,
        state: state.value ? state.value : null
    }
    let result = await sentenceListService(params);
    //渲染视图
    total.value = result.data.total;
    Sentence.value = result.data.items;
}

//删除记录
import { ElMessage, ElMessageBox } from 'element-plus'
const deleteSentence = (row) => {
    //提示用户  确认框

    ElMessageBox.confirm(
        '你确认要删除该条记录吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //调用接口
            let result = await sentenceDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            //刷新列表
            sentencelist();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消了删除',
            })
        })
}
//控制添加分类弹窗
const dialogVisible = ref(false)

const title = ref('')

//添加分类数据模型
const showDialog = async (row) => {
    // 确保 row 对象存在且 row.id 是一个有效的数字

    dialogVisible.value = true; // 设置对话框为可见
    if (row.state === '成功') {
        title.value = row.sentence + " 属于 " + row.sentenceType; // 设置对话框标题为 row 对象中的 sentence 属性
    } else {
        title.value = row.sentence + " 不属于 " + row.sentenceType;
    }

    // 异步获取详情数据
    let result = await sentenceGetService(row.id);
    responseData.value = result.data;

}
sentencelist()

</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>搜索历史</span>

            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="语句分类：">
                <el-select placeholder="请选择类型" v-model="type">
                    
                    <el-option label="CTL" value="CTL"></el-option>
                    <el-option label="Mu-" value="Mu-"></el-option>
                    <el-option label="NLP" value="NLP"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="结果状态：">
                <el-select placeholder="全部" v-model="state">
                    <el-option label="成功" value="成功"></el-option>
                    <el-option label="失败" value="失败"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="sentencelist">搜索</el-button>
                <el-button @click="type = '', state = ''">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 列表 -->
        <el-table :data="Sentence" style="width: 100%">
            <el-table-column label="字符串" width="400" prop="sentence"></el-table-column>
            <el-table-column label="分类" prop="sentenceType"></el-table-column>
            <el-table-column label="查询时间" prop="createTime"> </el-table-column>
            <el-table-column label="结果" prop="state"></el-table-column>
            <el-table-column label="详情&删除" width="150">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDialog(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteSentence(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
        <!-- 详情弹窗 -->
        <el-dialog v-model="dialogVisible" :title="title" width="30%">
            <el-table  :data=responseData style="width: 100%">
                <el-table-column label="Token" width="100" prop="word"></el-table-column>
                <el-table-column label="Type" prop="tags"></el-table-column>
                <template #empty>
                    <el-empty description="没有数据" />
                </template>
            </el-table>
            <template #footer>
                <span class="dialog-footer">
                    <el-button type="info" @click="dialogVisible = false"> 关闭详情 </el-button>
                </span>
            </template>
        </el-dialog>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}
</style>