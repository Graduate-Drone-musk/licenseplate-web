import logo from "./logo.svg";
import "./App.css";
import { Route } from "react-router-dom";
import index from "./containers/index";
import find from "./containers/find";
import map from "./containers/map";

function App() {
  return (
    <div className="All">
      <Route path="/" exact component={index} />
      <Route path="/find" component={find} />
      <Route path="/map" component={map} />
    </div>
  );
}

export default App;
