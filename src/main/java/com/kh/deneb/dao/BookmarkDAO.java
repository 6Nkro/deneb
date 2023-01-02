package com.kh.deneb.dao;

import com.kh.deneb.dto.BookmarkDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookmarkDAO {

    @Insert("insert into bookmark values (#{bookmark_seq}, #{parent_book_seq}, #{bookmark_name}, #{bookmark_url}, #{bookmark_memo}, 'icon.png')")
    void insert(BookmarkDTO bookmark);

    @Select("select bookmark_seq.nextval from dual")
    int selectNextSeq();

    @Select("select * from bookmark where bookmark_seq = #{value}")
    BookmarkDTO selectAllBySeq(int bookmark_seq);

    @Select("select parent_book_seq from bookmark where bookmark_seq = #{value}")
    int selectParentBySeq(int bookmark_seq);

    @Delete("delete bookmark where bookmark_seq = #{value}")
    int deleteBySeq(int bookmark_seq);

    @Select("select bookmark_seq from bookmark where parent_book_seq = #{value}")
    List<Integer> selectSeqByParentSeq(int parent_book_seq);

    @Update("update bookmark set bookmark_name = #{bookmark_name}, bookmark_url = #{bookmark_url}, bookmark_memo = #{bookmark_memo} where bookmark_seq = #{bookmark_seq}")
    void updateBySeq(BookmarkDTO bookmark);

    @Update("update bookmark set parent_book_seq = #{parent_book_seq} where bookmark_seq = #{bookmark_seq}")
    void updateParentBySeq(BookmarkDTO bookmark);

}
