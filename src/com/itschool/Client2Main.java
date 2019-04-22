package com.itschool;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client2Main
{
    public static void main(String[] args) {
        try {
            final Scanner scanner = new Scanner(System.in);
            final Socket socket = new Socket("127.0.0.1", 5001);
            final InputStream is = socket.getInputStream();
            final OutputStream os = socket.getOutputStream();
            System.out.println("Введите логин: ");
            final String login = scanner.nextLine();

            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        while ( ! isInterrupted()) {
                            Message msg = Message.readFromStream(is);
                            if (msg == null)
                                Thread.yield();
                            else
                            {
                                if (msg.to.equals(login) || msg.to.equals("*"))
                                    System.out.println(msg.toString());
                            }
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            };
            th.setDaemon(true);
            th.start();

            System.out.println(login + " вошёл в чат. Приветствуем!");

            try
            {
                while (true)
                {
                    String s = scanner.nextLine();
                    if (s.contains("exit"))
                        break;

                    int del = s.indexOf(':');
                    String to = "";
                    String text = s;

                    if (del >= 0) {
                        to = s.substring(0, del);
                        text = s.substring(del + 1);
                    }

                    Message m = new Message();
                    m.text = text;
                    m.from = login;
                    m.to = to;

                    m.writeToStream(os);
                }
                System.out.println(login + " вышел из чата.");
            } finally {
                th.interrupt();
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
