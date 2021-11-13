package com.example.lesson5.backend.domain.repository.specification;

import com.example.lesson5.backend.domain.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

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
