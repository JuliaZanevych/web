const hostUrl = 'http://127.0.0.1:8081';
const signInUrl = `${hostUrl}/auth/login`;
const usersUrl = `${hostUrl}/users`;

const singIn = () => {
    const username = document.getElementById('sighInUsername').value;
    const password = document.getElementById('sighInPassword').value;

    const data = {
        username,
        password
    };

    fetch(signInUrl, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        for (var pair of response.headers.entries()) {
            console.log(pair[0]+ ': '+ pair[1]);
        }
        const authToken = response.headers.get('Authorization');
        alert('Success: ' + authToken);
    }).catch(error => {
        alert('Error: ' + error);
    });
}

const singUp = () => {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const repeatedPassword = document.getElementById('repeatedPassword').value;
    const firstName = document.getElementById('firstName').value;
    const middleName = document.getElementById('middleName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    const data = {
        username,
        password,
        firstName,
        middleName,
        lastName,
        email,
        phone
    };

    fetch(usersUrl, {
        method: 'POST',
        body: JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(async response => {
        const json = await response.json();
        alert('Success: ' + json);
    }).catch(error => {
        alert('Error: ' + error);
    });
}
