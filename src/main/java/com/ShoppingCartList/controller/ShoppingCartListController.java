package com.ShoppingCartList.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.ShoppingCartList.model.ShoppingCartListRepository;
import com.ShoppingCartList.model.ShoppingCartListService;
import com.ShoppingCartList.model.ShoppingCartListVO;

@Controller
@RequestMapping("/shoppingcartlist")
public class ShoppingCartListController {

    @Autowired
    ShoppingCartListService shoppingCartListSvc;

    @Autowired
    ShoppingCartListRepository repository; // 添加這行
    /*
     * This method will serve as addShoppingCart.html handler.
     */
    @GetMapping("addShoppingCartList")
    public String addShoppingCart(ModelMap model) {
        ShoppingCartListVO shoppingCartListVO = new ShoppingCartListVO();
        model.addAttribute("shoppingCartListVO", shoppingCartListVO);
        return "front-end/shoppingcartlist/addShoppingCartList";
    }
    
    // 購物車
    @PostMapping("/add-to-cart")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("goodsName") String goodsName,
            @RequestParam("goodsPrice") int goodsPrice,
            @RequestParam("goodsNo") int goodsNo,
            @RequestParam("quantity") int quantity,
            HttpServletRequest req)  {
        HttpSession session = req.getSession();
        Object memAccount = session.getAttribute("memAccount");

        if (memAccount == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "請先登入才能加入購物車！");
            return ResponseEntity.ok(response);
        }

        // 先檢查是否已有相同商品
        List<ShoppingCartListVO> existingCartItems = repository.findByGoodsNo(goodsNo);
        
        if (!existingCartItems.isEmpty()) {
            // 如果已有相同商品，更新數量和總價
            ShoppingCartListVO existingItem = existingCartItems.get(0);
            int newQuantity = existingItem.getGoodsNum() + quantity;
            int newTotalPrice = goodsPrice * newQuantity;
            
            existingItem.setGoodsNum(newQuantity);
            existingItem.setOrderTotalprice(newTotalPrice);
            
            shoppingCartListSvc.updateShoppingCartList(existingItem);
        } else {
            // 如果是新商品，建立新的購物車項目
            ShoppingCartListVO shoppingCartListVO = new ShoppingCartListVO();
            shoppingCartListVO.setGoodsNo(goodsNo);
            shoppingCartListVO.setGoodsName(goodsName);
            shoppingCartListVO.setGoodsPrice(goodsPrice);
            shoppingCartListVO.setGoodsNum(quantity);
            shoppingCartListVO.setOrderTotalprice(goodsPrice * quantity);

            shoppingCartListSvc.addShoppingCartList(shoppingCartListVO);
        }

        int cartCount = shoppingCartListSvc.getAll().size();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("cartCount", cartCount);

        return ResponseEntity.ok(response);
    }
 // 更新購物車商品數量
    @PostMapping("updateQuantity")
    public String updateQuantity(@RequestParam("shoppingcartListNo") Integer shoppingcartListNo,
                                  @RequestParam("goodsNum") Integer goodsNum, Model model,
                                  HttpServletRequest req) {  // 加入 HttpServletRequest 參數

        // 檢查使用者是否已登入
        HttpSession session = req.getSession();
        Object memAccount = session.getAttribute("memAccount");

        if (memAccount == null) {
            model.addAttribute("errorMessage", "請先登入才能更新購物車數量！");
            return "front-end/shoppingcartlist/listAllShoppingCartList";  // 返回購物車頁面
        }
    	
    	// 根據 shoppingcartListNo 找到對應的購物車商品
        ShoppingCartListVO shoppingCartListVO = shoppingCartListSvc.getOneShoppingCartList(shoppingcartListNo);
        
        // 更新商品數量
        shoppingCartListVO.setGoodsNum(goodsNum);

        // 計算新的總價
        int newTotalPrice = shoppingCartListVO.getGoodsPrice() * goodsNum;
        shoppingCartListVO.setOrderTotalprice(newTotalPrice);

        // 儲存更新後的資料
        shoppingCartListSvc.updateShoppingCartList(shoppingCartListVO);
        
        // 更新後重新取得所有購物車資料
        List<ShoppingCartListVO> list = shoppingCartListSvc.getAll();
        model.addAttribute("shoppingCartListListData", list);
        model.addAttribute("success", "- (數量更新成功)");

        // 返回購物車頁面
        return "front-end/shoppingcartlist/listAllShoppingCartList";
    }

    
    /*
     * This method will be called on addShoppingCart.html form submission, handling POST request It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid ShoppingCartListVO shoppingCartListVO, BindingResult result, ModelMap model,
            @RequestParam("gpPhotos1") MultipartFile[] parts1) throws IOException {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中gpPhotos欄位的FieldError紀錄
        result = removeFieldError(shoppingCartListVO, result, "gpPhotos1");
        
        // 檢查照片1 - 確保至少上傳一張照片
        if (parts1[0].isEmpty()) {
            model.addAttribute("errorMessage", "商品主圖(必填): 請上傳至少一張照片");
            return "front-end/shoppingcartlist/addShoppingCartList";  // 若沒有上傳照片1，返回錯誤
        } else {
            for (MultipartFile multipartFile : parts1) {
                byte[] photoData1 = multipartFile.getBytes();
                shoppingCartListVO.setGpPhotos1(photoData1);  // 儲存圖片路徑
            }
        }
        
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
    public String update(@Valid ShoppingCartListVO shoppingCartListVO, BindingResult result, ModelMap model,
			             @RequestParam("gpPhotos1") MultipartFile[] parts1) throws IOException {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中gpPhotos欄位的FieldError紀錄
        result = removeFieldError(shoppingCartListVO, result, "gpPhotos1");
        
        // 檢查照片1 - 如果使用者沒有上傳新圖片，則保留原來的圖片
        if (parts1[0].isEmpty()) {
            // 如果沒有上傳新的圖片，保留原來的圖片
            byte[] originalPhoto1 = shoppingCartListSvc.getOneShoppingCartList(shoppingCartListVO.getGoodsNo()).getGpPhotos1();
            shoppingCartListVO.setGpPhotos1(originalPhoto1);  // 保留原來的圖片
        } else {
            // 如果上傳了新圖片，儲存並更新圖片
            for (MultipartFile multipartFile : parts1) {
                byte[] photoData1 = multipartFile.getBytes();
                shoppingCartListVO.setGpPhotos1(photoData1);  // 儲存新的圖片路徑
            }
        }
        
        if (result.hasErrors()) {
            model.addAttribute("shoppingCartListVO", shoppingCartListVO);
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