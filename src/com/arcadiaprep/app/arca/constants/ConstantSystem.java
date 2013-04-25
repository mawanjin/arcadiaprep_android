package com.arcadiaprep.app.arca.constants;

import java.io.File;

import com.arcadiaprep.app.scratchpad.component.WorkSpace;

import android.os.Environment;

public class ConstantSystem {
	public static String SYSTEM_ROOT_PATH = "com.arcadiaprep.app.arca";
	public static final String DATABASE_NAME = "exams";
	public static final String DATABASE_ORIGINAL_FILE_PATH = "db/exams.rdb";
	public static final String DATABASE_NAME_RUNTIME = "runtime";

	public static final int HTML_CHAR_LIMIT = 80;

	/**
	 * • Get recent discussions for a given Exam
	 * 
	 * @param examType
	 *            like:LSAT, GRE,GMAT,SAT
	 * @return
	 */
	public static String GET_SERVER_URL_DISCUSSIONS_BY_EXAM(String examType) {
		return "jreq.php?discussions_recent=1&exam_type=" + examType;
	}

	/**
	 * Get discussions for a given problem(q_id is the id in the
	 * iphone_questions)
	 * 
	 * @param questionid
	 * @return
	 */
	public static String GET_SERVER_URL_DISCUSSIONS_BY_QUESTION_ID(
			int questionid) {
		return "jreq.php?discussions_question=1&q_id=" + questionid;
	}

	/**
	 * Post a new discussion
	 * 
	 * @return
	 */
	public static String GET_SERVER_URL_POST_A_NEW_DISCUSSION() {
		return "post_a_new_discussion_thread";
	}

	/**
	 * • Post a new post for a given question
	 * 
	 * @param questionid
	 * @return
	 */
	public static String GET_SERVER_URL_POST_FOR_A_GIVEN_QUESTION(int questionid) {
		return "index.php?qdiscussions_popup=" + questionid + "&question="
				+ questionid;
	}

	public static String GET_SERVER_URL_POST_REPLY(int discussionID) {
		return "post_reply/" + discussionID;
	}
	
	public static String GET_SERVER_URL_USER_INFO(int userID) {
		return "jreq.php?user_info=1&user_id=" + userID;
	}

	public static String getDataBaseDir() {
		return Environment.getDataDirectory() + "/data/" + SYSTEM_ROOT_PATH
				+ "/databases/";
	}

	public static String application_mail_address = "contact@arcadiaprep.com";
	public static String application_mail_subject = "ArcadiaPrep Android App";
	public static String application_mail_content = "Hello,";
	public static String application_http_portal = "http://www.arcadiaprep.com";
	public static final int ACTIVITY_SELECT_IMAGE = 1001;

	public static boolean isLogin = true;
	/**
	 * Timeout in milliseconds
	 */
	public static int HTTP_TIMEOUT = 25000;
	/**
	 * 1 – LSAT 2 – GMAT 3 – GRE 4 – SAT 5 – MCAT
	 */
	public static String DISCUSSION_POST_TYPE = "1";
	/**
	 * this is for inperson post, set 0 for now
	 */
	public static String DISCUSSION_POST_PLAY_TIME = "0";
	public static String DISCUSSION_POST_LOGIN = "login";
	public static String DISCUSSION_POST_STEP = "2";

	public static File GET_WORK_SPACE_SHOT_IMAGE_FOLDER() {
		return new File(Environment.getExternalStorageDirectory(), WorkSpace.DEFAULT_APP_CAPTURE_DIRECTORY);
	}
	
	public static File GET_WORK_SPACE_SHOT_IMAGE() {
		return new File(Environment.getExternalStorageDirectory(), WorkSpace.COMPOSITE_IMAGE_PATH);
	}
	
}
