package com.kh.deneb.dao;

import com.kh.deneb.dto.BookmarkDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SubBookmarkDAO {

    @Select("select sub_bookmark_seq.nextval from dual")
    int selectNextSeq();

    @Insert("insert into sub_bookmark values (#{bookmark_seq}, #{parent_book_seq}, #{bookmark_name}, #{bookmark_url}, #{bookmark_memo}, #{video_id}, #{video_title}, #{video_channel})")
    void insert(BookmarkDTO bookmark);

    @Select("select * from sub_bookmark where bookmark_seq = #{value}")
    BookmarkDTO selectAllBySeq(int bookmark_seq);



}
