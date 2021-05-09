<template>
  <div
    v-loading="fullscreenLoading"
    class="app-container"
    style="padding: 0;"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0)"
  >
    <div class="filter-container">
      <el-page-header style="margin-top:10px;margin-left:15px" @back="handleGoBack" />
    </div>
    <el-row style="margin-top:10px">
      <el-col :span="24" style="font-size:30px;text-align: center;">
        <span>{{ params.memberId }}-{{ params.memberName }}-数据可视化分析情况</span>
      </el-col>
    </el-row>
    <el-row style="margin-top:50px">
      <el-col :span="8">
        <div id="general-course-score" ref="general-course-score" style="height: 300px" />
      </el-col>
      <el-col :span="8">
        <div id="basic-course-score" ref="basic-course-score" style="height: 300px" />
      </el-col>
      <el-col :span="8">
        <div id="professional-course-score" ref="professional-course-score" style="height: 300px" />
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <div id="nowcoder-trend-chart" ref="nowcoder-trend-chart" style="height: 450px" />
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" style="font-size:25px;text-align:center;margin-top: 30px">
        <span>获奖情况：共{{ totalPrizeNum }}项</span>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="prizes-num-chart" ref="prizes-num-chart" style="height: 300px;margin-left: 50px;" />
      </el-col>
      <el-col :span="12">
        <div id="prizes-detail-chart" ref="prizes-detail-chart" style="height: 300px;margin-left: -50px;" />
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24" style="font-size:25px;text-align:center;">
        <span>获奖详情</span>
      </el-col>
    </el-row>
    <el-row style="margin-top:10px">
      <el-table v-loading="listLoading" :data="awardRecordList" border style="width: 100%" fit highlight-current-row>

        <el-table-column prop="contestName" width="50" label="序号" type="index" />

        <el-table-column prop="awardName" width="600" label="获奖名称">
          <template slot-scope="{row}">
            <span>{{ row.awardName }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="contestTime" label="获奖时间" sortable>
          <template slot-scope="{row}">
            <span>{{ row.awardTime }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="contestTeamNum" width="120" label="级别（国/省）">
          <template slot-scope="{row}">
            <span>{{ row.awardLevel }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="contestAkNum" label="证书获奖等级">
          <template slot-scope="{row}">
            <span>{{ row.certificateLevel }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="contestAkNum" label="学校认定获奖等级">
          <template slot-scope="{row}">
            <span>{{ row.theSchoolRecognizesTheAwardLevel }}</span>
          </template>
        </el-table-column>

      </el-table>
    </el-row>
  </div>
</template>

<script>
import { getMemberAnalysisInfos } from '@/api/analysis'
import { getAwardRecordByMemberId } from '@/api/award'
import { getMemeberAllNowCoderRecordForAnalysis } from '@/api/member'

export default {
  data() {
    return {
      listLoading: false,
      indexListQuery: {
        current: 1,
        total: 0,
        size: 10,
        params: {
          memberId: '',
          nowCoderId: '',
          memberName: ''
        }
      },
      awardRecordList: [],
      fullscreenLoading: false,
      params: {
        memberId: '',
        nowCoderId: '',
        memberName: ''
      },
      totalPrizeNum: 0,
      generalCourseScores: [],
      basicCourseScores: [],
      professionalCourseScores: [],
      nationalPrizeNum: 0,
      provincialPrizeNum: 0,
      singlePrizeNum: 0,
      teamPrizeNum: 0
    }
  },
  created() {
    this.params.nowCoderId = this.$route.params.nowCoderId
    this.params.memberId = this.$route.params.memberId
    this.params.memberName = this.$route.params.memberName
    this.nationalPrizeNum = this.$route.params.nationalPrizeNum
    this.provincialPrizeNum = this.$route.params.provincialPrizeNum
    this.totalPrizeNum = this.nationalPrizeNum + this.provincialPrizeNum
    this.indexListQuery = this.$route.params.indexListQuery
    this.getData()
  },
  mounted() {

  },
  methods: {

    handleGoBack() {
      this.$router.push({
        name: 'AnalysisMember',
        params: {
          listQuery: this.indexListQuery
        }
      })
    },

    setNowCoderTrendChart(nowCoderRecordData) {
      var chartDom = document.getElementById('nowcoder-trend-chart')
      var myChart = this.$echarts.init(chartDom)
      var option = {

        title: {
          left: 'center',
          text: '牛客训练走势(共' + nowCoderRecordData.length + '场)'
        },
        xAxis: {
          type: 'time',
          boundaryGap: false
        },
        yAxis: {
          type: 'value',
          name: '该场比赛排名百分比',
          min: 0,
          max: 100,
          boundaryGap: [0, '100%'],
          interval: 10,
          axisLabel: {
            formatter: '{value} %'
          }
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100
          }, {
            start: 0,
            end: 100 }
        ],
        series: [
          {
            name: '牛客训练走势数据',
            type: 'scatter',

            data: nowCoderRecordData
          }
        ]
      }

      myChart.setOption(option)
    },

    setPrizesNumChart(nationalPrizeNum, provincialPrizeNum) {
      if (nationalPrizeNum + provincialPrizeNum > 0) {
        var mainContainer = document.getElementById('prizes-num-chart')

        var myChart = this.$echarts.init(mainContainer)

        var option = {
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
              radius: '100',
              top: '20',
              color: ['#f15a22', '#1E90FF'],
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: [
                { value: nationalPrizeNum, name: '国家级奖' },
                { value: provincialPrizeNum, name: '省级奖' }
              ]
            }
          ]
        }
        myChart.setOption(option)
      } else {
        var html = '<div><sapn style="margin-left: 10%;font-size: 18px;font-weight: bold;">获奖情况</sapn>' +
        '<span style="position: absolute;top: 40%;color:#868686; font-size: 20px;">暂未获奖！努力奋斗中！</span></div>'
        document.getElementById('prizes-detail-chart').innerHTML = html
        document.getElementById('prizes-detail-chart').removeAttribute('_echarts_instance_')
      }
    },

    setPrizesDetailChart(singlePrizeNum, teamPrizeNum) {
      if (singlePrizeNum + teamPrizeNum > 0) {
        var mainContainer = document.getElementById('prizes-detail-chart')

        var myChart = this.$echarts.init(mainContainer)
        var option = {
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
              color: ['#fdb933', '#4EEE94'],
              radius: '100',
              top: '20',
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '20',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: true
              },
              data: [
                { value: teamPrizeNum, name: '团体竞赛奖' },
                { value: singlePrizeNum, name: '个人竞赛奖' }

              ]
            }
          ]
        }
        myChart.setOption(option)
      } else {
        var html = '<div><sapn style="margin-left: 10%;font-size: 18px;font-weight: bold;">获奖情况</sapn>' +
        '<span style="position: absolute;top: 40%;color:#868686; font-size: 20px;">暂未获奖！努力奋斗中！</span></div>'
        document.getElementById('prizes-detail-chart').innerHTML = html
        document.getElementById('prizes-detail-chart').removeAttribute('_echarts_instance_')
      }
    },

    setCourseScoreEchart(name, id, generalCourseScores) {
      var mainContainer = document.getElementById(id)

      var myChart = this.$echarts.init(mainContainer)

      var option = {
        title: {
          text: name + '类学科成绩分布情况',
          left: 'center'
        },
        xAxis: {
          name: '分布',
          type: 'category',
          data: ['0-60', '60-70', '70-80', '80-90', '90-100']
        },
        yAxis: [
          {
            type: 'value',
            name: '学科门数',
            min: 0,
            max: 15,
            splitLine: {
              show: false
            },
            interval: 5,
            axisLabel: {
              formatter: '{value} 门'
            }
          }
        ],
        series: [
          {
            data: generalCourseScores,
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
          }
        ]
      }
      myChart.setOption(option)
    },

    getData() {
      this.fullscreenLoading = true
      this.listLoading = true
      var queryParams = {
        memberId: this.params.memberId
      }
      getAwardRecordByMemberId(queryParams).then(response => {
        this.awardRecordList = response.data
        var pattern = new RegExp('CCPC|ICPC|CCCC|JXCPC|江西省大学生科技创新与职业技能竞赛-程序设计竞赛')
        for (var i = 0, len = this.awardRecordList.length; i < len; i++) {
          if (pattern.test(this.awardRecordList[i].awardType)) {
            this.teamPrizeNum++
          } else {
            this.singlePrizeNum++
          }
        }
      })
      var nowCoderRecordData = []
      getMemeberAllNowCoderRecordForAnalysis(this.params).then(response => {
        nowCoderRecordData = response.data
      })

      getMemberAnalysisInfos(this.params).then(response => {
        const { generalCourseScores, basicCourseScores, professionalCourseScores } = response.data
        this.generalCourseScores = generalCourseScores
        this.basicCourseScores = basicCourseScores
        this.professionalCourseScores = professionalCourseScores
        this.setCourseScoreEchart('通识', 'general-course-score', this.generalCourseScores)
        this.setCourseScoreEchart('基础', 'basic-course-score', this.basicCourseScores)
        this.setCourseScoreEchart('专业', 'professional-course-score', this.professionalCourseScores)
        this.setPrizesNumChart(this.nationalPrizeNum, this.provincialPrizeNum)
        this.setPrizesDetailChart(this.singlePrizeNum, this.teamPrizeNum)
        this.setNowCoderTrendChart(nowCoderRecordData)
        this.fullscreenLoading = false
        this.listLoading = false
        // CCPC、ICPC、CCCC、江西省大学生科技创新与职业技能竞赛-程序设计竞赛、JXCPC、ICPC-JX
      })
    }
  }
}
</script>
