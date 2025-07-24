import { Route, Routes } from "react-router-dom"; // Importă Routes și Route
import { LoginView } from "./login-view";
import { ListaRetete } from "./recipe";
import { RecipeView } from "./recipe-view";
import { SignupView } from "./signup-view";

function App() {
  return (
    <div>
      <Routes>
        <Route path="/" element={<ListaRetete />} />
        <Route path="/recipe/:id" element={<RecipeView />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/signup" element={<SignupView />} />
      </Routes>
    </div>
  );
}

export default App;
