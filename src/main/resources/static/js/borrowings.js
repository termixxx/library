document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('newBorrowingForm');

    form?.addEventListener('submit', function (e) {
        e.preventDefault();
        borrowBook();
    });
});

function borrowBook() {
    const clientId = document.getElementById('clientId').value;
    const bookId = document.getElementById('bookId').value;
    const borrowDate = document.getElementById('borrowDate').value;
    const errorMessage = document.getElementById('errorMessage');

    errorMessage.style.display = 'none';

    fetch('/api/borrowings?clientId=' + clientId + '&bookId=' + bookId + '&borrowDate=' + borrowDate, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.json();
        })
        .then(() => {
            window.location.reload();
        })
        .catch(error => {
            errorMessage.textContent = error.message || 'Error borrowing book';
            errorMessage.style.display = 'block';
        });
}

function returnBook(borrowingId) {
    if (confirm('Are you sure the book has been returned?')) {
        fetch('/api/borrowings/' + borrowingId, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Return failed');
                }
                const row = document.querySelector(`tr[data-borrowing-id="${borrowingId}"]`);
                if (row) row.remove();
            })
            .catch(error => {
                alert('Error returning book: ' + error.message);
            })
            .then(() => {
                window.location.reload();
            });
    }
}