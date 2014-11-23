package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

public class BitmapGetter {
	private static final String TAG = BitmapGetter.class.getSimpleName();

	private Resources res;
	private LruCache<Integer, Bitmap> cache;

	public BitmapGetter(Resources res) {
		this.res = res;
		int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1024);
		cache = new LruCache<Integer, Bitmap>(maxMem / 2){
			@Override
			protected int sizeOf(Integer key, Bitmap value) {
				return value.getByteCount() / 1024;
			};
		};
//		Log.d(TAG, "Max memory: " + maxMem);
//		Log.d(TAG, "Max size: " + cache.maxSize());
//		Log.d(TAG, "Used memory: " + 
//				(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
	}

	public Bitmap getBitmap(int id) {
		Bitmap bm = cache.get(id);
		if (bm == null) {
			bm = BitmapFactory.decodeResource(res, id);
			cache.put(id, bm);
			Log.d(TAG, "Cache size: " + cache.size());
			Log.d(TAG, "Used memory: " + 
					(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
		}
		return bm;
	}

}
