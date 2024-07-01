package com.iotsmartaliv.dmvphonedemotest;


/**
 * 自定义接受推送消息的Service
 */

public class DemoIntentService { /*extends TopPushIntentService {
    public DemoIntentService() {

    }


    @Override
    public void onReceivePassThroughMessage(TopPushMessage topPushMessage) {
//        byte[] payload = topPushMessage.getPayload();
        String data = topPushMessage.getContent();
        String p = topPushMessage.getPlatform();
        if (data == null) {
            Log.e("GTdemo", "receiver payload = null");
        } else {
            Log.e("GTdemo", "透传信息 =" + data + "  " + "平台=" + p);
            Intent intent = new Intent("com.example.zoukeqing.helloworld.MY_BROADCAST");
            intent.putExtra("hello", data);
            sendBroadcast(intent);
        }
    }

    @Override
    public void onNotificationMessageClicked(TopPushMessage topPushMessage) {

    }

    @Override
    public void onReceiveCID(String s, String s1) {

    }*/

//    @Override
//    public void onReceiveServicePid(Context context, int pid) {
//    }
//
//    @Override
//    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
//    }
//
//    @Override
//    public void onReceiveClientId(Context context, String clientid) {
//        Log.e("GTdemo", "本机cid " + clientid);
//    }
//
//    @Override
//    public void onReceiveOnlineState(Context context, boolean online) {
//    }
//
//    @Override
//    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
////        Log.e(TAG, "onReceiveClientId -> " + "cmdMessage = " + cmdMessage);
//    }

//    @Override
//    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
//    }
//
//    @Override
//    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
//    }
}
