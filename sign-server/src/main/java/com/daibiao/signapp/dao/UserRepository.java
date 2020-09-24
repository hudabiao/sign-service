package com.daibiao.signapp.dao;

import com.daibiao.signapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.dao.UserRepository
 * @date 2020-03-20 16:52:00
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * UserRepository
     *
     * @description 根据登录id查询用户
     * @author hudaibiao-1
     * @param loginId 登录id
     * @date 2020/9/23 18:09
     * @return 用户
     * @version v1.0.0
     */
    @Query("select u from User u where loginId = :loginId")
    User getUserByLoginId(@Param("loginId") String loginId);

    /**
     * UserRepository
     *
     * @description 查询登录id重名次数
     * @author hudaibiao-1
     * @param loginId 登录id
     * @date 2020/9/23 18:14
     * @return 次数
     * @version v1.0.0
     */
    @Query("select count(u.loginId) from User u where loginId like CONCAT(:loginId,'%')")
    Integer countSimilarLoginIdByLoginId(@Param("loginId") String loginId);
}
