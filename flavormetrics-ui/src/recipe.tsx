import { useEffect, useState } from "react";
import { RecipeCard } from "./recipe-card";
import { fetchAll } from "./services/recipe-service";
import type { RecipeDto } from "./types/recipe-types";
import { useNavigate } from "react-router-dom";

export function ListaRetete() {
  const [retete, setRetete] = useState<RecipeDto[]>([]);
  const [incarca, setIncarca] = useState(true);
  const [eroare, setEroare] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const getRetete = async () => {
      try {
        const data = await fetchAll();
        setRetete(data.data);
      } catch (err) {
        setEroare("Nu s-au putut încărca rețetele.");
        console.error(err);
      } finally {
        setIncarca(false);
      }
    };

    getRetete();
  }, []);

  if (incarca) {
    return <p>Se încarcă rețetele...</p>;
  }

  if (eroare) {
    return <p style={{ color: "red" }}>Eroare: {eroare}</p>;
  }

  return (
    <div className="flex gap-5 flex-wrap justify-center">
      {retete.length === 0 ? (
        <p>Nu există rețete.</p>
      ) : (
        retete.map((reteta) => (
          <RecipeCard
            key={reteta.id}
            img={
              reteta.imageUrl ??
              "https://ik.imagekit.io/vq8udofpo/flavormetrics/recipe_placeholder.png?updatedAt=1753228200931"
            }
            title={reteta.name}
            tag={reteta.tags.at(0)?.name ?? ""}
            dietary={reteta.difficulty.toString()}
            instructions={reteta.instructions}
            onClick={() => navigate(`/recipe/${reteta.id}`)}
          />
        ))
      )}
    </div>
  );
}
