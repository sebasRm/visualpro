

const eliminarUsuario = async()=>
{
  sessionStorage.removeItem('usuarioActivo');
  window.location.href="/";        
}
export default eliminarUsuario