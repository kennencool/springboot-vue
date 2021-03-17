package com.kennen.yebserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kennen.yebserver.mapper.MailSendLogMapper;
import com.kennen.yebserver.pojo.Employee;
import com.kennen.yebserver.mapper.EmployeeMapper;
import com.kennen.yebserver.pojo.MailSendLog;
import com.kennen.yebserver.pojo.mail.MailConstants;
import com.kennen.yebserver.pojo.resp.RespBean;
import com.kennen.yebserver.pojo.resp.RespPageBean;
import com.kennen.yebserver.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hejiyuan
 * @since 2021-03-10
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Resource private EmployeeMapper employeeMapper;
    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private MailSendLogMapper mailSendLogMapper;
    
    @Override
    public RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, Date[] beginDateScope) {
        //  开启分页
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> employeeIPage = employeeMapper.getEmployeeByPage(page,employee,beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeeIPage.getTotal(),employeeIPage.getRecords());
        return respPageBean;
    }

    @Override
    public RespBean maxWorkID() {
        List<Map<String, Object>> maps = employeeMapper.selectMaps(new QueryWrapper<Employee>().select("max(id)"));
        return RespBean.success(null,Integer.parseInt(maps.get(0).get("max(id)").toString()) + 1);
    }

    @Override
    public RespBean addEmp(Employee employee) {
        //  计算合同期限
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365)));
        if(1 == employeeMapper.insert(employee)){
            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
            String msgId = UUID.randomUUID().toString();
            MailSendLog mailSendLog = new MailSendLog();
            mailSendLog.setMsgId(msgId);
            mailSendLog.setEmpId(employee.getId());
            mailSendLog.setStatus(MailConstants.DELIVERING);
            mailSendLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY);
            mailSendLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailSendLog.setCount(0);
            mailSendLog.setCreateTime(LocalDateTime.now());
            mailSendLog.setUpdateTime(LocalDateTime.now());
            mailSendLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailSendLogMapper.insert(mailSendLog);
            
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY,
                    emp,new CorrelationData(msgId));
            return RespBean.success("添加成功！");
        }
        return RespBean.error("添加失败！");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}
