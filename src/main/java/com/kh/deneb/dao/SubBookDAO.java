package com.kh.deneb.dao;

import com.kh.deneb.dto.BookDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubBookDAO {

    @Select("select sub_book_seq.nextval from dual")
    int selectNextSeq();

    @Insert("insert into sub_book values (#{book_seq}, #{parent_bookcase_seq}, #{book_name}, #{book_color}, #{book_icon}, #{book_icon_color}, #{book_type}, #{bookmark_order})")
    void insert(BookDTO book);

    @Select("select bookmark_order from sub_book where book_seq = #{value}")
    String selectBookmarkOrderBySeq(int book_seq);

    @Select("select * from sub_book where book_seq = #{value}")
    BookDTO selectAllBySeq(int book_seq);

    @Select("select book_seq from sub_book where parent_bookcase_seq = #{value}")
    List<Integer> selectSeqByParentSeq(int parent_bookcase_seq);

    @Delete("delete sub_book where book_seq = #{value}")
    void deleteBySeq(int book_seq);
}
