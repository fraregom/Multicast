# Multicast
  
   Francisco Reyes --  201473117-5
   
   Diego Zamora    --  201473076-4
    
  ---

   
   * Variables
        * Temperatura
        * Humedad
        * Presión
        
   * Tiempo 
        * Las mediciones son emitidas cada 5 segundos
        
        
   * Puertos de conección
        * 90000 : Recuperación de mensajes
        * 10001 : Temperatura
        * 10002 : Humedad
        * 10003 : Presión
   * Dirección IP Multicast por defecto
   
        * 224.0.0.1    
    
   * Especificaciones
        * [-R] : Variable opcional para recuperar el historial
        * 
    
   
   * Ejemplo del Cliente:
   
            user@user:∼$ java client <ip multicast> 101 -R
   
    