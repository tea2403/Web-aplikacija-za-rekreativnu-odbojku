import React from "react";
function Rezervacija(props) {
    const [timovi, setTimovi] = React.useState([]);
    const [error, setError] = React.useState("");
    const [izbor, setIzbor] = React.useState("");
    React.useEffect(() => {
        fetch("/api/tim/kapetan")
            .then(data => data.json())
            .then(podaci => {
                const novi = [];
                for (const key in podaci) {
                    novi.push({naziv: podaci[key].naziv, id: podaci[key].id})
                }
                setTimovi(novi);
            })
      }, []);
    function provjeri(){
        return izbor !== "";
    }
    function rezerviraj(e){
        e.preventDefault();
        setError("");
        const body = `termin=${props.prikaziformu.id}&tim=${izbor}`;
        const options = {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: body,
        };
        fetch("/api/tim/rezerviraj", options)
            .then(response => {
                if (response.ok) {
                    setError("Rezervacija uspješno zabilježena.")
                }
                else {
                    response.json().then((data) => {setError(data.message)})
                }
            })
    }
    return (
        <>
            <div className="novaTema">
                <form  id="poruka" onSubmit={rezerviraj}>
                    <div className="zatvori" id="iks" onClick={() => props.setPrikaziformu({ prikaz: !props.prikaziformu.prikaz, id: props.prikaziformu.id})}>✖️</div>
                    <div className="error" id="naplavomErr" style={{ height: "10%" }}>{error}</div>
                    <span>Za termin možete predbilježiti sebe ili cijeli svoj tim.</span>
                    <select id="izborTima" onChange={(event) => setIzbor(event.target.value)}>
                        <option value="">Odaberite</option>
                        <option value="-1">Zabilježi samo mene</option>
                        {timovi.map((tim, index) => (
                            <option key={tim.id} value={tim.id}>{tim.naziv}</option>
                        ))}
                    </select>
                    <button type="submit" disabled={!provjeri()}>Rezerviraj</button>
                </form>
            </div>
        </>
    );
}
export default Rezervacija;