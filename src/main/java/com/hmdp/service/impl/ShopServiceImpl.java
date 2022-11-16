package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据ID查询数据
     *
     * @param id 商品ID
     * @return 统一返回类
     */
    @Override
    public Result queryById(Long id) {
        // 缓存穿透
        // Shop shop = queryWithPassThrough(id);

        // 互斥锁解决缓存击穿
        Shop shop = queryWithMutex(id);
        if (shop == null) {
            return Result.fail("店铺不存在！");
        }
        return Result.ok(shop);
    }

    public Shop queryWithMutex(Long id) {
        String key = CACHE_SHOP_KEY + id;
        // 从redis中查询数据
        String jsonShop = stringRedisTemplate.opsForValue().get(key);
        // 存在，返回信息
        if (StrUtil.isNotBlank(jsonShop)) {
            return JSONUtil.toBean(jsonShop, Shop.class);
        }
        if (jsonShop != null) {
            return null;
        }
        String lockKey = null;
        Shop shop = null;
        try {
            // 实现缓存重建
            // 获取互斥锁
            lockKey = LOCK_SHOP_KEY + id;
            boolean isLock = tryLock(lockKey);
            // 判断是否获取成功
            if (!isLock) {
                // 失败，休眠并进行重试
                Thread.sleep(50);
                return queryWithPassThrough(id);
            }
            // 不存在，在数据中查询
            shop = getById(id);
            // 模拟重建的延迟
            Thread.sleep(200);
            if (shop == null) {
                // 将空值写入redis
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                // 不存在，返回错误信息
                return null;
            }
            // 存在，将信息写入redis中，并且返回信息
            stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放互斥锁
            unLock(lockKey);
        }
        return shop;
    }

    public Shop queryWithPassThrough(Long id) {
        String key = CACHE_SHOP_KEY + id;
        // 从redis中查询数据
        String jsonShop = stringRedisTemplate.opsForValue().get(key);
        // 存在，返回信息
        if (StrUtil.isNotBlank(jsonShop)) {
            return JSONUtil.toBean(jsonShop, Shop.class);
        }
        if (jsonShop != null) {
            return null;
        }
        // 不存在，在数据中查询
        Shop shop = getById(id);
        if (shop == null) {
            // 将空值写入redis
            stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 不存在，返回错误信息
            return null;
        }
        // 存在，将信息写入redis中，并且返回信息
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return shop;
    }

    /**
     * 更新店铺信息
     *
     * @param shop 店铺
     * @return 统一返回类
     */
    @Override
    @Transactional
    public Result update(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("店铺ID不能为空");
        }
        // 更新数据库
        updateById(shop);
        // 删除缓存
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        return Result.ok();
    }

    /**
     * 上锁
     */
    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "xxx", 10, TimeUnit.SECONDS);
        // 这里直接返回flag的话，会进行拆箱
        // 拆箱底层就是调用booleanValue()方法，如果flag为null的话就会空指针异常
        // 所以这里直接用工具类进行拆箱
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 开锁
     */
    private void unLock(String key) {
        stringRedisTemplate.delete(key);
    }
}
