package com.ShoppingCartList.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ShoppingCartList.controller.HibernateUtil_CompositeQuery_ShoppingCartList;

/**
 * ShoppingCartListService 類別，用於管理購物車相關業務邏輯
 */
@Service("shoppingCartListService")
public class ShoppingCartListService {

    @Autowired
    ShoppingCartListRepository repository;
    
    @Autowired
    private SessionFactory sessionFactory;

    // 新增購物車項目
    public void addShoppingCartList(ShoppingCartListVO shoppingCartListVO) {
        repository.save(shoppingCartListVO);
    }

    // 更新購物車項目
    public void updateShoppingCartList(ShoppingCartListVO shoppingCartListVO) {
        repository.save(shoppingCartListVO);
    }

    // 刪除購物車項目
    public void deleteShoppingCartList(Integer shoppingcartListNo) {
        if (repository.existsById(shoppingcartListNo)) {
            repository.deleteById(shoppingcartListNo);
        }
    }

    // 根據ID查詢單一購物車項目
    public ShoppingCartListVO getOneShoppingCartList(Integer shoppingcartListNo) {
        Optional<ShoppingCartListVO> optional = repository.findById(shoppingcartListNo);
        return optional.orElse(null);  // 如果沒有找到則返回null
    }

    // 查詢所有購物車項目
    public List<ShoppingCartListVO> getAll() {
        return repository.findAll();
    }

    // 根據條件查詢購物車項目
    public List<ShoppingCartListVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_ShoppingCartList.getAllC(map, sessionFactory.openSession());
    }
    
//  結帳用=========================================柏諭
	public List<ShoppingCartListVO> getCartItemsByMemNo(Integer memNo) {
		
		return repository.findmem(memNo);
	}
//  結帳用=========================================翊豪
	public void clearCart(Integer memNo) {
		// TODO Auto-generated method stub
		
	}
}
