function loginUser() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const userDto = {
        username: username,
        password: password
    };

    fetch('http://localhost:8080/api/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userDto),
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                throw new Error('Invalid credentials');
            } else {
                throw new Error('Failed to login');
            }
        })
        .then(data => {
            document.getElementById('response').innerText = `Logged in successfully. User ID: ${data}`;
        })
        .catch(error => {
            document.getElementById('response').innerText = error.message;
        });
}

function createUser() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const userDto = {
        username: username,
        password: password
    };

    fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(userDto),
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 400) {
                throw new Error('Username is already taken');
            } else {
                throw new Error('Failed to create user');
            }
        })
        .then(data => {
            document.getElementById('response').innerText = `User created successfully. User ID: ${data}`;
        })
        .catch(error => {
            document.getElementById('response').innerText = error.message;
        });
}