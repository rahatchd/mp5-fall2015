grammar Query;

// This puts "package formula;" at the top of the output Java files.
@header {
package ca.ece.ubc.cpen221.mp5.query;
}

// This adds code to the generated lexer and parser. Do not change these lines.
@members {
    // This method makes the lexer or parser stop running if it encounters
    // invalid input and throw a RuntimeException.
    public void reportErrorsAsExceptions() {
        //removeErrorListeners();
        
        addErrorListener(new ExceptionThrowingErrorListener());
    }
    
    private static class ExceptionThrowingErrorListener extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new RuntimeException(msg);
        }
    }
}

/*
 * These are the lexical rules. They define the tokens used by the lexer.
 *   *** Antlr requires tokens to be CAPITALIZED, like START_ITALIC, END_ITALIC, and TEXT.
 */
WS : [ \r\t\n]+ -> skip ;
IN : 'in';
CATEGORY : 'category';
NAME : 'name';
RATING : 'rating';
PRICE : 'price';
OR : [ \r\t\n]* '||' [ \r\t\n]* ;
AND :[ \r\t\n]* '&&' [ \r\t\n]*;
NUM : [1-5];
LPAREN :'(';
RPAREN : ')';
STRING : '"' ([\u0020-\u0021]|[\u0023-\u007E])* '"';

/*
 * These are the parser rules. They define the structures used by the parser.
 *    *** Antlr requires grammar nonterminals to be lowercase, like html, normal, and italic.
 */

orExpr : andExpr (OR andExpr)*;
andExpr : atom (AND atom)*;
atom : in | category | rating | price | name | LPAREN orExpr RPAREN;
query: andExpr EOF;

in : IN LPAREN STRING RPAREN;
category : CATEGORY LPAREN STRING RPAREN;
name : NAME LPAREN STRING RPAREN;
rating : RATING LPAREN range RPAREN;
price : PRICE LPAREN range RPAREN;
range : NUM '..' NUM;



