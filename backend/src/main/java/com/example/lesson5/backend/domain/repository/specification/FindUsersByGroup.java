package com.example.lesson5.backend.domain.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.Group;
//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.Group_;
//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.Membership;
//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.Membership_;
//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.User_;
//import org.debugroom.mynavi.sample.continuous.integration.backend.domain.model.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindUsersByGroup implements Specification<User> {

    private Group group;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        Join<User, Membership> joinMembership = root.join(User_.membershipsByUserId);
        Join<Membership, Group> joinGroup = joinMembership.join(Membership_.grpByGroupId);
        if(Objects.nonNull(group.getGroupName())){
            predicates.add(criteriaBuilder.equal(joinGroup.get(Group_.groupName), group.getGroupName()));
        } else {
            predicates.add(criteriaBuilder.equal(joinGroup.get(Group_.groupId), group.getGroupId()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
    }

}
