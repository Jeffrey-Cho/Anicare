package sep.software.anicare.model;

import sep.software.anicare.util.RandomUtil;

/**
 * Created by hongkunyoo on 15. 5. 2..
 */
public class CareHistory extends AniCareModel {
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
