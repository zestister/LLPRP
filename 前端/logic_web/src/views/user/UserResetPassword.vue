<script setup>
import { ref } from 'vue'

//用户密码
const password= ref({
    old_pwd:'',
    new_pwd:'',
    re_pwd:'',
})

//定义表单校验规则
const rules = {
    old_pwd: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 16, message: '长度为6~16位非空字符', trigger: 'blur' }
    ],
    new_pwd: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 6, max: 16, message: '长度为6~16位非空字符', trigger: 'blur' }
    ],
    re_pwd: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        { min: 6, max: 16, message: '长度为6~16位非空字符', trigger: 'blur' }
    ]
}

//修改个人信息
import {userPasswService} from '@/api/user.js'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
const router = useRouter();
const updateUserPwd = async ()=>{
    //调用接口
    let result = await userPasswService(password.value);
    ElMessage.success('修改成功,即将返回登陆界面');
    // 等待1秒钟后跳转到登录界面
  setTimeout(() => {
    router.push('/login')
  }, 1000)
    
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="password" :rules="rules" label-width="100px" size="large" style="width: 10cm;">
                    <el-form-item label="旧密码" prop="old_pwd">
                        <el-input v-model="password.old_pwd" ></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="new_pwd">
                        <el-input v-model="password.new_pwd"></el-input>
                    </el-form-item>
                    <el-form-item label="重复新密码" prop="re_pwd">
                        <el-input v-model="password.re_pwd"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserPwd">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container{
    background-color: rgba(255, 255, 255, 0.5);
    width: 60vh;
}
</style>