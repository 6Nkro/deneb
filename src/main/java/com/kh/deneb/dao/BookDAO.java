package com.kh.deneb.dao;

import com.kh.deneb.dto.BookDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface BookDAO {

    @Insert("insert into book values (#{book_seq}, #{parent_bookcase_seq}, #{book_name}, #{book_color}, #{book_icon}, #{book_icon_color}, #{book_type}, 0, 'Y', '[]')")
    void insert(BookDTO book);

    @Select("select book_seq.nextval from dual")
    int selectNextSeq();

    @Select("select * from book where book_seq = #{value}")
    BookDTO selectAllBySeq(int book_seq);

    @Select("select bookmark_order from book where book_seq = #{value}")
    String selectBookmarkOrderBySeq(int book_seq);

    @Update("update book set bookmark_order = #{bookmark_order} where book_seq = #{book_seq}")
    int updateOrderBySeq(BookDTO book);

    @Select("select parent_bookcase_seq from book where book_seq = #{value}")
    int selectParentBySeq(int book_seq);

    @Delete("delete book where book_seq = #{value}")
    int deleteBySeq(int book_seq);

    @Select("select book_seq from book where parent_bookcase_seq = #{value}")
    List<Integer> selectSeqByParentSeq(int parent_bookcase_seq);

    @Update("update book set book_open = #{book_open} where book_seq = #{book_seq}")
    int updateStateBySeq(BookDTO book);

    @Update("update book set book_name = #{book_name}, book_color = #{book_color}, book_icon = #{book_icon}, book_icon_color = #{book_icon_color} where book_seq = #{book_seq}")
    void updateBySeq(BookDTO book);

    @Update("update book set parent_bookcase_seq = #{parent_bookcase_seq} where book_seq = #{book_seq}")
    void updateParentBySeq(BookDTO book);

    @Insert("insert into book (book_seq, parent_bookcase_seq, book_name, book_color, book_icon, book_icon_color, book_type, video_index, book_open, bookmark_order) ${value}")
    void insertAll(String params);

    @Update("update book set video_index = #{video_index} where book_seq = #{book_seq}")
    int updateIndexBySeq(BookDTO book);

    @Select("select * from book where parent_bookcase_seq in (${parent_bookcase_seq}) order by decode (book_seq ${order})")
    List<BookDTO> selectAllByParent(HashMap<String, Object> params);
}