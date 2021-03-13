<template>
  <div>
    <div>
      <el-input size="small" style="width: 240px; margin-right: 10px"
          placeholder="请输入角色英文名" v-model="role.name">
        <template #prepend>ROLE_</template></el-input>
      <el-input size="small" style="width: 180px; margin-right: 10px"
          placeholder="请输入角色中文名" v-model="role.nameZh"></el-input>
      <el-button size="small" type="primary" @click="addRole">添加</el-button>
    </div>
    
    <div class="roleCollapse" >
      <el-collapse v-model="activeName" accordion @change="changeRole">
        <el-collapse-item v-for="(r,index) in roles" :key="index"
                          :title="r.nameZh" :name="r.id">
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>可访问资源</span>
                <el-button class="deleteRole" @click="deleteRole(r.id)"
                           type="danger" size="small">删除角色</el-button>
              </div>
            </template>
            <div>
              <el-tree show-checkbox
                       node-key="id"
                       ref="tree"
                       :key="index"
                       :default-checked-keys="selectedMenus"
                  :data="allMenus" :props="defaultProps"></el-tree>
              <div class="roleMenusButton">
                <el-button size="mini" @click="cancelUpdate">取消修改</el-button>
                <el-button size="mini" type="primary" @click="doUpdate(r.id, index)">确认修改</el-button>
              </div>
            </div>
            
          </el-card>
          
        </el-collapse-item>
      </el-collapse>
    </div>
    
  </div>
</template>

<script>
export default {
  name: "PermissMana",
  data(){
    return{
      role:{
        name:'',
        nameZh:''
      },
      roles:[],
      allMenus:[],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      selectedMenus:[],
      activeName: -1,
      
    }
  },
  mounted() {
    this.initRoles();
  },
  methods:{
    addRole(){
      if(this.role.name && this.role.nameZh){
        this.postRequest('/system/basic/permiss/role',this.role).then(resp=>{
          if(resp){
            this.initRoles();
            this.role.name = '';
            this.role.nameZh = '';
          }
        })
      }else{
        this.$message.error("请输入完整信息！");
      }
    },
    initRoles(){
      this.getRequest("/system/basic/permiss/role").then(resp=>{
        if(resp){
          this.roles = resp;
        }
      });
    },
    initAllMenus(){
      this.getRequest("/system/basic/permiss/menu").then(resp=>{
        if(resp){
          this.allMenus = resp;
        }
      });
    },
    initSelectedMenus(rid){
      this.getRequest("/system/basic/permiss/menu/" + rid).then(resp=>{
        if(resp){
          this.selectedMenus = resp;
        }
      });
    },
    changeRole(rid){
      if(rid){
        this.initAllMenus();
        this.initSelectedMenus(rid);
      }
    },
    deleteRole(rid){
      if(rid){
        this.deleteRequest('/system/basic/permiss/role/'+ rid).then(resp=>{
          if(resp){
            this.initRoles();
            this.activeName = -1;
          }
        });
      }
    },
    doUpdate(rid, index){
      let tree = this.$refs.tree[index];
      let selectedKeys = tree.getCheckedKeys(true);
      let url = '/system/basic/permiss/?rid=' + rid;
      selectedKeys.forEach(key=>{
        url += '&mids=' + key;
      });
      this.putRequest(url).then(resp=>{
        if(resp){
          this.initRoles();
          this.activeName = -1;
        }
      });
    },
    cancelUpdate(){
      this.activeName = -1;
    }
  }
}
</script>

<style>
  .roleCollapse{
    margin-top: 10px;
    width: 65%;
  }

  .deleteRole{
    float: right;
  }
  
  .roleMenusButton{
    display: flex;
    justify-content: flex-end;
  }
</style>