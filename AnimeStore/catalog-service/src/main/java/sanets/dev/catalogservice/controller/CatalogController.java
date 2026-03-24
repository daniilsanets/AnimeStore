package sanets.dev.catalogservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sanets.dev.catalogservice.dto.*;
import sanets.dev.catalogservice.service.CatalogService;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/products")
    public Page<ProductDto> listProducts(
            @ModelAttribute ProductFilter filter,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return catalogService.listProducts(filter, pageable);
    }

    @GetMapping("/products/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return catalogService.getProduct(id);
    }

    @PostMapping("/products")
    public ProductDto createProduct(@RequestBody CreateProductRequest request) {
        return catalogService.createProduct(request);
    }

    @PutMapping("/products/{id}")
    public ProductDto updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request
    ) {
        return catalogService.updateProduct(id, request);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        catalogService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categories")
    public Page<CategoryDto> listCategories(@PageableDefault(size = 50) Pageable pageable) {
        return catalogService.listCategories(pageable);
    }

    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody CreateCategoryRequest request) {
        return catalogService.createCategory(request);
    }
}

