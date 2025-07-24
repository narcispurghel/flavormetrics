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
import { ConflictError } from "@/errors";
import type { RegisterRequest } from "@/services/auth-service";
import { register } from "@/services/auth-service";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export function Signup() {
  const navigate = useNavigate();
  const [firstname, setFirstname] = useState("");
  const [lastname, setLastname] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [trigger, setTrigger] = useState<number>(0);
  const [isEmailValid, setIsEmailValid] = useState<boolean>(true);
  let body: RegisterRequest = {
    email: "",
    firstName: "",
    lastName: "",
    password: "",
  };

  const handleRegister = async (body: RegisterRequest) => {
    try {
      await register(body);
      navigate("/login?signup=success");
    } catch (err: unknown) {
      console.error(err);
      if (err instanceof ConflictError) {
        setIsEmailValid(false);
      }
      navigate("/signup?signup=err");
    }
  };

  useEffect(() => {
    if (trigger === 0) {
      return;
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
    body = {
      email: email,
      firstName: firstname,
      lastName: lastname,
      password: password,
    };

    handleRegister(body);
  }, [trigger]);

  return (
    <Card className="w-full max-w-sm h-fit">
      <CardHeader>
        <CardTitle>Login to your account</CardTitle>
        <CardDescription>
          Enter your email below to signup your account
        </CardDescription>
        <CardAction>
          <Button variant="link" onClick={() => navigate("/login")}>
            Login
          </Button>
        </CardAction>
      </CardHeader>
      <CardContent>
        <form onSubmit={() => console.log("submit")}>
          <div className="flex flex-col gap-6">
            <div className="grid gap-2">
              <Label htmlFor="firstname">First Name</Label>
              <Input
                id="firstname"
                type="name"
                placeholder="firstname"
                required
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setFirstname(e.target.value)
                }
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="lastname">Last Name</Label>
              <Input
                id="lastname"
                type="name"
                placeholder="lastname"
                required
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setLastname(e.target.value)
                }
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="m@example.com"
                required
                onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                  setEmail(e.target.value)
                }
                className={isEmailValid ? "" : "bg-red-200"}
              />
            </div>
            <div className="grid gap-2">
              <div className="flex items-center">
                <Label htmlFor="password">Password</Label>
              </div>
              <Input
                id="password"
                type="password"
                required
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
          onClick={() => setTrigger(trigger + 1)}
        >
          Signup
        </Button>
        <Button variant="outline" className="w-full">
          Login with Google
        </Button>
      </CardFooter>
    </Card>
  );
}
