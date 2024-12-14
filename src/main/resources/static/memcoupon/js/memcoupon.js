document.addEventListener('DOMContentLoaded', function() {
    const itemsPerPage = 5;
    const items = document.querySelectorAll('.coupon_49_item');
    const totalPages = Math.ceil(items.length / itemsPerPage);
    const pagination = document.getElementById('coupon_49_pagination');
    let currentPage = 1;

    function generatePagination() {
        const prevButton = document.createElement('button');
        prevButton.innerHTML = '&laquo;';
        prevButton.onclick = () => showPage(currentPage - 1);
        pagination.appendChild(prevButton);

        for (let i = 1; i <= totalPages; i++) {
            const button = document.createElement('button');
            button.textContent = i;
            button.onclick = () => showPage(i);
            pagination.appendChild(button);
        }

        const nextButton = document.createElement('button');
        nextButton.innerHTML = '&raquo;';
        nextButton.onclick = () => showPage(currentPage + 1);
        pagination.appendChild(nextButton);
    }

    function showPage(pageNum) {
        if (pageNum < 1 || pageNum > totalPages) return;
        
        currentPage = pageNum;
        const start = (pageNum - 1) * itemsPerPage;
        const end = start + itemsPerPage;

        items.forEach(item => item.style.display = 'none');

        for (let i = start; i < end && i < items.length; i++) {
            items[i].style.display = 'block';
        }

        updatePaginationButtons();
    }

    function updatePaginationButtons() {
        const buttons = pagination.querySelectorAll('button');
        buttons.forEach((button, index) => {
            if (index === 0) {
                button.disabled = currentPage === 1;
            } else if (index === buttons.length - 1) {
                button.disabled = currentPage === totalPages;
            } else {
                button.classList.toggle('coupon_49_active', index === currentPage);
            }
        });
    }

    if (items.length > 0) {
        generatePagination();
        showPage(1);
    }
});