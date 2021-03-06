package sep.software.anicare.model;

import android.os.Parcel;
import android.os.Parcelable;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.util.RandomUtil;

public class AniCareMessage extends AniCareModel {
	public enum Type {
		SYSTEM(0), MESSAGE(1);

        private final int value;
        Type(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
	}

    public enum CommType {

        REQUEST(0), ACCEPT(1), REJECT(2);

        private final int value;
        CommType(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

	private String id;
	private int rawType;
    private int rawCommType;
	private String sender;
	private String senderId;
	private String receiver;
	private String receiverId;
	private String rawDateTime = AniCareDateTime.now().toString();
	private String relationId;
	private String content;
    private String userPhone;
    private boolean resolved = false;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return Type.values()[this.rawType];
	}

	public void setType(Type type) {
		this.rawType = type.getValue();
	}

	public int getRawType() {
		return rawType;
	}

	public void setRawType(int rawType) {
		this.rawType = rawType;
	}

    public CommType getCommType() { return CommType.values()[this.rawCommType]; }

    public void setCommType(CommType commType) {
        this.rawCommType = commType.getValue();
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
		return new AniCareDateTime(rawDateTime);
	}

	public void setDateTime(AniCareDateTime dateTime) {
		this.rawDateTime = dateTime.toString();
	}

	public String getRelationId() {
		return relationId;
	}

    public boolean isResolved() {
        return resolved;
    }

    public void resolved() {
        this.resolved = true;
    }

	public void unresolved() {
		this.resolved = false;
	}

    public void makeRelation() {
        if (senderId == null || receiverId == null) {
            this.relationId = "00000000";
            return;
        }
        if (senderId.compareTo(receiverId) > 0) {
            this.relationId = senderId + receiverId;
        } else {
            this.relationId = receiverId + senderId;
        }
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

    public String getUserPhone() { return this.userPhone; }

    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public boolean isMine() {
        if (this.senderId == null) return false;
        return this.senderId.equals(AniCareApp.getAppContext().getAniCareService().getCurrentUser().getId());
    }

	public static Parcelable.Creator<AniCareMessage> getCreator() {
		return CREATOR;
	}

	public static AniCareMessage rand() {
		AniCareMessage msg = new AniCareMessage();
		msg.id = RandomUtil.getString(10);
		msg.rawType = RandomUtil.getInt(1) == 0 ? Type.SYSTEM.getValue() : Type.MESSAGE.getValue();
		msg.sender = RandomUtil.getName();
		msg.senderId = RandomUtil.getString(10);
		msg.receiver = RandomUtil.getName();
		msg.receiverId = RandomUtil.getString(10);
		msg.rawDateTime = RandomUtil.getDateTime();
		msg.relationId = RandomUtil.getString(10);
		msg.content = RandomUtil.getObjName();
				
		return msg;
	}

    /*
	 * Parcelable
	 */
	public final static Parcelable.Creator<AniCareMessage> CREATOR = new Creator<AniCareMessage>(){
		public AniCareMessage createFromParcel(Parcel in){
			return toClass(in, AniCareMessage.class);
		}
		public AniCareMessage[] newArray(int size){
			return new AniCareMessage[size];
		}
	};

    static class MessageBuilder {

    }
}
