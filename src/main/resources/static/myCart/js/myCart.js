
// 更新購物車數量
function updateCart(button, delta) {

	const goodsNo = button.getAttribute('data-id');
	const goodsPrice = button.getAttribute('data-price');
	const counterCname = button.getAttribute('data-counterCname');
	const counterDiv = document.querySelector(`[data-counter="${counterCname}"]`); // 找到櫃位的 DOM
	const totalAfterElement = counterDiv.querySelector(".afterPrice span");
	const totalCounterDiv = counterDiv.querySelector(".counterPrice");
	
	fetch('/cart/update', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			goodsNo: goodsNo,
			goodsPrice: goodsPrice,
			delta: delta,
			counterCname: counterCname
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				if (data.newQuantity === 0) {
					document.getElementById(`item-${goodsNo}`).remove();
					const counterDiv = document.querySelector(`[data-counter="${counterCname}"]`);
					alert("商品已移除");
					// 检查櫃位内是否还有商品
					if (!counterDiv.querySelector("[data-goods-no]")) {
						counterDiv.remove(); // 如果没有商品，删除整个櫃位
					} else {
						// 更新櫃位總金額和優惠券
						calculateCounterTotal(counterDiv);
						loadCouponsForCounter(counterDiv);						
						totalAfterElement.textContent = "";
					}
				} else {
					document.getElementById(`quantity-${goodsNo}`).innerText = data.newQuantity;
					document.getElementById(`total-${goodsNo}`).innerText = data.newTotal;

					// 更新櫃位总金额
					calculateCounterTotal(counterDiv);
					loadCouponsForCounter(counterDiv);
					totalAfterElement.textContent = "";
					totalCounterDiv.classList.remove("total-price"); // 移除刪除線的 class

				}
			} else {
				alert("更新失敗！");
			}
		});
}

// 刪除商品
function removeFromCart(button) {

	const goodsNo = button.getAttribute('data-id');
	const counterCname = button.getAttribute('data-counterCname');
	const counterDiv = document.querySelector(`[data-counter="${counterCname}"]`);
	const totalAfterElement = counterDiv.querySelector(".afterPrice span");
	const totalCounterDiv = counterDiv.querySelector(".counterPrice");

	fetch(`/cart/remove`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			goodsNo: goodsNo,
			counterCname: counterCname
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				document.getElementById(`item-${goodsNo}`).remove();
				alert("商品已移除");
				// 检查櫃位内是否还有其他商品
				const counterDiv = document.querySelector(`[data-counter="${counterCname}"]`);

				// 检查櫃位内是否还有商品
				if (!counterDiv.querySelector("[data-goods-no]")) {
					counterDiv.remove(); // 如果没有商品，删除整个櫃位
				}
				calculateCounterTotal(counterDiv);
				loadCouponsForCounter(counterDiv);
				totalAfterElement.textContent = "";
				totalCounterDiv.classList.remove("total-price"); // 移除刪除線的 class

			} else {
				alert("刪除失敗！");
			}
		});
}


//計算總金額
function calculateCounterTotal(counterDiv) {
	let totalAmount = 0;

	// 遍历当前櫃位内的所有商品
	const items = counterDiv.querySelectorAll(".item");
	for (let i = 0; i < items.length; i++) {
		const item = items[i];
		const price = parseInt(item.querySelector(".price").textContent); // 商品单价
		const quantity = parseInt(item.querySelector(".quantity").textContent); // 商品数量
		totalAmount += price * quantity; // 累加总金额
	}

	// 更新櫃位总金额显示
	const totalCounterElement = counterDiv.querySelector(`#total-counter-${counterDiv.getAttribute("data-counter")}`); // 直接通过 id 找到
	totalCounterElement.querySelector("span").textContent = totalAmount;
}


// 获取所有櫃位
function calculateAllCounters() {

	const counters = document.querySelectorAll("[data-counter]");
	for (let i = 0; i < counters.length; i++) {
		calculateCounterTotal(counters[i]); // 调用单个櫃位计算方法
	}
}


// 更新所有櫃位的优惠券选项
function updateAllCoupons() {
	const counters = document.querySelectorAll("[data-counter]");
	counters.forEach((counterDiv) => {
		loadCouponsForCounter(counterDiv); // 根据总金额加载优惠券
	});
}

document.addEventListener("DOMContentLoaded", function() {
	calculateAllCounters();
	updateAllCoupons();
});


// 动态加载优惠券，根据櫃位的总金额动态更新优惠券选项
function loadCouponsForCounter(counterDiv) {
	const counterCname = counterDiv.getAttribute("data-counter");
	const totalAmount = parseFloat(
		counterDiv.querySelector(".counterPrice span").textContent
	);

	// 如果总金额为 0，则清空优惠券
	if (totalAmount === 0) {
		const selectElement = counterDiv.querySelector(".couponSelect");
		selectElement.innerHTML = `<option value="">請選擇優惠券</option>`;
		return;
	}

	// 调用后端接口
	fetch(`test/coupons/getByTotal`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			counterCname: counterCname,
			totalAmount: totalAmount
		})
	})
		.then((response) => response.json())  // 解析返回的 JSON 数据
		.then((eligibleCoupons) => {
			const selectElement = counterDiv.querySelector(".couponSelect");
			// 清空当前选项
			selectElement.innerHTML = `<option value="">請選擇優惠券</option>`;

			// 添加符合条件的优惠券
			eligibleCoupons.forEach((coupon) => {
				const option = document.createElement("option");
				option.value = coupon.couponNo;
				option.textContent = `${coupon.couponTitle} - ${coupon.couponContext}`;
				selectElement.appendChild(option);
			});
		})
		.catch((error) => {
			console.error("加载优惠券失败：", error);
		});
}


//依照選擇的優惠券去更新折價後金額，若沒選則使用原價
function applyDiscount(event) {
	const selectedValue = event.target.value; // 获取选中的优惠券编号
	const counterDiv = event.target.closest("[data-counter]");// 找到父元素
	const totalAmount = parseFloat(
		counterDiv.querySelector(".counterPrice span").textContent
	);
	
	const totalCounterDiv = counterDiv.querySelector(".counterPrice");

	// 模拟发送到后端并获取折扣后的价格
	fetch(`/cart/test/coupons/applyCoupon`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({
			couponNo: selectedValue,
			totalAmount: totalAmount.toString()
		})
	})
		.then((response) => response.json())
		.then((finalPrice) => {
			// 更新折扣后的金额到页面
			const totalAfterElement = counterDiv.querySelector(".afterPrice span");
			totalAfterElement.textContent = finalPrice || "計算失敗";
			totalCounterDiv.classList.add("total-price"); // 添加刪除線的 class
		})
		.catch((error) => {
			console.error("发送失败：", error);
		});
}


