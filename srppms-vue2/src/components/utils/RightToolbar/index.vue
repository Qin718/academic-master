<template>
  <div class="top-right-btn" :style="style">
    <el-row>
      <el-tooltip v-if="searchButton" :content="showSearch ? '隐藏搜索' : '显示搜索'" class="item" effect="dark"
                  placement="top">
        <el-button size="mini" circle icon="el-icon-search" @click="toggleSearch()" />
      </el-tooltip>
      <el-tooltip class="item" effect="dark" content="刷新" placement="top">
        <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
      </el-tooltip>
      <el-tooltip v-if="columns" class="item" content="显隐列" effect="dark" placement="top">
        <el-dropdown :hide-on-click="false" style="padding-left: 12px" trigger="click">
          <el-button circle icon="el-icon-menu" size="mini"/>
          <el-dropdown-menu slot="dropdown">
            <template v-for="item in columns">
              <el-dropdown-item :key="item.prop">
                <el-checkbox :checked="item.checked" :label="item.label" @change="checkboxChange($event, item.label)"/>
              </el-dropdown-item>
            </template>
          </el-dropdown-menu>
        </el-dropdown>
      </el-tooltip>

    </el-row>
  </div>
</template>
<script>
export default {
  name: "RightToolbar",
  data() {
    return {};
  },
  props: {
    /* 是否显示搜索按钮 */
    searchButton: {
      type: Boolean,
      default: true,
    },
    /* 是否显示检索条件 */
    showSearch: {
      type: Boolean,
      default: true,
    },
    /* 显隐列信息 */
    columns: {
      type: Array,
    },
    /* 是否显示检索图标 */
    search: {
      type: Boolean,
      default: true,
    },
    /* 显隐列类型（transfer穿梭框、checkbox复选框） */
    showColumnsType: {
      type: String,
      default: "checkbox",
    },
    /* 右外边距 */
    gutter: {
      type: Number,
      default: 10,
    },
  },
  computed: {
    style() {
      const ret = {};
      if (this.gutter) {
        ret.marginRight = `${this.gutter / 2}px`;
      }
      return ret;
    }
  },
  created() {
  },
  methods: {
    // 是否显示搜索
    toggleSearch() {
      this.$emit("update:showSearch", !this.showSearch);
    },
    //刷新页面
    refresh() {
      this.$GlobalLoading(true, {text: "正在刷新页面，请稍候..."});
      setTimeout(() => {
        this.$emit("queryTable");
        this.$GlobalLoading(false, null);
      }, 1000);
    },
    // 勾选显隐列
    checkboxChange(event, label) {
      this.columns.filter(item => item.label === label)[0].checked = event;
    }
  },
};
</script>
<style lang="less" scoped>
.top-right-btn{
  position: relative;
  float: right;
}
</style>
