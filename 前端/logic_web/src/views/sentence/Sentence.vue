<script setup>
import { ref } from 'vue'
import { sentenceSearchService } from "@/api/sentence.js"
import { ElMessage } from 'element-plus';

// 查询数据结果模型
const responseData = ref({
  result: null,
  type:'',
  cost: '',
  success: null,
})

// 查询模型
const searchrequest = ref({
  text: '',
  type: 'Auto'
})

const startsearch = async () => {
  responseData.value.success = null;
  // 调用接口，接收数据
  let response = await sentenceSearchService(searchrequest.value);
  responseData.value.cost = response.data.cost;
  responseData.value.success = response.data.success;
  responseData.value.type = response.data.type;
  ElMessage.success("识别成功");
  if (responseData.value.success) {
    responseData.value.result = response.data.result;
  }
}
const clear= async () => {
  responseData.value = {  // 重置responseData为初始状态
    result: null,
    type:'',
    cost: '',
    success: null,
  }}
// 控制符号表显示
const showSymbolTable = ref(false);

// 显示符号表
const toggleSymbolTable = () => {
  showSymbolTable.value = !showSymbolTable.value;
}
// 符号表数据
const symbolTableData = ref([
  { symbol: '&', meaning: '与' },
  { symbol: '|', meaning: '或' },
  { symbol: '~|!', meaning: '非' },
  { symbol: '->', meaning: '蕴含' },
  { symbol: '(', meaning: '左括号' },
  { symbol: ')', meaning: '右括号' },
  { symbol: 'A', meaning: '所有路径' },
  { symbol: 'E', meaning: '存在至少一条路径' },
  { symbol: 'F', meaning: '未来某一状态' },
  { symbol: 'G', meaning: '所有状态' },
  { symbol: 'X', meaning: '下一个状态' },
  { symbol: 'U', meaning: '直到' },
  { symbol: 'a', meaning: '动作符号' },
  { symbol: 'u', meaning: '最小不动点算子' },
  { symbol: 'v', meaning: '最大不动点算子' },
  { symbol: 'P和p', meaning: '原子命题' },
  { symbol: '<>', meaning: '对某个动作符号动作' },
  { symbol: '[]', meaning: '对所有动作符号动作' }
])

</script>

<template>
  <div class="container">
    <div class="search">
      <el-card class="card" :body-style="{background: 'rgba(255,255,255,.5)'}">
        <el-form inline>
          <el-form-item class="form-item">
            <el-input v-model="searchrequest.text" placeholder="请输入字符串" size="large" style="width: 15cm;height: 1.5cm"
              @click="clear"></el-input>
          </el-form-item>
          <el-form-item class="form-item">
            <el-select placeholder="请选择类型" v-model="searchrequest.type" @click="clear">
              <el-option label="自动识别" value="Auto"></el-option>
              <el-option label="CTL" value="CTL"></el-option>
              <el-option label="Mu-" value="Mu-"></el-option>
              <el-option label="NLP" value="NLP"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="form-item">
            <el-button type="primary" size="large" @click="startsearch">查询</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 添加符号表按钮 -->
    <el-button v-if="responseData.success === null" type="text" @click="toggleSymbolTable">符号表</el-button>

    <!-- 显示符号表 -->
    <el-card v-if="showSymbolTable" class="card symbol-table" :body-style="{ background: 'rgba(255,255,255,.5)' }">
      <el-table :data="symbolTableData" style="width: 100%">
        <el-table-column label="符号" width="100" prop="symbol"></el-table-column>
        <el-table-column label="含义" prop="meaning"></el-table-column>
      </el-table>
    </el-card>
    <div class="result" v-if="responseData.success !== null">
      <el-card class="card result-card" :body-style="{ background: 'rgba(255,255,255,.5)' }">
        <div v-if="responseData.success === true">
          <div v-if="((searchrequest.type === 'CTL')||((searchrequest.type === 'Auto')))&&(responseData.result === null)">
            <p>输入语句 "{{ searchrequest.text }}" 属于{{ responseData.type }}公式,是经典命题公式</p>
          </div>
          <div v-else>
            <p>输入语句 "{{ searchrequest.text }}" 属于{{ responseData.type }}公式</p>
            <el-table v-if="responseData.result !== null" :data="responseData.result" style="width: 100%">
              <el-table-column label="Token" width="100" prop="word"></el-table-column>
              <el-table-column label="Type" prop="tags"></el-table-column>
              <template #empty>
                <el-empty description="没有数据" />
              </template>
            </el-table>
          </div>
        </div>
        <div v-else>
          <p>输入语句 "{{ searchrequest.text }}" 不属于{{ responseData.type }}公式</p>
        </div>
        <div class="cost">消耗时间：{{ responseData.cost }}s</div>
      </el-card>
    </div>
  </div>
</template>

<style lang="scss">
.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-size: cover;
  transform-origin: center; /* 保持中心点不变 */
}

.search {
  margin-bottom: 2cm;
  margin-top: -6cm;
  opacity: .7;
}

.card {
  width: 700px;
  padding: 20px;
  box-sizing: border-box;
}

.form-item {
  margin-right: 20px;
}

.result {
  margin-bottom: 3cm;
  opacity: .7;
}

.result-card {
  width: 700px;
  padding: 20px;
  box-sizing: border-box;
}

.cost {
  margin-top: 20px;
  color: #999;
}
.symbol-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
}
</style>