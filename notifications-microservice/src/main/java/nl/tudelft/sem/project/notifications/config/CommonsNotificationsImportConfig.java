package nl.tudelft.sem.project.notifications.config;

import nl.tudelft.sem.project.notifications.mailTemplates.config.MailTemplateCollectionConfig;
import nl.tudelft.sem.project.notifications.mailTemplates.config.MailTemplateConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties
@Import(value = {MailTemplateCollectionConfig.class, MailTemplateConfig.class})
public class CommonsNotificationsImportConfig {

}
