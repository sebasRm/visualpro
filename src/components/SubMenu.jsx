import React, {useState, useEffect} from 'react'
import LogoVisual from "../assets/img/logo.png"
import iconoUsuario from "../assets/img/directivo.png"
import MenuItem from './Navbar/MenuItem';

const menuItems =[
    {
        name:"Menú", 
        to: "/", 
        iconClassName:'bi bi-house'
    },
    {
        name:"Proyectos",
        to:"/proyectos",
        iconClassName:'bi bi-archive',
        subMenus:[{name:"Crear proyecto",to:"/Perfil"},{name:"Proyectos"}]
    },
];


 const SubMenu= (props)=> {

    let usuario =JSON.parse(sessionStorage.getItem('usuarioActivo'));
    const [inactive ,setInactive]= useState(false);

    useEffect(() => {
        if(inactive){
            document.querySelectorAll('.sub-menu').forEach(el =>
                {
                el.classList.remove("active");
                })
        }
    }, [inactive])

    return (
        <div className={`side-menu ${inactive ? 'inactive': ''}`}> 
            <div className="top-section">
                <div className="logo">
                    <img src={LogoVisual}/>
                </div>
                <div onClick={()=>{
                    setInactive(!inactive);
                }} className="toggle-menu-btn">
                  { inactive ?
                  (
                    <i class="bi bi-arrow-right-square-fill"></i>
                  ):(
                    <i class="bi bi-arrow-left-square-fill"></i>
                  ) }   
                </div>
            </div>
            
            <div className="divider"></div>
            
            <div className="main-menu">
                <ul>
                    {
                        menuItems.map((menuItem, index)=>(
                            <MenuItem 
                            key={index}
                            name={menuItem.name} 
                            to={menuItem.to}
                            subMenus={menuItem.subMenus || []}
                            iconClassName={menuItem.iconClassName}
                            onClick={()=>
                                {
                                    if(inactive){
                                       setInactive(false); 
                                    }
                                }
                            }

                            />
                            
                    ))}

                    {
                    /*<li>
                        <a className="menu-item">
                            <div className="menu-icon">
                                <i class="bi bi-house"></i>
                            </div>
                            <span>Inicio</span> 
                        </a>
                    </li>

                    <li>
                        <a className="menu-item">
                            <div className="menu-icon">
                            <i class="bi bi-archive"></i>
                            </div>
                            <span>Proyectos</span> 
                        </a>
                        <ul className="sub-menu">
                            <li>
                                <a >
                                     <span>Crear proyectos</span> 
                                </a>
                            </li>
                            <li>
                                <a >
                                    <span>Buscar proyectos</span> 
                                </a>
                            </li>
                        </ul>
                    </li>
                    */
                    }
                  
                </ul>
            
    </div>

    <div className="side-menu-footer">
        <div className="avatar">
          <img src={iconoUsuario} alt="user"/>
        </div>  

         <div className="user-info">
            <p>
              {usuario.map(usuario => <div>{usuario.nombres}</div>,)} 
            </p>

            <p>
             {usuario.map(usuario => <div>{usuario.correo}</div>)}    
            </p>
        </div>  

    </div>

          
        </div>    
    );
};
export default SubMenu;
