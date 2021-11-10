package com.example.lesson5.backend.domain.repository.specification;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.Address_;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.model.entity.User_;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindUsersNotHavingAddressOfZipCode implements Specification<User> {

    private String zipCd;

    @Override
    public Predicate toPredicate(Root<User> root,
                                 CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Path<Object> path = root.get("userId");
        Subquery<User> subQuery = criteriaBuilder.createQuery().subquery(User.class);
        Root<User> subQueryRoot = subQuery.from(User.class);
        Join<User, Address> subQueryJoinAddress = subQueryRoot.join(User_.addressByUserId);
        Predicate subQueryPredicate = criteriaBuilder.equal(
                subQueryJoinAddress.get(Address_.zipCode), zipCd);
        subQuery.select(subQueryRoot.get("userId"));
        subQuery.where(subQueryPredicate);

        return criteriaBuilder.not(criteriaBuilder.in(path).value(subQuery));

    }
}