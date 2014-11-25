package edu.virginia.cs2110.rnm6u.ghosthunter;

import java.util.Scanner;

import android.content.res.Resources;

public class ParseMap {

	private Resources res;

	public ParseMap(Resources res) {
		this.res = res;
	}

	public int [][] get2dArray (int resid) {

	Scanner parser = new Scanner(res.openRawResource(resid));
	parser.next();
	int width = parser.nextInt();
	parser.next();
	int height = parser.nextInt();
	parser.nextLine();
	String wholeString = parser.nextLine();

	String [] wholeStringArray = wholeString.split(",\\s");
	int [] wholeIntArray = new int [wholeStringArray.length];

	for (int q = 0; q < wholeStringArray.length; ++q) {
		wholeIntArray[q] = Integer.parseInt(wholeStringArray[q]);
	}
	int [][] tiles = new int [width][height];
	

	for ( int y = 0; y < height; ++y ) {
		for ( int x = 0; x < width; ++x ) {
			tiles[x][y] = wholeIntArray[x + y*width];
		}
	}

/*	for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			System.out.print(tiles[x][y] + "\t");
		}
		System.out.println();
	}
*/
	return tiles;

	}

}
