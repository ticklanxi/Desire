package com.example.display_156_box.bean;

/**
 * 轮询串口返回成功的指令
 */

public class MessageEvent {

    private String eventTag;
    private Object eventData;
    private Object[] eventDatas;

    public MessageEvent(String eventTag) {
        this.eventTag = eventTag;
    }

    public MessageEvent(String eventTag, Object eventData) {
        this.eventTag = eventTag;
        this.eventData = eventData;
    }

    public MessageEvent(String eventTag, Object... eventDatas) {
        this.eventTag = eventTag;
        this.eventDatas = eventDatas;
    }

    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    public Object getEventData() {
        return eventData;
    }

    public void setEventData(Object eventData) {
        this.eventData = eventData;
    }

    public Object[] getEventDatas() {
        return eventDatas;
    }

    public void setEventDatas(Object[] eventDatas) {
        this.eventDatas = eventDatas;
    }
}
