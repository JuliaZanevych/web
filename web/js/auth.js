const hostUrl = 'http://localhost:8080';
const signInUrl = `${hostUrl}/auth/sign-in`;
const signUpUrl = `${hostUrl}/users/sign-up`;
const currentUserUrl = `${hostUrl}/users/current`;

const singIn = () => {
    const username = document.getElementById('signInUsername').value;
    const password = document.getElementById('signInPassword').value;

    if (!username) {
        alert('Введіть username!');
        return;
    }

    if (!password) {
        alert('Введіть пароль!');
        return;
    }

    const data = {
        username,
        password,
    };

    const httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", signInUrl);
    httpRequest.setRequestHeader('Content-Type', 'application/json');

    httpRequest.onload = () => {
        if (httpRequest.readyState === httpRequest.DONE) {
            if (httpRequest.status === 204) {
                const authHeader = httpRequest.getResponseHeader('Authorization');
                const authToken = `Bearer ${authHeader}`;
                localStorage.setItem('authToken', authToken);

                window.location.href = 'index.html';
            } else {
                alert('Введені вами логін і пароль не є вірні!');
            }
        }
    };

    httpRequest.send(JSON.stringify(data));
}

const singUp = () => {
    const username = document.getElementById('signUpUsername').value;
    const password = document.getElementById('signUpPassword').value;
    const repeatedPassword = document.getElementById('signUpRepeatedPassword').value;
    const firstName = document.getElementById('signUpFirstName').value;
    const middleName = document.getElementById('signUpMiddleName').value;
    const lastName = document.getElementById('signUpLastName').value;
    const email = document.getElementById('signUpEmail').value;
    const phone = document.getElementById('signUpPhone').value;
    const info = document.getElementById('signUpInfo').value;

    if (!username) {
        alert('Введіть username!');
        return;
    }

    if (!password) {
        alert('Введіть пароль!');
        return;
    }

    if (!repeatedPassword !== !password) {
        alert('Повторно введено неправильний пароль!');
        return;
    }

    if (!firstName) {
        alert('Введіть ім\'я!');
        return;
    }

    if (!middleName) {
        alert('Введіть по батькові!');
        return;
    }

    if (!lastName) {
        alert('Введіть прізвище!');
        return;
    }

    if (!email) {
        alert('Введіть email!');
        return;
    }

    if (!phone) {
        alert('Введіть телефон!');
        return;
    }

    const data = {
        username,
        password,
        firstName,
        middleName,
        lastName,
        email,
        phone,
        info
    };

    const httpRequest = new XMLHttpRequest();
    httpRequest.open("POST", signUpUrl);
    httpRequest.setRequestHeader('Content-Type', 'application/json');

    httpRequest.onload = () => {
        if (httpRequest.readyState === httpRequest.DONE) {
            if (httpRequest.status === 201) {
                window.location.href = 'sign-in.html';
            } else {
                alert('Користувач з таким username вже існує!');
            }
        }
    };

    httpRequest.send(JSON.stringify(data));
}

const processAuth = () => {
    const authToken = localStorage.getItem('authToken');
    if (authToken) {
        const httpRequest = new XMLHttpRequest();
        httpRequest.open("GET", currentUserUrl);
        httpRequest.setRequestHeader('Authorization', authToken);

        httpRequest.onload = function () {
            if (httpRequest.readyState === httpRequest.DONE) {
                if (httpRequest.status === 200) {
                    const {firstName} = JSON.parse(this.response);
                    const loginSection = document.getElementById('login-section');
                    loginSection.innerHTML=`<span>Hi, ${firstName}!</span> <span class="sign-out" onclick="signOut()">(Sign out)</span>`;
                } else {
                    localStorage.removeItem('authToken')
                }
            }
        };

        httpRequest.send();
    }
}

const signOut = () => {
    localStorage.removeItem('authToken');
    location.reload();
}
