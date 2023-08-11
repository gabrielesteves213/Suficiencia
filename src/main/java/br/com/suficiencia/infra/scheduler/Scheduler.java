package br.com.suficiencia.infra.scheduler;

import br.com.suficiencia.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;


@Service
public class Scheduler {

    @Autowired
    private SystemService systemService;

    @EventListener(ApplicationEvent.class)
    public void initScheduler() {
        systemService.createRoles();
        systemService.createAdminUser();
    }

}
