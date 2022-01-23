# Los Serrano. Sistema de Informacion
Sistema de Información que proporciona la gestión básica de un equipo de fútbol. 

### Requisitos
Tener descargados los paquetes

​																 `javax.activation-1.2.0.jar`,  `javax.mail.jar` ,`ojdbc8.jar`

Deben estar situados en el mismo directorio que  `ejecutable.sh` y ejecutarlo desde una terminal de Linux. Además deberemos de tener acceso a una base de datos SQL para que funcione. El propio código genera las tablas necesarias y las inicializa desde la opción **MANTENIMIENTO**.





### Descripción resumida

El sistema consta de 4 subsistemas: *Equipo, Eventos, Abonados y Tienda* desde donde se gestiona todo lo correspondiente al club.



##### Subsistema Equipo

Nos proporciona las funcionalidades relativas a *dar de Alta/Baja a un componente, renovarle e incluso calcular el balance económico*, esto último teniendo en cuenta todos los movimientos de dinero que se producen en el sistema completo.



##### Subsistema Eventos

Desde aquí se manejan las entradas tanto a los partidos en el estadio como a los conciertos o demás espectáculos. Podremos *consultar calendarios, vender entradas, reportar incidencias por evento...*



##### Subsistema Abonados

Nos permite gestionar a nuestros abonados. Desde *dar de Alta/Baja, enviar noticias a nuestros abonados actuales y mucho más*. 

> Es notable decir que este código envía correos desde una cuenta GMAIL cuyo correo y contraseña se encuentran en el código. El correo no es personal y se hizo con la única finalidad de este proyecto. Por otra parte, cuidado con los correos que añadís en los Abonados porque se enviarán correos a todos ellos.



##### Subsistema Tienda

Gestión de la tienda del club: *Inventario, venta de productos, gestión de pedidos...* Una de las armas más potentes de la economía de un club que  no podía faltar !!



##### Más información

Revisar el archivo `losserrano.pdf`. Se da información acerca del proceso de selección de requisitos funcionales y su definición, esquemas entidad relación, esquemas externos, diagramas de flujo de datos, paso a tablas y demás. Un largo proceso que desemboca en este increíble proyecto. 





### Ejecución

1. En primer lugar debemos ir al archivo `main.java` y escribir nuestra *`database_url`, `user`* y *`password`.*

2. Ya solo tendremos que ejecutar

   > ./ejecutable.sh

3. Dentro de la interfaz podremos crear las tablas dirigiéndonos a la opción **6- MANTENIMIENTO**.

4. Pulsando en **1-Resetear BBDD (Borrar tablas, crearlas e insertar tuplas iniciales)** tendremos ya la BBDD lista para ser usada.





### Adicional

En nuestro caso hicimos una interfaz de usuario la cual no vamos a compartir en este repositorio más que la imagen. Se desarrolló a través de JDeveloper Studio siguiendo su tutorial inicial, por lo que la dificultad de esta interfaz es mínima, así que os invito a hacer la misma!

![Image text](https://github.com/pedrogallegolpz/DDSI-Sistema-De-Informacion_Los_Serrano/blob/master/interfaz.PNG)
