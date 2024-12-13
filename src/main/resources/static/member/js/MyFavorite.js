function removeGoods(element) {
    // 获取商品编号
    const goodsNo = element.getAttribute('data-goodsNo');

    if (!goodsNo) {
        alert('無法移除，商品不存在！');
        return;
    }

    fetch('/goodsTrack/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ goodsNo })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success === "true") {
            alert(data.message);
            element.closest('div').remove();
        } else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error('移除失敗:', error);
        alert('移除失敗，請稍後再試！');
    });
}
