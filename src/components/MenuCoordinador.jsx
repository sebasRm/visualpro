
import CardCrearProyectos from './Card/CardCrearProyectos';
import CardVisualizarProyectos from './Card/CardVisualizarProyectos';
import {BrowserRouter as Router,Switch, Route} from 'react-router-dom'
import fondoLogin from "../assets/img/fondoLogin.jpg"

import { Fragment } from "react";
import React, {Component} from 'react'; 
import '../assets/css/Menu.css';
import {Link} from 'react-router-dom'

import Perfil from './Perfil';

import Sesion from './Sesion';

import CerrarSesion from './Navbar/CerrarSesion';
import SubMenu from './SubMenu';
const Proyectos =() =>{
    return <h1>proyectos knbhghghjggjh</h1>
}
 

class MenuCoordinador extends Component{
    
    render()
    
    {

       
        let usuario =JSON.parse(sessionStorage.getItem('usuarioActivo'));
        return(  
    <Fragment>
   <SubMenu/>
     
                    <div className="">
                            <img src={fondoLogin} className="fondoLogin"/>
                    </div>

                    <div className="container-fluid text-center">
                        <div className="row">
                            <div className="col-lg-12">
                            <div className="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <h2>Bienvenido Coordinador </h2>
                            <h4>{usuario.map(usuario=> <div>{usuario.nombres} {usuario.apellidos}</div>)}</h4>
                            </div>
                            </div>
                        </div>
                    </div>

            
                    <div className="container ">
                        
                        <div className="row align-items-center justify-content-center center-block minh-100">

                            <div className="col-xs-6 col-sm-6 col-md-6 col-lg-4">
                             <CardCrearProyectos/>     
                                  
                            </div>

                            <div className="col-xs-6 col-sm-6  col-md-6 col-lg-4">
                            
                            <CardVisualizarProyectos/>     
                                  
                            </div>
                        </div>
                    </div>

                    
         </Fragment>
        ) 
    }
        
}export default MenuCoordinador;