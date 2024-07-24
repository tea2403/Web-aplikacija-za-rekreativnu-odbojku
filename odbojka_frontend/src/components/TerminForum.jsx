import React from "react";
import {useLocation} from "react-router-dom";

function TerminForum() {
    const [poruke, setPoruke] = React.useState([]);
    const [error, setError] = React.useState('');
    const [prikaziformu, setPrikaziformu] = React.useState(false);
    const [podaci, setPodaci] = React.useState({ komentar: ""});
    const [naslov, setNaslov] = React.useState('');
    const location = useLocation();
    const path = location.pathname;
    const x = decodeURIComponent(path).split("/");
    const id = x[x.length - 1];
    React.useEffect(() => {
        const regex = /(\d{4}-\d{2}-\d{2} \d{2}:\d{2}):\d{2}/;
        const regex2 = /(\d{4})-(\d{2})-(\d{2}) (\d{2}:\d{2}):\d{2}/;
        fetch(`/api/termin/komentari/${id}`)
            .then(data => data.json())
            .then(podaci => {
                setNaslov(regex2.exec(podaci[0].naslov)[3] + ". " + regex2.exec(podaci[0].naslov)[2] + ". " +  regex2.exec(podaci[0].naslov)[1] + ". " + regex2.exec(podaci[0].naslov)[4])
                const novi = [];
                for (let i = 1; i < podaci.length; i++) {
                    let key = i;
                    var rez = regex.exec(podaci[key].vrijeme);
                    novi.push({ autor: podaci[key].autor, vrijeme: rez[1],sadrzaj: podaci[key].tekst, id: podaci[key].id})
                }
                setPoruke(novi);
            })
    }, []);
    function prikaz_forma(){
        setPrikaziformu(!prikaziformu)
        setError("")
    }
    function onChange(event) {
        const {name, value} = event.target;
        setPodaci(stara => ({...stara, [name]: value}))
    }
    function provjeri(){
        return podaci.komentar.length > 0;
    }
    function novaPoruka(e) {
        e.preventDefault();
        setPrikaziformu(false);
        setError("");
        const body = `kom=${podaci.komentar}&termin=${id}`;
        const options = {
            method: "POST",
            headers: {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body: body,
        };
        fetch("/api/termin/komentari/", options)
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
    function izbrisi(index, id2) {
        if (!window.confirm("Jeste li sigurni da želite izbrisati komentar?")) return
        const body = `termin=${id}&kom=${id2}`;
        const options = {
            method: "DELETE",
            headers: {
                "Content-Type" : "application/x-www-form-urlencoded"
            },
            body: body,
        };
  
        return fetch("/api/termin/komentari/", options)
        .then(response => {
            if (response.ok) {
                setPoruke(stari => {
                    const novaL = [...stari];
                    novaL.splice(index, 1);
                    return novaL;
                });
            } else {
                response.json().then((data) => {alert(data.message)})
            }
        });
    }
    return(
        <>
            <div className="pozadina-pocetna" style={{backgroundImage: "url('/images/poz.png')", opacity: "1"}}></div>
            <div className="sveTeme">
                <h2 className="naslovteme">{naslov}</h2>
                <span style={{ alignSelf: "flex-end", color: "yellow", cursor: "pointer" }} onClick={() => window.scrollTo(0, document.body.scrollHeight)}>🡫</span>
                <div className="poruke">
                    {poruke.map((poruka, index) => (
                        <div key={poruka.id}>
                            <p><b>{poruka.autor}</b> </p> <p style={{ marginBottom: "10px", fontSize: "0.7rem" }}>{poruka.vrijeme}</p>
                            <p style={{ fontSize: "1rem" }}>{poruka.sadrzaj}</p>
                            <div title="Izbriši poruku" onClick={(e) => {izbrisi(index, poruka.id);}}>&#128465;</div>
                        </div>
                    ))}
                    <button id="napisi" onClick={prikaz_forma}>Nova poruka</button>
                </div>
            </div>
            {prikaziformu && (
                <div className="novaTema">
                    <form  id="poruka" onSubmit={novaPoruka}>
                        <div className="zatvori" id="iks" onClick={prikaz_forma}>✖️</div>
                        <div className="error" id="naplavomErr" style={{ height: "10%" }}>{error}</div>
                        <span>Vaš komentar:</span>
                        <div><textarea id="naslov"name="komentar" value={podaci.komentar} onChange={onChange} required/></div>
                        <button type="submit" disabled={!provjeri()}>Objavi</button>
                    </form>
                </div>
            )}
        </>
    )
}
export default TerminForum;