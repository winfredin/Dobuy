package com.search.controller;

import com.goods.model.GoodsVO;
import com.search.model.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public List<GoodsVO> search(@RequestParam String query) throws IOException {
        // 呼叫服務層進行處理
		return searchService.searchProducts(query);
		
    }
}
