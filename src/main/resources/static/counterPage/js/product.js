
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

    const products = Array.from(document.querySelectorAll(".product-item")); // 获取所有商品元素
    const itemsPerPage = 3; // 每页显示的商品数量
    let currentPage = 0;

    // 渲染当前页商品
    function renderProducts() {
        products.forEach((product, index) => {
            // 根据索引决定是否显示商品
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