# Multicast
  
 Este es un pequeño ejemplo de un sistema multicast, en el presente proyecto podemos encontrar un cliente y un servidor, este ultimo enviara este tipo de variables. 
 
   * Variables
        * Temperatura
        * Humedad
        * Presión
        
   * Tiempo 
        * Las mediciones son emitidas cada 5 segundos
        
       
   * Puertos de conexión, estos estan disponibles para un cliente, se utilizan para generar grupos multicast.
   
        * 90000 : Recuperación de mensajes
        * 10001 : Temperatura
        * 10002 : Humedad
        * 10003 : Presión
        
   * Dirección IP Multicast por defecto
   
        * 224.0.0.1    
    
   * Consideraciones
        * [-R] : Variable opcional para recuperar el historial    
        * La conexión al servidor, solo se realizara si se conoce la dirección IP de la maquina antes mencionada.      
    
   * Ejemplo del Cliente:
   
            user@user:∼$ java client 10.10.2.65 101 -R  
   * Ejemplo de Servidor:
         
               user@user:∼$ java server 224.0.0.1
       
