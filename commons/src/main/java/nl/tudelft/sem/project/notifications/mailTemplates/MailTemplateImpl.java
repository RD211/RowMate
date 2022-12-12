package nl.tudelft.sem.project.notifications.mailTemplates;

import lombok.*;
import nl.tudelft.sem.project.notifications.EventType;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class MailTemplateImpl implements MailTemplate {

    @NonNull
    String message;
    @NonNull
    String subject;
    @NonNull
    EventType eventType;
}
