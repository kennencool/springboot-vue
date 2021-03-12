package com.kennen.yebserver.controller;


import com.kennen.yebserver.pojo.Joblevel;
import com.kennen.yebserver.pojo.Position;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.service.IJoblevelService;
import io.swagger.annotations.Api;
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
@RequestMapping("/system/basic/job")
public class JoblevelController {
    @Resource private IJoblevelService joblevelService;
    
    @ApiOperation(value = "查询所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJoblevels(){
        return joblevelService.list();
    }
    
    @ApiOperation(value = "添加一个职称")
    @PostMapping("/")
    public RespBean addJoblevel(
            @RequestBody Joblevel joblevel
    ){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @ApiOperation(value = "更新职称信息")
    @PutMapping("/")
    public RespBean updateJoblevel(
            @RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "删除职称信息")
    @DeleteMapping("/{id}")
    public RespBean deleteJoblevel(
            @PathVariable Integer id
    ){
        if(joblevelService.removeById(id)){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "批量删除职称信息")
    @DeleteMapping("/")
    public RespBean deleteJoblevelsByIds(Integer[] ids){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }
    
}
