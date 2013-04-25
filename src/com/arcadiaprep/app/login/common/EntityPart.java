
package com.arcadiaprep.app.login.common;

public class EntityPart {

	private String name;
	private String body;

	public EntityPart(String name, String body) {
		this.name = name;
		this.body = body;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
