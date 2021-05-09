<template>
  <div class="app-container uploadBody">

    <el-upload
      ref="upload"
      action="http://localhost:8080/dmas/api/award/upload"
      list-type="text"
      :on-success="uploadFileSuccess"
      :auto-upload="false"
      :limit="1"
      :file-list="fileList"
      :headers="myHeaders"
      :on-error="uploadFileError"
      style="margin-top:150px"
      :on-exceed="handleExceed"
    >

      <el-button slot="trigger" plain type="primary" icon="el-icon-folder">选取文件</el-button>
      <el-button style="margin-left: 10px;" plain :loading="loading" type="success" @click="submitUpload">上传<i class="el-icon-upload el-icon--right" /></el-button>
      <el-button style="margin-left: 10px;" plain type="info" icon="el-icon-download" @click="handleGetTemplate">下载上传模板</el-button>
      <el-button style="margin-left: 10px;" plain :loading="loading" type="success" @click="submitUpload">表单上传<i class="el-icon-upload el-icon--right" /></el-button>
      <div slot="tip" class="el-upload__tip">只能上传1份xls/xlxs文件，且不超过 2 MB</div>
    </el-upload>

  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      myHeaders: {
        token: ''
      },
      times: [],
      fileList: [],
      loading: false
    }
  },
  methods: {
    handleGetTemplate() {
      axios({
        baseURL: process.env.VUE_APP_BASE_API,
        url: 'dmas/api/prizes/getTemplate',
        method: 'get',
        responseType: 'blob',
        headers: { 'token': this.$store.state.user.token }

      })
        .then((res) => {
          if (res) {
            const blob = new Blob([res.data], {
              type: 'application/vnd.ms-excel;charset=utf-8'
            }) // res就是接口返回的文件流了
            const objectUrl = URL.createObjectURL(blob)
            const elink = document.createElement('a')
            elink.download = '队员数据上传模板.xlsx' // 下载文件名称,
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
      const isLt1M = file.size / 1024 / 1024 < 2

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

<style rel="stylesheet/scss" lang="scss" scoped>
  .uploadBody{
    display:flex;
    justify-content:center;
  }

</style>
