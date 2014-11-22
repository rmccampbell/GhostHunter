package edu.virginia.cs2110.rnm6u.ghosthunter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

public class BitmapGetter {

	private Resources res;
	private LruCache<Integer, Bitmap> cache;

	public BitmapGetter(Resources res) {
		this.res = res;
		cache = new LruCache<Integer, Bitmap>((int) Runtime.getRuntime().maxMemory() / 2){
			@Override
			protected int sizeOf(Integer key, Bitmap value) {
				return value.getByteCount();
			};
		};
	}

	public Bitmap getBitmap(int id) {
		Bitmap bm = cache.get(id);
		if (bm == null) {
			bm = BitmapFactory.decodeResource(res, id);
			cache.put(id, bm);
		}
		return bm;
	}

}
