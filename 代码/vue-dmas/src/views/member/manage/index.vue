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

      <el-table-column prop="telephone" label="电话" sortable>
        <template slot-scope="{row}">
          <span>{{ row.telephone }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="qq" label="QQ">
        <template slot-scope="{row}">
          <span>{{ row.qq }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="isRetire" label="状态">
        <template slot-scope="{row}">
          <span v-if="row.isRetire=='0'">坚持奋斗</span>
          <span v-if="row.isRetire=='1'">已退役</span>
        </template>
      </el-table-column>

      <el-table-column label="操作">
        <template slot-scope="{row}">
          <el-button plain type="primary" size="mini" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
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
    <el-dialog title="更新队员信息" :visible.sync="dialogUpdateFormVisible">
      <el-form ref="updateMemberForm" label-position="top" :model="updateMemberForm" :rules="updateMemberFormRules">
        <el-row>
          <el-col :span="10" style="margin-left:60px">
            <el-form-item label="学号" prop="memberId">
              <el-input ref="memberId" v-model="updateMemberForm.memberId" placeholder="请输入学号" />
            </el-form-item>
          </el-col>
          <el-col :span="10" style="margin-left:20px">
            <el-form-item label="姓名" prop="memberName">
              <el-input ref="memberName" v-model="updateMemberForm.memberName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10" style="margin-left:60px">
            <el-form-item label="QQ" prop="qq">
              <el-input ref="qq" v-model="updateMemberForm.qq" placeholder="请输入QQ号" />
            </el-form-item>
          </el-col>
          <el-col :span="10" style="margin-left:20px">
            <el-form-item label="手机号" prop="telephone">
              <el-input ref="telephone" v-model="updateMemberForm.telephone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10" style="margin-left:60px">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="updateMemberForm.gender" style="margin-left: 20px;">
                <el-radio :label="0">男</el-radio>
                <el-radio :label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="10" style="margin-left:20px">
            <el-form-item label="状态" prop="isRetire">
              <el-radio-group v-model="updateMemberForm.isRetire" style="margin-left: 20px;">
                <el-radio :label="0">坚持奋斗</el-radio>
                <el-radio :label="1">已退役</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" icon="el-icon-close" @click="handleCloseDialog">取消</el-button>
        <el-button style="margin-left: 10px;" :loading="updateLoading" type="success" @click="handleUpdateMember">
          保存
          <i class="el-icon-check el-icon--right" />
        </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>

import { getMemberList, queryMemberByMemberId, updateMember } from '@/api/member'

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
    this.getDate()
  },
  methods: {
    handleUpdateMember() {
      this.$refs.updateMemberForm.validate(valid => {
        if (valid) {
          this.updateLoading = true
          updateMember(this.updateMemberForm).then(response => {
            if (response.success) {
              this.$message({
                message: '操作成功！',
                type: 'success'
              })
            }
            this.updateLoading = false
            this.dialogUpdateFormVisible = false
          })
          this.getDate()
        }
      })
    },
    handleCloseDialog() {
      this.dialogUpdateFormVisible = false
    },
    handleEdit(member) {
      this.dialogUpdateFormVisible = true
      this.updateMemberForm.memberId = member.memberId
      this.updateMemberForm.memberName = member.memberName
      this.updateMemberForm.password = member.password
      this.updateMemberForm.telephone = member.telephone
      this.updateMemberForm.qq = member.qq
      if (member.gender) {
        this.updateMemberForm.gender = 1
      } else {
        this.updateMemberForm.gender = 0
      }
      if (member.isRetire) {
        this.updateMemberForm.isRetire = 1
      } else {
        this.updateMemberForm.isRetire = 0
      }
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
        queryMemberByMemberId(this.listQuery).then(response => {
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
        // this.listQuery.userId = this.$store.state.user.userId
        getMemberList(this.listQuery).then(response => {
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
          message: '更新成功！本次更新耗时：' + response.data.data.time + '秒!',
          type: 'success'
        })
      })
    }
  }
}
</script>

