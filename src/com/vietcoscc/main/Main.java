package com.vietcoscc.main;

/**
 * Created by vaio on 17/04/2017.
 */
public class Main {
    public static void main(String[] args) {
        try {
            (new Parser("")).parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
