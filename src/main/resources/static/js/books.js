document.addEventListener('DOMContentLoaded', function () {
    // Инициализация элементов
    const showFormBtn = document.getElementById('showFormBtn');
    const addBookForm = document.getElementById('addBookForm');

    // Обработчик кнопки "Add New Book"
    showFormBtn?.addEventListener('click', function () {
        resetForm();
        addBookForm.style.display = 'block';
        showFormBtn.style.display = 'none';
    });

    // Обработчик кнопки "Cancel"
    document.getElementById('cancelBtn')?.addEventListener('click', function () {
        addBookForm.style.display = 'none';
        showFormBtn.style.display = 'block';
        resetForm();
    });

    // Обработчик отправки формы
    document.getElementById('bookForm')?.addEventListener('submit', function (e) {
        e.preventDefault();
        handleFormSubmit();
    });
});

// Сброс формы
function resetForm() {
    const form = document.getElementById('bookForm');
    if (form) {
        form.reset();
        document.getElementById('bookId').value = '';
        document.getElementById('method').value = 'POST';
        document.getElementById('errorMessage').style.display = 'none';
    }
}

// Редактирование книги
function editBook(bookId) {
    const addBookForm = document.getElementById('addBookForm');
    const showFormBtn = document.getElementById('showFormBtn');

    // Показываем индикатор загрузки
    addBookForm.innerHTML = '<div class="loading">Loading book data...</div>';
    addBookForm.style.display = 'block';
    showFormBtn.style.display = 'none';

    // Получаем данные книги
    fetch(`/api/books/${bookId}`)
        .then(response => {
            if (!response.ok) throw new Error('Book not found');
            return response.json();
        })
        .then(book => {
            // Заполняем форму данными
            addBookForm.innerHTML = `
                    <form id="bookForm" method="POST">
                        <input type="hidden" id="bookId" name="id" value="${book.id}">
                        <input type="hidden" name="_method" id="method" value="PUT">

                        <label for="title">Title:</label>
                        <input type="text" id="title" name="title" value="${book.title}" required><br>

                        <label for="author">Author:</label>
                        <input type="text" id="author" name="author" value="${book.author}" required><br>

                        <label for="isbn">ISBN:</label>
                        <input type="text" id="isbn" name="isbn" value="${book.isbn}" required><br>

                        <button type="submit">Update Book</button>
                        <button type="button" id="cancelBtn">Cancel</button>
                        <div id="errorMessage" class="error-message" style="display: none;"></div>
                    </form>
                `;

            // Добавляем обработчики для новой формы
            document.getElementById('bookForm').addEventListener('submit', function (e) {
                e.preventDefault();
                handleFormSubmit();
            });

            document.getElementById('cancelBtn').addEventListener('click', function () {
                addBookForm.style.display = 'none';
                showFormBtn.style.display = 'block';
                resetForm();
            });
        })
        .catch(() => {
            addBookForm.innerHTML = `
                    <div class="error-message">
                        Ошибка сохранения книги
                        <button onclick="location.reload()">Reload Page</button>
                    </div>
                `;
        });
}

// Обработка отправки формы
function handleFormSubmit() {
    const form = document.getElementById('bookForm');
    const formData = new FormData(form);
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.style.display = 'none';

    const bookId = formData.get('id');
    const url = bookId ? `/api/books/${bookId}` : '/api/books';
    const method = bookId ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        body: JSON.stringify(Object.fromEntries(formData)),
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw err;
                });
            }
            return response.json();
        })
        .then(() => {
            window.location.reload();
        })
        .catch(error => {
            errorMessage.textContent = 'Ошибка сохранения книги';
            errorMessage.style.display = 'block';
            console.error('Error:', error);
        });
}

// Удаление книги
function deleteBook(bookId) {
    if (!confirm('Are you sure you want to delete this book?')) {
        return;
    }

    fetch(`/api/books/${bookId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Delete failed');
            }
            // Удаляем строку из таблицы без перезагрузки
            const row = document.querySelector(`tr[data-book-id="${bookId}"]`);
            if (row) {
                row.style.transition = 'opacity 0.3s';
                row.style.opacity = '0';
                setTimeout(() => row.remove(), 300);
            }
        })
        .then(() => {
            window.location.reload();
        });
}