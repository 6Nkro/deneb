package com.kh.deneb.dao;

import com.kh.deneb.dto.BookcaseDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShareCountDAO {

    @Select("select count(*) from share_count where account_seq = #{account_seq} and parent_bookcase_seq = #{bookcase_seq}")
    int selectCountBySeq(BookcaseDTO bookcase);

    @Insert("insert into share_count values(share_count_seq.nextval, #{bookcase_seq}, #{account_seq})")
    void insert(BookcaseDTO bookcase);
}