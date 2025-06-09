<template>
  <el-card style="margin-top: 10px;">
    <!--1-->
    <el-card style="width:100%;  height:100%; float: left;margin-top: 10px">
      <DynamicSortLineChart/>
    </el-card>
    <!--2-->
    <el-card style="width:100%;  height:100%; float: left;margin-top: 10px">
      <StackLineChart/>
    </el-card>
    <!--3-->
    <el-card style="width:100%;  height:100%; float: left;margin-top: 10px">
      <UserInsertChart/>
    </el-card>
    <!--4-->
    <el-card style="width:100%;  height:100%; float: left;margin-top: 10px">
      <Contribution/>
    </el-card>
  </el-card>
</template>

<script>
import StackLineChart from '@/components/charts/dataCount/StackLineChart';
import UserInsertChart from '@/components/charts/dataCount/UserInsertChart';
import DynamicSortLineChart from '@/components/charts/dataCount/DynamicSortLineChart';
import Contribution from '@/components/charts/dataCount/Contribution';

export default {
  components: {StackLineChart, UserInsertChart, DynamicSortLineChart, Contribution},
	name: "DataCount"
};
</script>
