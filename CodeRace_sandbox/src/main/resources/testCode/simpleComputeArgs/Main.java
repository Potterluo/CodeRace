/*
 * Copyright 2022 Keriko Inc.
 * javac -encoding utf-8 .\Main.java
 * java -cp . Main 1 2
 */
public class Main {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        System.out.println("结果:" + (a + b));
    }
}
