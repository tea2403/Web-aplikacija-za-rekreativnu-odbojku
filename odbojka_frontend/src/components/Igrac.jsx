import React from "react";
import "./Igrac.css";
function Igrac(){
    const [error, setError] = React.useState('');
    const [podaci, setPodaci] = React.useState({ spol: "", rodenje: "", iskustvo: ""});
    const [klubovi, setKlubovi] = React.useState([{ klub: "", od: "", do: ""}]);
    const pozicije = ["Srednjak", "Korektor", "Tehničar", "Libero", "Primač"];
    const [odabranePozicije, SetOdabranePozicije] = React.useState([]);
    const user = localStorage.getItem("e_mail");
    const popis = [
      { klub: "HAOK ZAGREB", mjesto: "Zagreb", pbr: "10000" },
      { klub: "HAOK DUBRAVA", mjesto: "Zagreb", pbr: "10000" },
      { klub: "HAOK MLADOST", mjesto: "Zagreb", pbr: "10000" },
      { klub: "HAOK RIJEKA CO", mjesto: "Rijeka", pbr: "51000" },
      { klub: "HAOK ŽAL BANJOLE", mjesto: "Pula", pbr: "52100" },
      { klub: "HOK GORICA", mjesto: "Velika Gorica", pbr: "10410" },
      { klub: "KOP ČAKOVEC", mjesto: "Čakovec", pbr: "40000" },
      { klub: "KOP SIGET", mjesto: "Zagreb", pbr: "10000" },
      { klub: "KOP ZAGREB", mjesto: "Zagreb", pbr: "10000" },
      { klub: "MOK BROD", mjesto: "Slavonski Brod", pbr: "35000" },
      { klub: "MOK ČAZMA", mjesto: "Čazma", pbr: "43240" },
      { klub: "MOK GORNJA VEŽICA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "MOK GROBNIČAN", mjesto: "Rijeka", pbr: "51000" },
      { klub: "MOK MARSONIA", mjesto: "Slavonski Brod", pbr: "35000" },
      { klub: "MOK MURSA - OSIJEK", mjesto: "Osijek", pbr: "31000" },
      { klub: "MOK RIJEKA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "MOK UMAG", mjesto: "Umag", pbr: "52470" },
      { klub: "MOK VUKOVAR", mjesto: "Vukovar", pbr: "32000" },
      { klub: "MOK ŽELJEZNIČAR", mjesto: "Osijek", pbr: "31000" },
      { klub: "OK ALBONA", mjesto: "Labin", pbr: "52220" },
      { klub: "OK ARBANASI", mjesto: "Zadar", pbr: "23000" },
      { klub: "OK BEDEX", mjesto: "Zabok", pbr: "49210" },
      { klub: "OK BELIŠĆE", mjesto: "Belišće", pbr: "31551" },
      { klub: "OK BENKOVAC", mjesto: "Benkovac", pbr: "23420" },
      { klub: "OK BILJE", mjesto: "Osijek", pbr: "31000" },
      { klub: "OK BRCKO", mjesto: "Županja", pbr: "32270" },
      { klub: "OK BRDA", mjesto: "Split", pbr: "21000" },
      { klub: "OK CENTURION PULA", mjesto: "Pula", pbr: "52100" },
      { klub: "OK ČEPIN", mjesto: "Osijek", pbr: "31000" },
      { klub: "OK CRATIS VARAŽDIN", mjesto: "Varaždin", pbr: "42000" },
      { klub: "OK CROATIA", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK DARUVAR", mjesto: "Daruvar", pbr: "43500" },
      { klub: "OK DINAMO", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK DON BOSCO", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK DONAT", mjesto: "Zadar", pbr: "23000" },
      { klub: "OK DONJI MIHOLJAC", mjesto: "Donji Miholjac", pbr: "31540" },
      { klub: "OK DUBRAVA", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK DUBROVNIK 2001", mjesto: "Dubrovnik", pbr: "20000" },
      { klub: "OK DUGO SELO", mjesto: "Dugo Selo", pbr: "10370" },
      { klub: "OK ĐAKOVO", mjesto: "Đakovo", pbr: "31400" },
      { klub: "OK FENIKS", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK FORESTA", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK FUNTANA VRSAR", mjesto: "Poreč", pbr: "52450" },
      { klub: "OK GALEB", mjesto: "Biograd na Moru", pbr: "23210" },
      { klub: "OK GLINA", mjesto: "Glina", pbr: "44400" },
      { klub: "OK GORICA", mjesto: "Velika Gorica", pbr: "10410" },
      { klub: "OK GROBNIČAN", mjesto: "Rijeka", pbr: "51000" },
      { klub: "OK GUSAR", mjesto: "Omiš", pbr: "21310" },
      { klub: "OK IVANIĆ GRAD", mjesto: "Ivanić-Grad", pbr: "10310" },
      { klub: "OK KASTAV 1998", mjesto: "Kastav", pbr: "51215" },
      { klub: "OK KAŠTEL LUKŠIĆ", mjesto: "Kaštela", pbr: "21214" },
      { klub: "OK KELTEKS", mjesto: "Karlovac", pbr: "47000" },
      { klub: "OK KOLONIJA", mjesto: "Slavonski Brod", pbr: "35000" },
      { klub: "OK KOPRIVNICA", mjesto: "Koprivnica", pbr: "48000" },
      { klub: "OK KOSTRENA", mjesto: "Bakar", pbr: "51221" },
      { klub: "OK KOZALA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "OK KRAVARSKO", mjesto: "Velika Gorica", pbr: "10414" },
      { klub: "OK LINGA", mjesto: "Osijek", pbr: "31000" },
      { klub: "OK LIPA VOLLEY", mjesto: "Pakrac", pbr: "34550" },
      { klub: "OK MAKARSKA", mjesto: "Makarska", pbr: "21300" },
      { klub: "OK MARČANA", mjesto: "Labin", pbr: "52220" },
      { klub: "OK MARINA KAŠTELA", mjesto: "Kaštela", pbr: "21216" },
      { klub: "OK MEDICINAR TRNJE", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK MEDVEŠČAK", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK MERTOJAK", mjesto: "Split", pbr: "21000" },
      { klub: "OK MLADOST 2000", mjesto: "Knin", pbr: "22300" },
      { klub: "OK MULTIBIBUS", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK NEBO", mjesto: "Zaprešić", pbr: "10290" },
      { klub: "OK NEDELIŠĆE - ELTING", mjesto: "Čakovec", pbr: "40000" },
      { klub: "OK NEPOMUK", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK NOVA MOKOŠICA", mjesto: "Dubrovnik", pbr: "20000" },
      { klub: "OK NOVALJA", mjesto: "Novalja", pbr: "53291" },
      { klub: "OK NOVIGRAD", mjesto: "Novigrad", pbr: "52466" },
      { klub: "OK OGULIN", mjesto: "Ogulin", pbr: "47300" },
      { klub: "OK OLIMPIK", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK OPATIJA", mjesto: "Opatija", pbr: "51410" },
      { klub: "OK ORAO", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK OSIJEK JUG 2", mjesto: "Osijek", pbr: "31000" },
      { klub: "OK PETRINJA", mjesto: "Petrinja", pbr: "44250" },
      { klub: "OK PLAVI PEKLENICA", mjesto: "Mursko Središće", pbr: "40315" },
      { klub: "OK POREČ", mjesto: "Poreč", pbr: "52440" },
      { klub: "OK POSAVINA BROD", mjesto: "Slavonski Brod", pbr: "35000" },
      { klub: "OK POŽEGA", mjesto: "Požega", pbr: "34000" },
      { klub: "OK PREKO", mjesto: "Zadar", pbr: "23000" },
      { klub: "OK PULA", mjesto: "Pula", pbr: "52100" },
      { klub: "OK QUIRIN SISAK", mjesto: "Sisak", pbr: "44000" },
      { klub: "OK RIBOLA KAŠTELA", mjesto: "Kaštela", pbr: "21217" },
      { klub: "OK RJEČINA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "OK ROVINJ", mjesto: "Rovinj", pbr: "52210" },
      { klub: "OK RUGVICA", mjesto: "Dugo Selo", pbr: "10370" },
      { klub: "OK SAMOBOR", mjesto: "Samobor", pbr: "10430" },
      { klub: "OK SHEEFT", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK ŠIBENIK '91", mjesto: "Šibenik", pbr: "22000" },
      { klub: "OK SISAK", mjesto: "Sisak", pbr: "44000" },
      { klub: "OK SLAVONSKI BROD", mjesto: "Slavonski Brod", pbr: "35000" },
      { klub: "OK SOLIN", mjesto: "Solin", pbr: "21210" },
      { klub: "OK SPLIT", mjesto: "Split", pbr: "21000" },
      { klub: "OK ŠTRIGOVA", mjesto: "Mursko Središće", pbr: "40315" },
      { klub: "OK SUNJA", mjesto: "Sisak", pbr: "44250" },
      { klub: "OK SVETI MATEJ 06 VIŠKOVO", mjesto: "Kastav", pbr: "51215" },
      { klub: "OK TURNIĆ", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK UMAG", mjesto: "Umag", pbr: "52470" },
      { klub: "OK VALPOVKA", mjesto: "Valpovo", pbr: "31550" },
      { klub: "OK VELI VRH", mjesto: "Pula", pbr: "52100" },
      { klub: "OK VELIKA GORICA", mjesto: "Velika Gorica", pbr: "10410" },
      { klub: "OK VERUDA", mjesto: "Pula", pbr: "52100" },
      { klub: "OK VINICA", mjesto: "Varaždin", pbr: "42230" },
      { klub: "OK VODICE", mjesto: "Vodice", pbr: "22211" },
      { klub: "OK VODNJAN", mjesto: "Vodnjan", pbr: "52215" },
      { klub: "OK ZADAR", mjesto: "Zadar", pbr: "23000" },
      { klub: "OK Zagreb Nexus", mjesto: "Zagreb", pbr: "10000" },
      { klub: "OK ZRINSKI NUŠTAR", mjesto: "Vinkovci", pbr: "32100" },
      { klub: "OKM CENTROMETAL", mjesto: "Zadar", pbr: "23000" },
      { klub: "OOK FAŽANA", mjesto: "Vodnjan", pbr: "52212" },
      { klub: "OŽK SPLIT VOLLEY TEAM", mjesto: "Split", pbr: "21000" },
      { klub: "ŠOK PEŠČENICA", mjesto: "Zagreb", pbr: "10000" },
      { klub: "SU PLAVI ZMAJ", mjesto: "Split", pbr: "21000" },
      { klub: "ŽAOK ŠKURINJE RIJEKA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "ŽKOP JARUN", mjesto: "Zagreb", pbr: "10000" },
      { klub: "ŽOK AZENA VELIKA GORICA", mjesto: "Velika Gorica", pbr: "10410" },
      { klub: "ŽOK BALE", mjesto: "Vodnjan", pbr: "52215" },
      { klub: "ŽOK BARANJA", mjesto: "Beli Manastir", pbr: "31300" },
      { klub: "ŽOK BIBINJE", mjesto: "Zadar", pbr: "23205" },
      { klub: "ŽOK BJELOVAR", mjesto: "Bjelovar", pbr: "43000" },
      { klub: "ŽOK ČAZMA", mjesto: "Čazma", pbr: "43240" },
      { klub: "ŽOK CRIKVENICA", mjesto: "Crikvenica", pbr: "51260" },
      { klub: "ŽOK DRENOVA", mjesto: "Rijeka", pbr: "51000" },
      { klub: "ŽOK DUBROVNIK", mjesto: "Dubrovnik", pbr: "20000" },
      { klub: "ŽOK ENNA VUKOVAR", mjesto: "Vukovar", pbr: "32000" },
      { klub: "ŽOK FERIVI VIŠNJEVAC", mjesto: "Osijek", pbr: "31000" },
      { klub: "ŽOK IVANEC", mjesto: "Ivanec", pbr: "42240" },
      { klub: "ŽOK KAŠTEL (Pribislavec)", mjesto: "Čakovec", pbr: "40311" },
      { klub: "ŽOK LEPOGLAVA", mjesto: "Lepoglava", pbr: "42230" },
      { klub: "ŽOK LOŠINJ", mjesto: "Mali Lošinj", pbr: "51550"},
      { klub: "ŽOK LUDBREG", mjesto: "Ludbreg", pbr: "42230" },
      { klub: "ŽOK METKOVIĆ", mjesto: "Metković", pbr: "20350" },
      { klub: "ŽOK NAŠICE", mjesto: "Našice", pbr: "31500" },
      { klub: "ŽOK NOVA GRADIŠKA", mjesto: "Nova Gradiška", pbr: "35400" },
      { klub: "ŽOK OSIJEK", mjesto: "Osijek", pbr: "31000" },
      { klub: "ŽOK PAZIN", mjesto: "Pazin", pbr: "52000" },
      { klub: "ŽOK RIBOLA KAŠTELA", mjesto: "Kaštela", pbr: "21217" },
      { klub: "ŽOK ROVINJ - ROVIGNO", mjesto: "Rovinj", pbr: "52210" },
      { klub: "ŽOK ŠIBENIK", mjesto: "Šibenik", pbr: "22000" },
      { klub: "ŽOK SINJ", mjesto: "Sinj", pbr: "21230" },
      { klub: "ŽOK SUŠAK", mjesto: "Rijeka", pbr: "51000" },
      { klub: "ŽOK TENJA", mjesto: "Osijek", pbr: "31000" },
      { klub: "ŽOK TOPLICE", mjesto: "Jastrebarsko", pbr: "10450" },
      { klub: "ŽOK TROGIR", mjesto: "Trogir", pbr: "21220" },
      { klub: "ŽOK VALLIS AUREA", mjesto: "Oroslavje", pbr: "49216" },
      { klub: "ŽOK VINKOVCI", mjesto: "Vinkovci", pbr: "32100" },
      { klub: "ŽOK VITEZ 11 ANTUNOVAC", mjesto: "Osijek", pbr: "31000" },
      { klub: "ŽOK VRBOVEC", mjesto: "Vrbovec", pbr: "10340" },
      { klub: "ŽOK ŽUPA DUBROVAČKA", mjesto: "Dubrovnik", pbr: "20000" }
    ];
    
    

    React.useEffect(() => {
      fetch(`/api/igrac/${user}`)
        .then(data => data.json())
        .then(podaci => {
          const novi = {};
          for (const key in podaci) {
            novi[key] = podaci[key] == null ? "" : podaci[key];
          }
          setPodaci(novi);
      })
      fetch(`/api/trenirao/${user}`)
        .then(data => data.json())
        .then(podaci => {
          const novi = podaci.map(item => ({
            klub: item.klub !== null ? item.klub : "",
            od: item.od !== null ? item.od : "",
            do: item.do !== null ? item.do : ""
          }));
          setKlubovi(novi); 
      })
      fetch(`/api/pozicije/${user}`)
        .then(data => data.json())
        .then(podaci => {
          SetOdabranePozicije(podaci); 
      })
    }, []);

    function onSubmit(e) {
      var zast = 0;
      e.preventDefault();  
      setError(""); 
      const formData1 = new FormData();
      formData1.append("spol", podaci.spol);
      formData1.append("rodenje", podaci.rodenje);
      formData1.append("iskustvo", podaci.iskustvo);

      const options1 = {
        method: "POST",
        body: formData1
      };
      fetch("/api/igrac/uredi", options1)
        .then(response => {
          if (response.ok) {
            response.json().then(podaci1 => {
              const novi = {};
              for (const key in podaci1) {
                novi[key] = podaci1[key] === null ? "" : podaci1[key];
              }    
              novi["brisi"]=false
              document.querySelector('input[name="brisi"]').checked = false;
              setPodaci(novi); 
              zast = zast + 1;
              const data = klubovi.map(el => ({
                klub: el.klub,
                pbr: popis.find((klub) => klub.klub === el.klub).pbr,
                od_godina: el.od,
                do_godina: el.do
              }))
              const options = {
                method: "POST",
                headers: {
                  "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
              };               
              fetch("/api/klubovi/uredi", options)
                .then(response => {
                  if (response.ok) {
                    zast = zast + 1;
                    const options = {
                      method: "POST",
                      headers: {
                        "Content-Type": "application/json"
                      },
                      body: JSON.stringify(odabranePozicije)
                    };
                    fetch("/api/pozicije/", options)
                      .then(response => {
                        if (response.ok) {
                          zast = zast + 1;
                          if(zast == 3)setError("Podaci uspješno promijenjeni.")                                                            
                        } else {
                          response.json().then((data) => {setError(data.message)})
                        }
                      });
                  } else {
                    response.json().then((data) => {setError(data.message)})
                  }
                });
              })
          } else {
            response.json().then((data) => {setError(data.message)})
          }
        });
    }
    function onChange(event) {
      const {name, value} = event.target;
      setPodaci(stara => ({...stara, [name]: value}))
    }
    function onChange2(index, event){
      const { name, value } = event.target;
      const list = [...klubovi];
      list[index][name] = value;
      setKlubovi(list);
    }
    function dodajKlub(){
      setKlubovi([...klubovi, { klub: "", od: "", do: ""}]);
    };
    function izbrisiKlub(index){
      const list = [...klubovi];
      list.splice(index, 1);
      setKlubovi(list);
    };
    function provjeri(){
      klubovi.forEach((el, index) => {
         if(el.od > el.do) return false
      });
      return true
    }
    function oznaciPoz(pozicija){
      odabranePozicije.includes(pozicija)
        ? SetOdabranePozicije(odabranePozicije.filter(p => p !== pozicija))
        : SetOdabranePozicije([...odabranePozicije, pozicija]);
    };
    return (
    <>
      <div className="igrac">
        <div className="error">{error}</div>
        <form className="osobni_form" onSubmit={onSubmit}>
          <label> Spol:</label>
          <select value={podaci.spol} onChange={onChange} name="spol">
            <option value="">Ne želim reći.</option>
            <option value="M">Muški</option>
            <option value="Ž">Ženski</option>
            <option value="Ostalo">Ostalo</option>
          </select>
          <label> Datum rođenja:</label>
          <input value={podaci.rodenje} onChange={onChange} type="date" name="rodenje" style={{marginBottom: "17px"}}/>
          <label> Godina početka igranja:</label>
          <input value={podaci.iskustvo} list="godine" onChange={onChange} type="number" name="iskustvo"/><br/> 
          <datalist id="godine">
            {Array.from({ length: 100 }, (el, index) => {
              const poc = new Date().getFullYear() - 99;
              const god = poc + index;
              return (
                  <option key={index} value={god}>{god}</option>
              );
            })}
          </datalist>
          <div>
            Odaberite pozicije koje ste spremni igrati:<br/> 
            {pozicije.map((pozicija) => (
              <label key={pozicija}>
                <input type="checkbox" checked={odabranePozicije.includes(pozicija)} onChange={() => oznaciPoz(pozicija)}/> {pozicija}<br/>
              </label>
            ))}
          </div>
          <br/>
          <u>Klubovi u kojima ste trenirali: </u>
          {klubovi.map((item, i) => (
            <div className = "klubovi" key={i}>
              <div className = "klub" key={i}>
                <label> Klub:</label><br/>
                <select name="klub" value={item.klub} onChange={(event) => onChange2(i, event)}>
                  <option key="0" value={"0"}>-------------</option>                              
                  {popis.filter(obj => item.klub == obj.klub || !klubovi.some(klub => klub.klub == obj.klub)).map((obj, index) => (
                    <option key={index} value={obj.klub}>{obj.klub}</option>
                  ))}
                </select>
                <div className="trajanjeKlub" key={i}>
                  <label>Od:</label>
                  <input placeholder="godina" value={item.od} onChange={(event) => onChange2(i, event)} type="number" name="od" list={"godine" + i}/>
                  <label>Do:</label>
                  <input placeholder="godina" value={item.do} onChange={(event) => onChange2(i, event)} type="number" name="do" list={"godine" + i}/>
                  <datalist id={"godine" + i}>
                    {Array.from({ length: 100 }, (el, index) => {
                      const poc = new Date().getFullYear() - 99;
                      const god = poc + index;
                      return (
                        <option key={index} value={god}>{god}</option>
                      );
                    })}
                </datalist>
                </div>
              </div>
              {<button className="makniKlub" type="button" onClick={() => izbrisiKlub(i)}><b>-</b></button>}
            </div>
          ))}
          <button className="dodajKlub" type="button" onClick={dodajKlub}><b>+</b></button>
          {/* <div><span style={{color: "red", fontSize: "23px"}}>*</span> Označena polja su obavezna ako želite imati ulogu igrača.</div> */}
          <button type="submit" disabled={!provjeri()} className="osobni-submit">Promijeni</button>
        </form>
      </div>
    </>
  )
}
export default Igrac;