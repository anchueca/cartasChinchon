package cliente;

import cliente.excepciones.NumeroParametrosExcepcion;

/*
Interfaz de usuario del chinchón por consola
 */
public class ProcesadorComandos {
    private final AccionesConsola acciones;

    public ProcesadorComandos(AccionesConsola acciones) {
        this.acciones = acciones;
    }

    public AccionesConsola getAccionesConsola() {
        return this.acciones;
    }

    /*
    Proesa los comandos
     */
    public boolean procesarInstrccion(String instruccion) throws NumeroParametrosExcepcion {
        String[] palabras = instruccion.split("\\s+");
        if (!this.acciones.enPartida()) {
            switch (palabras[0]) {
                case "entrar": {
                    if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                    this.acciones.unirsePartida(palabras[1], palabras[2]);
                    break;
                }
                case "crear": {
                    if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                    this.acciones.crearPartida(palabras[1]);
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
        } else {
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
                default:
                    return false;

            }
        }
        return true;

    }
}
