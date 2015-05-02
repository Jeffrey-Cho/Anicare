package sep.software.anicare.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class AsyncChainer {

	private static int mCurrentCount = 0;
	private static int mCount = 0;

	private static final int NUM_OF_QUEUE = 16;
	private static Map<Object, Queue<Chainable>> mMapQueue;
	static {
		mMapQueue = new HashMap<Object, Queue<Chainable>>();
	}


	public static void asyncChain(Object key, Chainable...chains) {
		Queue<Chainable> queue = mMapQueue.get(key);
		if (queue == null) {
			mMapQueue.put(key, new ArrayBlockingQueue<Chainable>(NUM_OF_QUEUE));
			queue = mMapQueue.get(key);
		}
		queue.addAll(Arrays.asList(chains));
		AsyncChainer.notifyNext(key);
	}


	public static void notifyNext(Object key, Object... params) {
		if (++mCurrentCount < mCount) return;
		Queue<Chainable> queue = mMapQueue.get(key);
		if (queue != null && !queue.isEmpty()) {
			Chainable c = queue.poll();
			c.doNext(key, params);
		}
	}


	public static void clearChain(Object obj) {
		mCount = 0;
		Queue<Chainable> queue = mMapQueue.get(obj);
		queue.clear();
	}


	public static void waitChain(int count){
		mCurrentCount = 0;
		mCount = count;
	}


	public static interface Chainable {
		public void doNext(Object obj, Object... params);
	}
}
