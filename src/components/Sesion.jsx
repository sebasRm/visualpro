import React,{useRef} from 'react';
import axios from 'axios'

import '../assets/css/Sesion.css';
import fondoLogin from "../assets/img/fondoLogin.jpg"
import LogoVisual from "../assets/img/logo.png"

import { Fragment } from 'react/cjs/react.production.min';
import MenuCoordinador from './MenuCoordinador';
import {BrowserRouter as Router,Switch, Route} from 'react-router-dom'


const validarUsuario = async()=>
{
    var rol=null;
    let usuarios =JSON.parse(sessionStorage.getItem('usuarioActivo'));
    usuarios.map(usuarios=><div>{rol=usuarios.rol}</div>)
   
    if(rol=='Coordinador')
    {
        window.location.href="/MenuCoordinador";    
    }
    else
    {
        window.location.href="/MenuLider"; 
    }
    console.log(rol)
}


const enviarDatos = async (usuario,contrasena)=>
 {
        try
        {
            var formData = new FormData();
            formData.append('usuario', usuario);
            formData.append('contrasena',contrasena);
            const res = await axios.post('http://localhost/Apis/login.php',formData).then((resJson)=>{
                return resJson.data;    
            }); 
            return res;   
        }
        catch(error)
        {
            console.error(error);
        }       
}



export default function Sesion ()
{
    
    const reftUsuario = useRef(null);
    const reftPassword = useRef(null);

    const handleLogin= async()=>{

           let usuario = reftUsuario.current.value;
           let contrasena = reftPassword.current.value;

           if(usuario=="" || contrasena =="")
           {
            alert("Ingrese los campos correctamente")
           }
           else
           {
             const resJson= await enviarDatos(usuario,contrasena); 
            if(resJson.conectado == false)
            {
                alert("Usuario o contraseña incorrectas")
            }
            else
            {
                var res=resJson.datos
                sessionStorage.setItem('usuarioActivo', JSON.stringify(res));
               validarUsuario()

            }
        }
    }



    return(
      <Fragment>
       <div className="">
            <img src={fondoLogin} className="fondoLogin"/>
       </div>

       <div className="container-fluid">
      <div className="row align-items-center justify-content-center center-block minh-100 ">
       <div className="col-xs-6 col-sm-6  col-md-6 col-lg-4 ">
     
            <div className="card text-white bg-secondary">
                <div className="card-header text-center ">
                     <img src={LogoVisual} className="logo img-fluid mx-auto d-block"/>
                     <h2 className="card-title">Ingresar a Suite de dirección de proyectos</h2>  
                </div>

            <div className="card-body">
                <div className="input-group mb-3">
                   <div className="input-group-prepend">
                     <span className="input-group-text" id="basic-addon1">
                     ✉️</span>
                   </div>   
                <input
                 type="email"
                 className="form-control"
                 placeholder="Correo"
                 aria-label="Username"
                 aria-describedby="basic-addon1"
                 ref={reftUsuario} />
                </div>

                <div className="input-group mb-3">
                    <div className="input-group-prepend">
                    <span className="input-group-text" id="basic-addon2">
                        🔑</span>
                </div>
                
                <input 
                type="password" 
                className="form-control" 
                placeholder="Contraseña" 
                aria-label="Contraseña" 
                aria-describedby="basic-addon2" 
                ref={reftPassword}/>
                </div>

                <div className="text-center">
                <button onClick={handleLogin} 
                                className=" btn btn-lg btn-info btn-block">
                                    Acceder
                </button>
                </div>
                </div>
            </div>

            </div>
          
        </div>
     </div>
      </Fragment>     
    )
}