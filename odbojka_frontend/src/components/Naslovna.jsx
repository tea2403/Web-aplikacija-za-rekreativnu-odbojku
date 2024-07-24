import React from "react";
import "./Naslovna.css";
import {useNavigate} from "react-router-dom";
function Naslovna (){
  const navigate = useNavigate();
    return (
    <>
    <div className="pozadina-pocetna" style={{backgroundImage: "url('images/poz.png')", opacity: "1"}}></div>
    <div className="kartice">
      <div className="kartica" style={{backgroundImage: "url('images/najam.png')"}} onClick={(e) => { e.preventDefault(); navigate("/iznajmi")}}>
        <span className="karticaNaslov">Iznajmi teren</span>
      </div>
      <div className="kartica" style={{backgroundImage: "url('images/forum.png')"}} onClick={(e) => { e.preventDefault(); navigate("/forum")}}>
        <span className="karticaNaslov">Forum</span>
      </div>
      <div className="kartica" style={{backgroundImage: "url('images/tim.png')"}} onClick={(e) => { e.preventDefault(); navigate("/tim")}}>
        <span className="karticaNaslov">Timovi</span>
      </div>
      <div className="kartica" style={{backgroundImage: "url('images/rezerviraj.png')"}} onClick={(e) => { e.preventDefault(); navigate("/rezerviraj")}}>
        <span className="karticaNaslov">Rezerviraj!</span>
      </div>
    </div>
    </>
  )
}
export default Naslovna;