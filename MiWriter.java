import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Enumeration;
import java.io.*;

public class MiWriter {

  public MiWriter(){
  }

  public boolean comprobaciones(String actual_n, Moore actual, Hashtable<String, String> eventos_table, Hashtable<String, String> codigo_table){
    boolean error = false;
    LinkedList<String> estados = actual.getEstados();
    for (String estado: estados){
      if (!codigo_table.containsKey(actual.getSalidas().get(estado))){
        System.err.println("MOOR ERROR: Comportamiento '"+actual.getSalidas().get(estado)+"' no declarado, máquina '"+actual_n+"'");
        error = true;
      }
    }

    LinkedList<String[]> transiciones = actual.getTransiciones();
    for (String[] transicion: transiciones){
      if (!estados.contains(transicion[0])){
        error = true;
        System.err.println("MOOR ERROR: Estado '"+transicion[0]+"' no declarado en transicion ["+transicion[0]+", "+transicion[1]+", "+transicion[2]+"], máquina '"+actual_n+"'");
      }
      if (!estados.contains(transicion[1])){
        error = true;
        System.err.println("MOOR ERROR: Estado '"+transicion[1]+"' no declarado en transicion ["+transicion[0]+", "+transicion[1]+", "+transicion[2]+"], máquina '"+actual_n+"'");
      }
      if (!eventos_table.containsKey(transicion[2])) {
        error = true;
        System.err.println("MOOR ERROR: Evento no declarado en transicion ["+transicion[0]+", "+transicion[1]+", "+transicion[2]+"], máquina '"+actual_n+"'");
      }
    }
    return error;
  }

  public void write(String actual, Moore maquina_actual, Hashtable<String, String> eventos_table, Hashtable<String, String> codigo_table){
    String nombre_archivo = actual+".py";
    PrintWriter writer = null;
    try{
      writer = new PrintWriter(nombre_archivo, "UTF-8");
    }catch (FileNotFoundException fnf){
      //
    }catch (Exception e){
      System.out.println("Exception desconocida: "+ e.getMessage());
    }
    write_cabecera(writer, actual, codigo_table);
    write_output(writer, maquina_actual, codigo_table);
    write_transiciones(writer, maquina_actual, eventos_table);
    write_main(writer, maquina_actual);
    writer.close();
  }

  public void write_cabecera(PrintWriter writer, String actual, Hashtable<String, String> codigo_table){
    // Codigo usuario
    writer.println("\n__debugMoor__ = True\n");
    writer.println("# Sección código-usuario #############");
    String codigo = codigo_table.get(actual);
    if(codigo==null) codigo = "";
    else codigo = codigo.substring(2, codigo.length()-2);
    writer.println(codigo);
    writer.println("######################################");

    // procesar_entrada
    writer.println("\nimport sys\n\ndef procesar_entrada(str):\n\tsplitted = str.split(',')\n\tcpy = []");
    writer.println("\tfor elem in splitted:\n\t\telem = elem.replace('\\t', '')\n\t\telem = elem.replace(' ', '')");
    writer.println("\t\tcpy.append(elem)\n\treturn cpy\n\n");
  }

  public void write_output(PrintWriter writer, Moore actual, Hashtable<String, String> codigo_table){
    // output
    writer.println("def output(estado):");
    Hashtable<String, String> outputs = actual.getSalidas();
    Enumeration<String> estados = outputs.keys();
    String estado_actual = estados.nextElement();
    String linea = "\tif estado == "+"'"+estado_actual+"':";
    writer.println(linea);
    String comp_id = actual.getSalidas().get(estado_actual);

    // Debemos procesar linea a linea de codigo
    String codigo = codigo_table.get(comp_id);
    codigo = procesar_codigo(codigo);
    writer.println(codigo);

    while (estados.hasMoreElements()){
      estado_actual = estados.nextElement();
      linea = "\telif estado == "+"'"+estado_actual+"':";
      writer.println(linea);
      comp_id = actual.getSalidas().get(estado_actual);
      codigo = codigo_table.get(comp_id);
      codigo = procesar_codigo(codigo);
      writer.println(codigo);
    }
    writer.println("\telse:\n\t\tprint('Comportamiento indefinido para el estado '+estado+'. Abortando ejecución.')\n\t\tsys.exit(0)");
  }

