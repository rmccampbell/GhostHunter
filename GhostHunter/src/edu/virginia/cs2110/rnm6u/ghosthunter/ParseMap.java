package edu.virginia.cs2110.rnm6u.ghosthunter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import android.content.res.Resources;



public class ParseMap {
	
	
	private Resources res;


	public ParseMap(Resources res) {
		this.res = res;
	}
	
	
	public int [][] get2dArray (int resid) throws FileNotFoundException{

	
	Scanner readJson = new Scanner(res.openRawResource(resid));
	readJson.next();
	int width = readJson.nextInt();
	readJson.next();
	int height = readJson.nextInt();
	readJson.nextLine();
	System.out.println(width + "," + height);
	String wholeString = readJson.nextLine();

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
	
	/*for (int y = 0; y < height; y++) {
		for (int x = 0; x < width; x++) {
			System.out.print(tiles[x][y] + "\t"); //Would be drawing the tile at x, y on the map
		}
		System.out.println();
		
	}
	*/
	return tiles;
	
	}
	
	
	
}

	