

import java.util.Scanner;
import java.sql.SQLException;
import java.text.ParseException;
import SISTEMA.Serrano;


public class Seminario1{

	public  static void main(String[] args) throws SQLException, ParseException {

		Serrinario tomate = new Serrinario ("jdbc:oracle:thin:@//oracle0.ugr.es:1521/practbd.oracle0.ugr.es", "x6512027", "x6512027");
		tomate.connect();

		try{
			int opcion;
			Scanner input = new Scanner (System.in);
			boolean terminar=false;
			System.out.println("\nBienvenido a nuestra Base de Datos, introduzca un numero de 1 al 4:");
			while(!terminar){
				System.out.println("1-Equipo.");
        System.out.println("2-Eventos");
        System.out.println("3-Zona de abonado");
        System.out.println("4-Tienda");
        System.out.println("5-Salir.");
        System.out.println("6-MANTENIMIENTO - crear tablas");

				opcion = input.nextInt();

				switch(opcion){
		            case 1:
		                tomate.menuEquipo();
		            break;

		            case 2:
		                tomate.menuEventos();
		            break;

		            case 3:
		                tomate.menuAbonados();
		            break;

                case 4:
		                tomate.menuTienda();
		            break;

                case 5:
		                terminar = true;
						tomate.salir();
		            break;

                case 6:
		                tomate.crearTablas();
		            break;

		            default:
		                System.out.println("Esta opción no es válida. Por favor, indruzca un número del 1 al 4");
		            break;
		        }

			}
		}catch(SQLException e){
				System.out.println(e.getMessage());
			}
	}
}
