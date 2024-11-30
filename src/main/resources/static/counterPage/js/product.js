
/*======中間下拉式選單====== */

// 取得所有按鈕元素
const buttons = document.querySelectorAll('.menu-button');

// 對每個按鈕加上點擊事件
buttons.forEach(button => {
	button.addEventListener('click', () => {
		// 移除所有按鈕的 active 類別
		buttons.forEach(btn => btn.classList.remove('active'));
		// 將 active 類別添加到點擊的按鈕
		button.classList.add('active');
	});
});

/*======中間下拉式選單====== */

/* ===============中間商品左邊側欄樣式============= */
document.addEventListener("DOMContentLoaded", function() {
	// 獲取所有的連結
	const links = document.querySelectorAll(".hamburger-menu ul li a");

	links.forEach(link => {
		// 監聽每個連結的點擊事件
		link.addEventListener("click", function() {
			// 移除所有連結的 highlighted class
			links.forEach(item => item.classList.remove("highlighted"));

			// 為當前點擊的連結添加 highlighted class
			this.classList.add("highlighted");
		});
	});
});
/* ===============中間商品左邊側欄樣式============= */

/* ===============中間商品============= */
document.addEventListener("DOMContentLoaded", () => {
    const productContainer = document.getElementById("productContainer");
    const nextPageButton = document.getElementById("nextPage");
    const prevPageButton = document.getElementById("prevPage");
    const pageInfo = document.getElementById("pageInfo");
    const searchButton = document.getElementById("searchButton");
    const searchInput = document.getElementById("searchInput");

    const itemsPerPage = 3; // 每页显示的商品数量
    let currentPage = 0;
    let products = Array.from(document.querySelectorAll(".product-item")); // 初始化商品列表

    // 渲染当前页商品
    function renderProducts() {
        products.forEach((product, index) => {
            product.style.display =
                index >= currentPage * itemsPerPage &&
                index < (currentPage + 1) * itemsPerPage
                    ? "block"
                    : "none";
        });

        // 更新分页信息
        pageInfo.textContent = `${currentPage + 1} / ${Math.ceil(products.length / itemsPerPage)}`;

        // 按钮状态控制
        prevPageButton.disabled = currentPage === 0;
        nextPageButton.disabled = currentPage === Math.ceil(products.length / itemsPerPage) - 1;
    }

    // 下一页事件
    nextPageButton.addEventListener("click", () => {
        if (currentPage < Math.ceil(products.length / itemsPerPage) - 1) {
            currentPage++;
            renderProducts();
        }
    });

    // 上一页事件
    prevPageButton.addEventListener("click", () => {
        if (currentPage > 0) {
            currentPage--;
            renderProducts();
        }
    });

    // 搜索功能
    searchButton.addEventListener("click", () => {
        const searchValue = searchInput.value.trim().toLowerCase();

        // 过滤符合搜索条件的商品
        products = Array.from(document.querySelectorAll(".product-item")).filter((product) =>
            product.textContent.toLowerCase().includes(searchValue)
        );

        // 重置当前页为第一页
        currentPage = 0;

        // 重新渲染商品和分页
        renderProducts();
    });
	
	searchInput.addEventListener("keydown", (event) => {
	        if (event.key === "Enter") {
	            event.preventDefault(); // 阻止默认行为（如表单提交）
	            searchProducts(); // 触发搜索功能
	        }
	    });

    // 初始化渲染
    renderProducts();
});

/* ===============中間商品============= */

/* ===============輪播圖============= */

document.addEventListener("DOMContentLoaded", function () {
    const autoplayContainer = document.querySelector(".autoplay");

    // 初始化 Slick 輪播圖
    $(autoplayContainer).slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 2000,
        dots: true,
        arrows: false,
    });
});


/* ===============輪播圖============= */

/* ===============商品搜尋============= */

document.getElementById('searchButton').addEventListener('click', function () {
    // 获取搜索框中的值
    const searchValue = document.getElementById('searchInput').value.trim().toLowerCase();

    // 获取所有商品的元素
    const items = document.querySelectorAll('.product-item');

    // 遍历所有商品，显示或隐藏基于搜索结果的商品
    items.forEach(item => {
        const itemName = item.getAttribute('data-name').toLowerCase();
        if (itemName.includes(searchValue)) {
            item.style.display = 'block'; // 显示匹配的商品
        } else {
            item.style.display = 'none'; // 隐藏不匹配的商品
        }
    });
});

/* ===============商品搜尋============= */
