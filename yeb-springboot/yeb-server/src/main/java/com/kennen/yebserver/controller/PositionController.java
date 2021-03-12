package com.kennen.yebserver.controller;


import com.kennen.yebserver.pojo.Position;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IPositionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Resource private IPositionService positionService;
    
    @ApiOperation(value = "获取所有职位信息")
    @GetMapping("/")
    public List<Position> getAllPositions(){
        return positionService.list();
    }
    
    @ApiOperation(value = "添加职位信息")
    @PostMapping("/")
    public RespBean addPosition(
            @RequestBody Position position){
        position.setCreateDate(LocalDateTime.now());
        if(positionService.save(position)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }
    
    @ApiOperation(value = "更新职位信息")
    @PutMapping("/")
    public RespBean updatePosition(
            @RequestBody Position position){
        if(positionService.updateById(position)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除职位信息")
    @DeleteMapping("/{id}")
    public RespBean deletePosition(
            @PathVariable Integer id
    ){
        if(positionService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
    
    @ApiOperation(value = "批量删除职位信息")
    @DeleteMapping("/")
    public RespBean deletePositionsByIds(Integer[] ids){
        if(positionService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
}
