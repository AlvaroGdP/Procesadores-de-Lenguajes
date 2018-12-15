lexer grammar Moor_Lex;

CODE_TOKEN :'code';
BEHAVIOURS : 'behaviours';
EVENTS : 'events' -> pushMode(BLOQUE_EVENTOS);
MOORE : 'moore';
STATES : 'states';
OUTPUT : 'output';
TRANSITIONS : 'transitions';
LLAVE_I : '{' ;
LLAVE_D : '}' ;
PAR_I : '(' ;
PAR_D : ')' ;
PUNTO_COMA : ';' ;
ASIGNACION : ':=' ;
COMA : ',' ;
ID : [a-zA-Z][a-zA-Z0-9_]* ;
CODIGO : '/*' ~[*/]* '*/';
COMENTARIO : '#' ~[\r\n\f]* -> skip;
COMENTARIO_ML : '\'\'\'' ~['\'\'\'']* '\'\'\'' ->type(COMENTARIO),skip;
NL  : ('\n' | '\r') ->skip ;
BLANCO : ' ' -> skip;
TAB :  '\t' -> skip;


mode BLOQUE_EVENTOS;
LLAVE_I2 : '{'                ->type(LLAVE_I);
LLAVE_D2 : '}'                ->popMode,type(LLAVE_D);
PUNTO_COMA2 : ';'             ->type(PUNTO_COMA);
ASIGNACION2 : ':='            ->pushMode(EVENTO), type(ASIGNACION);
ID2 : [a-zA-Z][a-zA-Z0-9_]*   ->type(ID);
COMENTARIO2 : '#' ~[\r\n\f]*  ->type(COMENTARIO),skip;
COMENTARIO_ML2 : '\'\'\'' ~['\'\'\'']* '\'\'\'' ->type(COMENTARIO),skip;
NL2  : ('\n' | '\r')           ->type(NL),skip;
BLANCO2 : ' '                  -> type(BLANCO), skip;
TAB2 :  '\t'                   -> type(TAB),skip;

mode EVENTO;
PAR_I3 : '('                  ->type(PAR_I) ;
PAR_D3 : ')'                  ->type(PAR_D), popMode;
COMA3 : ','                   ->type(COMA);
ENTRADA : [a-zA-Z0-9]+ ;
NL3  : ('\n' | '\r')          -> type(NL), skip;
BLANCO3 : ' '                 -> type(BLANCO), skip;
TAB3 :  '\t'                  -> type(TAB), skip;
