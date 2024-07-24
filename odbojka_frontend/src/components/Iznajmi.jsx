import React from "react";
import Termin from "./Termin.jsx";
import "./Iznajmi.css";
import {useNavigate} from "react-router-dom";
function Iznajmi() {
    const [error, setError] = React.useState('');
    const [prikaziformu, setPrikaziformu] = React.useState({ prikaz: false, id: "-1"});
    const [podaci, setPodaci] = React.useState({ adresa: "", pbr: "", maxbroj: "", rekviziti : false, napomena: ""});
    const [mjesta, setMjesta] = React.useState([]);
    const [termini, setTermini] = React.useState([]);
    const [slike, setSlike] = React.useState([]);
    const [slike2, setSlike2] = React.useState([]);
    const [tereni, setTereni] = React.useState([]);
    const [prikaziIgrace, setPrikaziIgrace] = React.useState("");
    const [ljudi, setLjudi] = React.useState([]);
    const [prikaziGaleriju, setPrikaziGaleriju] = React.useState("");
    const navigate = useNavigate();
    React.useEffect(() => {
        fetch("/api/mjesta/")
            .then(data => data.json())
            .then(novo => {
                setMjesta(novo);
                fetch("/api/teren/")
                    .then(response => response.json())
                    .then(data => {
                        setTereni(data)
                    })
            })
        .catch(error => console.error("Greška:", error));
      }, []);
    function prikaz_forma(id){
        setPrikaziformu({ prikaz: !prikaziformu.prikaz, id: id})
        setSlike([])
        setError("")
    }
    function onSubmit(e) { 
        e.preventDefault();
        setError("");
        const formData = new FormData();
        formData.append("adresa", podaci.adresa);
        formData.append("pbr", podaci.pbr);
        formData.append("broj_ljudi", podaci.maxbroj);
        formData.append("rekviziti", podaci.rekviziti);
        formData.append("napomena", podaci.napomena);
        for (let i = 0; i < slike.length; i++) {
            formData.append("slike", slike[i]);
        }
        if (slike.length == 0) formData.append("slike", "");
        formData.append("termini", JSON.stringify(termini.map(({ zauzeto, ...ostali }) => ostali)));
        const options = {
            method: "POST",
            body: formData
        };
        return fetch(`/api/teren/${prikaziformu.id}`, options)
        .then(response => {
            if (response.ok) {
                alert("Svi valjani termini su zabilježeni (preklapanja nisu moguća).")
                setPrikaziformu({ prikaz: false, id: "-1"});
                window.location.reload();          
            } else {
                response.json().then((data) => {setError(data.message)})
            }
        });
    }
    function onChange(event) {
        var {name, value} = event.target;
        if (name == "rekviziti") {
            setPodaci(stara => ({...stara, [name]: event.target.checked.toString()}))
        }
        else setPodaci(stara => ({...stara, [name]: value}))
    }
    function provjeri(){
        return podaci.adresa.length > 0 && podaci.maxbroj.length > 0 && podaci.pbr.length > 0;
    }
    function dodajTermin(novi) {
        setTermini(novi);
    };
    function uredi(id){
        const teren = tereni.find(teren => teren.id === id);
        setPodaci({ adresa: teren.adresa, pbr: teren.pbr, maxbroj: teren.maxbroj, rekviziti : teren.rekviziti, napomena: teren.napomena})
        let ter = []
        {teren.termini.split(";;;").map((termin, index) => {
            if (termin != ""){
                const obj = JSON.parse(termin);
                obj.pocetak = obj.pocetak.replace(" ", "T").slice(0, -5)
                obj.kraj = obj.kraj.replace(" ", "T").slice(0, -5)
                ter.push(obj)             
            }
        })}
        setTermini(ter);
        setPrikaziformu({ prikaz: true, id: id})
        window.scrollTo(0, 0);
    }
    function brisi(){
        setPodaci({ adresa: "", pbr: "", maxbroj: "", rekviziti : false, napomena: ""});
        setTermini([])
    }
    function izbrisiSliku(indexT, indexS, id){
        setError("");
        const options = {
          method: "POST",
        };
        return fetch(`/api/teren/izbrisi/slika/${id}`, options)
        .then(response => {
            if (response.ok) {   
                if (slike2.length == 1){
                    const list = [...tereni];
                    list.splice(indexT, 1);
                    setTereni(list);
                }  
                setSlike2(stari => {
                    const novaL = [...stari];
                    novaL.splice(indexS, 1);
                    return novaL;
                });  
            } else {
                response.json().then((data) => {alert(data.message)})
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
                    setPrikaziIgrace(id)
                } else {
                    response.json().then((data) => {setError(data.message)})
                }
            });
    }
    function prikazGalerija(id, index){
        const options = {
            method: "GET",
        };
        fetch(`/api/teren/slike/brisi/${id}`, options)
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        setSlike2(data)
                    })
                    setPrikaziGaleriju(index)
                } else {
                response.json().then((data) => {setError(data.message)})
                }
            });
    }
    const pozicije = ["Tehničar", "Srednjak", "Libero", "Korektor", "Primač"]
    return (
        <>
            <div className="pozadina-pocetna" style={{backgroundImage: "url('images/poz.png')", opacity: "1"}}></div>
            <div className="sviTimovi" >
                <button id="zapocni" onClick={() => {brisi(); prikaz_forma("-1")}}>Iznajmi teren</button>
                <h2 className="mojiTimovi">Moji tereni:</h2>
                <div id="timovi">            
                    {tereni.map((teren, indexT) => (
                        <div key={teren.id} className="tim" id="cijeliTeren">
                            <h2>Teren {teren.id}</h2>
                            <p>Adresa: {teren.adresa}</p>
                            <p>Mjesto: {mjesta.find((mj) => mj.pbr == teren.pbr).naziv_mjesta}, {teren.pbr}</p>
                            <p>Iznajmljivač: {teren.iznajmljivac}</p>
                            <p>Kontakt: {teren.mob}</p>
                            <p>Maksimalan broj igrača: {teren.maxbroj}</p>
                            <p>Rekviziti: {teren.rekviziti === "true" ? "Da" : "Ne"}</p>
                            <p>Napomena: {teren.napomena}</p>
                            <p><a style={{cursor: "pointer"}} onClick={() => {prikazGalerija(teren.id, indexT)}}>Galerija📸</a></p>
                            {prikaziGaleriju === indexT && (
                                <div className="foto">
                                    {slike2.map((slika, indexS) => {
                                        if (slika != ""){
                                            return (
                                                <div key={slika.id} className="slika-container">
                                                    <img src={slika.slika} alt="slika"></img>
                                                    <button className="makniKlub izbrisiSliku" onClick={() => izbrisiSliku(indexT, indexS, slika.id)}>✖️</button>
                                                </div>
                                            );
                                        }
                                    })}
                                </div>
                            )}
                            <p>Termini:</p>
                            {teren.termini.split(";;;").map((termin, index) => {
                                if (termin != ""){
                                    const obj = JSON.parse(termin);
                                    const regex = /(\d{4}-\d{2}-\d{2} \d{2}:\d{2}):\d{2}/;
                                    return(
                                        <div key={index} className="jedanTermin">
                                            {prikaziIgrace === obj.id && (
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
                                            <p>Od: {regex.exec(obj.pocetak)[1]}</p>
                                            <p>Do: {regex.exec(obj.kraj)[1]}</p>
                                            <p>Cijena: {obj.cijena}€</p>
                                            <p><a style={{cursor: "pointer"}} onClick={() => navigate("/termin/" + obj.id)}>Forum💬</a></p>
                                            <p style={{cursor: "pointer", color: "#213547"}} onClick={() => {prikazIgraci(obj.id, index)}}>
                                                <b style={{ color: parseInt(obj.zauzeto) >= parseInt(teren.maxbroj) ? "red" : "#046d04" , 
                                                            textDecoration: parseInt(obj.zauzeto) >= parseInt(teren.maxbroj) ? "line-through" : "none"}}>
                                                    {parseInt(obj.zauzeto) >= parseInt(teren.maxbroj) ? "🔴" : "🟢"}
                                                    Zauzeto: {obj.zauzeto} / {teren.maxbroj}
                                                </b>
                                            </p>
                                        </div>)
                                }
                            })}
                            <button onClick={() => uredi(teren.id)} id="urediTeren">Uredi</button>
                        </div>
                    ))} 
                </div>
            </div>
            {prikaziformu.prikaz && (
                <div className="novaTema" id="teren">
                    <div className="zatvori" onClick={() => {prikaz_forma("-1-")}}>✖️</div>
                    <div className="error" id="naplavomErr">{error}</div>
                    <form onSubmit={onSubmit} id="noviTeren">
                        <div><input type="text" name="adresa" placeholder="Adresa" value={podaci.adresa} onChange={onChange} required/></div>
                        <label>Mjesto:</label>
                        <select value={podaci.pbr} name="pbr" onChange={onChange} required>
                            <option key="0" value={"0"}>-------------</option>   
                            {mjesta.map(mjesto1 => (
                                <option key={mjesto1.pbr} value={mjesto1.pbr}>{mjesto1.naziv_mjesta}, {mjesto1.pbr}</option>
                            ))}
                        </select>
                        <div><input placeholder="Maksimalan broj igrača" onChange={onChange} type="number" name="maxbroj" value={podaci.maxbroj} required min="1"/></div>
                        <span><input checked={podaci.rekviziti == "true"} onChange={onChange} type="checkbox" name="rekviziti"/>Dostupni rekviziti</span> <br/>
                        <label>Fotografije terena (barem jedna):</label>
                        <input onChange={(event) => setSlike(Array.from(event.target.files))} type="file" name="slike" multiple accept=".jpg, .jpeg, .png"></input>
                        <br/><label>Napomena (nije obavezno):</label>
                        <div><input type="text" name="napomena" value={podaci.napomena} onChange={onChange}/></div>
                        <br/><Termin dodajTermin={dodajTermin} termini={termini} setTermini={setTermini}/>    
                        <button type="submit" disabled={!provjeri()}>Iznajmi</button>
                    </form>
                </div>
            )}
        </>
    )

}
export default Iznajmi;