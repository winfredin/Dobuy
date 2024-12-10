package com.cancelunpaidordersjob;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CancelUnpaidOrdersJob implements Job {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String selectQuery = "SELECT usedOrderNo, usedNo, usedCount " +
                             "FROM UsedOrder " +
                             "WHERE deliveryStatus = 6 AND TIMESTAMPDIFF(MINUTE, usedOrderTime, NOW()) > 10";

        // 查詢超時未支付的訂單
        List<Map<String, Object>> unpaidOrders = jdbcTemplate.queryForList(selectQuery);

        for (Map<String, Object> order : unpaidOrders) {
            Integer orderNo = (Integer) order.get("usedOrderNo");
            Integer usedNo = (Integer) order.get("usedNo");
            Integer usedCount = (Integer) order.get("usedCount");

            // 還原庫存
            String updateStockQuery = "UPDATE Used SET usedStocks = usedStocks + ? WHERE usedNo = ?";
            jdbcTemplate.update(updateStockQuery, usedCount, usedNo);

            // 刪除訂單
            String deleteOrderQuery = "DELETE FROM UsedOrder WHERE usedOrderNo = ?";
            jdbcTemplate.update(deleteOrderQuery, orderNo);

            System.out.println("訂單已刪除：" + orderNo + "商品編號:"+ usedNo+ "，商品庫存已還原："+ usedCount+"個");
        }
    }

	
}
