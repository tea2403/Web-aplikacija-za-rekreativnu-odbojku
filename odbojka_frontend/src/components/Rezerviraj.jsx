import React from "react";
import "./Rezerviraj.css";
import Rezervacija from "./Rezervacija.jsx";
import {useNavigate} from "react-router-dom";
function Rezerviraj() {
    const [termini, setTermini] = React.useState([]);
    const [mjesta, setMjesta] = React.useState([]);
    const [slike, setSlike] = React.useState([]);
    const [error, setError] = React.useState("");
    const [prikaziGaleriju, setPrikaziGaleriju] = React.useState("");
    const [prikaziformu, setPrikaziformu] = React.useState({ prikaz: false, id: "-1"});
    const [prikaziIgrace, setPrikaziIgrace] = React.useState("");
    const [ljudi, setLjudi] = React.useState([]);
    const navigate = useNavigate();
    const [pbr, setPbr] = React.useState("");
    React.useEffect(() => {
        setPbr(sessionStorage.getItem("pbr") ||"")
        if (sessionStorage.getItem("pbr")) prikazMjesto({target: {value: sessionStorage.getItem("pbr")}});
        fetch("/api/mjesta/")
            .then(data => data.json())
            .then(novo => {setMjesta(novo);})
      }, []);

    function prikazMjesto(event) {
        setPbr(event.target.value)
        sessionStorage.setItem("pbr", event.target.value);
        const options = {
          method: "GET",
        };
        return fetch(`/api/termin/${event.target.value}`, options)
        .then(response => {
            if (response.ok) {
                response.json().then(data => {
                    data.map(x => {     
                    x.termin = JSON.parse(x.termin)})
                    setTermini(data)
                })
            } else {
                response.json().then((data) => {setError(data.message)})
            }
        });
    }
    function prikazGalerija(id, index){
        const options = {
            method: "GET",
        };
        fetch(`/api/teren/slike/${id}`, options)
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        setSlike(data)
                    })
                    setPrikaziGaleriju(index)
                } else {
                    response.json().then((data) => {setError(data.message)})
                }
             });
    }
    function prikazIgraci(id, index){
        const options = {
            method: "GET",
        };
        fetch(`/api/termin/igraci/${id}`, options)
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        setLjudi(data)
                    })
                    setPrikaziIgrace(index)
                } else {
                    response.json().then((data) => {setError(data.message)})
                }
            });
    }   
    const regex = /(\d{4})-(\d{2})-(\d{2}) (\d{2}:\d{2}):\d{2}/;
    const regex1 = /(\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}):\d{2}/;
    const dani = ["nedjelja", "ponedjeljak", "utorak", "srijeda", "četvrtak", "petak", "subota"];
    var dan;
    const pozicije = ["Tehničar", "Srednjak", "Libero", "Korektor", "Primač"]
    return (
        <>
        <div className="pozadina-pocetna" style={{backgroundImage: "url('/images/poz.png')", opacity: "1"}}></div>
        <select id="izborMjesto" onChange={prikazMjesto} value={pbr}>
            <option value="">Odaberite mjesto za koje želite vidjeti termine</option>
            {mjesta.map((mjesto1, index) => (
                <option key={mjesto1.pbr} value={mjesto1.pbr}>{mjesto1.naziv_mjesta}, {mjesto1.pbr}</option>
            ))}
        </select>
        <div id="termini">
        {termini.map((termin, index) => {
            if (index == 0 || dan != regex1.exec(termin.termin.pocetak)[1]){
                dan = regex1.exec(termin.termin.pocetak)[1];
                const datum = new Date(dan);
                const danIndex = datum.getDay();
                const tjedan = dani[danIndex];
            return(
            <div key={index}>
            <h2 className="dan">{regex.exec(termin.termin.pocetak)[3]}. {regex.exec(termin.termin.pocetak)[2]}. {regex.exec(termin.termin.pocetak)[1]}., {tjedan}</h2>
            <hr style={{ margin: "10px 0" }} />
            <div className="termin">
            <p id="podebljano">({regex.exec(termin.termin.pocetak)[3]}. {regex.exec(termin.termin.pocetak)[2]}
                . {regex.exec(termin.termin.pocetak)[1]}) <b>{regex.exec(termin.termin.pocetak)[4]} - {regex.exec(termin.termin.kraj)[4]}</b> ({regex.exec(termin.termin.kraj)[3]}
                . {regex.exec(termin.termin.kraj)[2]}. {regex.exec(termin.termin.kraj)[1]})</p>
                <p><u>Termin: {termin.idTerm}</u></p> 
                <p><b>Adresa:</b> {termin.adresa}</p> 
                <p><b>Mjesto:</b> {termin.mjesto}, {termin.pbr}</p>
                <p><b>Iznajmljivač:</b> {termin.iznajmljivac}</p>
                <p><b>Kontakt:</b> {termin.mob}</p>
                <p><b>Rekviziti:</b> {termin.rekviziti === "true" ? "Da" : "Ne"}</p>
                <p><b>Napomena:</b> {termin.napomena}</p>
                <p><b>Cijena:</b> {termin.termin.cijena}€</p>
                <a style={{cursor: "pointer"}} onClick={() => {prikazGalerija(termin.id, index)}}>Galerija📸</a>
                {prikaziGaleriju === index && (
                    <div className="cijelaG">
                        <div className="zatvori" onClick={() => {setPrikaziGaleriju(""); setSlike([]);}}>✖️</div>
                        <div className="galerija">
                            {slike.map((slika, index) => (
                                <img key={index} src={slika} alt="slika"></img>
                            ))}
                        </div>
                    </div>
                )}
                <a style={{cursor: "pointer"}} onClick={() => navigate("/termin/" + termin.idTerm)}>Forum💬</a>
                <p style={{cursor: "pointer", color: "#213547"}} onClick={() => {prikazIgraci(termin.idTerm, index)}}>
                    <b style={{ color: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "red" : "#046d04" , 
                                textDecoration: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "line-through" : "none"}}>
                        {parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "🔴" : "🟢"}
                        Zauzeto: {termin.termin.zauzeto} / {termin.maxbroj}
                    </b>
                </p>
                {prikaziIgrace === index && (
                    <div className="tablica">
                        <div className="zatvori" onClick={() => {setPrikaziIgrace(""); setLjudi([]);}}>✖️</div>
                        <div className="galerija" id="scrol">
                        <table>
                        <thead>
                            <tr>
                                <th></th>
                                {pozicije.map((poz, index) => (
                                    <th key={index}>{poz}</th>
                                ))}
                            </tr>
                        </thead>
                        <tbody>
                            {ljudi.map((osoba, index) => (
                                <tr key={index}>
                                    <td style={{cursor: "pointer"}} onClick={() => navigate(`/igrac/${osoba.email}`)}>{osoba.email}</td>
                                    {pozicije.map((poz, idx) => (
                                        <td key={idx}>
                                            {osoba.pozicije.split(" ").includes(poz) ? "⭐" : ""}
                                        </td>
                                    ))}
                                </tr>
                            ))}
                        </tbody>
                        </table> 
                        </div>
                    </div>
                )}
                <button onClick={() => setPrikaziformu({ prikaz: !prikaziformu.prikaz, id: termin.idTerm})} id="rezerviraj">Rezerviraj</button>
            </div></div>
            )} else return(
                <div key={index} className="termin">
                <p id="podebljano">({regex.exec(termin.termin.pocetak)[3]}. {regex.exec(termin.termin.pocetak)[2]}
                . {regex.exec(termin.termin.pocetak)[1]}) <b>{regex.exec(termin.termin.pocetak)[4]} - {regex.exec(termin.termin.kraj)[4]}</b> ({regex.exec(termin.termin.kraj)[3]}
                . {regex.exec(termin.termin.kraj)[2]}. {regex.exec(termin.termin.kraj)[1]})</p>
                <p><u>Termin: {termin.idTerm}</u></p> 
                <p><b>Adresa:</b> {termin.adresa}</p> 
                <p><b>Mjesto:</b> {termin.mjesto}, {termin.pbr}</p>
                <p><b>Iznajmljivač:</b> {termin.iznajmljivac}</p>
                <p><b>Kontakt:</b> {termin.mob}</p>
                <p><b>Rekviziti:</b> {termin.rekviziti === "true" ? "Da" : "Ne"}</p>
                <p><b>Napomena:</b> {termin.napomena}</p>
                <p><b>Cijena:</b> {termin.termin.cijena}€</p>
                <a style={{cursor: "pointer"}} onClick={() => {prikazGalerija(termin.id, index)}}>Galerija📸</a>
                {prikaziGaleriju === index && (
                    <div className="cijelaG">
                        <div className="zatvori" onClick={() => {setPrikaziGaleriju(""); setSlike([]);}}>✖️</div>
                        <div className="galerija">
                            {slike.map((slika, index) => (
                                <img key={index} src={slika} alt="slika"></img>
                            ))}
                        </div>
                    </div>
                )}
                <a style={{cursor: "pointer"}} onClick={() => navigate("/termin/" + termin.idTerm)}>Forum💬</a>
                <p style={{cursor: "pointer", color: "#213547"}} onClick={() => {prikazIgraci(termin.idTerm, index)}}>
                    <b style={{ color: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "red" : "#046d04" , 
                                textDecoration: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "line-through" : "none"}}>
                        {parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "🔴" : "🟢"}
                        Zauzeto: {termin.termin.zauzeto} / {termin.maxbroj}
                    </b>
                </p>
                {prikaziIgrace === index && (
                    <div className="tablica">
                        <div className="zatvori" onClick={() => {setPrikaziIgrace(""); setLjudi([]);}}>✖️</div>
                        <div className="galerija" id="scrol">
                        <table>
                        <thead>
                            <tr>
                                <th></th>
                                {pozicije.map((poz, index) => (
                                    <th key={index}>{poz}</th>
                                ))}
                            </tr>
                        </thead>
                        <tbody>
                            {ljudi.map((osoba, index) => (
                                <tr key={index}>
                                    <td style={{cursor: "pointer"}} onClick={() => navigate(`/igrac/${osoba.email}`)}>{osoba.email}</td>
                                    {pozicije.map((poz, idx) => (
                                        <td key={idx}>
                                            {osoba.pozicije.split(" ").includes(poz) ? "⭐" : ""}
                                        </td>
                                    ))}
                                </tr>
                            ))}
                        </tbody>
                        </table> 
                        </div>
                    </div>
                )}
                <button onClick={() => setPrikaziformu({ prikaz: !prikaziformu.prikaz, id: termin.idTerm})} id="rezerviraj">Rezerviraj</button>
            </div>
            )
        })}
        {prikaziformu.prikaz && (<Rezervacija prikaziformu={prikaziformu} setPrikaziformu={setPrikaziformu}/>)}</div>
        </>
    )
}
export default Rezerviraj;