package com.kh.deneb.dao;

import com.kh.deneb.dto.BookcaseDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BookcaseDAO {
    @Insert("insert into bookcase values (#{bookcase_seq}, #{account_seq}, '나의 책장', '[]')")
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
}
