package aliceGlow.example.aliceGlow.controller;

import aliceGlow.example.aliceGlow.dto.product.CreateProductDTO;
import aliceGlow.example.aliceGlow.dto.product.ProductDTO;
import aliceGlow.example.aliceGlow.dto.product.UpdateProductDTO;
import aliceGlow.example.aliceGlow.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> listProducts(){
        return productService.listProducts();
    }

    @PostMapping
    public ProductDTO createProduct(@Valid @RequestBody CreateProductDTO createProductDTO){
        return productService.createProduct(createProductDTO);
    }

    @PatchMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductDTO updateProductDTO){
        return productService.updateProduct(id, updateProductDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
