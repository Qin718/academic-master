<template>
	<div style="width: 100%; margin-right: 20px; height:  400px;; float: right">
		<div id="pieChartRight" style="width: 100%; height: 100%;"></div>
  </div>
</template>
<script>
export default {
  name: "pieChartRight",
  data() {
    return {
      year: 0, //年度
      dataList: [],
	    colorList: [], //颜色列表
    };
  },
  methods: {
	  //获取图表所需要的数据
	  getData() {
		  this.$http.post("/project/getList/kind/lastYear").then((res) => {
			  this.dataList = res;
			  this.dataList.forEach((item) => {
				  this.colorList.push(item.itemStyle);
			  });
			  //渲染图表
			  this.drawLine(this.dataList, this.colorList);
		  });
	  },
	  drawLine(data, colors) {
		  const chart = this.$echarts.init(document.getElementById("pieChartRight"));
		  
		  const option = {
			  title: {
				  text: "科研项目类型", // 主标题
				  subtext: this.year + "年度", // 副标题
				  x: "left", // x轴方向对齐方式
			  },
			  grid: {containLabel: true},
			  tooltip: {
				  trigger: "item",
				  formatter: "{a} <br/>{b} : {d}%",
			  },
			  color: ["red", "#1FC48D", "#6DC8EC", "#3F8FFF"],
			  legend: {
				  orient: "vertical",
				  icon: "circle",
				  align: "left",
				  x: "right",
				  y: "bottom",
			  },
			  series: [
				  {
					  name: "科研类型",
					  type: "pie",
					  radius: ["50%", "70%"],
					  avoidLabelOverlap: false,
					  center: ["40%", "50%"],
					  itemStyle: {
						  emphasis: {
							  shadowBlur: 10,
							  shadowOffsetX: 0,
							  shadowColor: "rgba(0, 0, 0, 0.5)",
						  },
						  color: function (params) {
							  return colors[params.dataIndex];
						  },
					  },
					  data: data,
				  },
			  ],
		  };
		  if (chart) {
			  chart.setOption(option);
		  }
    },
    nowTime() {
      let nowDate = new Date();
      let date = {
        year: nowDate.getFullYear(),
      };
      this.year = date.year - 1
    },
  },
  mounted() {
    this.getData();
    this.nowTime();
  },
};
</script>

<style lang="less">
</style>
