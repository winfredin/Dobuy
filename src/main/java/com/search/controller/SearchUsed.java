package com.search.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goods.model.GoodsVO;
import com.search.model.SearchService;
import com.used.model.UsedVO;

@RestController
@RequestMapping("/searchused")
public class SearchUsed{

    @Autowired
    private SearchService searchService;

    @GetMapping
    public List<UsedVO> search(@RequestParam String query) throws IOException {
        // 呼叫服務層進行處理
		return searchService.searchUsed(query);
		
    }
}
