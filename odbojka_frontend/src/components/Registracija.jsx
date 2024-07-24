import React from "react";
import "./Registracija.css";
import {useNavigate} from "react-router-dom";
function Registracija() {
  const navigate = useNavigate();
  const [forma, setForma] = React.useState({ email: "", lozinka: "", ime: "", prezime: ""});
  const [error, setError] = React.useState("");

  function onChange(event) {
    const {name, value} = event.target;
    setForma(stara => ({...stara, [name]: value}))
  }

  function onSubmit(e) {
    e.preventDefault();
    setError("");
    const EMAIL_FORMAT = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/i;
    const LOZINKA_FORMAT = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
    const PREZIME_FORMAT = /^[a-zA-ZčČćĆžŽšŠđĐ]+([ '-][a-zA-ZčČćĆžŽšŠđĐ]+)*$/;
    const {email, lozinka, ime, prezime} = forma;
    if (!EMAIL_FORMAT.test(email)) {
      setError(`Email mora biti u ispravnom obliku, npr. user@example.com, a ne '${email}'.`);
      return;
    }
    if (!LOZINKA_FORMAT.test(lozinka)) {
      setError("Lozinka mora imati najmanje 8 znakova, barem jedno veliko slovo, jedno malo slovo i jedan broj.");
      return;
    }
    if (!PREZIME_FORMAT.test(ime) || !PREZIME_FORMAT.test(prezime)) {
      setError("Ime i prezime smiju sadržavati samo slova.");
      return;
    }
    const data = {
      ime: forma.ime,
      prezime: forma.prezime,
      email: forma.email,
      lozinka: forma.lozinka
    };
    const options = {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    };

    return fetch("/api/osoba/registracija", options)
      .then(response => {
        if (response.ok) {
          navigate('/prijava');
        } else {
          response.json().then((data) => {setError(data.message)})
        }
      });
  }

  function provjeri(){
    const {email, lozinka, ime, prezime} = forma;
    return (email.length > 0 && lozinka.length > 0 && ime.length > 0 && prezime.length > 0)
  }

  return (
    <>
      <div className="pozadina-pocetna"></div>
      <div className="prijava-glavni-reg">
          <h2>Registracija</h2>
          <div className="error">{error}</div>
          <form className="prijava-form" onSubmit={onSubmit}>
              <div className="prijava-red">Ime:</div>
              <input name="ime" onChange={onChange} value={forma.ime} required /><br />
              <div className="prijava-red">Prezime:</div>
              <input name="prezime" onChange={onChange} value={forma.prezime} required /><br />
              <div className="prijava-red">E-mail:</div>
              <input name="email" onChange={onChange} value={forma.email} required /><br />
              <div className="prijava-red">Lozinka:</div>
              <input name="lozinka" type="password" onChange={onChange} value={forma.lozinka} required /><br />
              <div className="gumbi">
                  <button className="prijava-submit" onClick={(e) => { e.preventDefault(); navigate("/prijava"); }}> Već imam račun </button>
                  <button className="prijava-submit dr" type="submit" disabled={!provjeri()}>Registriraj se</button>
              </div>
          </form>
      </div>
    </>
  )
}
export default Registracija;