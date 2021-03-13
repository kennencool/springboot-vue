<template>
  <div>
    <div>
      <el-input size="small" class="addJob"
                placeholder="添加职称..." v-model="job.name"
                suffix-icon="el-icon-circle-plus-outline">
      </el-input>
      <el-select v-model="job.titleLevel" placeholder="请选择级别" size="small" class="selectJobs">
        <el-option
            v-for="item in titleLevels"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button size="small" type="primary" @click="addJob">添加</el-button>
    </div>
    
    <div>
      <el-table :data="jobs" style="width: 75%" border stripe class="jobTable"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="40" ></el-table-column>
        <el-table-column prop="id" label="编号" width="60"></el-table-column>
        <el-table-column prop="name" label="职位" width="120"></el-table-column>
        <el-table-column prop="titleLevel" label="级别" width="180"></el-table-column>
        <el-table-column prop="createDate" label="创建日期" width="240"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
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
               @click="deleteJobs"
               :disabled="this.multipleSelection.length == 0">批量删除</el-button>
    <el-dialog title="编辑职称" :visible.sync="dialogVisible" width="35%">
      <div>
        <el-tag>职称信息</el-tag>
        <el-input v-model="update.name" size="small" class="editJob" style="margin-right: 10px"></el-input>
        <el-select v-model="update.titleLevel" placeholder="请选择级别" size="small" class="selectJobs">
          <el-option
              v-for="item in titleLevels"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
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
  name: "JobMana",
  data(){
    return{
      job:{
        name:'',
        titleLevel:''
      },
      update:{
        name:'',
        titleLevel:''
      },
      titleLevels: [
          '正高级',
          '副高级',
          '中级',
          '初级',
          '员级'
      ],
      jobs: [],
      dialogVisible: false,
      multipleSelection: []
    }
  },
  mounted() {
    this.initJobs();
  },
  methods:{
    initJobs(){
      this.getRequest("/system/basic/job/").then(resp=>{
        if(resp){
          this.jobs = resp;
        }
      })
    },
    addJob(){
      if(this.job.name && this.job.titleLevel){
        this.postRequest("/system/basic/job/", this.job).then(resp=>{
          if(resp){
            this.initJobs();
          }
        })
      }else{
        this.$message.error("请添加完整的信息！");
      }
    },
    handleEdit(index, job){
      this.dialogVisible = true;
      Object.assign(this.update, job);
    },
    handleDelete(index, job){
      this.$confirm('此操作将删除['+ job.name +'，'+ job.titleLevel +']，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.deleteRequest("/system/basic/job/" + job.id).then(resp=>{
          if(resp){
            this.initJobs();
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
      if(this.update.name && this.update.titleLevel){
        this.update.createDate = '';
        this.putRequest("/system/basic/job/", this.update).then(resp=>{
          if(resp){
            this.initJobs();
            this.dialogVisible = false;
          }
        });
      }else{
        this.$message.error("请添加完整的信息！");
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    deleteJobs(){
      this.$confirm('此操作将删除['+ this.multipleSelection.length +']条职称信息，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        let ids = '?';
        this.multipleSelection.forEach(item=>{
          ids +=  'ids=' + item.id + "&";
        })
        this.deleteRequest("/system/basic/job/" + ids).then(resp=>{
          if(resp){
            this.initJobs();
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
  .addJob{
    width: 300px;
    margin-right: 10px;
  }
  
  .selectJobs{
    margin-right: 10px;
  }
  
  .jobTable{
    margin-top: 15px;
  }

  .editJob{
    margin-top: 10px;
    margin-left: 10px;
    margin-bottom: 20px;
    width: 100px;
  }
</style>