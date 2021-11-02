import java.util.*;

public class Out {

    public String line;

    Expr expr;

    Out() {
        line = "";
        expr = null;
    }

    public void parse(Scanner S) {
        // <out> ::= output <expr> ;
        if (!S.expectedToken(Core.OUTPUT)) {
            Utility.expectedhelper(Core.OUTPUT, S.currentToken());
            System.exit(-1);
        }
        // <expr> ::= <term> | <term> + <expr> | <term> – <expr>
        // <term> ::= <factor> | <factor> * <term>
        // <factor> ::= id | const | ( <expr> )
        if (S.currentToken() == Core.ID || S.currentToken() == Core.CONST || S.currentToken() == Core.LPAREN) {
            expr = new Expr();
            expr.parse(S);
        }
        // So if the currentToken != id or const or (, then invalid syntax
        else {
            Core[] expectedones = new Core[] { Core.ID, Core.CONST, Core.LPAREN };
            Utility.errorhelper(expectedones, S.currentToken());
            System.exit(-1);
        }
        if (!S.expectedToken(Core.SEMICOLON)) {
            Utility.expectedhelper(Core.SEMICOLON, S.currentToken());
            System.exit(-1);
        }
    }

    public void semantic(Stack<Map<String, Core>> scopetrack) {
        expr.semantic(scopetrack);
    }

    public void execute(Memory memory) {
        int exprnum = expr.execute(memory);
        System.out.println(exprnum);
    }
}
