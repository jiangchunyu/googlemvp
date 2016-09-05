package com.jcy.googlemvp.rxbus;

/**
 * @desc:
 * @author: Jiangcy
 * @datetime: 2016/9/3
 */
public class RxEvent {
    public int reciveType;
    public int threadType;
    public String eventAction;
    public  Object event;

    public RxEvent() {
    }

    /**
     * RxBus 事件
     * @param reciveType 接收者类型
     * @param threadType 事件类型
     * @param eventAction 事件Action
     * @param event       时间
     */
    public RxEvent(@EventType.ReceiveType int reciveType, @EventType.ThreadType int threadType, String eventAction, Object event) {
        this.reciveType = reciveType;
        this.threadType = threadType;
        this.eventAction = eventAction;
        this.event = event;
    }



    public Object getEvent() {
        return event;
    }

}
