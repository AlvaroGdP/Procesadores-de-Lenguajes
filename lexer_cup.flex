/* --------------------------Seccion codigo-usuario ------------------------*/
import java_cup.runtime.*;
import java.io.*;
import java.util.*;

%%
/* -----------------Seccion de opciones y declaraciones -----------------*/
/*--OPCIONES --*/
/* Nombre de la clase generada para el analizadorlexico */
%class analex_cup
%cupsym sym

/* Habilitar la compatibilidad con el interfaz CUP para el generador sintáctico */
%cup

/* Posibilitar acceso a la columna y fila actual de analisis*/
%line
%column

/* Codigo definido con el fin de que los token contengan info de linea y columna */
%{
     private Symbol symbol(int type) {
         return new Symbol(type, yyline, yycolumn);
     }
     private Symbol symbol(int type, Object value) {
         return new Symbol(type, yyline, yycolumn, value);
     }
%}

/* Definimos más estados*/
%state BLOQUE_EVENTOS
%state EVENTO

/* Declaraciones de macros */

LLAVE_I = \{
LLAVE_D = \}
PAR_I = \(
PAR_D = \)
PUNTO_COMA = \;
ASIGNACION = \:=
COMA = \,
ID = [a-zA-Z][a-zA-Z0-9_]*
ENTRADA = [a-zA-Z0-9]+
CODIGO = "/*" [^*/]* "*/"
COMENTARIO = #[^\r\n]* |'''[^''']*'''
NL  = \n | \r | \r\n
BLANCO = " "
TAB =  \t

%%
/* ------------------------Seccion de reglas y acciones ----------------------*/
<YYINITIAL>{

  "behaviours"        {return symbol(sym.BEHAVIOURS);}
  "code"              {return symbol(sym.CODE_TOKEN);}
  "events"            {yybegin(BLOQUE_EVENTOS); return symbol(sym.EVENTS);}
  "main"              {return symbol(sym.MAIN);}
  "states"            {return symbol(sym.STATES);}
  "transitions"       {return symbol(sym.TRANSITIONS);}
  "output"            {return symbol(sym.OUTPUT);}
  "moore"             {return symbol(sym.MOORE);}
  {LLAVE_I}           {return symbol(sym.LLAVE_I);}
  {LLAVE_D}           {return symbol(sym.LLAVE_D);}
  {PAR_I}             {return symbol(sym.PAR_I);}
  {PAR_D}             {return symbol(sym.PAR_D);}
  {PUNTO_COMA}        {return symbol(sym.PUNTO_COMA);}
  {ASIGNACION}        {return symbol(sym.ASIGNACION);}
  {COMA}              {return symbol(sym.COMA);}
  {ID}                {return symbol(sym.ID, new String(yytext()));}
  {CODIGO}            {return symbol(sym.CODIGO, new String(yytext()));}
  {NL}                {/* Obviar salto de linea */}
  {BLANCO}            {/* Obviar espacios */}
  {TAB}               {/* Obviar tabuladores */}
  {COMENTARIO}        {/* Obviar comentarios */}
  .                   {System.err.println("ERROR: Token Desconocido<" + yytext() +">: encontrado en linea: " + (yyline+1) + " columna: " + (yycolumn+1));}

}

<BLOQUE_EVENTOS>{

  {LLAVE_I}           {return symbol(sym.LLAVE_I);}
  {LLAVE_D}           { yybegin(YYINITIAL); return symbol(sym.LLAVE_D);}
  {PUNTO_COMA}        {return symbol(sym.PUNTO_COMA);}
  {ASIGNACION}        {yybegin(EVENTO); return symbol(sym.ASIGNACION);}
  {ID}                {return symbol(sym.ID, new String(yytext()));}
  {NL}                {/* Obviar salto de linea */}
  {BLANCO}            {/* Obviar espacios */}
  {TAB}               {/* Obviar tabuladores */}
  {COMENTARIO}        {/* Obviar comentarios */}
  .                   {System.err.println("ERROR: Token Desconocido<" + yytext() +">: encontrado en linea: " + (yyline+1) + " columna: " + (yycolumn+1));}

}

<EVENTO>{

  {PAR_I}             {return symbol(sym.PAR_I);}
  {PAR_D}             {yybegin(BLOQUE_EVENTOS); return symbol(sym.PAR_D);}
  {COMA}              {return symbol(sym.COMA);}
  {ENTRADA}           {return symbol(sym.ENTRADA, new String(yytext()));}
  {NL}                {/* Obviar salto de linea */}
  {BLANCO}            {/* Obviar espacios */}
  {TAB}               {/* Obviar tabuladores */}
  .                   {System.err.println("ERROR: Token Desconocido<" + yytext() +">: encontrado en linea: " + (yyline+1) + " columna: " + (yycolumn+1));}

}
