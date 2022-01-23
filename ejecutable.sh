#!/bin/bash

javac -cp ./ojdbc8.jar:./javax.mail.jar:./javax.activation-1.2.0.jar:. main.java
java -cp ./ojdbc8.jar:./javax.mail.jar:./javax.activation-1.2.0.jar:.  main
