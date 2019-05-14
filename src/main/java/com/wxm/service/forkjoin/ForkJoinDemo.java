package com.wxm.service.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {
	
	public static void main(String[] args) throws Exception {
		ForkJoinPool fjPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
//		System.out.println(fjPool.invoke(new OrderTask()));
		long b = System.currentTimeMillis();
		int loop = 1000;
		for(int i=1;i<loop;i++){
			fjPool.invoke(new ForkCounter());
		}
		long e = System.currentTimeMillis();
		System.out.println("forkjoin cost ms:"+(e-b));
		
		long bb = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(20);
		List<Callable<Integer>> list = new ArrayList<>();
		for(int i=1;i<loop;i++){
			list.add(new Counter());
		}
		executor.invokeAll(list);
		long ee = System.currentTimeMillis();
		System.out.println("executor cost ms:"+(ee-bb));
		executor.shutdown();
	}
}

class Counter implements Callable<Integer>{
	@Override
	public Integer call() throws Exception {
		int count = 0;
		for(int i=0;i<10000;i++){
			count += i;
		}
		return count;
	}
}

class ForkCounter extends RecursiveTask<Integer>{
	private static final long serialVersionUID = 1L;
	@Override
	protected Integer compute() {
		int count = 0;
		for(int i=0;i<10000;i++){
			count += i;
		}
		return count;
	}
}


class OrderTask extends RecursiveTask<OrderInfo>{
	private static final long serialVersionUID = 1L;
	@Override
	protected OrderInfo compute() {
		System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
		UserTask userTask = new UserTask();
		FoodTask foodTask = new FoodTask();
		CoffeeTask coffeeTask = new CoffeeTask();
		
		invokeAll(userTask,foodTask,coffeeTask);
		OrderInfo order = new OrderInfo(userTask.join(),foodTask.join(),coffeeTask.join());
		//设置超时时间
		//OrderInfo order = new OrderInfo(userTask.get(100,TimeUnit.MILLISECONDS),foodTask.join(),coffeeTask.join());
		return order;
	}
	
}

class UserTask extends RecursiveTask<User>{
	private static final long serialVersionUID = 1L;
	@Override
	protected User compute() {
		System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
		User user = new User();
		user.setId(1);
		user.setName("wxm");
		return user;
	}
}

class FoodTask extends RecursiveTask<Food>{
	private static final long serialVersionUID = 1L;
	@Override
	protected Food compute() {
//		System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
		Food food = new Food();
		food.setId(1);
		food.setName("fread");
		return food;
	}
}

class CoffeeTask extends RecursiveTask<Coffee>{
	private static final long serialVersionUID = 1L;
	@Override
	protected Coffee compute() {
//		System.out.println("执行"+ this.getClass().getSimpleName() + "线程名字为:" + Thread.currentThread().getName());
		Coffee coffee = new Coffee();
		coffee.setId(1);
		coffee.setName("cat shit");
		return coffee;
	}
}