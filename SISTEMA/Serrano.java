package SERRANO;



import java.sql.*;
import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

import java.util.Date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;


public class Serrano {
	private String URL;
	private String Username;
	private String Password;
	private Connection connection;
	private const int CAPACIDAD_ESTADIO = 21000;

	public Serrinario(String URL, String Username, String Password) {
		this.URL = URL;
		this.Username = Username;
		this.Password = Password;
	};

	public void connect() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				connection = DriverManager.getConnection(URL, Username, Password);
				connection.setAutoCommit(false);
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			System.out.println("Connected to the database");
		}
	};



	public void salir() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			System.out.println("Disconnected from the database");

		}
	};

public void crearTablas() throws SQLException {
  try{
    Statement stmt = connection.createStatement();

    // Se crea Tabla PRODUCTO
    stmt.executeUpdate("CREATE TABLE Producto(CProducto NUMBER CONSTRAINT CProducto_no_nulo NOT NULL CONSTRAINT CProducto_clave_primaria PRIMARY KEY)");

    // Se crea Tabla PROVEEDOR
    stmt.executeUpdate("CREATE TABLE Proveedor(CProveedor NUMBER CONSTRAINT Cproveedor_no_nulo NOT NULL CONSTRAINT Cproveedor_clave_primaria PRIMARY KEY,Contacto VARCHAR2(40))");

    // Se crea Tabla CLIENTE
    stmt.executeUpdate("CREATE TABLE Cliente(	CDNI VARCHAR2(9) CONSTRAINT Cdni_no_nulo NOT NULL CONSTRAINT Cdni_clave_primaria PRIMARY KEY) ");

    // Se crea Tabla EMPRESAEXTERNA
    stmt.executeUpdate("CREATE TABLE EmpresaExterna(CNombre VARCHAR2(40) CONSTRAINT Cnombre_no_nulo NOT NULL CONSTRAINT Cnombre_clave_primaria PRIMARY KEY)");

    // Se crea Tabla EMPLEADO
    stmt.executeUpdate("CREATE TABLE Empleado( VARCHAR2(9) CONSTRAINT Cdniempleado_clave_primaria PRIMARY KEY )");

    // Se crea Tabla COMPONENTE
    stmt.executeUpdate("CREATE TABLE Componente(
                      	CDNI VARCHAR2(9) CONSTRAINT Cdni_componente_no_nulo NOT NULL CONSTRAINT Cdni_componente_clave_primaria PRIMARY KEY,
                      	Nombre VARCHAR2(40) CONSTRAINT nombre_no_nulo NOT NULL,
                      	Direccion VARCHAR2(40) CONSTRAINT direccion_no_nulo NOT NULL,
                      	Salario NUMBER CONSTRAINT salario_no_nulo NOT NULL,
                      	Correo VARCHAR2(40) CONSTRAINT correo_no_nulo NOT NULL,
                      	Telefono VARCHAR2(9) CONSTRAINT telefono_no_nulo NOT NULL,
                      	Clausula_rescision NUMBER CONSTRAINT clausula_no_nulo NOT NULL,
                      	FechaInicio DATE, FechaFin DATE)");

    // Se crea Tabla PRODUCTOACTIVO
    stmt.executeUpdate("CREATE TABLE ProductoActivo(CProducto NUMBER CONSTRAINT Cproducto_clave_externa REFERENCES Producto(CProducto) CONSTRAINT Cproducto_act_clave_primaria PRIMARY KEY)");

    // Se crea Tabla COMPRATIENDA
    stmt.executeUpdate("CREATE TABLE CompraTienda(
                        	CDNI VARCHAR2(9) CONSTRAINT Cdni_clave_externa REFERENCES Cliente(CDNI),
                        	CProducto NUMBER CONSTRAINT Cproducto_CT_clave_externa REFERENCES Producto(CProducto),
                        	CONSTRAINT clave_primaria_compraTienda PRIMARY KEY (CDNI,CProducto))");

    // Se crea Tabla PEDIDOENVIO
    stmt.executeUpdate("CREATE TABLE Pedido_Envio(
                      	CPedido NUMBER CONSTRAINT Cpedido_no_nulo NOT NULL CONSTRAINT Cpedido_clave_primaria PRIMARY KEY,
                      	CProveedor NUMBER CONSTRAINT Cproveedor_clave_externa REFERENCES Proveedor(CProveedor))");

    // Se crea Tabla CONTIENE
    stmt.executeUpdate("CREATE TABLE Contiene(
                        CProducto NUMBER CONSTRAINT Cproducto_con_clave_externa REFERENCES ProductoActivo(CProducto),
                        CPedido NUMBER CONSTRAINT Cpedido_clave_externa REFERENCES Pedido_Envio(CPedido),
						Cantidad NUMBER,
                        CONSTRAINT clave_primaria_contiene PRIMARY KEY (CProducto,CPedido))");

    // Se crea Tabla COMPRAENTRADAACCESO
    stmt.executeUpdate("CREATE TABLE Compra_Entrada_Acceso(
                        	CDNI VARCHAR2(9) CONSTRAINT Cdni_ent_acc_clave_externa REFERENCES Cliente(CDNI),
                          CEvento NUMBER CONSTRAINT Cevento_no_nulo NOT NULL Cevento_ent_acc_clave_externa REFERENCES Evento(CEvento),
                        	num_asiento NUMBER CONSTRAINT num_asiento_no_nulo NOT NULL CONSTRAINT num_asiento_clave_primaria PRIMARY KEY)");

    // Se crea Tabla EVENTO
    stmt.executeUpdate("CREATE TABLE Evento(CEvento NUMBER CONSTRAINT Cevento_no_nulo NOT NULL CONSTRAINT Cevento_clave_primaria PRIMARY KEY, fecha DATE)");

    // Se crea Tabla INCIDENCIASINFORME
    stmt.executeUpdate("CREATE TABLE Incidencias_Informe(
                        	CEvento NUMBER CONSTRAINT Cevento_clave_externa REFERENCES Evento(CEvento),
                        	CIncidencia NUMBER CONSTRAINT Cincidencia_no_nulo NOT NULL CONSTRAINT Cincidencia_clave_primaria PRIMARY KEY)");

    // Se crea Tabla CONTRATADA
    stmt.executeUpdate("CREATE TABLE Contratada(
                    	   CEvento NUMBER CONSTRAINT Cevento_contr_clave_externa REFERENCES Evento(CEvento),
                         CNombre VARCHAR2(40) CONSTRAINT Cnombre_clave_externa REFERENCES EmpresaExterna(CNombre),
                         CONSTRAINT clave_primaria_contratada PRIMARY KEY (CEvento,CNombre))");

    // Se crea Tabla ORGANIZA
    stmt.executeUpdate("CREATE TABLE Organiza(
                        	CEvento CONSTRAINT Cevento_clave_externa_Evento REFERENCES Evento(CEvento),
                        	CDNI_empleado CONSTRAINT Cdniempleado_Emp_clave_externa REFERENCES Empleado(CDNI_empleado),
                        	CONSTRAINT clave_primaria_Organiza PRIMARY KEY (CEvento,CDNI_empleado))");

    // Se crea Tabla ABONADO
    stmt.executeUpdate("CREATE TABLE Abonado(
                            	CDNI VARCHAR2(9) CONSTRAINT Cdni_ab_no_nulo NOT NULL CONSTRAINT Cdni_ab_clave_primaria PRIMARY KEY REFERENCES Cliente(CDNI),
                            	Nombre VARCHAR2(40) CONSTRAINT nombre_ab_no_nulo NOT NULL,
                            	Apellidos VARCHAR2(40) CONSTRAINT direccion_ab_no_nulo NOT NULL,
                            	Correo VARCHAR2(40) CONSTRAINT correo_ab_no_nulo NOT NULL,
                            	Telefono VARCHAR2(9) CONSTRAINT telefono_ab_no_nulo NOT NULL,
                              Anyo_Alta NUMBER CONSTRAINT num_abonado_no_nulo	NOT NULL)");

    // Se crea Tabla COMPONENTEACTIVOCONTRATO
    stmt.executeUpdate("CREATE TABLE ComponenteActivo_Contrato(
                            	CDNI VARCHAR2(9) CONSTRAINT Cdniempl_act_clave_primaria PRIMARY KEY CONSTRAINT Cdni_comp_clave_externa REFERENCES Componente(CDNI),
                            	CDNI_empleado VARCHAR2(9) CONSTRAINT Cdniempleado_no_nulo NOT NULL REFERENCES Empleado(CDNI_empleado)) ");

    // Se crea Tabla ABONADOACTIVOCONTRATAABONO
    stmt.executeUpdate("CREATE TABLE AbonadoActivo_Contrata_Abono(
                          	CDNI VARCHAR2(9) CONSTRAINT Cdni_ab_act_no_nulo NOT NULL CONSTRAINT Cdni_ab_act_clave_primaria PRIMARY KEY REFERENCES Abonado(CDNI),
                          	num_asiento NUMBER CONSTRAINT num_asiento_ab_no_nulo NOT NULL UNIQUE,
                            numero_abonado NUMBER CONSTRAINT num_abonado_ab_no_nulo NOT NULL 	)");

    // Se crea Tabla EXTRADEPORTIVO
    stmt.executeUpdate("CREATE TABLE Extradeportivo(CEvento NUMBER CONSTRAINT Cevento_ext_clave_primaria PRIMARY KEY CONSTRAINT Cevento_ext_clave_externa REFERENCES Evento(CEvento))");

    // Se crea Tabla PARTIDO
    stmt.executeUpdate("CREATE TABLE Partido(CEvento NUMBER CONSTRAINT Cevento_pa_clave_primaria PRIM
    stmt.executeUpdARY KEY CONSTRAINT Cevento_pa_clave_externa REFERENCES Evento(CEvento))");

    // Se crea Tabla ACCESOABONADO
    stmt.executeUpdate("CREATE TABLE AccesoAbonado(
                        	CEvento CONSTRAINT Cevento_acc_clave_externa REFERENCES Partido(CEvento),
                        	num_asiento NUMBER CONSTRAINT num_asiento_acc_no_nulo NOT NULL REFERENCES AbonadoActivo_Contrata_Abono(num_asiento),
                        	CONSTRAINT clave_primaria_AccesoAbonado PRIMARY KEY (CEvento,num_asiento))");

    connection.commit();
    System.out.println("Tablas creadas");

  }catch(SQLException e){
    System.err.format("SQL exception: %s\n%s\n", e.getSQLState(), e.getMessage());
  }
};




