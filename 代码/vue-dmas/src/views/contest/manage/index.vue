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

      <el-table-column prop="contestName" label="比赛名称">
        <template slot-scope="{row}">
          <span>{{ row.contestName }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contestSite" label="比赛地点">
        <template slot-scope="{row}">
          <span>{{ row.contestSite }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contestTime" label="比赛日期" sortable>
        <template slot-scope="{row}">
          <span>{{ row.contestTime }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contestTeamNum" label="比赛人（队伍）数">
        <template slot-scope="{row}">
          <span>{{ row.contestTeamNum }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="contestAkNum" label="AK数">
        <template slot-scope="{row}">
          <span>{{ row.contestAkNum }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button plain type="primary" size="small" icon="el-icon-view" @click="handleDetail(scope.row)" />
          <el-button plain type="primary" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)" />
          <el-button plain type="primary" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)" />
          <!-- <el-button-group>

          </el-button-group> -->
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
    <el-dialog title="更新比赛信息" :visible.sync="dialogUpdateFormVisible">
      <el-form ref="updateContestForm" label-position="top" :model="updateContestForm" :rules="updateContestFormRules">
        <el-row>
          <el-col :span="5" style="margin-left:60px">
            <el-form-item label="比赛名称" prop="contestName" label-width="130px">
              <el-input ref="contestName" v-model="updateContestForm.contestName" placeholder="请输入比赛名称" />
            </el-form-item>
          </el-col>
          <el-col :span="5" style="margin-left:20px">
            <el-form-item label="比赛地点" prop="contestSite" label-width="130px">
              <el-input ref="contestSite" v-model="updateContestForm.contestSite" placeholder="请输入比赛地点" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="5" style="margin-left:60px">
            <el-form-item label="选手（队伍）数量" prop="contestTeamNum" label-width="130px">
              <el-input ref="contestTeamNum" v-model="updateContestForm.contestTeamNum" placeholder="请输入选手（队伍）数量" />
            </el-form-item>
          </el-col>

          <el-col :span="5" style="margin-left:20px">
            <el-form-item label="AK数量" prop="contestAkNum" label-width="130px">
              <el-input ref="contestAkNum" v-model="updateContestForm.contestAkNum" placeholder="请输入AK数量" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="5" style="margin-left:60px">

            <el-form-item label="比赛时间" prop="contestTime" label-width="130px">
              <el-date-picker
                ref="contestTime"
                v-model="updateContestForm.contestTime"
                type="date"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" icon="el-icon-close" @click="handleCloseDialog">取消</el-button>
        <el-button style="margin-left: 10px;" :loading="updateLoading" type="success" @click="handleUpdateContest">
          保存
          <i class="el-icon-check el-icon--right" />
        </el-button>
      </span>
    </el-dialog>
  </div>

</template>

<script>

import { getContestList, queryContest, updateContest, deleteContest } from '@/api/contest'

export default {

  data() {
    const validateContestName = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('比赛名称不能为空！请输入！'))
      } else {
        callback()
      }
    }
    const validateContestSite = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('比赛名称不能为空！请输入！'))
      } else {
        callback()
      }
    }
    const validateContestTeamNum = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('比赛队伍数量不能为空！请输入！'))
      } else {
        var reg = RegExp('^[0-9]*$')
        if (!reg.test(value)) {
          callback(new Error('请输入数字！'))
        } else {
          callback()
        }
        callback()
      }
    }
    const validateContestAkNum = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('AK选手（队伍）数量不能为空！请输入！'))
      } else {
        var reg = RegExp('^[0-9]*$')
        if (!reg.test(value)) {
          callback(new Error('请输入数字！'))
        } else {
          callback()
        }
        callback()
      }
    }
    const validateContestTime = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('请选择日期'))
      } else {
        callback()
      }
    }
    const validateContestProblemNum = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('比赛题目数量不能为空！请输入！'))
      } else {
        var reg = RegExp('^[0-9]*$')
        if (!reg.test(value)) {
          callback(new Error('请输入数字！'))
        } else {
          callback()
        }
        callback()
      }
    }

    return {
      updateLoading: false,
      dialogUpdateFormVisible: false,
      tableKey: 0,
      listLoading: true,
      current: 1,
      total: 0,
      size: 10,
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
      },
      updateContestForm: {
        contestId: '',
        contestName: '',
        contestSite: '',
        contestAkNum: '',
        contestTeamNum: '',
        contestTime: ''
      },
      showReviewer: false,
      downloadLoading: false,
      updateContestFormRules: {
        contestName: [{ required: true, trigger: 'blur', validator: validateContestName }],
        contestSite: [{ required: true, trigger: 'blur', validator: validateContestSite }],
        contestAkNum: [{ required: true, trigger: 'blur', validator: validateContestTeamNum }],
        contestTeamNum: [{ required: true, trigger: 'blur', validator: validateContestAkNum }],
        contestTime: [{ required: true, trigger: 'blur', validator: validateContestTime }],
        contestProblemNum: [{ required: true, trigger: 'blur', validator: validateContestProblemNum }]
      }
    }
  },
  created() {
    this.getDate()
  },
  methods: {

    handleDelete(contest) {
      this.$confirm('此操作将永久删除该场比赛, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const data = {
          contestId: contest.contestId
        }
        deleteContest(data).then(response => {
          if (response.success) {
            this.$message({
              type: 'success',
              message: '删除成功！'
            })
            this.getDate()
          } else {
            this.$message({
              type: 'danger',
              message: response.messga
            })
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    handleUpdateContest() {
      this.$refs.updateContestForm.validate(valid => {
        if (valid) {
          this.updateLoading = true
          updateContest(this.updateContestForm).then(response => {
            if (response.success) {
              this.$message({
                message: '操作成功！',
                type: 'success'
              })
              this.updateLoading = false
              this.dialogUpdateFormVisible = false
              this.getDate()
            }
          })
        }
      })
    },
    handleCloseDialog() {
      this.dialogUpdateFormVisible = false
    },
    handleEdit(contest) {
      this.dialogUpdateFormVisible = true
      this.updateContestForm.contestId = contest.contestId
      this.updateContestForm.contestName = contest.contestName
      this.updateContestForm.contestSite = contest.contestSite
      this.updateContestForm.contestAkNum = contest.contestAkNum
      this.updateContestForm.contestTeamNum = contest.contestTeamNum
      this.updateContestForm.contestTime = contest.contestTime
    },
    handleSearch() {
      // 如果没有搜索参数，直接获取全部数据并将当前页面设置为1
      if ((this.listQuery.params.contestTime === '' || this.listQuery.params.contestTime === null) && this.listQuery.params.contestName === '') {
        this.listQuery.current = 1
        this.getDate()
      } else {
        // 如果有条件则按按条件查询
        console.log(this.listQuery.params)
        queryContest(this.listQuery).then(response => {
          const { total, size, current, records } = response.data
          this.total = total
          this.size = size
          this.current = current
          this.tableDataList = records
          setTimeout(() => {
            this.listLoading = false
          }, 0.25 * 1000)
        })
      }
    },
    handleDetail(row) {
      this.$router.push({
        name: 'ContestDetail',
        params: {
          contestId: row.contestId
        }
      })
    },
    getDate() {
      if ((this.listQuery.params.contestTime !== null || this.listQuery.params.contestTime !== '') && this.listQuery.params.contestName !== '') {
        this.handleSearch()
      } else {
        this.listLoading = true
        this.listQuery.userId = this.$store.state.user.userId
        getContestList(this.listQuery).then(response => {
          const { total, size, current, records } = response.data
          this.total = total
          this.size = size
          this.current = current
          this.tableDataList = records
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
    }
  }
}
</script>
