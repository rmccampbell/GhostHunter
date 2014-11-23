package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapGetter {
	@SuppressWarnings("unused")
	private static final String TAG = BitmapGetter.class.getSimpleName();

	private Resources res;
	private HashMap<Integer, WeakReference<Bitmap>> cache;

	public BitmapGetter(Resources res) {
		this.res = res;
		cache = new HashMap<Integer, WeakReference<Bitmap>>();
//		int maxMem = (int) Runtime.getRuntime().maxMemory();
//		cache = new LruCache<Integer, Bitmap>(maxMem / 2){
//			@Override
//			protected int sizeOf(Integer key, Bitmap value) {
//				return value.getByteCount();
//			};
//		};
	}

	public Bitmap getBitmap(int id) {
		Bitmap bm = null;
		WeakReference<Bitmap> ref = cache.get(id);
		if (ref != null) {
			bm = ref.get();
		}
		if (bm == null) {
			bm = BitmapFactory.decodeResource(res, id);
			cache.put(id, new WeakReference<Bitmap>(bm));
		}
		return bm;
	}

}
