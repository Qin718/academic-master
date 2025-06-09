<template>
  <div class="test" style="width: 100%; margin-right: 20px; height:  400px;; float: right">
    <div id="Contribution" style="width: 100%; height: 100%;"></div>
  </div>
</template>

<script>
export default {
  name: "Contribution",
  data() {
    return {};
  },
  methods: {
	  //项目贡献度
    getDataList() {
      this.$http.post("/count/Contribution").then((res) => {
        this.Contribution(res)
      });
    },
    Contribution: function (res) {
      const chart = this.$echarts.init(document.getElementById('Contribution'))

      const option = {

        //设置背景
        // backgroundColor: '#d0d0d0',

        title: {
          top: 30,
          text: '项目贡献度',
          subtext: '一年内项目提交的数量',
          left: 'center',
          textStyle: {
            color: '#000'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            return (params.data[0] + '<br>贡献度：' + params.data[1])
          }
        },
        legend: {
          top: '30',
          left: '100',
          data: ['贡献度', 'Top 12'],
          textStyle: {
            // 设置字体颜色
            color: '#000'
          }
        },
        calendar: [{
          top: 100,
          left: 'center',
          range: res.date,
          splitLine: {
            show: true,
            lineStyle: {
              // 设置月份分割线的颜色
              color: '#D3D3D3',
              width: 4,
              type: 'solid'
            }
          },
          yearLabel: {show: false},
          dayLabel: {
            nameMap: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"], // 设置中文显示
            textStyle: {
              // 设置周显示颜色
              color: '#ff0000'
            }
          },
          monthLabel: {
            nameMap: 'cn', // 设置中文显示
            textStyle: {
              // 设置月显示颜色
              color: '#000'
            }
          },
          itemStyle: {
            normal: {
              // 设置背景颜色
              color: '#ffffff',
              borderWidth: 1,
              // 设置方块分割线段颜色
              borderColor: '#D3D3D3'
            }
          }
        }],
        series: [
          {
            name: '贡献度',
            type: 'scatter',
            coordinateSystem: 'calendar',
            data: res.data,
            // 根据值设置原点大小
            symbolSize: function (val) {
              return val[1];
            },
            itemStyle: {
              normal: {
                // 设置圆点颜色
                color: '#2ec7c9'
              }
            }
          }
        ]
      };
	    if (chart) {
		    chart.setOption(option);
	    }
    }
  },
  mounted() {
    this.getDataList();
  },
};
</script>
