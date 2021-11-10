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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindUsersHavingAddressOfZipCode implements Specification<User> {

    private String zipCd;

    @Override
    public Predicate toPredicate(Root<User> root,
                                 CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        Join<User, Address> joinAddress = root.join(User_.addressByUserId);
        predicates.add(criteriaBuilder.equal(joinAddress.get(Address_.zipCode), zipCd));

        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));

    }

}