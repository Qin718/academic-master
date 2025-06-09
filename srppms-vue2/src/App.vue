<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
import {title} from "./settings.js"

export default {
  name: 'App',
  methods: {
    titleTemplate(dynamicTitle) {
      return dynamicTitle ? `${dynamicTitle} - ${title}` : title
    }
  },
  computed: {
    dynamicTitle: {
      get() {
        return this.$store.state.settings.dynamicTitle && this.$store.state.settings.title
      },
    }
  },
  watch: {
    dynamicTitle(value) {
      let dynamicTitle = title;
      if (value) {
        const title = this.$store.state.settings.title
        dynamicTitle = this.titleTemplate(title);
      }
      document.getElementsByTagName("title")[0].innerText = dynamicTitle;
    },
  },
}
</script>

<style lang="less">
</style>
