package nl.tudelft.sem.project.entities.notifications.mailTemplates.config;

import nl.tudelft.sem.project.entities.notifications.EventType;
import nl.tudelft.sem.project.entities.notifications.mailTemplates.MailTemplate;
import nl.tudelft.sem.project.entities.notifications.mailTemplates.MailTemplateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;

public class MailTemplateCollectionConfig {

    @Autowired
    private transient List<MailTemplateImpl> listMailTemplates;

    @Bean
    public HashMap<EventType, MailTemplateImpl> getMailTemplates() {
        HashMap<EventType, MailTemplateImpl> map = new HashMap<>();

        for (MailTemplateImpl template : listMailTemplates) {
            map.put(template.getEventType(), template);
        }
        return map;
    }
}
