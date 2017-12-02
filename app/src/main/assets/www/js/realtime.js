function randomData() {
    now = new Date(+now + oneDay);
    value = Math.random() * 200;
    return {
        time: now, value: value
    }
}

var data = [];
var now = new Date();
var oneDay = 1000;
var value = Math.random() * 200;
//for (var i = 0; i < 1000; i++) {
//    data.push(randomData());
//}
var myChart;
document.addEventListener("DOMContentLoaded", function() {
    initChart('TOCO');
})

function initChart(name) {
     myChart = echarts.init(document.getElementById('realtime'));
      // specify chart configuration item and data
      var option = {
              title: {
                  text: name
              },
              tooltip: {
                  trigger: 'axis',
                  formatter: function (params) {
                      params = params[0];
                      var date = new Date(params.name);
                      var hour = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                      var minute = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                      var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                      return hour + ':' + minute + ':' + seconds + ' - ' + params.value[1];
                  },
                  axisPointer: {
                      animation: false
                  }
              },
              xAxis: {
                  type: 'time',
                  boundaryGap: [0, 1],
                  min: new Date().getTime(),
                  max: new Date().getTime() + 60 * 10 * 1000,
              },
              yAxis: {
                  type: 'value',
                  min: 0,
                  max: 200,
                  splitLine: {
                      show: false
                  }
              },
              series: [{
                  name: 'TOCO',
                  type: 'line',
                  showSymbol: false,
                  hoverAnimation: false,
                  data: data,
                  areaStyle: {
                                  normal: {
                                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                          offset: 0,
                                          color: 'rgb(255, 158, 68)'
                                      }, {
                                          offset: 1,
                                          color: 'rgb(255, 70, 131)'
                                      }])
                                  }
                              }, itemStyle: {
                                 normal: {
                                     color: 'rgb(255, 70, 131)'
                                 }
                              }
              }]
          };

      // use configuration item and data specified to show chart
      myChart.setOption(option);
}

function addData(time, value) {
    console.log(time, value);
    var now = new Date(time);
    var d = {
            name: now.toString(),
            value:
                [ now.getTime(),
                value
            ]
        };
    data.push(d);
     myChart.setOption({
          series: [{
              data: data
          }]
     });
}