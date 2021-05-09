<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.params.memberId" placeholder="学号" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />

      <el-select v-model="listQuery.params.isRetire" clearable placeholder="状态" style="margin-left: 10px" class="filter-item">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <el-button class="filter-item" plain style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="handleSearch">
        搜索
      </el-button>
      <el-button class="filter-item" :loading="updateRecordLoading" style="margin-left: 10px;" type="primary" icon="el-icon-refresh" plain @click="handleUpdateAllRecord">
        更新全员牛客训练情况
      </el-button>
    </div>
    <el-table v-loading="listLoading" border :data="tableData" fit highlight-current-row>

      <el-table-column prop="memberId" label="学号" sortable>
        <template slot-scope="{row}">
          <span>{{ row.memberId }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="memberName" label="姓名">
        <template slot-scope="{row}">
          <span>{{ row.memberName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="gender" label="状态">
        <template slot-scope="{row}">
          <span v-if="row.gender=='0'">男</span>
          <span v-if="row.gender=='1'">女</span>
        </template>
      </el-table-column>

      <el-table-column prop="isRetire" label="状态">
        <template slot-scope="{row}">
          <span v-if="row.isRetire=='0'">坚持奋斗</span>
          <span v-if="row.isRetire=='1'">已退役</span>
        </template>
      </el-table-column>

      <el-table-column label="获奖数" sortable>
        <template slot-scope="{row}">
          <span>{{ row.nationalPrizeNum+row.provincialPrizeNum }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="{row}">
          <el-button plain type="primary" size="mini" icon="el-icon-view" @click="handleDetail(row)">查看</el-button>
          <el-button plain type="primary" size="mini" icon="el-icon-s-data" @click="analysis(row)">分析</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top:20px; margin-left:20px" class="pagination">
      <el-pagination
        small
        background
        :current-page="current"
        :page-size="size"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, prev, pager, next, jumper, sizes"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script>

import { pagingMemberAnalysisInfo, queryMemberAnalysisByMemberId } from '@/api/member'

import axios from 'axios'

export default {

  data() {
    const validateMemberId = (rule, value, callback) => {
      if (value.length !== 8) {
        callback(new Error('学号为8位数字！请重新输入！'))
        var reg = RegExp('^[0-9]*$')
        if (!reg.test(value)) {
          callback(new Error('学号为8位数字！请重新输入！'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const validateTelephone = (rule, value, callback) => {
      if (value.length !== 11) {
        callback(new Error('手机号为11位数字！请重新输入！'))
        var reg = RegExp('^[0-9]*$')
        if (!reg.test(value)) {
          callback(new Error('手机号为11位数字！请重新输入！'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const validateQQ = (rule, value, callback) => {
      if (value.length < 5) {
        callback(new Error('QQ号至少为5位数字！请重新输入！'))
      } else {
        if (value.length > 10) {
          callback(new Error('QQ号不得超过11位，且为数字！请重新输入！'))
          var reg = RegExp('^[0-9]*$')
          if (!reg.test(value)) {
            callback(new Error('QQ号不得超过11位，且为数字！请重新输入！'))
          } else {
            callback()
          }
        } else {
          callback()
        }
      }
    }
    return {
      options: [
        {
          value: '0',
          label: '坚持奋斗'
        }, {
          value: '1',
          label: '已退役'
        }
      ],
      updateLoading: false,
      dialogUpdateFormVisible: false,
      tableKey: 0,
      listLoading: true,
      tableData: [],
      searchMemberId: '',
      current: 1,
      total: 0,
      size: 10,
      listQuery: {
        current: 1,
        total: 0,
        size: 10,
        params: {
          memberId: '',
          isRetire: ''
        }
      },
      updateRecordLoading: false,
      showReviewer: false,
      downloadLoading: false,
      updateMemberForm: {
        memberId: '',
        memberName: '',
        password: '',
        telephone: '',
        qq: '',
        gender: 0,
        isRetire: 0
      },
      updateMemberFormRules: {
        memberId: [{ required: true, trigger: 'blur', validator: validateMemberId }],
        telephone: [{ required: true, trigger: 'blur', validator: validateTelephone }],
        qq: [{ required: true, trigger: 'blur', validator: validateQQ }]
      }
    }
  },
  created() {
    if (this.$route.params.listQuery !== undefined) {
      console.log(this.$route.params.listQuery)
      this.listQuery = this.$route.params.listQuery
    }
    this.getDate()
  },
  methods: {
    analysis(row) {
      this.$router.push({
        name: 'AnalysisMemberDetail',
        params: {
          nowCoderId: row.nowCoderId,
          memberId: row.memberId,
          memberName: row.memberName,
          nationalPrizeNum: row.nationalPrizeNum,
          provincialPrizeNum: row.provincialPrizeNum,
          indexListQuery: this.listQuery
        }
      })
    },
    handleDetail(row) {
      this.$router.push({
        name: 'AnalysisMemberNowCoderDetail',
        params: {
          nowCoderId: row.nowCoderId,
          memberId: row.memberId,
          memberName: row.memberName,
          indexListQuery: this.listQuery
        }
      })
    },
    handleSearch() {
      // 如果没有搜索参数，直接获取全部数据并将当前页面设置为1
      if (this.listQuery.params.memberId === '' && this.listQuery.params.isRetire === '') {
        this.listQuery.current = 1
        this.getDate()
      } else {
        // 如果有条件则按按条件查询
        // 判断输入框内容是否符合规范
        if (this.listQuery.params.memberId.length > 8) {
          this.$message({
            message: '学号至多为8位数字！请重新输入',
            type: 'warning'
          })
          const reg = RegExp('^[0-9]*$')
          if (!reg.test(this.listQuery.params.memberId)) {
            this.$message({
              message: '学号仅为数字！请重新输入',
              type: 'warning'
            })
          }
        }
        // 开始查询
        this.listLoading = true
        // this.listQuery.userId = this.$store.state.user.userId
        queryMemberAnalysisByMemberId(this.listQuery).then(response => {
          const { total, size, current, records } = response.data
          this.total = total
          this.size = size
          this.current = current
          this.tableData = records
          setTimeout(() => {
            this.listLoading = false
          }, 0.25 * 1000)
        })
      }
    },
    getDate() {
      if (this.listQuery.params.memberId !== '') {
        this.handleSearch()
      } else {
        this.listLoading = true
        pagingMemberAnalysisInfo(this.listQuery).then(response => {
          const { total, size, current, records } = response.data
          this.total = total
          this.size = size
          this.current = current
          this.tableData = records
          setTimeout(() => {
            this.listLoading = false
          }, 0.25 * 1000)
        })
      }
    },
    handleCurrentChange(value) {
      this.listQuery.current = value
      this.getDate()
    },
    handleSizeChange(value) {
      this.listQuery.size = value
      this.listQuery.current = 1
      this.getDate()
    },
    handleUpdateAllRecord() {
      this.updateRecordLoading = true
      axios({
        baseURL: process.env.VUE_APP_BASE_API,
        url: 'dmas/api/member/updateAllNowCoderRecord',
        method: 'put',
        headers: { 'token': this.$store.state.user.token }
      }).then(response => {
        this.updateRecordLoading = false
        this.$message({
          showClose: true,
          message: '更新成功！',
          type: 'success'
        })
      })
    }
  }
}
</script>

