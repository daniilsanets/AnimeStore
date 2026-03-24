package sanets.dev.catalogservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sanets.dev.catalogservice.dto.ProductFilter;
import sanets.dev.catalogservice.entity.Product;

import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findById(Long id);
}
