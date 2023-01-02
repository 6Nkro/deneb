package com.kh.deneb.dao;

import com.kh.deneb.dto.SubBookcaseDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface SubBookcaseDAO {

    @Select("select sub_bookcase_seq.nextval from dual")
    int selectNextSeq();

    @Select("select count(*) from sub_bookcase where share_code = #{value}")
    int selectCountByShareCode(String share_code);

    @Insert("insert into sub_bookcase values (#{bookcase_seq}, #{share_code}, #{account_seq}, #{bookcase_name}, #{book_order}, #{share_public}, #{share_contents}, #{share_tag}, sysdate)")
    void insert(SubBookcaseDTO subBookcase);

    @Select("select book_order from sub_bookcase where bookcase_seq = #{value}")
    String selectBookOrderBySeq(int bookcase_seq);

    @Select("select bookcase_seq from sub_bookcase where account_seq = #{value}")
    ArrayList<Integer> selectSeqByParent(int account_seq);

    @Select("select * from sub_bookcase b " +
            "left join account a on b.account_seq =  a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "where bookcase_seq = #{value}")
    SubBookcaseDTO selectAllBySeq(int bookcase_seq);
    @Select("select * from sub_bookcase where share_code = #{value}")
    SubBookcaseDTO selectAllByShareCode(String share_code);

    @Select("select b.*, a.user_name, like_count, share_count " +
            "from(select sub_bookcase.*, row_number() over(order by bookcase_seq desc) rn " +
            "from sub_bookcase where share_public = 'Y') b " +
            "left join account a on b.account_seq =  a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "where (rn between #{minSeq} and #{maxSeq})")
    List<SubBookcaseDTO> selectAllListBySeqRange(HashMap<String, Object> data);

    @Select("select count(*) from sub_bookcase where share_public = 'Y'")
    int selectCountPublic();

}
