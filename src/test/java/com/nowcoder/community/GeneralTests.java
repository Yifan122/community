package com.nowcoder.community;

public class GeneralTests {

}

class Art {
    Art() {
        System.out.println("Art");
    }
}

class Drawing extends Art {
    Drawing() {
        System.out.println("Drawing");
    }
}

class Printing extends Drawing {
    Printing() {
        System.out.println("printting");
    }

    public static void main(String[] args) {
        Printing printing = new Printing();
    }
}



