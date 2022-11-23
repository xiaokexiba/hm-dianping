package com.hmdp.service;

import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
public interface IBlogService extends IService<Blog> {

    /**
     * 根据id查找博客
     *
     * @param id 博客ID
     * @return 统一返回类
     */
    Result queryBlogById(Long id);

    /**
     * @param current
     * @return
     */
    Result queryHotBlog(Integer current);

    /**
     * @param id
     * @return
     */
    Result likeBlog(Long id);

    Result queryBlogLikes(Long id);

    Result saveBlog(Blog blog);

    Result queryBlogOfFollow(Long max, Integer offset);
}
