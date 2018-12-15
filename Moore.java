import java.util.Hashtable;
import java.util.LinkedList;

public class Moore {

  String inicial;
  LinkedList<String> estados;
  LinkedList<String[]> transiciones;
  Hashtable<String, String> salidas;

  public Moore(){
    inicial = "";
    this.estados = new LinkedList<String>();
    this.transiciones = new LinkedList<String[]>();
    this.salidas = new Hashtable<String, String>();
  }

  public void setInicial(String inicial){
    this.inicial = inicial;
  }

  public String getInicial(){
    return this.inicial;
  }

  public LinkedList<String> getEstados(){
    return this.estados;
  }

  public LinkedList<String[]> getTransiciones(){
    return this.transiciones;
  }

  public Hashtable<String, String> getSalidas(){
    return this.salidas;
  }

}
