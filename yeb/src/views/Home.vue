<template>
  <div>
    <el-container>
      <el-header class="homeHeader">
        <div class="title">Springboot-Vue前后端分离</div>
        <div>
        <el-dropdown class="userInfo" @command="commandHandler">
          <span class="el-dropdown-link">{{ user.name }}<i><img :src="user.userface"></i></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        </div>
      </el-header>
      
      <el-container>
        <el-aside width="200px">
          <el-menu router>
            <el-submenu :index="index+''" v-for="(item,index) in routes" v-if="!item.hidden" :key="index">
              <template #title>
                <i :class="item.iconCls" style="color: aqua; margin-right: 5px"></i><span>{{item.name}}</span>
              </template>
              <el-menu-item-group>
                <el-menu-item :index="childItem.path" v-for="(childItem,childIndex) in item.children" :key="childIndex">
                  <i :class="childItem.iconCls" style="color: aqua; margin-right: 5px"></i><span>{{childItem.name}}</span>
                </el-menu-item>
              </el-menu-item-group>
            </el-submenu>
          </el-menu>
        </el-aside>
        
        <el-main>
          <el-breadcrumb v-if="this.$router.currentRoute.path!='/home'" separator="/">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="welcome" v-if="this.$router.currentRoute.path=='/home'">
            欢迎学习Springboot-Vue前后端分离项目
          </div>
          <router-view class="router-view"/>
        </el-main>
        
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Home",
  data(){
    return{
      user: JSON.parse(window.sessionStorage.getItem("user"))
    }
  },
  methods:{
    commandHandler(command){
      if(command == 'logout'){
        this.$confirm('此操作将注销登录，是否继续？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          this.getRequest("/logout");
          window.sessionStorage.removeItem("tokenStr");
          window.sessionStorage.removeItem("user");
          this.$store.commit("initRoutes",[]);
          this.$router.replace("/");
        }).catch(()=>{
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      }else if(command == 'userinfo'){
        // this.$router.push('/hrinfo')
      }
    }
  },
  computed:{
    routes(){
      return this.$store.state.routes;
    }
  }
}
</script>

<style>
  .homeHeader{
    background-color: #cac6c6;
    padding: 0px 15px;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .homeHeader .title{
    font-size: 35px;
    font-family: fantasy;
    color: #ffffff;
  }

  .homeHeader .userInfo{
    cursor: pointer;
  }

  .el-dropdown-link img{
    width: 36px;
    height: 36px;
    border-radius: 18px;
    margin-left: 8px;
  }
  
  .welcome{
    text-align: center;
    font-size: 40px;
    font-family: 微软雅黑;
    padding-top: 85px;
    color: lime;
  }
  
  .router-view{
    margin-top: 10px;
  }
</style>