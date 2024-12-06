package com.monthsettlement.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.counter.model.CounterVO;
import com.followers.model.FollowersVO;
import com.goods.model.GoodsVO;
import com.monthsettlement.model.MonthSettlementService;
import com.monthsettlement.model.MonthSettlementVO;



@Controller
@RequestMapping("/monthsettlement")
public class MonthSettlementController {

    @Autowired
    MonthSettlementService monthSettlementService;

    @Autowired
    MonthSettlementService counterService;

    // 進入新增頁面
    @GetMapping("/vendor-end/monthsettlement/addMonthSettlement")
    public String addMonthSettlement(ModelMap model) {
        MonthSettlementVO monthSettlementVO = new MonthSettlementVO();
        System.out.println("=====");
        model.addAttribute("monthSettlementVO", monthSettlementVO);
        return "vendor-end/monthsettlement/addMonthSettlement";
    }

    // 新增資料處理
    @PostMapping("insert")
    public String insert(@Valid MonthSettlementVO monthsettlementVO, BindingResult result, ModelMap model)
     	{
    	System.out.println("呼叫");
        /*************************** 1. 接收請求參數 - 格式驗證 ************************/
    	
        if (result.hasErrors()) {
        	System.out.println(monthsettlementVO.getCounterNo());
            return "vendor-end/monthsettlement/addMonthSettlement";
        }

        /*************************** 2. 開始新增資料 **********************************/
        monthSettlementService.addMonthSettlement(monthsettlementVO);
        System.out.println(monthsettlementVO.getComm());
        /*************************** 3. 新增完成，重導至清單頁 ***********************/
        List<MonthSettlementVO> list = monthSettlementService.getAll();
        model.addAttribute("monthsettlementData", list);
        model.addAttribute("success", "- (新增成功)");
        System.out.println(monthsettlementVO.getMonth());
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }
    
//    @GetMapping("listAllMonthSettlement")
//    public String listAll(@Valid MonthSettlementVO monthsettlementVO, BindingResult result, ModelMap model)
//     	{
//        /*************************** 2. 開始新增資料 **********************************/
//        /*************************** 3. 新增完成，重導至清單頁 ***********************/
//        List<MonthSettlementVO> list = monthSettlementService.getAll();
//        model.addAttribute("monthsettlementData", list);
//        model.addAttribute("success", "- (新增成功)");
//        System.out.println(monthsettlementVO.getMonth());
//        return "vendor-end/monthsettlement/listAllMonthSettlement";
//    }
    
    
    
    
    
    
    
//  winfred====================================================================以下

    @GetMapping("listAllMonthSettlement")
    public String listAll(HttpSession session, ModelMap model) {
        CounterVO counter = (CounterVO) session.getAttribute("counter");
        if (counter == null) {
            return "redirect:/counter/login";
        }
        
        List<MonthSettlementVO> list = monthSettlementService.getByCounterNo(counter.getCounterNo());
        model.addAttribute("monthSettlementListData", list);
        model.addAttribute("counter", counter); // 這裡確保counter被添加到模型中
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }
    
//  winfred====================================================================以上    

    
    
    
    
    
    
    
    
    
    
    
    @GetMapping("selectPage")
    public String selectPage(Model model) {
        // 取得所有追蹤清單資料供下拉式選單使用
        List<MonthSettlementVO> list = monthSettlementService.getAll();
        model.addAttribute("monthsettlementData", list);
        
        // 新增一個空的 FollowersVO 物件供表單綁定使用
        MonthSettlementVO monthsettlementVO = new MonthSettlementVO();
        model.addAttribute("MonthSettlementVO", monthsettlementVO);
        
        return "vendor-end/monthsettlement/selectPage";
    }
    
    // 更新資料頁面
    @PostMapping("getOneForUpdate")
    public String getOneForUpdate(@RequestParam("monthsettlementNo") String monthsettlementNo, ModelMap model) {
        MonthSettlementVO monthSettlementVO = monthSettlementService.getOneMonthSettlement(Integer.valueOf(monthsettlementNo));
        model.addAttribute("monthSettlementVO", monthSettlementVO);
        return "vendor-end/monthsettlement/update-MonthSettlement-Input";
    }

