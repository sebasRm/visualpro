import React, {Component} from 'react'; 
import creaProyecto from "./img/creaProyecto.jpg"
import {Link} from 'react-router-dom'

class CardCrearProyectos extends Component{

    render(){
        return(
       
             <div className="card text-center">
                 <img src={creaProyecto} className="img-fluid" />
                 <div className="card-body">
                     <h4 className="card-title">Crea tu proyecto</h4>
                     <p className="card-text text-secundary">En esta sección encontraras la lista de aquellos estudiantes que a han sido vacunados contra covid-19</p>
                      <Link to="/vacunacion" className="btn btn-outline-secundary">Ver</Link>
                </div>
             </div>)
    }
    
}

export default CardCrearProyectos
