<template>
  <div>
    <div>
      <el-input class="addPosInput"
                size="small"
          placeholder="添加职位..." suffix-icon="el-icon-circle-plus-outline" v-model="pos.name">
      </el-input>
      <el-button class="addPosButton" size="small" type="primary" @click="addPosition">添加</el-button>
    </div>
    <div class="posManaMain">
      <el-table
          stripe
          border
          :data="positions"
          @selection-change="handleSelectionChange"
          style="width: 70%">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="编号" width="90"></el-table-column>
        <el-table-column prop="name" label="职位" width="180"></el-table-column>
        <el-table-column prop="createDate" label="创建时间" width="260"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button
                size="mini"
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button type="danger" style="margin-top: 10px;" size="small" 
               @click="deletePositions"
               :disabled="this.multipleSelection.length == 0">批量删除</el-button>
    <el-dialog title="编辑职位" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="update.name" size="small" class="editPosition"></el-input>
      </div>
    <span class="dialog-footer">
      <el-button @click="dialogVisible = false" size="small">取 消</el-button>
      <el-button type="primary" @click="doUpdate" size="small">确 定</el-button>
    </span>
    </el-dialog>
    
  </div>
</template>

<script>
export default {
  name: "PosMana",
  data(){
    return{
      pos:{
        name: ''
      },
      positions: [],
      dialogVisible: false,
      update:{
        name:''
      },
      multipleSelection: []
    }
  },
  mounted() {
    this.initPositions();
  },
  methods:{
    initPositions(){
      this.getRequest("/system/basic/pos/").then(resp=>{
        if(resp){
          this.positions = resp;
          this.pos.name = '';
        }
      })
    },
    addPosition(){
      if(this.pos.name){
        this.postRequest("/system/basic/pos/",this.pos).then(resp=>{
          if(resp){
            this.initPositions();
          }
        });
      }else{
        this.$message.error("请输入职位名称！");
      }
    },
    handleEdit(index, position) {
      this.dialogVisible = true;
      Object.assign(this.update,position);
    },
    handleDelete(index, position) {
      this.$confirm('此操作将删除['+ position.name +']，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.deleteRequest("/system/basic/pos/" + position.id).then(resp=>{
          if(resp){
            this.initPositions();
          }
        });
      }).catch(()=>{
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    doUpdate(){
      if(this.update.name){
        this.update.createDate = '';
        this.putRequest("/system/basic/pos/",this.update).then(resp=>{
          if(resp){
            this.initPositions();
            this.dialogVisible = false;
          }
        });
      }else{
        this.$message.error("请输入职位名称！");
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deletePositions(){
      this.$confirm('此操作将删除['+ this.multipleSelection.length +']条职位，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let ids = '?';
        this.multipleSelection.forEach(item=>{
          ids +=  'ids=' + item.id + "&";
        })
        this.deleteRequest("/system/basic/pos/" + ids).then(resp=>{
          if(resp){
            this.initPositions();
          }
        });
      }).catch(()=>{
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    }
    
  }
}
</script>

<style>
  .addPosInput{
    width: 300px;
    margin-right: 30px;
  }
  
  .posManaMain{
    margin-top: 10px;
  }
  
  .editPosition{
    margin-top: 10px;
    margin-left: 10px;
    margin-bottom: 20px;
    width: 180px;
  }

</style>