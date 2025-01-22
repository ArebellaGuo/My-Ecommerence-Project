package com.project.Springboot_ecom_project.service;

import com.project.Springboot_ecom_project.payload.ProductDTO;
import com.project.Springboot_ecom_project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category);

    ProductDTO addProduct(ProductDTO productDTO, Long categoryId);


    ProductDTO updateProductById(Long productId, ProductDTO product);

    String deleteProductById(Long productId);


    ProductResponse findByCategoryId(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
