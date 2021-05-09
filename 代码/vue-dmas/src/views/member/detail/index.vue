<template>
  <div
    v-loading="fullscreenLoading"
    class="app-container"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0)"
  >
    <div class="filter-container">
      <el-page-header :content="pageHeatherContent" @back="handleGoBack" />
      <!-- <el-button @click="handleGoBack">返回</el-button> -->

    </div>
    <el-row :gutter="20" align="middle" type="flex" justify="center" style="margin-top:20px">
      <el-col :span="8">
        <el-card style="height:175px;">
          <div class="img-box">
            <img src="https://uploadfiles.nowcoder.com/images/20200925/999991351_1601022724031_636D455DDEFEAE241B7365A3DFCE5F0D" class="image">
          </div>
          <div style="compete-info">
            <el-link href="https://ac.nowcoder.com/acm/contest/7501" type="primary">2020ICPC·小米 网络选拔赛第一场</el-link>
            <div class="compete-item-time">
              <i class="item-time" />
              <div class="item-cont">
                2020-10-25 12:00 至 17:00（时长： 5小时）
              </div>
            </div>
            <div class="member-state-detail">
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>排名</span>
              </div>
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>AC数量</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" :offset="2">
        <el-card style="height:175px">
          <div class="img-box">
            <img src="https://uploadfiles.nowcoder.com/images/20200925/999991351_1601022724031_636D455DDEFEAE241B7365A3DFCE5F0D" class="image">
          </div>
          <div style="compete-info">
            <el-link href="https://ac.nowcoder.com/acm/contest/7501" type="primary">2020ICPC·小米 网络选拔赛第一场</el-link>
            <div class="compete-item-time">
              <i class="item-time" />
              <div class="item-cont">
                2020-10-25 12:00 至 17:00（时长： 5小时）
              </div>
            </div>
            <div class="member-state-detail">
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>排名</span>
              </div>
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>AC数量</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" align="middle" type="flex" justify="center" style="margin-top:20px">
      <el-col :span="8">
        <el-card style="height:175px;">
          <div class="img-box">
            <img src="https://uploadfiles.nowcoder.com/images/20200925/999991351_1601022724031_636D455DDEFEAE241B7365A3DFCE5F0D" class="image">
          </div>
          <div style="compete-info">
            <el-link href="https://ac.nowcoder.com/acm/contest/7501" type="primary">2020ICPC·小米 网络选拔赛第一场</el-link>
            <div class="compete-item-time">
              <i class="item-time" />
              <div class="item-cont">
                2020-10-25 12:00 至 17:00（时长： 5小时）
              </div>
            </div>
            <div class="member-state-detail">
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>排名</span>
              </div>
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>AC数量</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" :offset="2">
        <el-card style="height:175px">
          <div class="img-box">
            <img src="https://uploadfiles.nowcoder.com/images/20200925/999991351_1601022724031_636D455DDEFEAE241B7365A3DFCE5F0D" class="image">
          </div>
          <div style="compete-info">
            <el-link href="https://ac.nowcoder.com/acm/contest/7501" type="primary">2020ICPC·小米 网络选拔赛第一场</el-link>
            <div class="compete-item-time">
              <i class="item-time" />
              <div class="item-cont">
                2020-10-25 12:00 至 17:00（时长： 5小时）
              </div>
            </div>
            <div class="member-state-detail">
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>排名</span>
              </div>
              <div class="member-state-item">
                <div class="member-state-num">858 / 1808</div>
                <span>AC数量</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { getMemberDetail } from '@/api/member'

export default {
  data() {
    return {
      listQuery: {
        memberId: '',
        newcoderId: '',
        memberName: ''
      },
      fullscreenLoading: false,
      nowCoderDetail: [],
      examScoreDetail: [],
      pageHeatherContent: ''
    }
  },
  created() {
    this.fullscreenLoading = true
    this.listQuery.newcoderId = this.$route.params.newcoderId
    this.listQuery.memberId = this.$route.params.memberId
    this.listQuery.memberName = this.$route.params.memberName
    this.pageHeatherContent = this.listQuery.memberId + ' — ' + this.listQuery.memberName + '：' + '牛客训练详情'
    this.getNewCoderDetail()
  },
  methods: {
    handleGoBack() {
      this.$router.push('/member/manage')
    },
    getNewCoderDetail() {
      getMemberDetail(this.listQuery).then(response => {
        console.log(response)
        console.log(response.data)
        this.nowCoderDetail = response.data.newCoderInfoDTOList
        this.examScoreDetail = response.data.examScoreInfoDTOList
        this.fullscreenLoading = false
        console.log(this.nowCoderDetail)
        console.log(this.examScoreDetail)
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
    width: 100%;
    display: block;
  }

  .compete-info{
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
  .compete-item-time{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizelegibility;
    direction: ltr;
    font: 12px/1.5 system,-apple-system,BlinkMacSystemFont,"Helvetica Neue",Helvetica,"PingFang SC","Segoe UI","Microsoft YaHei","wenquanyi micro hei","Hiragino Sans GB","Hiragino Sans GB W3",Roboto,Oxygen,Ubuntu,Cantarell,"Fira Sans","Droid Sans",Arial,sans-serif;
    word-wrap: break-word;
    word-break: break-word;
    list-style: none;
    font-weight: 400;
    //display: inline-block;
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
    background: url(https://static.nowcoder.com/images/img/platform/icons3.png) 0 -318px no-repeat;
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
    font-size: 14px;
    color: #86909b;
    margin-left: 25px;
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
    display: flex;
    align-items: center;
    justify-items: center;
    flex-direction: row;
    color: #666;
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
  }
</style>
