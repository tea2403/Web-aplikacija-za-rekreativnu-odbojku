import React from "react";
import {Link} from "react-router-dom"
import "./Pocetna.css";
function Pocetna (){
    return (
    <>
        <div className="pozadina-pocetna"></div>
        <div className="pocetna">
            <img src="images\mreza1.png" alt="mreza" className="mreza"></img>
            <div className="naslov">Dobrodošli!</div>
            <h2>Izradite račun te sudjelujte u rekreativnim odbojkaškim aktivnostima.<br />
                Možete sudjelovati kao <u>igrač</u>, ali i kao <u>iznajmljivač terena</u>!</h2>
        </div>
        <div className="prijava-reg">
            <div id="linija">Već imam račun! <br />
                    <Link to="/prijava"> Prijavite se!</Link>
                    <img src="images\lopta.png" alt="lopta" className="lopta"></img>
            </div>
            <div>Trebam izraditi račun. <br />
                    <Link to="/registracija"> Registrirajte se!</Link>
            </div>
        </div>
    </>
    )
}
export default Pocetna;