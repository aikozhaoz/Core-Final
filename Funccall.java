import java.util.*;

public class Funccall {

    String id;
    Formals formals;
    ArrayList<String> actualParam;

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

    public void execute(Scanner inputScanner) {
        String funcName = id;
        // Check if function is declared before execution
        if(Memory.functionDeclaration.containsKey(funcName)){
            Utility.UseUndeclaredFunctionError(funcName);
            System.exit(-1);
        }

        Funcdecl function = Memory.functionDeclaration.get(funcName);

        // Getting all the actual parameters.
        actualParam = new ArrayList<String>();
        formals.execute(actualParam);
        // Check if the amount of the formal parameters == the amount of the actual parameters.
        if(function.formalParameters.size() != actualParam.size()){
            Utility.unmatchingFunctionParameter(funcName);
            System.exit(-1);
        }
        HashMap<String, Corevar> funcSpace = new HashMap<String, Corevar>();
        Stack<HashMap<String, Corevar>> mainstack = new Stack<HashMap<String, Corevar>>();
        for(int i=0; i<actualParam.size(); i++){
            String key = function.formalParameters.get(i);
            Corevar param = new Corevar();
            for (HashMap<String, Corevar> m: mainstack){
                if (m.containsKey(actualParam.get(i))){
                    param.setCorevar(Core.REF, m.get(actualParam.get(i)).value);
                }
            }
            funcSpace.put(key, param);
        }
        Stack<HashMap<String, Corevar>> funcstack = new Stack<HashMap<String, Corevar>>();
        Memory.stackSpace.push(funcstack);
        function.getFunctionBody().execute(inputScanner);
        Memory.stackSpace.pop();
    }

    public void print(){
        System.out.print("begin "+id+" ( ");
        formals.print();
        System.out.print(" );");
    }
}
