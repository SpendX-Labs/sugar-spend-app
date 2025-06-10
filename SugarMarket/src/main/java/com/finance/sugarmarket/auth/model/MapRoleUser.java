package com.finance.sugarmarket.auth.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class MapRoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mapRoleUserId")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private MFUser user;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roleId")
    private MFRole role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MFUser getUser() {
        return user;
    }

    public void setUser(MFUser user) {
        this.user = user;
    }

    public MFRole getRole() {
        return role;
    }

    public void setRole(MFRole role) {
        this.role = role;
    }

}