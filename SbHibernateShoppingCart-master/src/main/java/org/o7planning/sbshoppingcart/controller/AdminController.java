package org.o7planning.sbshoppingcart.controller;
 
import java.io.IOException;
import java.util.List;
 
import org.apache.commons.lang.exception.ExceptionUtils;
import org.o7planning.sbshoppingcart.dao.*;
import org.o7planning.sbshoppingcart.entity.*;
import org.o7planning.sbshoppingcart.form.*;
import org.o7planning.sbshoppingcart.model.*;
import org.o7planning.sbshoppingcart.pagination.PaginationResult;
import org.o7planning.sbshoppingcart.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@Transactional
public class AdminController {
 
   @Autowired
   private OrderDAO orderDAO;
 
   @Autowired
   private BanhDAO banhDAO;
 
   @Autowired
   private BanhFormValidator banhFormValidator;
   @Autowired
   private LoaiBanhDAO loaiBanhDAO;
   @Autowired
   private LoaiBanhFormValidator loaiBanhFormValidator;
 
   @InitBinder
   public void myInitBinder(WebDataBinder dataBinder) {
      Object target = dataBinder.getTarget();
      if (target == null) {
         return;
      }
      System.out.println("Target=" + target);
 
      if (target.getClass() == BanhForm.class) {
         dataBinder.setValidator(banhFormValidator);
      }
   }
 
   // GET: Show Login Page
   @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
   public String login(Model model) {
 
      return "login";
   }
 
   @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
   public String accountInfo(Model model) {
 
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      System.out.println(userDetails.getPassword());
      System.out.println(userDetails.getUsername());
      System.out.println(userDetails.isEnabled());
 
      model.addAttribute("userDetails", userDetails);
      return "accountInfo";
   }
 
   @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
   public String orderList(Model model, //
         @RequestParam(value = "page", defaultValue = "1") String pageStr) {
      int page = 1;
      try {
         page = Integer.parseInt(pageStr);
      } catch (Exception e) {
      }
      final int MAX_RESULT = 5;
      final int MAX_NAVIGATION_PAGE = 10;
 
      PaginationResult<OrderInfo> paginationResult //
            = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);
 
      model.addAttribute("paginationResult", paginationResult);
      return "orderList";
   }
 
   // GET: Show product.
   @RequestMapping(value = { "/admin/banh" }, method = RequestMethod.GET)
   public String product(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
      BanhForm banhForm = null;
 
      if (code != null && code.length() > 0) {
         Banh banh = banhDAO.findBanh(code);
         if (banh != null) {
            banhForm = new BanhForm(banh);
         }
      }
      if (banhForm == null) {
         banhForm = new BanhForm();
         banhForm.setNewBanh(true);
      }
      model.addAttribute("banhForm", banhForm);
      return "banh";
   }
   // POST: Save product
   @RequestMapping(value = { "/admin/banh" }, method = RequestMethod.POST)
  /* public String banhSave(Model model, //
                          @ModelAttribute("banhForm") @Validated BanhForm banhForm, //
                          BindingResult result, //
                          final RedirectAttributes redirectAttributes) {

      if (result.hasErrors()) {
         return "banh";
      }
      try {
         banhDAO.save(banhForm);
      } catch (Exception e) {
         Throwable rootCause = ExceptionUtils.getRootCause(e);
         String message = rootCause.getMessage();
         model.addAttribute("errorMessage", message);
         // Show loaiBanh form.
         return "banh";
      }
      return "redirect:/loaiBanhList";
   } */
   // POST: Save product
   public String banhSave(Model model, //
                          @RequestParam(value = "code", defaultValue = "") String code,
                          @RequestParam(value = "name", defaultValue = "") String name,
                          @RequestParam(value = "loaiBanh", defaultValue = "") String loaiBanh,
                          @RequestParam(value = "price", defaultValue = "") Double price
                          //@RequestParam(value = "image", defaultValue = "") MultipartFile image,
                         )
   {
         boolean newB = false;
         Banh banh = null;
              banh = banhDAO.findBanh(code);
         if(banh== null)
         {
           newB = true;
         }
         banh.setCode(code);
         banh.setName(name);
         banh.setPrice(price);
         banh.setLoaiBanh(loaiBanhDAO.findLoaiBanhName(loaiBanh));
         if(newB) banhDAO.insert(banh);
         else banhDAO.update(banh);
      return "redirect:/loaiBanhList";
   }

   // GET: Show loaiBanh.
   @RequestMapping(value = { "/admin/loaiBanh" }, method = RequestMethod.GET)
   public String loaiBanh(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
      LoaiBanhForm loaiBanhForm = null;

      if (code != null && code.length() > 0) {
         LoaiBanh LBanh = loaiBanhDAO.findLoaiBanh(code) ;
         if (LBanh != null) {
            loaiBanhForm = new LoaiBanhForm(LBanh);
         }
      }
      if (loaiBanhForm == null) {
         loaiBanhForm = new LoaiBanhForm();
         loaiBanhForm.setNewLoaiBanh(true);
      }
      model.addAttribute("loaiBanhForm", loaiBanhForm);
      return "loaiBanh";
   }

   // POST: Save LoaiBanh
   @RequestMapping(value = { "/admin/loaiBanh" }, method = RequestMethod.POST)
   public String loaiBanhSave(Model model, //
                             @ModelAttribute("loaiBanhForm") @Validated LoaiBanhForm loaiBanhForm, //
                             BindingResult result, //
                             final RedirectAttributes redirectAttributes) {

      if (result.hasErrors()) {
         return "loaiBanh";
      }
      try {
         loaiBanhDAO.save(loaiBanhForm);
      } catch (Exception e) {
         Throwable rootCause = ExceptionUtils.getRootCause(e);
         String message = rootCause.getMessage();
         model.addAttribute("errorMessage", message);
         // Show loaiBanh form.
         return "loaiBanh";
      }
      return "redirect:/loaiBanhList";
   }
   @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
   public String orderView(Model model, @RequestParam("orderId") String orderId) {
      OrderInfo orderInfo = null;
      if (orderId != null) {
         orderInfo = this.orderDAO.getOrderInfo(orderId);
      }
      if (orderInfo == null) {
         return "redirect:/admin/orderList";
      }
      List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
      orderInfo.setDetails(details);
 
      model.addAttribute("orderInfo", orderInfo);
 
      return "order";
   }
 
}