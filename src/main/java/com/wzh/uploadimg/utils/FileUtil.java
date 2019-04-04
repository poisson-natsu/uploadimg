package com.wzh.uploadimg.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @作者 yan
 * @创建日期 2017-3-16 14:24:04
 * @版本 V1.0
 * @描述
 */
public class FileUtil {

    /**
     * @param fileName 文件名称
     * @param baseSavePath 存储文件的根目录
     * @return 随机生成文件存储目录
     */
    public static String makeSaveDir(String fileName, String baseSavePath) {
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = fileName.hashCode();
        int dir1 = hashcode & 0xf;  //0--15
        int dir2 = (hashcode & 0xf0) >> 4;  //0-15

        //构造新的保存目录
        String dir = baseSavePath + "/" + dir1 + "/" + dir2;
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }

        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean exists(String filePath) {
        File file = new File(filePath);

        return file.exists();
    }

    /**
     * InputStream to byte[]
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 2];

        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }

    /**
     * byte[] to file
     * @param buf
     * @param fileName
     */
    public static void byte2File(byte[] buf, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        
        try {
            File file = new File(fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            bos.write(buf);
        } catch (IOException e) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if(fos!=null){
                        fos.close();
                    }
                } catch (IOException e) {
                    Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public static Date getFileCreateTime(String fullFileName){
        Path path= Paths.get(fullFileName);
        BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
        BasicFileAttributes attr;

        try {
            attr = basicview.readAttributes();
            Date createDate = new Date(attr.creationTime().toMillis());
            return createDate;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.set(1970, 0, 1, 0, 0, 0);

        return cal.getTime();
    }
}