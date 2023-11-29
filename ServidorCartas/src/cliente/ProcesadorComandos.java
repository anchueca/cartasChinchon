package cliente;

import modeloDominio.baraja.Mano;

/*
Interfaz de usuario del chinchón por consola
 */
public class ProcesadorComandos{
    private AccionesConsola acciones;
    public ProcesadorComandos(PartidaCliente partida) {
        this.acciones=new EjecutorConsola(partida);
    }
    public ProcesadorComandos(AccionesConsola acciones) {
        this.acciones=acciones;
    }

    public AccionesConsola getAccionesConsola(){
        return this.acciones;
    }

    /*
    Proesa los comandos
     */
    public String procesarInstrccion(String instruccion) {
        String[] palabras = instruccion.split("\\s+");
        try {
            if(!this.acciones.enPartida()){
                switch (palabras[0]) {
                    case "entrar": {
                        if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                        return this.acciones.unirsePartida(palabras[1], palabras[2]);
                    }
                    case "crear": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        return this.acciones.crearPartida(palabras[1]);
                    }
                    case "salir": {
                        return this.acciones.salir();
                    }
                    case "ayuda": {
                        return "Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    }
                    case "partidas": {
                        return acciones.listaPartidas();
                    }
                }
            }else {
                switch (palabras[0]) {
                    case "salir": {
                        return this.acciones.salirPartida();
                    }
                    case "salir!": {
                        return this.acciones.salirForzado();
                    }
                    case "ayuda": {
                        return "Mostrando ayuda " + (this.acciones.enPartida() ? "inicio" : "juego");
                    }
                    case "mover": {
                        if (palabras.length != 3) throw new NumeroParametrosExcepcion();
                        this.acciones.getPartida().moverMano(Integer.parseInt(palabras[1]), Integer.parseInt(palabras[2]));
                        break;
                    }
                    case "echar": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        int i = Integer.parseInt(palabras[1]);
                        Mano mano = this.acciones.getPartida().verMano();
                        if (this.acciones.getPartida().echarCarta(mano.verCarta(i))) {
                            mano.tomarCarta(i);
                        } else return "Jugada no válida";
                        break;
                    }
                    case "cerrar": {
                        if (palabras.length != 2) throw new NumeroParametrosExcepcion();
                        int i = Integer.parseInt(palabras[1]);
                        Mano mano = this.acciones.getPartida().verMano();
                        if (this.acciones.getPartida().cerrar(mano.verCarta(i))) {
                            mano.tomarCarta(i);
                        } else return ("Jugada no válida");
                        break;
                    }
                    case "coger": {
                        if (palabras.length > 2) throw new NumeroParametrosExcepcion();
                        boolean jugadaValida;
                        if (palabras.length == 1) {
                            jugadaValida = this.acciones.getPartida().cogerCartaCubierta();
                        } else {
                            jugadaValida = palabras[1].compareTo("descubierta") == 0
                                    ? this.acciones.getPartida().cogerCartaDecubierta()
                                    : this.acciones.getPartida().cogerCartaCubierta();
                        }
                        if (!jugadaValida) return ("Jugada no válida");
                        break;
                    }
                    case "ordenar": {
                        this.acciones.getPartida().verMano().ordenar();
                        break;
                    }
                    case "empezar": {
                        this.acciones.getPartida().empezarPartida();
                        return "";
                    }
                    case "jugadores": {
                        return this.acciones.listaJugadores();
                    }
                    case "estado": {
                        return this.acciones.estado();
                    }
                    case "puntuaciones": {
                        return this.acciones.puntuaciones();
                    }
                    case "ver": {
                        return this.acciones.pintarPartida();
                    }

                }
                return ("Comando no reconocido.");
            }
        } catch (NumeroParametrosExcepcion ex) {
            return ("Número de parámetros incorrecto.");
        } catch (NumberFormatException e) {
            return ("Parámetros incorrectos");
        }
        return "OK";
    }

}
