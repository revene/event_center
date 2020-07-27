package com.event.test;

import com.blanc.event.model.Event;
import com.blanc.event.sevice.EventScheduleConsumerService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ClassTest {


    public void test(String name) {

    }

    public static void main(String[] args) throws Exception {
        for (Method m : EventScheduleConsumerService.class.getMethods()) {
            System.out.println("----------------------------------------");
            System.out.println("   method: " + m.getName());
            System.out.println("   return: " + m.getReturnType().getName());
            for (Parameter p : m.getParameters()) {
                System.out.println("parameter: " + p.getType().getName() + ", " + p.getName());
            }
        }


        System.out.println("==============================");
        System.out.println("==============================");



        for (Field m : Event.class.getDeclaredFields()) {
            System.out.println("----------------------------------------");
            System.out.println("   Field: " + m.getName());
            System.out.println("   Field Type: " + m.getType().getName());

        }
    }


}
