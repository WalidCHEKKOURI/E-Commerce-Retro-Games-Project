package net.kzn.retrogames.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.kzn.retrogames.util.FileUploadUtility;
import net.kzn.retrogames.validator.ProductValidator;
import net.kzn.retrogamesbackend.dao.CategoryDAO;
import net.kzn.retrogamesbackend.dao.ProductDAO;
import net.kzn.retrogamesbackend.dto.Category;
import net.kzn.retrogamesbackend.dto.Product;








@Controller
@RequestMapping("/manage")
public class ManagementController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ProductDAO productDAO;
	
	private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);
	
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
	public ModelAndView showManageProducts(@RequestParam(name="operation",required=false) String operation) {
		
		
	ModelAndView mv =new ModelAndView("page");
	mv.addObject("userClickManageProducts", true);
	
	mv.addObject("title", "Manage Products");
	Product nProduct =new Product();
	// set few of the fields
	nProduct.setSupplierId(1);
	nProduct.setActive(true);
	mv.addObject("product",nProduct);
	if(operation!=null) {
		
		if(operation.equals("product")) {
			
			
		mv.addObject("message", "Product Submitted Succesfully!");			
			
		}
		
		
	}
	
	
	return mv;
		
		
		
		
		
		
	}

	//handling product submission
	@RequestMapping(value="/products", method=RequestMethod.POST)
	public String handleProductSubmission(@Valid @ModelAttribute("product") Product mProduct, BindingResult results, Model model, HttpServletRequest request) {
		
		

		
		new ProductValidator().validate(mProduct, results);
		
		
		if(results.hasErrors()) {
			model.addAttribute("userClickManageProducts",true);
			model.addAttribute("title", "Manage Products");
			model.addAttribute("message", "Validation fails for adding the product!");
			return "page";
		}
		
		logger.info(mProduct.toString());
		// create a newproduct record
		productDAO.add(mProduct);
		
		
		if(!mProduct.getFile().getOriginalFilename().equals(""))
		{
			FileUploadUtility.uploadFile(request, mProduct.getFile(), mProduct.getCode());
		}
		
		
		return "redirect:/manage/products?operation=product";
	
	}
	
	
	
	

	@RequestMapping("/{id}/product")
	public ModelAndView showEditProduct(@PathVariable int id) {		

		ModelAndView mv = new ModelAndView("page");	
		mv.addObject("title","Product Management");		
		mv.addObject("userClickManageProduct",true);
		
		// Product nProduct = new Product();		
		mv.addObject("product", productDAO.get(id));

			
		return mv;
		
	}
	
	@RequestMapping(value = "/product/{id}/activation", method=RequestMethod.POST)
	@ResponseBody
	public String mhandleProductActivation(@PathVariable int id) 
	{		
		Product product = productDAO.get(id);
		boolean isActive = product.isActive();
		product.setActive(!isActive);
		productDAO.update(product);		
		return (isActive)? "Product Dectivated Successfully!": "Product Activated Successfully";
	}
	
	
// returnig categories for all the request mapping 
	@ModelAttribute("categories")
	public List<Category> getCategories(){
		
		return categoryDAO.list();
			
	}
}