package sanets.dev.catalogservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import sanets.dev.catalogservice.dto.CategoryDto;
import sanets.dev.catalogservice.dto.CreateCategoryRequest;
import sanets.dev.catalogservice.dto.CreateProductRequest;
import sanets.dev.catalogservice.dto.ProductDto;
import sanets.dev.catalogservice.dto.ProductFilter;
import sanets.dev.catalogservice.dto.UpdateProductRequest;
import sanets.dev.catalogservice.entity.Category;
import sanets.dev.catalogservice.entity.Product;
import sanets.dev.catalogservice.repository.CatalogRepository;
import sanets.dev.catalogservice.repository.CategoryRepository;
import sanets.dev.catalogservice.repository.specification.ProductSpecifications;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductDto> listProducts(ProductFilter filter, Pageable pageable) {
        return catalogRepository
                .findAll(ProductSpecifications.withFilter(filter), pageable)
                .map(this::toProductDto);
    }

    public ProductDto getProduct(Long id) {
        return catalogRepository.findById(id)
                .map(this::toProductDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found: " + id));
    }

    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found: " + request.getCategoryId()));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .type(request.getType())
                .category(category)
                .active(request.getActive() == null ? true : request.getActive())
                .build();

        return toProductDto(catalogRepository.save(product));
    }

    @Transactional
    public ProductDto updateProduct(Long id, UpdateProductRequest request) {
        Product product = catalogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Product not found: " + id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Category not found: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setType(request.getType());
        product.setCategory(category);
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }

        return toProductDto(catalogRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!catalogRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Product not found: " + id);
        }
        catalogRepository.deleteById(id);
    }

    public Page<CategoryDto> listCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(this::toCategoryDto);
    }

    @Transactional
    public CategoryDto createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        return toCategoryDto(categoryRepository.save(category));
    }

    private ProductDto toProductDto(Product product) {
        Category category = product.getCategory();
        Long categoryId = category == null ? null : category.getId();

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getType(),
                categoryId,
                product.getActive()
        );
    }

    private CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
