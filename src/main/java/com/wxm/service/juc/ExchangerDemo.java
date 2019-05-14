package com.wxm.service.juc;

import java.util.concurrent.Exchanger;

public class ExchangerDemo {

	public static void main(String[] args) {
		Exchanger<String> ex = new Exchanger<>();
		
		ExchangerRunnable<String> er1 = new ExchangerRunnable<>(ex, "A");
		ExchangerRunnable<String> er2 = new ExchangerRunnable<>(ex, "B");
		
		new Thread(er1).start();
		new Thread(er2).start();
	}
}

class ExchangerRunnable<T> implements Runnable {

	private Exchanger<T> exchanger;
	private T t;

	public ExchangerRunnable(Exchanger<T> exchanger, T t) {
		this.exchanger = exchanger;
		this.t = t;
	}

	@Override
	public void run() {
		try {
			T previous = this.t;
			t = this.exchanger.exchange(this.t);
			System.out.println(Thread.currentThread().getName() + " exchanged " + previous + " for " + this.t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
