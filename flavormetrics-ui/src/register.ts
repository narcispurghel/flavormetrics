import { RoleType } from '../enums/role-enum.ts'
import { RegisterData } from '../interfaces/register-data-interface.ts'
import { Data } from '../interfaces/data-interface.ts'
import { AUTH_ENDPOINTS } from '../constants/server-constants.ts'
import { PASSWORD_REGEX, EMAIL_REGEX } from '../constants/validation-constants.ts'

const registerForm: Element | null = document.querySelector(".register-form");
const emailInput: HTMLInputElement | null = document.querySelector(".email-input") as HTMLInputElement;
const firstNameInput: HTMLInputElement | null = document.querySelector(".first-name-input") as HTMLInputElement;
const lastNameInput: HTMLInputElement | null = document.querySelector(".last-name-input") as HTMLInputElement;
const passwordInput: HTMLInputElement | null = document.querySelector(".password-input") as HTMLInputElement;
const confirmPasswordInput: HTMLInputElement | null = document.querySelector(".confirm-password-input") as HTMLInputElement;
const accountTypeInput: HTMLInputElement | null = document.querySelector(".account-type-input") as HTMLInputElement;
const passwordValidationElement: HTMLElement | null = document.querySelector(".check-password");
const passwordMatchElement: HTMLElement | null = document.querySelector(".match-password");
const accountTypeValidationElement: HTMLElement | null = document.querySelector(".valid-account-type");

const checkIfAccountTypeIsValidRoleEnum: Function = (input: HTMLInputElement): boolean => {
  const role = RoleType[input.value as keyof typeof RoleType];
  if (input.value.length === 0 && accountTypeValidationElement) {
    input.style.border = "none"
    accountTypeValidationElement.style.display = "none"
    return false
  } else if (!role && accountTypeValidationElement) {
    input.style.border = "2px solid red"
    accountTypeValidationElement.style.display = "block"
    accountTypeValidationElement.style.color = "red"
    accountTypeValidationElement.textContent = "invalid account type"
    return false
  } else if (role && accountTypeValidationElement) {
    input.style.border = "2px solid green"
    accountTypeValidationElement.style.display = "none"
    return true
  }
  return false
}

const checkIfEmailIsValid: Function = (input: HTMLInputElement): boolean => {
  if (input.value.length === 0) {
    emailInput.style.border = "none"
    return false
  } else if (!EMAIL_REGEX.exec(input.value)) {
    emailInput.style.border = "2px solid red"
    return false
  } else {
    emailInput.style.border = "2px solid green"
    return true
  }
}

const checkNameInput: Function = (input: HTMLInputElement): boolean => {
  if (input.value.length === 0) {
    input.style.border = "none"
    return false
  } else if (input.value.length < 3) {
    input.style.border = "2px solid red"
    return false
  } else {
    input.style.border = "2px solid green"
    return true
  }
}

const checkIfPasswordsMatches: Function = (): boolean => {
  if (passwordInput && confirmPasswordInput && passwordMatchElement) {
    if (passwordInput.value !== confirmPasswordInput.value) {
      confirmPasswordInput.style.border = "2px solid red"
      passwordMatchElement.style.display = "block"
      passwordMatchElement.style.color = "red"
      passwordMatchElement.textContent = "password doesn't match"
      return false
    } else {
      passwordMatchElement.style.display = "none";
      confirmPasswordInput.style.border = "2px solid green"
      return true
    }
  }
  return false
}

const checkPasswordValue: Function = (): boolean => {
  if (passwordInput && passwordValidationElement) {
    if (passwordInput.value.length == 0) {
      passwordInput.style.border = "none"
      return false
    } else if (!PASSWORD_REGEX.exec(passwordInput.value)) {
      passwordInput.style.border = "2px solid red"
      passwordValidationElement.style.display = "block";
      passwordValidationElement.style.color = "red"
      passwordValidationElement.textContent = "weak password"
      return false
    } else {
      passwordValidationElement.style.display = "none";
      passwordInput.style.border = "2px solid green"
      return true
    }
  }
  return false
}

const handleSubmit: Function = (event: Event): void => {
  event.preventDefault();
  let role: RoleType;

  if (emailInput && !checkIfEmailIsValid(emailInput)) {
    emailInput.style.border = "2px solid red"
    return
  }
  if (firstNameInput && !checkNameInput(firstNameInput)) {
    firstNameInput.style.border = "2px solid red"
    return
  }
  if (lastNameInput && !checkNameInput(lastNameInput)) {
    lastNameInput.style.border = "2px solid red"
    return
  }
  if (passwordInput && !checkPasswordValue(passwordInput)) {
    passwordInput.style.border = "2px solid red"
    return
  }
  if (passwordInput && confirmPasswordInput && !checkIfPasswordsMatches()) {
    confirmPasswordInput.style.border = "2px solid red"
    return
  }
  if (accountTypeInput && !checkIfAccountTypeIsValidRoleEnum(accountTypeInput)) {
    accountTypeInput.style.border = "2px solid red"
    return
  } else {
    role = RoleType[accountTypeInput.value as keyof typeof RoleType]
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
    .catch((error: any) => console.log(error));
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
  if (emailInput) {
    emailInput.addEventListener("input", () => checkIfEmailIsValid(emailInput))
  }
  if (accountTypeInput) {
    accountTypeInput.addEventListener("input", () => checkIfAccountTypeIsValidRoleEnum(accountTypeInput))
  }
}