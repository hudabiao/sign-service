package com.daibiao.signapp.controller;

import com.daibiao.signapp.enumerate.FileNameEnum;
import com.daibiao.signapp.enumerate.SignStatusEnum;
import com.daibiao.signapp.model.Contract;
import com.daibiao.signapp.service.ContractService;
import com.daibiao.signapp.util.FileUtil;
import com.daibiao.signapp.util.PdfUtil;
import com.daibiao.signapp.vo.SignData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.controller.ContractController
 * @description 签订协议controller
 * @date 2020-03-20 16:59:00
 */
@Slf4j
@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Value("${upload.path}")
    private String savePath;

    /**图片base64文件头数据*/
    private static final String IMG_HEADER = "data:image/png;base64,";


    /**
     * ContractController
     *
     * @description 根据登录ID查询-合同列表
     * @author hudaibiao-1
     * @param loginId 登录ID
     * @date 2020/9/23 16:41
     * @return List<Contract>
     * @version v1.0.0
     */
    @GetMapping("/{loginId}")
    public List<Contract> getContractListByLoginId(@PathVariable String loginId){
        return contractService.getContractListByLoginId(loginId);
    }

    /**
     * ContractController
     *
     * @description 删除合同
     * @author hudaibiao-1
     * @param id 合同id
     * @param loginId 登录ID
     * @date 2020/9/23 16:47
     * @throws IOException IO异常
     * @version v1.0.0
     */
    @DeleteMapping("/{id}/{loginId}")
    public void delete(@PathVariable("id") String id, @PathVariable("loginId") String loginId) throws IOException {
        contractService.deleteContract(id);
        String path = savePath + File.separator + loginId + File.separator + id;
        FileUtils.deleteDirectory(new File(path));
    }

    /**
     * ContractController
     *
     * @description 保存合同
     * @author hudaibiao-1
     * @param files 合同文件
     * @param userId 用户ID
     * @param loginId 登录ID
     * @date 2020/9/23 16:48
     * @throws IOException IO异常
     * @version v1.0.0
     */
    @PostMapping
    public void saveContract(@RequestParam("files") MultipartFile[] files,
                             @RequestParam("userId") String userId,
                             @RequestParam("loginId") String loginId) throws IOException {
        List<String> contractNameList = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename).collect(Collectors.toList());
        List<Contract> contractList = contractService.saveContract(contractNameList,userId);
        // 保存文件
        List<String> contractIds = contractList.stream().map(Contract::getId).collect(Collectors.toList());
        for(int i=0;i< contractIds.size();i++){
            String uploadPath = savePath + File.separator + loginId + File.separator + contractIds.get(i);
            FileUtil.saveFile(uploadPath,files[i]);
        }
    }

    /**
     * ContractController
     *
     * @description 签名
     * @author hudaibiao-1
     * @param signData 签名数据
     * @date 2020/9/23 16:54
     * @throws IOException IO异常
     * @version v1.0.0
     */
    @PostMapping("/sign")
    public void sign(@RequestBody SignData signData) throws IOException {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        String imgData = signData.getBase64ImgData();
        // 将文件头base64信息移除
        if (imgData.contains(IMG_HEADER)) {
            imgData = imgData.replace(IMG_HEADER, "");
        }
        byte[] imgByte = base64Decoder.decodeBuffer(imgData);
        String path = savePath + File.separator + signData.getLoginId() + File.separator + signData.getContractId();
        String sourceFilename = FileNameEnum.UNSIGNED_FILENAME.getName();
        String targetFilename = FileNameEnum.SIGNED_FILENAME.getName();
        PdfUtil.signImgToPdf(path, sourceFilename, targetFilename, imgByte);
        contractService.updateStatus(signData.getContractId(), SignStatusEnum.YQM.getCode());
    }
}
