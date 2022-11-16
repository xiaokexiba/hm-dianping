package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IShopService extends IService<Shop> {

    /**
     * 根据ID查询数据
     *
     * @param id 商品ID
     * @return 统一返回类
     */
    Result queryById(Long id);

    /**
     * 更新店铺信息
     *
     * @param shop 店铺
     * @return 统一返回类
     */
    Result update(Shop shop);
}
