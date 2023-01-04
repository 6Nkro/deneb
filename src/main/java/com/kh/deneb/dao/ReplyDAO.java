package com.kh.deneb.dao;

import com.kh.deneb.dto.ReplyDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReplyDAO {

    @Insert("insert into share_reply values (share_reply_seq.nextval, #{parent_bookcase_seq}, #{account_seq}, #{reply_contents}, sysdate)")
    void insert(ReplyDTO reply);

    @Select("select r.*, a.user_name from share_reply r " +
            "left join account a on r.account_seq = a.account_seq " +
            "where parent_bookcase_seq = #{value} order by reply_seq")
    List<ReplyDTO> selectAllListBySeq(int parent_bookcase_seq);

    @Delete("delete share_reply where reply_seq = #{value}")
    void delete(int reply_seq);
}
