import org.antlr.v4.runtime.*;                  // http://www.antlr.org/
import org.antlr.v4.runtime.tree.*;
import java.io.*;


public class Main {

  public static void main(String[] args){
    ANTLRFileStream input = null;
  try{
    input = new ANTLRFileStream(args[0]); // a character stream
  }catch (IOException e){
    //
  }
    Moor_Lex lex = new Moor_Lex(input); // transforms characters into tokens
    CommonTokenStream tokens = new CommonTokenStream(lex); // a token stream
    Moor_Syn parser = new Moor_Syn(tokens); // transforms tokens into parse trees
    parser.programa();
  }

}
