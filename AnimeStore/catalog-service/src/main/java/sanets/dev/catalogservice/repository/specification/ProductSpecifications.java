package sanets.dev.catalogservice.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import sanets.dev.catalogservice.dto.ProductFilter;
import sanets.dev.catalogservice.entity.Product;


public class ProductSpecifications {
    public static Specification<Product> withFilter(ProductFilter filter){
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getName() != null) {
                predicate = cb.and(predicate,
                        cb.like(root.get("name"), "%" + filter.getName() + "%"));
            }

            if (filter.getPrice() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("price"), filter.getPrice()));
            }

            if (filter.getType() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("type"), filter.getType()));
            }

            if (filter.getCategoryId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            if (filter.getActive() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("active"), filter.getActive()));
            }

            return predicate;
        };

    }
}
