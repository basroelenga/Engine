package main;

import engine.Engine;

public class Main {

	public static void main(String[] args) {
	
		new Engine(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}