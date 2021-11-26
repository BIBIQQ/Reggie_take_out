package com.ff.controller;

import com.ff.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author FF
 * @date 2021/11/25
 * @TIME:9:33
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String path;
    /**
     * 文件上传    参数变量名需要和前端的name相同
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        //  file存储的是临时文件
        //  上传的文件的名字 全球唯一
        //  上传路径
        //   保存上传的图片位置
        String filename = file.getOriginalFilename();
        //文件名字改良
        // 获得文件的后缀
        filename = filename.substring(filename.lastIndexOf("."));
        //拼接名字
        String name = UUID.randomUUID().toString().replace("-","");

        filename = name+filename;

        //创建文件夹
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdir();
        }

       /* InputStream is = file.getInputStream();

        //写入本地
        FileOutputStream fos = new FileOutputStream(file1+"//"+filename);

        while (true){
            int read = is.read();
            if(read == -1){
                break;
            }
            fos.write(read);
        }*/
        file.transferTo(new File(path+filename));

        return Result.success(filename,"上传成功");
    }

    /**
     * 上传的图片前端展示
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
        //输入流  ，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(path+name));
        //输出流  ，写回图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

           int len = 0 ;
           byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
