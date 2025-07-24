import type { UUID } from "crypto";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { RecipeCard } from "./recipe-card";
import { fetchById } from "./services/recipe-service";
import type { RecipeDto } from "./types/recipe-types";

export function RecipeView() {
  const { id } = useParams<{ id: UUID }>();
  const [recipe, setRecipe] = useState<RecipeDto | undefined>(undefined);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | undefined>(undefined);
  const navigate = useNavigate();

  useEffect(() => {
    const getRecipe = async () => {
      if (!id) {
        setError("ID-ul rețetei lipsește din URL.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(undefined);
        const fetchedRecipe = await fetchById(id);
        setRecipe(fetchedRecipe);
      } catch (err: any) {
        setError(err.message ?? "Eroare la încărcarea rețetei.");
      } finally {
        setLoading(false);
      }
    };

    getRecipe();
  }, [id]);

  if (loading) {
    return <div className="text-center p-4">Se încarcă rețeta...</div>;
  }

  if (error) {
    return <div className="text-center p-4 text-red-500">Eroare: {error}</div>;
  }

  if (!recipe) {
    return <div className="text-center p-4">Rețeta nu a fost găsită.</div>;
  }

  return (
    <div className="flex justify-center p-4">
      <RecipeCard
        title={recipe.name}
        tag={recipe.tags?.at(0)?.name ?? ""}
        dietary={recipe.difficulty?.toString() ?? ""}
        img={
          recipe.imageUrl ??
          "https://ik.imagekit.io/vq8udofpo/flavormetrics/recipe_placeholder.png?updatedAt=1753228200931"
        }
        instructions={recipe.instructions}
        onClick={() => navigate(`/recipe/${recipe.id.toString()}`)}
      />
    </div>
  );
}
