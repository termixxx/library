document.addEventListener('DOMContentLoaded', function () {
    const showFormBtn = document.getElementById('showFormBtn');
    const addClientForm = document.getElementById('addClientForm');
    const cancelBtn = document.getElementById('cancelBtn');
    const form = document.getElementById('clientForm');

    showFormBtn?.addEventListener('click', function () {
        resetForm();
        addClientForm.style.display = 'block';
        showFormBtn.style.display = 'none';
    });

    cancelBtn?.addEventListener('click', function () {
        addClientForm.style.display = 'none';
        showFormBtn.style.display = 'block';
        resetForm();
    });

    form?.addEventListener('submit', function (e) {
        e.preventDefault();
        handleFormSubmit();
    });
});

function resetForm() {
    const form = document.getElementById('clientForm');
    if (form) {
        form.reset();
        document.getElementById('clientId').value = '';
        document.getElementById('method').value = 'POST';
        document.getElementById('errorMessage').style.display = 'none';
    }
}

function editClient(clientId) {
    const addClientForm = document.getElementById('addClientForm');
    const showFormBtn = document.getElementById('showFormBtn');

    addClientForm.innerHTML = '<div class="loading">Loading client data...</div>';
    addClientForm.style.display = 'block';
    showFormBtn.style.display = 'none';

    fetch(`/api/clients/${clientId}`)
        .then(response => {
            if (!response.ok) throw new Error('Client not found');
            return response.json();
        })
        .then(client => {
            addClientForm.innerHTML = `
                    <form id="clientForm" method="POST">
                        <input type="hidden" id="clientId" name="id" value="${client.id}">
                        <input type="hidden" name="_method" id="method" value="PUT">

                        <label for="fullName">Full Name:</label>
                        <input type="text" id="fullName" name="fullName" value="${client.fullName}" required><br>

                        <label for="birthDate">Birth Date:</label>
                        <input type="date" id="birthDate" name="birthDate" value="${client.birthDate}" required><br>

                        <button type="submit">Update Client</button>
                        <button type="button" id="cancelBtn">Cancel</button>
                        <div id="errorMessage" class="error-message" style="display: none;"></div>
                    </form>
                `;

            document.getElementById('clientForm').addEventListener('submit', function (e) {
                e.preventDefault();
                handleFormSubmit();
            });

            document.getElementById('cancelBtn').addEventListener('click', function () {
                addClientForm.style.display = 'none';
                showFormBtn.style.display = 'block';
                resetForm();
            });
        })
        .catch(error => {
            addClientForm.innerHTML = `
                    <div class="error-message">
                        ${error.message}
                        <button onclick="location.reload()">Reload Page</button>
                    </div>
                `;
        });
}

function handleFormSubmit() {
    const form = document.getElementById('clientForm');
    const formData = new FormData(form);
    const errorMessage = document.getElementById('errorMessage');
    errorMessage.style.display = 'none';

    const clientId = formData.get('id');
    const url = clientId ? `/api/clients/${clientId}` : '/api/clients';
    const method = clientId ? 'PUT' : 'POST';

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
            errorMessage.textContent = error.message || 'Error saving client';
            errorMessage.style.display = 'block';
            console.error('Error:', error);
        });
}

function deleteClient(clientId) {
    if (!confirm('Are you sure you want to delete this client?')) {
        return;
    }

    fetch(`/api/clients/${clientId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Delete failed');
            }
            const row = document.querySelector(`tr[data-client-id="${clientId}"]`);
            if (row) {
                row.style.transition = 'opacity 0.3s';
                row.style.opacity = '0';
                setTimeout(() => row.remove(), 300);
            }
        })
        .catch(error => {
            alert('Error deleting client: ' + error.message);
            console.error('Error:', error);
        });
}