package sep.software.anicare.service;

import java.util.List;

import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.model.CareHistory;

/**
 * Created by hongkunyoo on 15. 5. 5..
 */
public interface AniCareDBService {
    public List<AniCareMessage> listMessage();
    public boolean addMessage(AniCareMessage message);
    public void deleteMessage(String id);
    public void deleteMessageAll();
    public void updateMessage(String id, AniCareMessage message);


    public List<CareHistory> listHistory();
    public boolean addHistory(CareHistory history);
    public void deleteHistory(String id);
    public void deleteHistoryAll();
    public void updateMessage(String id, CareHistory history);


}
