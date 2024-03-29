package com.kh.deneb.dao;

import com.kh.deneb.dto.BookcaseDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BookcaseDAO {
    @Insert("insert into bookcase values (#{bookcase_seq}, #{account_seq}, '빈 페이지', '[]')")
    void insertDefault(BookcaseDTO bookcase);

    @Insert("insert into bookcase values (#{bookcase_seq}, #{account_seq}, #{bookcase_name}, '[]')")
    void insert(BookcaseDTO bookcase);

    @Select("select bookcase_seq.nextval from dual")
    int selectNextSeq();

    @Select("select * from bookcase where bookcase_seq = #{value}")
    BookcaseDTO selectAllBySeq(int bookcase_seq);

    @Select("select book_order from bookcase where bookcase_seq = #{value}")
    String selectBookOrderBySeq(int bookcase_seq);

    @Update("update bookcase set book_order = #{book_order} where bookcase_seq = #{bookcase_seq}")
    int updateOrderBySeq(BookcaseDTO bookcase);

    @Select("select account_seq from bookcase where bookcase_seq = #{value}")
    int selectParentBySeq(int bookcase_seq);

    @Delete("delete bookcase where bookcase_seq = #{value}")
    int deleteBySeq(int bookcase_seq);

    @Update("update bookcase set bookcase_name = #{bookcase_name} where bookcase_seq = #{bookcase_seq}")
    void updateNameBySeq(BookcaseDTO bookcase);

    @Insert("insert into bookcase values (#{bookcase_seq}, #{account_seq}, #{bookcase_name}, #{book_order})")
    void insertClone(BookcaseDTO bookcase);

    @Select("select * from bookcase where account_seq = #{account_seq} order by decode (bookcase_seq ${order})")
    List<BookcaseDTO> selectAllByParent(HashMap<String, Object> params);
}