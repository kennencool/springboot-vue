package com.kennen.yebserver.mapper;

import com.kennen.yebserver.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenusById(Integer id);

    List<Menu> getMenusByRole();
}
