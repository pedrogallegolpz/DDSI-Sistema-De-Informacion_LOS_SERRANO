# Los Serrano. Sistema de Informacion
Information System that provides the basic management of a soccer team.

### Requisitos
Have the below packages downloaded
​																 `javax.activation-1.2.0.jar`,  `javax.mail.jar` ,`ojdbc8.jar`

They should be located in the same directory as `executable.sh` and run from a Linux terminal. In addition, we must have access to an SQL database for it to work. The code itself generates the necessary tables and initializes them from the **MAINTENANCE** option.





### Brief description

The system consists of 4 subsystems: *Team, Events, Subscribers and Store* from where everything corresponding to the club is managed.


##### Subsystem Team
It provides us with the functionalities related to *Registering/Removing a component, renewing it and even calculating the economic balance*, the latter taking into account all the movements of money that occur in the complete system.



##### Events Subsystem

Tickets for both matches in the stadium and concerts or other shows are handled from here. We will be able to *consult calendars, sell tickets, report incidents by event...*



##### Subsystem Subscribers

It allows us to manage our subscribers. From *registration/deregistration, sending news* to our current subscribers and much more.

> It is remarkable to say that this code sends emails from a GMAIL account whose email and password are in the code. The email is not personal and was made for the sole purpose of this project. On the other hand, be careful with the emails that you add in the Subscribers because emails will be sent to all of them.



##### Store Subsystem

Club store management: *Inventory, product sales, order management...* One of the most powerful weapons in the economy of a club that could not be missing !!



##### More information

Review the file `losserrano.pdf`. Information is given about the selection process of functional requirements and their definition, entity relationship schemas, external schemas, data flow diagrams, passage to tables and others. A long process that leads to this incredible project.

### Execute

1. Firstly, go to the file `main.java` and write the  *`database_url`, `user`* y *`password`.*

2. Execute

   > ./ejecutable.sh

3. Within the interface we can create the tables by going to the option **6- MANTENIMIENTO**.

4. Clicking on **1-Resetear BBDD (Borrar tablas, crearlas e insertar tuplas iniciales)** we'll have our database ready to be used.





### Additional

In our case we made a user interface which we are not going to share in this repository other than the image. It was developed through JDeveloper Studio following its initial tutorial, so the difficulty of this interface is minimal, so I invite you to do it yourself!

![Image text](https://github.com/pedrogallegolpz/DDSI-Sistema-De-Informacion_Los_Serrano/blob/master/interfaz.PNG)
