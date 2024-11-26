package com.goodstype.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.goodstype.model.GoodsTypeVO;
import com.goodstype.model.GoodsTypeService;

@Controller
@RequestMapping("/goodstype")
public class GoodsTypeController {

    @Autowired
    GoodsTypeService goodsTypeSvc;

    /*
     * This method will serve as addGoodsType.html handler.
     */

    @GetMapping("addGoodsType")
    public String addGoodsType(ModelMap model) {
        GoodsTypeVO goodsTypeVO = new GoodsTypeVO();
        model.addAttribute("goodsTypeVO", goodsTypeVO);
        return "back-end/goodstype/addGoodsType"; // 應該返回對應的新增頁面
    }

    /*
     * This method will be called on addGoodsType.html form submission, handling POST request.
     * It also validates the user input.
     */
    @PostMapping("insert")
    public String insert(@Valid GoodsTypeVO goodsTypeVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "back-end/goodstype/addGoodsType";
        }
        /*************************** 2.開始新增資料 *****************************************/
        goodsTypeSvc.addGoodsType(goodsTypeVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        model.addAttribute("goodsTypeListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/goodstype/listAllGoodsType"; // 新增成功後重導至商品類別列表頁
    }

    /*
     * This method will be called on listAllGoodsType.html form submission, handling POST request.
     */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("goodstNo") String goodstNo, ModelMap model) {
    	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        GoodsTypeVO goodsTypeVO = goodsTypeSvc.getOneGoodsType(Integer.valueOf(goodstNo));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("goodsTypeVO", goodsTypeVO);
        return "back-end/goodstype/update_goodsType_input"; // 返回更新頁面
    }

    /*
     * This method will be called on update_goodsType_input.html form submission, handling POST request.
     * It also validates the user input.
     */
    @PostMapping("update")
    public String update(@Valid GoodsTypeVO goodsTypeVO, BindingResult result, ModelMap model) {
    	 
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
            return "back-end/goodstype/update_goodsType_input"; // 如果有錯誤，返回更新頁面
        }

        /*************************** 2.開始修改資料 *****************************************/
        goodsTypeSvc.updateGoodsType(goodsTypeVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        goodsTypeVO = goodsTypeSvc.getOneGoodsType(Integer.valueOf(goodsTypeVO.getGoodstNo()));
        model.addAttribute("goodsTypeVO", goodsTypeVO);
        return "back-end/goodstype/listOneGoodsType"; // 修改成功後轉交商品類別單一查看頁面
    }

    /*
     * This method will be called on listAllGoodsType.html form submission, handling POST request.
     */
    @PostMapping("delete")
    public String delete(@RequestParam("goodstNo") String goodstNo, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        goodsTypeSvc.deleteGoodsType(Integer.valueOf(goodstNo));

        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        model.addAttribute("goodsTypeListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/goodstype/listAllGoodsType"; // 刪除完成後轉交商品類別列表頁面
    }

    /*
     * 第一種作法 Method used to populate the List Data in view.
     */
    @ModelAttribute("goodsTypeListData")
    protected List<GoodsTypeVO> referenceListData() {
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        return list;
    }

    /*
     * 【 第二種作法 】 Method used to populate the Map Data in view.
     */
    @ModelAttribute("goodsTypeMapData")
    protected Map<Integer, String> referenceMapData() {
        Map<Integer, String> map = new LinkedHashMap<>();
        List<GoodsTypeVO> list = goodsTypeSvc.getAll();
        for (GoodsTypeVO goodsType : list) {
            map.put(goodsType.getGoodstNo(), goodsType.getGoodstName());
        }
        return map;
    }

    // 去除BindingResult中某個欄位的FieldError紀錄
    public BindingResult removeFieldError(GoodsTypeVO goodsTypeVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(goodsTypeVO, "goodsTypeVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    /*
     * This method will be called on select_page.html form submission, handling POST request
     */
    @PostMapping("listGoodsTypes_ByCompositeQuery")
    public String listGoodsTypes(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<GoodsTypeVO> list = goodsTypeSvc.getAll(map);
        model.addAttribute("goodsTypeListData", list);
        return "back-end/goodstype/listAllGoodsType";
    }
}
