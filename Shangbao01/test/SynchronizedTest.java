import static org.junit.Assert.*;

import org.junit.Test;


public class SynchronizedTest {

	@Test
	public void test() {
		Thread thread1 = new Run();
		Thread thread2 = new Run();
		thread1.start();
		thread2.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class Sync{
		public void fun(){
			synchronized(SynchronizedTest.class){
			System.out.println("开始.....");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("结束.....");
			}
		}
	}
	
	class Run extends Thread{
		@Override
		public void run() {
			Sync sync = new Sync();
			sync.fun();
		}
	}
}
