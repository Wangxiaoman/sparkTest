package com.wxm.service.juc;

import java.util.concurrent.CountDownLatch;

public class CountDownLanchDemo {
	
//	public static void main(String[] args) {
//		CountDownLatch latcher = new CountDownLatch(5);
//		new Thread(new Waiter(latcher)).start();
//		new Thread(new Coster(latcher,5)).start();
//		
//		System.out.println("Thread main");
//	}
}

class Coster implements Runnable{

	private CountDownLatch latcher;
	private int counter;
	
	public Coster(CountDownLatch latcher, int counter){
		this.counter = counter;
		this.latcher = latcher;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < counter; i++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			latcher.countDown();
			System.out.println("Thread Coster, latcher count:"+latcher.getCount());
		}
	}
}

class Waiter implements Runnable{
	
	private CountDownLatch counter;
	
	public Waiter(CountDownLatch counter){
		this.counter = counter;
	}

	@Override
	public void run() {
		try {
			counter.await();
			System.out.println("Thread Waiter finish ~");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}

