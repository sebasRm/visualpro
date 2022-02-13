
import React, {Component, Fragment} from 'react'; 
import fondoDos from "../assets/img/fondoDos.jpg"
import iconoDirectivo from "../assets/img/directivo.png"
import '../assets/css/Perfil.css';

import {Link} from 'react-router-dom'
import * as Hi from'react-icons/hi'
import * as AiIcons from'react-icons/ai'



const eliminarUsuario = async()=>
{
    sessionStorage.removeItem('usuarioActivo');
  window.location.href="/";        
}

class Perfil  extends Component{
render(){
  
    let usuario =JSON.parse(sessionStorage.getItem('usuarioActivo'));
    
    return(
    <Fragment>
       
    <div className="">
      <img src={fondoDos} className="fondoDos"/>
    </div>

    <div className="container-fluid">
      <div className="row align-items-center justify-content-center center-block minh-100">

       <div className="col-xs-6 col-sm-8  col-md-8 col-lg-4 ">
            <div className="card text-white" style={{background:'#1e90ff'}}>
                <div className="card-header  text-center">  
                     <img src={iconoDirectivo} className="icono"/>
                     <h1 className="card-title">Perfil Usuario</h1>  
                </div>         
            <div className="card-body">       
                   <div className="container-fluid">
                       <div className="row align-items-center justify-content-center">
                           <div className="col-xs-12 col-sm-12  col-md-12 col-lg-2">

                           </div>
                           <div className="col-xs-12 col-sm-12  col-md-12 col-lg-10 ">
                            <table>
                                <tr>
                                    <th>
                                        <h4>
                                        {<Hi.HiIdentification/>}
                                        </h4>
                                    </th>
                                    <td>
                                    <h4>
                                    {usuario.map(usuario => <div>{usuario.identificacion}</div>)}    
                                    </h4>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        <h4>
                                        <Hi.HiOutlineMailOpen/>   
                                        </h4>
                                    </th>
                                    <td>
                                    <h4>
                                    {usuario.map(usuario => <div>{usuario.correo}</div>)}    
                                    </h4>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        <h4>
                                        <Hi.HiOutlineUserCircle/> 
                                        </h4>
                                    </th>
                                    <td>
                                    <h4>
                                    {usuario.map(usuario => <div>{usuario.nombres}</div>,)}
                                    </h4>
                                    
                                    
                                    <h4>    
                                     {usuario.map(usuario => <div>{usuario.apellidos}</div>)} 
                                    </h4>
                                    </td>
                                </tr>
                                <tr>
                                    <th>
                                        <h4>
                                        <AiIcons.AiOutlineControl/>  
                                        </h4>
                                    </th>
                                    <td>
                                    <h4>
                                    {usuario.map(usuario => <div>{usuario.rol}</div>)}        
                                    </h4>
                                    </td>
                                    
                                </tr>
                            </table>
                           </div>
                       </div>
                           
                </div>

                <div className="card-footer text-center">
               
                    <button onClick={eliminarUsuario}
                                className="btn btn-dark btn-lg btn-block" >
                                    Cerrar Sesion
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

}export default Perfil