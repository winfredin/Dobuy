// Example starter JavaScript for disabling form submissions if there are invalid fields
(() => {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  const forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
    }, false)
  })
})()


  document.addEventListener('DOMContentLoaded', function () {
    // 選取所有數量和價格欄位
    const quantities = document.querySelectorAll('.quantity');
    const prices = document.querySelectorAll('.price');
    const totalPriceField = document.getElementById('totalPrice');

    // 定義計算總價的函數
    function calculateTotal() {
      let total = 0;
      quantities.forEach((quantity, index) => {
        const price = parseFloat(prices[index].value) || 0;
        const qty = parseInt(quantity.value) || 0;
        total += price * qty;
      });
      totalPriceField.value = total.toFixed(2); // 更新總計欄位
    }

    // 監聽數量變化事件
    quantities.forEach(quantity => {
      quantity.addEventListener('input', calculateTotal);
    });

    // 初始計算總計
    calculateTotal();
  });

