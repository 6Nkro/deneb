package com.kh.deneb.dao;

import com.kh.deneb.dto.AccountDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface AccountDAO {
    @Insert("insert into account values(#{account_no}, #{user_email}, #{user_name}, #{user_pw}, sysdate, #{storage_order})")
    int insert(HashMap<String, Object> data);

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
    AccountDTO getAccount(HashMap<String, Object> data);
}