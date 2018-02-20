package com;

import sun.awt.windows.ThemeReader;

public class test extends Thread {
	
    public void run()
    {
        int n = 0;
        //while ((++n) < 1000);
        
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(Thread.interrupted());
        System.out.println(this.isInterrupted());
    }
     
    public static void main(String[] args) throws Exception
    {
    	test thread1 = new test();
        System.out.println("isAlive: " + thread1.isAlive());
        thread1.start();
          Thread.sleep(1000);      
        System.out.println("isAlive: " + thread1.isAlive());
        //thread1.resume();
        thread1.interrupt();
        


        thread1.join();  // 等线程thread1结束后再继续执行 
        System.out.println("thread1已经结束!");
        System.out.println("isAlive: " + thread1.isAlive());
    }
	

}
