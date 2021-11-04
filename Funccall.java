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
        // System.out.println("Funccall class: Function call starts");
        String funcName = id;
        // Check if function is declared before execution
        if(!Memory.functionDeclaration.containsKey(funcName)){
            Utility.UseUndeclaredFunctionError(funcName);
            System.exit(-1);
        }

        // Get the corresponding function
        Funcdecl function = Memory.functionDeclaration.get(funcName);

        // Getting all the actual parameters.
        actualParam = new ArrayList<String>();
        formals.execute(actualParam);
        // Check if the amount of the formal parameters == the amount of the actual parameters.
        if(function.formalParameters.size() != actualParam.size()){
            Utility.unmatchingFunctionParameter(funcName);
            // System.out.println("Formal parameter size: "+function.formalParameters.size());
            System.exit(-1);
        }
        // Create FuncSpace and get mainstack
        HashMap<String, Corevar> funcSpace = new HashMap<String, Corevar>();
        Stack<HashMap<String, Corevar>> mainstack = Memory.stackSpace.peek();
        // System.out.println("Funccall class: 1st. actual parameter value: "+actualParam.get(0));
        // System.out.println("Funccall class: 1st. formal parameter value: "+function.formalParameters.get(0));
        for(int i=0; i<actualParam.size(); i++){
            String key = function.formalParameters.get(i);
            // System.out.println("Funccall class: ith. formal parameter: "+key);
            Corevar param = new Corevar();
            boolean inGlobal = true;
            // System.out.println("actual parameter value: "+actualParam.get(i));
            for (HashMap<String, Corevar> m: mainstack){
                // System.out.println("actual parameter value: "+actualParam.get(i));
                if (m.containsKey(actualParam.get(i))){
                    inGlobal = false;
                    // param.setCorevar(Core.REF, m.get(actualParam.get(i)).value);
                    param = m.get(actualParam.get(i));
                    // System.out.println("actual parameter value: "+actualParam.get(i));
                }
            }
            if (inGlobal){
                // param.setCorevar(Core.REF, Memory.globalSpace.get(actualParam.get(i)).value);
                param = Memory.globalSpace.get(actualParam.get(i));
            }
            // System.out.println("formal parameter value: "+param.type);
            funcSpace.put(key, param);
        }
        Stack<HashMap<String, Corevar>> funcstack = new Stack<HashMap<String, Corevar>>();
        funcstack.push(funcSpace);
        Memory.stackSpace.push(funcstack);
        // System.out.println("before execution");
        // System.out.println("left id: " + Memory.stackSpace.peek().contains(funcName));
        function.getFunctionBody().execute(inputScanner);
        Memory.stackSpace.pop();
    }

    public void print(){
        System.out.print("begin "+id+" ( ");
        formals.print();
        System.out.print(" );");
    }
}
