package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.counter.model.CounterService;
import com.counter.model.CounterVO;
import com.countercarousel.model.CountercarouselService;
import com.countercarousel.model.CountercarouselVO;
import com.counterorder.model.CounterOrderService;
import com.counterorder.model.CounterOrderVO;
import com.counterorderdetail.model.CounterOrderDetailService;
import com.counterorderdetail.model.CounterOrderDetailVO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.goodstype.model.GoodsTypeService;
import com.goodstype.model.GoodsTypeVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.used.model.UsedService;
import com.used.model.UsedVO;
import com.usedpic.model.UsedPicVO;

@Controller
public class FrontEndController {

    @Autowired
    GoodsService goodsSvc;
    @Autowired
    GoodsTypeService goodstSvc;
    @Autowired
    CounterService counterSvc;
    @Autowired
    CounterOrderService counterOrderSvc;
    @Autowired
    CounterOrderDetailService counterOrderDetailSvc;
    @Autowired
    CouponService couponSvc;
    @Autowired
    CountercarouselService countercarouselSvc;
    @Autowired
    MemberService memSvc;

    @GetMapping("")
    public String index() {
        return "loading";
    }

    @GetMapping("member")
    public String getMemberPage(HttpSession session, Model model) {
        if (session.getAttribute("memNo") == null) {
            return "redirect:/mem/login";
        }

        Integer memNo = Integer.valueOf(session.getAttribute("memNo").toString());
        List<CounterOrderVO> orders = counterOrderSvc.ListfindByMemNoAndStatusNot4(memNo);

        List<GoodsVO> goodsList = new ArrayList<>();
        List<CounterOrderVO> newList = new ArrayList<>();

        for (CounterOrderVO order : orders) {
            List<CounterOrderDetailVO> details = counterOrderDetailSvc.getDetailsByOrderNo(order.getCounterOrderNo());
            for (CounterOrderDetailVO detail : details) {
                GoodsVO goods = goodsSvc.getOneGoods(detail.getGoodsNo());
                goodsList.add(goods);
            }
            order.setCounterOrderDatailVO(details);
            newList.add(order);
        }

        model.addAttribute("goodsNamelist", goodsList);
        model.addAttribute("couponList", couponSvc.getAll());
        model.addAttribute("counterList", counterSvc.getAll());
        model.addAttribute("orders", newList);

        return "front-end/normalpage/member";
    }

    @GetMapping("home")
    public String getHomePage(Model model) {
        model.addAttribute("counterVOList", counterSvc.getAll());
        model.addAttribute("goodslist", goodsSvc.getAllGoodsStatus1());
        model.addAttribute("carousellist", countercarouselSvc.getAll());
        return "front-end/normalpage/homepage";
    }

    @GetMapping("goodspage")
    public String getGoodsPage(Model model) {
        model.addAttribute("list", goodsSvc.getgoods());
        model.addAttribute("glist", goodstSvc.getAll());
        return "front-end/normalpage/goodspage";
    }

    @GetMapping("/goods/filter")
    public List<GoodsVO> filterGoodsByType(@RequestParam("goodstNo") String goodstNo) {
        List<GoodsVO> goodsList = goodsSvc.getgoods();
        int typeNo = Integer.parseInt(goodstNo);
        List<GoodsVO> filteredList = new ArrayList<>();
        for (GoodsVO goods : goodsList) {
            if (goods.getGoodsTypeVO() != null && goods.getGoodsTypeVO().getGoodstNo() == typeNo) {
                filteredList.add(goods);
            }
        }
        return filteredList;
    }

    @PostMapping("updatemem")
    public String updateMember(@Valid MemberVO memberVO, BindingResult result, ModelMap model, HttpSession session)
            throws IOException {
        Integer memNo = Integer.parseInt(session.getAttribute("memNo").toString());
        memSvc.updateMem(memberVO);
        memberVO = memSvc.findOne(memNo);
        model.addAttribute("memberVO", memberVO);
        return "front-end/normalpage/member";
    }

    @PostMapping("deleteac")
    public String deleteAccount(ModelMap model, HttpSession session) {
        Integer memNo = Integer.parseInt(session.getAttribute("memNo").toString());
        MemberVO memberVO = memSvc.findOne(memNo);
        memberVO.setMemStatus(0);
        memSvc.updateMem(memberVO);

        session.invalidate();
        return "front-end/normalpage/member";
    }


 @PostMapping("changepas")
 public String changepas(@RequestParam("memPassword") String memPassword,
   @RequestParam("confirmPassword") String confirmPassword, ModelMap model, HttpSession session)
   throws IOException {

  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  if (!memPassword.equals(confirmPassword)) {
   model.addAttribute("error", "確認密碼輸入錯誤");

  }
  memSvc.updatePass(memNo, memPassword);
  MemberVO memberVO = memSvc.findOne(memNo);
  model.addAttribute("memberVO", memberVO);
  return "front-end/normalpage/member";

 }

 @GetMapping("content/credit")
 public String getcreditPage() {
  return "content/credit"; // 對應 templates/content/profile.html
 }

 @GetMapping("content/changeps")
 public String getchangepsPage(HttpSession session, Model model) {
  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  MemberVO memberVO;
  memberVO = memSvc.findOne(memNo);
  memberVO.setMemPassword("");
  model.addAttribute("memberVO", memberVO);
  return "content/changeps"; // 對應 templates/content/profile.html
 }

 @GetMapping("content/delete")
 public String getdeletePage(Model model, HttpSession session) {
  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  MemberVO memberVO;
  memberVO = memSvc.findOne(memNo);
  model.addAttribute("memberVO", memberVO);
  return "content/delete"; // 對應 templates/content/profile.html
 }

 @GetMapping("content/add")
 public String getaddPage(HttpSession session, Model model) {
  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  MemberVO memberVO;
  memberVO = memSvc.findOne(memNo);
  model.addAttribute("memberVO", memberVO);
  return "content/add"; // 對應 templates/content/profile.html
 }

 @GetMapping("content/profileup")
 public String getprofileupPage(Model model, HttpSession session) {
  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  MemberVO memberVO;
  memberVO = memSvc.findOne(memNo);
  model.addAttribute("memberVO", memberVO);
  return "content/profileup"; // 對應 templates/content/profile.html
 }

 @GetMapping("content/profile")
 public String getProfilePage(Model model, HttpSession session) {
  Object memNoObj = session.getAttribute("memNo");
  Integer memNo = Integer.parseInt(memNoObj.toString());
  MemberVO memberVO;
  memberVO = memSvc.findOne(memNo);
  model.addAttribute("memberVO", memberVO);
  return "content/profile";
 }

}