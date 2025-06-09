<template>
  <div style="width: 100%;height: 100%">
    <iframe :src="src" style="width: 100%;height: 100%"></iframe>
  </div>
</template>
<script>
export default {
	name: "vue",
  data() {
    return {
      src: "https://jakhuang.github.io/form-generator/#/"
    }
  }
}
</script>
<style lang="less" scoped>

</style>
