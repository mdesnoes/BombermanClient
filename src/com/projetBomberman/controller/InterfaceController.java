package com.projetBomberman.controller;


public interface InterfaceController {

	void start();
	void step();
	void run();
	void stop();
	void quitter();
	void setTime(long time);
	long getTime();
	int getInitTime();

}
