import java.util.*;

public class Formals {
    
    int option;
    String id;
    Formals formals;

    Formals() {
        option = 0;
        id = "";
        formals = null;
    }

    public void parse(Scanner S) {
        // Option 1: <formals> ::= id
        if (S.currentToken() == Core.ID) {
            option = 1;
            id = S.getID();
            S.nextToken();
        } else {
            Utility.expectedhelper(Core.ID, S.currentToken());
            System.exit(-1);
        }
        // Option 2: <formals> ::= id , <formals>
        if (S.currentToken() == Core.COMMA) {
            option = 2;
            S.expectedToken(Core.COMMA);
            formals = new Formals();
            formals.parse(S);
        }
    }

    public void execute(ArrayList<String> parameters) {
        // System.out.println("Formal class: Right before formal calls: " + parameters);
        parameters.add(id);
        // System.out.println("Formal class: Right after formal calls: " + parameters);
        if (option == 2){
            formals.execute(parameters);
        }
    }

    public void print(){
        System.out.print(id);
        if (option == 2){
            System.out.print(", ");
            formals.print();
        }
    }
}
