parser grammar Moor_Syn;
options {tokenVocab = Moor_Lex;}

programa : bloque_code comportamientos eventos cuerpo;

bloque_code : (CODE_TOKEN LLAVE_I codigo_bloque LLAVE_D);
codigo_bloque : (codigo_individual codigo_bloque | codigo_individual);
codigo_individual : ID ASIGNACION CODIGO;

comportamientos : BEHAVIOURS LLAVE_I comportamiento LLAVE_D;
comportamiento : codigo_individual | ID ASIGNACION CODIGO comportamiento;

eventos : EVENTS LLAVE_I evento LLAVE_D;
evento : ID ASIGNACION PAR_I valor PAR_D PUNTO_COMA | ID ASIGNACION PAR_I valor PAR_D PUNTO_COMA evento;
valor : ENTRADA | ENTRADA COMA valor;

cuerpo : maquinas;

maquinas : maquina maquinas | maquina;
maquina : MOORE ID LLAVE_I cuerpo_maquina LLAVE_D;
cuerpo_maquina : estados salidas transiciones;

estados : STATES estado PUNTO_COMA | STATES PAR_I estado PAR_D PUNTO_COMA;
estado : ID | ID COMA estado;

salidas : OUTPUT salida PUNTO_COMA;
salida : PAR_I tupla_salida PAR_D | PAR_I tupla_salida PAR_D COMA salida;
tupla_salida : ID COMA ID;

transiciones : TRANSITIONS transicion PUNTO_COMA;
transicion : PAR_I tripla_transicion PAR_D | PAR_I tripla_transicion PAR_D COMA transicion;
tripla_transicion : ID COMA ID COMA ID;
