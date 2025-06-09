<template>
  <div ref="rightPanel" class="rightPanel-container">
    <div class="rightPanel-background"/>
    <div class="rightPanel">
      <div class="rightPanel-items">
        <!-- 系统布局设置 -->
        <settings/>
      </div>
    </div>
  </div>
</template>

<script>
import settings from "./settings";

export default {
  name: 'RightPanel',
  components: {settings},
  props: {
    clickNotClose: {
      default: false,
      type: Boolean
    }
  },
  computed: {
    show: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  watch: {
    show(value) {
      if (value && !this.clickNotClose) {
        this.addEventClick()
      }
    }
  },
  mounted() {
	  this.addEventClick();
  },
  beforeDestroy() {
    const elx = this.$refs.rightPanel
    elx.remove()
  },
  methods: {
    addEventClick() {
      window.addEventListener('click', this.closeSidebar)
    },
    closeSidebar(evt) {
      const parent = evt.target.closest('.el-drawer__body')
      if (!parent) {
        this.show = false
        window.removeEventListener('click', this.closeSidebar)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.rightPanel-background {
  position: fixed;
  top: 0;
  left: 0;
  opacity: 0;
  transition: opacity .3s cubic-bezier(.7, .3, .1, 1);
  background: rgba(0, 0, 0, .2);
  z-index: -1;
}

.rightPanel {
  width: 100%;
  max-width: 260px;
  height: 100vh;
  position: fixed;
  top: 0;
  right: 0;
  box-shadow: 0 0 15px 0 rgba(0, 0, 0, .05);
  transition: all .25s cubic-bezier(.7, .3, .1, 1);
  transform: translate(100%);
  background: #fff;
  z-index: 40000;
}
</style>
