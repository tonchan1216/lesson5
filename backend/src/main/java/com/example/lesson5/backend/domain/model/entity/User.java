package com.example.lesson5.backend.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usr", schema = "public", catalog = "sample")
public class User {
    private long userId;
    private String firstName;
    private String familyName;
    private String loginId;
    private Boolean isLogin;
    private Integer ver;
    private Timestamp lastUpdatedAt;
    private Address addressByUserId;
    private Collection<Email> emailsByUserId;
    private Collection<Membership> membershipsByUserId;

    @Id
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "first_name", length = 512)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "family_name", length = 512)
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Basic
    @Column(name = "login_id", length = 32)
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Basic
    @Column(name = "is_login")
    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }

    @Basic
    @Column(name = "ver")
    @Version
    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    @Basic
    @Column(name = "last_updated_at")
    public Timestamp getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Timestamp lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, firstName, familyName, loginId, isLogin, ver, lastUpdatedAt);
    }

    @OneToOne(mappedBy = "usrByUserId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Address getAddressByUserId() {
        return addressByUserId;
    }

    public void setAddressByUserId(Address addressByUserId) {
        this.addressByUserId = addressByUserId;
    }

    @OneToMany(mappedBy = "usrByUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Collection<Email> getEmailsByUserId() {
        return emailsByUserId;
    }

    public void setEmailsByUserId(Collection<Email> emailsByUserId) {
        this.emailsByUserId = emailsByUserId;
    }

    @OneToMany(mappedBy = "usrByUserId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Collection<Membership> getMembershipsByUserId() {
        return membershipsByUserId;
    }

    public void setMembershipsByUserId(Collection<Membership> membershipsByUserId) {
        this.membershipsByUserId = membershipsByUserId;
    }
}
