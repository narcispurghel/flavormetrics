import { AUTH_ENDPOINTS } from '../constants/server-constants.ts'
import { Data } from '../interfaces/data-interface.ts'
import { LoginData } from '../interfaces/login-data-interface.ts'

const loginForm: HTMLFormElement | null = document.querySelector(".login-form")
const emailInput: HTMLInputElement | null = document.querySelector(".email-input")
const passwordInput: HTMLInputElement | null = document.querySelector(".password-input")

const handleSubmit: Function = (event: Event): void => {
    event.preventDefault()
    if (emailInput && emailInput.value.length === 0) {
        return
    }
    if (passwordInput && passwordInput.value.length === 0) {
        return
    }
    const formData: Data<LoginData> = {
        data: {
            email: emailInput?.value ?? "",
            password: passwordInput?.value ?? ""
        }
    }

    fetch(`${AUTH_ENDPOINTS.get("LOGIN")}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then((response: Response) => {
            if (!response.ok) {
                throw new Error(`Failed to fetch data, response status is ${response.status}`)
            }
            return response.json
        })
        .then((data: any) => {
            console.log(data)
            window.location.href = "../index.html?login=success"
        })
        .catch((error: any) => console.log(error))
}

if (loginForm) {
    loginForm.addEventListener("submit", (event: Event) => handleSubmit(event))
}