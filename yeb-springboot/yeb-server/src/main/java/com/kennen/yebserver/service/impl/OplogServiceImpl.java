package com.kennen.yebserver.service.impl;

import com.kennen.yebserver.pojo.Oplog;
import com.kennen.yebserver.mapper.OplogMapper;
import com.kennen.yebserver.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
