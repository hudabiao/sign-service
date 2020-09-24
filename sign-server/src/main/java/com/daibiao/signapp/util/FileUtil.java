package com.daibiao.signapp.util;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.daibiao.signapp.enumerate.FileNameEnum;
import com.daibiao.signapp.exception.BaseException;

/**
 * FileUtil
 *
 * @author hudaibiao-1
 * @version v1.0.0
 * @description 文件操作
 * @date 2020-09-23 21:04:00
 */
public class FileUtil {

    /**
     * FileUtil
     *
     * @description 保存文件
     * @author hudaibiao-1
     * @param uploadPath 保存路径
     * @param file 上传文件
     * @date 2020/9/23 21:07
     * @throws IOException IO异常
     * @version v1.0.0
     */
    public static void saveFile(String uploadPath, MultipartFile file) throws IOException {
        File direct = new File(uploadPath);
        if (!direct.mkdirs()) {
            throw new BaseException("创建文件夹失败");
        }
        String filename = FileNameEnum.UNSIGNED_FILENAME.getName();
        File saveFile = new File(uploadPath + File.separator + filename);
        file.transferTo(saveFile);
    }

}
