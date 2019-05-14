package com.wxm.service.juc;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class DelayedDemo {

	public static void main(String[] args) {
		DelayQueue<DelayedTask> dq = new DelayQueue<>();
		DelayedTask t1 = new DelayedTask(1);
		System.out.println("t1:" + t1 + ",delay " + t1.getDelay(TimeUnit.NANOSECONDS));
		dq.add(t1);
		DelayedTask t2 = new DelayedTask(2);
		dq.add(t2);
		System.out.println("t2:" + t2 +",delay "+ t2.getDelay(TimeUnit.NANOSECONDS));
		try {
			DelayedTask tt1= dq.take();
			System.out.println("tt1:" + tt1.toString());
			
			DelayedTask tt2= dq.take();
			System.out.println("tt2:" + tt2.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class DelayedTask implements Delayed {
	private static final AtomicLong sequencer = new AtomicLong();

	private int delaySecond;
	private long sequenceNumber;
	private final long createNanoTime = System.nanoTime();

	public DelayedTask(int delaySecond) {
		this.delaySecond = delaySecond;
		this.sequenceNumber = sequencer.getAndIncrement();
	}

	@Override
	public int compareTo(Delayed o) {
		if (o == this) {
			return 0;
		}
		long sub = o.getDelay(TimeUnit.NANOSECONDS) - this.getDelay(TimeUnit.NANOSECONDS);
		if (sub == 0) {
			return 0;
		} else if (sub > 0) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(createNanoTime + TimeUnit.SECONDS.toNanos(delaySecond) - System.nanoTime(),
				TimeUnit.NANOSECONDS);
	}

	@Override
	public String toString() {
		return "DelayedTask [delaySecond=" + delaySecond + ", sequenceNumber=" + sequenceNumber + "]";
	}

}
