package com.kaziamyr.onlinebookstore.specification;

import com.kaziamyr.onlinebookstore.model.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationProvider {
    public Specification<Book> getSpecification(List<String> params, String fieldName) {
        return (root, query, criteriaBuilder) -> {
            CriteriaBuilder.In<String> inPredicate = criteriaBuilder.in(root.get(fieldName));
            params.forEach(inPredicate::value);
            return criteriaBuilder.and(inPredicate);
        };
    }
}