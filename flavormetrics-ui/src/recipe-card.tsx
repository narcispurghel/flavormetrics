import "./App.css";

import { Button } from "@/components/ui/button";
import {
  HoverCard,
  HoverCardContent,
  HoverCardTrigger,
} from "@radix-ui/react-hover-card";
import {
  Card,
  CardAction,
  CardContent,
  CardDescription,
  CardTitle,
} from "./components/ui/card";

interface ChildProps {
  readonly title: string;
  readonly tag: string;
  readonly dietary: string;
  readonly img: string | undefined;
  readonly instructions: string | undefined;
  readonly onClick: () => void;
}

export function RecipeCard(props: ChildProps) {
  return (
    <Card
      className="text-amber-50 gap-4 w-60 h-90 bg-g-prim"
      onClick={props.onClick}
    >
      <CardContent className="w-full h-fit">
        <img alt="recipe" src={props.img} className="w-full h-40 rounded-sm" />
      </CardContent>
      <CardAction className="text-amber-50 flex flex-row justify-center w-full">
        <HoverCard>
          <HoverCardTrigger asChild>
            <Button variant="link" className="text-amber-50">
              {props.tag.length === 0 ? "" : `@${props.tag}`}
            </Button>
          </HoverCardTrigger>
          <HoverCardContent className="w-80 text-amber-50">
            <div>Content</div>
          </HoverCardContent>
        </HoverCard>
        <HoverCard>
          <HoverCardTrigger asChild>
            <Button variant="link" className="text-amber-50">
              {props.dietary.length === 0 ? "" : `@${props.dietary}`}
            </Button>
          </HoverCardTrigger>
          <HoverCardContent className="w-80 text-amber-50">
            <div>Content</div>
          </HoverCardContent>
        </HoverCard>
      </CardAction>
      <CardTitle className="m-0 text-center">{props.title}</CardTitle>
      <CardDescription className="text-amber-50 overflow-hidden text-center">
        {props.instructions ? verifyInstructions(props.instructions) : ""}
      </CardDescription>
    </Card>
  );
}

function verifyInstructions(instructions: string): string {
  return instructions.length >= 50 ? instructions?.slice(0, 50) : instructions;
}
