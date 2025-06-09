<template>
  <div class="footer">
    <el-row :gutter="10">
      <el-col :span="8">
        <div>
          <el-button
	          v-for="{name, url} in buttons"
	          :key="name"
	          @click="ToUrl(url)"
          >
	          {{name}}
          </el-button>
        </div>
      </el-col>
      <el-col :span="8">
        <div>
	        <span>Copyright © 2025 NUDT All Rights Reserved.</span>
        </div>
      </el-col>
      <el-col :span="8">
        <div>
           <span>
          科研项目管理系统 秦睿哲 哈林鹏 梁登辉&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </span>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

export default {
  name: "Foot",
  data() {
    return {
	    year: new Date().getFullYear(),
    };
  },
  methods: {
    ToUrl(url) {
      window.open(url)
    }
  }
};
</script>

<style>
.footer {
  line-height: 40px;
  text-align: center;
  font-size: 12px;
  letter-spacing: 1px;
  margin-top: 2px;
  color: #2b2f3a;
  background-image: linear-gradient(
    to right,
    #04af9212,
    #fb9b0c1b,
    #7603cd1d,
    #f83c5824,
    #478ee023
  );
  border-top: solid #ffd436 2px;
}
</style>