////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//
//              EQUIPO
//
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

 // ALTA COMPONENTE
  public altaComponente(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

      System.out.println("Introduzca el DNI del nuevo componente:");
      String dni = input.nextLine();


	  // si el DNI ya está en componente
      ResultSet salida = statement.executeQuery("SELECT DNI FROM Componente WHERE DNI=" +dni);
      if(salida.next()){
		  
		  //si el DNI ya está en componente activo
		  ResultSet salida2 = statement.executeQuery("SELECT DNI FROM ComponenteActivo_Contrato WHERE DNI=" +dni);
		  if(salida2.next()){
			  System.out.println("Ya está registrado como componente activo.");
		  }
		  else{
			  System.out.println("Introduzca el DNI del empleado que lo contrata:");
      		  String dni_empleado = input.nextLine();
			  statement.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES("+ dni +"," + dni_empleado +")");
		  }

      }
	  else{
		  System.out.println("Introduzca los siguientes datos sobre el nuevo componente");
		  System.out.println("Nombre: ");
      	  String nombre = input.nextLine();
	  	  System.out.println("Dirección:");
      	  String direccion = input.nextLine();
		  System.out.println("Salario:");
      	  String salario = input.nextLine();
	      System.out.println("correo:");
      	  String correo = input.nextLine();
		  System.out.println("Teléfono:");
      	  String telefono = input.nextLine();
		  System.out.println("Cláusula de rescisión:");
      	  String clausula = input.nextLine();
		  System.out.println("Fecha de inicio del contrato:");
      	  String fechainicio = input.nextLine();
		  System.out.println("Fecha de finalización del contrato:");
      	  String fechafin = input.nextLine();

		  statement.executeUpdate("INSERT INTO Componente VALUES("+ dni +"," + nombre +"," + direccion +","+ salario + ","+ correo+ "," + telefono+","+clausula+ ","+fechainicio+"," + fechafin+ ")");

		  System.out.println("Introduzca el DNI del empleado que lo contrata:");
      	  String dni_empleado = input.nextLine();
		  statement.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES("+ dni +"," + dni_empleado +")");
	  }

      connection.commit();

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };

 // BAJA COMPONENTE
  public bajaComponente(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);

	  System.out.println("Introduzca DNI del componente que va a dar de baja:");
      String dni = input.nextLine();

      statement.executeUpdate("DELETE FROM ComponenteActivo_Contrato WHERE DNI=" + dni );

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

  // RENOVAR COMPONENTE
  public renovarComponente(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);

	  //hay que comprobar que este en activo, si no está llamar a dar de alta

	  System.out.println("Introduzca DNI del componente que va a renovar su contrato:");
      String dni = input.nextLine();

	  //comprobar que la fecha es válida
	  System.out.println("Introduzca la nueva fecha en la que finalizará su contrato:");
      String fechafin = input.nextLine();

	  System.out.println("Introduzca su nuevo salario (-1 si mantiene el mismo):");
      String salario = input.nextLine();

	  System.out.println("Introduzca su nueva cláusula de rescisión (-1 si mantiene la misma):");
      String clausula = input.nextLine();

	  if(salario.matches("-1")){
		  if(clausula.matches("-1")){
			   statement.executeUpdate("UPDATE Componente SET FechaFin="+fechafin+" WHERE DNI="+dni);
		  }
		  else{
			   statement.executeUpdate("UPDATE Componente SET Clausula_rescision="+clausula+", FechaFin="+fechafin+" WHERE DNI="+dni);
		  }
	  }
	  else{
		  if(clausula.matches("-1")){
			  statement.executeUpdate("UPDATE Componente SET Salario="+salario+", FechaFin="+fechafin+" WHERE DNI="+dni);
		  }
		  else{
			  statement.executeUpdate("UPDATE Componente SET Salario= "+salario+", Clausula_rescision="+clausula+", FechaFin="+fechafin+" WHERE DNI="+dni);
		  }
	  }

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


