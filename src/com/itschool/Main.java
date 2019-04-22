package com.itschool;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Server s = new Server(5001);

        try
        {
            s.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
