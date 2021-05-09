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
      <el-page-header style="margin-top:10px;margin-left:15px" :content="pageHeatherContent" @back="handleGoBack" />
      <div style="text-align:center;margin-top:10px">
        <el-button type="success" plain icon="el-icon-document" @click="handleExamDetail">学科情况</el-button>
        <el-button :loading="exportLoading" type="success" plain icon="el-icon-download" @click="handleExportToExcel">导出数据为Excel</el-button>
      </div>
      <el-row>
        <div class="contest-list">
          <ul class="contest-list-ul">
            <li v-for="(item,i) in nowCoderDetailList" :key="i" style="margin-bottom:40px" class="contest-list-li">
              <el-card>
                <div class="img-box">
                  <img :src="item.logoUrl" class="image">
                </div>
                <div style="contest-info">
                  <el-link class="contest-item-name" target="_blank" :href="'https://ac.nowcoder.com/acm/contest/'+item.contestId+'#rank/%22searchUserName%22%3A%22'+item.teamName+'%22'" type="primary">{{ item.contestName }}</el-link>
                  <div class="contest-item-time">
                    <div class="item-time">
                      <svg-icon icon-class="contest-time" />
                    </div>
                    <div class="item-cont">
                      {{ item.startTime }} 至 {{ item.endTime }}（时长： {{ item.contestDuration }}小时）
                    </div>
                  </div>
                  <div class="member-state-detail">
                    <div class="member-state-item">
                      <div class="member-state-num">{{ item.rank }} / {{ item.userCount }}</div>
                      <span>排名</span>
                    </div>
                    <div class="member-state-item">
                      <div class="member-state-num">{{ item.acceptCount }} / {{ item.problemCount }}</div>
                      <span>AC数量</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </li>
          </ul>
        </div>
      </el-row>
      <div style="text-align:center;margin-bottom:10px;" class="pagination">
        <el-pagination
          small
          background
          :current-page="current"
          :page-size="size"
          :page-sizes="[4, 8]"
          layout="total, prev, pager, next, jumper, sizes"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>

  </div>
</template>

<script>
import { pageNowCoderRecordList } from '@/api/member'
import axios from 'axios'

export default {
  data() {
    return {
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
      listQuery: {
        current: 1,
        total: 0,
        size: 4,
        params: {
          memberId: '',
          nowCoderId: '',
          memberName: ''
        }
      },
      exportLoading: false,
      fullscreenLoading: false,
      nowCoderDetailList: [],
      examScoreDetailList: [],
      pageHeatherContent: '',
      current: 1,
      total: 0,
      size: 10
    }
  },
  created() {
    this.fullscreenLoading = true
    if (this.$route.params.nowCoderId === undefined || this.$route.params.memberId === undefined || this.$route.params.memberName === undefined) {
      this.handleGoBack()
    }
    this.indexListQuery = this.$route.params.indexListQuery
    this.listQuery.params.nowCoderId = this.$route.params.nowCoderId
    this.listQuery.params.memberId = this.$route.params.memberId
    this.listQuery.params.memberName = this.$route.params.memberName
    this.pageHeatherContent = this.listQuery.params.memberId + ' - ' + this.listQuery.params.memberName + '：' + '牛客训练详情'
    this.getNewCoderDetail()
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
    getNewCoderDetail() {
      pageNowCoderRecordList(this.listQuery).then(response => {
        const { total, size, current, records } = response.data
        this.total = total
        this.size = size
        this.current = current
        this.nowCoderDetailList = records
        this.fullscreenLoading = false
      })
    },
    handleCurrentChange(value) {
      this.listQuery.current = value
      this.getNewCoderDetail()
    },
    handleSizeChange(value) {
      this.listQuery.size = value
      this.listQuery.current = 1
      this.getNewCoderDetail()
    },
    handleExamDetail() {
      this.$router.push({
        name: 'AnalysisMemberExamDetail',
        params: {
          nowCoderId: this.listQuery.params.nowCoderId,
          memberId: this.listQuery.params.memberId,
          memberName: this.listQuery.params.memberName,
          indexListQuery: this.indexListQuery
        }
      })
    },
    handleExportToExcel() {
      this.exportLoading = true
      axios({
        baseURL: process.env.VUE_APP_BASE_API,
        url: 'dmas/api/member/exportToExcecl',
        method: 'post',
        responseType: 'blob',
        headers: { 'token': this.$store.state.user.token },
        data: this.listQuery.params

      }).then((res) => {
        if (res) {
          const blob = new Blob([res.data], {
            type: 'application/vnd.ms-excel;charset=utf-8'
          }) // res就是接口返回的文件流了
          const objectUrl = URL.createObjectURL(blob)
          const elink = document.createElement('a')
          elink.download = this.listQuery.params.memberId + '-' + this.listQuery.params.memberName + '.xlsx' // 下载文件名称,
          elink.style.display = 'none'
          elink.href = objectUrl
          document.body.appendChild(elink)
          this.exportLoading = false
          elink.click()
          URL.revokeObjectURL(elink.href) // 释放URL 对象
          document.body.removeChild(elink)
        }
      })
        .catch(function(error) {
          console.log(error)
        })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .img-box{
    float: left;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    color: #333;
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 14px;
    width: 80px;
    height: 80px;
    float: left;
    overflow: hidden;
    background: #bbb;
  }

  .contest-info{
    color: #333;
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 14px;
    margin-left: 90px;

  }
  .contest-item-time{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    min-width: 100px;
    margin: 5px 20px 10px 0;
    font-size: 10px;
    color: #86909b;
  }
  .item-time{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 14px;
    color: #86909b;
    width: 20px;
    height: 20px;
    float: left;
    margin-left: 10px;
  }
  .item-cont{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 10px;
    color: #86909b;
    margin-left: 25px;
    max-width: 30em;
  }
  .member-state-detail{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 8px;
    background: #f0f0f0;
    padding: 15px 0;
    height: 50px;
    text-align: center;
    flex-direction: row;
    margin: 0 auto;
    color: #666;
    margin-top: 25px;
  }
  .member-state-item{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    font-size: 8px;
    display: inline-block;
    text-align: center;
    padding: 0 10px;
    color: #666;
    border-left: 1px solid #e9e9e9;
    width: 128px;
    margin: 0 auto;
    margin-top: -6px;
  }
  .member-state-num{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    text-align: center;
    color: #666;
    font-size: 8px;
    margin: 0 auto;
    margin-bottom: 0px;
  }
  .contest-item-name{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    margin-bottom: 5px;
    font-size: 14px;
    display: inline-block;
    vertical-align: middle;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    word-wrap: normal;
    max-width: 20em;
    text-decoration: none;
    color: #25bb9b;
    margin-left: 12px;
  }
  .contest-list{
    color: #333;
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    margin: 0;
    padding: 0;
    font-weight: 400;
    list-style: none;
    font-size: 0;
  }
  .contest-list-ul{
    width: 1100px;
    margin-top:20px;
    margin-left: 120px;

  }
  .contest-list-li{
    width: 450px;
    float: left;
    margin-left: 50px;
  }
</style>
