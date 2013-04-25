package com.arcadiaprep.app.arca.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;

import com.arcadiaprep.app.arca.event.ApplicationEvent;
import com.arcadiaprep.app.arca.listener.ApplicationEventListener;

public class EventDispatcher {
	private static EventDispatcher _instance = null;
	private List<ApplicationEventListener> mListeners = new ArrayList<ApplicationEventListener>();
    private static final String TAG = "EventDispatcher.singleton";

	public EventDispatcher() {
	}
	
	public static EventDispatcher getInstance () {
		if (_instance == null) {
			_instance = new EventDispatcher();
		}
	        
		return _instance;
	}
	
	public synchronized void addEventListener(ApplicationEventListener listener)  {
		Log.d(TAG, "addEventListener");
		mListeners.add(listener);
	}

	public synchronized void removeEventListener(ApplicationEventListener listener)   {
		Log.d(TAG, "removeEventListener");
		mListeners.remove(listener);
	}
	
	// call this method whenever you want to notify
	// the event listeners of the particular event
	public synchronized void fireEvent( ApplicationEvent event ) {
		Iterator<ApplicationEventListener> i = mListeners.iterator();
		while(i.hasNext())  {
			((ApplicationEventListener) i.next()).onApplicationEvent( event );
		}
	}

}
