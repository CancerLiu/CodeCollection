package com.code.collection.formulary.stateMachine;

import org.apache.commons.lang3.StringUtils;

/**
 * 模拟状态机。这里使用二维数组来模拟状态机。通过状态——>事件——>状态 的事件驱动模型来模拟。
 */
public abstract class StateManager {

    /**
     * 具体的状态
     */
    private enum State {
        pending("待确认"),
        wait("待支付"),
        prepared("可生产"),
        generating("二维码待生成"),
        canceled("作废"),
        abnormal("异常");

        private String state;

        State(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public static State getState(String s) {
            try {
                return valueOf(s);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 得到对应状态英文名对应的中文名
         *
         * @return 具体的中文名
         */
        public static String getCn(String state) {
            State s = State.getState(state);
            return s == null ? "未知状态" : s.getState();
        }
    }

    /**
     * 具体的事件，事件是状态发生改变的原因
     */
    private enum Event {
        create("创建"),

        confirm("确认"),
        prepare("准备"),
        cancel("取消"),

        abnormal("异常"),

        pay("支付"),

        generate("生成二维码");

        private String event;

        Event(String event) {
            this.event = event;
        }

        public String getEvent() {
            return event;
        }

        public static Event getEvent(String event) {
            try {
                return valueOf(event);
            } catch (Exception e) {
                return null;
            }
        }

        /**
         * 得到对应状态英文名对应的中文名
         *
         * @return 具体的中文名
         */
        public static String getCn(String event) {
            Event s = Event.getEvent(event);
            return s == null ? "未知事件" : s.getEvent();
        }
    }


    /**
     * 状态机管理器，此处使用二维数组来模拟。
     */
    private static String[][] state_context = new String[][]{
            {null, Event.create.name(), State.pending.name()},

            //"待确认"
            {State.pending.name(), Event.cancel.name(), State.canceled.name()},
            {State.pending.name(), Event.confirm.name(), State.wait.name()},
            {State.pending.name(), Event.prepare.name(), State.generating.name()},

            //"待支付"
            {State.wait.name(), Event.cancel.name(), State.canceled.name()},
            {State.wait.name(), Event.prepare.name(), State.generating.name()},
            {State.wait.name(), Event.pay.name(), State.generating.name()},
            {State.wait.name(), Event.generate.name(), State.prepared.name()},

            //"二维码待生成"
            {State.generating.name(), Event.generate.name(), State.prepared.name()},
            {State.generating.name(), Event.abnormal.name(), State.abnormal.name()},

            //"异常"
            {State.abnormal.name(), Event.generate.name(), State.prepared.name()},
    };

    private static String _processEvent(String state, String event) {
        for (String[] sc : state_context) {
            if (StringUtils.equals(state, sc[0]) && StringUtils.equals(event, sc[1])) {
                return sc[2];
            }
        }

        String notice = String.format("当前状态[%s]不能进行[%s]操作！", State.getCn(state), Event.getCn(event));
        throw new RuntimeException(notice);
    }

    public void processEvent(Long id, Event event, String user, String mark) {
        /**
         * 这里可通过id从数据库中得到对象，然后得到该对象的状态，这里直接写死一个状态,做模拟
         */

        String state = State.pending.name();

        //通过初始状态和事件来得到结果状态
        String stateTo = _processEvent(state, event.name());

        /**
         * 这里可以调用相关dao层方法将事件和结果状态绑定id存储到数据库
         * 这里不再写出来
         *
         * 然后可以进一步的写入日志，供以后查询。
         */

        onStateChanged(id, stateTo);
    }

    /**
     * 当到达结果状态的时候，各个子类需要处理的事
     */
    protected abstract void onStateChanged(Long id, String stateTo);

}
