<script setup>
import {
    User,
    Crop,
    EditPen,
    SwitchButton,
    CaretBottom
} from '@element-plus/icons-vue'
import avatar from '@/assets/default.png'

import {userInfoService} from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import {useTokenStore} from '@/stores/token.js'
const tokenStore = useTokenStore();
const userInfoStore = useUserInfoStore();
//调用函数,获取用户详细信息
const getUserInfo = async()=>{
    //调用接口
    let result = await userInfoService();
    //数据存储到pinia中
    userInfoStore.setInfo(result.data);
}

getUserInfo();
//条目被点击后,调用的函数
import {useRouter} from 'vue-router'
const router = useRouter();
const goHome = () => {
    router.push('/')
  }
import {ElMessage,ElMessageBox} from 'element-plus'
const handleCommand = (command)=>{
    //判断指令
    if(command === 'logout'){
        //退出登录
        ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            //退出登录
            //1.清空pinia中存储的token以及个人信息
            tokenStore.removeToken()
            userInfoStore.removeInfo()

            //2.跳转到登录页面
            router.push('/login')
            ElMessage({
                type: 'success',
                message: '退出登录成功',
            })
            
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '用户取消了退出登录',
            })
        })
    }else{
        //路由
        router.push('/user/'+command)
    }
}

</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        <!-- 主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div><strong><el-icon>
                            <User />
                        </el-icon>
                        {{ userInfoStore.info.nickname?userInfoStore.info.nickname:"用户" }}</strong>,欢迎使用LLRP</div>
                <!-- 下拉菜单 -->
                <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="userInfoStore.info.userPic? userInfoStore.info.userPic:avatar" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="history" :icon="Crop">搜索记录</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <router-view></router-view>
                <el-button v-if="$route.path !== '/search'" type="success" class="home-button"
                    @click="goHome">返回查询</el-button>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>LLRP ©2023 Created by zest</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;
    background-image: url('@/assets/layout.png');
    .home-button {
      position: absolute;
      bottom: 100px;
      right: 150px;
    }

    .el-header {
        background-color: rgba(255, 255, 255, 0.5);
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #999;
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }
    

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>