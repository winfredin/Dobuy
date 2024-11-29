let items = document.querySelectorAll('.goods .item');
let itemsToShow = 4; // 初始顯示的商品數量

// 顯示指定數量的商品
function showItems() {
    for (let i = 0; i < items.length; i++) {
        if (i < itemsToShow) {
            items[i].classList.remove('hide'); // 顯示商品
        }
    }
}

// 檢查滾動位置，觸發加載更多商品
function handleScroll() {
    // 檢查是否已經滾動到頁面底部
    let scrollPosition = window.scrollY + window.innerHeight;
    let pageHeight = document.documentElement.scrollHeight;

    if (scrollPosition >= pageHeight - 10 && itemsToShow < items.length) {
        // 滾動到頁面底部並且還有商品未顯示
        itemsToShow += 4; // 顯示更多商品
        showItems(); // 顯示新商品
    }
}

// 初始加載 4 個商品
showItems();

// 當滾動時，檢查是否需要顯示更多商品
window.addEventListener('scroll', handleScroll);

$(".mempro").on("click",function(){
    $(".two").removeClass("aaa");
    $(".twogoods span#a1").removeClass("aaa");
    $(".buy").removeClass("aaa");
    $("div#b1").removeClass("aaa");
    $(".prothings").slideDown(1000);  
    $(".twothings").slideUp(1000);
    $(".proac").addClass("aaa");
    $(".pro span#1").addClass("aaa");

    const itemId = $("span").data("id"); // 取得 data-id 的值
        

        // 發送 AJAX 請求到伺服器
        $.ajax({
            url: "content/profile", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });

});
$(".proac").on("click",function(){
    $(".two").removeClass("aaa");
    $(".twogoods span#a1").removeClass("aaa");
    $(".pro span#2").removeClass("aaa");
    $(".pro span#3").removeClass("aaa");
    $(".pro span#4").removeClass("aaa");
    $(".pro span#5").removeClass("aaa");
    $(".buy").removeClass("aaa");
    $("div#b1").removeClass("aaa");
    $(".prothings").slideDown(1000);  
    $(".twothings").slideUp(1000);
    $(".pro span#1").addClass("aaa");

    const itemId = $("span").data("id"); // 取得 data-id 的值
        // 發送 AJAX 請求到伺服器
        $.ajax({
            url: "content/profile", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            data: { id: itemId }, // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });


    if($(".proac").hasClass("aaa")){
        $(".proac").removeClass("aaa");
        $(".pro span#1").removeClass("aaa");
        $(".prothings").slideUp(600);  
    }else{
        $(".proac").addClass("aaa");
    }
});

$(".pro span#1").on("click",function(){
    $(".proac").addClass("aaa");
    $(".pro span#1").addClass("aaa");
    $(".pro span#2").removeClass("aaa");
    $(".pro span#3").removeClass("aaa");
    $(".pro span#4").removeClass("aaa");
    $(".pro span#5").removeClass("aaa");
    const itemId = $("span").data("id"); // 取得 data-id 的值
        // 發送 AJAX 請求到伺服器
        $.ajax({
            url: "content/profile", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            data: { id: itemId }, // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });
});

$(".buy").on("click",function(){
    window.location.href ="member";
})




$(".two").on("click",function(){
    $(".proac").removeClass("aaa");
    $(".pro span#1").removeClass("aaa");
    $(".buy").removeClass("aaa");
    $("div#b1").removeClass("aaa");
    $(".twothings").slideDown(800);
    $(".prothings").slideUp(500);
    
    $(".twogoods span#a1").addClass("aaa");
    if($(".two").hasClass("aaa")){  
        $(".two").removeClass("aaa");
        $(".twogoods span#a1").removeClass("aaa");
        $(".twothings").slideUp(600);
    }else{
        $(".two").addClass("aaa");
    }
});

$("span#4").on("click",function(){
    $(".proac").removeClass("aaa");
    $(".pro span#1").removeClass("aaa");
    $("span#4").addClass("aaa");
    $(".pro span#2").removeClass("aaa");
    $(".pro span#3").removeClass("aaa");
    $(".pro span#5").removeClass("aaa");
    $.ajax({
            url: "content/credit", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });
});

$("span#2").on("click",function(){
    $(".proac").removeClass("aaa");
    $(".pro span#1").removeClass("aaa");
    $("span#2").addClass("aaa");
    $(".pro span#4").removeClass("aaa");
    $(".pro span#3").removeClass("aaa");
    $(".pro span#5").removeClass("aaa");
    $.ajax({
            url: "content/changeps", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });

});
        $("span#5").on("click",function(){
    $(".proac").removeClass("aaa");
    $(".pro span#1").removeClass("aaa");
    $("span#5").addClass("aaa");
    $(".pro span#4").removeClass("aaa");
    $(".pro span#3").removeClass("aaa");
    $(".pro span#2").removeClass("aaa");
    $.ajax({
            url: "content/delete", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });
});

$("span#3").on("click",function(){
    $(".proac").removeClass("aaa");
    $(".pro span#1").removeClass("aaa");
    $("span#3").addClass("aaa");
    $(".pro span#2").removeClass("aaa");
    $(".pro span#4").removeClass("aaa");
    $(".pro span#5").removeClass("aaa");
    $.ajax({
            url: "content/add", // 後端的 API 路徑
            method: "GET", // 或 "POST"，根據需求
            // 傳遞參數給後端
            success: function (response) {
                // 成功後更新 use_1 區塊
                $(".use_1").html(response);
            },
            error: function (xhr, status, error) {
                console.error("發生錯誤:", error);
                alert("無法更新內容，請稍後再試。");
            },
        });
});