package com.daibiao.signapp.dao;

import com.daibiao.signapp.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.dao.ContractRepository
 * @date 2020-03-20 16:52:00
 */
public interface ContractRepository extends JpaRepository<Contract, String> {

    /**
     * ContractRepository
     *
     * @description 根据登录id查询合同
     * @author hudaibiao-1
     * @param loginId 登录id
     * @date 2020/9/23 18:05
     * @return 合同列表
     * @version v1.0.0
     */
    @Query("select c from Contract c left join User u on c.userId = u.id where u.loginId = :loginId")
    List<Contract> queryContractsByUserLoginId(@Param("loginId") String loginId);

    /**
     * ContractRepository
     *
     * @description 更新合同状态
     * @author hudaibiao-1
     * @param id 合同id
     * @param status 状态
     * @param updateDatetime 更新时间
     * @date 2020/9/23 18:08
     * @version v1.0.0
     */
    @Modifying
    @Query("update Contract u set status = :status, updateDatetime = :updateDatetime where id = :id")
    void updateStatus(@Param("id") String id, @Param("status") String status, @Param("updateDatetime") Date updateDatetime);
}