// LISTAR COMPONENTES

  public listarComponentes(Statement statement) throws SQLException{
    try{
      // producto cartesiano con componente
      ResultSet componentes = statement.executeQuery("SELECT * FROM ComponenteActivo_Contrato ");
      while(inventario.next()){
           System.out.println(inventario.getString(1)+" "+inventario.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


// BALANCE DE CUENTAS
public balanceCuentas(Statement statement) throws SQLException{
    try{
      // LISTAR
      ResultSet componentes = statement.executeQuery("SELECT * FROM ComponenteActivo_Contrato ");
      while(inventario.next()){
           System.out.println(inventario.getString(1)+" "+inventario.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

	public void menuEquipo() throws SQLException{
		try{

			Scanner input = new Scanner(System.in);
			Statement statement = connection.createStatement();
			int opcion;

			boolean terminar = false;

			// SAVEPOINT
			Savepoint CheckPoint = connection.setSavepoint("SinDetalles");


			while (!terminar){
				System.out.println("\nIntroduzca una opcion del 1 al 6:");
				System.out.println("\t1-Dar de alta componente.");
				System.out.println("\t2-Finalizar contrato.");
				System.out.println("\t3-Renovar contrato.");
        System.out.println("\t4-Listar componentes.");
        System.out.println("\t5-Balance de cuentas.");
        System.out.println("\t6-Volver al menú principal.");

				opcion = input.nextInt();

				switch(opcion){
					case 1:
					    altaComponente(statement);
					break;

					case 2:
					    bajaComponente(statement);
					break;

					case 3:
					    renovarComponente(statement);
					break;

					case 4:
					    listarComponentes(statement);
					break;

					case 5:
					    balanceCuentas(statement);
					break;

					case 6:
					    terminar=true;
					break;
				}
			}

			connection.commit();
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	};




  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  //
  //              EVENTOS
  //
  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  // COMPRAR ENTRADAS
  public comprarEntradas(Statement statement) throws SQLException{
    try{
			Scanner input = new Scanner(System.in);

      // LISTAR Eventos
      ResultSet listadoeventos = statement.executeQuery("SELECT * FROM EVENTOS ");
      while(listadoeventos.next()){
           System.out.println(listadoeventos.getString(1)+" "+listadoeventos.getString(2));
      }

			System.out.println("Seleccione un evento:");
      String cevento = input.nextLine();

      // Listar asientos de cevento
      ResultSet salida = statement.executeQuery("SELECT num_asiento FROM ProductoActivo WHERE CEvento=" + cevento );
      while(salida.next()){
			     System.out.println(salida.getString(1));
			}

			System.out.println("Introduzca el número de asiento:");
			String asiento = input.nextLine();

      statement.executeQuery("DELETE FROM ProductoActivo WHERE num_asiento=" + asiento );

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };




  // CONSULTAR PARTIDOS
  public consultarProximosPartidos(Statement statement) throws SQLException{
    try{
      // LISTAR
      ResultSet listadopartidos = statement.executeQuery("SELECT * FROM PARTIDO ");
      while(listadopartidos.next()){
           System.out.println(listadopartidos.getString(1)+" "+listadopartidos.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };



  // CONSULTAR EXTRADEPORTIVOS
  public consultarProximosExtradeportivos(Statement statement) throws SQLException{
    try{
      // LISTAR
      ResultSet listadoextradeportivos = statement.executeQuery("SELECT * FROM EXTRADEPORTIVO ");
      while(listadoextradeportivos.next()){
           System.out.println(listadoextradeportivos.getString(1)+" "+listadoextradeportivos.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };



  // EMPRESAS EXTERNAS
  public empresasExternas(Statement statement) throws SQLException{
    try{
      // LISTAR
      ResultSet empresas = statement.executeQuery("SELECT * FROM EmpresaExterna ");
      while(empresas.next()){
           System.out.println(empresas.getString(1));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


  // Registrar Incidencia
  public registrarIncidencia(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

      // LISTAR Eventos
      ResultSet listadoeventos = statement.executeQuery("SELECT * FROM EVENTOS ");
      while(listadoeventos.next()){
           System.out.println(listadoeventos.getString(1)+" "+listadoeventos.getString(2));
      }

      System.out.println("Seleccione un evento sobre el que quieras registrar la incidencia:");
      String cevento = input.nextLine();

      // Listar asientos de cevento
      ResultSet salida = statement.executeQuery("SELECT CIncidencia FROM Incidencias_Informe WHERE CEvento=" + cevento );
      system.out.println("Las incidencias registradas hasta ahora son:");
      while(salida.next()){
           System.out.println(salida.getString(1));
      }

      System.out.println("Introduzca la nueva incidencia");
      String incidencia = input.nextLine();

      statement.executeUpdate("INSERT INTO Incidencias_Informe VALUES("+ CEvento +"," + incidencia + ")");

      connection.commit();

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };




  // MENU
  public void menuEventos() throws SQLException{
    try{

      Scanner input = new Scanner(System.in);
      Statement statement = connection.createStatement();
      int opcion;

      boolean terminar = false;

      // SAVEPOINT
      Savepoint CheckPoint = connection.setSavepoint("SinDetalles");


      while (!terminar){
        System.out.println("\nIntroduzca una opcion del 1 al 6:");
        System.out.println("\t1-Comprar entrada.");
        System.out.println("\t2-Consultar próximos partidos.");
        System.out.println("\t3-Consultar próximos eventos-extradeportivos.");
        System.out.println("\t4-Empresas externas.");
        System.out.println("\t5-Registrar informe del evento.");
        System.out.println("\t6-Volver al menú principal.");

        opcion = input.nextInt();

        switch(opcion){
          case 1:
              comprarEntradas(statement);
          break;

          case 2:
              consultarProximosPartidos(statement);
          break;

          case 3:
              consultarProximosExtradeportivos(statement);
          break;

          case 4:
              terminar=true;
          break;
        }
      }

      connection.commit();
    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };



  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  //
  //              ABONADOS
  //
  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////

// Mostrar asientos ocupados
	public mostrarAsientosOcupadosAbono(Statement statement) throws SQLException{
		try{
      // LISTAR
			System.out.println("ASIENTOS OCUPADOS PARA LA TEMPORADA - SECCIÓN ABONADO.")
      ResultSet asientos = statement.executeQuery("SELECT num_asiento FROM AbonadoActivo_Contrata_Abono ");
      while(asientos.next()){
           System.out.print("ASIENTO: " + asientos.getString(1)+ "\t ");
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}


	/*
	DAR DE ALTA ABONADO
		Crear una ficha nueva de abonado no registrado actualmente en el sistema y que
		haya aportado la cantidad de 150€. El abonado proporciona al sistema un correo
		electrónico, nombre, apellidos, año de alta, dni, teléfono y asiento. Hay que
		asegurarse de que el asiento se encuentra disponible. El dni no puede estar
		repetido. Se almacena en la base de datos los datos personales del abonado y
		se devuelve en la salida el número de abonado correspondiente. El número de
		abonados aumenta en uno. Como salida se proporciona su número de abonado
		correspondiente (es único).
	*/
	public altaAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);
			ResultSet asientos;
			boolean ya_era_abonado;

			// Datos del abonado
			System.out.println("Vamos a proceder a solicitar sus datos para el abono. Introduzca ");
			do{
				System.out.print("DNI (formato 11111111X) :");
				String DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			// Comprobamos si ya había sido abonado. En ese caso solo habría que meterlo
			// como abonado abonado activo
			ResultSet abonadoantiguo = statement.executeQuery("SELECT * FROM Abonado where CDNI="+DNI);
			ya_era_abonado = abonadoantiguo.next();

			if(!ya_era_abonado){
				// Si no fue abonado, le pedimos los nuevos datos
				do{
					System.out.print("Nombre :");
					String nombre = input.nextLine();
				}while(!nombre.matches("[a-zA-Z]+"));

				do{
					System.out.print("Apellidos :");
					String apellidos = input.nextLine();
				}while(!apellidos.matches("[a-zA-Z]+"));

				do{
					System.out.print("Correo (debe contener  @domino):");
					String correo = input.nextLine();
				}while(!correo.matches("[a-z]+@[a-z]+")

				do{
					System.out.print("Teléfono :");
					String telefono = input.nextLine();
				}while(!telefono.matches("^([\+]\d\d)?[0-9]{9}}$"));
			}



			System.out.println("Seleccione un asiento (1-21000).");
			String asientodeseado = input.nextLine();

			asientos = statement.executeQuery("SELECT * FROM AbonadoActivo_Contrata_Abono WHERE num_asiento="+asientodeseado);

			// Si no se ha insertado un asiento válido
			while(!asientos.next() and asientodeseado>0 and asientodeseado<=CAPACIDAD_ESTADIO){
				System.out.println("El asiento " + asientodeseado + " no está disponible. Los asientos ocupados son:")
				mostrarAsientosOcupadosAbono();
				System.out.println("Por favor, seleccione otro asiento.");
				asientodeseado = input.nextLine();

				asientos = statement.executeQuery("SELECT * FROM AbonadoActivo_Contrata_Abono WHERE num_asiento="+asientodeseado);
			}

			Date dt = new Date();
			Integer aux = new Integer(dt.getYear()+1900);
			String anio_alta = y.toString();
			// Añadimos/Modificamos en la tabla ABONADO
			if(ya_era_abonado){
				statement.executeUpdate("UPDATE Abonado SET Anyo_Alta=NOW() WHERE CDNI="+DNI);
				//statement.executeUpdate("UPDATE Abonado SET Anyo_Alta="+anio_alta+" WHERE CDNI="+DNI);
			}else{
				statement.executeUpdate("INSERT INTO Abonado VALUES("+DNI+","+nombre+","+apellidos+","+correo+","+telefono+",NOW()")
				//statement.executeUpdate("INSERT INTO Abonado VALUES("+DNI+","+nombre+","+apellidos+","+correo+","+telefono+","+anio_alta")")
			}

			ResultSet nuevonumabonoRS = statement.executeQuery("SELECT count(*) FROM Abonado");
			nuevonumabonoRS.next();
			String nuevo_num_abono = String.valueOf(Integer.parseInt(nuevonumabonoRS.getString(1))+1)


			// Añadimos en la tabla AbonadoActivo_Contrata_Abono
			statement.executeUpdate("INSERT INTO AbonadoActivo_Contrata_Abono VALUES("+DNI+","+asientos.getString(2)+","+nuevo_num_abono")")

			// Añadimos a la tabla de Acceso abonado
			ResultSet partidos = statement.executeQuery("SELECT Partido.CEvento FROM Partido,Evento WHERE Partido.CEvento=Evento.CEvento AND fecha > NOW() ");
			while(partidos.next()){
				statement.executeUpdate("INSERT INTO AccesoAbonado VALUES("+partidos.getString(1)+","asientos.getString(2));
			}

			System.out.println("Felicidades "+nombre+ ". Ya eres nuevo abonado de los Serrano! Tu asiento es el "+asientos.getString(2)+" y su NºAbonado "+nuevo_num_abono);

			// Sumar al balance 150€
		}catch(SQLException){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}




	/*
	DAR DE BAJA ABONADO
		El sistema elimina la ficha del abonado cuyo número de abonado se indica en
		la entrada y deja libre su asiento correspondiente
	*/
	public bajaAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);
			boolean ya_era_abonado;

			// Datos del abonado
			System.out.println("Vamos a proceder a solicitar sus datos para el abono. Introduzca ");
			do{
				System.out.print("DNI (formato 11111111X) :");
				String DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			ResultSet abonadosActivos = statement.executeQuery("SELECT num_asiento FROM AbonadoActivo_Contrata_Abono WHERE CDNI="+DNI);
			if(abonadosActivos.next()){
				String asiento = abonadosActivos.getString(1);
				statement.executeUpdate("DELETE FROM AccesoAbonado WHERE num_asiento="+asiento);
				statement.executeUpdate("DELETE FROM AbonadoActivo_Contrata_Abono WHERE CDNI="+DNI);
				System.out.println("Se ha dado de baja correctamente al antiguo abonado "+ DNI);
			}else{
				System.out.println("No existe ningún abonado activo con DNI "+DNI);
			}


		}catch(SQLException){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}


	/*
	MOSTRAR INFORMACIÓN ABONADO
		Muestra la información de un abonado
	*/
	public mostrarInfoAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);
			System.out.println("Escriba el DNI del abonado del que quiera conocer los datos ");
			String DNI;
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));


			ResultSet abonados = statement.executeQuery("SELECT Abonado.CDNI, Nombre, Apellidos, Correo, Telefono, Anyo_Alta, numero_abonado FROM  Abonado, AbonadoActivo_Contrata_Abono WHERE Abonado.CDNI=AbonadoActivo_Contrata_Abono.CDNI AND Abonado.CDNI="+DNI);
			if(abonados.next()){
				String DNI = abonados.getString(1);
				String nombre = abonados.getString(2);
				String apellidos = abonados.getString(3);
				String correo = abonados.getString(4);
				String telefono = abonados.getString(5);
				String anio_alta = abonados.getString(6);
				String num_abono = abonados.getString(7);

				System.out.println("\nINFORMACIÓN PERSONAL DE ABONADO");
				System.out.println("NºAbono: "+num_abono);
				System.out.println("Apellidos, Nombre: "+apellidos ", "+nombre);
				System.out.println("DNI: "+DNI);
				System.out.println("Correo: "+correo);
				System.out.println("Teléfono: "+telefono);
				System.out.println("Abono dado de alta en "+anio_alta+"\n");
			}else{
				System.out.println("No existe ningún abonado activo con DNI "+DNI);
			}
		}catch(SQLException){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}


	/*
	MOSTRAR LISTADO DE ABONADOS
		Muestra la lista de todos los abonados
	*/
	public listadoAbonados(Statement statement) throws SQLException{
		try{
			ResultSet abonados = statement.executeQuery("SELECT Abonado.CDNI, Nombre, Apellidos numero_abonado FROM  Abonado, AbonadoActivo_Contrata_Abono WHERE Abonado.CDNI=AbonadoActivo_Contrata_Abono.CDNI");
			System.out.println("\nABONADOS");
			while(abonados.next()){
				String DNI = abonados.getString(1);
				String nombre = abonados.getString(2);
				String apellidos = abonados.getString(3);
				String num_abono = abonados.getString(4);
				System.out.println("Nombre (NºAbono - DNI): "+apellidos+","+nombre+" ("+num_abono+"-"+DNI+")");
			}
		}catch(SQLException){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}



  public void menuAbonados() throws SQLException{
    try{

      Scanner input = new Scanner(System.in);
      Statement statement = connection.createStatement();
      int opcion;

      boolean terminar = false;

      // SAVEPOINT
      Savepoint CheckPoint = connection.setSavepoint("SinDetalles");


      while (!terminar){
        System.out.println("\nIntroduzca una opcion del 1 al 7:");
        System.out.println("\t1-Dar de alta abonado.");
        System.out.println("\t2-Dar de baja abonado.");
        System.out.println("\t3-Mostrar información de un abonado.");
        System.out.println("\t4-Mostrar listado de abonados.");
        System.out.println("\t5-Envío noticias ( PREGUNTAR PROFESOR ).");
        System.out.println("\t6-Mostrar asientos ocupados.");
        System.out.println("\t7-Volver al menú principal.");

        opcion = input.nextInt();

        switch(opcion){
          case 1:
              altaAbonado(statement);
          break;

          case 2:
              bajaAbonado(statement);
          break;

          case 3:
			  mostrarInfoAbonado(statement);
          break;

          case 4:
			  listadoAbonados(statement);

          break;

		  case 5:

		  break;
          case 6:
			  mostrarAsientosOcupadosAbono(statement);
		  break;
        }
      }

      connection.commit();
    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };




  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  //
  //              TIENDA
  //
  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////


  // VENDER ITEMS
  public venderItems(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);

      // LISTAR productos
      ResultSet productos = statement.executeQuery("SELECT * FROM ProductoActivo ");
      while(productos.next()){
           System.out.println(productos.getString(1)+" "+productos.getString(2)+" "+productos.getString(3));
      }

	  System.out.println("Seleccione el producto que se ha vendido:");
      String cproducto = input.nextLine();

	  System.out.println("Seleccione cuántos ítems se han vendido:");
      String vendidos = input.nextLine();

      ResultSet cantidad= statement.executeQuery("SELECT Cantidad FROM ProductoActivo WHERE CProducto=" + cproducto );
	  cantidad.next();
	  String ccantidad = cantidad.getString(1);
	  
	  statement.executeUpdate("UPDATE ProductoActivo SET Cantidad=" +String.valueOf(Integer.parseInt(ccantidad)-Integer.parseInt(vendidos)) + " WHERE CProducto=" + cproducto );  

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

   // REALIZAR PEDIDO
  public realizarPedido(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

      System.out.println("Introduzca el código asociado al nuevo pedido:");
      String cpedido = input.nextLine();

      // el cpedido no puede estar ya registrado
      ResultSet salida = statement.executeQuery("SELECT CPedido FROM Pedido_Envio WHERE cpedido=" +CPedido);
      while(salida.next()){
			   System.out.println("Ya existe un pedido con dicho código, introduzca uno nuevo: ");
      		   String cproducto = input.nextLine();
			   salida = statement.executeQuery("SELECT CPedido FROM Pedido_Envio WHERE cpedido=" +CPedido); 
      }

      System.out.println("Introduzca el proveedor al que se realizar el pedido:");
      String cproveedor = input.nextLine();

	  statement.executeUpdate("INSERT INTO Pedido_Envio VALUES("+ cpedido +"," + cproveedor +")");

	  boolean anadir_producto = true;
	  int contador=0;

	  while(anadir_producto){

		System.out.println("Introduzca el código del producto que se quiere pedir (-1 si quiere dejar de añadir productos):");
		String cproducto = input.nextLine();


		if( (cproducto.matches("-1")) and (contador>0) ){
			anadir_producto = false;
		}
		else{
			System.out.println("Introduzca la cantidad del producto que se quiere pedir:");
			String pedir_cantidad = input.nextLine();
        	statement.executeUpdate("INSERT INTO Contiene VALUES("+ cproducto +"," + cpedido + ","+ pedir_cantidad +")");
			contador++;
		}

	  }

      connection.commit();

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };


   // ALTA PRODUCTO 
  public altaProducto(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

      System.out.println("Introduzca el código asociado al nuevo producto:");
      String cproducto = input.nextLine();

      // el cproducto no puede estar ya registrado
      ResultSet salida = statement.executeQuery("SELECT CProducto FROM Producto WHERE cproducto=" +CProducto);
      while(salida.next()){
			   System.out.println("Ya existe un producto con dicho código, introduzca uno nuevo: ");
      		   String cproducto = input.nextLine();
			   salida = statement.executeQuery("SELECT CProducto FROM Producto WHERE cproducto=" +CProducto);  
		  }
      }

      System.out.println("Introduzca la cantidad disponible del nuevo producto:");
      String cantidad = input.nextLine();

	  System.out.println("Introduzca el precio inicial del nuevo producto:");
      String precio = input.nextLine();

	  statement.executeUpdate("INSERT INTO Producto VALUES("+ cproducto +")");

      statement.executeUpdate("INSERT INTO ProductoActivo VALUES("+ cproducto +"," + cantidad +"," + precio + ")");

      connection.commit();

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };

  // BAJA PRODUCTO
  public bajaProducto(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);

      // LISTAR productos
      ResultSet productos = statement.executeQuery("SELECT * FROM ProductoActivo ");
      while(productos.next()){
           System.out.println(productos.getString(1)+" "+productos.getString(2)+" "+productos.getString(3));
      }

	  System.out.println("Seleccione el producto que va a dejar de ser activo:");
      String cproducto = input.nextLine();

      statement.executeUpdate("DELETE FROM ProductoActivo WHERE CProducto=" + cproducto );

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


  // MOSTRAR INVENTARIO
  public mostrarInventario(Statement statement) throws SQLException{
    try{
      // LISTAR
      ResultSet inventario = statement.executeQuery("SELECT * FROM ProductoActivo ");
      while(inventario.next()){
           System.out.println(inventario.getString(1)+" "+inventario.getString(2)+" "+inventario.getString(3));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

   // RECIBIR PEDIDO
 public recibirPedido(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);

      // LISTAR pedidos por recibir
      ResultSet pedidos = statement.executeQuery("SELECT * FROM Pedido_Envio ");
      while(productos.next()){
           System.out.println(productos.getString(1)+" "+productos.getString(2));
      }

	  System.out.println("Seleccione el pedido que se ha recibido:");
      String cpedido = input.nextLine();

	  ResultSet producto= statement.executeQuery("SELECT * FROM Contiene WHERE CPedido=" + cpedido );
	  while(producto.next()){
		String cantidad_recibida = producto.getString(3);
		String cproducto = producto.getString(1);

		ResultSet productoactivo = statement.executeQuery("SELECT Cantidad FROM ProductoActivo WHERE CProducto=" + cproducto );
		productoactivo.next();
		String cantidad_antigua = productoactivo.getString(1);
		
		statement.executeUpdate("UPDATE ProductoActivo SET Cantidad=" +String.valueOf(Integer.parseInt(cantidad_recibida)+Integer.parseInt(cantidad_antigua)) + " WHERE CProducto=" + cproducto ); 
	  } 

	  statement.executeUpdate("DELETE FROM Contiene WHERE CPedido=" + cpedido );
      statement.executeUpdate("DELETE FROM Pedido_Envio WHERE CPedido=" + cpedido );

      connection.commit();

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


  public void menuTienda() throws SQLException{
    try{

      Scanner input = new Scanner(System.in);
      Statement statement = connection.createStatement();
      int opcion;

      boolean terminar = false;

      // SAVEPOINT
      Savepoint CheckPoint = connection.setSavepoint("SinDetalles");


      while (!terminar){
        System.out.println("\nIntroduzca una opcion del 1 al 7:");
        System.out.println("\t1-Vender ítems.");
        System.out.println("\t2-Realizar pedido.");
        System.out.println("\t3-Dar de alta un producto.");
        System.out.println("\t4-Dar de baja un producto.");
        System.out.println("\t5-Mostrar inventario.");
        System.out.println("\t6-Recibir pedido.");
        System.out.println("\t7-Volver al menú principal.");

        opcion = input.nextInt();

        switch(opcion){
          case 1:
              venderItems(statement);
          break;

          case 2:
              realizarPedido(statement);
          break;

          case 3:
              altaProducto(statement);
          break;

          case 4:
              bajaProducto(statement);
          break;

		  case 5:
		  	  mostrarInventario(statement);
		  break;

		  case 6:
		  	  recibirPedido(statement);
		  break;

		  case 7:
		      terminar=true;
		  break;

		  default:
		      System.out.println("Esta opción no es válida. Por favor, introduzca un número del 1 al 7");
		  break;
			
        }
      }

      connection.commit();
    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };

}
