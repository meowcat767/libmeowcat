package site.meowcat.examples.logging;

import site.meowcat.logging.Log;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class LogExample {
    public static void main(String[] args) {
        try {
            System.out.println("access security");
            Log.warn("access: PERMISSION DENIED");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("access security grid");
            TimeUnit.SECONDS.sleep(1);
            Log.warn("access: PERMISSION DENIED");
            System.out.println("access main security grid");
            TimeUnit.SECONDS.sleep(1);
            Log.warn("access: PERMISSION DENIED...and...");
            TimeUnit.SECONDS.sleep(1);
            while (true) {
                Log.error("YOU DIDN'T SAY THE MAGIC WORD!");
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
