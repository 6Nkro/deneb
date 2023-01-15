package com.kh.deneb.dao;

import com.kh.deneb.dto.BookmarkDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface SubBookmarkDAO {

    @Select("select sub_bookmark_seq.nextval from dual")
    int selectNextSeq();

    @Insert("insert into sub_bookmark (bookmark_seq, parent_book_seq, bookmark_name, bookmark_url, bookmark_memo, video_id, video_title, video_channel) ${value}")
    void insertAll(String params);

    @Select("select * from sub_bookmark where bookmark_seq = #{value}")
    BookmarkDTO selectAllBySeq(int bookmark_seq);

    @Select("select bookmark_seq from sub_bookmark where parent_book_seq = #{value}")
    List<Integer> selectSeqByParentSeq(int parent_book_seq);

    @Delete("delete sub_bookmark where bookmark_seq = #{value}")
    void deleteBySeq(int bookmark_seq);

    @Select("select * from sub_bookmark where parent_book_seq in (${parent_book_seq}) order by decode (bookmark_seq ${order})")
    List<BookmarkDTO> selectAllByParent(HashMap<String, Object> params);
}
