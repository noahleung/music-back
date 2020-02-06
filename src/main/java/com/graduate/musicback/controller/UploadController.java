package com.graduate.musicback.controller;

import com.graduate.musicback.utils.SnowflakeIdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Api(description = "图片的上传",tags = "工作人员接口")
@RestController
public class UploadController {

    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;

    @Value("${file.picture}")
    private String filePicture;

    @Value("${file.music}")
    private String fileMusic;

    @ApiOperation("上传图片专用接口")
    @PostMapping("/picture/upload")
    public String pictureUplooad(MultipartFile file){

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名

        fileName = snowflakeIdWorker.nextId() + suffixName; // 新文件名
        File dest = new File(filePicture + fileName);
        if (!dest.getParentFile().exists()) {
            // 若不存在该文件夹，则创建一个
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        String filename = "http://localhost:9090/file/picture/" + fileName;//返回的文件URL
        String filename = "http://116.62.37.59:9090/file/picture/" + fileName;//返回的文件URL

        return filename;
    }


    @ApiOperation("上传图片专用接口")
    @PostMapping("/music/upload")
    public String musicUplooad(MultipartFile file){

        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名

        fileName = snowflakeIdWorker.nextId() + suffixName; // 新文件名
        File dest = new File(fileMusic + fileName);
        if (!dest.getParentFile().exists()) {
            // 若不存在该文件夹，则创建一个
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = "http://116.62.37.59:9090/file/music/" + fileName;//返回的文件URL

        return filename;
    }


}
