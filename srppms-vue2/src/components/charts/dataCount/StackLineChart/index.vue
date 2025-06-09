<template>
	<div style="width: 100%; margin-right: 20px; height:  400px;; float: right">
    <div id="StackLineChart" style="width: 100%; height: 100%;"></div>
  </div>
</template>

<script>
export default {
  name: "StackLineChart",
  data() {
    return {};
  },
  methods: {
    //各种类科研项目的发表-折线图
    getDataList() {
      this.$http.post("/count/project/line").then((res) => {
        this.StackLineChart(res)
      });
    },
    StackLineChart(data) {
      const legend_data = []
      const series_data = []
      for (let i = 0; i < data.data.length; i++) {
        series_data.push(
          {
            name: data.type[i],
            type: 'line',
            stack: 'Total',
            data: data.data[i]
          })
        legend_data.push(data.type[i])
      }
      const lineChart = this.$echarts.init(document.getElementById("StackLineChart"));
	    const option = {
		    title: {
			    //小标题
			    text: '各种类科研项目的发表-折线图'
		    },
		    tooltip: {
			    trigger: 'axis'
		    },
		    legend: {
			    //线段的名称
			    data: legend_data
		    },
		    grid: {
			    left: '3%',
			    right: '4%',
			    bottom: '3%',
			    containLabel: true
		    },
		    toolbox: {
			    feature: {
				    saveAsImage: {}
			    }
		    },
		    xAxis: {
			    type: 'category',
			    boundaryGap: false,
			    data: data.x//横坐标
		    },
		    yAxis: {
			    type: 'value'
		    },
		    //数据集合
		    series: series_data
	    };
	    if (lineChart) {
		    lineChart.setOption(option);
	    }
    },
  },
	mounted() {
		this.getDataList();
  },
};
</script>
