<template>
  <div>
    <div style="display: flex; justify-content: space-between">
      <div>
        <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..."
                  v-model="empName" @keydown.enter.native="initEmps" 
                  clearable @clear="initEmps"
                  style="width: 300px;margin-right: 10px" size="small"></el-input>
        <el-button type="primary" size="small" @click="initEmps">搜索</el-button>
        <el-button type="primary" size="small">
          <i class="fa fa-angle-double-down" aria-hidden="true"></i> 高级搜索</el-button>
      </div>
      <div>
        <el-button type="success" size="small">
          <i class="fa fa-level-up" aria-hidden="true"></i>导入数据</el-button>
        <el-button type="success" size="small">
          <i class="fa fa-level-down" aria-hidden="true"></i>导出数据</el-button>
        <el-button type="primary" size="small" @click="showAddEmp">
          <i class="el-icon-plus" aria-hidden="true" ></i>添加员工</el-button>
      </div>
    </div>
    
    <div style="margin-top: 5px">
      <el-table :data="emps" stripe border style="width: 100%"
                v-loading="loading"
                element-loading-text="拼命加载中"
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)">
        <el-table-column type="selection" width="50"></el-table-column>
        <el-table-column prop="workID" label="工号" align="left" width="90"></el-table-column>
        <el-table-column fixed prop="name" label="姓名" align="left" width="90"></el-table-column>
        <el-table-column prop="gender" label="性别" align="left" width="60"></el-table-column>
        <el-table-column prop="birthday" label="出生日期" align="left" width="160"></el-table-column>
        <el-table-column prop="idCard" label="身份证号" align="left" width="220"></el-table-column>
        <el-table-column prop="wedlock" label="婚姻状况" align="left" width="90"></el-table-column>
        <el-table-column prop="nation.name" label="民族" align="left" width="120"></el-table-column>
        <el-table-column prop="nativePlace" label="籍贯" align="left" width="120"></el-table-column>
        <el-table-column prop="politicsstatus.name" label="政治面貌" align="left" width="120"></el-table-column>
        <el-table-column prop="email" label="邮箱" align="left" width="180"></el-table-column>
        <el-table-column prop="phone" label="电话号码" align="left" width="150"></el-table-column>
        <el-table-column prop="address" label="联系地址" align="left" width="250"></el-table-column>
        <el-table-column prop="department.name" label="所属部门" align="left" width="90"></el-table-column>
        <el-table-column prop="position.name" label="职位" align="left" width="90"></el-table-column>
        <el-table-column prop="engageForm" label="聘用形式" align="left" width="90"></el-table-column>
        <el-table-column prop="tiptopDegree" label="学历" align="left" width="90"></el-table-column>
        <el-table-column prop="school" label="学校" align="left" width="150"></el-table-column>
        <el-table-column prop="specialty" label="专业" align="left" width="150"></el-table-column>
        <el-table-column prop="workState" label="在职状态" align="left" width="90"></el-table-column>
        <el-table-column prop="beginDate" label="入职日期" align="left" width="120"></el-table-column>
        <el-table-column prop="conversionTime" label="转正日期" align="left" width="120"></el-table-column>
        <el-table-column prop="beginContract" label="合同起始日期" align="left" width="120"></el-table-column>
        <el-table-column prop="endContract" label="合同终止日期" align="left" width="120"></el-table-column>
        <el-table-column prop="contractTerm" label="合同期限" align="left" width="100">
          <template slot-scope="scope">
            <el-tag>{{scope.row.contractTerm}}</el-tag>年
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="220">
          <template slot-scope="scope">
            <el-button style="padding: 3px" size="mini">编辑</el-button>
            <el-button style="padding: 3px" type="primary" size="mini">查看高级资料</el-button>
            <el-button style="padding: 3px" type="danger" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout='sizes, prev, pager, next, jumper, ->, total'
            :total="this.total">
        </el-pagination>
      </div>
    </div>
    <el-dialog
        title="添加员工"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <el-form ref="empForm" :model="emp" >
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名：" prop="name">
                <el-input v-model="emp.name" size="mini" style="width: 150px" 
                          prefix-icon="el-icon-edit" placeholder="请输入员工姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="性别：" prop="gender">
                <el-radio-group v-model="emp.gender">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期：" prop="birthday">
                <el-date-picker
                    v-model="emp.birthday"
                    type="date"
                    size="mini" style="width: 150px;"
                    value-format="yyyy-MM-dd"
                    placeholder="出生日期" >
                </el-date-picker>
              </el-form-item>            
            </el-col>
            <el-col :span="7">
              <el-form-item label="政治面貌：" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="政治面貌"
                  size="mini" style="width: 180px">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="籍贯：" prop="nativePlace">
                <el-input v-model="emp.nativePlace" size="mini" style="width: 180px"
                          prefix-icon="el-icon-edit" placeholder="请输入籍贯"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="民族：" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="民族"
                           size="mini" style="width: 120px">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="职位:" prop="posId">
                <el-select v-model="emp.posId" placeholder="职位" size="mini" clearable style="width: 130px;">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="职称:" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="职称" size="mini" clearable style="width: 130px;">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="所属部门:">
                
              </el-form-item>
            </el-col>
            <el-col :span="5">
            </el-col>
            <el-col :span="6">
              <el-form-item label="入职日期：" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    type="date"
                    size="mini" style="width: 150px;"
                    value-format="yyyy-MM-dd"
                    placeholder="入职日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="聘用形式:" prop="engageForm">
                <el-radio-group size="mini" v-model="emp.engageForm">
                  <el-radio label="劳动合同">劳动合同</el-radio>
                  <el-radio label="劳务合同">劳务合同</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <template>
          <span>
            <el-button >取 消</el-button>
            <el-button type="primary" >确 定</el-button>
          </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data(){
    return{
      total: 0,
      emps:[],
      page: 1,
      size: 10,
      loading:false,
      empName:'',
      dialogVisible:false,
      emp:{
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notWorkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null
      },
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      
    }
  },
  mounted() {
    this.initEmps();
  },
  methods:{
    initEmps(){
      this.loading = true;
      this.getRequest("/employee/basic/?currentPage="+this.page+"&size="+this.size
        +"&name="+ this.empName).then(resp=>{
        this.loading = false;
        if(resp){
          this.total = resp.total;
          this.emps = resp.data;
        }
      });
    },
    sizeChange(size){
      this.size = size;
      this.initEmps();
    },
    currentChange(current){
      this.page = current;
      this.initEmps();
    },
    showAddEmp(){
      this.dialogVisible = true;
    },
    
  }
}
</script>

<style>

</style>