import { AUTH_ENDPOINTS } from "../constants/server-constants.js";
const loginForm = document.querySelector(".login-form");
const emailInput = document.querySelector(".email-input");
const passwordInput = document.querySelector(".password-input");
const handleSubmit = (event) => {
    var _a, _b;
    event.preventDefault();
    if (emailInput && emailInput.value.length === 0) {
        return;
    }
    if (passwordInput && passwordInput.value.length === 0) {
        return;
    }
    const formData = {
        data: {
            email: (_a = emailInput === null || emailInput === void 0 ? void 0 : emailInput.value) !== null && _a !== void 0 ? _a : "",
            password: (_b = passwordInput === null || passwordInput === void 0 ? void 0 : passwordInput.value) !== null && _b !== void 0 ? _b : ""
        }
    };
    fetch(`${AUTH_ENDPOINTS.get("LOGIN")}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then((response) => {
        if (!response.ok) {
            throw new Error(`Failed to fetch data, response status is ${response.status}`);
        }
        return response.json;
    })
        .then((data) => {
        console.log(data);
        window.location.href = "../index.html?login=success";
    })
        .catch((error) => console.log(error));
};
if (loginForm) {
    loginForm.addEventListener("submit", (event) => handleSubmit(event));
}
//# sourceMappingURL=login.js.map