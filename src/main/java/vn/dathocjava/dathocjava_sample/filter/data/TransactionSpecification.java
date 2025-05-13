package vn.dathocjava.dathocjava_sample.filter.data;

import org.springframework.data.jpa.domain.Specification;
import vn.dathocjava.dathocjava_sample.model.Transaction;

import java.util.Date;

public class TransactionSpecification {
    public static Specification<Transaction> hasCategoryId(Long categoryId){
        return (root, query, cb) -> categoryId == null ? null
                : cb.equal(root.get("category").get("id"), categoryId);
    }
    public static Specification<Transaction> transactionDate(Date from, Date to){
        return (root, query, cb) -> {
            if (from != null && to != null) {
                return cb.between(root.get("transactionDate"), from, to);
            }else if (from != null){
                return cb.greaterThanOrEqualTo(root.get("transactionDate"), from);

            } else if (to != null) {
                return cb.lessThanOrEqualTo(root.get("transactionDate"), to);
            } else {
                return null;
            }
        };

    }
}
