package sanets.dev.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sanets.dev.catalogservice.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

