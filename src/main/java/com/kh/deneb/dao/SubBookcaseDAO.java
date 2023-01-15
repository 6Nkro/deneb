package com.kh.deneb.dao;

import com.kh.deneb.dto.SubBookcaseDTO;
import org.apache.ibatis.annotations.Delete;
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

    @Select("select b.*, a.user_name, like_count, share_count, reply_count from sub_bookcase b " +
            "left join account a on b.account_seq = a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) r " +
            "on b.bookcase_seq = r.parent_bookcase_seq " +
            "where bookcase_seq = #{value}")
    SubBookcaseDTO selectAllBySeq(int bookcase_seq);
    @Select("select b.*, a.user_name, like_count, share_count, reply_count " +
            "from (select sub_bookcase.*, row_number() over(order by bookcase_seq desc) rn " +
            "from sub_bookcase where share_public = 'Y') b " +
            "left join account a on b.account_seq = a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) r " +
            "on b.bookcase_seq = r.parent_bookcase_seq " +
            "where (rn between #{minSeq} and #{maxSeq})" +
            "order by rn")
    List<SubBookcaseDTO> selectAllListBySeqRange(HashMap<String, Object> data);

    @Select("select b.*, a.user_name, like_count, share_count, reply_count " +
            "from (select sb.*, sa.user_name, row_number() over(order by bookcase_seq desc) rn " +
            "from sub_bookcase sb " +
            "left join account sa on sb.account_seq = sa.account_seq "+
            "where share_public = 'Y' " +
            "and ${search_type} like '%'||#{search_text}||'%') b " +
            "left join account a on b.account_seq = a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) r " +
            "on b.bookcase_seq = r.parent_bookcase_seq " +
            "where (rn between #{minSeq} and #{maxSeq})" +
            "order by rn")
    List<SubBookcaseDTO> selectSearchListBySeqRange(HashMap<String, Object> params);
    @Select("select count(*) from sub_bookcase where share_public = 'Y'")
    int selectCountPublic();
    @Select("select count(*) from sub_bookcase b " +
            "left join account a on b.account_seq = a.account_seq " +
            "where share_public = 'Y' " +
            "and ${search_type} like '%'||#{search_text}||'%'")
    int selectSearchCountPublic(HashMap<String, Object> params);
    @Select("select * from sub_bookcase where share_code = #{value}")
    SubBookcaseDTO selectAllByShareCode(String share_code);

    @Select("select b.*, like_count, share_count, reply_count from sub_bookcase b " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) r " +
            "on b.bookcase_seq = r.parent_bookcase_seq " +
            "where account_seq = #{value} " +
            "order by bookcase_seq desc")
    List<SubBookcaseDTO> selectAllListByAccountSeq(int account_seq);

    @Select("select b.*, a.user_name, l.like_count, s.share_count, r.reply_count " +
            "from (select sb.*, nvl(sl.like_count, 0) lc, nvl(ss.share_count, 0) sc, nvl(sr.reply_count, 0) rc, " +
            "row_number() over(order by sl.like_count, ss.share_count, sr.reply_count) rn " +
            "from sub_bookcase sb " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) sl " +
            "on sb.bookcase_seq = sl.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) ss " +
            "on sb.bookcase_seq = ss.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) sr " +
            "on sb.bookcase_seq = sr.parent_bookcase_seq " +
            "where share_public = 'Y') b " +
            "left join account a on b.account_seq = a.account_seq " +
            "left join (select parent_bookcase_seq, count(*) like_count " +
            "from like_count group by parent_bookcase_seq) l " +
            "on b.bookcase_seq = l.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) share_count " +
            "from share_count group by parent_bookcase_seq) s " +
            "on b.bookcase_seq = s.parent_bookcase_seq " +
            "left join (select parent_bookcase_seq, count(*) reply_count " +
            "from share_reply group by parent_bookcase_seq) r " +
            "on b.bookcase_seq = r.parent_bookcase_seq " +
            "where rn <= 10 " +
            "order by lc desc, sc desc, rc desc")
    List<SubBookcaseDTO> selectAllListSortByPopular();

    @Delete("delete sub_bookcase where bookcase_seq = #{value}")
    void deleteBySeq(int bookcase_seq);
}
