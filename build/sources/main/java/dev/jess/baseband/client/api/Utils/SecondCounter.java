package dev.jess.baseband.client.api.Utils;

import java.util.LinkedList;
import java.util.Queue;


public class SecondCounter {

	private final Queue<Long> count = new LinkedList<>();


	public void increment() {
		count.add(System.currentTimeMillis() + 1000L);
	}


	public int getCount() {
		long time = System.currentTimeMillis();
		try {
			while (! count.isEmpty() && count.peek() < time) {
				count.remove();
			}
		} catch (Exception e) {
			// empty catch block
		}
		return count.size();
	}

}
