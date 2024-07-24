import React from "react";
import "./Header.css";
import {useNavigate} from "react-router-dom";

function Header(props) {
  const [prikaz, setPrikaz] = React.useState(false);
  const navigate = useNavigate();
  function logout() {
    fetch("/api/logout").then(() => {
      props.onLogout();
      navigate('/')
    });
  }

  function prikaz_podataka(){
    setPrikaz(!prikaz)
  }


  return (
    <>
      <header className="Header">
        <img src="\images\home.png" alt="home" className="slika-profila home" onClick={(e) => { e.preventDefault(); navigate('/')}}></img>
        <span className="aktivnosti" onClick={(e) => { e.preventDefault(); navigate("/moje_aktivnosti")}}>Moje aktivnosti</span>
        <img src={props.profilna ? props.profilna : "/images/profil.png"} alt="profil" className="slika-profila" onClick={prikaz_podataka}></img>
      </header>
    {prikaz && (
      <div className="izbornik">
        <div className="centriraj">
          <img src={props.profilna ? props.profilna : "/images/profil.png"} alt="profil" className="slika-profila2"></img>
          <div><b>Ime:</b> {localStorage.getItem("ime")} </div>
          <div><b>Prezime:</b> {localStorage.getItem("prezime")} </div>
          <div><b>E-mail:</b> {localStorage.getItem("e_mail")} </div><br/>
        </div>
        <button id="uredi" onClick={(e) => {e.preventDefault(); navigate("/osobni_podaci"); prikaz_podataka();}}>Uredi podatke</button>
        <button id="odjava" onClick={logout}>Odjava</button>
      </div>
    )}</>
  )
}

export default Header;




