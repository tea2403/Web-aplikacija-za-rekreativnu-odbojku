import React from "react";
import "./Forum.css";
import {useNavigate} from "react-router-dom";
import ReactPaginate from 'react-paginate';
function Forum() {
    const [prikaziformu, setPrikaziformu] = React.useState(false);
    const [podaci, setPodaci] = React.useState({ naslov: "", komentar: ""});
    const [teme, setTeme] = React.useState([]);
    const [error, setError] = React.useState("");
    const navigate = useNavigate();
    const [trenutna, setTrenutna] = React.useState(0);

    React.useEffect(() => {
      fetch("/api/teme/")
        .then(data => data.json())
        .then(podaci => {
          const novi = [];
          for (const key in podaci) {
            novi.push({ naslov: podaci[key].naslov, komentar: podaci[key].komentar})
          }
          setTeme(novi);
        })
    }, []);

    const offset = trenutna * 5;
    const prikazane = teme.slice(offset, offset + 5);

    function handlePageClick(data) {
      setTrenutna(data.selected);
    };

    function onChange(event) {
      const {name, value} = event.target;
      setPodaci(stara => ({...stara, [name]: value}))
    }
    function prikaz_forma(){
      setPrikaziformu(!prikaziformu)
      setPodaci({ naslov: "", komentar: ""})
      setError("")
    }
    function provjeri(){
      return podaci.naslov.length > 0 && podaci.komentar.length > 0;
    }
    function izbrisi(index) {
      if (!window.confirm("Jeste li sigurni da želite izbrisati ovu temu?")) return
      const body = `naslov=${teme[index].naslov}`;
      const options = {
        method: "DELETE",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: body,
      };
      
      return fetch("/api/teme/", options)
        .then(response => {
          if (response.ok) {
              setPrikaziformu(false);
              setTeme(stari => {
                  const novaL = [...stari];
                  novaL.splice(index, 1);
                  return novaL;
              });
          } else {
              response.json().then((data) => {alert(data.message)})
          }
        });
    }

    function onSubmit(e) { 
      e.preventDefault();
      setError("");
      const body = `naslov=${podaci.naslov}`;
      const options = {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: body,
      };
      
      return fetch("/api/teme/", options)
        .then(response => {
          if (response.ok) {
            const body = `kom=${podaci.komentar}&naslov=${podaci.naslov}`;
            const options = {
              method: "POST",
              headers: {
                "Content-Type": "application/x-www-form-urlencoded"
              },
              body: body,
            };
            fetch("/api/forum/komentari/", options)
              .then(response => {
                if (response.ok) {
                  setPrikaziformu(false);
                  setTeme(stari => [
                    { naslov: podaci.naslov, komentar: podaci.komentar }, ...stari
                  ]);
                }
              })
          } else {
            response.json().then((data) => {setError(data.message)})
          }
        });
    }
    return (
    <>
      <div className="pozadina-pocetna" style={{backgroundImage: "url('/images/poz.png')", opacity: "1"}}></div>
      <div className="sveTeme" >
        <button id="zapocni" onClick={prikaz_forma}>Započni temu</button>
        {prikaziformu && (
          <div className="novaTema">
            <div className="zatvori" onClick={prikaz_forma}>✖️</div>
            <div className="error" id="naplavomErr">{error}</div>
            <form onSubmit={onSubmit}>
              <div><input type="text" name="naslov" placeholder="Naslov teme" value={podaci.naslov} onChange={onChange} required/></div>
              <span>Početni komentar:</span>
              <div><textarea id="naslov"name="komentar" value={podaci.komentar} onChange={onChange} maxLength={255} required/></div>
              <button type="submit" disabled={!provjeri()}>Objavi</button>
            </form>
          </div>
        )}
        <div>
          <div id="teme">
            {prikazane.map((tema, index) => (
              <div className="tema" key={offset + index} onClick={() => navigate(`/forum/tema/${tema.naslov}`)}>
                <div style={{ fontSize: "1.2rem" }}><b>{tema.naslov}</b></div>
                <p style={{ margin: "0px" }}>{tema.komentar}</p>
                <button onClick={(e) => {e.stopPropagation(); izbrisi(offset + index);}}>Izbriši</button>
              </div>
            ))}
          </div>
          <ReactPaginate
            previousLabel={"Prethodna"}
            nextLabel={"Sljedeća"}
            breakLabel={"..."}
            breakClassName={"break-me"}
            pageCount={Math.ceil(teme.length / 5)}
            marginPagesDisplayed={2}
            pageRangeDisplayed={5}
            onPageChange={handlePageClick}
            containerClassName={"pagination"}
            activeClassName={"active"}
          />
        </div> 
      </div> 	
    </>
    
    )
}
export default Forum;