package com.example.lesson5.backend.domain.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.example.lesson5.backend.domain.model.entity.*;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindUsersByNotGroup implements Specification<User> {

    private Group group;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        Path<Object> path = root.get("userId");
        Subquery<User> subQuery = criteriaBuilder.createQuery().subquery(User.class);
        Root<User> subQueryRoot = subQuery.from(User.class);
        Join<User, Membership> subQueryJoinMembership = subQueryRoot.join(User_.membershipsByUserId);
        Join<Membership, Group> subQueryJoinGroup = subQueryJoinMembership.join(Membership_.grpByGroupId);
        Predicate subQueryPredicate = null;
        if(Objects.nonNull(group.getGroupName())){
            subQueryPredicate = criteriaBuilder.equal(
                    subQueryJoinGroup.get(Group_.groupName), group.getGroupName());
        }else {
            subQueryPredicate = criteriaBuilder.equal(
                    subQueryJoinGroup.get(Group_.groupId), group.getGroupId());
        }
        subQuery.select(subQueryRoot.get("userId"));
        subQuery.where(subQueryPredicate);

        return criteriaBuilder.not(criteriaBuilder.in(path).value(subQuery));
    }
}
