package com.hejin.file.net;

import com.hejin.common.Constants;
import com.hejin.common.network.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * 作者 : 贺金龙
 * 创建时间 :  2018/2/11 8:17
 * 类描述 :
 * 修改人 :
 * 修改内容 :
 * 修改时间 :
 * 类说明 :
 *  
 */
public interface FileService {
    /**
     * 这个是一个相应的网络请求
     */
    @POST(Constants.UrlPath.TIMING)
    Observable<BaseBean<String>> getTiming();
}
