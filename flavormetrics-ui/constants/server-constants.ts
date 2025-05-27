export const API_URL: string = "http://localhost:8080/api/v1"
export const AUTH_ENDPOINTS: Map<string, string> = new Map([
    ["REGISTER", `${API_URL}/auth/register`],
    ["LOGIN", `${API_URL}/auth/register`]
])