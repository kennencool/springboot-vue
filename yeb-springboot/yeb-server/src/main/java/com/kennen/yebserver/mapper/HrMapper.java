package com.kennen.yebserver.mapper;

import com.kennen.yebserver.pojo.Hr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kennen.yebserver.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface HrMapper extends BaseMapper<Hr> {

    /**
     * 根据用户id获取菜单
     * @param id
     * @return
     */
    List<Menu> getMenusById(Integer id);

    /**
     * 获取其他操作员
     * @param id
     * @param keyword
     * @return
     */
    List<Hr> getAllHrs(@Param("id") Integer id, @Param("keyword") String keyword);
}
