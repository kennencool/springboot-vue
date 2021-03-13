<template>
  <div>
    <div>
      <el-input placeholder="输入部门名称进行搜索..." style="width: 500px"
                prefix-icon="el-icon-search" v-model="filterText">
      </el-input>

      <el-tree
          class="depTree"
          :data="deps"
          :props="defaultProps"
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          ref="tree">
        <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span>
            <el-button size="mini" @click="addDep(data)" class="depButton" type="primary">添加子部门</el-button>
            <el-button size="mini" @click="removeDep(data)" class="depButton" type="danger">删除部门</el-button>
          </span>
        </span>
        </template>
      </el-tree>
      
      <el-dialog title="添加部门" :visible.sync="dialogVisible" width="320px">
          <table>
            <tr>
              <td><el-tag style="margin-right: 60px">上级部门：</el-tag>{{pname}}</td>
            </tr>
            <tr>
              <td><el-tag>部门名称：</el-tag><el-input v-model="dep.name" size="small" class="addDep"></el-input>
              </td>
            </tr>
          </table>
        <span class="dialog-footer">
      <el-button @click="dialogVisible = false" size="mini">取 消</el-button>
      <el-button type="primary" @click="doUpdate" size="mini">确 定</el-button>
    </span>
      </el-dialog>
    </div>
    
    <div></div>
  </div>
</template>

<script>
export default {
  name: "DepMana",
  data(){
    return{
      filterText:'',
      deps:[],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      dialogVisible: false,
      dep: {
        name: '',
        parentId: -1
      },
      pname:'',
    }
  },
  mounted() {
    this.initDeps();
  },
  methods: {
    initDeps(){
      this.getRequest("/system/basic/department/").then(resp=>{
        if(resp){
          this.deps = resp;
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    addDep(data){
      this.dep.parentId = data.id;
      this.pname = data.name;
      this.dialogVisible = true;
    },
    removeDep(data){
      this.$confirm('此操作将删除部门['+ data.name +']，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.deleteRequest("/system/basic/department/" + data.id).then(resp=>{
          if(resp){
            this.initDeps();
          }
        });
      }).catch(()=>{
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    }
    ,
    initDep(){
      this.dep = {
        name:'',
        parentId: -1
      };
      this.pname = '';
    },
    doUpdate(){
      if(this.dep.name && this.dep.parentId){
        this.postRequest("/system/basic/department/",this.dep).then(resp=>{
          if(resp){
            this.initDeps();
            this.dialogVisible = false;
            this.initDep();
          }
        });
      }else{
        this.$message.error("请添加完整的信息！");
      }
    },
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
}
</script>

<style>
  .depTree{
    width: 500px;
    margin-top: 10px;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  
  .depButton{
    padding: 2px;
  }
  
  .addDep{
    width: 160px;
    margin-left: 5px;
    margin-bottom: 10px;
  }
</style>