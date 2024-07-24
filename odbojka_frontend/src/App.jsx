import * as React from "react";
import Prijava from "./components/Prijava"
import Header from "./components/Header";
import Pocetna from "./components/Pocetna";
import Registracija from "./components/Registracija"
import Naslovna from "./components/Naslovna"
import Osobni_podaci from "./components/Osobni"
import Tim from "./components/Tim"
import Tema from "./components/Tema"
import Forum from "./components/Forum"
import Iznajmi from "./components/Iznajmi"
import Rezerviraj from "./components/Rezerviraj"
import Moje_aktivnosti from "./components/Moje_aktivnosti"
import IgracInfo from "./components/IgracInfo"
import TerminForum from "./components/TerminForum"
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import "./App.css"

function App() {
  const [logiran, setLogiran] = React.useState(false);
  const [profilna, setProfilna] = React.useState("");
  React.useEffect(() => {
    if (logiran) {
      fetch(`/api/osoba/${localStorage.getItem("e_mail")}`)
        .then(data => data.json())
        .then(podaci => {setProfilna(podaci["profilna"])})
    }
  }, [logiran]);
  const routerLogiran = createBrowserRouter([
    {
      path: "/",
      element: <AppContainer onLogout={onLogout} profilna={profilna}/>,
      children: [
        {
          path: "/",
          element: <Naslovna/>,
          children: []
        },
        {
          path: "/osobni_podaci",
          element: <Osobni_podaci/>,
          children: []
        },
        {
          path: "/moje_aktivnosti",
          element: <Moje_aktivnosti/>,
          children: []
        }
        ,
        {
          path: "/tim",
          element: <Tim/>,
          children: []
        }
        ,
        {
          path: "/rezerviraj",
          element: <Rezerviraj/>,
          children: []
        }
        ,
        {
          path: "/forum",
          element: <Forum/>,
          children: []
        }
        ,
        {
          path: "/forum/tema/:id",
          element: <Tema />,
          children: []
        }
        ,
        {
          path: "/iznajmi",
          element: <Iznajmi/>,
          children: []
        },
        {
          path: "/igrac/:email",
          element: <IgracInfo/>,
          children: []
        },
        {
          path: "/termin/:id",
          element: <TerminForum/>,
          children: []
        }
      ]
    }
  ]);

  const routerNelogiran = createBrowserRouter([
    {
      path: "/",
      element: <Pocetna/>,
      children: []
    },
    {
      path: "/prijava",
      element: <Prijava onLogin={onLogin}/>,
      children: []
    },
    {
      path: "/registracija",
      element: <Registracija/>,
      children: []
    }
  ]);

  function onLogin(ime, prezime, e_mail) {
    sessionStorage.setItem("logiran", "true");
    localStorage.setItem("ime", decodeURIComponent(ime));
    localStorage.setItem("prezime", decodeURIComponent(prezime));
    localStorage.setItem("e_mail", e_mail);
    setLogiran(true);
}

  function onLogout() {
      sessionStorage.removeItem("logiran");
      localStorage.removeItem("ime");
      localStorage.removeItem("prezime");
      localStorage.removeItem("e_mail");
      sessionStorage.removeItem("pbr");
      setProfilna("");
      setLogiran(false);
  }
  if (logiran == false && sessionStorage.getItem("logiran") != null) {
    setLogiran(sessionStorage.getItem("logiran"))
  }
  if (!logiran) {
    return (<RouterProvider router={routerNelogiran}/>)
  }
  return (
    <RouterProvider router={routerLogiran}/>
  )
}

export default App

function AppContainer(props) {
  return (
    <div>
      <Header onLogout={props.onLogout} profilna={props.profilna}/>
      <div className="App">
        <Outlet/>
      </div>
    </div>
  )
}
