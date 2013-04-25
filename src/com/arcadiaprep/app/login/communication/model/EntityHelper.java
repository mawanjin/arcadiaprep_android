package com.arcadiaprep.app.login.communication.model;

import com.arcadiaprep.app.login.common.EntityPart;
import com.arcadiaprep.app.login.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityHelper {

	/* login */
	private static final String LOGIN_NAME = "login_user";
	private static final String LOGIN_PASS = "login_pass";
	private static final String LOGIN = "login";
	
	/* register */
	private static final String FIRST_NAME = "fname";
	private static final String LAST_NAME = "lname";
	private static final String EMAIL = "email";
	private static final String PASSWORD = "pass";
	private static final String REPEATED_PASSWORD = "r_pass";
	private static final String EXAM = "exam";
	
	/* photo upload */
	private static final String PHOTO_USERNAME = "login_user";
	private static final String PHOTO_PASSWORD = "login_pass";
	private static final String STEP = "step";
	private static final String COD = "cod";
	private static final String SET_PROFILE_PIC = "set_profile_pic";
	private static final String CAPTION = "caption";
	public static final String PICTURE = "picture";
	
	public static List<EntityPart> getLoginEntityParts(DataLogin data) {
		List<EntityPart> parts = new ArrayList<EntityPart>(3);
		parts.add(new EntityPart(LOGIN_NAME, data.getUserName()));
		parts.add(new EntityPart(LOGIN_PASS, data.getUserPassword()));
		parts.add(new EntityPart(LOGIN, LOGIN));
		return parts;
	}
	
	public static List<EntityPart> getRegisterEntityParts(DataRegister data) {
		List<EntityPart> parts = new ArrayList<EntityPart>(3);
		parts.add(new EntityPart(FIRST_NAME, data.getFirstName()));
		parts.add(new EntityPart(LAST_NAME, data.getLastName()));
		parts.add(new EntityPart(EMAIL, data.getEmail()));
		parts.add(new EntityPart(PASSWORD, data.getPassword()));
		parts.add(new EntityPart(REPEATED_PASSWORD, data.getRepeatedPassword()));
		parts.add(new EntityPart(EXAM, data.getExam()));
		return parts;
	}
	
	private static Random random;
	public static List<EntityPart> getPhotoUploadEntityParts(User user) {
		List<EntityPart> parts = new ArrayList<EntityPart>(3);
		parts.add(new EntityPart(PHOTO_USERNAME, user.getUserName()));
		parts.add(new EntityPart(PHOTO_PASSWORD, user.getPassword()));
		parts.add(new EntityPart(STEP, "2"));
		if (null == random)
			random = new Random();
		parts.add(new EntityPart(COD, String.valueOf(random.nextInt())));
		parts.add(new EntityPart(SET_PROFILE_PIC, "1"));
		parts.add(new EntityPart(CAPTION, "Picture from Mobile"));
		return parts;
	}
}
