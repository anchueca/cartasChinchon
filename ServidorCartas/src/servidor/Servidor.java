package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
	public static void main(String[] args) {
		ExecutorService pool=Executors.newCachedThreadPool();
		try {
			ServerSocket s=new ServerSocket(55555);
			Socket cliente;
			System.out.println("Inicio servidor");
			while(true) {
				try {
					cliente=s.accept();
					pool.execute(new AtenderCliente(cliente));
					System.out.println("Solicitus aceptada "+cliente.toString());
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			pool.shutdown();
		}
	}
}