    // 更新資料處理
    @PostMapping("update")
    public String update(@Valid MonthSettlementVO monthsettlementVO, BindingResult result, ModelMap model) {

        /*************************** 1. 接收請求參數 - 格式驗證 ************************/
        if (result.hasErrors()) {
            return "vendor-end/monthsettlement/update-MonthSettlement-Input";
        }

        /*************************** 2. 開始更新資料 **********************************/
        monthSettlementService.updateMonthSettlement(monthsettlementVO);

        /*************************** 3. 更新完成，重導至單筆資料顯示頁 *****************/
        model.addAttribute("success", "- (更新成功)");
        monthsettlementVO = monthSettlementService.getOneMonthSettlement(Integer.valueOf(monthsettlementVO.getMonthSettlementNo()));
        model.addAttribute("monthsettlementVO", monthsettlementVO);
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }

    // 刪除資料處理
    @PostMapping("delete")
    public String delete(@RequestParam("monthSettlementNo") String monthsettlementNo, ModelMap model) {

        /*************************** 1. 開始刪除資料 **********************************/
        monthSettlementService.deleteMonthSettlement(Integer.valueOf(monthsettlementNo));

        /*************************** 2. 刪除完成，重導至清單頁 ************************/
        List<MonthSettlementVO> list = monthSettlementService.getAll();
        model.addAttribute("listAllMonthSettlement", list);
        model.addAttribute("success", "- (刪除成功)");
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }

    // 條件查詢
    @PostMapping("listByCompositeQuery")
    public String listByCompositeQuery(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<MonthSettlementVO> list = monthSettlementService.getAll();
        model.addAttribute("monthSettlementList", list);
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }

    // 選單資料（櫃位清單）
    @ModelAttribute("monthSettlementListData")
    protected List<MonthSettlementVO> referenceListData() {
    	List<MonthSettlementVO> list = monthSettlementService.getAll();
        return list;
    }

