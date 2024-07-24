import React from "react";
import "./Tim.css";
import {useNavigate} from "react-router-dom";
import ReactPaginate from 'react-paginate';
function Tim() {
    const [error, setError] = React.useState("");
    const [prikaziformu, setPrikaziformu] = React.useState(false);
    const [podaci, setPodaci] = React.useState({ naziv: ""});
    const [timovi, setTimovi] = React.useState([]);
    const [noviIgracEmail, setNoviIgracEmail] = React.useState("");
    const [prikaziUnos, setPrikaziUnos] = React.useState(-1);
    const user = localStorage.getItem("e_mail");
    const navigate = useNavigate();
    const [trenutna, setTrenutna] = React.useState(0);

    const offset = trenutna * 8;
    const prikazane = timovi.slice(offset, offset + 8);

    function handlePageClick(data) {
      setTrenutna(data.selected);
    };

    React.useEffect(() => {
      fetch("/api/tim/")
        .then(data => data.json())
        .then(podaci => {
          const novi = [];
          for (const key in podaci) {
            novi.push({ naziv: podaci[key].naziv, kapetan: podaci[key].kapetan, igraci: podaci[key].igraci.split(" ").filter(email => email !== podaci[key].kapetan)})
          }
          setTimovi(novi);
        })
    }, []);

    function dodajIgraca (index){
      if (noviIgracEmail === "") return
      setError("");
      const body = `naziv=${timovi[index].naziv}&igrac=${noviIgracEmail}`;
      const options = {
        method: "POST",
        headers: {
          "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: body,
      };
      
      return fetch("/api/tim/dodaj", options)
        .then(response => {
          if (response.ok) {
            var igrac = noviIgracEmail;
            const novi = [...timovi];
            novi[index].igraci.push(igrac);
            setTimovi(novi);
            setPrikaziUnos(-1); 
            setNoviIgracEmail("")
          } else {
            response.json().then((data) => {alert(data.message)})
          }
        });
    };
    function izbrisiIgraca (index, i){
      setError("");
      if (i==-1 && !window.confirm("Ako izbrišete kapetana izbrisat će se cijeli tim. Jeste li sigurni da želite izbrisati ovaj tim?")) return
      var izbrisi
      timovi[index].igraci[i] ? izbrisi = timovi[index].igraci[i] : izbrisi = timovi[index].kapetan
      const body = `naziv=${timovi[index].naziv}&igrac=${izbrisi}`;
      const options = {
          method: "DELETE",
          headers: {
            "Content-Type" : "application/x-www-form-urlencoded"
          },
          body: body,
      };
      
      return fetch("/api/tim/izbrisi", options)
      .then(response => {
        if (response.ok) {
          if (i==-1){
            const novi = [...timovi];
            novi.splice(index, 1);
            setTimovi(novi);
            return
          }
          const novi = [...timovi];
          const noviIgraci = [...novi[index].igraci];
          noviIgraci.splice(i, 1);
          novi[index].igraci = noviIgraci;
          if (izbrisi == user && user != "admin@email.com") {
            novi.splice(index, 1);
          }
          setTimovi(novi);
        } else {
          response.json().then((data) => {alert(data.message)})
        }
      });
    };
    function prikaz_forma(){
      setPrikaziformu(!prikaziformu)
      setPodaci({ naziv: ""})
      setError("")
    }
    function onChange(event) {
      const {name, value} = event.target;
      setPodaci(stara => ({...stara, [name]: value}))
    }
    function provjeri(){
      return podaci.naziv.length > 0;
    }
    function onSubmit(e) { 
      e.preventDefault();
      setError("");
      const body = `naziv=${podaci.naziv}`;
      const options = {
        method: "POST",
        headers: {
          "Content-Type" : "application/x-www-form-urlencoded"
        },
        body: body,
      };
      
      return fetch("/api/tim/", options)
        .then(response => {
          if (response.ok) {
            setTimovi(stari => [
              { naziv: podaci.naziv, kapetan: localStorage.getItem("e_mail"), igraci: []}, ...stari
            ]);
            setPrikaziformu(false);
          } else {
            response.json().then((data) => {setError(data.message)})
          }
        });
    }
    return (
        <>
            <div className="pozadina-pocetna" style={{backgroundImage: "url('images/poz.png')", opacity: "1"}}></div>
            <div className="sviTimovi" >
              <button id="zapocni" onClick={prikaz_forma}>Napravi novi tim</button>
              <h2 className="mojiTimovi">Moji timovi:</h2>
              <div id="timovi">
                  {prikazane.map((tim, index) => (
                      <div key={offset + index} className="tim">
                          <h2>{tim.naziv}</h2>
                          <p className="igracRed"><b style={{cursor: "pointer"}} onClick={() => navigate(`/igrac/${tim.kapetan}`)}>1. {tim.kapetan}</b> {(user === tim.kapetan || user == "admin@email.com") && (
                              <button onClick={() => {izbrisiIgraca(offset + index, -1)}}>-</button>)}</p>
                          {tim.igraci.map((igrac, i) => (
                              <div key={i} className="igracRed">
                                  <p key={i} onClick={() => navigate(`/igrac/${igrac}`)} >{i + 2}. {igrac}</p>
                                  {(user === igrac || user === tim.kapetan || user == "admin@email.com") && (
                                      <button onClick={() => {izbrisiIgraca(offset + index, i)}}>-</button>
                                  )}
                              </div>
                          ))}                      
                          {(user === tim.kapetan || user == "admin@email.com") && (
                              <button id="dodajIgraca" onClick={() => setPrikaziUnos(index)}>Dodaj</button>
                          )}
                          {prikaziUnos == index && (                           
                              <div className="noviIgrac">
                                  <input type="email" value={noviIgracEmail} onChange={(e) => setNoviIgracEmail(e.target.value)} placeholder="Unesite email novog igrača" />
                                  <button onClick={() => {dodajIgraca(offset + index); }}>Potvrdi</button>
                              </div>
                          )}
                      </div>
                  ))}
              </div>
              <ReactPaginate
                previousLabel={"Prethodna"}
                nextLabel={"Sljedeća"}
                breakLabel={"..."}
                breakClassName={"break-me"}
                pageCount={Math.ceil(timovi.length / 8)}
                marginPagesDisplayed={2}
                pageRangeDisplayed={5}
                onPageChange={handlePageClick}
                containerClassName={"pagination2"}
                activeClassName={"active"}
              />
                {prikaziformu && (
                    <div className="novaTema" id="noviTim">
                      <div className="zatvori" onClick={prikaz_forma}>✖️</div>
                      <div className="error" id="naplavomErr">{error}</div>
                      <form onSubmit={onSubmit}>
                          <div><input type="text" name="naziv" placeholder="Naziv tima" value={podaci.naziv} onChange={onChange} required/></div>
                          <button type="submit" disabled={!provjeri()}>Napravi tim</button>
                      </form>
                    </div>
                )}
            </div>
        </>
    )
}
export default Tim;



