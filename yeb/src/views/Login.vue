<template>
  <div>
    <el-form :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名：">
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码：">
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" v-model="loginForm.code" placeholder="点击图片更换验证码" style="width: 200px; margin-right: 5px">
        </el-input>
        <img :src="captchaUrl" @click="updateCaptcha" style="cursor: pointer">
      </el-form-item>
      <el-form-item>
        <el-checkbox v-model="checked">记住我</el-checkbox>
        <el-button type="primary" @click="submitLogin" style="width:100%">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "Login",
  data(){
    return{
      captchaUrl:'/captcha?time=' + new Date(),
      loginForm:{
        username:'',
        password:'',
        code:''
      },
      checked: true,
      rules:{
        username:[{required:true, message:'请输入用户名', trigger:'blur'}],
        password:[{required:true, message:'请输入密码', trigger:'blur'}],
        code:[{required:true, message:'请输入验证码', trigger:'blur'}],
      }
    }
  },
  methods:{
    updateCaptcha(){
      this.captchaUrl = '/captcha?time=' + new Date();
    },
    submitLogin(){
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          this.$message.error('请输入必填字段！');
          return false;
        }
      });
    }
  }
}
</script>

<style>
  .loginContainer{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 15px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .loginTitle{
    margin: 0px auto 40px auto;
    text-align: center;
  }

</style>