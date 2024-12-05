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



//=====================二手商品管理事件====================================

$(document).ready(function() {
    // 当 span id 为 a1 的元素被点击时，触发 AJAX 事件
    $('#a1').click(function() {
        // 使用AJAX从后端以POST方式获取数据
        $.ajax({
            url: '/used/getAllSellerUsedListFragment', // 服务器端 API，返回 Thymeleaf 片段
            type: 'POST',
            success: function(response) {
                console.log("Fragment HTML:", response); // 打印返回的 HTML，便于调试

                // 使用 fragment 替换 <div class="use_1">
                $('.use_1').html(response);

                // 检查并销毁已有 DataTables 实例
                if ($.fn.DataTable.isDataTable('#UsedList')) {
                    $('#UsedList').DataTable().destroy();
                }

                // 重新初始化 DataTables
                $('#UsedList').DataTable({
                    "lengthMenu": [3, 5, 10, 20, 50, 100],
                    "searching": true,
                    "paging": true,
                    "ordering": true,
                    "language": {
                        "processing": "處理中...",
                        "loadingRecords": "載入中...",
                        "lengthMenu": "顯示 _MENU_ 筆結果",
                        "zeroRecords": "沒有符合的結果",
                        "info": "顯示第 _START_ 至 _END_ 筆結果，共<font color=red> _TOTAL_ </font>筆",
                        "infoEmpty": "顯示第 0 至 0 筆結果，共 0 筆",
                        "infoFiltered": "(從 _MAX_ 筆結果中過濾)",
                        "paginate": {
                            "first": "第一頁",
                            "previous": "上一頁",
                            "next": "下一頁",
                            "last": "最後一頁"
                        }
                    }
                });

                // 初始化 Slick 特效
                function initializeSlick() {
                    $('.photo-slider').not('.slick-initialized').slick({
                        dots: false,
                        infinite: true,
                        speed: 300,
                        slidesToShow: 1,
                        adaptiveHeight: true
                    });
                }

                // 初次载入时初始化 Slick
                initializeSlick();

                // 监听 DataTables 分页切换事件，重新初始化 Slick
                $('#UsedList').on('draw.dt', function () {
                    initializeSlick();
                });
            },
            error: function(xhr, status, error) {
                console.error('Error fetching used list data:', error);
            }
        });
    });
});
//====================二手商品刪除事件======================

document.addEventListener("DOMContentLoaded", function () {
    // 初始化外部 DataTable
    let dataTable;

    // 动态绑定嵌入 DataTable 的删除按钮事件
    document.body.addEventListener('click', function (event) {
        if (event.target.classList.contains('deleteUsed')) {
            const button = event.target;

            // 获取绑定的商品编号 (usedNo)
            const usedNo = button.getAttribute('data-usedNo');

            // 确认删除操作
            if (!confirm(`確定要刪除商品編號 ${usedNo} 嗎？`)) return;

            // 发送删除请求
            fetch(`/used/deleteUsed`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ usedNo: usedNo }) // 将 usedNo 发送到后端
            })
                .then(response => response.json()) // 将响应解析为 JSON
                .then(data => {
                    if (data.success) {
                        alert(data.message); // 显示成功消息

                        
						$('#a1').click();// 重新觸發一次click事件取得 fragment
                    } else {
                        alert(`刪除失敗：${data.message}`);
                    }
                })
                .catch(error => {
                    console.error('刪除過程中發生錯誤:', error);
                    alert('刪除過程中發生錯誤');
                });
        }
    });

    // 当页面动态嵌入 DataTable 后，重新初始化 DataTable
    function initializeDataTable() {
        if (dataTable) {
            dataTable.destroy(); // 如果 DataTable 已初始化，先销毁
        }

        // 重新初始化 DataTable
        dataTable = $('#UsedList').DataTable({
            paging: true,
            searching: true,
            ordering: true
        });
    }

    // 假设触发嵌入 DataTable 的点击事件
    document.getElementById('loadDataTableButton').addEventListener('click', function () {
        // 模拟嵌入新表格（根据你的需求可能是 AJAX 或其他加载方式）
        // 这里假设 DataTable 已嵌入 DOM 中

        // 初始化新的 DataTable
        initializeDataTable();
    });
});





