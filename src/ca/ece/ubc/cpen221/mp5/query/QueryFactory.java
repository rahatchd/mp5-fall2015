package ca.ece.ubc.cpen221.mp5.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import ca.ece.ubc.cpen221.mp5.query.QueryParser.AndExprContext;
import ca.ece.ubc.cpen221.mp5.query.QueryParser.AtomContext;

public class QueryFactory {
    public static Query parse(String string) {
        // Create a stream of tokens using the lexer.
        CharStream stream = new ANTLRInputStream(string);
        QueryLexer lexer = new QueryLexer(stream);
        lexer.reportErrorsAsExceptions();
        TokenStream tokens = new CommonTokenStream(lexer);
        // Feed the tokens into the parser.
        QueryParser parser = new QueryParser(tokens);
        parser.reportErrorsAsExceptions();
        
        // Generate the parse tree using the starter rule.
        ParseTree tree = parser.query(); // "root" is the starter rule.
        
        ParseTreeWalker walker = new ParseTreeWalker();
        
        QueryListener_QueryCreator listener = new QueryListener_QueryCreator();
        
        walker.walk(listener, tree);
        
        // return the Document value that the listener created
        return listener.getQuery();
    }
    
    private static class QueryListener_QueryCreator extends QueryBaseListener {
        private Stack<Query> stack = new Stack<Query>();
        
        public Query getQuery(){
            return stack.peek();
        }
        
        @Override
        public void exitIn(@NotNull QueryParser.InContext ctx){
            String string = ctx.STRING().getText();
            String trimmedString = string.substring(1, string.length()-1);
            Query q = new InQuery(trimmedString);
            stack.push(q);
        }
        
        @Override
        public void exitPrice(@NotNull QueryParser.PriceContext ctx){
            int low = Integer.parseInt(ctx.range().NUM(0).getText());
            int high = Integer.parseInt(ctx.range().NUM(1).getText());
            Query q = new PriceQuery(low,high);
            stack.push(q);
        }
        
        @Override
        public void exitQuery(@NotNull QueryParser.QueryContext ctx){
            assert stack.size() == 1;
        }
        
        @Override
        public void exitName(@NotNull QueryParser.NameContext ctx){
            String string = ctx.STRING().getText();
            String trimmedString = string.substring(1, string.length()-1);
            Query q = new NameQuery(trimmedString);
            stack.push(q);
        }
        
        @Override
        public void exitRating(@NotNull QueryParser.RatingContext ctx){
            int low = Integer.parseInt(ctx.range().NUM(0).getText());
            int high = Integer.parseInt(ctx.range().NUM(1).getText());
            Query q = new RatingQuery(low,high);
            stack.push(q);
        }
        
        @Override
        public void exitOrExpr(@NotNull QueryParser.OrExprContext ctx){
            List<Query> andExprs = new LinkedList<Query>();
            for (AndExprContext v : ctx.andExpr()){
                andExprs.add(stack.pop());
            }
            Query q = new OrQuery(andExprs);
            stack.push(q);
        }
        
        @Override
        public void exitRange(@NotNull QueryParser.RangeContext ctx){
            
        }
        
        @Override
        public void exitAtom(@NotNull QueryParser.AtomContext ctx){
            
        }
        
        @Override
        public void exitCategory(@NotNull QueryParser.CategoryContext ctx){
            String string = ctx.STRING().getText();
            String trimmedString = string.substring(1, string.length()-1);
            Query q = new CatQuery(trimmedString);
            stack.push(q);
        }
        
        @Override
        public void exitAndExpr(@NotNull QueryParser.AndExprContext ctx){
            List<Query> atoms = new LinkedList<Query>();
            for (AtomContext v : ctx.atom()){
                atoms.add(stack.pop());
            }
            Query q = new AndQuery(atoms);
            stack.push(q);
        }
        
    }
}