package com.example.administrator.signincouse.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.administrator.signincouse.service.SocketService.SOCKET_RECEIVE;

/**
 *
 * Created by shi hao on 2017/2/25.
 */

public class SocketAcceptThread extends Thread {

    private Context context;
    private BufferedReader mReader = null;

    public SocketAcceptThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("service", "socket service - SocketAcceptThread::run");
        try {
            // 实例化ServerSocket对象并设置端口号为 5222
            ServerSocket mServerSocket = new ServerSocket(5222);
            while (true) {
                // 等待客户端的连接（阻塞）
                Socket mClientSocket = mServerSocket.accept();
                manageConnection(mClientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void manageConnection(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Client" + socket.hashCode() + "connected");
                    // 获得输入流
                    mReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String receivedMsg;
                    while ((receivedMsg = mReader.readLine()) != null) {
                        System.out.println("收到客户端发的消息：" + receivedMsg);
                        Intent sendIntent = new Intent(SOCKET_RECEIVE);
                        sendIntent.putExtra("action", "RcvStr");
                        sendIntent.putExtra("content", receivedMsg);
                        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                        context.sendBroadcast(sendIntent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        mReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
