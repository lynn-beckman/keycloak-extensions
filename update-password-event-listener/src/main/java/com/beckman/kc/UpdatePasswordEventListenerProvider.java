package com.beckman.kc;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailSenderProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.keycloak.events.admin.ResourceType;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordEventListenerProvider implements EventListenerProvider {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePasswordEventListenerProvider.class);

    private final KeycloakSession session;

    // Pebble engine and templates, loaded once statically
    private static final PebbleEngine pebbleEngine = new PebbleEngine.Builder().build();
    private static PebbleTemplate htmlTemplate;
    private static PebbleTemplate textTemplate;

    // Static block to load templates only once
    static {
        try {
            // Initialize Pebble templates
            htmlTemplate = pebbleEngine.getTemplate("templates/update-password-email.html");
            textTemplate = pebbleEngine.getTemplate("templates/update-password-email.txt");

        } catch (Exception e) {
            logger.error("Error loading email templates", e);
        }
    }

    public UpdatePasswordEventListenerProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        logger.info("Event received: {}", event.getType());
        if (event.getType().equals(EventType.UPDATE_CREDENTIAL)) {
            logger.info("User updated password with ID: {}", event.getUserId());
            UserModel user = session.users().getUserById(session.getContext().getRealm(), event.getUserId());
            sendUpdatePasswordEmail(user);
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        if (event.getResourceType() == ResourceType.USER && event.getOperationType() == OperationType.CREATE) {
            String userId = event.getResourcePath().split("/")[1];
            logger.info("User registered with ID: {}", userId);
            UserModel user = session.users().getUserById(session.getContext().getRealm(), userId);
            if (user != null) {
                sendUpdatePasswordEmail(user);
            }

        }

    }

    @Override
    public void close() {

    }

    private void sendUpdatePasswordEmail(UserModel user) {
        String firstName = user.getFirstName();
        // Prepare data for the template
        Map<String, Object> context = new HashMap<>();
        context.put("firstName", firstName);

        StringWriter htmlWriter = new StringWriter();
        StringWriter textWriter = new StringWriter();
        try {
            // Render the templates using Pebble
            htmlTemplate.evaluate(htmlWriter, context);
            textTemplate.evaluate(textWriter, context);
        } catch (Exception e) {
            logger.error("Error evaluating templates", e);
            return;
        }

        String htmlBody = htmlWriter.toString();
        String textBody = textWriter.toString();
        String subject = "Notification of MARS Password Change";

        // Send the email
        sendEmail(user, subject, htmlBody, textBody);
    }

    private void sendEmail(UserModel user, String subject, String htmlBody, String textBody) {

        EmailSenderProvider emailSender = session.getProvider(EmailSenderProvider.class);
        try {
            emailSender.send(session.getContext().getRealm().getSmtpConfig(), user, subject, textBody, htmlBody);
        } catch (EmailException e) {
            logger.error("Error sending email", e);
        }
    }

}
