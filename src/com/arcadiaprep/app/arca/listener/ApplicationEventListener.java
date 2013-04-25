package com.arcadiaprep.app.arca.listener;

import com.arcadiaprep.app.arca.event.ApplicationEvent;

public interface ApplicationEventListener {

    public void onApplicationEvent( ApplicationEvent event );

}
