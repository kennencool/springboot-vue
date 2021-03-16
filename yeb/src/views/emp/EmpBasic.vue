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
        <el-upload
            :headers="headers"
            style="display: inline-flex; margin-right: 8px;"
            show-file-list="false"
            :before-upload="beforeUpload"
            :on-success="onSuccess"
            :on-error="onError"
            :disabled="importDataDisabled"
            action="/employee/basic/import">
          <el-button type="success" size="small" :icon="importDataBtnIcon" :disabled="importDataDisabled" >
            {{ importDataBtnText }}</el-button>
        </el-upload>
        <el-button type="success" size="small" @click="exportData" icon="el-icon-download">
          导出数据</el-button>
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
        <el-table-column prop="id" label="工号" align="left" width="90"></el-table-column>
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
            <el-button style="padding: 3px" size="mini" @click="showEditEmpView(scope.row)">编辑</el-button>
            <el-button style="padding: 3px" type="primary" size="mini">查看高级资料</el-button>
            <el-button style="padding: 3px" type="danger" size="mini" @click="deleteEmp(scope.row)">删除</el-button>
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
        :title="title"
        :visible.sync="dialogVisible"
        width="85%">
      <div>
        <el-form ref="empForm" :model="emp" :rules="rules" >
          <el-row>
            <el-col :span="6">
              <el-form-item label="姓名:" prop="name">
                <el-input v-model="emp.name" size="mini" style="width: 150px" 
                          prefix-icon="el-icon-edit" placeholder="请输入员工姓名"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="性别:" prop="gender">
                <el-radio-group v-model="emp.gender">
                  <el-radio label="男">男</el-radio>
                  <el-radio label="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出生日期:" prop="birthday">
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
              <el-form-item label="政治面貌:" prop="politicId">
                <el-select v-model="emp.politicId" placeholder="政治面貌"
                  size="mini" style="width: 180px">
                  <el-option
                      v-for="item in politicsstatus"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="籍贯:" prop="nativePlace">
                <el-input v-model="emp.nativePlace" size="mini" style="width: 180px"
                          prefix-icon="el-icon-edit" placeholder="请输入籍贯"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="民族:" prop="nationId">
                <el-select v-model="emp.nationId" placeholder="民族"
                           size="mini" style="width: 120px">
                  <el-option
                      v-for="item in nations"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电子邮箱:" prop="email">
                <el-input v-model="emp.email" placeholder="请输入电子邮箱" size="mini"
                  style="width: 150px;" prefix-icon="el-icon-message">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="联系地址:" prop="address">
                <el-input v-model="emp.address" placeholder="请输入联系地址" size="mini"
                          style="width: 200px" prefix-icon="el-icon-edit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="职位:" prop="posId">
                <el-select v-model="emp.posId" placeholder="职位" size="mini" 
                           clearable style="width: 150px;">
                  <el-option
                      v-for="item in positions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="职称:" prop="jobLevelId">
                <el-select v-model="emp.jobLevelId" placeholder="职称" size="mini" 
                           clearable style="width: 150px;">
                  <el-option
                      v-for="item in jobLevels"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="所属部门:" prop="departmentId">
                <el-popover
                    placement="bottom"
                    title="所属部门"
                    :width="250"
                    trigger="manual"
                    v-model:visible="visible">
                  <el-tree :data="allDeps" :props="defaultProps" @node-click="handleNodeClick" default-expand-all></el-tree>
                  <div slot="reference" style="width: 150px; display: inline-flex; border: 1px solid #dedede; 
                        height: 24px; border-radius: 5px; cursor:pointer; margin-top: 9px;  align-items: center; font-size: 13px;padding-left: 8px;box-sizing: border-box"
                       @click="showDepView">{{inputDepName}}</div>
                </el-popover>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="电话号码:" prop="phone">
                <el-input v-model="emp.phone" placeholder="请输入电话号码" size="mini"
                          style="width: 200px;" prefix-icon="el-icon-phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="工号:" prop="id">
                <el-input v-model="emp.id" size="mini" disabled style="width: 120px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="学历:" prop="tiptopDegree">
                <el-select v-model="emp.tiptopDegree" placeholder="学历" size="mini" 
                           style="width: 120px;">
                  <el-option
                        v-for="item in tiptopDegrees"
                        :key="item"
                        :label="item"
                        :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="毕业院校:" prop="school">
                <el-input v-model="emp.school" placeholder="请输入毕业院校" size="mini"
                          style="width: 150px;"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="专业名称:" prop="specialty">
                <el-input v-model="emp.specialty" placeholder="请输入专业名称" size="mini"
                          style="width: 200px;"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="入职日期:" prop="beginDate">
                <el-date-picker
                    v-model="emp.beginDate"
                    type="date"
                    size="mini" style="width: 140px;"
                    value-format="yyyy-MM-dd"
                    placeholder="入职日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="转正日期:" prop="conversionTime">
                <el-date-picker
                    v-model="emp.conversionTime"
                    type="date"
                    size="mini" style="width: 140px;"
                    value-format="yyyy-MM-dd"
                    placeholder="转正日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="合同起始日期:" prop="beginContract">
                <el-date-picker
                    v-model="emp.beginContract"
                    type="date"
                    size="mini" style="width: 140px;"
                    value-format="yyyy-MM-dd"
                    placeholder="起始日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="合同终止日期:" prop="endContract">
                <el-date-picker
                    v-model="emp.endContract"
                    type="date"
                    size="mini" style="width: 140px;"
                    value-format="yyyy-MM-dd"
                    placeholder="终止日期" >
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="身份证号码:" prop="idCard">
                <el-input v-model="emp.idCard" placeholder="请输入身份证号码" size="mini"
                          style="width: 170px" prefix-icon="el-icon-edit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="婚姻状况:" prop="wedlock">
                <el-radio-group size="mini" v-model="emp.wedlock">
                  <el-radio label="已婚">已婚</el-radio>
                  <el-radio label="未婚">未婚</el-radio>
                  <el-radio label="离异">离异</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="8">
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
            <el-button size="mini" @click="quitView">取 消</el-button>
            <el-button type="primary" size="mini" @click="doAddEmp">确 定</el-button>
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
      importDataBtnText:'导入数据',
      importDataBtnIcon:'el-icon-upload2',
      importDataDisabled:false,
      headers:{
        Authorization:window.sessionStorage.getItem("tokenStr")
      },
      total: 0,
      emps:[],
      page: 1,
      size: 10,
      loading:false,
      empName:'',
      dialogVisible:false,
      title:'',
      emp:{
        id:null,
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
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notWorkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null
      },
      rules:{
        name:[{required:true, message:'请输入员工姓名', trigger:'blur'}],
        gender: [{required:true, message:'请输入性别', trigger:'blur'}],
        birthday: [{required:true, message:'请选择出生日期', trigger:'blur'}],
        idCard: [{required:true, message:'请输入身份证号码', trigger:'blur'},{pattern:"^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", message:'身份证号码有误', trigger:'blur'}],
        wedlock: [{required:true, message:'请选择婚姻状况', trigger:'blur'}],
        nationId: [{required:true, message:'请选择民族', trigger:'blur'}],
        nativePlace: [{required:true, message:'请输入籍贯', trigger:'blur'}],
        politicId: [{required:true, message:'请选择政治面貌', trigger:'blur'}],
        email: [{required:true, message:'请输入电子邮箱', trigger:'blur'},{type:'email',message:"邮箱格式有误", trigger:'blur'}],
        phone: [{required:true, message:'请输入电话号码', trigger:'blur'}],
        address: [{required:true, message:'请输入联系地址', trigger:'blur'}],
        departmentId: [{required:true, message:'请选择部门', trigger:'blur'}],
        jobLevelId: [{required:true, message:'请选择职称', trigger:'blur'}],
        posId: [{required:true, message:'请选择职位', trigger:'blur'}],
        engageForm: [{required:true, message:'请选择聘用形式', trigger:'blur'}],
        tiptopDegree: [{required:true, message:'请选择学历', trigger:'blur'}],
        specialty: [{required:true, message:'请输入专业', trigger:'blur'}],
        school: [{required:true, message:'请输入学校', trigger:'blur'}],
        beginDate: [{required:true, message:'请选择入职日期', trigger:'blur'}],
        beginContract: [{required:true, message:'请选择合同起始日期', trigger:'blur'}],
        endContract: [{required:true, message:'请选择合同终止日期', trigger:'blur'}],
      },
      nations:[],
      jobLevels:[],
      politicsstatus:[],
      positions:[],
      tiptopDegrees:['博士','硕士','本科','大专','高中','初中','小学','其他'],
      visible:false,
      defaultProps:{
        children: 'children',
        label: 'name'
      },
      allDeps:[],
      inputDepName:'',
      
    }
  },
  mounted() {
    this.initEmps();
    this.initData();
  },
  methods:{
    getMaxWorkID(){
      this.getRequest("/employee/basic/maxWorkID").then(resp=>{
        if(resp){
          this.emp.workID = resp.obj;
        }
      });
    },
    initPositions(){
      this.getRequest("/employee/basic/position").then(resp=>{
        if(resp){
          this.positions = resp;
        }
      });
    },
    initData(){
      if(!window.sessionStorage.getItem("nations")){
        this.getRequest("/employee/basic/nation").then(resp=>{
          if(resp){
            this.nations = resp;
            window.sessionStorage.setItem("nations",JSON.stringify(resp));
          }
        })
      }else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      if(!window.sessionStorage.getItem("jobLevels")){
        this.getRequest("/employee/basic/jobLevel").then(resp=>{
          if(resp){
            this.jobLevels = resp;
            window.sessionStorage.setItem("jobLevels",JSON.stringify(resp));
          }
        })
      }else{
        this.jobLevels = JSON.parse(window.sessionStorage.getItem("jobLevels"));
      }
      if(!window.sessionStorage.getItem("politicsstatus")){
        this.getRequest("/employee/basic/politicsstatus").then(resp=>{
          if(resp){
            this.politicsstatus = resp;
            window.sessionStorage.setItem("politicsstatus",JSON.stringify(resp));
          }
        })
      }else{
        this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"));
      }
      if(!window.sessionStorage.getItem("allDeps")){
        this.getRequest("/employee/basic/department").then(resp=>{
          if(resp){
            this.allDeps = resp;
            window.sessionStorage.setItem("allDeps",JSON.stringify(resp));
          }
        })
      }else{
        this.allDeps = JSON.parse(window.sessionStorage.getItem("allDeps"));
      }
    },
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
      this.title = "添加员工";
      this.getMaxWorkID();
      this.initPositions();
      this.emp = {
            id:null,
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
            workState: '在职',
            workID: '',
            contractTerm: null,
            conversionTime: '',
            notWorkDate: null,
            beginContract: '',
            endContract: '',
            workAge: null
      };
      this.inputDepName = '';
      this.dialogVisible = true;
    },
    showDepView(){
      this.visible = !this.visible;
    },
    handleNodeClick(data){
      this.visible = !this.visible;
      this.emp.departmentId = data.id;
      this.inputDepName = data.name;
    },
    doAddEmp(){
      if(this.emp.id){
        this.$refs['empForm'].validate(valid=>{
          if(valid){
            this.putRequest("/employee/basic/",this.emp).then(resp=>{
              if(resp){
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        })
      }else{
        this.$refs['empForm'].validate(valid=>{
          if(valid){
            this.postRequest("/employee/basic/",this.emp).then(resp=>{
              if(resp){
                this.dialogVisible = false;
                this.initEmps();
              }
            });
          }
        })
      }
    },
    deleteEmp(data){
      this.$confirm('此操作将删除员工['+ data.name +']，是否继续？','提示',{
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(()=>{
        this.deleteRequest("/employee/basic/" + data.id).then(resp=>{
          if(resp){
            this.initEmps();
          }
        });
      }).catch(()=>{
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      });
    },
    showEditEmpView(data){
      this.title = "编辑员工信息";
      this.dialogVisible = true;
      this.initPositions();
      this.emp = data;
      this.inputDepName = data.department.name;
    },
    quitView(){
      this.visible = false;
      this.dialogVisible = false;
    },
    exportData(){
      this.downloadRequest("/employee/basic/export");
    },
    beforeUpload(){
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataBtnText = '正在导入';
      this.importDataDisabled = true;
    },
    onSuccess(){
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
      this.importDataDisabled = false;
      this.initEmps();
    },
    onError(){
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataBtnText = '导入数据';
      this.importDataDisabled = false;
    },
    
  }
}
</script>

<style>

</style>