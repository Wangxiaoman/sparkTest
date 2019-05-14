package com.wxm.service.juc;

import java.util.concurrent.Semaphore;

public class SemaphoneDemo {
	
	public static void main(String[] args) {
		Semaphore semaphone = new Semaphore(2,true);
		new Thread(new ExecuteRunnable(semaphone,20)).start();
		new Thread(new ExecuteRunnable(semaphone,10)).start();
		new Thread(new ExecuteRunnable(semaphone,5)).start();
	}
	
	
}

class ExecuteRunnable implements Runnable{
	
	private Semaphore semaphone;
	private int sleepSecond;
	public ExecuteRunnable(Semaphore semaphone, int sleepSecond){
		this.semaphone = semaphone;
		this.sleepSecond = sleepSecond;
	}

	@Override
	public void run() {
		try {
			semaphone.acquire(1);
			System.out.println(Thread.currentThread().getName()+" acquire");
			Thread.sleep(sleepSecond * 1000);
			semaphone.release();
			System.out.println(Thread.currentThread().getName() + sleepSecond +" release");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
