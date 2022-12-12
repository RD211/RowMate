package nl.tudelft.sem.project.notifications.config;

import nl.tudelft.sem.project.entities.notifications.mailTemplates.MailTemplateImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailTemplatesBean {

    @Bean
    public MailTemplateImpl getMailActivityFull() {
        return new MailTemplateImpl();
    }
}
