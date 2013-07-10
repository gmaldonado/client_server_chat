#Compilar

para compilar ambas clases se debe hacer

javac ClientChat.java
javac ServerChat.java

Para ejecutar

#Ejecutar servidor

para ejecutar el servidor simplemenete se debe hacer

java ServerChat

#Ejecutar cliente

para ejecutar el cliente se debe hacer

java ClientChat

una vez que el cliente inicie pedirá los parámetros para:

-IP del servidor
-IP del cliente
-Nickname

Luego de esto dirá welcome si todo salió bien (en caso contario lanzará un error). Tampoco se pueden usar nick repetidos para 
usuarios que ya están en el sistema.

#Modo de uso

para utilizar se puede:

enviar mensaje:
para enviar un mensaje se debe utilizar

msg-nickname-mensaje real

la palabra msg siempre se pone, luego va el nickname de aquien se le envia el mensaje y finalmente el mensaje 
que se desea enviar. TODO debe ir separado por un "-" (sin comillas).

desconectar:

para desconectarse hay que escribir "disconnect" (sin comillas)

listar usuarios conectados:

para ver los usuarios conectados se debe hacer "users" (sin comillas)

#Nota

Si un nodo se cae y se le intenta enviar un mensaje, nos dará un mensaje de timeout y se eliminará de la lista de usuarios conectados.
Todos los clientes deben estar en la misma red.


