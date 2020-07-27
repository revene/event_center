package com.event.test;

import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

public class CronTest {


    private static void test() {
        String expression = "0 0/5 * * * ?";
        CronSequenceGenerator generator = new CronSequenceGenerator(expression);


        Date date = new Date();
        System.out.println(date.getTime() );
        Date now = new Date(1544520354758l);
        System.out.println("now=="+now.toString());
        Date next = generator.next(now);
        System.out.println(next.toString());

         next = generator.next(next);
        System.out.println(next.toString());
    }

    public static void main(String[] args) {
        test();
    }
}
