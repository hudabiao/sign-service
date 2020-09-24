package com.daibiao.signapp.controller;

import com.daibiao.signapp.annotion.PassToken;
import com.daibiao.signapp.model.Result;
import com.daibiao.signapp.model.User;
import com.daibiao.signapp.service.UserService;
import com.daibiao.signapp.util.TokenUtil;
import com.daibiao.signapp.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.controller.LoginController
 * @description 登录
 * @date 2020-03-19 16:21:00
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * LoginController
     *
     * @description 登录
     * @author hudaibiao-1
     * @param user 用户信息
     * @date 2020/9/23 17:54
     * @return Result 返回结果
     * @version v1.0.0
     */
    @PassToken
    @PostMapping
    public ResponseEntity<Result> login(@RequestBody User user) {
        String loginId = user.getLoginId();
        String password = user.getPassword();

        User user1 = userService.selectUserByLoginId(loginId);
        if (user1 == null) {
            return ResponseEntity.ok(new Result<>("02", "用户不存在", null));
        }
        if (!password.equals(user1.getPassword())) {
            return ResponseEntity.ok(new Result<>("02", "密码错误", null));
        }
        String token = TokenUtil.createJwtToken(user1.getId());
        UserVO userVO = new UserVO();
        userVO.setToken(token);
        BeanUtils.copyProperties(user1, userVO);
        return ResponseEntity.ok(new Result<>("01", "登录成功", userVO));
    }
}
