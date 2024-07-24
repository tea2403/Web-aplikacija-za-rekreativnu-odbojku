import React from "react";
import "./IgracInfo.css";
import {useLocation} from "react-router-dom";
function IgracInfo() {
    const [igracInfo, setIgracInfo] = React.useState({ spol: "", datum: "", rodenje: ""});
    const [pozicije, setPozicije] = React.useState([]);
    const [klubovi, setKlubovi] = React.useState([{ klub: "", od: "", do: ""}]);
    const [podaci, setPodaci] = React.useState({ ime: "", prezime: "", broj: "", profilna: null, pbr: "", grad: ""});
    const location = useLocation();
    const path = location.pathname;
    const x = decodeURIComponent(path).split("/");
    const igrac = x[x.length - 1];
    React.useEffect(() => {
        fetch(`/api/igrac/${igrac}`)
            .then(data => data.json())
            .then(podaci => {
                for (const key in podaci) {
                    if (podaci[key] == null) podaci[key] = ""
                }
                setIgracInfo(podaci);
            })
        fetch(`/api/pozicije/${igrac}`)
            .then(data => data.json())
            .then(podaci => {
                setPozicije(podaci);
            })
        fetch(`/api/trenirao/${igrac}`)
            .then(data => data.json())
            .then(podaci => {
                const novi = podaci.map(item => ({
                    klub: item.klub !== null ? item.klub : "",
                    od: item.od !== null ? item.od : "",
                    do: item.do !== null ? item.do : ""
                }));
                const sortirani = novi.sort((a, b) => {
                    if (!a.od && !b.od) return ;
                    if (!a.od) return -1;
                    if (!b.od) return 1;
                    return new Date(a.od) - new Date(b.od);
                });
                setKlubovi(sortirani); 
            })
        fetch(`/api/osoba/${igrac}`)
            .then(data => data.json())
            .then(podaci => {
                const novi = {};
                for (const key in podaci) {
                    novi[key] = podaci[key] == null ? "" : podaci[key];
                }
                if (novi["pbr"] != "") novi["pbr"] = ", " + novi["pbr"]
                setPodaci(novi);
            })
      }, []);
    function datum(string) {
        if (string == "") return ""
        const [god, mj, dan] = string.split("-");
        return `${dan}. ${mj}. ${god}`;
    }
    return (
        <>
        <div className="pozadina-pocetna" style={{backgroundImage: "url('/images/poz.png')", opacity: "1"}}></div>
        <div className="sviPodaci">
            <div>
                <img className="profilna2" src={podaci.profilna ? podaci.profilna : "/images/profil.png"} alt="profil"></img>
            </div>
            <div id="podaci">
                <h2>Moji podaci:</h2>
                <p><b>Ime:</b> {podaci.ime}</p>
                <p><b>Prezime:</b> {podaci.prezime}</p>
                <p><b>Broj telefona:</b> {podaci.broj}</p>
                <p><b>E-mail:</b> {igrac}</p>
                <p><b>Mjesto stanovanja:</b> {podaci.grad}{podaci.pbr}</p>
                <p><b>Spol:</b> {igracInfo.spol}</p>
                <p><b>Datum rođenja:</b> {datum(igracInfo.rodenje)}</p>
                <p><b>Početak igranja odbojke:</b> {igracInfo.iskustvo}{igracInfo.iskustvo ? "." : ""}</p>
                <h2>Pozicije koje mogu igrati:</h2>
                <ul>
                    {pozicije.map((pozicija, index) => (
                        <li key={index}>{pozicija}</li>
                    ))}
                </ul>
                <h2>Klubovi:</h2>
                <ul>
                    {klubovi.map((klub, index) => (
                        <li key={index}>{klub.klub} ({klub.od} - {klub.do})</li>
                    ))}
                </ul>
            </div>
        </div>
        </>
    )
}
export default IgracInfo;
