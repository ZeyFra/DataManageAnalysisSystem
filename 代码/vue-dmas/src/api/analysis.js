import request from '@/utils/request'

export function getMemberAnalysisInfos(params) {
  return request({
    url: '/dmas/api/member/getMemberAnalysisInfos',
    method: 'get',
    params
  })
}
