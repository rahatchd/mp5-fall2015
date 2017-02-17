// Generated from Query.g4 by ANTLR 4.4

package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, WS=2, IN=3, CATEGORY=4, NAME=5, RATING=6, PRICE=7, OR=8, AND=9, 
		NUM=10, LPAREN=11, RPAREN=12, STRING=13;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "IN", "CATEGORY", "NAME", "RATING", "PRICE", "OR", "AND", 
		"NUM", "LPAREN", "RPAREN", "STRING"
	};


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


	public QueryLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Query.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\17r\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\3\6\3\"\n\3\r\3\16\3#\3\3"+
		"\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\7\tG\n\t\f"+
		"\t\16\tJ\13\t\3\t\3\t\3\t\3\t\7\tP\n\t\f\t\16\tS\13\t\3\n\7\nV\n\n\f\n"+
		"\16\nY\13\n\3\n\3\n\3\n\3\n\7\n_\n\n\f\n\16\nb\13\n\3\13\3\13\3\f\3\f"+
		"\3\r\3\r\3\16\3\16\7\16l\n\16\f\16\16\16o\13\16\3\16\3\16\2\2\17\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\3\2\5\5\2\13"+
		"\f\17\17\"\"\3\2\63\67\4\2\"#%\u0080w\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2"+
		"\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2"+
		"\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\35\3"+
		"\2\2\2\5!\3\2\2\2\7\'\3\2\2\2\t*\3\2\2\2\13\63\3\2\2\2\r8\3\2\2\2\17?"+
		"\3\2\2\2\21H\3\2\2\2\23W\3\2\2\2\25c\3\2\2\2\27e\3\2\2\2\31g\3\2\2\2\33"+
		"i\3\2\2\2\35\36\7\60\2\2\36\37\7\60\2\2\37\4\3\2\2\2 \"\t\2\2\2! \3\2"+
		"\2\2\"#\3\2\2\2#!\3\2\2\2#$\3\2\2\2$%\3\2\2\2%&\b\3\2\2&\6\3\2\2\2\'("+
		"\7k\2\2()\7p\2\2)\b\3\2\2\2*+\7e\2\2+,\7c\2\2,-\7v\2\2-.\7g\2\2./\7i\2"+
		"\2/\60\7q\2\2\60\61\7t\2\2\61\62\7{\2\2\62\n\3\2\2\2\63\64\7p\2\2\64\65"+
		"\7c\2\2\65\66\7o\2\2\66\67\7g\2\2\67\f\3\2\2\289\7t\2\29:\7c\2\2:;\7v"+
		"\2\2;<\7k\2\2<=\7p\2\2=>\7i\2\2>\16\3\2\2\2?@\7r\2\2@A\7t\2\2AB\7k\2\2"+
		"BC\7e\2\2CD\7g\2\2D\20\3\2\2\2EG\t\2\2\2FE\3\2\2\2GJ\3\2\2\2HF\3\2\2\2"+
		"HI\3\2\2\2IK\3\2\2\2JH\3\2\2\2KL\7~\2\2LM\7~\2\2MQ\3\2\2\2NP\t\2\2\2O"+
		"N\3\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2R\22\3\2\2\2SQ\3\2\2\2TV\t\2\2"+
		"\2UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2XZ\3\2\2\2YW\3\2\2\2Z[\7(\2"+
		"\2[\\\7(\2\2\\`\3\2\2\2]_\t\2\2\2^]\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2"+
		"\2\2a\24\3\2\2\2b`\3\2\2\2cd\t\3\2\2d\26\3\2\2\2ef\7*\2\2f\30\3\2\2\2"+
		"gh\7+\2\2h\32\3\2\2\2im\7$\2\2jl\t\4\2\2kj\3\2\2\2lo\3\2\2\2mk\3\2\2\2"+
		"mn\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7$\2\2q\34\3\2\2\2\n\2#HQW`km\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}