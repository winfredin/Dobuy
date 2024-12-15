
function confirmOrder(button, event) {

	// 暂停表单提交行为
	event.preventDefault();
	const counterDiv = button.closest(".summary-container");// 找到父元素
	console.log(counterDiv);

	const recipientName = document.querySelector(".recipientName").value;
	const recipientAddress = document.querySelector(".recipientAddress").value;
	const recipientPhone = document.querySelector(".recipientPhone").value;

	if (!recipientName) {
		alert("收件人姓名不能為空！");
		return;
	}
	if (!recipientAddress) {
		alert("收件人地址不能為空！");
		return;
	}
	if (!recipientPhone) {
		alert("收件人電話不能為空！");
		return;
	}
	document.getElementById("hiddenRecipientName").value = recipientName;
	document.getElementById("hiddenRecipientAddress").value = recipientAddress;
	document.getElementById("hiddenRecipientPhone").value = recipientPhone;

	// 通过 dataset 获取值
	const totalAmountBefore = counterDiv.querySelector(".totalAmountBefore").dataset.value;
	const totalAmountAfter = counterDiv.querySelector(".totalAmountAfter").dataset.value;
	const couponNo = counterDiv.querySelector(".newCouponsVO").dataset.value || 0;

	console.log("確認提交的數據：");
	console.log("收件人姓名:", recipientName);
	console.log("收件人地址:", recipientAddress);
	console.log("收件人電話:", recipientPhone);
	console.log("總金額（折前）:", totalAmountBefore);
	console.log("總金額（折後）:", totalAmountAfter);
	console.log("優惠券:", couponNo);
	// 动态更新隐藏字段的值
	const form = button.closest("form");
	form.querySelector("input[name='totalAmountBefore']").value = totalAmountBefore;
	form.querySelector("input[name='totalAmountAfter']").value = totalAmountAfter;
	form.querySelector("input[name='couponNo']").value = couponNo;


	// 提交表單
	button.closest("form").submit();
};

document.addEventListener("DOMContentLoaded", function () {
       const totalAmountBefore = document.querySelector(".totalAmountBefore").getAttribute("data-value");
       const totalAmountAfter = document.querySelector(".totalAmountAfter").getAttribute("data-value");

	   // 如果折扣後總額等於折扣前總額，表示沒有使用優惠券
	   if (totalAmountBefore === totalAmountAfter) {
	       document.querySelector(".totalAmountBefore").classList.remove("discounted");
	       document.querySelector(".totalAmountAfter").style.display = "none";
	   } else {
	       document.querySelector(".totalAmountBefore").classList.add("discounted");
	   }
   });
