package com.sunhill.app.util;


public class IdGenerator {

    public static Integer generate(){
        return (int) (System.currentTimeMillis() & 0xfffffff);
    }
}
