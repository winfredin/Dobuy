
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
	if (!validateRecipientInfo()) {
       return; // 如果驗證不通過，停止執行
   }
	document.getElementById("hiddenRecipientName").value = recipientName;
	document.getElementById("hiddenRecipientAddress").value = recipientAddress;
	document.getElementById("hiddenRecipientPhone").value = recipientPhone;

	// 通过 dataset 获取值
	const totalAmountBefore = counterDiv.querySelector(".totalAmountBefore").dataset.value;
	const totalAmountAfter = counterDiv.querySelector(".totalAmountAfter").dataset.value;
	const couponNo = button.getAttribute('data-couponNo');

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
		
	   console.log(totalAmountBefore);
	   console.log(totalAmountAfter);
	   // 如果折扣後總額等於折扣前總額，表示沒有使用優惠券
	   if (totalAmountBefore === totalAmountAfter) {
	       document.querySelector(".totalAmountBefore").classList.remove("discounted");
	       document.querySelector(".totalAmountAfter").style.display = "none";
	   } else {
	       document.querySelector(".totalAmountBefore").classList.add("discounted");
	   }
   });

   
   function validateRecipientInfo() {
       const recipientPhone = document.querySelector(".recipientPhone").value;

       // 檢查收件人電話是否為空
       if (!recipientPhone) {
           alert("收件人電話不能為空！");
           return false; // 阻止後續操作
       }

       // 使用正規表達式驗證：10位數字
       const phonePattern = /^\d{10}$/;
       if (!phonePattern.test(recipientPhone)) {
           alert("收件人電話必須是10位數字！");
           return false; // 阻止後續操作
       }

       return true; // 通過驗證
   }

