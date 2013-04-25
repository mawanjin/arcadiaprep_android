package com.arcadiaprep.app.arca.service;

import java.io.Serializable;

import com.arcadiaprep.app.arca.vo.DiscussionUserVO;

public interface IDiscussionFragment extends Serializable  {
	public void showDiscussionUserInfoDialog(DiscussionUserVO vo);
	/**
	 * 
	 * @param postDiscussionType defined in ConstantDiscussion
	 * @param discussionId if no value specify it to -1
	 * @param questionId if no value specify it to -1
	 */
	public void showDiscussionReplyDialog(int postDiscussionType, int discussionId,int questionId);
	public void showDiscussionDialog(int questionId);
}
