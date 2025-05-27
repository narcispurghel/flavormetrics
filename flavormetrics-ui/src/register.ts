import { RoleType } from '../enums/role-enum.ts'
import { RegisterData } from '../interfaces/register-data-interface.ts'
import { Data } from '../interfaces/data-interface.ts'
import { AUTH_ENDPOINTS } from '../constants/server-constants.ts'
import { PASSWORD_REGEX } from '../constants/validation-constants.ts'

const registerForm: Element | null = document.querySelector(".register-form");
const emailInput: HTMLInputElement | null = document.querySelector(".email-input") as HTMLInputElement;
const firstNameInput: HTMLInputElement | null = document.querySelector(".first-name-input") as HTMLInputElement;
const lastNameInput: HTMLInputElement | null = document.querySelector(".last-name-input") as HTMLInputElement;
const passwordInput: HTMLInputElement | null = document.querySelector(".password-input") as HTMLInputElement;
const confirmPasswordInput: HTMLInputElement | null = document.querySelector(".confirm-password-input") as HTMLInputElement;
const accountTypeInput: HTMLInputElement | null = document.querySelector(".account-type-input") as HTMLInputElement;
const passwordValidationElement: HTMLElement | null = document.querySelector(".check-password");
const passwordMatchElement: HTMLElement | null = document.querySelector(".match-password");

const checkIfAccountTypeIsValidRoleEnum: Function = (input: HTMLInputElement): void => {
  const role = RoleType[input.value as keyof typeof RoleType];
  if (!role) {
    input.style.border = "2px solid red"
  } else if (input.value.length === 0) {
    input.style.border = "none"
  } else {
    input.style.border = "2px solid green"
  }
}

const checkNameInput: Function = (input: HTMLInputElement): void => {
  if (input.value.length <= 5) {
    input.style.border = "2px solid red"
  } else if (input.value.length === 0) {
    input.style.border = "none"
  } else {
    input.style.border = "2px solid green"
  }
}

const checkIfPasswordsMatches: Function = (): void => {
  if (passwordInput && confirmPasswordInput && passwordMatchElement) {
    if (passwordInput.value !== confirmPasswordInput.value) {
      confirmPasswordInput.style.border = "2px solid red"
      passwordMatchElement.style.display = "block"
      passwordMatchElement.style.color = "red"
      passwordMatchElement.textContent = "password doesn't match"
    } else {
      passwordMatchElement.style.display = "none";
      confirmPasswordInput.style.border = "2px solid green"
    }
  }
}

const checkPasswordValue: Function = (): void => {
  if (passwordInput && passwordValidationElement) {
    if (passwordInput.value.length == 0) {
      passwordInput.style.border = "none"
    } else if (!PASSWORD_REGEX.exec(passwordInput.value)) {
      passwordInput.style.border = "2px solid red"
      passwordValidationElement.style.display = "block";
      passwordValidationElement.style.color = "red"
      passwordValidationElement.textContent = "weak password"
    } else {
      passwordValidationElement.style.display = "none";
      passwordInput.style.border = "2px solid green"
    }
  }
}

const handleSubmit: Function = (event: Event): void => {
  event.preventDefault();

  const accountType = accountTypeInput?.value;
  const role = RoleType[accountType as keyof typeof RoleType];

  if (!role) {
    throw new Error("Invalid account type")
  }

  const data: Data<RegisterData> = {
    data: {
      username: emailInput?.value ?? "",
      firstName: firstNameInput?.value ?? "",
      lastName: lastNameInput?.value ?? "",
      password: passwordInput?.value ?? "",
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
    .then((response: Response) => {
      if (response.status !== 201) {
        throw new Error(`Failed to create user account, response status is ${response.status}, expected 201`)
      }
      return response.json
    })
    .then((data: any) => {
      console.log(data)
      window.location.href = "../components/login-page.html"
    })
    .catch((error: any) => console.error(error));
}

if (registerForm == null) {
  throw new Error("Cannot find register form Element");
} else {
  registerForm.addEventListener("submit", (event: Event) => handleSubmit(event));
  passwordInput.addEventListener("input", () => checkPasswordValue())
  confirmPasswordInput.addEventListener("input", () => checkIfPasswordsMatches())
  if (firstNameInput) {
    firstNameInput.addEventListener("input", () => checkNameInput(firstNameInput))
  }
  if (lastNameInput) {
    lastNameInput.addEventListener("input", () => checkNameInput(lastNameInput))
  }
  if (emailInput) { // TODO: Create a specific function for email input
    emailInput.addEventListener("input", () => checkNameInput(emailInput))
  }
  if (accountTypeInput) {
    accountTypeInput.addEventListener("input", () => checkIfAccountTypeIsValidRoleEnum(accountTypeInput))
  }
}