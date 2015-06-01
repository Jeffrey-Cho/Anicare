package sep.software.anicare.model;

import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class CareHistory extends AniCareModel {
    private String id;
    private int point;
    private String rawDateTime;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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


    public static CareHistory rand() {
        return rand(false);
    }

    public static CareHistory rand(boolean hasId) {
        CareHistory history = new CareHistory();
        history.setId(RandomUtil.getId());

        return history;
    }
}
