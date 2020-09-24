package com.daibiao.signapp.service;

import com.daibiao.signapp.dao.ContractRepository;
import com.daibiao.signapp.enumerate.SignStatusEnum;
import com.daibiao.signapp.model.Contract;
import com.daibiao.signapp.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.service.ContractService
 * @description ContractService
 * @date 2020-03-18 17:20:00
 */
@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;

    /**
     * ContractService
     *
     * @description 删除合同
     * @author hudaibiao-1
     * @param id 合同id
     * @date 2020/9/23 19:53
     * @version v1.0.0
     */
    public void deleteContract(String id) {
        contractRepository.deleteById(id);
    }

    /**
     * ContractService
     *
     * @description 保存合同
     * @author hudaibiao-1
     * @param nameList 合同列表
     * @param userId 用户id
     * @date 2020/9/23 19:53
     * @return 合同列表
     * @version v1.0.0
     */
    public List<Contract> saveContract(List<String> nameList, String userId) {
        List<Contract> contractList = new ArrayList<>();
        Contract contract = null;
        for (String contractName : nameList) {
            contract = new Contract();
            contract.setId(UUIDUtil.generateId());
            contract.setUserId(userId);
            contract.setName(contractName);
            contract.setStatus(SignStatusEnum.WQM.getCode());
            contract.setCreateDatetime(new Date());
            contract.setUpdateDatetime(new Date());
            contractList.add(contract);
        }
        return contractRepository.saveAll(contractList);
    }

    /**
     * ContractService
     *
     * @description 根据登录id查询合同列表
     * @author hudaibiao-1
     * @param loginId 登录id
     * @date 2020/9/23 19:54
     * @return 合同列表
     * @version v1.0.0
     */
    public List<Contract> getContractListByLoginId(String loginId) {
        return contractRepository.queryContractsByUserLoginId(loginId);
    }

    /**
     * ContractService
     *
     * @description 更新状态
     * @author hudaibiao-1
     * @param id 合同id
     * @param status 状态
     * @date 2020/9/23 20:03
     * @version v1.0.0
     */
    @Transactional(rollbackOn = Exception.class)
    public void updateStatus(String id, String status) {
        contractRepository.updateStatus(id, status, new Date());
    }
}
