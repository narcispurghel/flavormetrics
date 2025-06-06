import { RoleType } from "../enums/role-enum.js";
import { AUTH_ENDPOINTS } from "../constants/server-constants.js";
import { PASSWORD_REGEX, EMAIL_REGEX } from "../constants/validation-constants.js";
const registerForm = document.querySelector(".register-form");
const emailInput = document.querySelector(".email-input");
const firstNameInput = document.querySelector(".first-name-input");
const lastNameInput = document.querySelector(".last-name-input");
const passwordInput = document.querySelector(".password-input");
const confirmPasswordInput = document.querySelector(".confirm-password-input");
const accountTypeInput = document.querySelector(".account-type-input");
const passwordValidationElement = document.querySelector(".check-password");
const passwordMatchElement = document.querySelector(".match-password");
const accountTypeValidationElement = document.querySelector(".valid-account-type");
const checkIfAccountTypeIsValidRoleEnum = (input) => {
    const role = RoleType[input.value];
    if (input.value.length === 0 && accountTypeValidationElement) {
        input.style.border = "none";
        accountTypeValidationElement.style.display = "none";
        return false;
    }
    else if (!role && accountTypeValidationElement) {
        input.style.border = "2px solid red";
        accountTypeValidationElement.style.display = "block";
        accountTypeValidationElement.style.color = "red";
        accountTypeValidationElement.textContent = "invalid account type";
        return false;
    }
    else if (role && accountTypeValidationElement) {
        input.style.border = "2px solid green";
        accountTypeValidationElement.style.display = "none";
        return true;
    }
    return false;
};
const checkIfEmailIsValid = (input) => {
    if (input.value.length === 0) {
        emailInput.style.border = "none";
        return false;
    }
    else if (!EMAIL_REGEX.exec(input.value)) {
        emailInput.style.border = "2px solid red";
        return false;
    }
    else {
        emailInput.style.border = "2px solid green";
        return true;
    }
};
const checkNameInput = (input) => {
    if (input.value.length === 0) {
        input.style.border = "none";
        return false;
    }
    else if (input.value.length < 3) {
        input.style.border = "2px solid red";
        return false;
    }
    else {
        input.style.border = "2px solid green";
        return true;
    }
};
const checkIfPasswordsMatches = () => {
    if (passwordInput && confirmPasswordInput && passwordMatchElement) {
        if (passwordInput.value !== confirmPasswordInput.value) {
            confirmPasswordInput.style.border = "2px solid red";
            passwordMatchElement.style.display = "block";
            passwordMatchElement.style.color = "red";
            passwordMatchElement.textContent = "password doesn't match";
            return false;
        }
        else {
            passwordMatchElement.style.display = "none";
            confirmPasswordInput.style.border = "2px solid green";
            return true;
        }
    }
    return false;
};
const checkPasswordValue = () => {
    if (passwordInput && passwordValidationElement) {
        if (passwordInput.value.length == 0) {
            passwordInput.style.border = "none";
            return false;
        }
        else if (!PASSWORD_REGEX.exec(passwordInput.value)) {
            passwordInput.style.border = "2px solid red";
            passwordValidationElement.style.display = "block";
            passwordValidationElement.style.color = "red";
            passwordValidationElement.textContent = "weak password";
            return false;
        }
        else {
            passwordValidationElement.style.display = "none";
            passwordInput.style.border = "2px solid green";
            return true;
        }
    }
    return false;
};
const handleSubmit = (event) => {
    var _a, _b, _c, _d;
    event.preventDefault();
    let role;
    if (emailInput && !checkIfEmailIsValid(emailInput)) {
        emailInput.style.border = "2px solid red";
        return;
    }
    if (firstNameInput && !checkNameInput(firstNameInput)) {
        firstNameInput.style.border = "2px solid red";
        return;
    }
    if (lastNameInput && !checkNameInput(lastNameInput)) {
        lastNameInput.style.border = "2px solid red";
        return;
    }
    if (passwordInput && !checkPasswordValue(passwordInput)) {
        passwordInput.style.border = "2px solid red";
        return;
    }
    if (passwordInput && confirmPasswordInput && !checkIfPasswordsMatches()) {
        confirmPasswordInput.style.border = "2px solid red";
        return;
    }
    if (accountTypeInput && !checkIfAccountTypeIsValidRoleEnum(accountTypeInput)) {
        accountTypeInput.style.border = "2px solid red";
        return;
    }
    else {
        role = RoleType[accountTypeInput.value];
    }
    const data = {
        data: {
            username: (_a = emailInput === null || emailInput === void 0 ? void 0 : emailInput.value) !== null && _a !== void 0 ? _a : "",
            firstName: (_b = firstNameInput === null || firstNameInput === void 0 ? void 0 : firstNameInput.value) !== null && _b !== void 0 ? _b : "",
            lastName: (_c = lastNameInput === null || lastNameInput === void 0 ? void 0 : lastNameInput.value) !== null && _c !== void 0 ? _c : "",
            password: (_d = passwordInput === null || passwordInput === void 0 ? void 0 : passwordInput.value) !== null && _d !== void 0 ? _d : "",
            role: role,
        },
    };
    fetch(`${AUTH_ENDPOINTS.get("REGISTER")}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    })
        .then((response) => {
        if (response.status !== 201) {
            throw new Error(`Failed to create user account, response status is ${response.status}, expected 201`);
        }
        return response.json;
    })
        .then((data) => {
        console.log(data);
        window.location.href = "../components/login-page.html";
    })
        .catch((error) => console.log(error));
};
if (registerForm == null) {
    throw new Error("Cannot find register form Element");
}
else {
    registerForm.addEventListener("submit", (event) => handleSubmit(event));
    passwordInput.addEventListener("input", () => checkPasswordValue());
    confirmPasswordInput.addEventListener("input", () => checkIfPasswordsMatches());
    if (firstNameInput) {
        firstNameInput.addEventListener("input", () => checkNameInput(firstNameInput));
    }
    if (lastNameInput) {
        lastNameInput.addEventListener("input", () => checkNameInput(lastNameInput));
    }
    if (emailInput) {
        emailInput.addEventListener("input", () => checkIfEmailIsValid(emailInput));
    }
    if (accountTypeInput) {
        accountTypeInput.addEventListener("input", () => checkIfAccountTypeIsValidRoleEnum(accountTypeInput));
    }
}
//# sourceMappingURL=register.js.map