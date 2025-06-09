package com.example.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.entity.User;
import com.example.system.domain.vo.UserVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    List<UserVo> getUserListVo();

    List<User> getUserList();

    void updatePwd(@Param("account") String account, @Param("password") String password);

    User getUserByAccount(@Param("account") String account);

    void insertUser(@Param("user") User user);

    @MapKey("year")
    List<Map<String, Integer>> getUserInsertCount();
}
