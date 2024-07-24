import React from "react";
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";
import "./Tema.css";
function Tema() {
    const [poruke, setPoruke] = React.useState([]);
    const [obavijesti, setObavijesti] = React.useState("");
    const [prikaziformu, setPrikaziformu] = React.useState(false);
    const [error, setError] = React.useState("");
    const [podaci, setPodaci] = React.useState({ komentar: ""});
    const location = useLocation();
    const path = location.pathname;
    const x = decodeURIComponent(path).split("/");
    const naslov = x[x.length - 1];
    const navigate = useNavigate();
    React.useEffect(() => {
        const regex = /(\d{4}-\d{2}-\d{2} \d{2}:\d{2}):\d{2}/;
        fetch(`/api/forum/komentari/${naslov}`)
            .then(data => data.json())
            .then(podaci => {
                const novi = [];
                for (const key in podaci) {
                    var rez = regex.exec(podaci[key].vrijeme);
                    novi.push({ autor: podaci[key].autor, vrijeme: rez[1],sadrzaj: podaci[key].tekst, id: podaci[key].id})
                }
                setPoruke(novi);
            })
        fetch(`/api/teme/obavijesti/info/${naslov}`)
            .then(data => data.json())
            .then(podaci => {
                setObavijesti(podaci);
            })
      }, []);
      

    function salji(zvono){
        if (zvono) {
            fetch(`/api/teme/obavijesti/ukljuci/${naslov}`)
        } else fetch(`/api/teme/obavijesti/iskljuci/${naslov}`)
    };

    function ukljuci_obavijesti(){
        setObavijesti(!obavijesti);
        salji(!obavijesti);
    };
    function prikaz_forma(){
        setPrikaziformu(!prikaziformu)
        setPodaci({ komentar: ""})
        setError("")
    }
    function onChange(event) {
        const {name, value} = event.target;
        setPodaci(stara => ({...stara, [name]: value}))
    }
    function provjeri(){
        return podaci.komentar.length > 0;
    }
    function izbrisi(index, id) {
        if (!window.confirm("Jeste li sigurni da želite izbrisati komentar?")) return
        const body = `naslov=${naslov}&kom=${id}`;
        const options = {
            method: "DELETE",
            headers: {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body: body,
        };
  
        return fetch("/api/forum/komentari/", options)
        .then(response => {
            if (response.ok) {
                setPoruke(stari => {
                    const novaL = [...stari];
                    novaL.splice(index, 1);
                    return novaL;
                });
                if (poruke.length == 1) navigate("/forum")
            } else {
                response.json().then((data) => {alert(data.message)})
            }
        });
    }
    function novaPoruka(e) {
        e.preventDefault();
        setPrikaziformu(false);
        setError("");
        const body = `kom=${podaci.komentar}&naslov=${naslov}`;
        const options = {
            method: "POST",
            headers: {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body: body,
        };
        fetch("/api/forum/komentari/", options)
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                    window.scrollTo(0, document.body.scrollHeight);
                }
                else {
                    response.json().then((data) => {setError(data.message)})
                }
            })
    }
    return (
        <>
            <div className="pozadina-pocetna" style={{backgroundImage: "url('/images/poz.png')", opacity: "1"}}></div>
            <div className="sveTeme">
                <h2 className="naslovteme">{naslov} <span className="zvono" style={{ marginLeft: "5px" }} onClick={ukljuci_obavijesti}>{obavijesti ? "🔔" : "🔕"}</span></h2>
                <span style={{ alignSelf: "flex-end", color: "yellow", cursor: "pointer" }} onClick={() => window.scrollTo(0, document.body.scrollHeight)}>🡫</span>
                <div className="poruke">
                    {poruke.map((poruka, index) => (
                        <div key={poruka.id}>
                            <p><b>{poruka.autor}</b> </p> <p style={{ marginBottom: "10px", fontSize: "0.7rem" }}>{poruka.vrijeme}</p>
                            <p style={{ fontSize: "1rem" }}>{poruka.sadrzaj}</p>
                            <div title="Izbriši poruku" onClick={(e) => {izbrisi(index, poruka.id);}}>&#128465;</div>
                        </div>
                    ))}
                    <button id="napisi" onClick={prikaz_forma}>Novi komentar</button>
                </div>
            </div>
            {prikaziformu && (
                <div className="novaTema">
                <form  id="poruka" onSubmit={novaPoruka}>
                    <div className="zatvori" id="iks" onClick={prikaz_forma}>✖️</div>
                    <div className='error' id="naplavomErr" style={{ height: "10%" }}>{error}</div>
                    <span>Vaš komentar:</span>
                    <div><textarea id="naslov"name="komentar" value={podaci.komentar} onChange={onChange} maxLength={255} required/></div>
                    <button type="submit" disabled={!provjeri()}>Objavi</button>
                </form>
                </div>
            )}
        </>
    )
}
export default Tema;