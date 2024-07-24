import React from "react";
import "react-datepicker/dist/react-datepicker.css";
import "./Termin.css";
function Termin(props){
    const [pocetak, setPocetak] = React.useState("");
    const [kraj, setKraj] = React.useState("");
    const [cijena, setCijena] = React.useState("");

    function dodaj(){
        const novi = {
            pocetak: pocetak,
            kraj: kraj,
            cijena: cijena,
        };
        props.setTermini(stari => [...stari, novi]);
        setPocetak("");
        setKraj("");
        setCijena(0);
    };

    function onChange(event, index) {
        const {name, value} = event.target;
        const novi = [...props.termini];
        novi[index][name] = value;
        props.dodajTermin(novi);
    }

    function izbrisiTermin(index){
        const list = [...props.termini];
        list.splice(index, 1);
        props.setTermini(list);
    }
    
    return(
        <>     
            {props.termini.map((termin, index) => (
                <span key={index} className="okvir klubovi">
                    <div className="klub">
                        <div style={{padding: "5px"}}>
                            <label>Početak termina:</label><br/>
                            <input type="datetime-local" name="pocetak" value={termin.pocetak} onChange={(event) => onChange(event, index)} required />
                        </div>
                        <div className="sati">
                            <label>Kraj termina:</label><br />
                            <input type="datetime-local" name="kraj" value={termin.kraj} onChange={(event) => onChange(event, index)} required/>
                        </div>
                        <div className="cijena">
                            <label>Cijena terena (€):</label>
                            <input type="number" name="cijena" value={termin.cijena} onChange={(event) => onChange(event, index)} required/>
                        </div>
                    </div>
                    <button className="makniKlub" type="button" onClick={() => izbrisiTermin(index)}><b>-</b></button>                  
                </span>
            ))}
            <button id="dodajTermin" className="dodajKlub" type="button" onClick={dodaj}>+</button> 
        </>               
    )
}
export default Termin;