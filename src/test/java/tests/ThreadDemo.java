package tests;

public class ThreadDemo {

	public static void main(String[] args) {
		MyThread mt = new MyThread();
		mt.start();
		mt.join();
		for (int i = 0; i < 50; i++)
			System.out.println("print statement in main : " + i);
	}

}

class MyThread implements Runnable {
	Thread t;
	@Override
	public void run() {
		for (int i = 0; i < 50; i++)
			System.out.println("inside my thread run method : " + i);
	}

	public void start() {
		t = new Thread(this, "mythread");
		t.start();
	}
	
	public void join() {
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
