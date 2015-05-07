package sep.software.anicare.service;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.CareHistory;
import sep.software.anicare.util.ObjectPreferenceUtil;

/**
 * Created by hongkunyoo on 15. 5. 5..
 */
public class AniCareDBServicePreference implements AniCareDBService {

    private ObjectPreferenceUtil mObjectPreference;

    public AniCareDBServicePreference() {
        mObjectPreference = AniCareApp.getAppContext().getObjectPreference();
    }

    @Override
    public List<AniCareMessage> listMessage() {
        List<AniCareMessage> list = null;
        try {
            list = mObjectPreference.getList("messageDB", new TypeToken<List<AniCareMessage>>(){}.getType());
        } catch (Exception e) {
            list = new ArrayList<AniCareMessage>();
        }
        return list;
    }

    @Override
    public boolean addMessage(AniCareMessage message) {
        List<AniCareMessage> list = listMessage();
        list.add(message);
        mObjectPreference.put("messageDB", list);
        return true;
    }

    @Override
    public void deleteMessage(String id) {
        List<AniCareMessage> list = listMessage();
        for (int i = 0 ; i < list.size() ; i++) {
            AniCareMessage msg = list.get(i);
            if (msg.getId().equals(id)) {
                list.remove(i);
            }
        }
        mObjectPreference.put("messageDB", list);
    }

    @Override
    public void deleteMessageAll() {
        mObjectPreference.remove("messageDB");
    }

    @Override
    public void updateMessage(String id, AniCareMessage message) {
        List<AniCareMessage> list = listMessage();
        for (AniCareMessage msg : list) {
            if (msg.getId().equals(id)) {
                msg.setContent(message.getContent());
                msg.setRawType(message.getRawType());
                msg.setSender(message.getSender());
                msg.setSenderId(message.getSenderId());
                msg.setReceiver(message.getReceiver());
                msg.setReceiverId(message.getReceiverId());
                msg.setContent(message.getContent());
                msg.setRawDateTime(message.getRawDateTime());
                msg.setRelationId(message.getRelationId());
            }
        }
        mObjectPreference.put("messageDB", list);
    }

    @Override
    public List<CareHistory> listHistory() {
        return null;
    }

    @Override
    public boolean addHistory(CareHistory history) {
        return false;
    }

    @Override
    public void deleteHistory(String id) {

    }

    @Override
    public void deleteHistoryAll() {

    }

    @Override
    public void updateMessage(String id, CareHistory history) {

    }
}
