package com.bit.house.controller;

import com.bit.house.domain.*;
import com.bit.house.service.CategoryService;
import com.bit.house.service.ColorService;
import com.bit.house.service.ProductService;
import com.bit.house.service.SellerService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/seller")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    SellerService sellerService;

    @Autowired
    ColorService colorService;

    @Autowired
    ProductService productService;

    @GetMapping("/category")
    public String category(ObjectMapper mapper, Model model, @AuthenticationPrincipal Principal principal) {

        String id = principal.getName();
        SellerVO sellerInfo = sellerService.searchSellerInfo(id);
        model.addAttribute("sellerInfo", sellerInfo);

        mapper = new ObjectMapper();
        String jsonText;
        String jsonColor;

        List<ColorVO> color = colorService.searchColor();
        List<CategoryVO> category = categoryService.searchCategory();

        try {
            jsonText = mapper.writeValueAsString(category);
            jsonColor = mapper.writeValueAsString(color);

            model.addAttribute("jsonText", jsonText);
            model.addAttribute("jsonColor", jsonColor);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "th/seller/category";
    }


    @PostMapping("/addProduct")
    public String addProduct(MultipartFile productMainImgUrl, MultipartFile[] productSubImgUrl, MultipartFile productExpImgUrl,
                           HttpServletRequest request, String productName, String modelName, String sellerName, String customerPrice,
                           String sellPrice, String purchasePrice, String categoryCode, String[] optionColor, String[] productQty,
                           ProductVO productVO, ProductOptionVO productOptionVO) throws IOException {

        String uploadFolderSeller = request.getSession().getServletContext().getRealPath("image/product");
        String productMainImg = productMainImgUrl.getOriginalFilename();
        String productExpImg = productExpImgUrl.getOriginalFilename();

        for(int i=0; i<productSubImgUrl.length; i++){
            String productSubImg1 = productSubImgUrl[0].getOriginalFilename();
            String productSubImg2 = productSubImgUrl[1].getOriginalFilename();
            String productSubImg3 = productSubImgUrl[2].getOriginalFilename();

            productVO.setProductSubImg1("product/" + productSubImg1);
            productVO.setProductSubImg2("product/" + productSubImg2);
            productVO.setProductSubImg3("product/" + productSubImg3);

            File saveFile = new File(uploadFolderSeller, productSubImgUrl[i].getOriginalFilename());
            try {
                productSubImgUrl[i].transferTo(saveFile);

                log.info("success");

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        productVO.setProductNo(modelName+"-"+categoryCode);
        productVO.setSellerName(sellerName);
        productVO.setProductName(productName);
        productVO.setModelName(modelName);
        productVO.setCustomerPrice(Integer.parseInt(customerPrice));
        productVO.setSellPrice(Integer.parseInt(sellPrice));
        productVO.setPurchasePrice(Integer.parseInt(purchasePrice));
        productVO.setCategoryCode(categoryCode);
        productVO.setProductMainImg("product/" + productMainImg);
        productVO.setProductExpImg("product/" + productExpImg);

        productService.insertProduct(productVO);

        for(int j=0; j<optionColor.length; j++){
            productOptionVO.setProductOptionNo(modelName+"-"+categoryCode+"-"+optionColor[j]);
            productOptionVO.setProductNo(modelName+"-"+categoryCode);
            productOptionVO.setColorCode(optionColor[j]);
            productOptionVO.setProductQty(Integer.parseInt(productQty[j]));
            productService.insertProductOption(productOptionVO);
        }

        File saveFileMain = new File(uploadFolderSeller, productMainImg);
        productMainImgUrl.transferTo(saveFileMain);
        File saveFileExp = new File(uploadFolderSeller, productExpImg);
        productExpImgUrl.transferTo(saveFileExp);

        return "redirect:/seller/category";
    }

    @GetMapping("/productList")
    public String productList(Model model, @AuthenticationPrincipal Principal principal){
        String id = principal.getName();
        SellerVO sellerInfo = sellerService.searchSellerInfo(id);
        List<ProductVO> productListInfo = productService.searchProductListInfo(id);

        model.addAttribute("sellerInfo", sellerInfo);
        model.addAttribute("productListInfo", productListInfo);

        log.info(String.valueOf(productListInfo));
        return "th/seller/productList";
    }


    @GetMapping("/{productNo}")
    private String productChange(ObjectMapper mapper, @PathVariable String productNo, Model model, @AuthenticationPrincipal Principal principal) {

        String id = principal.getName();
        SellerVO sellerInfo = sellerService.searchSellerInfo(id);

        model.addAttribute("sellerInfo", sellerInfo);
        model.addAttribute("productTarget", productService.searchProductTarget(productNo, id));
        model.addAttribute("productColorList", productService.searchProductColorList(productNo, id));

        List<ColorVO> color = colorService.searchColor();
        List<CategoryVO> category = categoryService.searchCategory();

        mapper = new ObjectMapper();
        String jsonText;
        String jsonColor;

        try {
            jsonText = mapper.writeValueAsString(category);
            jsonColor = mapper.writeValueAsString(color);

            model.addAttribute("jsonText", jsonText);
            model.addAttribute("jsonColor", jsonColor);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "th/seller/productChange";
    }

    @PostMapping("/productChangeInfo")
    private String productChangeInfo(String categoryCode, String productName, String modelName, String customerPrice, String sellPrice, String purchasePrice,
    String[] optionColor, String[] productQty, MultipartFile productMainImgUrl, MultipartFile[] productSubImgUrl, MultipartFile productExpImgUrl,
                                   HttpServletRequest request, String productNo2, String[] productOptionNo2, String categoryCode2,
                                    String productMainImgBefore, String productSubImgBefore1, String productSubImgBefore2, String productSubImgBefore3, String productExpImgBefore) throws IOException {


        String uploadFolder = request.getSession().getServletContext().getRealPath("image/product");

        String productSubImg1 = productSubImgBefore1;
        String productSubImg2 = productSubImgBefore2;
        String productSubImg3 = productSubImgBefore3;

       if(!productSubImgUrl[0].isEmpty()) {
            for (int i = 0; i < productSubImgUrl.length; i++) {

                File saveFile = new File(uploadFolder, productSubImgUrl[i].getOriginalFilename());

                try {
                    productSubImgUrl[i].transferTo(saveFile);

                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                productSubImg1 = "product/" + productSubImgUrl[0].getOriginalFilename();
                productSubImg2 = "product/" + productSubImgUrl[1].getOriginalFilename();
                productSubImg3 = "product/" + productSubImgUrl[2].getOriginalFilename();

            }
        }

            String categoryCode3 = categoryCode;
            if (categoryCode == null) {
                categoryCode3 = categoryCode2;
                log.info(categoryCode3);
            }

            String productNo3 = modelName + "-" + categoryCode3;

            int customerPrice2 = Integer.parseInt(customerPrice);
            int sellPrice2 = Integer.parseInt(sellPrice);
            int purchasePrice2 = Integer.parseInt(purchasePrice);

            String productMainImg2 = productMainImgBefore;
            if (!productMainImgUrl.isEmpty()) {
                productMainImg2 = "product/" + productMainImgUrl.getOriginalFilename();
                File saveFileMain = new File(uploadFolder, productMainImgUrl.getOriginalFilename());
                productMainImgUrl.transferTo(saveFileMain);
            }

            String productExpImg2 = productExpImgBefore;
            if (!productExpImgUrl.isEmpty()) {
                productExpImg2 = "product/" + productExpImgUrl.getOriginalFilename();
                File saveFileExp = new File(uploadFolder, productExpImgUrl.getOriginalFilename());
                productExpImgUrl.transferTo(saveFileExp);

            }

            productService.updateProductInfo(productNo3, productName, modelName, customerPrice2, sellPrice2, purchasePrice2, categoryCode3, productMainImg2,
                    productSubImg1, productSubImg2, productSubImg3, productExpImg2, productNo2);


            for (int j = 0; j < optionColor.length; j++) {
                String productOptionNo = modelName + "-" + categoryCode + "-" + optionColor[j];

                productService.updateProductOption(productOptionNo, productNo3, optionColor[j], Integer.parseInt(productQty[j]), productOptionNo2[j]);
            }

        return "redirect:/seller/productList";
    }
}