  public void write_transiciones(PrintWriter writer, Moore actual, Hashtable<String, String> eventos_table){
    // Transiciones
    writer.println("\n\ndef transition(estado, entrada):");
    String linea = "\tif estado ==";
    LinkedList<String[]> transiciones = actual.getTransiciones();
    int index = 0;
    String estado_inicio = transiciones.get(index)[0];
    String estado_destino = transiciones.get(index)[1];
    String entrada_id = transiciones.get(index)[2];
    linea = linea + "'"+estado_inicio+"' and entrada == ";
    String entrada = eventos_table.get(entrada_id);
    entrada = procesar_entrada(entrada);
    linea = linea + entrada +":";
    writer.println(linea);
    writer.println("\t\tnuevo_estado = '"+estado_destino+"'\n\t\tif(__debugMoor__):\n\t\t\tprint('[Transicion]\\n\\t' + estado + ', '+ str(entrada) + ' ----> ' + nuevo_estado)\n\t\t\tprint('Estado actual: '+ nuevo_estado+'.\n')\n\t\treturn nuevo_estado");

    for (index = 1; index<actual.getTransiciones().size(); index++){
      linea = "\telif estado == '"+transiciones.get(index)[0]+"' and entrada == ";
      entrada_id = transiciones.get(index)[2];
      entrada = eventos_table.get(entrada_id);
      estado_destino = transiciones.get(index)[1];
      entrada = procesar_entrada(entrada);
      linea = linea + entrada + ":";
      writer.println(linea);
        writer.println("\t\tnuevo_estado = '"+estado_destino+"'\n\t\tif(__debugMoor__):\n\t\t\tprint('[Transicion]\\n\\t' + estado + ', '+ str(entrada) + ' ----> ' + nuevo_estado)\n\t\t\tprint('Estado actual: '+ nuevo_estado+'.\n')\n\t\treturn nuevo_estado");
    }
    linea = "\telse:\n\t\tprint ('Transicion no definida para el estado '+ estado +' y entrada '+ str(entrada) + '. Abortando ejecución.')\n\t\tsys.exit(0)";
    writer.println(linea);
  }

  public void write_main(PrintWriter writer, Moore actual){
    //Main
    String estado_inicial = actual.getInicial();
    writer.println("\ndef main():\n\t# Estado inicial\n\testado_actual = '"+estado_inicial+"'\n");
    writer.println("\tprint('\\nEstado inicial: '+ estado_actual+'.')\n\toutput(estado_actual)\n\tprint('-----\\n')\n");
    writer.println("\twhile True:\n\t\tentrada = input('Introduce la entrada para realizar la transicion: ')\n\t\tentrada = procesar_entrada(entrada)\n\t\testado_actual = transition(estado_actual, entrada)");
    writer.println("\t\toutput(estado_actual)\n\t\tprint('-----\\n')\n");
    writer.println("if __name__ == '__main__':\n\tmain()");
  }

  public String procesar_codigo(String codigo){
    codigo = codigo.replace("/*\n", "");
    codigo = codigo.replace("/*", "");
    codigo = codigo.replace("*/", "");
    String[] splitted = codigo.split("\n");
    codigo = "";
    for (String elem: splitted){
      codigo = codigo+"\t\t"+elem+"\n";
    }
    return codigo;
  }

  public String procesar_entrada(String entrada){
    String splitted[] = entrada.split(",");
    String entradas = "[";
    for (String elem: splitted){
      entradas = entradas + "'"+elem+"', ";
    }
    entradas = entradas.substring(0, entradas.length()-2);
    entradas = entradas + "]";
    return entradas;
  }

}
