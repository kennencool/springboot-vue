<template>
  <div>
    <el-container>
      <el-header>Header</el-header>
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
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Home",
  methods:{
   
  },
  computed:{
    routes(){
      return this.$store.state.routes;
    }
  }
}
</script>

<style scoped>

</style>