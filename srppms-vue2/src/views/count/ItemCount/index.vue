<template>
  <div style="width: 100%">
    <!-- 去年 -->
    <el-card>
      <el-row :gutter="20">
        <!-- 去年新增项目 -->
        <el-col :span="4">
          <div>
            <el-statistic
              :title="title1"
              :value="value1_1"
              decimal-separator="."
              group-separator=","
            >
            </el-statistic>
          </div>
        </el-col>
        <!-- 去年成功项目 -->
        <el-col :span="4">
          <div>
            <el-statistic :title="title1_2" :value="value1_2"></el-statistic>
          </div>
        </el-col>
        <!-- 去年申请资金 -->
        <el-col :span="5">
          <div>
            <el-statistic
              :precision="2"
              :title="title1_3"
              :value="value1_3"
              group-separator=","
            >
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <!-- 去年最受欢迎的项目 -->
        <el-col :span="6">
          <div>
            <el-statistic :title="title1_4" :value="value1_4">
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="formatter">
                {{ this.value1_4 }}
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <!-- 获赞数量 -->
        <el-col :span="3">
          <div>
            <el-statistic :title="title1_5" :value="value1_5">
              <template slot="suffix">
                <span class="like" @click="like = !like">
                  <i
                    v-show="!!like"
                    class="el-icon-star-on"
                    style="color: red"
                  ></i>
                  <i v-show="!like" class="el-icon-star-off"></i>
                </span>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 今年 -->
    <el-card style="margin-top: 10px;">
      <el-row :gutter="20">
        <!-- 今年新增项目 -->
        <el-col :span="4">
          <div>
            <el-statistic
              :title="title2"
              :value="value2_1"
              decimal-separator="."
              group-separator=","
            >
            </el-statistic>
          </div>
        </el-col>
        <!-- 今年成功项目 -->
        <el-col :span="4">
          <div>
            <el-statistic :title="title2_2" :value="value2_2"></el-statistic>
          </div>
        </el-col>
        <!-- 今年申请资金 -->
        <el-col :span="5">
          <div>
            <el-statistic
              :precision="2"
              :title="title2_3"
              :value="value2_3"
              group-separator=","
            >
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <!-- 今年最受欢迎的项目 -->
        <el-col :span="6">
          <div>
            <el-statistic :title="title2_4" :value="value2_4">
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="formatter">
                {{ this.value2_4 }}
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <!-- 获赞数量 -->
        <el-col :span="3">
          <div>
            <el-statistic :title="title2_5" :value="value2_5">
              <template slot="suffix">
                <span class="like2" @click="like2 = !like2">
                  <i
                    v-show="!!like2"
                    class="el-icon-star-on"
                    style="color: red"
                  ></i>
                  <i v-show="!like2" class="el-icon-star-off"></i>
                </span>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- ____________________________________________________________ -->
    <!-- 图表 -->
    <el-card style="margin-top: 10px;">
      <!-- 饼状图-->
      <div style="width: 1500px; margin-left: 10px; height: 450px">
        <el-card style="width:45%; height:100%; float: left">
          <pieChartLeft/>
        </el-card>
        <el-card style="width:45%; padding-left: 30px; height:100%;">
          <pieChartRight/>
        </el-card>
      </div>
    </el-card>
    <!-- 折线图-->
    <el-card style="margin-top: 10px; float: left">
      <div style="width: 1380px;">
        <lineChart/>
      </div>
    </el-card>
  </div>
</template>
<script>
import pieChartLeft from '@/components/charts/ItemCount/pieChartLeft';
import pieChartRight from '@/components/charts/ItemCount/pieChartRight';
import lineChart from '@/components/charts/ItemCount/lineChart';

export default {
  components: {pieChartLeft, pieChartRight, lineChart},
  name: "ItemCount",
  data() {
    return {
      like: true,
      like2: true,
      //去年
      value1_1: 0,
      value1_2: 0,
      value1_3: 0,
      value1_4: "",
      value1_5: 0,
      title1: "去年新增项目",
      title1_2: "去年成功项目",
      title1_3: "去年申请资金",
      title1_4: "去年最受欢迎的项目",
      title1_5: "获赞数量",

      value2_1: 0,
      value2_2: 0,
      value2_3: 0,
      value2_4: "",
      value2_5: 0,
      title2: "今年新增项目",
      title2_2: "今年成功项目",
      title2_3: "今年申请资金",
      title2_4: "今年最受欢迎的项目",
      title2_5: "获赞数量",
      title: {
        text: "今年项目图片",
      },
    };
  },
  methods: {
    //今年
    getThisYear() {
      this.$http.post("/project/getList/thisYear").then((res) => {
	      if (res !== null) {
		      this.value2_1 = res["newAdd"];
		      this.value2_2 = res["success"];
		      this.value2_3 = res["money"];
		      this.value2_4 = res["itemName"];
		      this.value2_5 = res["scoreNumber"];
	      }
      });
    },
    //去年
    getLastYear() {
      this.$http.post("/project/getList/lastYear").then((res) => {
	      if (res !== null) {
		      this.value1_1 = res["newAdd"];
		      this.value1_2 = res["success"];
		      this.value1_3 = res["money"];
		      this.value1_4 = res["itemName"];
		      this.value1_5 = res["scoreNumber"];
	      }
      });
    },
  },
  mounted() {
    this.getThisYear();
    this.getLastYear();
  },
};
</script>
<style lang="less">
.like {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}

.like2 {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}

.container {
  width: 500px;
  height: 300px;
}

//.verticalBar {
//  width: 1px;
//  height: 400px;
//  background: blue;
//  display: inline-block;
//  vertical-align: top;
//}
</style>
