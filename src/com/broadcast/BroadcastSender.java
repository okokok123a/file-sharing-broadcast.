package com.broadcast;

import java.net.*;
import java.io.*;

public class BroadcastSender {

    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            // Tạo socket UDP
            socket = new DatagramSocket();

            // Cho phép gửi gói tin broadcast
            socket.setBroadcast(true);

            // Địa chỉ broadcast trong mạng LAN (ví dụ: 255.255.255.255)
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            int port = 12345;  // Cổng để gửi gói tin

            // Gửi thông điệp
            String message = "Hello, this is a broadcast message!";
            byte[] buffer = message.getBytes();

            // Tạo gói tin Datagram
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);

            // Gửi gói tin
            socket.send(packet);
            System.out.println("Broadcast message sent.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
