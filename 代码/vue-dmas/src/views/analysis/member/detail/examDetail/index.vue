<template>
  <div class="app-container">
    <div class="filter-container">
      <el-page-header :content="pageHeatherContent" @back="handleGoBack" />
    </div>
    <el-table v-loading="listLoading" border :data="tableData" fit highlight-current-row>
      <el-table-column prop="memberId" label="学号">
        <template slot-scope="{row}">
          <span>{{ row.memberId }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="memberName" label="姓名">
        <template slot-scope="{row}">
          <span>{{ row.memberName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="courseName" label="课程名称">
        <template slot-scope="{row}">
          <span>{{ row.courseName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="courseType" label="课程类型" sortable>
        <template slot-scope="{row}">
          <span>{{ row.courseType }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="courseProperty" label="课程属性" sortable>
        <template slot-scope="{row}">
          <span>{{ row.courseProperty }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="courseRank" label="学分" sortable>
        <template slot-scope="{row}">
          <span>{{ row.courseRank }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="score" label="成绩" sortable>
        <template slot-scope="{row}">
          <span>{{ row.score }}</span>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import { getExamDetailList } from '@/api/member'
export default {

  data() {
    return {
      listLoading: true,
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
      tableData: [],
      listQuery: {
        memberId: '',
        nowCoderId: '',
        memberName: ''
      },
      showReviewer: false,
      pageHeatherContent: ''
    }
  },
  created() {
    this.indexListQuery = this.$route.params.indexListQuery
    this.listQuery.nowCoderId = this.$route.params.nowCoderId
    this.listQuery.memberId = this.$route.params.memberId
    this.listQuery.memberName = this.$route.params.memberName
    this.pageHeatherContent = this.listQuery.memberId + ' - ' + this.listQuery.memberName + '：' + '学科成绩详情'
    this.getExamDetail()
  },
  methods: {
    getExamDetail() {
      getExamDetailList(this.listQuery).then(response => {
        this.tableData = response.data
        this.listLoading = false
      })
    },
    handleGoBack() {
      this.$router.push({
        name: 'AnalysisMemberNowCoderDetail',
        params: {
          memberId: this.listQuery.memberId,
          memberName: this.listQuery.memberName,
          nowCoderId: this.listQuery.nowCoderId,
          indexListQuery: this.indexListQuery
        }
      })
    }
  }

}

</script>

