package com.kh.deneb.dao;

import com.kh.deneb.dto.BookDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SubBookDAO {

    @Select("select sub_book_seq.nextval from dual")
    int selectNextSeq();

    @Insert("insert into sub_book values (#{book_seq}, #{parent_bookcase_seq}, #{book_name}, #{book_color}, #{book_icon}, #{book_icon_color}, #{book_type}, #{video_id}, #{bookmark_order})")
    void insert(BookDTO book);

    @Select("select bookmark_order from sub_book where book_seq = #{value}")
    String selectBookmarkOrderBySeq(int book_seq);

    @Select("select * from sub_book where book_seq = #{value}")
    BookDTO selectAllBySeq(int book_seq);
}
