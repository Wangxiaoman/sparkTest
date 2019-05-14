package com.wxm.service.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierDemo {

	public static void main(String[] args) throws InterruptedException {
		AtomicInteger a1 = new AtomicInteger(1);
		AtomicInteger a2 = new AtomicInteger(2);
		AtomicInteger a3 = new AtomicInteger(3);
		
		List<AtomicInteger> as = new ArrayList<>();
		as.add(a1);
		as.add(a2);
		as.add(a3);
		
		CyclicBarrier cyc1 = new CyclicBarrier(3, new CyclicBarrierAction(as,"cyc1"));
		CyclicBarrier cyc2 = new CyclicBarrier(3, new CyclicBarrierAction(as,"cyc1"));
		CyclicBarrier cyc3 = new CyclicBarrier(3, new CyclicBarrierAction(as,"cyc1"));

		List<CyclicBarrier> clist = new ArrayList<>();
		clist.add(cyc1);
		clist.add(cyc2);
		clist.add(cyc3);

		Thread t1 = new Thread(new CyclicBarrierRunnable(clist,a1));
		Thread t2 = new Thread(new CyclicBarrierRunnable(clist,a2));
		Thread t3 = new Thread(new CyclicBarrierRunnable(clist,a3));

		t1.start();
		t2.start();
		t3.start();
		
		t1.join();
		t2.join();
		t3.join();
		
		System.out.println("a1:"+a1.get());
		System.out.println("a2:"+a2.get());
		System.out.println("a3:"+a3.get());
		
	}
}

class CyclicBarrierAction implements Runnable {

	private List<AtomicInteger> ais;
	private String name;

	public CyclicBarrierAction(List<AtomicInteger> ais, String name) {
		this.ais = ais;
		this.name = name;
	}

	@Override
	public void run() {
		if (ais != null && ais.size() > 0) {
			int sum = 0;
			for (AtomicInteger ai : ais) {
				sum += ai.get();
			}
			
			int average = sum / ais.size();
			for (AtomicInteger ai : ais) {
				ai.set(ai.get() + average);
				System.out.println("cyc ai : " + ai.get());
			}
			
		}
		System.out.println(Thread.currentThread().getName() + this.name +" cyclic ai increment ~");
	}
}

class CyclicBarrierRunnable implements Runnable {

	private List<CyclicBarrier> cycs;
	private AtomicInteger ai;

	public CyclicBarrierRunnable(List<CyclicBarrier> cycs, AtomicInteger ai) {
		this.cycs = cycs;
		this.ai = ai;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < cycs.size(); i++) {
			System.out.println(
					Thread.currentThread().getName() + " waiting at barrier, i: " + i + ",ai:" + ai.getAndAdd(i));
			try {
				cycs.get(i).await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}

		System.out.println(Thread.currentThread().getName() + " done!");
	}

}
