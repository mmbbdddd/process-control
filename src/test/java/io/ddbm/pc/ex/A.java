package io.ddbm.pc.ex;

public class A {
    String name = "A";

    public void say() {
        System.out.println("A@" + name);
    }

    public static void main(String[] args) {
        A a = new A();
        a.say();

        a = new A1();
        a.say();
        a = new A2();
        a.say();
        a = new A3();
        a.say();
    }
}


