package com.kennen.yebserver.mapper;

import com.kennen.yebserver.pojo.Hr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kennen.yebserver.pojo.Menu;

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
}
