package com.example.samplethread2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    MainHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackGroundThread thread = new BackGroundThread();
                thread.start();
            }
        });
        //핸들러 객체 생성
        handler = new MainHandler();
    }

    //사용자정의 스레드
    class BackGroundThread extends Thread{
        int value = 0; //변수 선언

        public void run(){
            for(int i=0; i<100; i++){
                try{
                    Thread.sleep(1000);
                }catch (Exception e){}

                value += 1;
                //호출의 결과로 메시지 객체를 리턴받음
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle(); //번들객체 생성
                bundle.putInt("value", value); //번들객체에 정수형 데이터 저장
                message.setData(bundle); //메시지객체에 번들객체 데이터 세팅

                handler.sendMessage(message); //메시지 큐에 (사용자정의 스레드가 보낸)메시지를 넣음
            }
        }
    }

    class MainHandler extends Handler {
        //코드가 수행되는 위치는 새로 만든 스레드가 아닌 메인 스레드가 됨
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //번들객체타입의 메시지의 데이터를 저장
            Bundle bundle = msg.getData();
            int value = bundle.getInt("value"); //value이름으로 된 정수형 데이터 저장
            textView.setText("value의 값 : " + value); //메인 스레드의 textView객체의 setText변경경
        }
    }
}