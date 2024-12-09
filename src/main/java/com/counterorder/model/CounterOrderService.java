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
    
//    @Transactional
//    @Scheduled(fixedRate = 100000)  // 這裡是固定時間間隔（60000 毫秒 = 1 分鐘）
//    public void restoreInventoryForExpiredOrders() {
//    	System.out.println("Running scheduled task...");
//        List<CounterOrderVO> expiredOrders = repository.findExpiredOrders();
//        CounterOrderVO counterOrderVO = new CounterOrderVO();
//        Integer goodsNo = counterOrderVO.getGoodsNo();  
//        for (CounterOrderVO order : expiredOrders) {
//            if (order.getReservedAmount() > 0) {
//                // 恢復庫存
//                
//                GoodsVO goods = goodsService.getOneGoods(goodsNo);
//                goods.setGoodsAmount(goods.getGoodsAmount() + order.getReservedAmount());  // 加回預扣數量
//                goodsService.updateGoodsAmount(goodsNo, goods.getGoodsAmount());
//
//                // 更新訂單的預扣數量為 0，表示庫存已經恢復
//                order.setReservedAmount(0);  // 更新為 0
//                repository.save(order);
//            }
//        }

}