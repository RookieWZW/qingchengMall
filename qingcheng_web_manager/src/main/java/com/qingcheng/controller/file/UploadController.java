package com.qingcheng.controller.file;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * @program: qingcheng_parent
 * @description: 文件上传
 * @author: RookieWZW
 * @create: 2019-11-16 13:48
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OSSClient ossClient;

    @PostMapping("/native")
    public String nativeUpload(@RequestParam("file")MultipartFile file){
        String path = request.getSession().getServletContext().getRealPath("img");

        String filePath = path + "/" + file.getOriginalFilename();
        File desFile = new File(filePath);
        if (!desFile.getParentFile().exists()){
            desFile.mkdirs();
        }
        try {
            file.transferTo(desFile);

        }catch (Exception e){
            e.printStackTrace();;
        }
        return "http://localhost:9101/img/"+file.getOriginalFilename();
    }

    @PostMapping("/oss")
    public String ossUpload(@RequestParam("file")MultipartFile file,String folder){

        String bucketName = "qingchengdianshang";
        String fileName = folder + "/" + UUID.randomUUID()+file.getOriginalFilename();
        try {
            ossClient.putObject(bucketName,fileName,file.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        return "https://"+bucketName+".oss-cn-beijing.aliyuncs.com/"+fileName;
    }
}