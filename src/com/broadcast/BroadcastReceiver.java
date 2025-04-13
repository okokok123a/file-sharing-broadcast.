package com.broadcast;

import java.io.*;
import java.net.*;

public class BroadcastReceiver {

    public static void main(String[] args) {
        int udpPort = 12345;

        try {
            DatagramSocket socket = new DatagramSocket(udpPort);
            socket.setBroadcast(true);

            System.out.println("📡 Đang lắng nghe thông báo chia sẻ tệp tin...");

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("📢 Nhận được thông báo: " + message);
                String[] parts = message.split(";");

                if (parts.length == 4) {
                    String fileName = parts[0];
                    long fileSize = Long.parseLong(parts[1]);
                    String serverIP = parts[2];
                    int serverPort = Integer.parseInt(parts[3]);

                    System.out.println("📁 Phát hiện tệp chia sẻ: " + fileName + " (" + fileSize + " bytes)");
                    System.out.println("🔄 Đang tải file từ: " + serverIP + ":" + serverPort);

                    // Kết nối TCP để tải file
                    Socket tcpSocket = new Socket(serverIP, serverPort);
                    InputStream is = tcpSocket.getInputStream();
                    FileOutputStream fos = new FileOutputStream("downloaded_" + fileName);

                    byte[] fileBuffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = is.read(fileBuffer)) != -1) {
                        fos.write(fileBuffer, 0, bytesRead);
                    }

                    // Đóng các streams
                    fos.close();
                    is.close();
                    tcpSocket.close();

                    // In ra đường dẫn tuyệt đối nơi file được lưu
                    System.out.println("📍 File lưu tại: " + new File("downloaded_" + fileName).getAbsolutePath());

                    System.out.println("✅ Tải file thành công và lưu dưới tên: downloaded_" + fileName);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}