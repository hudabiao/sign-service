package com.daibiao.signapp.controller;

import com.daibiao.signapp.dto.FileInfoDto;
import com.daibiao.signapp.enumerate.SignStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by Administrator on 2020-03-21.
 */
/**
 * FileController
 *
 * @description 文件操作
 * @author hudaibiao-1
 * @date 2020/9/23 16:55
 * @version v1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String PDF_SUFFIX = ".pdf";

    @Value("${upload.path}")
    private String savePath;

    /**
     * FileController
     *
     * @description 文件上传
     * @author hudaibiao-1
     * @param file 文件上传
     * @date 2020/9/23 16:56
     * @throws IOException IOException
     * @version v1.0.0
     */
    @PostMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info(file.getName());
        String uploadPath = this.getClass().getResource("/upload").getPath();
        String filename = "contract_unsigned.pdf";
        File saveFile = new File(uploadPath + File.separator + filename);
        file.transferTo(saveFile);
    }

    /**
     * FileController
     *
     * @description 文件下载
     * @author hudaibiao-1
     * @param fileInfoDto 文件信息
     * @date 2020/9/23 16:57
     * @throws UnsupportedEncodingException 不支持编码异常
     * @version v1.0.0
     */
    @GetMapping("/download")
    public void download(FileInfoDto fileInfoDto,
                         HttpServletResponse response) throws UnsupportedEncodingException {
        String name = new String(fileInfoDto.getName().getBytes("GBK"), StandardCharsets.ISO_8859_1);
        String path = savePath + File.separator + fileInfoDto.getLoginId() + File.separator + fileInfoDto.getContractId();
        outputFile(response, "attachment; filename = ",
                path, fileInfoDto.getStatus(), name + PDF_SUFFIX);
    }

    /**
     * FileController
     *
     * @description 文件预览
     * @author hudaibiao-1
     * @param fileInfoDto 文件信息
     * @date 2020/9/23 16:57
     * @version v1.0.0
     */
    @GetMapping("/preview")
    public void preview(FileInfoDto fileInfoDto, HttpServletResponse response) {
        String path = savePath + File.separator + fileInfoDto.getLoginId() + File.separator + fileInfoDto.getContractId();
        outputFile(response, "inline; filename = ", path, fileInfoDto.getStatus(), null);
    }

    /**
     * FileController
     *
     * @description 文件输出
     * @author hudaibiao-1
     * @param response 响应对象
     * @param type 文件操作类型
     * @param path 文件保存路径
     * @param status 状态
     * @param contractName 合同名称
     * @date 2020/9/23 16:59
     * @version v1.0.0
     */
    private void outputFile(HttpServletResponse response,
                            String type,
                            String path,
                            String status,
                            String contractName) {
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        String filename = "contract_unsigned.pdf";
        if (SignStatusEnum.YQM.getCode().equals(status)) {
            filename = "contract_signed.pdf";
        }
        if (contractName == null) {
            contractName = filename;
        }
        response.setHeader("Content-Disposition", type + contractName);
        path = path + File.separator + filename;
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
