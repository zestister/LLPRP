import request from '@/utils/request.js'
//语句查询
export const sentenceSearchService = (sentenceData)=>{
    //借助于UrlSearchParams完成传递
    const params = new URLSearchParams()
    for(let key in sentenceData){
        params.append(key,sentenceData[key]);
    }
    return request.post('/llrp',params);
}

//语句历史记录
export const sentenceListService = (params)=>{
    return request.get('/llrp/history',{params:params})
}
//语句删除
export const sentenceDeleteService = (id)=>{
    return request.delete('/llrp?id='+id)
}
//语句详情
export const sentenceGetService = (id)=>{
    return request.get('/llrp/detail?id='+id)
}
