
/*======中間下拉式選單====== */

// 取得所有按鈕元素
const buttons = document.querySelectorAll('.menu-button');

// 對每個按鈕加上點擊事件
const sortByStockButton = document.getElementById('sortByStock'); // "最熱銷"按钮
const sortLowToHighButton = document.querySelector(".dropdown-content a:nth-child(1)");
const sortHighToLowButton = document.querySelector(".dropdown-content a:nth-child(2)");

sortLowToHighButton.addEventListener('click', function(event) {
	event.preventDefault(); // 阻止默認跳轉行為
	// 移除所有按鈕的 active 類別
	buttons.forEach(btn => btn.classList.remove('active'));
	// 將 active 類別添加到 "價格 ▼" 按鈕
	const parentButton = sortLowToHighButton.closest('.dropdown').querySelector('.menu-button');
	parentButton.classList.add('active');
});


// 為 "價格由高到低" 添加點擊事件
sortHighToLowButton.addEventListener('click', function(event) {
	event.preventDefault(); // 阻止默認跳轉行為

	// 移除所有按鈕的 active 類別
	buttons.forEach(btn => btn.classList.remove('active'));

	// 將 active 類別添加到 "價格 ▼" 按鈕
	const parentButton = sortHighToLowButton.closest('.dropdown').querySelector('.menu-button');
	parentButton.classList.add('active');
});



/*======中間下拉式選單====== */


