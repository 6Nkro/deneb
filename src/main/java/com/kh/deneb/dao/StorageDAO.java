package com.kh.deneb.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Mapper
@Repository
public interface StorageDAO {
    @Insert("insert into storage values (#{storage_no}, #{account_no}, 'Storage', 'N')")
    int insertDefault(HashMap<String, Object> data);

    @Select("select storage_seq.nextval from dual")
    int selectNextSeq();
}
