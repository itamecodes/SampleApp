package com.vivek.phunwaresampleapp.utils;

import com.squareup.otto.Bus;
/**
 * A singleton used to initialize the eventbus.
 * The getinstance is used to access the BUS
 * 
 * This makes use of Otto-An event bus
 * 
 * @author vivek
 */
public final class EventBus {
	  private static final Bus BUS = new Bus();

	  public static Bus getInstance() {
	    return BUS;
	  }
	  
	  private EventBus() {
	  }
}
