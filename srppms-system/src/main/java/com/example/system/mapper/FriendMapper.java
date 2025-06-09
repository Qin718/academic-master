package com.example.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.system.domain.bo.FriendBo;
import com.example.system.domain.entity.Friend;
import com.example.system.domain.vo.FriendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (SysFriend)表数据库访问层
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    List<FriendVo> getFriendList(@Param("account") String account);

    List<FriendVo> getListApp(@Param("userId") int userId);

    void insertFriendListId(@Param("list") List<Friend> friendList);

    void insertFriend(Friend friend);

    void deleteFriend(@Param("bo") FriendBo bo);

    void friendUpdateList(@Param("list") List<Friend> list);
}

