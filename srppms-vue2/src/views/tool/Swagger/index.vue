<template>
  <div style="width: 100%;height: 100%">
    <iframe :src="src" style="width: 100%;height: 100%"></iframe>

  </div>
</template>
<script>
export default {
  name: "swagger",
  data() {
    return {
      src: ""
    }
  },
  methods: {
    getUrl() {
      this.$http.post("/dict/getDictVoList/swagger").then(res => {
        // window.location.href = res[0]["dict"]
        this.src = res[0]["dict"] + `${window.location.search}`
      })
    }
  },
  mounted() {
	  this.getUrl();
  }
}
</script>
<style lang="less" scoped>

</style>
