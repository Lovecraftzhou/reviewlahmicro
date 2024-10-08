import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/article/list',
    method: 'get',
    params: query
  })
}

export function fetchmerchants(query) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/merchants/list',
    method: 'get',
    params: query
  })
}

export function fetchArticle(id) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/article/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/article/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/article/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: 'http://localhost:9527/dev-api/vue-element-admin/article/update',
    method: 'post',
    data
  })
}