 // Modify referenceMapData method to match your needs
    @ModelAttribute("monthSettlementMapData")
    protected List<Map<String, Object>> referenceMapData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> option1 = new HashMap<>();
        option1.put("counterNo", 1);
        option1.put("counterName", "櫃位一");
        list.add(option1);
        // Add more options similarly
//        Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//        map.put(1, "櫃位一");
//        map.put(2, "櫃位二");
//        map.put(3, "櫃位三");
        return list;
    }

    // 去除 BindingResult 中某欄位的錯誤
    public BindingResult removeFieldError(MonthSettlementVO monthSettlementVO, BindingResult result, String removedFieldName) {
        List<FieldError> errorsToKeep = result.getFieldErrors().stream()
                .filter(fieldError -> !fieldError.getField().equals(removedFieldName))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(monthSettlementVO, "monthSettlementVO");
        for (FieldError fieldError : errorsToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
    
 // 正確的寫法
//    @GetMapping("/listOneMonthSettlement")
//    public String listOneMonthSettlement(Model model) {
//        // 1. 先從資料庫或服務取得資料
//        List<MonthSettlementVO> monthSettlementVO = monthSettlementService.getAll();  // 或其他方式獲取資料
//        
//        // 2. 將資料加入到 model 中
//        model.addAttribute("monthSettlementVO", monthSettlementVO);
//        
//        // 3. 返回視圖名稱
//        return "vendor-end/monthsettlement/listOneMonthSettlement";
//    }
    
    @GetMapping("/listOneMonthSettlement")
    public String listOneMonthSettlement(@RequestParam(required = false) Integer monthSettlementNo, Model model) {
        // 防禦性程式設計
        if (monthSettlementNo == null) {
            // 可以選擇重導到錯誤頁面或其他處理方式
            return "redirect:/monthsettlement/selectPage";
        }
        
        // 取得月結資料
        MonthSettlementVO monthSettlementVO = monthSettlementService.getOneMonthSettlement(monthSettlementNo);
        
        // 確保資料存在
        if (monthSettlementVO == null) {
            // 可以添加錯誤訊息
            model.addAttribute("error", "找不到指定的月結資料");
            return "redirect:/monthsettlement/selectPage";
        }
        MonthSettlementVO monthSettlementVO35 = monthSettlementService.findById(monthSettlementNo);

        if (monthSettlementVO != null) {
            model.addAttribute("monthSettlementVO", monthSettlementVO);
        } else {
            model.addAttribute("error", "找不到相關月結資料！");
        }
        
//        // 將資料添加到 model
//        model.addAttribute("monthSettlementVO", monthSettlementVO);
        
        return "vendor-end/monthsettlement/listOneMonthSettlement";
    }
    
//    ----------測試櫃位月結營收-----------------
    @GetMapping("/monthsettlement/listAllMonthSettlement5")
	public String listAllMonthSettlement(HttpSession session,Model model) {
		//登錄時檢查
//    	Integer counterNo = Integer.valueOf((String) session.getAttribute("counterNo"));
//	        if (counterNo != null) {
//	        	return "vendor-end/monthsettlement/listAllMonthSettlement";
//	        } else {
//	            return "redirect:/counter/login";
//	        }	
//    		System.out.println("===========");
    		Integer counterNo = (Integer) session.getAttribute("counterNo");
//    		if (counterNo == null) {
//            return "redirect:/counter/login"; // 未登入時跳轉到登入頁
//    		}
//    		Integer counterNo = (Integer) session.getAttribute("counterNo");
    		List<MonthSettlementVO> monthsettlements = (List<MonthSettlementVO>) monthSettlementService.getOneMonthSettlement(counterNo);
    		model.addAttribute("monthsettlementData", monthsettlements);
    		model.addAttribute("monthsettlement", monthSettlementService.getOneMonthSettlement(1));	        
	        return "vendor-end/monthsettlement/listOneMonthSettlement";
	       
	}
    
    
    @GetMapping("/monthsettlement/listMonthSettlement")
    public String listMonthSettlement(HttpSession session, Model model) {
        // 檢查櫃位是否已登入
        Integer counterNo = (Integer) session.getAttribute("counterNo");

        if (counterNo == null) {
            // 如果未登入，保存當前請求路徑並重定向到登入頁面
            session.setAttribute("originalRequest", "/monthsettlement/listAllMonthSettlement");
            return "redirect:/counter/login"; // 假設櫃位的登入頁面是這個路徑
        }

        // 已登入，執行原有邏輯
        List<MonthSettlementVO> monthSettlements = (List<MonthSettlementVO>) monthSettlementService.getOneMonthSettlement(counterNo); // 根據櫃位編號取得月結資料
        if (monthSettlements == null || monthSettlements.isEmpty()) {
            model.addAttribute("error", "找不到任何月結資料");
        } else {
//            model.addAttribute("monthSettlements", monthSettlements);
        }
//        model.addAttribute("monthSettlements", monthSettlements);
        // 將櫃位編號加入模型，供頁面顯示
        model.addAttribute("counterNo", counterNo);

        return "vendor-end/monthsettlement/listAllMonthSettlement"; // 假設這是前端月結資料頁面
    }


    
    
    
  //
//    @PostMapping("listmonthsettlement_ByCompositeQuery")
//    public String listmonthsettlement(HttpSession session ,HttpServletRequest req, Model model) {
////        Map<String, String[]> map = req.getParameterMap();
//    	MonthSettlementVO monthsettlement = (MonthSettlementVO) session.getAttribute("monthsettlement");
//        MonthSettlementVO list = monthSettlementService.getOneMonthSettlement(monthsettlement.getMonthSettlementNo());
//        model.addAttribute("monthsettlementData", list); // for listAllEmp.html 第85行用
//        return "vendor-end/monthsettlement/listAllMonthSettlement";
//    }
    
    @PostMapping("listmonthsettlement_ByCompositeQuery")
    public String listmonthsettlement(HttpSession session, HttpServletRequest req, Model model) {
        // 從 session 中獲取商家編號
        Integer counterNo = (Integer) session.getAttribute("counterNo");

        // 確保商家已登入
//        if (counterNo == null) {
//            return "redirect:/counter/login";
//        }

        // 根據商家編號和其他條件進行查詢
        MonthSettlementVO monthsettlement = (MonthSettlementVO) session.getAttribute("monthsettlement");
        MonthSettlementVO list = monthSettlementService.getOneMonthSettlement(monthsettlement.getMonthSettlementNo());

        model.addAttribute("monthsettlementData", list);
        return "vendor-end/monthsettlement/listAllMonthSettlement";
    }


    
   
}
