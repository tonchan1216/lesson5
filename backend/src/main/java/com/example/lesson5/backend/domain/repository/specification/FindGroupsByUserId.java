package com.example.lesson5.backend.domain.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.example.lesson5.backend.domain.model.entity.Group_;
import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.Membership_;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.model.entity.User_;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindGroupsByUserId implements Specification<Group> {

    private Long userId;

    @Override
    public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        Join<Group, Membership> membershipJoin = root.join(Group_.membershipsByGroupId);
        Join<Membership, User> userJoin = membershipJoin.join(Membership_.usrByUserId);
        predicates.add(criteriaBuilder.equal(userJoin.get(User_.userId), userId));

        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
    }

}
