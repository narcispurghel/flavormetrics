import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { BadCredentialsError } from "@/errors";
import {
  fetchLogin,
  type LoginRequest,
  type LoginResponse,
} from "@/services/auth-service";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export function Login() {
  const navigate = useNavigate();
  const [fetchTrigger, setFetchTrigger] = useState<number>(0);
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [validCredentials, setValidCredentials] = useState<boolean>(true);

    const fetchData = async (body: LoginRequest) => {
      try {
        const res: LoginResponse = await fetchLogin(body);
        console.log(res)
        navigate("/?login=success");
      } catch (err: unknown) {
        if (err instanceof BadCredentialsError) {
          setValidCredentials(false);
        }
        console.error(err);
        navigate("/login/?login=err");
      }
    };

  useEffect(() => {
    if (fetchTrigger === 0) {
      return;
    }

    const body: LoginRequest = { email: email, password: password };

    fetchData(body);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [fetchTrigger, navigate]);

  return (
    <Card className="w-full max-w-sm h-fit">
      <CardHeader>
        <CardTitle>Login to your account</CardTitle>
        <CardDescription>
          Enter your email below to login to your account
        </CardDescription>
        <CardAction>
          <Button variant="link" onClick={() => navigate("/signup")}>
            Sign Up
          </Button>
        </CardAction>
      </CardHeader>
      <CardContent>
        <form>
          <div className="flex flex-col gap-6">
            <div className="grid gap-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="username@example.com"
                required
                className={validCredentials ? "" : "bg-red-200"}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setEmail(e.target.value)
                }
              />
            </div>
            <div className="grid gap-2">
              <div className="flex items-center">
                <Label htmlFor="password">Password</Label>
                <a
                  href="/forgot-password"
                  className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                >
                  Forgot your password?
                </a>
              </div>
              <Input
                id="password"
                type="password"
                placeholder="password"
                required
                className={validCredentials ? "" : "bg-red-200"}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setPassword(e.target.value)
                }
              />
            </div>
          </div>
        </form>
      </CardContent>
      <CardFooter className="flex-col gap-2">
        <Button
          type="submit"
          className="w-full"
          onClick={() => setFetchTrigger(fetchTrigger + 1)}
        >
          Login
        </Button>
        <Button variant="outline" className="w-full">
          Login with Google
        </Button>
      </CardFooter>
    </Card>
  );
}
