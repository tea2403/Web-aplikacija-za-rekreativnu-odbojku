import React from "react";
import "./MojeAktivnosti.css";
import {useNavigate} from "react-router-dom";
function Aktivnosti(){
    const [slike, setSlike] = React.useState([]);
    const [igrac, setIgrac] = React.useState([]);
    const [prikaziGaleriju, setPrikaziGaleriju] = React.useState("");
    const [prikaziIgrace, setPrikaziIgrace] = React.useState("");
    const [ljudi, setLjudi] = React.useState([]);
    const navigate = useNavigate();
    React.useEffect(() => {
        const options = {
            method: "GET",
          };
        fetch("/api/igrac/termini", options)
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        const data2 = data.flatMap(x => {     
                            x.termin = JSON.parse(x.termin)
                            const timovi = x.tim.split(":::").filter(part => part !== "");
                            if (timovi == "") return x
                            return timovi.map(part => ({
                                ...x,
                                tim: part.split(";;;")[0],
                                tim_id: part.split(";;;")[1]
                            }));
                        })
                        setIgrac(data2)
                    })
                } else {
                    response.json().then((data) => {setError(data.message)})
                }
            })
    }, []);

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
    function izbrisi(index, id, tim) {
        if (!window.confirm("Jeste li sigurni da želite otkazati rezervaciju?")) return
        const body = `termin=${id}&tim=${tim}`;
        const options = {
            method: "DELETE",
            headers: {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body: body,
        };
        return fetch("/api/termin/", options)
        .then(response => {
            if (response.ok) {
                window.location.reload();
            } else {
                response.json().then((data) => {alert(data.message)})
            }
        });
    }

    const regex = /(\d{4})-(\d{2})-(\d{2}) (\d{2}:\d{2}):\d{2}/;
    const regex1 = /(\d{4}-\d{2}-\d{2}) (\d{2}:\d{2}):\d{2}/;
    const dani = ['nedjelja', 'ponedjeljak', 'utorak', 'srijeda', 'četvrtak', 'petak', 'subota'];
    const pozicije = ["Tehničar", "Srednjak", "Libero", "Korektor", "Primač"]
    var dan;
    return (
        <>
            <div className="pozadina-pocetna" style={{backgroundImage: "url('images/poz.png')", opacity: "1"}}></div>
            <div id="termini" style={{top: "9vh"}}>
            {igrac.map((termin, index) => {
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
                    {termin.tim && ( <div style={{textAlign: "center"}}><u>TIM: {termin.tim}</u></div>)}
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
                    <a style={{cursor: "pointer", color: "#213547"}} onClick={() => {prikazIgraci(termin.idTerm, index)}}>
                        <b style={{ color: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "red" : "#046d04" , 
                                    textDecoration: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "line-through" : "none"}}>
                            {parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "🔴" : "🟢"}
                            Zauzeto: {termin.termin.zauzeto} / {termin.maxbroj}
                        </b>
                    </a>
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
                    <button onClick={(e) => {izbrisi(index, termin.idTerm, termin.tim !== "" ? termin.tim_id : -1);}} id="rezerviraj">Odustani</button>
                </div></div>
                )} else return(
                    <div key={index} className="termin">
                    <p id="podebljano">({regex.exec(termin.termin.pocetak)[3]}. {regex.exec(termin.termin.pocetak)[2]}
                    . {regex.exec(termin.termin.pocetak)[1]}) <b>{regex.exec(termin.termin.pocetak)[4]} - {regex.exec(termin.termin.kraj)[4]}</b> ({regex.exec(termin.termin.kraj)[3]}
                    . {regex.exec(termin.termin.kraj)[2]}. {regex.exec(termin.termin.kraj)[1]})</p>
                    {termin.tim && ( <div style={{textAlign: "center"}}><u>TIM: {termin.tim}</u></div>)}
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
                    <a style={{cursor: "pointer", color: "#213547"}} onClick={() => {prikazIgraci(termin.idTerm, index)}}>
                        <b style={{ color: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "red" : "#046d04" , 
                                    textDecoration: parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "line-through" : "none"}}>
                            {parseInt(termin.termin.zauzeto) >= parseInt(termin.maxbroj) ? "🔴" : "🟢"}
                            Zauzeto: {termin.termin.zauzeto} / {termin.maxbroj}
                        </b>
                    </a>
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
                    <button onClick={(e) => {izbrisi(index, termin.idTerm, termin.tim !== "" ? termin.tim_id : -1);}} id="rezerviraj">Odustani</button>
                </div>
                )
            })}
            </div>
        </>
    )
}
export default Aktivnosti;