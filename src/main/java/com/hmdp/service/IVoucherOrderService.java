package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xoke
 * @since 2022/11/18
 */
public interface IVoucherOrderService extends IService<VoucherOrder> {

    /**
     * 秒杀下单
     *
     * @param voucherId 订单ID
     * @return 统一返回类
     */
    Result seckillVoucher(Long voucherId);

    /**
     * @param voucherId
     */
    void createVoucherOrder(VoucherOrder voucherId);
}
