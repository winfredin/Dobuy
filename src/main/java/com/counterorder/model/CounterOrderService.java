package com.counterorder.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.counterorderdetail.model.CounterOrderDetailVO;
import com.goods.model.GoodsService;
import com.goods.model.GoodsVO;
import com.memcoupon.model.MemCouponVO;




@Service("CounterOrderService")
public class CounterOrderService {

	@Autowired
	CounterOrderRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
    private GoodsService goodsService;

	public void addCounterOrder(CounterOrderVO counterOrderVO) {
		repository.save(counterOrderVO);
	}

	public void updateCounterOrder(CounterOrderVO counterOrderVO) {
		repository.save(counterOrderVO);
	}

	public void deleteCounterOrder(Integer counterOrderNo) {
		if (repository.existsById(counterOrderNo))
			repository.deleteByCounterOrderNo(counterOrderNo);
//		    repository.deleteById(empno);
	}

	public CounterOrderVO getOneCounterOrder(Integer counterOrderNo) {
		Optional<CounterOrderVO> optional = repository.findById(counterOrderNo);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<CounterOrderVO> getAll() {
		return repository.findAll();
	}
	public Integer getone(Integer memno) {
		return repository.findone(memno);
	}
	
//	結帳用
	public CounterOrderVO createOrder(Integer memNo, Integer counterNo, int totalBefore, int totalAfter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
    /**柏翔
     * 根據櫃位編號和會員編號查詢待付款訂單
     * 
     * @param counterNo 櫃位編號
     * @param memNo 會員編號
     * @return 找到的訂單，如果沒有則返回 null
     */
    // 在 CounterOrderService 中添加這個方法
    @Transactional
    public CounterOrderVO findByCounterNoAndMemNo(Integer counterNo, Integer memNo) {
        // 使用 JPA 查詢
        return repository.findByCounterNoAndMemNoAndOrderStatus(counterNo, memNo, 0)
                .orElse(null);
    }

    /**柏翔
     * 獲取指定會員在指定櫃位的所有訂單
     * 
     * @param counterNo 櫃位編號
     * @param memNo 會員編號
     * @return 訂單列表
     */
    public List<CounterOrderVO> getAllOrdersByCounterAndMember(Integer counterNo, Integer memNo) {
        return repository.findAllByCounterNoAndMemNo(counterNo, memNo);
    }
    
    @Transactional
    public void updateOrderWithCoupon(CounterOrderVO order, MemCouponVO coupon) {
        repository.save(order);
    }
    
    @Scheduled(fixedRate = 60000) // 每 100 秒執行一次
    @Transactional
    public void restoreInventoryForExpiredOrders() {
        System.out.println("Running scheduled task...");

        // 找出所有過期且未處理的訂單
        List<CounterOrderVO> expiredOrders = repository.findExpiredOrders();

        // 處理每一筆過期訂單
        for (CounterOrderVO order : expiredOrders) {
            Integer reservedAmount = order.getReservedAmount();
            if (reservedAmount != null && reservedAmount > 0) {
                // 從訂單明細中獲取商品資訊
                List<CounterOrderDetailVO> details = order.getCounterOrderDatailVO();

                for (CounterOrderDetailVO detail : details) {
                    Integer goodsNo = detail.getGoodsNo();
                    Integer reservedGoodsAmount = detail.getGoodsNum();

                    // 恢復商品庫存
                    GoodsVO goods = goodsService.getOneGoods(goodsNo);
                    if (goods != null) {
                        Integer updatedAmount = goods.getGoodsAmount() + reservedGoodsAmount;
                        System.out.println("Updating goods ID: " + goodsNo + " from amount: " 
                                            + goods.getGoodsAmount() + " to: " + updatedAmount);
                        goods.setGoodsAmount(updatedAmount);
                        goodsService.updateGoodsAmount(goodsNo, updatedAmount);
                    } else {
                        System.out.println("Goods not found with ID: " + goodsNo);
                    }
                }

                // 將訂單的預扣數量設為 0，表示恢復完成
                order.setReservedAmount(0);
                repository.save(order); // 更新訂單狀態
            } else {
                System.out.println("Order with ID " + order.getCounterOrderNo() + " has no reserved amount.");
            }
        }
    }
    
    
//    柏翔
    @Transactional
    public void updateCounterOrder49(CounterOrderVO order) {
        // 加入日誌
        System.out.println("準備更新訂單 " + order.getCounterOrderNo());
        System.out.println("更新金額為: " + order.getOrderTotalAfter());
        
        try {
            repository.save(order);
            // 確認更新後的結果
            CounterOrderVO updated = repository.findById(order.getCounterOrderNo()).orElse(null);
            if (updated != null) {
                System.out.println("更新後金額為: " + updated.getOrderTotalAfter());
            }
        } catch (Exception e) {
            System.err.println("更新訂單時發生錯誤: " + e.getMessage());
            throw e;
        }
    }
    
    //gary
    public List<CounterOrderVO> ListfindByMemNoAndStatusNot4(Integer memNo) {
    	
    	List<CounterOrderVO> buyerOrderList	= repository.findByMemNoAndStatusNot4(memNo);
    	
    	return buyerOrderList;
    }
    
    
    
    
    
    

}

