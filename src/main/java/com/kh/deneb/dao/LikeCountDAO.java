package com.kh.deneb.dao;

import com.kh.deneb.dto.BookcaseDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LikeCountDAO {

    @Select("select count(*) from like_count where account_seq = #{account_seq} and parent_bookcase_seq = #{bookcase_seq}")
    int selectCountBySeq(BookcaseDTO bookcase);

    @Insert("insert into like_count values(like_count_seq.nextval, #{bookcase_seq}, #{account_seq})")
    int insert(BookcaseDTO bookcase);

    @Delete("delete like_count where parent_bookcase_seq = #{bookcase_seq} and account_seq = #{account_seq}")
    int delete(BookcaseDTO bookcase);

    @Delete("delete like_count where parent_bookcase_seq = #{value}")
    void deleteBySeq(int bookcase_seq);
}