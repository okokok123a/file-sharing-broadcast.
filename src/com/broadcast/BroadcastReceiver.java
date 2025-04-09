package com.broadcast;

import java.net.*;

public class BroadcastReceiver {

    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            // Tạo socket UDP
            socket = new DatagramSocket(12345);  // Cổng nhận gói tin broadcast
            socket.setBroadcast(true);

            System.out.println("Listening for broadcast messages...");

            while (true) {
                byte[] buffer = new byte[1024];  // Đệm để lưu gói tin nhận được
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Nhận gói tin broadcast
                socket.receive(packet);

                // Chuyển đổi gói tin thành chuỗi
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received broadcast message: " + message);

                // Xử lý thông điệp nhận được (ví dụ: phát hiện máy tính chia sẻ tệp tin)
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
