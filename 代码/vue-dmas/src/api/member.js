import request from '@/utils/request'

export function getMemberList(params) {
  return request({
    url: '/dmas/api/member/pageMemberList',
    method: 'get',
    params
  })
}

export function queryMemberByMemberId(data) {
  return request({
    url: '/dmas/api/member/queryMemberByMemberId',
    method: 'post',
    data
  })
}

export function updateMember(data) {
  return request({
    url: '/dmas/api/member/updateMember',
    method: 'put',
    data
  })
}

export function getTemplate() {
  return request({
    url: 'dmas/api/member/getTemplate',
    method: 'get',
    responseType: 'blob'
  })
}

export function pageNowCoderRecordList(data) {
  return request({
    url: 'dmas/api/nowCoder/pageNowCoderRecordList',
    method: 'post',
    data
  })
}

export function getExamDetailList(data) {
  return request({
    url: 'dmas/api/member/getExamDetailList',
    method: 'post',
    data
  })
}

export function pagingMemberAnalysisInfo(params) {
  return request({
    url: 'dmas/api/member/pagingMemberAnalysisInfo',
    method: 'get',
    params
  })
}

export function queryMemberAnalysisByMemberId(data) {
  return request({
    url: '/dmas/api/member/queryMemberAnalysisByMemberId',
    method: 'post',
    data
  })
}

export function getMemeberAllNowCoderRecordForAnalysis(params) {
  return request({
    url: '/dmas/api/nowCoder/getMemeberAllNowCoderRecordForAnalysis',
    method: 'get',
    params
  })
}

export function getAllMemeberDistribution() {
  return request({
    url: '/dmas/api/member/getAllMemeberDistribution',
    method: 'get'
  })
}

export function getMemeberAllExamDetailForAnalysis() {
  return request({
    url: '/dmas/api/member/getMemeberAllExamDetailForAnalysis',
    method: 'get'
  })
}
