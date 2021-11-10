package com.example.lesson5.backend.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailPK implements Serializable {
    private long userId;
    private long emailNo;

    @Column(name = "user_id", nullable = false)
    @Id
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name = "email_no", nullable = false)
    @Id
    public long getEmailNo() {
        return emailNo;
    }

    public void setEmailNo(long emailNo) {
        this.emailNo = emailNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailPK emailPK = (EmailPK) o;
        return userId == emailPK.userId &&
                emailNo == emailPK.emailNo;
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, emailNo);
    }
}
