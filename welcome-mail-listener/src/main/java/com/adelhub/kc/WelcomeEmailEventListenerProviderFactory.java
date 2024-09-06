package com.adelhub.kc;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeEmailEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeEmailEventListenerProvider.class);


    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new WelcomeEmailEventListenerProvider(session);
    }

    @Override
    public void init(Config.Scope config) {
        logger.info("Initialized welcome email provider");
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return "welcome-email-listener";
    }
}
