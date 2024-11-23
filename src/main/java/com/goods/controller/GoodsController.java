package com.goods.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsSvc;

    /*
     * This method will serve as addCouponDetail.html handler.
     */
    @GetMapping("addGoods")
    public String addGoods(ModelMap model) {
        GoodsVO goodsVO = new GoodsVO();
        model.addAttribute("goodsVO", goodsVO);
        return "back-end/goods/addGoods";
    }

    /*
     * This method will be called on addCouponDetail.html form submission, handling POST request
     * It also validates the user input
     */
    @PostMapping("insert")
    public String insert(@Valid GoodsVO goodsVO, BindingResult result, ModelMap model,
                         @RequestParam("gpPhotos1") MultipartFile[] parts1,
                         @RequestParam(value = "gpPhotos2", required = false) MultipartFile[] parts2,
                         @RequestParam(value = "gpPhotos3", required = false) MultipartFile[] parts3,
                         @RequestParam(value = "gpPhotos4", required = false) MultipartFile[] parts4,
                         @RequestParam(value = "gpPhotos5", required = false) MultipartFile[] parts5,
                         @RequestParam(value = "gpPhotos6", required = false) MultipartFile[] parts6,
                         @RequestParam(value = "gpPhotos7", required = false) MultipartFile[] parts7,
                         @RequestParam(value = "gpPhotos8", required = false) MultipartFile[] parts8,
                         @RequestParam(value = "gpPhotos9", required = false) MultipartFile[] parts9,
                         @RequestParam(value = "gpPhotos10", required = false) MultipartFile[] parts10) throws IOException {
        
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中gpPhotos欄位的FieldError紀錄
        result = removeFieldError(goodsVO, result, "gpPhotos1");
        result = removeFieldError(goodsVO, result, "gpPhotos2");
        result = removeFieldError(goodsVO, result, "gpPhotos3");
        result = removeFieldError(goodsVO, result, "gpPhotos4");
        result = removeFieldError(goodsVO, result, "gpPhotos5");
        result = removeFieldError(goodsVO, result, "gpPhotos6");
        result = removeFieldError(goodsVO, result, "gpPhotos7");
        result = removeFieldError(goodsVO, result, "gpPhotos8");
        result = removeFieldError(goodsVO, result, "gpPhotos9");
        result = removeFieldError(goodsVO, result, "gpPhotos10");
        
        // 檢查照片1 - 確保至少上傳一張照片
        if (parts1[0].isEmpty()) {
            model.addAttribute("errorMessage", "商品主圖(必填): 請上傳至少一張照片");
            return "back-end/goods/addGoods";  // 若沒有上傳照片1，返回錯誤
        } else {
            for (MultipartFile multipartFile : parts1) {
                byte[] photoData1 = multipartFile.getBytes();
                goodsVO.setGpPhotos1(photoData1);  // 儲存圖片路徑
            }
        }

        // 檢查照片2 - 如果上傳了照片2，則處理
        if (parts2 != null && parts2.length > 0 && !parts2[0].isEmpty()) {
            for (MultipartFile multipartFile : parts2) {
                byte[] photoData2 = multipartFile.getBytes();
                goodsVO.setGpPhotos2(photoData2);  // 儲存圖片路徑
            }
        }
        // 檢查照片3 - 如果上傳了照片3，則處理
        if (parts3 != null && parts3.length > 0 && !parts3[0].isEmpty()) {
        	for (MultipartFile multipartFile : parts3) {
        		byte[] photoData3 = multipartFile.getBytes();
        		goodsVO.setGpPhotos3(photoData3);  // 儲存圖片路徑
        	}
        }
        
        // 檢查照片4 - 如果上傳了新圖片，則更新照片4，否則保持原來的圖片不變
        if (parts4 != null && parts4.length > 0 && !parts4[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts4) {
        		byte[] photoData4 = multipartFile.getBytes();
        		goodsVO.setGpPhotos4(photoData4);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片5 - 如果上傳了新圖片，則更新照片5，否則保持原來的圖片不變
        if (parts5 != null && parts5.length > 0 && !parts5[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts5) {
        		byte[] photoData5 = multipartFile.getBytes();
        		goodsVO.setGpPhotos5(photoData5);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片6 - 如果上傳了新圖片，則更新照片6，否則保持原來的圖片不變
        if (parts6 != null && parts6.length > 0 && !parts6[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts6) {
        		byte[] photoData6 = multipartFile.getBytes();
        		goodsVO.setGpPhotos6(photoData6);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片7 - 如果上傳了新圖片，則更新照片7，否則保持原來的圖片不變
        if (parts7 != null && parts7.length > 0 && !parts7[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts7) {
        		byte[] photoData7 = multipartFile.getBytes();
        		goodsVO.setGpPhotos7(photoData7);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片8 - 如果上傳了新圖片，則更新照片8，否則保持原來的圖片不變
        if (parts8 != null && parts8.length > 0 && !parts8[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts8) {
        		byte[] photoData8 = multipartFile.getBytes();
        		goodsVO.setGpPhotos8(photoData8);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片9 - 如果上傳了新圖片，則更新照片9，否則保持原來的圖片不變
        if (parts9 != null && parts9.length > 0 && !parts9[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts9) {
        		byte[] photoData9 = multipartFile.getBytes();
        		goodsVO.setGpPhotos9(photoData9);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片10 - 如果上傳了新圖片，則更新照片10，否則保持原來的圖片不變
        if (parts10 != null && parts10.length > 0 && !parts10[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts10) {
        		byte[] photoData10 = multipartFile.getBytes();
        		goodsVO.setGpPhotos10(photoData10);  // 儲存新的圖片路徑
        	}
        }

        if (result.hasErrors()) {
            return "back-end/goods/addGoods";  // 如果有錯誤，返回新增頁面
        }

        /*************************** 2.開始新增資料 *****************************************/
        goodsSvc.addGoods(goodsVO);

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<GoodsVO> list = goodsSvc.getAll();
        model.addAttribute("goodsListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/goods/listAllGoods";  // 新增成功後重導至商品列表
    }

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("goodsNo") String goodsNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
        GoodsVO goodsVO = goodsSvc.getOneGoods(Integer.valueOf(goodsNo));
        
        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("goodsVO", goodsVO);
        return "back-end/goods/update_goods_input"; // 查詢完成後轉交update_goods_input.html
    }
    
	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
    @PostMapping("update")
    public String update(@Valid GoodsVO goodsVO, BindingResult result, ModelMap model,
                         @RequestParam("gpPhotos1") MultipartFile[] parts1,
                         @RequestParam(value = "gpPhotos2", required = false) MultipartFile[] parts2,
                         @RequestParam(value = "gpPhotos3", required = false) MultipartFile[] parts3,
                         @RequestParam(value = "gpPhotos4", required = false) MultipartFile[] parts4,
                         @RequestParam(value = "gpPhotos5", required = false) MultipartFile[] parts5,
                         @RequestParam(value = "gpPhotos6", required = false) MultipartFile[] parts6,
                         @RequestParam(value = "gpPhotos7", required = false) MultipartFile[] parts7,
                         @RequestParam(value = "gpPhotos8", required = false) MultipartFile[] parts8,
                         @RequestParam(value = "gpPhotos9", required = false) MultipartFile[] parts9,
                         @RequestParam(value = "gpPhotos10", required = false) MultipartFile[] parts10 ) throws IOException {
        
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        // 去除BindingResult中gpPhotos欄位的FieldError紀錄
        result = removeFieldError(goodsVO, result, "gpPhotos1");
        result = removeFieldError(goodsVO, result, "gpPhotos2");
        result = removeFieldError(goodsVO, result, "gpPhotos3");
        result = removeFieldError(goodsVO, result, "gpPhotos4");
        result = removeFieldError(goodsVO, result, "gpPhotos5");
        result = removeFieldError(goodsVO, result, "gpPhotos6");
        result = removeFieldError(goodsVO, result, "gpPhotos7");
        result = removeFieldError(goodsVO, result, "gpPhotos8");
        result = removeFieldError(goodsVO, result, "gpPhotos9");
        result = removeFieldError(goodsVO, result, "gpPhotos10");

        // 檢查照片1 - 如果使用者沒有上傳新圖片，則保留原來的圖片
        if (parts1[0].isEmpty()) {
            // 如果沒有上傳新的圖片，保留原來的圖片
            byte[] originalPhoto1 = goodsSvc.getOneGoods(goodsVO.getGoodsNo()).getGpPhotos1();
            goodsVO.setGpPhotos1(originalPhoto1);  // 保留原來的圖片
        } else {
            // 如果上傳了新圖片，儲存並更新圖片
            for (MultipartFile multipartFile : parts1) {
                byte[] photoData1 = multipartFile.getBytes();
                goodsVO.setGpPhotos1(photoData1);  // 儲存新的圖片路徑
            }
        }

        // 檢查照片2 - 如果上傳了新圖片，則更新照片2，否則保持原來的圖片不變
        if (parts2 != null && parts2.length > 0 && !parts2[0].isEmpty()) {
            // 如果上傳了新圖片，儲存並更新
            for (MultipartFile multipartFile : parts2) {
                byte[] photoData2 = multipartFile.getBytes();
                goodsVO.setGpPhotos2(photoData2);  // 儲存新的圖片路徑
            }
        }
        
        // 檢查照片3 - 如果上傳了新圖片，則更新照片3，否則保持原來的圖片不變
        if (parts3 != null && parts3.length > 0 && !parts3[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts3) {
        		byte[] photoData3 = multipartFile.getBytes();
        		goodsVO.setGpPhotos3(photoData3);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片4 - 如果上傳了新圖片，則更新照片4，否則保持原來的圖片不變
        if (parts4 != null && parts4.length > 0 && !parts4[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts4) {
        		byte[] photoData4 = multipartFile.getBytes();
        		goodsVO.setGpPhotos4(photoData4);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片5 - 如果上傳了新圖片，則更新照片5，否則保持原來的圖片不變
        if (parts5 != null && parts5.length > 0 && !parts5[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts5) {
        		byte[] photoData5 = multipartFile.getBytes();
        		goodsVO.setGpPhotos5(photoData5);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片6 - 如果上傳了新圖片，則更新照片6，否則保持原來的圖片不變
        if (parts6 != null && parts6.length > 0 && !parts6[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts6) {
        		byte[] photoData6 = multipartFile.getBytes();
        		goodsVO.setGpPhotos6(photoData6);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片7 - 如果上傳了新圖片，則更新照片7，否則保持原來的圖片不變
        if (parts7 != null && parts7.length > 0 && !parts7[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts7) {
        		byte[] photoData7 = multipartFile.getBytes();
        		goodsVO.setGpPhotos7(photoData7);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片8 - 如果上傳了新圖片，則更新照片8，否則保持原來的圖片不變
        if (parts8 != null && parts8.length > 0 && !parts8[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts8) {
        		byte[] photoData8 = multipartFile.getBytes();
        		goodsVO.setGpPhotos8(photoData8);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片9 - 如果上傳了新圖片，則更新照片9，否則保持原來的圖片不變
        if (parts9 != null && parts9.length > 0 && !parts9[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts9) {
        		byte[] photoData9 = multipartFile.getBytes();
        		goodsVO.setGpPhotos9(photoData9);  // 儲存新的圖片路徑
        	}
        }
        
        // 檢查照片10 - 如果上傳了新圖片，則更新照片10，否則保持原來的圖片不變
        if (parts10 != null && parts10.length > 0 && !parts10[0].isEmpty()) {
        	// 如果上傳了新圖片，儲存並更新
        	for (MultipartFile multipartFile : parts10) {
        		byte[] photoData10 = multipartFile.getBytes();
        		goodsVO.setGpPhotos10(photoData10);  // 儲存新的圖片路徑
        	}
        }

        // 如果有驗證錯誤，則返回更新頁面
        if (result.hasErrors()) {
            model.addAttribute("goodsVO", goodsVO);
            return "back-end/goods/update_goods_input";  // 如果有錯誤，返回更新頁面
        }

        /*************************** 2.開始修改資料 *****************************************/
        goodsSvc.updateGoods(goodsVO);  // 更新商品資料

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        goodsVO = goodsSvc.getOneGoods(goodsVO.getGoodsNo());  // 獲取更新後的商品資料
        model.addAttribute("goodsVO", goodsVO);  // 將更新後的資料傳遞到前端
        return "back-end/goods/listOneGoods";  // 返回商品資料頁面
    }


	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
    @PostMapping("delete")
    public String delete(@RequestParam("goodsNo") String goodsNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
    	// EmpService empSvc = new EmpService();
    	goodsSvc.deleteGoods(Integer.valueOf(goodsNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<GoodsVO> list = goodsSvc.getAll();
        model.addAttribute("goodsListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/goods/listAllGoods"; // 刪除完成後轉交listAllGoods.html
    }

	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
    @PostMapping("listGoods_ByCompositeQuery")
    public String listGoods(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<GoodsVO> list = goodsSvc.getAll(map);
        model.addAttribute("goodsListData", list); // for listAllEmp.html 第85行用
        return "back-end/goods/listAllGoods";
    }

    public BindingResult removeFieldError(GoodsVO goodsVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(goodsVO, "goodsVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
}
