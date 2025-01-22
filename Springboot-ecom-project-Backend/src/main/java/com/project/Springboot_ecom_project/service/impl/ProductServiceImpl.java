package com.project.Springboot_ecom_project.service.impl;

import com.project.Springboot_ecom_project.exception.APIException;
import com.project.Springboot_ecom_project.exception.ResourceNotFound;
import com.project.Springboot_ecom_project.model.Cart;
import com.project.Springboot_ecom_project.model.Category;
import com.project.Springboot_ecom_project.model.Product;
import com.project.Springboot_ecom_project.payload.CategoryDTO;
import com.project.Springboot_ecom_project.payload.ProductDTO;
import com.project.Springboot_ecom_project.payload.ProductResponse;
import com.project.Springboot_ecom_project.repository.CartRepository;
import com.project.Springboot_ecom_project.repository.CategoryRepository;
import com.project.Springboot_ecom_project.repository.ProductRepository;
import com.project.Springboot_ecom_project.service.CartService;
import com.project.Springboot_ecom_project.service.FileService;
import com.project.Springboot_ecom_project.service.ProductService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private FileService fileService;
    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;

    @Override
    public ProductResponse getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, String category) {
        Sort sortByAndOder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //configure page details
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOder);
        //create dynamic SQL based on filters/situations
        Specification<Product> specification = Specification.where(null);
        if (keyword != null && !keyword.isEmpty()){
            specification= specification.and((root, query,criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")),"%"+keyword.toLowerCase()+"%"));
        }

        if (category != null && !category.isEmpty()){
            specification= specification.and((root, query,criteriaBuilder) ->
                    criteriaBuilder.like(root.get("category").get("categoryName"),category));
        }
        
        //add page details to product list
        Page<Product> products = productRepository.findAll(specification,pageDetails);
        //get productlist from page
        List<Product> productList = products.getContent();

        //convert productlist to productDTOList
        List<ProductDTO> productDTOList = productList.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    if (product.getCategory() != null) {
                        CategoryDTO categoryDTO = modelMapper.map(product.getCategory(), CategoryDTO.class);
                        productDTO.setCategoryDTO(categoryDTO);
                    }
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                }).collect(Collectors.toList());
        //add productdtolist to productresponse
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(products.getNumber());
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setLastPage(products.isLast());
        return productResponse;
    }

    @Override
    @Transactional
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Product product = modelMapper.map(productDTO, Product.class);
        //check if categoryId is valid
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", categoryId));
        //search product in product repository
        Optional<Product> findProduct = productRepository.findByCategoryIdAndProductName(categoryId, product.getProductName());
        if (findProduct.isPresent()) {
            throw new APIException("Product already exists!");
        }
        //set up product category
        product.setCategory(category);
        //if product does not exist,add product in product repository
        productRepository.save(product);
        ProductDTO addedProductDTO = modelMapper.map(product, ProductDTO.class);
        CategoryDTO categoryDTO = modelMapper.map(product.getCategory(), CategoryDTO.class);
        addedProductDTO.setCategoryDTO(categoryDTO);
        return addedProductDTO;
    }

    @Override
    @Transactional
    public ProductDTO updateProductById(Long productId, ProductDTO productDTO) {
        //find product by id
        Product savedProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product", productId));
        //if product exists, update existing product
        savedProduct.setProductName(productDTO.getProductName());
        savedProduct.setDescription(productDTO.getDescription());
        savedProduct.setImage(productDTO.getImage());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setQuantity(productDTO.getQuantity());
        //validate categoryid
        if (productDTO.getCategoryDTO() != null) {
            Long categoryId = productDTO.getCategoryDTO().getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFound("Category", categoryId));
            savedProduct.setCategory(category);
        }

        //save updated product in product repository
        productRepository.save(savedProduct);

        //return productDTO
        CategoryDTO categoryDTO = modelMapper.map(savedProduct.getCategory(),CategoryDTO.class);
        ProductDTO savedProductDTO= modelMapper.map(savedProduct, ProductDTO.class);
        savedProductDTO.setCategoryDTO(categoryDTO);
        return savedProductDTO;
    }

    @Override
    @Transactional
    public String deleteProductById(Long productId) {
        //check if product exists in repository
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product", productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

        cartRepository.saveAll(carts);
        //if product exists, delete from repository
        productRepository.delete(product);
        return "Product with productId:" + productId + " is deleted successfully!";
    }

    @Override
    public ProductResponse findByCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //validate categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFound("Category", categoryId));

        //configure sort
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        //configure page details
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        List<Product> productList = productPage.getContent();
        //convert productList to productDTOList
        List<ProductDTO> productDTOList = productList.stream()
                .map(product ->{
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    CategoryDTO categoryDTO = modelMapper.map(product.getCategory(),CategoryDTO.class);
                    productDTO.setCategoryDTO(categoryDTO);
                    return productDTO;})
                .collect(Collectors.toList());
        //add productDTOList to productresponse
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //configure sort
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        //configure page
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageable);
        List<Product> productList = productPage.getContent();
        if (productList.isEmpty()) {
            throw new APIException("No product found with keyword :", keyword);
        }

        List<ProductDTO> productDTOList = productList.stream()
                .map(product ->{
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    CategoryDTO categoryDTO = modelMapper.map(product.getCategory(),CategoryDTO.class);
                    productDTO.setCategoryDTO(categoryDTO);
                    return productDTO;})
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOList);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product",  productId));

        String fileName = fileService.uploadImage(path, image);
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    private String constructImageUrl(String imageName) {
        return imageBaseUrl.endsWith("/") ? imageBaseUrl + imageName : imageBaseUrl + "/" + imageName;
    }


}
