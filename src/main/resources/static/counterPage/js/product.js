
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


/* ===============中間商品============= */
document.addEventListener("DOMContentLoaded", () => {
    const categoryLinks = document.querySelectorAll(".goods-item a"); // 分类标签
    const products = document.querySelectorAll(".product-item"); // 商品列表
    const searchButton = document.getElementById("searchButton"); // 搜索按钮
    const searchInput = document.getElementById("searchInput"); // 搜索框

    let filteredProducts = Array.from(products); // 当前筛选的商品列表
    let currentPage = 0; // 当前页码
    const itemsPerPage = 3; // 每页显示的商品数量
    const pageInfo = document.getElementById("pageInfo");
    const nextPageButton = document.getElementById("nextPage");
    const prevPageButton = document.getElementById("prevPage");

    // 渲染当前商品列表
    function renderProducts() {
        // 隐藏所有商品
        products.forEach(product => (product.style.display = "none"));

        // 显示当前页的商品
        filteredProducts.slice(currentPage * itemsPerPage, (currentPage + 1) * itemsPerPage).forEach(product => {
            product.style.display = "block";
        });

        // 更新分页信息
        const totalPages = Math.ceil(filteredProducts.length / itemsPerPage);
        pageInfo.textContent = `${currentPage + 1} / ${totalPages}`;

        // 控制按钮状态
        prevPageButton.disabled = currentPage === 0;
        nextPageButton.disabled = currentPage === totalPages - 1 || totalPages === 0;
    }

    // 搜索功能
    function searchProducts() {
        const searchValue = searchInput.value.trim().toLowerCase();

        // 根据关键字筛选商品
        filteredProducts = Array.from(products).filter(product => {
            const itemName = product.getAttribute("data-name").toLowerCase();
            return itemName.includes(searchValue);
        });

        // 重置当前页为第一页
        currentPage = 0;

        // 重新渲染商品
        renderProducts();

        // 清除分类高亮状态
        categoryLinks.forEach(link => link.classList.remove("highlighted"));
    }

    // 分类筛选功能
    categoryLinks.forEach(link => {
        link.addEventListener("click", event => {
            event.preventDefault(); // 阻止默认跳转

            const selectedCategory = link.getAttribute("data-category");

            if (selectedCategory === "all") {
                // 显示所有商品
                filteredProducts = Array.from(products);
            } else {
                // 根据分类筛选商品
                filteredProducts = Array.from(products).filter(product => {
                    const productCategory = product.getAttribute("data-category");
                    return productCategory === selectedCategory;
                });
            }

            // 重置当前页为第一页
            currentPage = 0;

            // 重新渲染商品
            renderProducts();

            // 设置当前分类高亮
            categoryLinks.forEach(link => link.classList.remove("highlighted"));
            link.classList.add("highlighted");

            // 清空搜索框
            searchInput.value = "";
        });
    });

    // 搜索按钮事件
    searchButton.addEventListener("click", searchProducts);

    // 按下 Enter 键触发搜索
    searchInput.addEventListener("keydown", event => {
        if (event.key === "Enter") {
            event.preventDefault(); // 阻止默认提交
            searchProducts();
        }
    });

    // 下一页事件
    nextPageButton.addEventListener("click", () => {
        const totalPages = Math.ceil(filteredProducts.length / itemsPerPage);
        if (currentPage < totalPages - 1) {
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

    // 初次渲染商品
    renderProducts();
});


/* ===============中間商品============= */

/* ===============輪播圖============= */

document.addEventListener("DOMContentLoaded", function() {
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


/* ===============商品收藏愛心============= */
function toggleHeart(element) {
    // 切换爱心颜色
	const memAccount = element.getAttribute('data-memAccount');
	if (!memAccount) { // 判斷 null, undefined, 空字串
	       alert("請先登入");
	       return;
	   }
    element.classList.toggle('heart-active');
	const goodsNo = element.getAttribute('data-goodsNo');
    // 判断当前爱心状态
    const isActive = element.classList.contains('heart-active');
    const apiUrl = isActive ? '/goodsTrack/add' : '/goodsTrack/remove'; // 根據狀態选择API

    // 调用后端 API
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ goodsNo: goodsNo }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success === 'true') {
            } else {
                // 如果失败，可以回滚状态
                element.classList.toggle('heart-active');
            }
        })
        .catch(error => {
            console.error('请求出错:', error);
            // 如果请求失败，可以回滚状态
            element.classList.toggle('heart-active');
        });
}


/* ===============商品收藏愛心============= */



