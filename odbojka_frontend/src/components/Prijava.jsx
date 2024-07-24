import React from "react";
import "./Prijava.css";
import {useNavigate} from "react-router-dom";
function Prijava(props) {
  const navigate = useNavigate();
  const [forma, setForma] = React.useState({ username: "", password: ""});
  const [error, setError] = React.useState("");

  function onChange(event) {
    const {name, value} = event.target;
    setForma(stara => ({...stara, [name]: value}))
  }

  function onSubmit(e) {
    e.preventDefault();
    setError("");
    const body = `username=${forma.username}&password=${forma.password}`;
    const options = {
      method: 'POST',
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: body
    };
    fetch("/api/login", options)
      .then(response => {
        if (response.status === 401 || response.status === 500) {
          setError("Netočno korisničko ime ili lozinka.");
        } else {
          props.onLogin(response.headers.get("X-Ime"), response.headers.get("X-Prezime"), response.headers.get("X-Email"));
          navigate('/');
        }
      });
  }

  function provjeri(){
    const {username, password} = forma;
    return username.length > 0 && password.length > 0;
  }

  return (
    <>
      <div className="pozadina-pocetna"></div>
      <div className="prijava-glavni">
          <h2>Prijava</h2>
          <div className="error">{error}</div>
          <form className="prijava-form" onSubmit={onSubmit}>
              <div className="prijava-red">Korisničko ime:</div>
              <input name="username" onChange={onChange} value={forma.username} required/><br />
              <div className="prijava-red">Lozinka:</div>
              <input name="password" type="password" onChange={onChange} value={forma.password} required/><br />
              <div className="gumbi">
                  <button className="prijava-submit" onClick={(e) => { e.preventDefault(); navigate("/registracija"); }}> Izradi račun </button>
                  <button className="prijava-submit dr" type="submit" disabled={!provjeri()}>Prijavi se</button>
              </div>
          </form>
      </div>
    </>
  )
}
export default Prijava;