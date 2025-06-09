import store from '@/store'

const {body} = document
const WIDTH = 992 // refer to Bootstrap's responsive design

export default {
  watch: {
    $route() {
      this.$_checkCollapse()
    }
  },
  beforeMount() {
    window.addEventListener('resize', this.$_resizeHandler)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.$_resizeHandler)
  },
  mounted() {
    const isMobile = this.$_isMobile()
    if (isMobile) {
      store.dispatch('app/toggleDevice', 'mobile')
    }
    this.$_checkCollapse()
  },
  methods: {
    // use $_ for mixins properties
    // https://vuejs.org/v2/style-guide/index.html#Private-property-names-essential
    $_isMobile() {
      const rect = body.getBoundingClientRect()
      return rect.width - 1 < WIDTH
    },
    $_resizeHandler() {
      if (!document.hidden) {
        const isMobile = this.$_isMobile()
        if (isMobile) {
          store.dispatch('app/toggleDevice', isMobile ? 'mobile' : 'desktop')
        }
      }
    },
    $_checkCollapse() {
      const device = this.$store.state.app.device
      const isCollapse = this.$store.state.app.isCollapse
      if (device === 'mobile' && !isCollapse) {
        //关闭侧边栏菜单
        store.dispatch('app/changeCollapse')
        //关闭顶部导航
        this.$store.dispatch('settings/changeSetting', {key: 'topNav',value: false})
      }
    }
  }
}
