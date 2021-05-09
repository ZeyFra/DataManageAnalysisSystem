e<template>
  <div class="app-container">
    <el-form ref="uploadForm" label-position="top" :model="uploadForm" :rules="uploadFormRules">
      <el-row>
        <el-col :span="5" style="margin-left:60px">
          <el-form-item label="比赛名称" prop="contestName" label-width="130px">
            <el-input ref="contestName" v-model="uploadForm.contestName" placeholder="请输入比赛名称" />
          </el-form-item>
        </el-col>
        <el-col :span="5" style="margin-left:20px">
          <el-form-item label="比赛地点" prop="contestSite" label-width="130px">
            <el-input ref="contestSite" v-model="uploadForm.contestSite" placeholder="请输入比赛地点" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="5" style="margin-left:60px">
          <el-form-item label="选手（队伍）数量" prop="contestTeamNum" label-width="130px">
            <el-input ref="contestTeamNum" v-model="uploadForm.contestTeamNum" placeholder="请输入选手（队伍）数量" />
          </el-form-item>
        </el-col>

        <el-col :span="5" style="margin-left:20px">
          <el-form-item label="AK数量" prop="contestAkNum" label-width="130px">
            <el-input ref="contestAkNum" v-model="uploadForm.contestAkNum" placeholder="请输入AK数量" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="5" style="margin-left:60px">

          <el-form-item label="比赛时间" prop="contestTime" label-width="130px">
            <el-date-picker
              ref="contestTime"
              v-model="uploadForm.contestTime"
              type="date"
              placeholder="选择日期"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label-width="130px" style="width:45%; margin-left:60px">
        <el-upload
          ref="upload"
          action="http://localhost:8080/dmas/api/contest/upload"
          list-type="text"
          :on-success="uploadFileSuccess"
          :auto-upload="false"
          :limit="1"
          :data="uploadForm"
          :file-list="fileList"
          :headers="myHeaders"
          :on-error="uploadFileError"
          :on-exceed="handleExceed"
        >

          <el-button slot="trigger" type="primary" plain icon="el-icon-folder">选取文件</el-button>
          <el-button style="margin-left: 10px;" plain :loading="loading" type="success" @click="submitUpload">上传<i class="el-icon-upload el-icon--right" /></el-button>
          <el-button style="margin-left: 10px;" plain type="info" icon="el-icon-download" @click="handleGetTemplate">下载上传模板</el-button>
          <div slot="tip" class="el-upload__tip">只能上传1份xls/xlxs文件，且不超过 1 MB</div>
        </el-upload>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import axios from 'axios'

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
    const validatecontestTime = (rule, value, callback) => {
      if (value.length === 0) {
        callback(new Error('请选择日期'))
      } else {
        callback()
      }
    }
    const validatecontestProblemNum = (rule, value, callback) => {
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
      myHeaders: {
        token: ''
      },
      times: [],
      uploadForm: {
        contestName: '',
        contestSite: '',
        contestAkNum: '',
        contestTeamNum: '',
        contestTime: ''
      },
      fileList: [],
      loading: false,
      uploadFormRules: {
        contestName: [{ required: true, trigger: 'blur', validator: validateContestName }],
        contestSite: [{ required: true, trigger: 'blur', validator: validateContestSite }],
        contestAkNum: [{ required: true, trigger: 'blur', validator: validateContestTeamNum }],
        contestTeamNum: [{ required: true, trigger: 'blur', validator: validateContestAkNum }],
        contestTime: [{ required: true, trigger: 'blur', validator: validatecontestTime }],
        contestProblemNum: [{ required: true, trigger: 'blur', validator: validatecontestProblemNum }]
      }
    }
  },
  methods: {
    handleGetTemplate() {
      axios({
        baseURL: process.env.VUE_APP_BASE_API,
        url: 'dmas/api/contest/getTemplate',
        method: 'get',
        responseType: 'blob',
        headers: { 'token': this.$store.state.user.token }

      })
        .then((res) => {
          console.log(res)
          if (res) {
            const blob = new Blob([res.data], {
              type: 'application/vnd.ms-excel;charset=utf-8'
            }) // res就是接口返回的文件流了
            const objectUrl = URL.createObjectURL(blob)
            const elink = document.createElement('a')
            elink.download = '比赛数据上传模板.xlsx' // 下载文件名称,
            elink.style.display = 'none'
            elink.href = objectUrl
            document.body.appendChild(elink)
            elink.click()
            URL.revokeObjectURL(elink.href) // 释放URL 对象
            document.body.removeChild(elink)
          }
        })
        .catch(function(error) {
          console.log(error)
        })
    },
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1

      if (isLt1M) {
        return true
      }

      this.$message({
        message: '请不要上传大于1M的Excel文件！',
        type: 'warning'
      })
      return false
    },
    handleSuccess({ results, header }) {
      this.tableData = results
      this.tableHeader = header
    },
    submitUpload() {
      this.$refs.uploadForm.validate(valid => {
        if (valid) {
          const files = this.$refs.upload.uploadFiles
          if (files.length === 0) {
            this.$message({
              message: '请选择文件',
              type: 'warning'
            })
            return false
          }
          const rawFile = files[0]
          if (!this.isExcel(rawFile)) {
            this.$message.error('仅支持 .xlsx, .xls结尾的文件！')
            return false
          }
          this.myHeaders.token = this.$store.state.user.token
          this.loading = true
          this.$refs.upload.submit()
          this.loading = false
        }
      })
    },
    isExcel(file) {
      return /\.(xlsx|xls)$/.test(file.name)
    },

    // 上传失败
    uploadFileError() {
      this.$message.error('上传失败！')
    },
    // 上传成功
    uploadFileSuccess(response, file, fileList) {
      if (response.success) {
        this.$message.success('上传成功！')
      } else {
        this.$message.error('上传失败!!！')
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    }
  }
}
</script>