document.addEventListener("DOMContentLoaded", () => {
	const categoryLinks = document.querySelectorAll(".goods-item a"); // 分类标签
	const products = document.querySelectorAll(".product-item"); // 商品列表
	const searchButton = document.getElementById("searchButton"); // 搜索按钮
	const searchInput = document.getElementById("searchInput"); // 搜索框
	const sortLowToHighButton = document.querySelector(".dropdown-content a:nth-child(1)");
	const sortHighToLowButton = document.querySelector(".dropdown-content a:nth-child(2)");
	const randomSortButton = document.getElementById("randomSortButton");
	const theNewest = document.getElementById("theNewest"); // 搜索按钮
	const productsContainer = document.getElementById("productContainer");
	const productItems = Array.from(productsContainer.querySelectorAll(".product-item"));


	let filteredProducts = Array.from(products); // 当前筛选的商品列表
	let currentPage = 0; // 当前页码
	const itemsPerPage = 3; // 每页显示的商品数量
	const pageInfo = document.getElementById("pageInfo");
	const nextPageButton = document.getElementById("nextPage");
	const prevPageButton = document.getElementById("prevPage");
	
	// 渲染当前商品列表
	function renderProducts() {
	    const parentContainer = document.getElementById("productContainer");
	    parentContainer.innerHTML = ""; // 清空商品容器
	
	    const start = currentPage * itemsPerPage;
	    const end = start + itemsPerPage;

	    const currentPageItems = filteredProducts.slice(start, end);
	    currentPageItems.forEach(product => {
	        parentContainer.appendChild(product); // 按新順序插入商品
	    });
		
		// 計算總頁數
	    const totalPages = Math.ceil(filteredProducts.length / itemsPerPage);

	    // 更新頁面信息
	    pageInfo.textContent = `${currentPage + 1} / ${totalPages}`;


	    // 確保分頁按鈕的狀態
	    prevPageButton.disabled = currentPage === 0;
	    nextPageButton.disabled = currentPage === totalPages - 1 || totalPages === 0;

	    // 動態插入總頁數到 <span id="pageInfo">
	    if (totalPages > 0) {
	        pageInfo.textContent = `${currentPage + 1} / ${totalPages}`;
	    } else {
	        pageInfo.textContent = "0 / 0"; // 如果沒有商品
	    }
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

	//最新排行
	theNewest.addEventListener("click", function() {
		// 对商品排序
		productItems.sort(function(a, b) {
			const timeA = new Date(a.querySelector(".product-time").getAttribute("data-time"));
			const timeB = new Date(b.querySelector(".product-time").getAttribute("data-time"));
			return timeB - timeA; // 按时间降序排序
		});

		// 重置当前页为第一页
		currentPage = 0;

		// 更新全局变量 filteredProducts，保存排序后的商品
		filteredProducts = productItems;

		// 调用分页渲染函数
		renderProducts();
	});

	// 绑定随机排列事件
	randomSortButton.addEventListener("click", () => {
		randomizeGoods();
	});

	// 随机排列函数
	function randomizeGoods() {
		filteredProducts.sort(() => Math.random() - 0.5); // 随机打乱数组
		currentPage = 0; // 重置当前页为第一页
		renderProducts(); // 重新渲染商品
	}


	// 分类筛选功能
	categoryLinks.forEach(link => {
		link.addEventListener("click", event => {
			event.preventDefault(); // 阻止默认跳转

			const selectedCategory = link.getAttribute("data-category");

			if (selectedCategory === "all") {
				filteredProducts = Array.from(products); // 显示所有商品
			} else {
				filteredProducts = Array.from(products).filter(product => {
					const productCategory = product.getAttribute("data-category");
					return productCategory === selectedCategory;
				});
			}

			currentPage = 0; // 重置当前页为第一页
			renderProducts();

			// 设置当前分类高亮
			categoryLinks.forEach(link => link.classList.remove("highlighted"));
			link.classList.add("highlighted");

			searchInput.value = ""; // 清空搜索框
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

	// 按库存排序函数
	function sortByStock() {
		filteredProducts.sort((a, b) => {
			const stockA = parseInt(a.querySelector(".product-store").textContent, 10); // 库存数量 A
			const stockB = parseInt(b.querySelector(".product-store").textContent, 10); // 库存数量 B
			return stockA - stockB; // 从小到大排序
		});

		// 移除所有按钮的 active 类
		buttons.forEach(button => button.classList.remove('active'));

		// 为 "最熱銷" 按钮添加 active 类
		sortByStockButton.classList.add('active');

		currentPage = 0;
		// 重新渲染商品
		renderProducts();
	}

	// 为 "最熱銷" 按钮绑定点击事件
	sortByStockButton.addEventListener('click', sortByStock);


	buttons.forEach(button => {

		button.addEventListener('click', () => {
			// 移除所有按鈕的 active 類別
			buttons.forEach(btn => btn.classList.remove('active'));
			// 將 active 類別添加到點擊的按鈕
			button.classList.add('active');
		});
	});




	// 排序功能
	function sortGoodsByPrice(order) {
		filteredProducts.sort((a, b) => {
			const priceA = parseFloat(a.querySelector(".product-price").textContent.replace(/[^\d.]/g, ""));
			const priceB = parseFloat(b.querySelector(".product-price").textContent.replace(/[^\d.]/g, ""));

			return order === "LowToHigh" ? priceA - priceB : priceB - priceA;
		});


		currentPage = 0; // 重置当前页为第一页
		renderProducts(); // 重新渲染商品
	}


	sortLowToHighButton.addEventListener("click", () => {
		sortGoodsByPrice("LowToHigh");
	});

	sortHighToLowButton.addEventListener("click", () => {
		sortGoodsByPrice("HighToLow");
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



document.addEventListener('DOMContentLoaded', () => {

	// 使用全局變量 window.favoriteGoodsSet
	//	   console.log(window.favoriteGoodsSet); // 確保全局變量正確加載
	// 遍历所有的爱心图标
	document.querySelectorAll('.heart-icon-container .fas.fa-heart').forEach(icon => {
		const goodsNo = icon.getAttribute('data-goodsNo'); // 获取商品编号
		if (favoriteGoodsSet.includes(goodsNo)) {
			// 如果该商品编号在 favoriteGoodsSet 中，将爱心标记为选中
			icon.classList.add('heart-active');
		}
	});
	
});
/* ===============商品收藏愛心============= */








