package com.kh.deneb.service;

import com.kh.deneb.dao.ReplyDAO;
import com.kh.deneb.dto.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ReplyService {

    @Autowired
    ReplyDAO replyDAO;
    public List<ReplyDTO> sendReply(ReplyDTO reply) {
        replyDAO.insert(reply);
        return getReplyList(reply.getParent_bookcase_seq());
    }

    public List<ReplyDTO> getReplyList(int parent_bookcase_seq) {
        return replyDAO.selectAllListBySeq(parent_bookcase_seq);
    }

    public List<ReplyDTO> deleteReply(ReplyDTO reply) {
        replyDAO.delete(reply.getReply_seq());
        return getReplyList(reply.getParent_bookcase_seq());
    }

    public HashMap<String, Object> getCommentList(int page) {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> commentList = new HashMap<>();

        int count = replyDAO.selectCommentCount();
        if (page == -1 ) {
            page = (int) Math.ceil(count / 10d);
        }

        params.put("minSeq", page * 10 - 9);
        params.put("maxSeq", page * 10);

        commentList.put("commentList", replyDAO.selectAllListBySeqRange(params));
        commentList.put("count", count);

        return commentList;
    }
}