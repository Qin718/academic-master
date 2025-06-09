<template>
  <div class="test" style="width: 100%; margin-right: 20px; height:  400px;; float: right">
    <div id="DynamicSortLineChart" style="width: 100%; height: 100%;"></div>
  </div>
</template>

<script>
export default {
  name: "DynamicSortLineChart",
  data() {
    return {};
  },
  methods: {
    //各种类科研项目的发表-动态排序折线图
    getDataList() {
      this.$http.post("/count/project/DynamicSortLineChart").then((res) => {
        this.DynamicSortLineChart(res)
      });
    },
    DynamicSortLineChart: function (res) {
      const types = res.type;
      const datasetWithFilters = [];
      const seriesList = [];
      const lineChart = this.$echarts.init(document.getElementById("DynamicSortLineChart"));
      this.$echarts.util.each(types, function (type) {
        const datasetId = type;
        datasetWithFilters.push({
          id: datasetId,
          fromDatasetId: 'id',
          transform: {
            type: 'filter',
            config: {
              and: [
                {dimension: 'year', gte: res.year},
                {dimension: 'type', '=': type}
              ]
            }
          }
        });
        seriesList.push({
          type: 'line',
          datasetId: datasetId,
          showSymbol: false,
          name: type,
          endLabel: {
            show: true,
            formatter: function (params) {
              return params.value[0] + ': ' + params.value[2];
            }
          },
          labelLayout: {
            moveOverlap: 'shiftY'
          },
          emphasis: {
            focus: 'series'
          },
          encode: {
            x: 'year',
            y: 'num',
            label: ['type', 'num'],
            itemName: 'year',
            tooltip: ['num']
          }
        });
      });
      const option = {
        animationDuration: 10000,
        dataset: [
          {
            id: 'id',
            source: res.data
          },
          ...datasetWithFilters
        ],
        title: {
          text: '各种类科研项目的发表-动态排序折线图'
        },
        tooltip: {
          order: 'valueDesc',
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          nameLocation: 'middle'
        },
        yAxis: {
          name: '数量'
        },
        grid: {
          right: 140
        },
        series: seriesList
      };
	    if (lineChart) {
		    lineChart.setOption(option);
	    }
    }
  },
  mounted() {
    this.getDataList();
  },
};
</script>
