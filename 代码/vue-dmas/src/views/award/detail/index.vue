<template>
  <div class="app-container">
    <div class="filter-container">

      <el-input v-model="listQuery.params.contestName" placeholder="比赛名称" style="width: 200px;" class="filter-item" />

      <el-date-picker v-model="listQuery.params.contestTime" type="month" value-format="yyyy-MM" format="yyyy-MM" style="margin-left: 10px;" placeholder="选择日期" class="filter-item" />

      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="handleSearch">
        搜索
      </el-button>

    </div>
    <el-table v-loading="listLoading" :data="tableDataList" border fit highlight-current-row>

      <el-table-column type="expand" label="获奖学生" width="100" @click="showArardMember">

        <template slot-scope="{row}">
          <el-tag>获奖学生</el-tag>
          <el-button v-for="(item,index) in row.awardMembers" :key="index" style="margin-left:10px" type="text" @click="handleDetail(item)">{{ item.memberId }}-{{ item.memberName }}</el-button>
        </template>
      </el-table-column>

      <el-table-column prop="contestName" width="50" label="序号" type="index" />

      <el-table-column prop="contestName" width="450" label="获奖名称">
        <template slot-scope="{row}">
          <span>{{ row.awardName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contestSite" label="获奖类型简写">
        <template slot-scope="{row}">
          <span>{{ row.awardType }}</span>
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

  </div>

</template>

<script>

import { getAwardList } from '@/api/award'

export default {

  data() {
    return {

      listLoading: false,
      tableDataList: [],
      listQuery: {
        current: 1,
        total: 0,
        size: 10,
        userId: '',
        params: {
          contestTime: '',
          contestName: ''
        }
      }
    }
  },
  created() {
    this.listLoading = true
    this.getDate()
  },
  methods: {

    showArardMember() {
      console.log('click')
    },

    handleSearch() {

    },

    getDate() {
      getAwardList().then(response => {
        console.log(response)
        this.tableDataList = response.data
        this.listLoading = false
      })
    },
    handleDetail(item) {
      this.$router.push({
        name: 'AnalysisMemberNowCoderDetail',
        params: {
          nowCoderId: item.nowCoderId,
          memberId: item.memberId,
          memberName: item.memberName,
          indexListQuery: this.listQuery
        }
      })
    }
  }
}
</script>
