package com.wzh.uploadimg.upload;

import com.wzh.uploadimg.utils.FileUtil;
import com.wzh.uploadimg.utils.Msg;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class UploadCtrl {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${wzh_image.path}")
    private String baseSavePath;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Msg upload(HttpServletRequest request) {

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        Map<String, MultipartFile> fileMap = multipartHttpServletRequest.getFileMap();
        List<String> failList = new ArrayList<>();
        List<String> okList = new ArrayList<>();
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                long fileSize = entry.getValue().getSize();
                String fileName = entry.getValue().getOriginalFilename();
                int extIndex = fileName.lastIndexOf(".");
                if (extIndex == -1) {
                    failList.add(fileName);
                    continue;
                }
                String ext = fileName.substring(extIndex+1);
                String fileBasePath = FileUtil.makeSaveDir(fileName, baseSavePath)+"/";
                String pathName = fileBasePath+fileName;
                String thumbName = fileBasePath+"thumb."+ext;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = new Date();
                String timestamp = simpleDateFormat.format(d);
                String sql = "insert into image(name,size,extension,path,thumbpath,time) values (?,?,?,?,?,?)";
                int updateCount = jdbcTemplate.update(sql,fileName,fileSize,ext,pathName,thumbName, timestamp);

                if (updateCount == 1) {
                    try {
                        byte[] bytes = entry.getValue().getBytes();
                        Files.write(Paths.get(pathName),bytes);
                        Thumbnails.of(entry.getValue().getInputStream())
                                .scale(1f)
                                .outputQuality(0.5f)
                                .toFile(thumbName);
                        okList.add(fileName);
                    } catch (Exception ex) {
                        failList.add(fileName);
                    }
                }else {
                    failList.add(fileName);
                }
            }
        }
        Map<String, List<String>> ret = new HashMap<>();
        ret.put("success", okList);
        ret.put("fail", failList);
        return Msg.success(okList.size()>0).add(ret);
    }
}
