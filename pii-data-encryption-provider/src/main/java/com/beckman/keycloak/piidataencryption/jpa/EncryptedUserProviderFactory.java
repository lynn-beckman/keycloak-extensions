package com.beckman.keycloak.piidataencryption.jpa;

import jakarta.persistence.EntityManager;
import org.keycloak.Config;
import org.keycloak.connections.jpa.JpaConnectionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.UserProviderFactory;

public class EncryptedUserProviderFactory implements UserProviderFactory<EncryptedUserProvider>  {

    @Override
    public EncryptedUserProvider create(KeycloakSession ks) {
        EntityManager em = ks.getProvider(JpaConnectionProvider.class).getEntityManager();
        return new EncryptedUserProvider(ks, em);
    }

    @Override
    public void init(Config.Scope scope) {
    }

    @Override
    public void postInit(KeycloakSessionFactory ksf) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "jpa-encrypted";
    }

}
