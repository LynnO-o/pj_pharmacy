<template>
  <div>
    <div style="margin-bottom: 10px">
      <div style="display: flex; align-items: center; grid-gap: 10px">
        <div class="card" style="flex: 1; padding: 20px; display: flex; align-items: center; grid-gap: 20px">
          <img style="width: 80px; height: 80px" src="@/assets/imgs/1.png" alt="">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 10px">入驻药店</div>
            <div style="font-weight: bold">{{ data.count.drugShop || 0 }}</div>
          </div>
        </div>
        <div class="card" style="flex: 1; padding: 20px; display: flex; align-items: center; grid-gap: 20px">
          <img style="width: 80px; height: 80px" src="@/assets/imgs/2.png" alt="">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 10px">售卖药品</div>
            <div style="font-weight: bold">{{ data.count.drug || 0  }}</div>
          </div>
        </div>
        <div class="card" style="flex: 1; padding: 20px; display: flex; align-items: center; grid-gap: 20px">
          <img style="width: 80px; height: 80px" src="@/assets/imgs/3.png" alt="">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 10px">医药帖子</div>
            <div style="font-weight: bold">{{ data.count.forum || 0  }}</div>
          </div>
        </div>
        <div class="card" style="flex: 1; padding: 20px; display: flex; align-items: center; grid-gap: 20px">
          <img style="width: 80px; height: 80px" src="@/assets/imgs/4.png" alt="">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 10px">药品订单</div>
            <div style="font-weight: bold">{{ data.count.drugOrders || 0  }}</div>
          </div>
        </div>
        <div class="card" style="flex: 1; padding: 20px; display: flex; align-items: center; grid-gap: 20px">
          <img style="width: 80px; height: 80px" src="@/assets/imgs/5.png" alt="">
          <div style="flex: 1; font-size: 20px">
            <div style="margin-bottom: 10px">医用订单</div>
            <div style="font-weight: bold">{{ data.count.goodsOrders || 0  }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="card" style="padding: 20px; margin-bottom: 10px">
      <div style="height: 400px" id="line"></div>
    </div>

    <div style="margin-bottom: 10px; display: flex; align-items: center; grid-gap: 10px">
      <div class="card" style="flex: 1; padding: 20px; height: 400px" id="pie"></div>
      <div class="card" style="flex: 1; padding: 20px; height: 400px" id="bar"></div>
    </div>

  </div>
</template>

<script setup>

import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts'

const lineOption = {
  title: {
    text: '近一周药品订单销售额的趋势图',
    subtext: '统计维度：每日药品订单销售额',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true,
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
  ]
}

const pieOption = {
  title: {
    text: '不同类型的药品数量分布图',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      type: 'pie',
      center: ['50%', '60%'],
      radius: '60%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

const barOption = {
  title: {
    text: '不同药店的订单销售额统计',
    subtext: '柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  grid: {
    left: '15%',
    bottom:'10%'
  },
  xAxis: {
    type: 'category',
    data: [],
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar',
      smooth: true,
      itemStyle: {
        normal: {
          color: function(params) { // 根据索引返回对应的颜色
            let colorList = ['#ffee00','#32C5E9','#ff6868','#08b448','#FFDB5C','#ff9f7f','#fb7293','#E062AE','#E690D1','#e7bcf3']
            return colorList[params.dataIndex];
          }
        }
      },
    }
  ]
}

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  count: {}
})

// 等页面所有元素加载完成后再设置 echarts图表
onMounted(() => {
  // 请求数据  初始化图表
  // 折线图
  let lineDom = document.getElementById('line');
  let lineChart = echarts.init(lineDom);
  request.get('/line').then(res => {
    lineOption.xAxis.data = res.data.dateStrList
    lineOption.series[0].data = res.data.totalList
    lineChart.setOption(lineOption)
  })

  // 饼图
  let pieDom = document.getElementById('pie');
  let pieChart = echarts.init(pieDom);
  request.get('/pie').then(res => {
    pieOption.series[0].data = res.data
    pieChart.setOption(pieOption)
  })

  // 柱状图
  let barDom = document.getElementById('bar');
  let barChart = echarts.init(barDom);
  request.get('/bar').then(res => {
    barOption.xAxis.data = res.data.drugShopNameList
    barOption.series[0].data = res.data.totalList
    barChart.setOption(barOption)
  })
})

request.get('/count').then(res => {
  data.count = res.data
})
</script>