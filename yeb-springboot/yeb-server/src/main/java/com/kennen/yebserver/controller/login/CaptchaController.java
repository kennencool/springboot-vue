package com.kennen.yebserver.controller.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/10 17:43
 * @Description: 二维码接口
 */
@RestController
public class CaptchaController {
    @Autowired private DefaultKaptcha defaultKaptcha;
    
    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha",produces = "image/jpeg")
    public void captcha(HttpServletRequest request, HttpServletResponse response){
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");
        response.setHeader("Cache-Control","post-check=0, pre-check=0");
        response.setHeader("Pragma","no-cache");
        response.setContentType("image/jpeg");
        
        //  生成验证码
        String text = defaultKaptcha.createText();
        System.out.println("验证码内容：" + text);
        //  将验证码文本放入session用于之后服务端验证
        request.getSession().setAttribute("captcha",text);
        //  根据文本验证码内容创建图像验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try{
            outputStream = response.getOutputStream();
            //  输出流输出图片，格式为jpg
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}