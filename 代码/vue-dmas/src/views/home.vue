<template>

  <div>
    <el-row style="margin-top: 50px">
      <el-col :span="12">
        <div id="members-chart" ref="members-chart" style="height: 510px;margin-left: 50px;" />
      </el-col>
      <el-col :span="12">
        <div id="prizes-chart" ref="prize-chart" style="height: 510px;margin-left: -50px;" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAllMemeberDistribution } from '@/api/member'
import { getAllRecordDistribution } from '@/api/award'

export default {
  data() {
    return {
    }
  },
  created() {

  },
  mounted() {
    // this.setMemberChart()
    this.getData()
  },
  methods: {
    setMemberChart(MemeberDistribution) {
      console.log(MemeberDistribution)
      var chartDom = document.getElementById('members-chart')
      var myChart = this.$echarts.init(chartDom)
      var option

      option = {
        title: {
          text: '队内成员分布情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          bottom: 10,
          left: 'center',
          formatter: '{name} 级'
        },
        series: [
          {
            name: '队内成员分布情况',
            type: 'pie',
            radius: '60%',
            center: ['50%', '50%'],
            data: MemeberDistribution,
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
      myChart.setOption(option)
    },
    setPrizeChart(data) {
      var chartDom = document.getElementById('prizes-chart')
      var myChart = this.$echarts.init(chartDom)
      var nationalLevel = data.nationalLevel1 + data.nationalLevel2 + data.nationalLevel3
      var provincialLevel = data.provincialLevel1 + data.provincialLevel2 + data.provincialLevel3
      var option = {
        title: {
          text: '奖项细况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c}项 ({d}%)'
        },

        legend: {
          bottom: 10,
          left: 'center'
        },
        series: [
          {
            type: 'pie',
            radius: '60%',
            center: ['50%', '50%'],
            selectedMode: 'single',
            color: ['#1E90FF', '#f15a22'],
            data: [

              {
                value: provincialLevel,
                name: '省级奖项',
                label: {
                  formatter: [
                    '{title|{b}}{abg|}',
                    '  {LevelHead|获奖等级}{PrizeNumHead|奖项数}{RateHead|占比}',
                    '{hr|}',
                    '  {level|一等奖}{PrizeNum|' + data.provincialLevel1 + '}{rate|' + (data.provincialLevel1 * 100 / provincialLevel).toFixed(2) + '% }',
                    '  {level|二等奖}{PrizeNum|' + data.provincialLevel2 + '}{rate|' + (data.provincialLevel2 * 100 / provincialLevel).toFixed(2) + '% }',
                    '  {level|三等奖}{PrizeNum|' + data.provincialLevel3 + '}{rate|' + (data.provincialLevel3 * 100 / provincialLevel).toFixed(2) + '% }'
                  ].join('\n'),
                  backgroundColor: '#eee',
                  borderColor: '#777',
                  borderWidth: 1,
                  borderRadius: 4,
                  rich: {
                    title: {
                      color: '#eee',
                      align: 'center',
                      fontSize: '10px'
                    },
                    abg: {
                      backgroundColor: '#333',
                      width: '100%',
                      align: 'right',
                      height: 25,
                      borderRadius: [4, 4, 0, 0]
                    },
                    level: {
                      height: 30,
                      align: 'left',
                      fontSize: '10px',
                      padding: [0, 15, 0, 0]
                    },
                    LevelHead: {
                      color: '#333',
                      height: 24,
                      align: 'left',
                      fontSize: '10px'
                    },
                    PrizeNum: {
                      width: 20,
                      padding: [0, 0, 0, 0],
                      align: 'center',
                      fontSize: '10px'
                    },
                    PrizeNumHead: {
                      color: '#333',
                      width: 20,
                      padding: [0, 0, 0, 10],
                      align: 'center',
                      fontSize: '10px'
                    },
                    rate: {
                      width: 40,
                      align: 'right',
                      padding: [0, 0, 0, 0],
                      fontSize: '10px'
                    },
                    RateHead: {
                      color: '#333',
                      width: 40,
                      align: 'center',
                      padding: [0, 0, 0, 0],
                      fontSize: '10px'
                    },
                    hr: {
                      borderColor: '#777',
                      width: '100%',
                      borderWidth: 0.5,
                      height: 0
                    }
                  }
                }
              },
              {
                value: nationalLevel,
                name: '国家级奖项',
                label: {
                  formatter: [
                    '{title|{b}}{abg|}',
                    '  {LevelHead|获奖等级}{PrizeNumHead|奖项数}{RateHead|占比}',
                    '{hr|}',
                    '  {level|一等奖}{PrizeNum|' + data.nationalLevel1 + '}{rate|' + (data.nationalLevel1 * 100 / nationalLevel).toFixed(2) + '% }',
                    '  {level|二等奖}{PrizeNum|' + data.nationalLevel2 + '}{rate|' + (data.nationalLevel2 * 100 / nationalLevel).toFixed(2) + '% }',
                    '  {level|三等奖}{PrizeNum|' + data.nationalLevel3 + '}{rate|' + (data.nationalLevel3 * 100 / nationalLevel).toFixed(2) + '% }'
                  ].join('\n'),
                  backgroundColor: '#eee',
                  borderColor: '#777',
                  borderWidth: 1,
                  borderRadius: 4,
                  rich: {
                    title: {
                      color: '#eee',
                      align: 'center',
                      fontSize: '10px'
                    },
                    abg: {
                      backgroundColor: '#333',
                      width: '100%',
                      align: 'right',
                      height: 25,
                      borderRadius: [4, 4, 0, 0]
                    },
                    level: {
                      height: 30,
                      align: 'left',
                      fontSize: '10px'
                    },
                    LevelHead: {
                      color: '#333',
                      height: 24,
                      align: 'left',
                      fontSize: '10px'
                    },
                    PrizeNum: {
                      width: 20,
                      padding: [0, 0, 0, 0],
                      align: 'center',
                      fontSize: '10px'
                    },
                    PrizeNumHead: {
                      color: '#333',
                      width: 20,
                      padding: [0, 0, 0, 10],
                      align: 'center',
                      fontSize: '10px'
                    },
                    rate: {
                      width: 40,
                      align: 'right',
                      padding: [0, 0, 0, 0],
                      fontSize: '10px'
                    },
                    RateHead: {
                      color: '#333',
                      width: 40,
                      align: 'center',
                      padding: [0, 0, 0, 0],
                      fontSize: '10px'
                    },
                    hr: {
                      borderColor: '#777',
                      width: '100%',
                      borderWidth: 0.5,
                      height: 0
                    }
                  }
                }
              }
            ],
            labelLine: {
              show: true,
              showAbove: true,
              minTurnAngle: 60
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
      myChart.setOption(option)
    },
    getData() {
      getAllMemeberDistribution().then(response => {
        getAllRecordDistribution().then(response => {
          this.setPrizeChart(response.data)
        })
        this.setMemberChart(response.data)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
