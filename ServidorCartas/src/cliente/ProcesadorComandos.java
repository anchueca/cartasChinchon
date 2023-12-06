package cliente;

import modeloDominio.excepciones.NumeroParametrosExcepcion;

/*
Interfaz de usuario del chinchón por consola
 */
public class ProcesadorComandos {
    /*
     AccionesConsola posee la implementación de los métodos que se deben ejecutar
     */
    private final AccionesConsola acciones;

    public ProcesadorComandos(AccionesConsola acciones) {
        this.acciones = acciones;
    }

    /*
    Proesa los comandos. Sería mejor que el control de argumenots lo hiciera el método y no aquí.
    Devuelve falso si no se reconoce el comando.

    Hay otro equivalente en el servidor. Sería deseable juntarlos en alguna estructura más general para no repetir
    la estructura. Quizá se podría emplear un Map que haga corresponder la instrucción y el método asociado
    (¿Delegados, alguna interfaz/clase abstracta?) no obstante no sé como se haría en Java y sería quizá mucho trabajo.
    Estaría bien que encapsulara también la documentación de los métodos aosciados. Así se podría generar automáticamente
    el resultado de ayuda (está sin implementar).
     */
    public boolean procesarInstrccion(String instruccion) throws NumeroParametrosExcepcion {
        String[] palabras = instruccion.split("\\s+");
        //Acciones fuera de la partida
        if (!this.acciones.enPartida()) {
            switch (palabras[0]) {
                case "entrar": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.acciones.unirsePartida(palabras[1], palabras[2]);
                    break;
                }
                case "crear": {
                    if (palabras.length < 2 || palabras.length > 3) throw new NumeroParametrosExcepcion();
                    if (palabras.length == 2) this.acciones.crearPartida(palabras[1], "");
                    else this.acciones.crearPartida(palabras[1], palabras[2]);
                    break;
                }
                case "salir": {
                    this.acciones.salir();
                    break;
                }
                case "ayuda": {
                    //"Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    break;
                }
                case "partidas": {
                    acciones.listaPartidas();
                    break;
                }
                default:
                    return false;
            }
        } else {//Acciones dentro de la partida
            switch (palabras[0]) {
                case "salir": {
                    this.acciones.salirPartida();
                    break;
                }
                case "salir!": {
                    this.acciones.salirForzado();
                    break;
                }
                case "ayuda": {
                    //"Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    break;
                }
                case "mover": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.acciones.mover(Integer.parseInt(palabras[1]), Integer.parseInt(palabras[2]));
                    break;
                }
                case "echar": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    this.acciones.echar(i);
                    break;
                }
                case "cerrar": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    int i = Integer.parseInt(palabras[1]);
                    this.acciones.cerrar(i);
                    break;
                }
                case "coger": {
                    if (palabras.length > 2) throw new NumeroParametrosExcepcion();
                    if (palabras.length == 2) this.acciones.coger(palabras[1]);
                    else this.acciones.coger();
                    break;
                }
                case "ordenar": {
                    this.acciones.ordenar();
                    break;
                }
                case "empezar": {
                    this.acciones.empezar();
                    break;
                }
                case "jugadores": {
                    this.acciones.listaJugadores();
                    break;
                }
                case "estado": {
                    this.acciones.estado();
                    break;
                }
                case "puntuaciones": {
                    this.acciones.puntuaciones();
                    break;
                }
                case "ver": {
                    this.acciones.pintarPartida();
                    break;
                }
                case "turno": {
                    this.acciones.verTurno();
                    break;
                }
                case "crearIA": {
                    if (palabras.length > 3) throw new NumeroParametrosExcepcion();
                    if (palabras.length ==2) this.acciones.crearIA(palabras[1]);
                    else this.acciones.crearIA("");
                    break;
                }
                default:
                    return false;

            }
        }
        return true;
    }
}
