package com.weibo.hackathon.deadline.engine.net;

import java.io.*;
import java.net.Socket;

/**
 * Created by axb on 15/4/17.
 */
public class NetworkChannel {
    private int i = 0;
    private Socket socket;
    private Reader reader;
    private Writer writer;
    private StringBuffer buffer = new StringBuffer();

    public NetworkChannel(Socket socket) throws IOException {
        this.socket = socket;
        reader = new InputStreamReader(socket.getInputStream());
        writer = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
    }

    public void init() throws IOException {
        writer.write((char) 255);
        writer.write((char) 253);
        writer.write((char) 34);
        writer.write((char) 1);
        writer.write((char) 0x0);
        writer.write((char) 255);
        writer.write((char) 240);
        writer.write((char) 255);
        writer.write((char) 251);
        writer.write((char) 1);
        writer.write("\u001B[2J");
        writer.write((char) 0x1b);
        writer.write((char) 0x5b);
        writer.write((char) 0x48);
        writer.flush();
        Thread thread = new Thread() {
            @Override
            public void run() {
                int len;
                char chars[] = new char[64];
                try {
                    while ((len = reader.read(chars)) != -1) {
                        buffer.append(new String(chars, 0, len));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    public char[] receive() throws IOException {
        char[] data = buffer.toString().toCharArray();
        // 并发问题暂时无视
        buffer.setLength(0);
        return data;

    }

    public char[] blockingReceive() throws IOException {
        int len;
        char chars[] = new char[64];
        StringBuffer buffer =  new StringBuffer();
        try {
            if ((len = reader.read(chars)) != -1) {
                buffer.append(new String(chars, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString().toCharArray();
    }

    public void send(char[] data) throws IOException {
        try {
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write("**********************************\r\n");
            // writer.write(i+++"**********************************\r\n");
            // writer.write("**********************************\r\n");
            writer.write(data);
            writer.write((char) 0x1b);
            writer.write((char) 0x5b);
            writer.write((char) 0x48);

            writer.flush();

        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {}
            throw e;
        }

    }
    
    public void sendRaw(char[] data) throws IOException {
        try {
            writer.write(data);

            writer.flush();

        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {}
            throw e;
        }

    }
}
