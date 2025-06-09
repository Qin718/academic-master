<template>
  <el-button plain size="mini" type="danger" @click="printTable">
    打印
  </el-button>
</template>


<script>
import Print from 'print-js'

export default {
  props: ["data", "properties"],
  methods: {
    printTable() {
      const props = []
      this.properties.forEach(item => {
        props.push({
          field: item.prop,
          displayName: item.label
        })
      })
      this.data.forEach(item => {
        for (let index in item) {
          if (item[index] === null || item[index] === undefined) {
            item[index] = ''
          }
        }
      })
      Print({
        type: 'json',
        documentTitle: '打印表格',
        printable: this.data,
        properties: props,
        //表内数据
        gridStyle: 'width:auto; white-space: nowrap;text-align: center;border: 1px solid lightgray;font-size: 12px; line-height: 26px;',
        //表头
        gridHeaderStyle: 'font-weight: 500; border: 1px solid lightgray; line-height: 30px;',
      })
    }
  }
}
</script>

<style lang="less" scoped>

</style>
