
import React,{  } from 'react';
import {BrowserRouter as Router,Switch, Route} from 'react-router-dom'
import Sesion from './components/Sesion';
import MenuCoordinador from './components/MenuCoordinador';
import MenuLider from './components/MenuLider'
import Perfil from './components/Perfil';
import CerrarSesion from './components/Navbar/CerrarSesion';
import SubMenu from './components/SubMenu'
import './assets/css/App.css';

const Proyectos =() =>{
    return <h1>proyectos</h1>
}


function App(){ 
    return(   
        <Router>
        <div className="App">
        
            <Switch>
            <Route path="/" exact component={Sesion}/>   
            <Route path="/MenuCoordinador"  component={MenuCoordinador}/>
            <Route path="/MenuLider" exact component={MenuLider}/> 
            <Route path="/Perfil" exact component={Perfil}/> 
            <Route path="/CerrarSesion" exact component={CerrarSesion}/>   
            <Route path={"/proyectos"}><Proyectos/></Route>   
            </Switch>
        </div>    
      </Router>           
    );
}

export default App;