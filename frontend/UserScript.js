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
            showLoggedInState(username);
        })
        .catch(error => {
            document.getElementById('response').innerText = error.message;
        });
}

function showLoggedInState(username) {
    document.getElementById('loginContainer').style.display = 'none';
    document.getElementById('loggedInContainer').style.display = 'block';
    document.getElementById('loggedInUser').innerText = `Logged in as: ${username}`;

    const userInfoContainer = document.getElementById('loggedInContainer');
    userInfoContainer.style.position = 'absolute';
    userInfoContainer.style.top = '10px';
    userInfoContainer.style.right = '10px';
}

function logoutUser() {
    document.getElementById('loginContainer').style.display = 'block';
    document.getElementById('loggedInContainer').style.display = 'none';
}

function toggleCreateAccountForm() {
    document.getElementById('loginContainer').style.display = 'none';
    document.getElementById('createAccountContainer').style.display = 'block';
}

function toggleLoginForm() {
    document.getElementById('loginContainer').style.display = 'block';
    document.getElementById('createAccountContainer').style.display = 'none';
}

function createAccount() {
    const newUsername = document.getElementById('newUsername').value;
    const newPassword = document.getElementById('newPassword').value;

    const userDto = {
        username: newUsername,
        password: newPassword
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
            const createResponseElement = document.getElementById('createResponse');
            createResponseElement.style.color = '#008000'; // Green color
            createResponseElement.innerText = `User created successfully. User ID: ${data}`;
        })
        // .then(data => {
        //     document.getElementById('createResponse').innerText = `User created successfully. User ID: ${data}`;
        // })
        .catch(error => {
            const createResponseElement = document.getElementById('createResponse');
            createResponseElement.style.color = '#ff0000'; // Red color
            createResponseElement.innerText = error.message;
        });
        // .catch(error => {
        //     document.getElementById('createResponse').innerText = error.message;
        // });
}