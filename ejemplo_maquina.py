# Sección código-usuario #############

# Comentario que será copiado al archivo final
#import miscosas;

def dibujar():
  print("Hello World")

######################################

import sys

def procesar_entrada(str):
    splitted = str.split(",")
    cpy = []
    for elem in splitted:
        # Eliminamos espacios iniciales. Esto evita errores cuando el usuario ha introducido espacios
        # tras comas para una mejor visualización (Ej: a, b, c --> a,b,c)
        if elem[0] == " " or elem[0] == "\t":
            elem = elem[1:]
        cpy.append(elem)
    return cpy


def output(estado):
    if estado == "e1":
        dibujar()
    elif estado == "e2":
        print("Esto es el comportamiento 2")
    elif estado == "e3":
        print("Esto es el comportamiento 2")
    elif estado == "e4":
        dibujar()
    else:
        print("Comportamiento indefinido para el estado "+estado+". Abortando ejecución.")
        sys.exit(0)


def transition(estado, entrada):
    if estado == "e1" and entrada == ['a', 'b', 'c']:
        nuevo_estado = "e2"
        print("[Transicion]\n\t" + estado + ", "+ str(entrada) + " ----> " + nuevo_estado+"\n")
        return nuevo_estado
    elif estado == "e2" and entrada == ['a', 'b']:
        nuevo_estado = "e3"
        print("[Transicion]\n\t" + estado + ", "+ str(entrada) + " ----> " + nuevo_estado+"\n")
        return nuevo_estado
    else:
        print ("Transicion no definida para el estado "+ estado +" y entrada "+ str(entrada) + ". Abortando ejecución.")
        sys.exit(0)


# Estado inicial
estado_actual = "e1"

print("\nEstado actual: "+ estado_actual+".")
output(estado_actual)
print("-----\n")

while True:
    # Obtenemos la entrada, que se almacena literalmente como una cadena, NO como lista
    entrada = input("Introduce la entrada para realizar la transicion: ")
    entrada = procesar_entrada(entrada)
    estado_actual = transition(estado_actual, entrada)
    print("Estado actual: "+ estado_actual+".")
    output(estado_actual)
    print("-----\n")
