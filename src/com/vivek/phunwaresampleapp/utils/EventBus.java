package com.vivek.phunwaresampleapp.utils;

import com.squareup.otto.Bus;

public final class EventBus {
	  private static final Bus BUS = new Bus();

	  public static Bus getInstance() {
	    return BUS;
	  }

	  private EventBus() {
	    // No instances.
	  }
}
