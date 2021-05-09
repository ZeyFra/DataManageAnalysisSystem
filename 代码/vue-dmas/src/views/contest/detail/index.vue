<template>
  <div class="app-container">
    <div class="filter-container">
      <el-page-header @back="handleGoBack" />
      <!-- <el-button @click="handleGoBack">返回</el-button> -->
    </div>

    <el-row>
      <div id="myChart" ref="myChart" style="width: 100%; height: 500px" />
    </el-row>
    <el-row>
      <el-table v-loading="listLoading" :data="contestMemberDetailList" border style="width: 100%" fit highlight-current-row>
        <el-table-column v-for="item in contestMemberColumnLsit" :key="item.property" :label="item.propertyName" :property="item.property" align="center" sortable>
          <template slot-scope="scope">
            <span>{{ scope.row[scope.column.property] }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>

import { getContestInfo, getContestMemberColumnList, getContestMemberDetailList } from '@/api/contest'

export default {
  data() {
    return {
      listLoading: false,
      contestMemberDetailList: [],
      contestMemberColumnLsit: []
    }
  },
  created() {
    const contestId = this.$route.params.contestId
    this.getContestInfo(contestId)
  },
  methods: {

    handleGoBack() {
      this.$router.push('/contest/manage')
    },

    getContestInfo(contestId) {
      const params = { contestId }
      getContestInfo(params).then(response => {
        this.contestName = response.data.contestInfo.contestName
        this.setMyEchart(response.data)
        this.listLoading = true
        getContestMemberColumnList(params).then(response => {
          this.contestMemberColumnLsit = response.data
          getContestMemberDetailList(params).then(response => {
            this.contestMemberDetailList = response.data
            this.listLoading = false
          })
        })
      })
    },

    setMyEchart(data) {
      const { contestInfo, problemNameList, problemInfoList, problemPassInfo } = data

      var mainContainer = document.getElementById('myChart')

      var myChart = this.$echarts.init(mainContainer)
      window.onload = function() {}

      var option = {
        title: {
          text: contestInfo.contestName,
          left: 50
        },
        legend: {},
        tooltip: {},
        dataset: [
          {
            dimensions: ['problemName', 'problemPassNum', 'problemSubmitNum'],
            source: problemInfoList
          }
        ],
        xAxis: [
          {
            type: 'category',
            data: problemNameList,
            axisPointer: {
              type: 'shadow'
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            name: '提交/通过数',
            min: 0,
            max: (contestInfo.maxSubmitNum / 10) * 10 + 10,
            splitLine: {
              show: false
            },
            interval: 100,
            axisLabel: {
              formatter: '{value} 次'
            }
          },
          {
            type: 'value',
            name: '我院通过人数',
            splitLine: {
              show: false
            },
            min: 0,
            max: contestInfo.myTeamNum,
            interval: parseInt(contestInfo.myTeamNum / 5),
            axisLabel: {
              formatter: '{value} 人'
            }
          }
        ],

        series: [
          {
            name: '通过数',
            type: 'bar',
            barWidth: 20,
            barCategoryGap: '0%',
            datasetIndex: 0,
            label: {
              show: true, // 开启显示
              position: 'top', // 在上方显示
              color: 'black',
              fontSize: 12
            },
            itemStyle: {
              barBorderColor: 'rgba(0,238,118,1)',
              color: 'rgba(0,238,118,1)'
            }
          },
          {
            name: '提交数',
            type: 'bar',
            barWidth: 20,
            barCategoryGap: '0%',
            datasetIndex: 0,
            label: {
              show: true, // 开启显示
              position: 'top', // 在上方显示
              color: 'black',
              fontSize: 12
            }
          },

          {
            type: 'line', yAxisIndex: 1,
            data: problemPassInfo
          }
        ]
      }

      myChart.setOption(option)
    }
  }
}
</script>
