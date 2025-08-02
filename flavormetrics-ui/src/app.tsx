import { Route, Routes } from "react-router-dom"; // Importă Routes și Route
import { Navigation } from "./components/navigation";
import { LoginView } from "./login-view";
import { ListaRetete } from "./recipe";
import { RecipeView } from "./recipe-view";
import { SignupView } from "./signup-view";

function App() {
  return (
    <div className="w-screen flex flex-col h-fit gap-20">
      <Navigation />
      <div>
        <Routes>
          <Route path="/" element={<ListaRetete />} />
          <Route path="/recipe/:id" element={<RecipeView />} />
          <Route path="/login" element={<LoginView />} />
          <Route path="/signup" element={<SignupView />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
