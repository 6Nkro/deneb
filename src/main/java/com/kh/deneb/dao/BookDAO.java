package com.kh.deneb.dao;

import com.kh.deneb.dto.BookDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookDAO {

    @Insert("insert into book values (#{book_seq}, #{parent_bookcase_seq}, #{book_name}, #{book_color}, #{book_icon}, #{book_icon_color}, #{book_type}, #{video_id}, 0, 'Y', '[]')")
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

    @Update("update book set book_name = #{book_name}, book_color = #{book_color}, book_icon = #{book_icon}, book_icon_color = #{book_icon_color}, video_id = #{video_id} where book_seq = #{book_seq}")
    void updateBySeq(BookDTO book);

    @Update("update book set parent_bookcase_seq = #{parent_bookcase_seq} where book_seq = #{book_seq}")
    void updateParentBySeq(BookDTO book);

    @Insert("insert into book values (#{book_seq}, #{parent_bookcase_seq}, #{book_name}, #{book_color}, #{book_icon}, #{book_icon_color}, #{book_type}, #{video_id}, 0, 'Y', #{bookmark_order})")
    void insertClone(BookDTO book);

    @Update("update book set video_time = #{video_time} where book_seq = #{book_seq}")
    int updateVideoTimeBySeq(BookDTO book);
}