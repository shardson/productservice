package com.shardson.springcloud.productservice.controller;

import com.shardson.springcloud.productservice.dto.Coupon;
import com.shardson.springcloud.productservice.model.Product;
import com.shardson.springcloud.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/productapi")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${couponService.url}")
    private String couponServiceURL;

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public Product create (@RequestBody Product product){

        System.out.println(" couponServiceURL = " + couponServiceURL);
        System.out.println(" product.getCouponCode= " + product.getCouponCode());

        Coupon coupon = restTemplate.getForObject(couponServiceURL + product.getCouponCode(), Coupon.class);

        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));

        return productRepository.save(product);
    };

    @RequestMapping(value = "/product/{name}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable("name") String name){
        return productRepository.findByName(name);
    };
}
