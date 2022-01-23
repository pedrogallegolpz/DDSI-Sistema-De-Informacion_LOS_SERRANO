package SISTEMA;



import java.sql.*;
import java.util.*;

import javax.sound.sampled.SourceDataLine;

import java.util.Date;


import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Period;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;


public class Serrano {
	private String URL;
	private String Username;
	private String Password;
	private Connection connection;
	private int CAPACIDAD_ESTADIO = 21000;

	public Serrano(String URL, String Username, String Password) {
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

	public boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	};

public void crearTablas() throws SQLException {
  try{
    Statement stmt = connection.createStatement();

    // Se crea Tabla PRODUCTO
		try{
    	stmt.executeUpdate("CREATE TABLE Producto(CProducto NUMBER CONSTRAINT CProducto_no_nulo NOT NULL CONSTRAINT CProducto_clave_primaria PRIMARY KEY)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla PROVEEDOR
		try{
    	stmt.executeUpdate("CREATE TABLE Proveedor(CProveedor NUMBER CONSTRAINT Cproveedor_no_nulo NOT NULL CONSTRAINT Cproveedor_clave_primaria PRIMARY KEY,Contacto VARCHAR2(40))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla CLIENTE
		try{
    	stmt.executeUpdate("CREATE TABLE Cliente(	CDNI VARCHAR2(9) CONSTRAINT Cdni_no_nulo NOT NULL CONSTRAINT Cdni_clave_primaria PRIMARY KEY) ");
		}catch(SQLException e){
	  	System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
	  }

    // Se crea Tabla EMPRESAEXTERNA
		try{
    	stmt.executeUpdate("CREATE TABLE EmpresaExterna(CNombre VARCHAR2(40) CONSTRAINT Cnombre_no_nulo NOT NULL CONSTRAINT Cnombre_clave_primaria PRIMARY KEY,  Correo VARCHAR2(40) CONSTRAINT correo_empresa_no_nulo NOT NULL)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla EMPLEADO
		try{
    	stmt.executeUpdate("CREATE TABLE Empleado( CDNI_empleado VARCHAR2(9) CONSTRAINT Cdniempleado_clave_primaria PRIMARY KEY )");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla COMPONENTE
		try{
    	stmt.executeUpdate("CREATE TABLE Componente(CDNI VARCHAR2(9) CONSTRAINT Cdni_componente_no_nulo NOT NULL CONSTRAINT Cdni_componente_clave_primaria PRIMARY KEY, Nombre VARCHAR2(40) CONSTRAINT nombre_no_nulo NOT NULL, Direccion VARCHAR2(40) CONSTRAINT direccion_no_nulo NOT NULL, Salario NUMBER CONSTRAINT salario_no_nulo NOT NULL,	Correo VARCHAR2(40) CONSTRAINT correo_no_nulo NOT NULL, Telefono VARCHAR2(9) CONSTRAINT telefono_no_nulo NOT NULL, Clausula_rescision NUMBER CONSTRAINT clausula_no_nulo NOT NULL, FechaInicio DATE, FechaFin DATE)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla PRODUCTOACTIVO
		try{
    	stmt.executeUpdate("CREATE TABLE ProductoActivo(CProducto NUMBER CONSTRAINT Cproducto_clave_externa REFERENCES Producto(CProducto) CONSTRAINT Cproducto_act_clave_primaria PRIMARY KEY, Cantidad NUMBER CONSTRAINT cantidad_prodact_no_nulo NOT NULL, Precio NUMBER CONSTRAINT precio_prodac_no_nulo NOT NULL)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla COMPRATIENDA
		try{
    	stmt.executeUpdate("CREATE TABLE CompraTienda( CDNI VARCHAR2(9) CONSTRAINT Cdni_clave_externa REFERENCES Cliente(CDNI), CProducto NUMBER CONSTRAINT Cproducto_CT_clave_externa REFERENCES Producto(CProducto), Cantidad NUMBER, CONSTRAINT clave_primaria_compraTienda PRIMARY KEY (CDNI,CProducto))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla PEDIDOENVIO
		try{
    	stmt.executeUpdate("CREATE TABLE Pedido_Envio( CPedido NUMBER CONSTRAINT Cpedido_no_nulo NOT NULL CONSTRAINT Cpedido_clave_primaria PRIMARY KEY, CProveedor NUMBER CONSTRAINT Cproveedor_clave_externa REFERENCES Proveedor(CProveedor))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla CONTIENE
		try{
    	stmt.executeUpdate("CREATE TABLE Contiene( CProducto NUMBER CONSTRAINT Cproducto_con_clave_externa REFERENCES ProductoActivo(CProducto), CPedido NUMBER CONSTRAINT Cpedido_clave_externa REFERENCES Pedido_Envio(CPedido), Cantidad NUMBER, CONSTRAINT clave_primaria_contiene PRIMARY KEY (CProducto,CPedido))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


    // Se crea Tabla EVENTO
		try{
    	stmt.executeUpdate("CREATE TABLE Evento(CEvento NUMBER CONSTRAINT Cevento_no_nulo NOT NULL CONSTRAINT Cevento_clave_primaria PRIMARY KEY, fecha DATE)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


    // Se crea Tabla COMPRAENTRADAACCESO
		try{
    	stmt.executeUpdate("CREATE TABLE Compra_Entrada_Acceso(CDNI VARCHAR2(9) CONSTRAINT Cdni_ent_acc_clave_extern REFERENCES Cliente(CDNI),CEvento NUMBER CONSTRAINT Cevento_no_nul NOT NULL CONSTRAINT Cevento_ent_acc_clave_e REFERENCES Evento(CEvento),num_asiento NUMBER CONSTRAINT num_asiento_no_nul NOT NULL, CONSTRAINT asiento_cevent_clave_primar PRIMARY KEY (num_asiento, CEvento))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla INCIDENCIASINFORME
		try{
    	stmt.executeUpdate("CREATE TABLE Incidencias_Informe( CEvento NUMBER CONSTRAINT Cevento_clave_externa REFERENCES Evento(CEvento), CIncidencia VARCHAR2(100) CONSTRAINT Cincidencia_no_nulo NOT NULL, CONSTRAINT Cincidencia_clave_primaria PRIMARY KEY (CEvento, CIncidencia))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla CONTRATADA
		try{
    	stmt.executeUpdate("CREATE TABLE Contratada( CEvento NUMBER CONSTRAINT Cevento_contr_clave_externa REFERENCES Evento(CEvento), CNombre VARCHAR2(40) CONSTRAINT Cnombre_clave_externa REFERENCES EmpresaExterna(CNombre), CONSTRAINT clave_primaria_contratada PRIMARY KEY (CEvento,CNombre))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla ORGANIZA
		try{
    	stmt.executeUpdate("CREATE TABLE Organiza( CEvento CONSTRAINT Cevento_clave_externa_Evento REFERENCES Evento(CEvento), CDNI_empleado CONSTRAINT Cdniempleado_Emp_clave_externa REFERENCES Empleado(CDNI_empleado), CONSTRAINT clave_primaria_Organiza PRIMARY KEY (CEvento,CDNI_empleado))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla ABONADO
		try{
    	stmt.executeUpdate("CREATE TABLE Abonado( CDNI VARCHAR2(9) CONSTRAINT Cdni_ab_no_nulo NOT NULL CONSTRAINT Cdni_ab_clave_primaria PRIMARY KEY REFERENCES Cliente(CDNI), Nombre VARCHAR2(40) CONSTRAINT nombre_ab_no_nulo NOT NULL, Apellidos VARCHAR2(40) CONSTRAINT direccion_ab_no_nulo NOT NULL, Correo VARCHAR2(40) CONSTRAINT correo_ab_no_nulo NOT NULL, Telefono VARCHAR2(9) CONSTRAINT telefono_ab_no_nulo NOT NULL, Anyo_Alta NUMBER CONSTRAINT num_abonado_no_nulo	NOT NULL)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla COMPONENTEACTIVOCONTRATO
		try{
    	stmt.executeUpdate("CREATE TABLE ComponenteActivo_Contrato( CDNI VARCHAR2(9) CONSTRAINT Cdniempl_act_clave_primaria PRIMARY KEY CONSTRAINT Cdni_comp_clave_externa REFERENCES Componente(CDNI), CDNI_empleado VARCHAR2(9) CONSTRAINT Cdniempleado_no_nulo NOT NULL REFERENCES Empleado(CDNI_empleado)) ");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla ABONADOACTIVOCONTRATAABONO
		try{
    	stmt.executeUpdate("CREATE TABLE AbonadoActivo_Contrata_Abono( CDNI VARCHAR2(9) CONSTRAINT Cdni_ab_act_no_nulo NOT NULL CONSTRAINT Cdni_ab_act_clave_primaria PRIMARY KEY REFERENCES Abonado(CDNI), num_asiento NUMBER CONSTRAINT num_asiento_ab_no_nulo NOT NULL UNIQUE, numero_abonado NUMBER CONSTRAINT num_abonado_ab_no_nulo NOT NULL 	)");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla EXTRADEPORTIVO
		try{
    	stmt.executeUpdate("CREATE TABLE Extradeportivo(CEvento NUMBER CONSTRAINT Cevento_ext_clave_primaria PRIMARY KEY CONSTRAINT Cevento_ext_clave_externa REFERENCES Evento(CEvento))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla PARTIDO
		try{
    	stmt.executeUpdate("CREATE TABLE Partido(CEvento NUMBER CONSTRAINT Cevento_pa_clave_primaria PRIMARY KEY CONSTRAINT Cevento_pa_clave_externa REFERENCES Evento(CEvento))");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

    // Se crea Tabla ACCESOABONADO
		try{
    	stmt.executeUpdate("CREATE TABLE AccesoAbonado( CEvento CONSTRAINT Cevento_acc_clave_externa REFERENCES Partido(CEvento), num_asiento NUMBER CONSTRAINT num_asiento_acc_no_nulo NOT NULL REFERENCES AbonadoActivo_Contrata_Abono(num_asiento), CONSTRAINT clave_primaria_AccesoAbonado PRIMARY KEY (CEvento,num_asiento))");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		try{
			stmt.executeUpdate("create sequence sec_abonado start with 1 increment by 1 maxvalue 999999 minvalue 1");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		try{
			stmt.executeUpdate("create sequence sec_codigoenvio start with 1 increment by 1 maxvalue 999999 minvalue 1");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER actualizarcantidadVentaItems AFTER INSERT ON CompraTienda FOR EACH ROW BEGIN UPDATE ProductoActivo SET Cantidad=Cantidad-:new.Cantidad  WHERE CProducto=:new.CProducto; END;");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER anadir_cliente_compratienda BEFORE INSERT ON CompraTienda FOR EACH ROW BEGIN INSERT INTO Cliente VALUES(:new.CDNI); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_OUTPUT.PUT_LINE('INSERCIÓN FALLIDA.1'); END;");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }



		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER anadir_cliente_compraentrada BEFORE INSERT ON Compra_Entrada_Acceso FOR EACH ROW BEGIN INSERT INTO Cliente VALUES(:new.CDNI); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_OUTPUT.PUT_LINE('INSERCIÓN FALLIDA.2'); END;");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER anadir_cliente_abonado BEFORE INSERT ON Abonado FOR EACH ROW BEGIN INSERT INTO Cliente VALUES(:new.CDNI); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_OUTPUT.PUT_LINE('INSERCIÓN FALLIDA.3'); END;");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


		/* try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER insertar_componente AFTER INSERT ON Componente FOR EACH ROW BEGIN INSERT INTO ComponenteActivo_Contrato VALUES(:new.CDNI,'76543123W'); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_OUTPUT.PUT_LINE('INSERCIÓN FALLIDA.4'); END;");
		}catch(SQLException e){
	System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
	} */

		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER insertar_abonado BEFORE INSERT ON AbonadoActivo_Contrata_Abono FOR EACH ROW DECLARE asiento_ocupado INTEGER; BEGIN SELECT COUNT(*) INTO asiento_ocupado FROM AbonadoActivo_Contrata_Abono WHERE num_asiento= :new.num_asiento; IF(asiento_ocupado!=0) THEN RAISE_APPLICATION_ERROR(-20001,'No se puede asignar dicho asiento al abonado.'); END IF; END;");
		}catch(SQLException e){
	System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
	}

		try{
			stmt.executeUpdate("CREATE OR REPLACE TRIGGER nuevo_pedido BEFORE INSERT ON Pedido_Envio FOR EACH ROW DECLARE encontrado INTEGER; BEGIN SELECT COUNT(*) INTO encontrado FROM Pedido_Envio WHERE CProveedor = :new.CProveedor; IF(encontrado!=0) THEN RAISE_APPLICATION_ERROR(-20002,'No se puede realizar otro pedido a dicho proveedor hasta que llegue el pedido actual.'); END IF; END;");
		}catch(SQLException e){
	System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
	}

	try{
		stmt.executeUpdate("CREATE OR REPLACE TRIGGER anadir_producto BEFORE INSERT ON ProductoActivo FOR EACH ROW BEGIN INSERT INTO Producto VALUES(:new.CProducto); EXCEPTION WHEN DUP_VAL_ON_INDEX THEN DBMS_OUTPUT.PUT_LINE('Ya era un producto (no activo)'); END;");
	}catch(SQLException e){
		System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
	}


    connection.commit();
    System.out.println("Tablas y disparadores creados (66% reset...)");

  }catch(SQLException e){
    System.err.format("SQL exception: %s\n%s\n", e.getSQLState(), e.getMessage());
  }
};


public void inicializar(){
	try{
		Statement stmt = connection.createStatement();



		// Tabla Producto
		try{
			stmt.executeUpdate("INSERT INTO Producto VALUES(1000)");
			stmt.executeUpdate("INSERT INTO Producto VALUES(1001)");
			stmt.executeUpdate("INSERT INTO Producto VALUES(1002)");
			stmt.executeUpdate("INSERT INTO Producto VALUES(1003)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Proveedor
		try{
			stmt.executeUpdate("INSERT INTO Proveedor VALUES(800, 'proveedorserrano@gmail.com')");
			stmt.executeUpdate("INSERT INTO Proveedor VALUES(801, 'camisetas@gmail.com')");
			stmt.executeUpdate("INSERT INTO Proveedor VALUES(802, 'balonesManolo@gmail.com')");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Cliente
		/*try{
			stmt.executeUpdate("INSERT INTO Cliente VALUES('86543423W')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('42343423L')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('99744323Q')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('77777777A')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('77777777B')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('77777777C')");
			stmt.executeUpdate("INSERT INTO Cliente VALUES('77777777D')");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}*/

		// Tabla EmpresaExterna
		try{
			stmt.executeUpdate("INSERT INTO EmpresaExterna VALUES('Transportes Paqui','paquiempresa@gmail.com')");
			stmt.executeUpdate("INSERT INTO EmpresaExterna VALUES('Seguridad Paco SL.','pacosecurity@hotmail.com')");
			stmt.executeUpdate("INSERT INTO EmpresaExterna VALUES('Refrigerios Martinez','noreplay@gmail.com')");
	}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Empleado
		try{
			stmt.executeUpdate("INSERT INTO Empleado VALUES('76543123W')");
			stmt.executeUpdate("INSERT INTO Empleado VALUES('76544223W')");
			stmt.executeUpdate("INSERT INTO Empleado VALUES('76544434L')");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Componente
		try{
			stmt.executeUpdate("INSERT INTO Componente VALUES ('76544313W', 'Tomate Tomatito López', 'Calle La Amargura 23', 1200, 'pedrogallego@correo.ugr.es','666622554', 7000000, TO_DATE('01/01/2022', 'dd/mm/yyyy'), TO_DATE('26/01/2023', 'dd/mm/yyyy'))");
			stmt.executeUpdate("INSERT INTO Componente VALUES ('73133133A', 'Hugo Antonio Teruel', 'Calle Hornillo de Cartuja 11', 2000, 'hugovignas@gmail.com','66804746', 50000, TO_DATE('05/03/2022', 'dd/mm/yyyy'), TO_DATE('30/05/2023', 'dd/mm/yyyy'))");
			stmt.executeUpdate("INSERT INTO Componente VALUES ('17122639D', 'Antonio José Lara', 'Calle Emperatriz Eugenia', 1500, 'antoniojoselp@gmail.com','66444554', 30000, TO_DATE('01/11/2022', 'dd/mm/yyyy'), TO_DATE('30/10/2023', 'dd/mm/yyyy'))");
			stmt.executeUpdate("INSERT INTO Componente VALUES ('75334239C', 'Amparo Almohalla', 'Plazita', 2000, 'amparoalmohalla@gmail.com','66534554', 30500, TO_DATE('06/05/2022', 'dd/mm/yyyy'), TO_DATE('30/04/2023', 'dd/mm/yyyy'))");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla ProductoActivo
		try{
			stmt.executeUpdate("INSERT INTO ProductoActivo VALUES(1000, 20, 15.0)");
			stmt.executeUpdate("INSERT INTO ProductoActivo VALUES(1001, 30, 19.95)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla CompraTienda
		try{
			stmt.executeUpdate("INSERT INTO CompraTienda VALUES('77777777A', 1000, 3)");
			stmt.executeUpdate("INSERT INTO CompraTienda VALUES('77777777B', 1001, 10)");
			stmt.executeUpdate("INSERT INTO CompraTienda VALUES('77777777C', 1001, 5)");
			stmt.executeUpdate("INSERT INTO CompraTienda VALUES('77777777C', 1000, 2)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Pedido_Envio y Contiene (Preferiblemente la inicializamos vacía)
		try{
			stmt.executeUpdate("INSERT INTO Pedido_Envio VALUES(sec_codigoenvio.nextval, 800 )");
			stmt.executeUpdate("INSERT INTO Contiene VALUES(1000,sec_codigoenvio.currval,35)");
			stmt.executeUpdate("INSERT INTO Pedido_Envio VALUES(sec_codigoenvio.nextval, 802 )");
			stmt.executeUpdate("INSERT INTO Contiene VALUES(1000,sec_codigoenvio.currval,3)");
			stmt.executeUpdate("INSERT INTO Pedido_Envio VALUES(sec_codigoenvio.nextval, 801 )");
			stmt.executeUpdate("INSERT INTO Contiene VALUES(1001,sec_codigoenvio.currval,5)");

		}catch(SQLException e){
			System.out.println("El error está aquí");
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Evento
		try{
			stmt.executeUpdate("INSERT INTO Evento VALUES (500,  TO_DATE('26/01/2022', 'dd/mm/yyyy'))");
			stmt.executeUpdate("INSERT INTO Evento VALUES (501,  TO_DATE('29/01/2022', 'dd/mm/yyyy'))");
			stmt.executeUpdate("INSERT INTO Evento VALUES (502,  TO_DATE('03/02/2022', 'dd/mm/yyyy'))");
	}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Compra_Entrada_Acceso
		try{
			stmt.executeUpdate("INSERT INTO Compra_Entrada_Acceso VALUES ('86543423W', 500, 2003)");
			stmt.executeUpdate("INSERT INTO Compra_Entrada_Acceso VALUES ('42343423L', 500, 19532)");
			stmt.executeUpdate("INSERT INTO Compra_Entrada_Acceso VALUES ('99744323Q', 501, 10000)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Incidencias_Informe
		try{
			stmt.executeUpdate("INSERT INTO Incidencias_Informe VALUES (500, 'Ha vomitao un chiquillo')");
			stmt.executeUpdate("INSERT INTO Incidencias_Informe VALUES (500, 'Ha habido un incendio')");
			stmt.executeUpdate("INSERT INTO Incidencias_Informe VALUES (502, 'Se ha muerto un jugador')");
	}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Contratada
		try{
			stmt.executeUpdate("INSERT INTO Contratada VALUES (500, 'Transportes Paqui')");
			stmt.executeUpdate("INSERT INTO Contratada VALUES (500, 'Seguridad Paco SL.')");
			stmt.executeUpdate("INSERT INTO Contratada VALUES (501, 'Seguridad Paco SL.')");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Organiza
		try{
			stmt.executeUpdate("INSERT INTO Organiza VALUES (500, '76543123W')");
			stmt.executeUpdate("INSERT INTO Organiza VALUES (502, '76544223W')");
			stmt.executeUpdate("INSERT INTO Organiza VALUES (501, '76543123W')");
	}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Abonado
		try{
			stmt.executeUpdate("INSERT INTO Abonado VALUES ('77777777A', 'Amparo', 'Melano', 'e.amparoalmo@go.ugr.es', '668822554', 1976)");
			stmt.executeUpdate("INSERT INTO Abonado VALUES ('77777777B', 'Pedro', 'Picapiedra', 'e.pedrogallego@go.ugr.es', '732822554', 1998)");
			stmt.executeUpdate("INSERT INTO Abonado VALUES ('77777777C', 'Antonio', 'Vignas', 'e.antoniojoselp@go.ugr.es', '687362374', 1977)");
			stmt.executeUpdate("INSERT INTO Abonado VALUES ('77777777D', 'Hugo', 'Lara', 'e.hugovignas@go.ugr.es', '623737282', 2000)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		/*
		stmt.executeUpdate("INSERT INTO Empleado VALUES('76544223W')");
		stmt.executeUpdate("INSERT INTO Empleado VALUES('76544434L')");
		*/
		// Tabla ComponenteActivo_Contrato
		/*try{
			stmt.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES('76544313W', '76543123W')");
			stmt.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES('73133133A', '76544223W')");
			stmt.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES('75334239C', '76543123W')");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}*/

		// Tabla AbonadoActivo_Contrata_Abono (Preferiblemente la inicializamos vacía)
		//stmt.executeUpdate("INSERT INTO AbonadoActivo_Contrata_Abono VALUES ('42343423L', 21021, 1);");
		//stmt.executeUpdate("INSERT INTO AbonadoActivo_Contrata_Abono VALUES ('99744323Q', 17532, 2);");

		// Tabla EXTRADEPORTIVO
		try{
			stmt.executeUpdate("INSERT INTO Extradeportivo VALUES (501)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla Partido
		try{
			stmt.executeUpdate("INSERT INTO Partido VALUES (502)");
			stmt.executeUpdate("INSERT INTO Partido VALUES (500)");
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		// Tabla AccesoAbonado (Preferiblemente la inicializamos vacía ya que va ligada a AbonadoActivo_Contrata_Abono)
		//stmt.executeUpdate("INSERT INTO AccesoAbonado VALUES (502, 17532);");
		System.out.println("Tuplas insertadas correctamente (100% reset...)");

	}catch(SQLException e){
    System.err.format("SQL exception: %s\n%s\n", e.getSQLState(), e.getMessage());
  }
}






public void eliminarTablas() throws SQLException {
  try{
    Statement stmt = connection.createStatement();


		//Se eliminan los disparadores
		try{
			stmt.executeUpdate("DROP TRIGGER actualizarcantidadVentaItems");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		try{
			stmt.executeUpdate("DROP TRIGGER anadir_cliente_compratienda");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		try{
			stmt.executeUpdate("DROP TRIGGER anadir_cliente_compraentrada");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		try{
			stmt.executeUpdate("DROP TRIGGER anadir_cliente_abonado");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		/* try{
			stmt.executeUpdate("DROP TRIGGER insertar_componente");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}  */
		try{
			stmt.executeUpdate("DROP TRIGGER insertar_abonado");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		try{
			stmt.executeUpdate("DROP TRIGGER nuevo_pedido");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
		try{
			stmt.executeUpdate("DROP TRIGGER anadir_producto");
			}catch(SQLException e){
		  System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}

		System.out.println("Disparadores borrados correctamente (17% reset...)");

		// Se elimina la Tabla AccesoABonado
		try{
    	stmt.executeUpdate("DROP TABLE AccesoAbonado");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Extradeportivo
    try{
			stmt.executeUpdate("DROP TABLE Extradeportivo");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Partido
		try{
			stmt.executeUpdate("DROP TABLE Partido");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla AbonadoActivo_Contrata_Abono
		try{
			stmt.executeUpdate("DROP TABLE AbonadoActivo_Contrata_Abono");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla ComponenteActivo_Contrato
		try{
			stmt.executeUpdate("DROP TABLE ComponenteActivo_Contrato");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Abonado
		try{
			stmt.executeUpdate("DROP TABLE Abonado");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Organiza
		try{
			stmt.executeUpdate("DROP TABLE Organiza");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Contratada
		try{
			stmt.executeUpdate("DROP TABLE Contratada");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Incidencias_Informe
		try{
			stmt.executeUpdate("DROP TABLE Incidencias_Informe");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Compra_Entrada_Acceso
		try{
			stmt.executeUpdate("DROP TABLE Compra_Entrada_Acceso");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Evento
		try{
			stmt.executeUpdate("DROP TABLE Evento");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Contiene
		try{
			stmt.executeUpdate("DROP TABLE Contiene");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Pedido_Envio
		try{
			stmt.executeUpdate("DROP TABLE Pedido_Envio");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla CompraTienda
		try{
			stmt.executeUpdate("DROP TABLE CompraTienda");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla ProductoActivo
		try{
			stmt.executeUpdate("DROP TABLE ProductoActivo");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Componente
		try{
			stmt.executeUpdate("DROP TABLE Componente");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Empleado
		try{
			stmt.executeUpdate("DROP TABLE Empleado");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla EmpresaExterna
		try{
			stmt.executeUpdate("DROP TABLE EmpresaExterna");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Cliente
		try{
			stmt.executeUpdate("DROP TABLE Cliente");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Proveedor
		try{
			stmt.executeUpdate("DROP TABLE Proveedor");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		// Se elimina la Tabla Producto
		try{
			stmt.executeUpdate("DROP TABLE Producto");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }


		try{
			stmt.executeUpdate("drop sequence sec_abonado");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }

		try{
			stmt.executeUpdate("drop sequence sec_codigoenvio");
		}catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }



    connection.commit();
    System.out.println("Tablas borradas correctamente (33% reset...)");

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

	// Eliminar actividad jugadores con contrato expirado
	public void desactivarContratosExpirados(Statement statement) throws SQLException{
		try{
			ResultSet componentes_activos = statement.executeQuery("SELECT Componente.CDNI FROM Componente, ComponenteActivo_Contrato WHERE Componente.CDNI=ComponenteActivo_Contrato.CDNI AND FechaFin<=SYSDATE");
			ArrayList<String> dni_componentes = new ArrayList<String>();
			while(componentes_activos.next()){
				dni_componentes.add(componentes_activos.getString(1));
			}

			for(int i=0; i<dni_componentes.size(); i++){
				statement.executeUpdate("DELETE FROM ComponenteActivo_Contrato WHERE CDNI='"+dni_componentes.get(i)+"'");
				System.out.println("Componente con DNI: "+dni_componentes.get(i)+" eliminado por fin de contrato.");
			}
		}catch(SQLException e){
	    System.err.format("SQL exception: %s\n%s\n", e.getSQLState(), e.getMessage());
	  }
	}

 // ALTA COMPONENTE
  public void altaComponente(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);
		String dni;
      	do{
				System.out.print("Introduzca el DNI del nuevo componente (formato 11111111X) :");
				dni = input.nextLine();
		  }while(!dni.matches("[0-9]{8}[a-zA-Z]"));


			System.out.println("Introduzca los siguientes datos sobre el nuevo componente");
			String nombre;
			do{
				System.out.print("Nombre completo :");
				 nombre = input.nextLine();
			}while(!nombre.matches("[^0-9]+"));

			String direccion;
			do{
				System.out.print("Dirección (Calle ..., N ..., Población) :");
				direccion = input.nextLine();
			}while(!direccion.matches("Calle +[^0-9]+, N +[0-9]+, +[^0-9]+"));
			//[a-zA-Z]

			String salario;
			do{
				System.out.println("Salario: ");
				salario = input.nextLine();
			}while(!isNumeric(salario));

			String correo;
			do{
				System.out.print("Correo (debe contener  @domino):");
				correo = input.nextLine();
			}while(!correo.matches("[a-z0-9\\.]+@[a-z\\.]+"));

			String telefono;
			do{
				System.out.print("Teléfono :");
				telefono = input.nextLine();
			}while(!telefono.matches("[0-9]{9}"));

			String clausula;
			do{
				System.out.println("Cláusula de rescisión:");
				clausula = input.nextLine();
			}while(!isNumeric(clausula));

			//COMPROBAR QUE SON FECHAS

			String fechainicio;
		  	do{
				System.out.println("Fecha de inicio del contrato:");
				fechainicio=input.nextLine();
		  }while(!fechainicio.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}"));
		  fechainicio = "TO_DATE('"+fechainicio+"','dd/mm/yyyy')";

			String fechafin;
		  	do{
				System.out.println("Fecha de fin del contrato:");
				fechafin=input.nextLine();
		  }while(!fechafin.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}"));
		  fechafin = "TO_DATE('"+fechafin+"','dd/mm/yyyy')";

			try{
				statement.executeUpdate("INSERT INTO Componente VALUES('"+ dni +"','" + nombre +"','" + direccion +"',"+ salario + ",'"+ correo+ "','" + telefono+"',"+clausula+ ","+fechainicio+"," + fechafin+ ")");
			}catch(SQLException e){
				statement.executeUpdate("UPDATE Componente SET nombre='"+nombre+"', Direccion='"+direccion+"', Salario="+salario+", Correo='"+correo+"', Clausula_rescision="+clausula+", FechaInicio="+fechafin+", FechaFin="+fechafin+" WHERE CDNI='"+dni+"'");
			}

			String dni_empleado;
			System.out.print("Estos son los DNIs de nuestros empleados:\n");
			ResultSet empleados = statement.executeQuery("SELECT CDNI_empleado FROM Empleado");
			while(empleados.next()){
				System.out.println(empleados.getString(1));
			}
			do{
				System.out.print("Introduzca el DNI del empleado que lo contrata (formato 11111111X) :");
				dni_empleado = input.nextLine();
			}while(!dni_empleado.matches("[0-9]{8}[a-zA-Z]"));

			try{
				statement.executeUpdate("INSERT INTO ComponenteActivo_Contrato VALUES('"+ dni +"','" + dni_empleado +"')");
			}catch(SQLException e){
				System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
			}

      //connection.commit();

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };

 // BAJA COMPONENTE
  public void bajaComponente(Statement statement) throws SQLException{
    try{
		Savepoint CheckPoint = connection.setSavepoint("BajaComponente");


	  	Scanner input = new Scanner(System.in);

			String dni;
	   	do{
				System.out.print("Introduzca el DNI del componente que va a dar de baja (formato 11111111X) :");
				dni = input.nextLine();
	  	}while(!dni.matches("[0-9]{8}[a-zA-Z]"));

      statement.executeUpdate("DELETE FROM ComponenteActivo_Contrato WHERE CDNI='" + dni +"'");

		boolean terminar=false;
		while(!terminar){
		  System.out.println("\nIntroduzca:");
		  System.out.println("\t1-Si quiere cancelar la baja del componente.");
		  System.out.println("\t2-Para guardar la baja.");

		  int opcion = input.nextInt();

			if(opcion==1){
				connection.rollback(CheckPoint);
				terminar=true;
			}
			if(opcion==2){
				connection.commit();
				terminar=true;
			}
		}

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

  // RENOVAR COMPONENTE
  public void renovarComponente(Statement statement) throws SQLException{
    try{

		Savepoint CheckPoint = connection.setSavepoint("RenovarComponente");
		  Scanner input = new Scanner(System.in);

			String dni;
		  do{
				System.out.print("Introduzca el DNI del componente que va a renovar su contrato (formato 11111111X) :");
				dni = input.nextLine();
		  }while(!dni.matches("[0-9]{8}[a-zA-Z]"));

		  String fechafin;
		  do{
				System.out.println("Introduzca la nueva fecha en la que finalizará su contrato:");
				fechafin =input.nextLine();
		  }while(!fechafin.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}"));
		  fechafin = "TO_DATE('"+fechafin+"','dd/mm/yyyy')";

			String salario;
		  do{
			  System.out.println("Introduzca su nuevo salario (-1 si mantiene el mismo):");
	      salario = input.nextLine();
		  }while(!isNumeric(salario));

			String clausula;
		  do{
			  System.out.println("Introduzca su nueva cláusula de rescisión (-1 si mantiene la misma):");
	      clausula = input.nextLine();
		  }while(!isNumeric(clausula));

		  if(salario.matches("-1")){
			  if(clausula.matches("-1")){
				   statement.executeUpdate("UPDATE Componente SET FechaFin="+fechafin+" WHERE CDNI='"+dni+"'");
			  }
			  else{
				   statement.executeUpdate("UPDATE Componente SET Clausula_rescision="+clausula+", FechaFin="+fechafin+" WHERE CDNI='"+dni+"'");
			  }
		  }
		  else{
			  if(clausula.matches("-1")){
				  statement.executeUpdate("UPDATE Componente SET Salario="+salario+", FechaFin="+fechafin+" WHERE CDNI='"+dni+"'");
			  }
			  else{
				  statement.executeUpdate("UPDATE Componente SET Salario= "+salario+", Clausula_rescision="+clausula+", FechaFin="+fechafin+" WHERE CDNI='"+dni+"'");
			  }
		  }

		boolean terminar=false;
		while(!terminar){
			System.out.println("\nIntroduzca:");
		  System.out.println("\t1-Si quiere cancelar la renovacion del componente.");
		  System.out.println("\t2-Para guardar la renovacion.");

		  int opcion = input.nextInt();

			if(opcion==1){
				connection.rollback(CheckPoint);
				terminar=true;
			}
			if(opcion==2){
				connection.commit();
				terminar=true;
			}
		}

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


// LISTAR COMPONENTES

  public void listarComponentes(Statement statement) throws SQLException{
    try{
      ResultSet componentes = statement.executeQuery("SELECT * FROM ComponenteActivo_Contrato INNER JOIN Componente ON ComponenteActivo_Contrato.CDNI = Componente.CDNI");
      while(componentes.next()){
           System.out.println(componentes.getString(1)+" "+componentes.getString(2)+" "+componentes.getString(4)+" "+componentes.getString(5)+" "+componentes.getString(6)+" "+componentes.getString(7)+" "+componentes.getString(8)+" "+componentes.getString(9)+" "+componentes.getString(10)+" "+componentes.getString(11));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


	// BALANCE DE CUENTAS
	public void balanceCuentas(Statement statement) throws SQLException{
    try{
			float presupuesto=0;
			System.out.println("\n___________________________________________________");

			// Cuenta abonados
			ResultSet abonados = statement.executeQuery("SELECT COUNT(CDNI) FROM AbonadoActivo_Contrata_Abono");
			abonados.next();
			presupuesto += 150*Integer.parseInt(abonados.getString(1));
			System.out.println("+"+String.valueOf(150*Integer.parseInt(abonados.getString(1)))+"€ (ABONOS: 150€ * "+abonados.getString(1)+"abonos)");

			// Cuenta entradas
			ResultSet entradas = statement.executeQuery("SELECT COUNT(CDNI) FROM Compra_Entrada_Acceso");
			entradas.next();
			presupuesto += 20*Integer.parseInt(entradas.getString(1));
			System.out.println("+"+String.valueOf(20*Integer.parseInt(entradas.getString(1)))+"€ (ENTRADAS: 20€ * "+entradas.getString(1)+"entradas)");

			// Cuenta Equipo
			ResultSet componentes = statement.executeQuery("SELECT Salario FROM ComponenteActivo_Contrato, Componente WHERE ComponenteActivo_Contrato.CDNI=Componente.CDNI");
			float prescomp=0;
			while(componentes.next()){
				prescomp+=Float.parseFloat(componentes.getString(1));
			}
			presupuesto -= prescomp;
			System.out.println("-"+String.valueOf(prescomp)+"€ (SUELDO COMPONENTES)");

			// Cuenta Tienda
				// productos vendidos
			ResultSet tienda = statement.executeQuery("SELECT CompraTienda.Cantidad, Precio FROM CompraTienda, ProductoActivo WHERE CompraTienda.CProducto=ProductoActivo.CProducto");
			float prestienda_vendidos=0;
			float prestienda_pedidos=0;
			while(tienda.next()){
				float cantidad=Float.parseFloat(tienda.getString(1));
				float precio=Float.parseFloat(tienda.getString(2));

				prestienda_vendidos+=precio*cantidad;
			}
			presupuesto += prestienda_vendidos;
			System.out.println("+"+String.valueOf(prestienda_vendidos)+"€ (PRODUCTOS VENDIDOS)");

				// productos en el inventario y productos en camino antes de recibirlos
			tienda = statement.executeQuery("SELECT Cantidad, Precio FROM ProductoActivo");
			while(tienda.next()){
				float cantidad=Float.parseFloat(tienda.getString(1));
				float precio=Float.parseFloat(tienda.getString(2));

				prestienda_pedidos+=precio*cantidad/2.0;
			}
			tienda = statement.executeQuery("SELECT Contiene.Cantidad, Precio FROM ProductoActivo,Contiene WHERE Contiene.CProducto=ProductoActivo.CProducto");
			while(tienda.next()){
				float cantidad=Float.parseFloat(tienda.getString(1));
				float precio=Float.parseFloat(tienda.getString(2));

				prestienda_pedidos+=precio*cantidad/2.0;
			}
			presupuesto-=prestienda_pedidos;
			System.out.println("-"+String.valueOf(prestienda_pedidos)+"€ (PRODUCTOS PEDIDOS Y SIN VENDER)");

			System.out.println("---------------------------------------------------");
			System.out.println(String.valueOf(presupuesto)+"€ TOTAL BALANCE");
			System.out.println("___________________________________________________\n");




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

			desactivarContratosExpirados(statement);

			while (!terminar){
				System.out.println("\nMENÚ EQUIPO");
				System.out.println("\nIntroduzca una opcion del 1 al 6:");
				System.out.println("\t1-Dar de alta componentes.");
				System.out.println("\t2-Finalizar contrato.");
				System.out.println("\t3-Renovar contrato.");
    		System.out.println("\t4-Listar componentes.");
    		System.out.println("\t5-Balance de cuentas.");
    		System.out.println("\t6-Volver al menú principal.");

				opcion = input.nextInt();

				switch(opcion){
					case 1:

						Savepoint CheckPoint = connection.setSavepoint("AltaComponente");

						boolean fin=false;
						while(!fin){
							altaComponente(statement);

							int op=0;
							while(op!= 1 && op!=2 && op!=3){
								System.out.println("\nIntroduzca:");
								System.out.println("\t1-Para seguir insertando.");
								System.out.println("\t2-Si quiere cancelar todos los componentes insertados.");
								System.out.println("\t3-Para guardar los cambios.");

								op = input.nextInt();
							}

							switch(op){
								case 1:
									break;
								case 2:
									connection.rollback(CheckPoint);
									fin=true;
									break;
								case 3:
									connection.commit();
									fin=true;
									break;
								default:
									break;

							}
						}

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

					default:
	      			System.out.println("Esta opción no es válida. Por favor, introduzca un número del 1 al 6");
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


	// Mostrar asientos libres
	public void mostrarAsientosLibresEvento(Statement statement, String cevento) throws SQLException{
		try{
			ArrayList<Integer> ocupados = new ArrayList<Integer>();

			// Asientos ocupados por venta de entradas
			ResultSet asientos = statement.executeQuery("SELECT DISTINCT num_asiento FROM Compra_Entrada_Acceso WHERE CEvento="+cevento);
			while(asientos.next()){
				int index_asiento_ocupado = Integer.parseInt(asientos.getString(1));
				ocupados.add(index_asiento_ocupado);
			}

			// Asientos ocupados por venta de abonos
			ResultSet asientosab = statement.executeQuery("SELECT DISTINCT num_asiento FROM AccesoAbonado WHERE CEvento="+cevento);
			while(asientosab.next()){
				int index_asiento_ocupado = Integer.parseInt(asientosab.getString(1));
				ocupados.add(index_asiento_ocupado);
			}


			Collections.sort(ocupados);
			System.out.println("ASIENTOS LIBRES PARA EL EVENTO "+cevento+". (Por rangos)");
			if(ocupados.size()>0){
				if(1!=ocupados.get(0)){
					System.out.print("[1-"+(ocupados.get(0)-1)+"], ");
				}
				for(int i=0; i<ocupados.size()-1; i++){
					if(ocupados.get(i)+1<=ocupados.get(i+1)-1){
						System.out.print("["+(ocupados.get(i)+1)+"-"+(ocupados.get(i+1)-1)+"], ");
					}
				}
				if(CAPACIDAD_ESTADIO!=ocupados.get(ocupados.size()-1)){
						System.out.print("["+(ocupados.get(ocupados.size()-1)+1)+"-"+CAPACIDAD_ESTADIO+"], ");
				}
			}else{
				System.out.print("[1-"+CAPACIDAD_ESTADIO+"]");
			}
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	};



  // COMPRAR ENTRADAS
  public void comprarEntradas(Statement statement) throws SQLException{
  	try{
			Scanner input = new Scanner(System.in);

			// Decir que cliente eres pprimero
			System.out.println("Para efectuar la compra de una entrada tiene que ser cliente. Díganos el DNI del cliente:");
			String DNI;
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			//statement.executeUpdate("INSERT INTO Cliente VALUES('"+DNI+"')");

    	ResultSet listadoeventos = statement.executeQuery("SELECT * FROM EVENTO");
    	while(listadoeventos.next()){
         System.out.println(listadoeventos.getString(1)+" "+listadoeventos.getString(2));
    	}

			// COMPROBAR QUE EL EVENTO ES CORRECTO
			System.out.println("Seleccione un evento:");
    	String cevento = input.nextLine();
			ResultSet comprobar = statement.executeQuery("SELECT * FROM EVENTO WHERE CEvento="+cevento);
			while(!comprobar.next()){
				System.out.println("Ese evento no existe. Seleccione un evento:");
	    	cevento = input.nextLine();
				comprobar = statement.executeQuery("SELECT * FROM EVENTO WHERE CEvento="+cevento);
			}


			//COMPROBAR QUE DICHO ASIENTO ESTÁ DISPONIBLE Y SE PUEDE SELECCIONAR (PERTENECE A COMPRA_ENTRADA_ACCESO)
			mostrarAsientosLibresEvento(statement, cevento);
			System.out.println("Introduzca el número de asiento:");
			String asiento = input.nextLine();
			comprobar = statement.executeQuery("SELECT * FROM Compra_Entrada_Acceso WHERE num_asiento="+asiento+" AND CEvento="+cevento);
			while(comprobar.next() || Integer.parseInt(asiento)<=0 || Integer.parseInt(asiento)>CAPACIDAD_ESTADIO){
				System.out.println("Ese asiento no está disponible. Introduzca uno disponible:");
	    	asiento = input.nextLine();
				comprobar = statement.executeQuery("SELECT * FROM Compra_Entrada_Acceso WHERE num_asiento="+asiento);
			}

     	statement.executeQuery("INSERT INTO Compra_Entrada_Acceso VALUES('"+DNI+"',"+ cevento+","+asiento +")");

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };




  // CONSULTAR PARTIDOS
  public void consultarProximosPartidos(Statement statement) throws SQLException{
    try{
      ResultSet listadopartidos = statement.executeQuery("SELECT PARTIDO.CEVENTO, FECHA FROM PARTIDO, EVENTO WHERE PARTIDO.CEVENTO=EVENTO.CEVENTO ");
      while(listadopartidos.next()){
           System.out.println(listadopartidos.getString(1)+" "+listadopartidos.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };



  // CONSULTAR EXTRADEPORTIVOS
  public void consultarProximosExtradeportivos(Statement statement) throws SQLException{
    try{
			ResultSet listadoextradeportivos = statement.executeQuery("SELECT EXTRADEPORTIVO.CEVENTO, FECHA FROM EXTRADEPORTIVO, EVENTO WHERE EXTRADEPORTIVO.CEVENTO=EVENTO.CEVENTO ");

      while(listadoextradeportivos.next()){
           System.out.println(listadoextradeportivos.getString(1)+" "+listadoextradeportivos.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };



  // EMPRESAS EXTERNAS
  public void empresasExternas(Statement statement) throws SQLException{
    try{

      ResultSet empresas = statement.executeQuery("SELECT * FROM EmpresaExterna ");
      while(empresas.next()){
           System.out.println(empresas.getString(1) + "\t" + empresas.getString(2));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


	// Consultar incidencias
	public void mostrarIncidencias(Statement statement, String cevento) throws SQLException{
		try{
			ResultSet salida = statement.executeQuery("SELECT CIncidencia FROM Incidencias_Informe WHERE CEvento=" + cevento );
			boolean no_vacio = salida.next();
			if(no_vacio){
				System.out.println("\nLas incidencias registradas hasta ahora en dicho evento son:");
			}else{
				System.out.println("No hay incidencias registradas por el momento en este evento.");
			}
      while(no_vacio){
         System.out.println(salida.getString(1));
				 no_vacio = salida.next();
      }
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}

  // Registrar Incidencia
  public void registrarIncidencia(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

	  Savepoint CheckPoint = connection.setSavepoint("RegistrarIncidencia");

			ResultSet listadoeventos = statement.executeQuery("SELECT * FROM EVENTO ");
      while(listadoeventos.next()){
           System.out.println(listadoeventos.getString(1)+" "+listadoeventos.getString(2));
      }

	  	// Preguntamos sobre el evento de la incidencia
      System.out.println("Seleccione un evento sobre el que quieras registrar la incidencia:");
      String cevento = input.nextLine();
			ResultSet comprobacion = statement.executeQuery("SELECT * FROM EVENTO WHERE cevento="+cevento);

			// Si no existe un evento con código cevento
			while(!comprobacion.next()){
				System.out.println("No existe ningún Evento " +cevento+".\n Por favor, seleccione un evento existente sobre el que quieras registrar la incidencia:");
	      cevento = input.nextLine();
				comprobacion = statement.executeQuery("SELECT * FROM EVENTO WHERE cevento="+cevento);
			}

			// Mostramos las incidencias ya registradas en ese evento si las hay.
			mostrarIncidencias(statement, cevento);

	  	//COMPROBAR QUE DICHA INCIDENCIA NO ESTÁ YA REGISTRADA: Idea, hacer el insert sí o sí. En caso de clave duplicada
			// dará una excepción
      System.out.println("Introduzca la nueva incidencia");
      String incidencia = input.nextLine();

	    statement.executeUpdate("INSERT INTO Incidencias_Informe VALUES("+ cevento +",'" + incidencia + "')");

			boolean fin=false;
			while(!fin){
				System.out.println("\nIntroduzca:");
			  System.out.println("\t1-Si quiere cancelar el registro de incidencia.");
			  System.out.println("\t2-Para guardar el registro.");

			  int opcion = input.nextInt();

				if(opcion==1){
					connection.rollback(CheckPoint);
					fin=true;
				}
				if(opcion==2){
					connection.commit();
					fin=true;
				}
			}



			mostrarIncidencias(statement, cevento);


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
				System.out.println("\nMENÚ EVENTOS");
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

		  	Savepoint CheckPoint1 = connection.setSavepoint("CompraEntrada");

				boolean fin=false;
				while(!fin){
					comprarEntradas(statement);

					int op=0;
					while(op!=1 && op!=2 && op!=3){
						System.out.println("\nIntroduzca:");
						System.out.println("\t1-Para seguir comprando.");
						System.out.println("\t2-Si quiere cancelar sus compras.");
						System.out.println("\t3-Para guardar las compras.");

						op = input.nextInt();
					}

					switch(op){
						case 1:
							break;
						case 2:
							connection.rollback(CheckPoint1);
							fin=true;
							break;
						case 3:
							connection.commit();
							fin=true;
							break;
						default:
							break;
					}
				}


          break;

          case 2:
              consultarProximosPartidos(statement);
          break;

          case 3:
              consultarProximosExtradeportivos(statement);
          break;

				  case 4:
				  		empresasExternas(statement);
				  break;

				  case 5:
				  		registrarIncidencia(statement);
					break;

          case 6:
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



  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  //
  //              ABONADOS
  //
  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////

	// Mostrar asientos ocupados
	public void mostrarAsientosOcupadosAbono(Statement statement) throws SQLException{
		try{
			System.out.println("ASIENTOS OCUPADOS PARA LA TEMPORADA - SECCIÓN ABONADO.");
      		ResultSet asientos = statement.executeQuery("SELECT num_asiento FROM AbonadoActivo_Contrata_Abono ");
      		while(asientos.next()){
           		System.out.print("ASIENTO: " + asientos.getString(1)+ "\t ");
      		}
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	};



	// Mostrar asientos libres
	public void mostrarAsientosLibresAbonado(Statement statement) throws SQLException{
		try{
			ResultSet asientos = statement.executeQuery("SELECT DISTINCT num_asiento FROM AbonadoActivo_Contrata_Abono");

			ArrayList<Integer> ocupados = new ArrayList<Integer>();
			while(asientos.next()){
				int index_asiento_ocupado = Integer.parseInt(asientos.getString(1));
				ocupados.add(index_asiento_ocupado);
			}
			Collections.sort(ocupados);
			System.out.println("ASIENTOS LIBRES PARA LA TEMPORADA - ESPACIO ABONADO (Por rangos)");

			if(ocupados.size()>0){
				if(1!=ocupados.get(0)){
						System.out.print("[1-"+(ocupados.get(0)-1)+"], ");
				}
				for(int i=0; i<ocupados.size()-1; i++){
					if(ocupados.get(i)+1<=ocupados.get(i+1)-1){
						System.out.print("["+(ocupados.get(i)+1)+"-"+(ocupados.get(i+1)-1)+"], ");
					}
				}
				if(CAPACIDAD_ESTADIO!=ocupados.get(ocupados.size()-1)){
						System.out.print("["+(ocupados.get(ocupados.size()-1)+1)+"-"+CAPACIDAD_ESTADIO+"], ");
				}
			}else{
				System.out.print("[1-"+CAPACIDAD_ESTADIO+"]");
			}
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	};

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
	public void altaAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);
			ResultSet asientos;
			boolean ya_era_abonado;

			System.out.println("Vamos a proceder a solicitar sus datos para el abono. Introduzca ");
			String DNI ;
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			// Comprobamos si ya había sido abonado. En ese caso solo habría que meterlo
			// como abonado abonado activo
			ResultSet abonadoantiguo = statement.executeQuery("SELECT * FROM Abonado WHERE CDNI='"+DNI+"'");

			ya_era_abonado = abonadoantiguo.next();

			String nombre="", apellidos="", correo="", telefono="";

			if(!ya_era_abonado){
				// Si no fue abonado, le pedimos los nuevos datos
				do{
					System.out.print("Nombre :");
					nombre = input.nextLine();
				}while(!nombre.matches("[a-zA-Z]+"));

				do{
					System.out.print("Apellidos :");
					apellidos = input.nextLine();
				}while(!apellidos.matches("[a-zA-Z' ']+"));

				do{
					System.out.print("Correo (debe contener  @domino):");
					correo = input.nextLine();
				}while(!correo.matches("[a-z0-9\\.]+@[a-z\\.]+"));

				do{
					System.out.print("Teléfono :");
					telefono = input.nextLine();
				}while(!telefono.matches("[0-9]{9}"));

				//statement.executeUpdate("INSERT INTO Cliente VALUES('"+DNI+"')");

			}


			// SELECCIÓN DE ASIENTOS
			mostrarAsientosLibresAbonado(statement);
			System.out.println("Seleccione un asiento.");
			String asientodeseado = input.nextLine();

			asientos = statement.executeQuery("SELECT * FROM AbonadoActivo_Contrata_Abono WHERE num_asiento="+asientodeseado);

			// Si no se ha insertado un asiento válido
			while(asientos.next() || Integer.parseInt(asientodeseado)<=0 || Integer.parseInt(asientodeseado)>CAPACIDAD_ESTADIO){
				System.out.println("El asiento " + asientodeseado + " no está disponible. Los asientos libres son:");
				mostrarAsientosLibresAbonado(statement);
				System.out.println("Por favor, seleccione otro asiento.");
				asientodeseado = input.nextLine();

				asientos = statement.executeQuery("SELECT * FROM AbonadoActivo_Contrata_Abono WHERE num_asiento="+asientodeseado);
			}

			// Añadimos/Modificamos en la tabla ABONADO
			if(ya_era_abonado){
				statement.executeUpdate("UPDATE Abonado SET Anyo_Alta=2022 WHERE CDNI='"+DNI+"'");
			}else{
				statement.executeUpdate("INSERT INTO Abonado VALUES('"+DNI+"','"+nombre+"','"+apellidos+"','"+correo+"','"+telefono+"',2022)");
			}


			// Añadimos en la tabla AbonadoActivo_Contrata_Abono
			statement.executeUpdate("INSERT INTO AbonadoActivo_Contrata_Abono VALUES('"+DNI+"',"+asientodeseado+",sec_abonado.nextval)");


			// Añadimos a la tabla de Acceso abonado y Compra_Entrada_Acceso
			ResultSet partidos = statement.executeQuery("SELECT Partido.CEvento FROM Partido,Evento WHERE Partido.CEvento=Evento.CEvento AND fecha > SYSDATE ");
			ArrayList<String> ceventos = new ArrayList<String>();
			int k=0;
			while(partidos.next()){
				ceventos.add(partidos.getString(1));
			}

			for(int i=0; i<ceventos.size();i++){
				statement.executeUpdate("INSERT INTO AccesoAbonado VALUES("+ceventos.get(i)+","+asientodeseado+")");
			}


			ResultSet nuevonumabonoRS = statement.executeQuery("SELECT numero_abonado FROM AbonadoActivo_Contrata_Abono WHERE CDNI='"+DNI+"'");
			nuevonumabonoRS.next();

			System.out.println("Felicidades "+nombre+ ". Ya eres nuevo abonado de los Serrano! Tu asiento es el "+asientodeseado+" y su NºAbonado "+nuevonumabonoRS.getString(1));


		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	};



	/*
	DAR DE BAJA ABONADO
		El sistema elimina la ficha del abonado cuyo número de abonado se indica en
		la entrada y deja libre su asiento correspondiente
	*/
	public void bajaAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);

			Savepoint CheckPoint = connection.setSavepoint("BajaAbonado");
			boolean ya_era_abonado;

			// Datos del abonado
			String DNI;
			System.out.println("Vamos a proceder a solicitar sus datos para el abono. Introduzca ");
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			ResultSet abonadosActivos = statement.executeQuery("SELECT num_asiento FROM AbonadoActivo_Contrata_Abono WHERE CDNI='"+DNI+"'");
			if(abonadosActivos.next()){
				String asiento = abonadosActivos.getString(1);
				statement.executeUpdate("DELETE FROM AccesoAbonado WHERE num_asiento="+asiento);
				statement.executeUpdate("DELETE FROM AbonadoActivo_Contrata_Abono WHERE CDNI='"+DNI+"'");


				// CONFIRMAR CAMBIOS
				int opcion=0;
				while(opcion!=1 && opcion!=2){
					System.out.println("\nIntroduzca:");
					System.out.println("\t1-Si quiere cancelar la baja del abonado.");
					System.out.println("\t2-Para guardar la baja.");

					opcion = input.nextInt();
				}

				if(opcion==1){
					connection.rollback(CheckPoint);
				}
				if(opcion==2){
					System.out.println("Se ha dado de baja correctamente al antiguo abonado "+ DNI);
					connection.commit();
				}

			}else{
				System.out.println("No existe ningún abonado activo con DNI "+DNI);
			}

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}


	/*
	MOSTRAR INFORMACIÓN ABONADO
		Muestra la información de un abonado
	*/
	public void mostrarInfoAbonado(Statement statement) throws SQLException{
		try{
			Scanner input = new Scanner(System.in);
			System.out.println("Escriba el DNI del abonado del que quiera conocer los datos ");
			String DNI;
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));


			ResultSet abonados = statement.executeQuery("SELECT Abonado.CDNI, Nombre, Apellidos, Correo, Telefono, Anyo_Alta, numero_abonado FROM  Abonado, AbonadoActivo_Contrata_Abono WHERE Abonado.CDNI=AbonadoActivo_Contrata_Abono.CDNI AND Abonado.CDNI='"+DNI+"'");
			if(abonados.next()){
				String nombre = abonados.getString(2);
				String apellidos = abonados.getString(3);
				String correo = abonados.getString(4);
				String telefono = abonados.getString(5);
				String anio_alta = abonados.getString(6);
				String num_abono = abonados.getString(7);

				System.out.println("\nINFORMACIÓN PERSONAL DE ABONADO");
				System.out.println("NºAbono: "+num_abono);
				System.out.println("Apellidos, Nombre: "+apellidos+", "+nombre);
				System.out.println("DNI: "+DNI);
				System.out.println("Correo: "+correo);
				System.out.println("Teléfono: "+telefono);
				System.out.println("Abono dado de alta en "+anio_alta+"\n");
			}else{
				System.out.println("No existe ningún abonado activo con DNI "+DNI);
			}
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}

	/*
		ENVÍO DE NOTICIAS
	*/
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
	      MimeMessage msg = new MimeMessage(session);
	      //set message headers
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress("no_reply@example.com", "Los Serrano"));

	      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
    	  Transport.send(msg);

	      System.out.print(".");
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	}

	/*
		Fuente: https://www.journaldev.com/2532/javamail-example-send-mail-in-java-smtp
	*/
	public void envioNoticias(Statement statement) throws SQLException{

		final String fromEmail = "losserranoddsi@gmail.com"; //requires valid gmail id
		final String password = "bzhmxzmjvoluolfh"; // contraseña de aplicación

		System.out.println("<<SSLEmail Iniciado>>");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); //SMTP Port

		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		Session session = Session.getDefaultInstance(props, auth);
		System.out.println("<<Sesión creada>>");

		String body="PRÓXIMOS PARTIDOS:\n";
		ResultSet eventos = statement.executeQuery("SELECT EVENTO.CEVENTO, Fecha FROM EVENTO, PARTIDO WHERE EVENTO.CEVENTO=PARTIDO.CEVENTO ");
		while(eventos.next()){
			body = body + eventos.getString(1)+" ("+eventos.getString(2)+")\n";
		}

		body = body + "\nPRÓXIMOS EVENTOS EXTRADEPORTIVOS\n";
		eventos = statement.executeQuery("SELECT EVENTO.CEVENTO, Fecha FROM EVENTO, EXTRADEPORTIVO WHERE EVENTO.CEVENTO=EXTRADEPORTIVO.CEVENTO ");
		while(eventos.next()){
			body = body + eventos.getString(1)+" ("+eventos.getString(2)+")\n";
		}

		ResultSet correosq = statement.executeQuery("SELECT correo FROM Abonado, AbonadoActivo_Contrata_Abono  WHERE Abonado.CDNI=AbonadoActivo_Contrata_Abono.CDNI ");
		System.out.print("Enviando");
		while(correosq.next()){
			sendEmail(session, correosq.getString(1),"Envío de noticias a los serraners", body);
			//sendEmail(session, toEmail,"Envío de noticias", body);
		}

		System.out.println("\nNoticias enviadas correctamente.");

	}

	/*
	MOSTRAR LISTADO DE ABONADOS
		Muestra la lista de todos los abonados
	*/

	public void listadoAbonados(Statement statement) throws SQLException{
		try{
			ResultSet abonados = statement.executeQuery("SELECT Abonado.CDNI, Nombre, Apellidos, numero_abonado, correo FROM  Abonado, AbonadoActivo_Contrata_Abono WHERE Abonado.CDNI=AbonadoActivo_Contrata_Abono.CDNI");
			System.out.println("\nABONADOS");
			while(abonados.next()){
				String DNI = abonados.getString(1);
				String nombre = abonados.getString(2);
				String apellidos = abonados.getString(3);
				String num_abono = abonados.getString(4);
				String correo = abonados.getString(5);
				System.out.println("Nombre (NºAbono - DNI): "+apellidos+","+nombre+" (Nº"+num_abono+" - "+DNI+") ["+correo+"]");
			}
		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
	}



  public void menuAbonados() throws SQLException{
    try{

      Scanner input = new Scanner(System.in);
      Statement statement = connection.createStatement();
      int opcion;

      boolean terminar = false;

      while (!terminar){
				System.out.println("\nMENÚ ABONADOS");
        System.out.println("\nIntroduzca una opcion del 1 al 7:");
        System.out.println("\t1-Dar de alta abonado.");
        System.out.println("\t2-Dar de baja abonado.");
        System.out.println("\t3-Mostrar información de un abonado.");
        System.out.println("\t4-Mostrar listado de abonados.");
        System.out.println("\t5-Envío noticias.");
        System.out.println("\t6-Mostrar asientos ocupados.");
        System.out.println("\t7-Volver al menú principal.");

        opcion = input.nextInt();

        switch(opcion){
          case 1:

		  Savepoint CheckPoint1 = connection.setSavepoint("AltaAbonados");

			boolean fin=false;
			while(!fin){
				altaAbonado(statement);

				int op=0;
				while(op!=1 && op!=2 && op!=3){
					System.out.println("\nIntroduzca:");
					System.out.println("\t1-Para seguir dando de alta.");
					System.out.println("\t2-Si quiere cancelar la alta de abonados.");
					System.out.println("\t3-Para guardar los abonados dados de alta.");
					op = input.nextInt();
				}

				switch(op){
					case 1:
						break;
					case 2:
						connection.rollback(CheckPoint1);
						fin=true;
						break;
					case 3:
						connection.commit();
						fin=true;
						break;
					default:
						break;
				}
			}
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
						envioNoticias(statement);
				  break;

          case 6:
					  mostrarAsientosOcupadosAbono(statement);
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




  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////
  //
  //              TIENDA
  //
  ////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////


  // VENDER ITEMS
  public void venderItems(Statement statement) throws SQLException{
    try{
		  Scanner input = new Scanner(System.in);
		  Scanner input1 = new Scanner(System.in);

			// Decir que cliente eres pprimero
			System.out.println("Para efectuar la compra tiene que ser cliente. Díganos el DNI del cliente:");
			String DNI;
			do{
				System.out.print("DNI (formato 11111111X) :");
				DNI = input.nextLine();
			}while(!DNI.matches("[0-9]{8}[a-zA-Z]"));

			Savepoint CheckPoint1 = connection.setSavepoint("VenderItems");

			int opcion=0;
			while(opcion!=-1){
				//COMPROBAR QUE EL CÓDIGO ES CORRECTO
				mostrarInventario(statement);
				System.out.println("Seleccione el producto a vender:");
				String cproducto = input.nextLine();
				ResultSet comprobar_cproducto= statement.executeQuery("SELECT * FROM ProductoActivo WHERE CProducto=" + cproducto );

				if(!comprobar_cproducto.next()){
					System.out.println("No tenemos ese producto en el Inventario para vender. Volviendo al menú de tienda...");
					return;
				}

				ResultSet cantidad= statement.executeQuery("SELECT Cantidad FROM ProductoActivo WHERE CProducto=" + cproducto );
				cantidad.next();
				String ccantidad = cantidad.getString(1);
				String vendidos;
				do{
					System.out.println("Seleccione cuántos ítems se han vendido:");
					vendidos = input.nextLine();
				}while(!isNumeric(vendidos) && (Integer.parseInt(vendidos)>Integer.parseInt(ccantidad) || Integer.parseInt(vendidos)<=0) );

				// DISPARADOR
				//statement.executeUpdate("UPDATE ProductoActivo SET Cantidad=Cantidad-" +vendidos+ " WHERE CProducto=" + cproducto );
				try{
					statement.executeUpdate("INSERT INTO CompraTienda VALUES('"+DNI+"',"+cproducto+","+vendidos+")");
				}catch(SQLException e){
					//statement.executeUpdate("UPDATE ProductoActivo SET Cantidad=Cantidad-" +vendidos+ " WHERE CProducto=" + cproducto );
				}
				System.out.println("\nIntroduzca:");
				System.out.println("\t-1-Si no quiere seguir registrando las ventas.");
				System.out.println("\t1-Para seguir registrando las ventas.");
				opcion = input1.nextInt();
			}

			int op=0;
			while(opcion!=1 && op!=2){
				System.out.println("\nIntroduzca:");
				System.out.println("\t1-Si quiere cancelar las ventas.");
				System.out.println("\t2-Para guardar las ventas realizadas.");

				op = input1.nextInt();

			}

			if(op==1){
				connection.rollback(CheckPoint1);
			}
			if(op==2){
				connection.commit();
			}


		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

   // REALIZAR PEDIDO
  public void realizarPedido(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);
		// SAVEPOINT
		Savepoint CheckPoint = connection.setSavepoint("RealizarPedido");
	  	//COMPROBAR QUE EXISTE DICHO PROVEEDOR
			ResultSet lista_proveedores = statement.executeQuery("SELECT CProveedor FROM Proveedor");
			System.out.println("LISTA PROVEEDORES");
			while(lista_proveedores.next()){
				System.out.println("\t"+lista_proveedores.getString(1));
			}
			System.out.println("Introduzca el proveedor al que se realizar el pedido:");
      		String cproveedor = input.nextLine();
			ResultSet comprobarproveedor = statement.executeQuery("SELECT * FROM Proveedor WHERE CProveedor="+cproveedor);
			while(!comprobarproveedor.next()){
				System.out.println("No existe ningún proveedor con ese código. Por favor introduce un código de proveedor existente:");
				cproveedor = input.nextLine();
				comprobarproveedor = statement.executeQuery("SELECT * FROM Proveedor WHERE CProveedor="+cproveedor);
			}


			statement.executeUpdate("INSERT INTO Pedido_Envio VALUES(sec_codigoenvio.nextval," + cproveedor +")");

		  boolean anadir_producto = true;
		  int contador=0;

			String cproducto;
			String pedir_cantidad;
		  while(anadir_producto){

				ResultSet lista_cproductos = statement.executeQuery("SELECT CProducto FROM ProductoActivo");
				System.out.println("\nLISTA PRODUCTOS");
				while(lista_cproductos.next()){
					System.out.println("\t"+lista_cproductos.getString(1));
				}
				System.out.println("Introduzca el código del producto que se quiere pedir (-1 si quiere dejar de añadir productos):");
				cproducto = input.nextLine();
				ResultSet comprobar_cproductos = statement.executeQuery("SELECT CProducto FROM ProductoActivo WHERE CProducto="+cproducto);
				while(!comprobar_cproductos.next() && !cproducto.matches("-1") && contador>0){
					System.out.println("El código del producto no es válido. Seleccione uno de los posibles. (-1 si quiere dejar de añadir productos):");
					cproducto = input.nextLine();
					comprobar_cproductos = statement.executeQuery("SELECT CProducto FROM ProductoActivo WHERE CProducto="+cproducto);
				}

				if( (cproducto.matches("-1")) && (contador>0) ){
					anadir_producto = false;
				}else{
					do{
			  			System.out.println("Introduzca la cantidad del producto que se quiere pedir:");
	      				pedir_cantidad = input.nextLine();
		  			}while(!isNumeric(pedir_cantidad) && (Integer.parseInt(pedir_cantidad)<=0) );
	      			statement.executeUpdate("INSERT INTO Contiene VALUES("+ cproducto+",sec_codigoenvio.currval," + pedir_cantidad +")");
					contador++;
				}

	  	}

			int opcion=0;
			while(opcion!=1 && opcion!=2){
				System.out.println("\nIntroduzca:");
				System.out.println("\t1-Si quiere cancelar el pedido.");
				System.out.println("\t2-Para aceptar el pedido.");
				opcion = input.nextInt();
			}



				if(opcion==1){
					connection.rollback(CheckPoint);
				}
				if(opcion==2){
					connection.commit();
				}

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };


   // ALTA PRODUCTO
  public void altaProducto(Statement statement) throws SQLException{
    try{
      Scanner input = new Scanner(System.in);

      System.out.println("Introduzca el código asociado al nuevo producto:");
      String cproducto = input.nextLine();

      ResultSet salida = statement.executeQuery("SELECT CProducto FROM ProductoActivo WHERE cproducto=" +cproducto);
			while(salida.next()){
			  System.out.println("Ya existe un producto con dicho código, introduzca uno nuevo: ");
			  cproducto = input.nextLine();
			  salida = statement.executeQuery("SELECT CProducto FROM Producto WHERE cproducto=" +cproducto);
		  }


      String cantidad;
			do{
			  System.out.println("Introduzca la cantidad disponible del nuevo producto:");
    	  cantidad = input.nextLine();
		  }while(!isNumeric(cantidad));

			String precio;
		  do{
			  System.out.println("Introduzca el precio inicial del nuevo producto:");
        precio = input.nextLine();
		  }while(!isNumeric(precio) && Integer.parseInt(precio)<=0);

			try{
				statement.executeUpdate("INSERT INTO ProductoActivo VALUES("+ cproducto +"," + cantidad +"," + precio + ")");
			}catch(SQLException e){
				System.out.println("El producto "+cproducto+ " ya era un producto activo");
			}

    }catch(SQLException e){
      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
    }
  };

  // BAJA PRODUCTO
  public void bajaProducto(Statement statement) throws SQLException{
    try{
	  Scanner input = new Scanner(System.in);
		// SAVEPOINT
		Savepoint CheckPoint = connection.setSavepoint("BajaProducto");
     mostrarInventario(statement);

	System.out.println("Seleccione el producto que va a dejar de ser activo:");
    String cproducto = input.nextLine();

		ResultSet comprobar = statement.executeQuery("SELECT CPedido FROM Contiene WHERE CProducto="+cproducto);
		boolean pedido_en_curso = comprobar.next();
		if(!pedido_en_curso){
			statement.executeUpdate("DELETE FROM ProductoActivo WHERE CProducto=" + cproducto );


		int opcion=0;
		while(opcion!=1 && opcion!=2){
			System.out.println("\nIntroduzca:");
		System.out.println("\t1-Si quiere cancelar la baja del producto.");
		System.out.println("\t2-Para aceptar la baja del producto.");

		opcion = input.nextInt();

		}

				if(opcion==1){
					connection.rollback(CheckPoint);
				}
				if(opcion==2){
					connection.commit();
				}
	}else{
		ArrayList<String> cpedido = new ArrayList<String>();
		cpedido.add(comprobar.getString(1));

		while(comprobar.next()){
			cpedido.add(comprobar.getString(1));
		}
		String salida="¡ATENCIÓN! Hay pedidos (Pedidos: ";
		for(int i=0; i<cpedido.size();i++){
			salida+=cpedido.get(i)+", ";
		}
		salida+=") de este producto y no se podrá de dar de baja hasta que se reciban los pedidos de este producto.\n";
		System.out.println(salida);
	}

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };


  // MOSTRAR INVENTARIO
  public void mostrarInventario(Statement statement) throws SQLException{
    try{
      ResultSet inventario = statement.executeQuery("SELECT * FROM ProductoActivo ");
			System.out.println("\nCÓDIGO\tUDS\tPRECIO(€)");
      while(inventario.next()){
      	System.out.println(inventario.getString(1)+"\t"+inventario.getString(2)+"\t"+inventario.getString(3));
      }

		}catch(SQLException e){
			System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		}
  };

   // RECIBIR PEDIDO
 public void recibirPedido(Statement statement) throws SQLException{
    try{
	  	Scanner input = new Scanner(System.in);
		  // SAVEPOINT
		  Savepoint CheckPoint = connection.setSavepoint("RecibirPedido");

			System.out.println("LISTA PEDIDOS PENDIENTES DE RECEPCIÓN");
      ResultSet pedidos = statement.executeQuery("SELECT * FROM Pedido_Envio ");
      while(pedidos.next()){
      	System.out.println(pedidos.getString(1)+" "+pedidos.getString(2));
      }

	  System.out.println("Seleccione el pedido que se ha recibido:");
      String cpedido = input.nextLine();
	  ResultSet comprobarpedido = statement.executeQuery("SELECT CPedido FROM Pedido_Envio WHERE CPedido="+cpedido );
			while(!comprobarpedido.next()){
				System.out.println("No existe ningún pedido pendiente con ese código. Por favor introduce un código de pedido existente:");
				cpedido = input.nextLine();
				comprobarpedido = statement.executeQuery("SELECT CPedido FROM Pedido_Envio WHERE CPedido="+cpedido);
			}

	  ResultSet producto= statement.executeQuery("SELECT * FROM Contiene WHERE CPedido=" + cpedido );
	  ArrayList<String> cantidad_recibida = new ArrayList<String>();
	  ArrayList<String> cproducto = new ArrayList<String>();
		while(producto.next()){

			cantidad_recibida.add(producto.getString(3));
			cproducto.add(producto.getString(1));
		}

		for(int i=0; i<cantidad_recibida.size(); i++){
			statement.executeUpdate("UPDATE ProductoActivo SET Cantidad=Cantidad+" +cantidad_recibida.get(i) + " WHERE CProducto=" + cproducto.get(i) );
		}
	  	statement.executeUpdate("DELETE FROM Contiene WHERE CPedido=" + cpedido );
      statement.executeUpdate("DELETE FROM Pedido_Envio WHERE CPedido=" + cpedido );

			int opcion=0;
			while(opcion!=1 && opcion!=2){
				System.out.println("\nIntroduzca:");
				System.out.println("\t1-Si quiere cancelar recibir el pedido.");
				System.out.println("\t2-Para aceptar la recepcion del pedido.");

				opcion = input.nextInt();
			}


				if(opcion==1){
					connection.rollback(CheckPoint);
				}
				if(opcion==2){
					connection.commit();
				}

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
				System.out.println("\nMENÚ TIENDA");
				System.out.println("\nIntroduzca una opcion del 1 al 7:");
	      System.out.println("\t1-Vender ítems.");
	      System.out.println("\t2-Realizar pedido.");
	      System.out.println("\t3-Dar de alta productos.");
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
		  		Savepoint CheckPoint1 = connection.setSavepoint("AltaProductos");

						boolean fin=false;
						while(!fin){
							altaProducto(statement);

							int op=0;
							if(op!=1 && op!=2 && op!=3){
								System.out.println("\nIntroduzca:");
								System.out.println("\t1-Para seguir dando de alta.");
								System.out.println("\t2-Para cancelar la operación.");
								System.out.println("\t3-Para guardar y finalizar.");
								op = input.nextInt();
							}

							switch(op){
								case 1:
									break;
								case 2:
									connection.rollback(CheckPoint1);
									fin=true;
									break;
								case 3:
									connection.commit();
									fin=true;
									break;
								default:
									break;
							}
						}

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





	  ////////////////////////////////////////////////////////////////////////////////
	  ////////////////////////////////////////////////////////////////////////////////
	  //
	  //              MANTENIMIENTO
	  //
	  ////////////////////////////////////////////////////////////////////////////////
	  ////////////////////////////////////////////////////////////////////////////////
			public void resetBBDD(Statement statement) throws SQLException{
				eliminarTablas();
				crearTablas();
				inicializar();
				System.out.println("Reseteo completado.");
			};

		  public void menuMantenimiento() throws SQLException{
		    try{

		      Scanner input = new Scanner(System.in);
		      Statement statement = connection.createStatement();
		      int opcion;

		      boolean terminar = false;

		      // SAVEPOINT
      			Savepoint CheckPoint = connection.setSavepoint("SinDetalles");


		      while (!terminar){
						System.out.println("\nMENÚ MANTENIMIENTO");
						System.out.println("\nIntroduzca una opcion:");
						System.out.println("\t1-Resetear BBDD (Borrar tablas, crearlas e insertar tuplas iniciales).");
						System.out.println("\t2-Limpiar componentes con contrato expirado.");
			      System.out.println("\t3-Volver al menú principal.");

		        opcion = input.nextInt();

		        switch(opcion){
		          case 1:
		              resetBBDD(statement);
		          break;

		          case 2:
									desactivarContratosExpirados(statement);
						  break;

							case 3:
									terminar=true;
							break;

						  default:
						      System.out.println("Esta opción no es válida. Por favor, introduzca un número válido");
						  break;

		        }
		      }

		      connection.commit();
		    }catch(SQLException e){
		      System.err.format("SQL State: %s\n%s\n", e.getSQLState(), e.getMessage());
		    }
		  };

			}
