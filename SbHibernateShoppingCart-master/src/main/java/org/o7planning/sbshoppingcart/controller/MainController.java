package org.o7planning.sbshoppingcart.controller;
 
import java.io.IOException;
import java.util.List;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.o7planning.sbshoppingcart.dao.*;
import org.o7planning.sbshoppingcart.entity.*;
import org.o7planning.sbshoppingcart.form.CustomerForm;
import org.o7planning.sbshoppingcart.model.*;
import org.o7planning.sbshoppingcart.pagination.PaginationResult;
import org.o7planning.sbshoppingcart.utils.Utils;
import org.o7planning.sbshoppingcart.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@Transactional
public class MainController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private CustomerFormValidator customerFormValidator;
    @Autowired
    private LoaiBanhDAO loaiBanhDAO;

    @Autowired
    private BanhDAO banhDAO;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        // Case update quantity in cart
        // (@ModelAttribute("cartForm") @Validated CartInfo cartForm)
        if (target.getClass() == CartInfo.class) {

        }

        // Case save customer information.
        // (@ModelAttribute @Validated CustomerInfo customerForm)
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }

    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "TiemBanh";
    }

    @RequestMapping("/TintucSuKien")
    public String tintuc() {return "TintucSuKien";}


 @RequestMapping(value = { "/selectBanh" },method = RequestMethod.GET )
    public String listBanh1Handler(Model model, //
                                     @RequestParam(value = "name", defaultValue = "") String name,
                                     @RequestParam(value = "page", defaultValue = "1") int page){
        final int maxResult = 12;
        final int maxNavigationPage = 10;
        PaginationResult<BanhInfo> result = banhDAO.queryBanhs(page, //
                maxResult, maxNavigationPage, name);

        model.addAttribute("paginationBanhs", result);
        return "selectBanh";
    }
    @RequestMapping(value = { "/banhList" }, method = RequestMethod.GET)
    public String listBanhHandler(Model model, //
                                  @RequestParam(value = "code", defaultValue = "") String code)
         {
     LoaiBanhInfo loaiBanhInfo = null;
     if (code != null && code.length() > 0) {
         loaiBanhInfo = this.loaiBanhDAO.findLoaiInfo(code);
     }
     if (loaiBanhInfo == null) {
         return "redirect:/loaiBanhList";
     }
     List<BanhInfo> banhs = this.loaiBanhDAO.listBanhInfos(code);
     loaiBanhInfo.setBanhs(banhs);
     model.addAttribute("loaiBanhInfo", loaiBanhInfo);
         return "banhList";
 }
   @RequestMapping({ "/loaiBanhList" })
   public String listLoaiBanhHandler(Model model, //
                                    @RequestParam(value = "name", defaultValue = "") String likeName,
                                    @RequestParam(value = "page", defaultValue = "1") int page) {
      final int maxResult = 12;
      final int maxNavigationPage = 10;
      PaginationResult<LoaiBanhInfo> result = loaiBanhDAO.queryLoaiBanhs(page, //
              maxResult, maxNavigationPage, likeName);

      model.addAttribute("paginationLoaiBanhs", result);
      return "loaiBanhList";
   }
 
   @RequestMapping({ "/buyProduct" })
   public String listProductHandler(HttpServletRequest request, Model model, //
         @RequestParam(value = "code", defaultValue = "") String code) {
 
      Banh banh = null;
      if (code != null && code.length() > 0) {
         banh = banhDAO.findBanh(code);
      }
      if (banh != null) {
 
         //
         CartInfo cartInfo = Utils.getCartInSession(request);
 
         BanhInfo banhInfo = new BanhInfo(banh);
 
         cartInfo.addProduct(banhInfo, 1);
      }
 
      return "redirect:/shoppingCart";
   }
 
   @RequestMapping({ "/shoppingCartRemoveProduct" })
   public String removeProductHandler(HttpServletRequest request, Model model, //
         @RequestParam(value = "code", defaultValue = "") String code) {
      Banh banh = null;
      if (code != null && code.length() > 0) {
         banh = banhDAO.findBanh(code);
      }
      if (banh != null) {
 
         CartInfo cartInfo = Utils.getCartInSession(request);
 
         BanhInfo banhInfo = new BanhInfo(banh);
 
         cartInfo.removeBanh(banhInfo);
 
      }
 
      return "redirect:/shoppingCart";
   }
 
   // POST: Update quantity for product in cart
   @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
   public String shoppingCartUpdateQty(HttpServletRequest request, //
         Model model, //
         @ModelAttribute("cartForm") CartInfo cartForm) {
 
      CartInfo cartInfo = Utils.getCartInSession(request);
      cartInfo.updateQuantity(cartForm);
 
      return "redirect:/shoppingCart";
   }
 
   // GET: Show cart.
   @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
   public String shoppingCartHandler(HttpServletRequest request, Model model) {
      CartInfo myCart = Utils.getCartInSession(request);
 
      model.addAttribute("cartForm", myCart);
      return "shoppingCart";
   }
 
   // GET: Enter customer information.
   @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
   public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
 
      CartInfo cartInfo = Utils.getCartInSession(request);
 
      if (cartInfo.isEmpty()) {
 
         return "redirect:/shoppingCart";
      }
      CustomerInfo customerInfo = cartInfo.getCustomerInfo();
 
      CustomerForm customerForm = new CustomerForm(customerInfo);
 
      model.addAttribute("customerForm", customerForm);
 
      return "shoppingCartCustomer";
   }
 
   // POST: Save customer information.
   @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
   public String shoppingCartCustomerSave(HttpServletRequest request, //
         Model model, //
         @ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
         BindingResult result, //
         final RedirectAttributes redirectAttributes) {
 
      if (result.hasErrors()) {
         customerForm.setValid(false);
         // Forward to reenter customer info.
         return "shoppingCartCustomer";
      }
 
      customerForm.setValid(true);
      CartInfo cartInfo = Utils.getCartInSession(request);
      CustomerInfo customerInfo = new CustomerInfo(customerForm);
      cartInfo.setCustomerInfo(customerInfo);
 
      return "redirect:/shoppingCartConfirmation";
   }
 
   // GET: Show information to confirm.
   @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
   public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
      CartInfo cartInfo = Utils.getCartInSession(request);
 
      if (cartInfo == null || cartInfo.isEmpty()) {
 
         return "redirect:/shoppingCart";
      } else if (!cartInfo.isValidCustomer()) {
 
         return "redirect:/shoppingCartCustomer";
      }
      model.addAttribute("myCart", cartInfo);
 
      return "shoppingCartConfirmation";
   }
 
   // POST: Submit Cart (Save)
   @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
 
   public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
      CartInfo cartInfo = Utils.getCartInSession(request);
 
      if (cartInfo.isEmpty()) {
 
         return "redirect:/shoppingCart";
      } else if (!cartInfo.isValidCustomer()) {
 
         return "redirect:/shoppingCartCustomer";
      }
      try {
         orderDAO.saveOrder(cartInfo);
      } catch (Exception e) {
 
         return "shoppingCartConfirmation";
      }
 
      // Remove Cart from Session.
      Utils.removeCartInSession(request);
 
      // Store last cart.
      Utils.storeLastOrderedCartInSession(request, cartInfo);
 
      return "redirect:/shoppingCartFinalize";
   }
 
   @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
   public String shoppingCartFinalize(HttpServletRequest request, Model model) {
 
      CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);
 
      if (lastOrderedCart == null) {
         return "redirect:/shoppingCart";
      }
      model.addAttribute("lastOrderedCart", lastOrderedCart);
      return "shoppingCartFinalize";
   }
   @RequestMapping(value = { "/loaiBanhImage" }, method = RequestMethod.GET)
   public void loaiBanhImage(HttpServletRequest request, HttpServletResponse response, Model model,
                            @RequestParam("code") String code) throws IOException {
      LoaiBanh loaiBanh = null;
      if (code != null) {
         loaiBanh = this.loaiBanhDAO.findLoaiBanh(code);
      }
      if (loaiBanh != null && loaiBanh.getImage() != null) {
         response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
         response.getOutputStream().write(loaiBanh.getImage());
      }
      response.getOutputStream().close();
   }
    @RequestMapping(value = { "/banhImage" }, method = RequestMethod.GET)
    public void banhImage(HttpServletRequest request, HttpServletResponse response, Model model,
                             @RequestParam("code") String code) throws IOException {
        Banh banh = null;
        if (code != null) {
            banh = this.banhDAO.findBanh(code);
        }
        if (banh != null && banh.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(banh.getImage());
        }
        response.getOutputStream().close();
    }
}