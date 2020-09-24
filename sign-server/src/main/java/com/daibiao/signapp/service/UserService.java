package com.daibiao.signapp.service;

import com.alibaba.fastjson.JSON;
import com.daibiao.signapp.dao.UserRepository;
import com.daibiao.signapp.exception.BaseException;
import com.daibiao.signapp.util.UUIDUtil;
import com.daibiao.signapp.vo.ParamsVO;
import com.daibiao.signapp.enumerate.RoleEnum;
import com.daibiao.signapp.enumerate.SignStatusEnum;
import com.daibiao.signapp.model.Contract;
import com.daibiao.signapp.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.service.UserService
 * @description UserService
 * @date 2020-03-18 17:20:00
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * UserService
     *
     * @description 查询全部用户
     * @author hudaibiao-1
     * @date 2020/9/23 20:04
     * @return 用户列表
     * @version v1.0.0
     */
    public List<User> getAllUserByCondition() {
        User user = new User();
        user.setRole(RoleEnum.XY.getCode());
        Example<User> example = Example.of(user);
        return userRepository.findAll(example);
    }

    /**
     * UserService
     *
     * @description 分页查询用户列表
     * @author hudaibiao-1
     * @param paramsVO 查询参数
     * @date 2020/9/23 20:05
     * @return 用户分页
     * @version v1.0.0
     */
    public Page<User> selectUserByPage(ParamsVO paramsVO) {
        User user = JSON.parseObject(paramsVO.getEntity(), User.class);
        if (user == null) {
            user = new User();
        } else if (StringUtils.isBlank(user.getUsername())) {
            user.setUsername(null);
        }
        user.setRole(RoleEnum.XY.getCode());
        Example<User> example = Example.of(user);
        Sort sort = Sort.by(Sort.Direction.DESC, "createDatetime");
        return userRepository.findAll(example, PageRequest.of(paramsVO.getPage(), paramsVO.getSize(), sort));
    }

    /**
     * UserService
     *
     * @description 查询用户
     * @author hudaibiao-1
     * @param loginid 登录id
     * @date 2020/9/23 20:12
     * @return 用户
     * @version v1.0.0
     */
    public User selectUserByLoginId(String loginid) {
        return userRepository.getUserByLoginId(loginid);
    }

    /**
     * UserService
     *
     * @description 查询用户
     * @author hudaibiao-1
     * @param id id
     * @date 2020/9/23 20:12
     * @return 用户
     * @version v1.0.0
     */
    public User selectUserById(String id) {
        return userRepository.getOne(id);
    }

    /**
     * UserService
     *
     * @description 保存用户及合同
     * @author hudaibiao-1
     * @param user 用户
     * @param contractNameList 合同
     * @date 2020/9/23 20:13
     * @return 用户
     * @version v1.0.0
     */
    public User saveUser(User user, List<String> contractNameList) {
        String loginId = user.getLoginId();
        Integer similarLoginIdCount = userRepository.countSimilarLoginIdByLoginId(loginId);
        if (similarLoginIdCount != null && similarLoginIdCount > 0) {
            throw new BaseException("该手机号已存在！");
        }
        String userId = UUID.randomUUID().toString().replaceAll("-", "");
        String password = user.getPhone().substring(5);
        user.setPassword(password);
        user.setId(userId);
        user.setRole(RoleEnum.XY.getCode());
        user.setCreateDatetime(new Date());
        user.setUpdateDatetime(new Date());
        // 构造合同信息
        List<Contract> contractList = contractNameList.stream().map(name -> {
            Contract contract = new Contract();
            contract.setName(name);
            contract.setUserId(userId);
            contract.setId(UUIDUtil.generateId());
            contract.setStatus(SignStatusEnum.WQM.getCode());
            contract.setCreateDatetime(new Date());
            contract.setUpdateDatetime(new Date());
            return contract;
        }).collect(Collectors.toList());
        user.setContractList(contractList);
        userRepository.save(user);
        return user;
    }

    /**
     * UserService
     *
     * @description 删除用户
     * @author hudaibiao-1
     * @param id id
     * @date 2020/9/23 20:14
     * @version v1.0.0
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
