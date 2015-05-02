package sep.software.anicare.event;

import sep.software.anicare.model.AniCareDateTime;
import sep.software.anicare.util.RandomUtil;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.GsonBuilder;

public class AniCareMessage implements Parcelable {
	public static enum TYPE {
		PUSH, MESSAGE;
	}
	
	private String id;
	private TYPE rawType;
	private String type;
	private String sender;
	private String senderId;
	private String receiver;
	private String receiverId;
	private String rawDateTime;
	private AniCareDateTime dateTime;
	private String relationId;
	private String content;
	
	private static GsonBuilder gb = new GsonBuilder();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TYPE getRawType() {
		return rawType;
	}

	public void setRawType(TYPE rawType) {
		this.rawType = rawType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getRawDateTime() {
		return rawDateTime;
	}

	public void setRawDateTime(String rawDateTime) {
		this.rawDateTime = rawDateTime;
	}

	public AniCareDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(AniCareDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static Parcelable.Creator<AniCareMessage> getCreator() {
		return CREATOR;
	}

	public static AniCareMessage rand() {
		AniCareMessage msg = new AniCareMessage();
		msg.id = RandomUtil.getString(10);
		msg.type = RandomUtil.getInt(1) == 0 ? TYPE.PUSH.toString() : TYPE.MESSAGE.toString();
		msg.sender = RandomUtil.getName();
		msg.senderId = RandomUtil.getString(10);
		msg.receiver = RandomUtil.getName();
		msg.receiverId = RandomUtil.getString(10);
		msg.rawDateTime = RandomUtil.getDateTime();
		msg.relationId = RandomUtil.getString(10);
		msg.content = RandomUtil.getObjName();
				
		return msg;
	}
	
	public String toString() {
		AniCareMessage msg = new AniCareMessage();
		msg.id = this.id;
		msg.type = this.type;
		msg.sender = this.sender;
		msg.senderId = this.senderId;
		msg.receiver = this.receiver;
		msg.receiverId = this.receiverId;
		msg.rawDateTime = this.rawDateTime;
		msg.relationId = this.relationId;
		msg.content = this.content;
		
		return gb.create().toJson(msg);
	}

	public final static Parcelable.Creator<AniCareMessage> CREATOR = new Creator<AniCareMessage>(){
		public AniCareMessage createFromParcel(Parcel in){
			return gb.create().fromJson(in.readString(), AniCareMessage.class);
		}
		public AniCareMessage[] newArray(int size){
			return new AniCareMessage[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(gb.create().toJson(this));
	}
	
}
