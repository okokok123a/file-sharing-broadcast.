package com.broadcast;

import java.io.*;
import java.net.*;

public class BroadcastSender {

    public static void main(String[] args) {
        String filePath = "E:\\xulyanh.pdf"; // Đường dẫn tệp muốn chia sẻ
        int udpPort = 12345;                // Cổng gửi broadcast
        int tcpPort = 23456;                // Cổng truyền tệp

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("❌ File không tồn tại: " + filePath);
                return;
            }

            String fileName = file.getName();
            long fileSize = file.length();

            // Đặt IP tĩnh của máy thật (Windows) - Sửa lại
            String localIP = "192.168.1.9"; // Thay IP này bằng IP của máy Windows

            // Gửi thông điệp broadcast: TênFile;KíchThước;IP;CổngTCP
            String message = fileName + ";" + fileSize + ";" + localIP + ";" + tcpPort;
            byte[] buffer = message.getBytes();

            // Gửi qua UDP broadcast
            DatagramSocket udpSocket = new DatagramSocket();
            udpSocket.setBroadcast(true);
            InetAddress broadcastAddress = InetAddress.getByName("192.168.1.255");

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, udpPort);
            udpSocket.send(packet);
            System.out.println("📢 Đã gửi broadcast thông báo tệp tin!");
            udpSocket.close();

            // Bắt đầu TCP Server
            ServerSocket serverSocket = new ServerSocket(tcpPort);
            System.out.println("🧩 Đang chờ client yêu cầu tải file...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔁 Client đã kết nối: " + clientSocket.getInetAddress());

                // Thiết lập timeout cho socket
                clientSocket.setSoTimeout(10000); // 10 giây

                new Thread(() -> handleClient(clientSocket, file)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gửi file cho client
    private static void handleClient(Socket clientSocket, File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = clientSocket.getOutputStream();

            byte[] fileBuffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(fileBuffer)) != -1) {
                os.write(fileBuffer, 0, bytesRead);
            }

            os.flush();
            fis.close();
            clientSocket.close();
            System.out.println("✅ Đã gửi file xong cho: " + clientSocket.getInetAddress());

        } catch (SocketTimeoutException e) {
            System.err.println("⏰ TIMEOUT: Client không phản hồi kịp - " + clientSocket.getInetAddress());
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("❌ Lỗi khi gửi file cho client: " + clientSocket.getInetAddress());
            e.printStackTrace();
        }
    }
}
