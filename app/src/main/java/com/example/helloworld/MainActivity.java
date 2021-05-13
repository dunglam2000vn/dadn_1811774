package com.example.helloworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {
    MQTTService mqttService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttService = new MQTTService( this);
        mqttService.setCallback(new MqttCallbackExtended() {
            @Override public void connectComplete(boolean reconnect, String serverURI) {

            }
            @Override public void connectionLost( Throwable cause){

            }
            @Override public void messageArrived(String topic, MqttMessage message) throws Exception {
                String data_to_microbit = message.toString();
                //port.write(data_to_microbit.getBytes(),1000);
                Toast.makeText(MainActivity.this,data_to_microbit,Toast.LENGTH_LONG).show();
                System.out.println(data_to_microbit);
            }
            @Override public void deliveryComplete(IMqttDeliveryToken token)
            {

            }
        });
    }

    private void sendDataMQTT( String data){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(true);
        byte[] b = data.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);
        Log.d("ABC","Publish:"+ msg);
        try {
            mqttService.mqttAndroidClient.publish("dunglam2000vn/feeds/bbc-led", msg);
        } catch ( MqttException e){

        }
    }
}