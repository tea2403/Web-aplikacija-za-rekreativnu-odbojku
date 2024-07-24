import React from "react";
import "./Osobni.css";
import Igrac from "./Igrac.jsx";
function Osobni() {
    const [podaci, setPodaci] = React.useState({ ime: "", prezime: "", broj: "", profilna: null, brisi: false, pbr: ""});
    const [mjesta, setMjesta] = React.useState([]);
    const [error, setError] = React.useState('');
    const [prikazIgrac, setPrikazIgrac] = React.useState(false);
    const user = localStorage.getItem("e_mail");
    React.useEffect(() => {
      fetch(`/api/osoba/${user}`)
        .then(data => data.json())
        .then(podaci => {
          const novi = {};
          for (const key in podaci) {
            novi[key] = podaci[key] == null ? "" : podaci[key];
          }
          novi["profilna"]=null
          novi["brisi"]=false
          setPodaci(novi);
        })

      fetch("/api/mjesta/")
        .then(data => data.json())
        .then(novo => {setMjesta(novo);})
      }, []);
    
    function onChange(event) {
      const {name, value} = event.target;
      setPodaci(stara => ({...stara, [name]: value}))
    }
    function onSubmit(e) {
      e.preventDefault();  
      localStorage.setItem("ime", decodeURIComponent(podaci.ime));
      localStorage.setItem("prezime", decodeURIComponent(podaci.prezime));
      setError(""); 
      const formData = new FormData();
      formData.append("ime", podaci.ime);
      formData.append("prezime", podaci.prezime);
      formData.append("broj", podaci.broj);
      formData.append("profilna", podaci.profilna);
      formData.append("brisi", podaci.brisi);
      formData.append("pbr", podaci.pbr);
      const options = {
        method: "POST",
        body: formData
      };
      return fetch("/api/osoba/uredi", options)
        .then(response => {
          if (response.ok) {
            response.json().then(podaci1 => {
                var zast = 0
                const novi = {};
                for (const key in podaci1) {
                  novi[key] = podaci1[key] === null ? "" : podaci1[key];
                }
                novi["profilna"]=null
                novi["brisi"]=false
                if (podaci.profilna != null || podaci.brisi == "on") zast =1
                setPodaci(novi); 
                if (zast == 1)  window.location.reload()
                setError("Podaci uspješno promijenjeni.")         
            })
          } else {
            response.json().then((data) => {setError(data.message)})
          }
        });
    }
    function provjeri(){
      return podaci.ime.length > 0 && podaci.prezime.length > 0;
    }
    return (
        <>
          <div className="pozadina-pocetna" style={{backgroundImage: "url('images/poz.png')", opacity: "1"}}></div>
          <div className="osobni_glavni">
            <h2>Izmijenite svoje podatke</h2>
            <div className="error">{error}</div>
            <form className="osobni_form" onSubmit={onSubmit}>
              <label>Ime:</label>
              <input onChange={onChange} type="text" name="ime" value={podaci.ime} required/>
              <label>Prezime:</label>
              <input onChange={onChange} type="text" name="prezime" value={podaci.prezime} required/>
              <label>Broj mobitela:</label>
              <input onChange={onChange} type="tel" name="broj" value={podaci.broj}/>
              <label>Odaberite mjesto u kojem živite:</label>
              <select value={podaci.pbr} name="pbr" onChange={onChange}>
                <option key="0" value={"0"}>Ne želim reći.</option>
                {mjesta.map(mjesto1 => (
                  <option key={mjesto1.pbr} value={mjesto1.pbr}>{mjesto1.naziv_mjesta}, {mjesto1.pbr}</option>
                ))}
              </select>
              Promijenite profilnu sliku:
              <input onChange={(event) => setPodaci(stara => ({...stara, [event.target.name]: event.target.files[0]}))} type="file" name="profilna"  accept=".jpg, .jpeg, .png"></input>             
              <span><input onChange={onChange} type="checkbox" name="brisi"/><b>ne želim imati profilnu sliku</b></span> <br/>                           
              <button type="submit" className="osobni-submit" disabled={!provjeri()}>Promijeni</button>
            </form>
          </div>
          <div className="kruzic-prav" style={prikazIgrac ? {borderBottom: "none", borderBottomLeftRadius: "0px", borderBottomRightRadius: "0px"} : {}} onClick={() => setPrikazIgrac(!prikazIgrac)}>
            <span className="kruzic"><b>{prikazIgrac ? "\u25bc" : "\u25b6"}</b></span>IGRAČ
          </div>{prikazIgrac && <Igrac />}
        </>
    )
}
export default Osobni;