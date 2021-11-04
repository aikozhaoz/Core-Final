import java.util.*;

public class Funccall {

    String id;
    Formals formals;

    Funccall(){
        id = "";
        formals = null;
    }

    public void parse(Scanner S) {
        // <func-call> ::= begin id ( <formals> )
        if (!S.expectedToken(Core.BEGIN)) {
            Utility.expectedhelper(Core.BEGIN, S.currentToken());
            System.exit(-1);
        }

        if (S.currentToken()==Core.ID) {
            id = S.getID();
            S.nextToken();
        }else {
            Utility.expectedhelper(Core.ID, S.currentToken());
            System.exit(-1);
        }

        if (!S.expectedToken(Core.LPAREN)) {
            Utility.expectedhelper(Core.LPAREN, S.currentToken());
            System.exit(-1);
        }

        formals = new Formals();
        formals.parse(S);
        
        if (!S.expectedToken(Core.RPAREN)) {
            Utility.expectedhelper(Core.RPAREN, S.currentToken());
            System.exit(-1);
        }
        if (!S.expectedToken(Core.SEMICOLON)) {
            Utility.expectedhelper(Core.SEMICOLON, S.currentToken());
            System.exit(-1);
        }
    }

    public void execute() {
        Funcdecl function = Memory.functionDeclaration.get(id);
        // Formals formalParameters = function.getFormals();
        // for (x:formals){

        // }
        
        Stack<HashMap<String, Corevar>> funcstack = new Stack<HashMap<String, Corevar>>();

    }

    public void print(){
        System.out.print("begin "+id+" ( ");
        formals.print();
        System.out.print(" );");
    }
}
