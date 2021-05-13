package com.example.helloworld;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTService {
    final String serverUri ="tcp://io.adafruit.com:1883";
    final String clientId ="Busculate";
    final String subscriptionTopic ="dunglam2000vn/feeds/bbc-led";
    final String username ="dunglam2000vn";
    final String password ="aio_LEUM76Vs8i2mKpCe83igp2EtPOBP";
    public MqttAndroidClient mqttAndroidClient;

    public MQTTService(Context context){
        mqttAndroidClient = new MqttAndroidClient(context,serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
        @Override public void connectComplete( boolean b, String s){
            Log.w("mqtt",s);
        }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            Log.w("Mqtt", mqttMessage.toString());
        }
        @Override public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

        }
        });
        connect();
    }

    public void setCallback( MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override public void onSuccess( IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }
                @Override public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt","Failedtoconnectto:"+ serverUri + exception. toString());
                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override public void onSuccess( IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }
                @Override public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt","Subscribedfail!");
                }
            });
        } catch ( MqttException ex) {
            System.err.println("Exceptionstsubscribing");
            ex.printStackTrace();
        }
    }
}

