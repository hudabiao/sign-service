package com.daibiao.signapp.controller;

import com.daibiao.signapp.model.Contract;
import com.daibiao.signapp.util.FileUtil;
import com.daibiao.signapp.vo.ParamsVO;
import com.daibiao.signapp.model.Result;
import com.daibiao.signapp.model.User;
import com.daibiao.signapp.service.UserService;
import com.daibiao.signapp.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.controller.UserController
 * @description 用户controller
 * @date 2020-03-20 16:59:00
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String savePath;

    /**
     * UserController
     *
     * @description 查询所有用户
     * @author hudaibiao-1
     * @date 2020/9/23 17:56
     * @return 响应结果
     * @version v1.0.0
     */
    @GetMapping
    public ResponseEntity<Result> getAllUser() {
        return ResponseEntity.ok(ResultUtil.success(userService.getAllUserByCondition()));
    }

    /**
     * UserController
     *
     * @description 分页查询
     * @author hudaibiao-1
     * @param paramsVO 查询参数
     * @date 2020/9/23 17:58
     * @return 响应结果
     * @version v1.0.0
     */
    @GetMapping("/page")
    public ResponseEntity<Result> getUserByPage(ParamsVO paramsVO) {
        return ResponseEntity.ok(ResultUtil.success(userService.selectUserByPage(paramsVO)));
    }

    /**
     * UserController
     *
     * @description 保存合同
     * @author hudaibiao-1
     * @param files 文件
     * @param user 用户
     * @date 2020/9/23 18:01
     * @throws IOException IO异常
     * @version v1.0.0
     */
    @PostMapping
    public void saveContract(@RequestParam("files") MultipartFile[] files,
                             User user) throws IOException {
        List<String> contractNameList = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename).collect(Collectors.toList());
        user = userService.saveUser(user,contractNameList);
        // 保存文件
        String loginId = user.getLoginId();
        List<String> contractIds = user.getContractList().stream().map(Contract::getId).collect(Collectors.toList());
        for(int i=0;i<contractIds.size();i++){
            String uploadPath = savePath + File.separator + loginId + File.separator + contractIds.get(i);
            FileUtil.saveFile(uploadPath,files[i]);
        }
    }

    /**
     * UserController
     *
     * @description 删除用户
     * @author hudaibiao-1
     * @param id 用户id
     * @param loginId 登录id
     * @date 2020/9/23 18:02
     * @throws IOException IO异常
     * @version v1.0.0
     */
    @DeleteMapping("/{id}/{loginId}")
    public void delete(@PathVariable("id")String id, @PathVariable("loginId")String loginId) throws IOException {
        userService.deleteUser(id);
        String path = savePath + File.separator + loginId;
        FileUtils.deleteDirectory(new File(path));
    }
}
