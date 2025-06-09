<template>
	<div style="width: 100%; height:400px; padding-right: 2px">
    <div id="lineChart" style="width: 100%; height: 100%;"/>
  </div>
</template>
<script>
export default {
  name: "lineChart",
  data() {
    return {};
  },
  methods: {
	  getDataList() {
		  this.$http.post("/project/number").then((res) => {
			  this.projectNumber(res);
		  });
	  },
	  //今年发表的项目数量列表
	  projectNumber(res) {
      // 基于准备好的dom，初始化echarts实例
      const lineChart = this.$echarts.init(document.getElementById("lineChart"));
      // 指定相关的配置项和数据
      const Option = {
        title: {
	        text: "今年发表的项目数量列表", //主标题文本
          top: "2%",
          left: "20", //左对齐
          subtext: this.nowTime() + "年度",//副标题
          textStyle: {
            fontSize: 32 //标题字体的大小
          }
        },
        legend: {
          data: ["发表数量"],
          // 图例的类型
          // icon: 'rect',
          top: 22,
          right: 24,
          itemGap: 15,
          itemWidth: 20,
          itemHeight: 10,
          textStyle: {
            // padding: [0, 0, 0, 5],
            color: "rgba(0,0,0,0.87)"
          }
        },
        color: ["#289df5"],
        grid: {
          show: false,
          top: "100",
          bottom: "60",
          right: "60",
          left: "100",
        },
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/> {b} 月份 : {c} ",
        },
        xAxis: {
          type: "category",
          //数据是否从零刻度开始
          boundaryGap: false,
          axisLine: {
            show: true
          },
          //x轴刻度
          axisTick: {
            length: 0
          },
          axisLabel: {
            interval: 0,
            textStyle: {
              color: "#00c5d7"
            }
          },
          name: "(月)",
          nameTextStyle: {
            padding: [24, 0, 0, 0],
            color: '#00c5d7'
          },
          nameGap: 20,
          data: ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"]
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: true
          },
          axisTick: {
            length: 0 // 刻度线的长度
          },
          //y刻度线
          splitLine: {
            show: true,
            lineStyle: {
              color: ["#051d5f"],
              width: 1,
              type: "solid"
            }
          },
          // 左侧y轴样式
          axisLabel: {
            textStyle: {
              color: "#a3a4b2"
            }
          }
        },
        series: [
          {
            name: "发表数量",
            type: "line",
            //光滑
            smooth: true,
            symbol: "circle", // 拐点类型
            symbolSize: 15, // 拐点圆的大小
            animation: true, //false: hover圆点不缩放 .true:hover圆点默认缩放
            itemStyle: {
              normal: {
                color: "#5BB8F9", // 折线条的颜色
                borderColor: "#a9dbff", // 拐点边框颜色
                borderWidth: 5, //拐点边框大小
                areaStyle: {
                  type: "value",
                  //折线下面的渐变色
                  color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {offset: 0, color: "#5BB8F9"},
                    {offset: 1, color: "#FFFFFF"}
                  ])
                }
              }
            },
	          data: res
          }
        ]
      };
      // 使用制定的配置项和数据显示图表
		  if (lineChart) {
			  lineChart.setOption(Option);
		  }
    },
    nowTime() {
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
      };
      return date.year;
    },
  },
  mounted() {
	  this.getDataList();
  },
}
</script>
<style lang="less" scoped>
</style>
