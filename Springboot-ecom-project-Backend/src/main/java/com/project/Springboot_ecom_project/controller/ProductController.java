package com.project.Springboot_ecom_project.controller;

import com.project.Springboot_ecom_project.config.PageInstants;
import com.project.Springboot_ecom_project.model.Category;
import com.project.Springboot_ecom_project.model.Product;
import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.ProductDTO;
import com.project.Springboot_ecom_project.payload.ProductResponse;
import com.project.Springboot_ecom_project.service.CategoryService;
import com.project.Springboot_ecom_project.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * user -> getallproducts, getproductbycategory,getproductByKeyword
 * admin -> add product,update product, delete product
 *
 */
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "keyword", required = false)String keyword,
            @RequestParam(name = "category", required = false)String category,
            @RequestParam(name = "pageNumber", defaultValue = PageInstants.PAGE_NUMBER,required = false)Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = PageInstants.PAGE_SIZE, required = false)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = PageInstants.SORT_PRODUCT_BY,required = false)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = PageInstants.SORT_ORDER,required = false)String sortOrder
    ){
        ProductResponse allProducts = productService.getAllProduct(pageNumber,pageSize,sortBy,sortOrder,keyword,category);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/seller/products/categories/{categoryId}")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO,
    @PathVariable Long categoryId){
        ProductDTO addedProductDTO = productService.addProduct(productDTO, categoryId);
        return new ResponseEntity<>(addedProductDTO,HttpStatus.OK);
    }

    @PutMapping("/seller/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
                                                    @PathVariable Long productId){
        ProductDTO updatedtProductDTO = productService.updateProductById(productId, productDTO);
        return new ResponseEntity<>(updatedtProductDTO,HttpStatus.OK);
    }


    @DeleteMapping("/seller/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
       String status= productService.deleteProductById(productId);
       return new ResponseEntity<>(status,HttpStatus.OK);
    }


    @GetMapping("/public/products/{categoryId}")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                  @RequestParam(name = "pageNumber", defaultValue = PageInstants.PAGE_NUMBER,required = false)Integer pageNumber,
                                                                  @RequestParam(name = "pageSize", defaultValue = PageInstants.PAGE_SIZE, required = false)Integer pageSize,
                                                                  @RequestParam(name = "sortBy",defaultValue = PageInstants.SORT_PRODUCT_BY,required = false)String sortBy,
                                                                  @RequestParam(name = "sortOrder",defaultValue = PageInstants.SORT_ORDER,required = false)String sortOrder
    ){
        ProductResponse productList = productService.findByCategoryId(categoryId,pageNumber,pageSize,sortBy,sortOrder);

        return new ResponseEntity<>(productList,HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
   @RequestParam(name = "pageNumber", defaultValue = PageInstants.PAGE_NUMBER,required = false)Integer pageNumber,
   @RequestParam(name = "pageSize",defaultValue = PageInstants.PAGE_SIZE,required = false)Integer pageSize,
   @RequestParam(name = "sortBy", defaultValue = PageInstants.SORT_PRODUCT_BY, required = false)String sortBy,
   @RequestParam(name = "sortOrder", defaultValue = PageInstants.SORT_ORDER,required = false)String sortOrder)
    {
        ProductResponse productResponse = productService.searchProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/seller/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }



}
