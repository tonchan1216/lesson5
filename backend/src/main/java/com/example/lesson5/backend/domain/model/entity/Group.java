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
@Table(name = "grp", schema = "public", catalog = "sample")
public class Group {
    private long groupId;
    private String groupName;
    private Integer ver;
    private Timestamp lastUpdatedAt;
    private Collection<Membership> membershipsByGroupId;

    @Id
    @Column(name = "group_id", nullable = false)
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Basic
    @Column(name = "group_name", nullable = true, length = 512)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "ver", nullable = true)
    @Version
    public Integer getVer() {
        return ver;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    @Basic
    @Column(name = "last_updated_at", nullable = true)
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
        Group group = (Group) o;
        return groupId == group.groupId ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(groupId, groupName, ver, lastUpdatedAt);
    }

    @OneToMany(mappedBy = "grpByGroupId", cascade = CascadeType.ALL, orphanRemoval = true)
    public Collection<Membership> getMembershipsByGroupId() {
        return membershipsByGroupId;
    }

    public void setMembershipsByGroupId(Collection<Membership> membershipsByGroupId) {
        this.membershipsByGroupId = membershipsByGroupId;
    }
}
