package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * @Author 86178
 * @create 2019/7/2 10:23
 */
public interface PromoService {

    //根据itemId获取即将进行的或正在进行的秒杀活动
    PromoModel getPromoByItemId(Integer itemId);
}
