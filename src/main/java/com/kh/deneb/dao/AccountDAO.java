package com.kh.deneb.dao;

import com.kh.deneb.dto.AccountDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface AccountDAO {
    @Insert("insert into account values(#{account_seq}, #{user_email}, #{user_name}, #{user_pw}, sysdate, #{bookcase_order})")
    void insert(AccountDTO account);

    @Delete("delete from account where user_seq = #{value}")
    int delete(int seq);

    @Select("select * from account")
    List<AccountDTO> selectAll();

    @Select("select count(*) from account where user_email = #{value}")
    int selectCountByEmail(String user_email);

    @Select("select count(*) from account where user_name = #{value}")
    int selectCountByName(String user_name);

    @Select("select account_seq.nextval from dual")
    int selectNextSeq();

    @Select("select * from account where user_email = #{user_email} and user_pw = #{user_pw}")
    AccountDTO selectAllByEmailAndPw(AccountDTO data);

    @Select("select account_seq from account where user_email = #{user_email} and user_pw = #{encrypted_pw}")
    int selectSeqByEmailAndPw(HashMap<String, Object> data);

    @Select("select * from account where account_seq = #{value}")
    AccountDTO selectAllBySeq(int seq);

    @Select("select bookcase_order from account where account_seq = #{value}")
    String selectBookcaseOrderBySeq(int seq);

    @Update("update account set bookcase_order = #{bookcase_order} where account_seq = #{account_seq}")
    int updateOrderBySeq(AccountDTO account);

    @Select("select account_seq from account where user_name = #{value}")
    String selectSeqByName(String user_name);
}