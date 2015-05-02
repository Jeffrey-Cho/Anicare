package sep.software.anicare.model;

import sep.software.anicare.util.RandomUtil;

import com.google.gson.Gson;

public class AniCareUser {
	private String id;
	private String name;
	private String fbId;
	private String registrationId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public static AniCareUser rand() {
		AniCareUser user = new AniCareUser();
		user.setName(RandomUtil.getName());
		user.setFbId(RandomUtil.getString(10));
		user.setRegistrationId(RandomUtil.getString(10));
		return user;
	}
}
