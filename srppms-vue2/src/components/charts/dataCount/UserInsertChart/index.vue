<template>
	<div class="test" style="width: 100%; margin-right: 20px; height: 400px; float: right">
    <div id="UserInsertChart" style="width: 100%; height: 100%;"></div>
  </div>
</template>

<script>
export default {
  name: "UserInsertChart",
  data() {
    return {
      series_data: [],
      y_data: [],
    };
  },
  methods: {
	  //用户注册趋势-柱状图
    getDataList() {
      this.$http.post("/count/UserInsert").then((res) => {
        this.UserInsertChart(res)
        this.updateChart(res)
      });
    },
    async updateChart(data) {
      for (let i = 0; i < data.length; i++) {
        const item = data[i]
        const key = item.year
        const value = item.count
        this.y_data.push(key)
        this.series_data.push(0)
        this.series_data[i] += value
        const lineChart = this.$echarts.init(document.getElementById("UserInsertChart"));
	      
	      const option = {
		      series: [
			      {
				      type: 'bar',
				      data: this.series_data
			      }
		      ]
	      };
	      if (lineChart) {
		      lineChart.setOption(option);
	      }
      }

    },
    UserInsertChart(data) {
      data.forEach(item => {
	      this.y_data.push(item.year)
	      this.series_data.push(item.count)
      })
      const lineChart = this.$echarts.init(document.getElementById("UserInsertChart"));
	    const option = {
		    title: {
			    //小标题
			    text: '用户注册趋势-柱状图'
		    },
		    xAxis: {
			    max: 'dataMax'
		    },
		    yAxis: {
			    type: 'category',
			    data: this.y_data,
			    inverse: true,
			    animationDuration: 300,
			    animationDurationUpdate: 300,
			    max: this.y_data.length
		    },
		    series: [
			    {
				    realtimeSort: true,
				    name: '用户注册数量',
				    type: 'bar',
				    data: this.series_data,
				    label: {
					    show: true,
					    position: 'right',
					    valueAnimation: true
				    }
			    }
		    ],
		    legend: {
			    show: true
		    },
		    animationDuration: 0,
		    animationDurationUpdate: 3000,
		    animationEasing: 'linear',
		    animationEasingUpdate: 'linear'
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
