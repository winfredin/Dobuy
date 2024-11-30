package com.ShoppingCartList.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeVO;

@Controller
@RequestMapping("/shoppingcartlist")
public class ShoppingCartListController {

    @Autowired
    ShoppingCartListService shoppingCartListSvc;

    /*
     * This method will serve as addShoppingCart.html handler.
     */
    @GetMapping("addShoppingCartList")
    public String addShoppingCart(ModelMap model) {
        ShoppingCartListVO shoppingCartListVO = new ShoppingCartListVO();
        model.addAttribute("shoppingCartListVO", shoppingCartListVO);
        return "front-end/shoppingcartlist/addShoppingCartList";
    }

    /*
     * This method will be called on addShoppingCart.html form submission, handling POST request It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid ShoppingCartListVO shoppingCartListVO, BindingResult result, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "front-end/shoppingcartlist/addShoppingCartList";
        }
        
        /*************************** 2.開始新增資料 *****************************************/
        shoppingCartListSvc.addShoppingCartList(shoppingCartListVO);
        
        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        model.addAttribute("shoppingCartListListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/shoppingcartlist/listAllShoppingCartList"; // 新增成功後重導至listAllShoppingCart.html
    }

    /*
     * This method will be called on listAllShoppingCart.html form submission, handling POST request
     */
    @PostMapping("getOne_For_Update")
    public String getOneForUpdate(@RequestParam("shoppingcartListNo") String shoppingcartListNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        ShoppingCartListVO shoppingCartListVO = shoppingCartListSvc.getOneShoppingCartList(Integer.valueOf(shoppingcartListNo));
        
        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("shoppingCartListVO", shoppingCartListVO);
        return "front-end/shoppingcartlist/update_shoppingcartlist_input"; // 查詢完成後轉交updateShoppingCart.html
    }

    /*
     * This method will be called on updateShoppingCart.html form submission, handling POST request It also validates the user input
     */
    @PostMapping("update")
    public String update(@Valid ShoppingCartListVO shoppingCartListVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "front-end/shoppingcartlist/update_shoppingcartlist_input";
        }
        
        /*************************** 2.開始修改資料 *****************************************/
        shoppingCartListSvc.updateShoppingCartList(shoppingCartListVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        shoppingCartListVO = shoppingCartListSvc.getOneShoppingCartList(Integer.valueOf(shoppingCartListVO.getShoppingcartListNo()));
        model.addAttribute("shoppingCartListVO", shoppingCartListVO);
        return "front-end/shoppingcartlist/listOneShoppingCartList"; // 修改成功後轉交listOneShoppingCart.html
    }

    /*
     * This method will be called on listAllShoppingCart.html form submission, handling POST request
     */
    @PostMapping("delete")
    public String delete(@RequestParam("shoppingcartListNo") String shoppingcartListNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        shoppingCartListSvc.deleteShoppingCartList(Integer.valueOf(shoppingcartListNo));
        
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        model.addAttribute("shoppingCartListListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "front-end/shoppingcartlist/listAllShoppingCartList"; // 刪除完成後轉交listAllShoppingCart.html
    }

    /*
     * 第一種作法 Method used to populate the List Data in view.
     */
    @ModelAttribute("shoppingCartListListData")
    protected List<ShoppingCartListVO> referenceListData() {
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        return list;
    }
    /*
     * Populate the Map Data for goods dropdown in view.
     */
    @ModelAttribute("shoppingCartListMapData")
    protected Map<Integer, String> referenceMapData() {
        Map<Integer, String> map = new LinkedHashMap<>();
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll(); // Assuming you have a GoodsService
        for (ShoppingCartListVO shoppingCartList : list) {
            map.put(shoppingCartList.getShoppingcartListNo(), shoppingCartList.getGoodsName());
        }
        return map;
    }

    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(ShoppingCartListVO shoppingCartListVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(shoppingCartListVO, "shoppingCartListVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    /*
     * This method will be called on select_page.html form submission, handling POST request
     */
    @PostMapping("listShoppingCartLists_ByCompositeQuery")
    public String listAllShoppingCart(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll(map);
        model.addAttribute("shoppingCartListListData", list); // for listAllShoppingCart.html
        return "front-end/shoppingcartlist/listAllShoppingCartList";
    }
}
