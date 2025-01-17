package com.beckman.keycloak.piidataencryption.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.keycloak.models.jpa.entities.UserEntity;

@Entity
@Table(name = "USER_ENTITY_ENCRYPTED")
public class EncryptedUserEntity {

    @Id
    @Column(name = "ID", length = 36)
    protected String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected UserEntity user;

    @Column(name = "EMAIL", length = 500)
    protected String email;
    
    // This is necessary to be able to dynamically switch unique email constraints on and off in the realm settings
    @Column(name = "EMAIL_CONSTRAINT", length = 500)
    protected String emailConstraint;

    @Column(name = "USERNAME", length = 500)
    protected String username;

    @Column(name = "FIRST_NAME", length = 500)
    protected String firstName;

    @Column(name = "LAST_NAME", length = 500)
    protected String lastName;

    public EncryptedUserEntity() {
    }

    public EncryptedUserEntity(String id, UserEntity user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailConstraint() {
        return emailConstraint;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailConstraint(String emailConstraint) {
        this.emailConstraint = emailConstraint;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
