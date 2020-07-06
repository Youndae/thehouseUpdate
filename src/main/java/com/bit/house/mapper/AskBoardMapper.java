package com.bit.house.mapper;

import com.bit.house.domain.AskBoardVO;
import com.bit.house.domain.CommentVO;
import com.bit.house.domain.Criteria;
import org.apache.ibatis.annotations.Mapper;

import javax.xml.stream.events.Comment;
import java.util.List;

@Mapper
public interface AskBoardMapper {
    //게시글 목록
    public List<AskBoardVO> askBoardList(Criteria cri) throws Exception;
    int listCountCriteria(Criteria cri) throws Exception;
    //검색
    public List<AskBoardVO> searchList(String keyword, int pageStart, int perPageNum ) throws Exception;
    int searchListCountCriteria(Criteria cri, String keyword) throws Exception;

    //게시글 상세
    public AskBoardVO askDetail(int askBoardNo) throws Exception;
    int askCommentCount(int askBoardNo) throws Exception;

    //게시글 작성
    public int insertAsk(AskBoardVO askBoardVO) throws Exception;

    //게시글 수정
    public int askUpdate(AskBoardVO askBoardVO) throws Exception;

    //게시글 삭제
    public int askDelete(int askBoardNo) throws Exception;

    //답글 작성
    public int askReplyUp(AskBoardVO askBoardVO) throws Exception;

    public int askReply(AskBoardVO askBoardVO) throws Exception;

    //댓글
    public List<CommentVO> askComment(int askBoardNo) throws Exception;

    public void insertAskComment(CommentVO commentVO) throws Exception;





}
