<template>
  <div>
    <el-card class="box-card update_bulletin">
      <div slot="header" class="update_title">
        <span>系统公告</span>
      </div>
      <el-collapse accordion>
        <div v-for="item in dateList" :key="item.id">
          <el-collapse-item :title="item.title">
            <ul v-for="content in item.contents" :key="content">
              <li>{{content}}</li>
            </ul>
          </el-collapse-item>
        </div>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "notice",
	data() {
		return {
			dateList: []
		};
	},
  methods: {
    getUpdate() {
      this.$http.post("/update/getList").then((res) => {
        this.dateList = res;
      });
    },
  },
  mounted() {
    this.getUpdate();
  },
};
</script>

<style lang="less" scoped>
.update_bulletin {
  .update_title {
    font-size: 22px;
    color: #000000;
  }
}
</style>
