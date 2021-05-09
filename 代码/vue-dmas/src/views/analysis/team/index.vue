<template>
  <div>
    <div
      v-loading="fullscreenLoading"

      style="padding: 0; margin-left: 5px;margin-top:100px;"
      element-loading-text="拼命加载中"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0)"
    />
    <div class="app-container">
      <el-row style="margin-top:15px">
        <el-col :span="24" style="font-size:30px;text-align: center;">
          <div v-if="fullscreenLoading === false">
            <span>团队学科成绩情况</span>
          </div>

        </el-col>
      </el-row>
      <el-row style="margin-top:30px">
        <el-col :span="8">
          <div id="general-course-score" ref="general-course-score" style="height: 350px" />
        </el-col>
        <el-col :span="8">
          <div id="basic-course-score" ref="basic-course-score" style="height: 350px" />
        </el-col>
        <el-col :span="8">
          <div id="professional-course-score" ref="professional-course-score" style="height: 350px" />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" style="font-size:30px;text-align: center;">
          <div v-if="fullscreenLoading == false">
            <span>团队获奖情况(共：{{ totalPrizes }}项)</span>
          </div>
        </el-col>
      </el-row>
      <el-row style="margin-top:30px">
        <el-col :span="8">
          <div id="prize-year-chart" ref="prize-year-chart" style="height: 350px;" />
        </el-col>
        <el-col :span="9">
          <div id="prizes-chart" ref="prize-total-chart" style="height: 350px;" />
        </el-col>
        <el-col :span="7">
          <div id="prizes-detail-chart" ref="prize-detail-chart" style="height: 350px;" />
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { getAllRecordYearDistribution, getAllRecordDistribution, getAwardList } from '@/api/award'

export default {
  data() {
    return {
      fullscreenLoading: false,
      generalCourseScores: [],
      basicCourseScores: [],
      professionalCourseScores: [],
      totalPrizes: 0,
      singlePrizeNum: 0,
      teamPrizeNum: 0
    }
  },
  created() {
    this.fullscreenLoading = true
  },
  mounted() {
    this.getData()
  },
  methods: {

    setCourseScoreEchart(name, id, CourseScores, max) {
      var mainContainer = document.getElementById(id)

      var myChart = this.$echarts.init(mainContainer)

      var option = {
        title: {
          text: name + '类学科成绩分布情况',
          left: 'center'
        },
        xAxis: {
          type: 'category',
          data: ['0-60', '60-70', '70-80', '80-90', '90-100']
        },
        yAxis: [
          {
            type: 'value',
            name: '学科门数',
            min: 0,
            max: max,
            splitLine: {
              show: true
            },
            interval: 50,
            axisLabel: {
              formatter: '{value}门'
            }
          }
        ],
        series: [
          {
            data: CourseScores,
            type: 'bar', yAxisIndex: 0,
            showBackground: true,
            color: 'rgba(135, 206 ,250, 1)',
            label: {
              show: true,
              position: 'top'
            },
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          },
          {
            data: CourseScores,
            type: 'line',
            yAxisIndex: 0
          }
        ]
      }
      myChart.setOption(option)
    },
    setAwardYearDistributionChart(data) {
      var chartDom = document.getElementById('prize-year-chart')
      var myChart = this.$echarts.init(chartDom)
      var option = {
        title: {
          text: '历年获奖',
          left: 'center'
        },
        dataset: {
          dimensions: ['year', 'num'],
          source: data
        },
        xAxis: {
          name: '年份',
          type: 'category'

        },
        yAxis: {
          type: 'value',
          name: '获奖数',
          min: 0,
          max: 100,
          splitLine: {
            show: true
          },
          interval: 20,
          axisLabel: {
            formatter: '{value} 项'
          }
        },
        series: [
          {
            type: 'bar', yAxisIndex: 0,
            showBackground: true,
            color: '#fedcbd',
            label: {
              show: true,
              position: 'top'
            },
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)'
            }
          },
          {
            type: 'line',
            color: '#f15a22',
            yAxisIndex: 0
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
            radius: '40%',
            center: ['50%', '50%'],
            selectedMode: 'single',
            color: ['#f15a22', '#1E90FF'],
            data: [
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
              },
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
    setPrizesDetailChart() {
      var mainContainer = document.getElementById('prizes-detail-chart')
      var myChart = this.$echarts.init(mainContainer)
      var option = {
        title: {
          text: '奖项',
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
            name: '获奖数量',
            type: 'pie',
            avoidLabelOverlap: false,
            radius: '45%',
            top: '20',
            itemStyle: {
              borderRadius: 5,
              borderColor: '#fff',
              borderWidth: 1
            },
            color: ['#fdb933', '#4EEE94'],
            legend: {
              bottom: 10,
              left: 'center',
              data: ['个人竞赛奖', '团体竞赛奖']
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
            labelLine: {
              show: true,
              showAbove: true,
              minTurnAngle: 60
            },
            data: [
              { value: this.teamPrizeNum, name: '团体竞赛奖' },
              { value: this.singlePrizeNum, name: '个人竞赛奖' }

            ]
          }
        ]
      }
      myChart.setOption(option)
    },
    getData() {
      axios({
        baseURL: process.env.VUE_APP_BASE_API,
        url: 'dmas/api/member/getMemeberAllExamDetailForAnalysis',
        method: 'get',
        headers: { 'token': this.$store.state.user.token }
      }).then(response => {
        this.generalCourseScores = response.data.generalCourseScores
        this.basicCourseScores = response.data.basicCourseScores
        this.professionalCourseScores = response.data.professionalCourseScores

        getAwardList().then(response => {
          var awardRecordList = response.data
          this.totalPrizes = awardRecordList.length
          var pattern = new RegExp('CCPC|ICPC|CCCC|JXCPC|江西省大学生科技创新与职业技能竞赛-程序设计竞赛')
          for (var i = 0, len = awardRecordList.length; i < len; i++) {
            if (pattern.test(awardRecordList[i].awardType)) {
              this.teamPrizeNum++
            } else {
              this.singlePrizeNum++
            }
          }
          this.setPrizesDetailChart()
        })
        getAllRecordYearDistribution().then(response => {
          this.setAwardYearDistributionChart(response.data)
        })
        getAllRecordDistribution().then(response => {
          this.setPrizeChart(response.data)
        })
        this.setCourseScoreEchart('通识', 'general-course-score', this.generalCourseScores, response.data.max)
        this.setCourseScoreEchart('基础', 'basic-course-score', this.basicCourseScores, response.data.max)
        this.setCourseScoreEchart('专业', 'professional-course-score', this.professionalCourseScores, response.data.max)

        this.fullscreenLoading = false
      })
      // this.generalCourseScores = [2, 42, 140, 312, 168]
      // this.basicCourseScores = [2, 42, 140, 312, 168]
      // this.professionalCourseScores = [1, 23, 72, 151, 41]
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
