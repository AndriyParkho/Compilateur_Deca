// Generated from /home/bastien/ensimag/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ASM=1, CLASS=2, EXTENDS=3, ELSE=4, FALSE=5, IF=6, INSTANCEOF=7, NEW=8, 
		NULL=9, READINT=10, READFLOAT=11, PRINT=12, PRINTLN=13, PRINTLNX=14, PRINTX=15, 
		PROTECTED=16, RETURN=17, THIS=18, TRUE=19, WHILE=20, IDENT=21, LT=22, 
		GT=23, EQUALS=24, PLUS=25, MINUS=26, TIMES=27, SLASH=28, PERCENT=29, DOT=30, 
		COMMA=31, OPARENT=32, CPARENT=33, OBRACE=34, CBRACE=35, EXCLAM=36, SEMI=37, 
		EQEQ=38, NEQ=39, GEQ=40, LEQ=41, AND=42, OR=43, INT=44, FLOAT=45, STRING=46, 
		MULTI_LINE_STRING=47, SEP=48, INCLUDE=49;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"ASM", "CLASS", "EXTENDS", "ELSE", "FALSE", "IF", "INSTANCEOF", "NEW", 
			"NULL", "READINT", "READFLOAT", "PRINT", "PRINTLN", "PRINTLNX", "PRINTX", 
			"PROTECTED", "RETURN", "THIS", "TRUE", "WHILE", "LETTER", "DIGIT", "IDENT", 
			"LT", "GT", "EQUALS", "PLUS", "MINUS", "TIMES", "SLASH", "PERCENT", "DOT", 
			"COMMA", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "EXCLAM", "SEMI", 
			"EQEQ", "NEQ", "GEQ", "LEQ", "AND", "OR", "POSITIVE_DIGIT", "INT", "NUM", 
			"SIGN", "EXP", "DEC", "FLOATDEC", "DIGITHEX", "NUMHEX", "FLOATHEX", "FLOAT", 
			"STRING_CAR", "EOL", "STRING", "MULTI_LINE_STRING", "COMM_CLSQ", "COMM_LIGNE", 
			"COMM", "SEP", "FILENAME", "INCLUDE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'asm'", "'class'", "'extends'", "'else'", "'false'", "'if'", "'instanceof'", 
			"'new'", "'null'", "'readInt'", "'readFloat'", "'print'", "'println'", 
			"'printlnx'", "'printx'", "'protected'", "'return'", "'this'", "'true'", 
			"'while'", null, "'<'", "'>'", "'='", "'+'", "'-'", "'*'", "'/'", "'%'", 
			"'.'", "','", "'('", "')'", "'{'", "'}'", "'!'", "';'", "'=='", "'!='", 
			"'>='", "'<='", "'&&'", "'||'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ASM", "CLASS", "EXTENDS", "ELSE", "FALSE", "IF", "INSTANCEOF", 
			"NEW", "NULL", "READINT", "READFLOAT", "PRINT", "PRINTLN", "PRINTLNX", 
			"PRINTX", "PROTECTED", "RETURN", "THIS", "TRUE", "WHILE", "IDENT", "LT", 
			"GT", "EQUALS", "PLUS", "MINUS", "TIMES", "SLASH", "PERCENT", "DOT", 
			"COMMA", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "EXCLAM", "SEMI", 
			"EQEQ", "NEQ", "GEQ", "LEQ", "AND", "OR", "INT", "FLOAT", "STRING", "MULTI_LINE_STRING", 
			"SEP", "INCLUDE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 63:
			SEP_action((RuleContext)_localctx, actionIndex);
			break;
		case 65:
			INCLUDE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void SEP_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		}
	}
	private void INCLUDE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 doInclude(getText()); 
					  skip(); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\63\u01eb\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\26\3\26\3\27\3\27\3\30\3\30\5\30\u0113\n\30\3\30\3\30\3\30\7\30"+
		"\u0118\n\30\f\30\16\30\u011b\13\30\3\31\3\31\3\32\3\32\3\33\3\33\3\34"+
		"\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3"+
		"%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\3*\3*\3*\3+\3+\3+\3,\3,\3,\3-\3-\3-"+
		"\3.\3.\3.\3/\3/\3\60\3\60\3\60\7\60\u0154\n\60\f\60\16\60\u0157\13\60"+
		"\5\60\u0159\n\60\3\61\6\61\u015c\n\61\r\61\16\61\u015d\3\62\3\62\5\62"+
		"\u0162\n\62\3\63\3\63\3\63\3\63\3\64\3\64\3\64\3\64\3\65\3\65\3\65\3\65"+
		"\5\65\u0170\n\65\3\65\3\65\5\65\u0174\n\65\3\66\3\66\3\67\6\67\u0179\n"+
		"\67\r\67\16\67\u017a\38\38\38\38\58\u0181\n8\38\38\38\38\38\38\38\38\5"+
		"8\u018b\n8\39\39\59\u018f\n9\3:\3:\3;\3;\3<\3<\3<\3<\3<\3<\7<\u019b\n"+
		"<\f<\16<\u019e\13<\3<\3<\3=\3=\3=\3=\3=\3=\3=\7=\u01a9\n=\f=\16=\u01ac"+
		"\13=\3=\3=\3>\3>\3>\3>\7>\u01b4\n>\f>\16>\u01b7\13>\3>\3>\3>\3?\3?\3?"+
		"\3?\7?\u01c0\n?\f?\16?\u01c3\13?\3@\3@\5@\u01c7\n@\3A\3A\3A\3A\5A\u01cd"+
		"\nA\3A\3A\3B\3B\3B\6B\u01d4\nB\rB\16B\u01d5\3C\3C\3C\3C\3C\3C\3C\3C\3"+
		"C\3C\7C\u01e2\nC\fC\16C\u01e5\13C\3C\3C\3C\3C\3C\3\u01b5\2D\3\3\5\4\7"+
		"\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22"+
		"#\23%\24\'\25)\26+\2-\2/\27\61\30\63\31\65\32\67\339\34;\35=\36?\37A "+
		"C!E\"G#I$K%M&O\'Q(S)U*W+Y,[-]\2_.a\2c\2e\2g\2i\2k\2m\2o\2q/s\2u\2w\60"+
		"y\61{\2}\2\177\2\u0081\62\u0083\2\u0085\63\3\2\r\4\2C\\c|\4\2&&aa\4\2"+
		"--//\4\2GGgg\4\2HHhh\5\2\62;CHch\4\2RRrr\4\2$$^^\3\2\f\f\4\2\13\13\"\""+
		"\4\2/\60aa\2\u01f8\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2"+
		"\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I"+
		"\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2"+
		"\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2_\3\2\2\2\2q\3\2\2\2\2w\3\2\2\2"+
		"\2y\3\2\2\2\2\u0081\3\2\2\2\2\u0085\3\2\2\2\3\u0087\3\2\2\2\5\u008b\3"+
		"\2\2\2\7\u0091\3\2\2\2\t\u0099\3\2\2\2\13\u009e\3\2\2\2\r\u00a4\3\2\2"+
		"\2\17\u00a7\3\2\2\2\21\u00b2\3\2\2\2\23\u00b6\3\2\2\2\25\u00bb\3\2\2\2"+
		"\27\u00c3\3\2\2\2\31\u00cd\3\2\2\2\33\u00d3\3\2\2\2\35\u00db\3\2\2\2\37"+
		"\u00e4\3\2\2\2!\u00eb\3\2\2\2#\u00f5\3\2\2\2%\u00fc\3\2\2\2\'\u0101\3"+
		"\2\2\2)\u0106\3\2\2\2+\u010c\3\2\2\2-\u010e\3\2\2\2/\u0112\3\2\2\2\61"+
		"\u011c\3\2\2\2\63\u011e\3\2\2\2\65\u0120\3\2\2\2\67\u0122\3\2\2\29\u0124"+
		"\3\2\2\2;\u0126\3\2\2\2=\u0128\3\2\2\2?\u012a\3\2\2\2A\u012c\3\2\2\2C"+
		"\u012e\3\2\2\2E\u0130\3\2\2\2G\u0132\3\2\2\2I\u0134\3\2\2\2K\u0136\3\2"+
		"\2\2M\u0138\3\2\2\2O\u013a\3\2\2\2Q\u013c\3\2\2\2S\u013f\3\2\2\2U\u0142"+
		"\3\2\2\2W\u0145\3\2\2\2Y\u0148\3\2\2\2[\u014b\3\2\2\2]\u014e\3\2\2\2_"+
		"\u0158\3\2\2\2a\u015b\3\2\2\2c\u0161\3\2\2\2e\u0163\3\2\2\2g\u0167\3\2"+
		"\2\2i\u016f\3\2\2\2k\u0175\3\2\2\2m\u0178\3\2\2\2o\u0180\3\2\2\2q\u018e"+
		"\3\2\2\2s\u0190\3\2\2\2u\u0192\3\2\2\2w\u0194\3\2\2\2y\u01a1\3\2\2\2{"+
		"\u01af\3\2\2\2}\u01bb\3\2\2\2\177\u01c6\3\2\2\2\u0081\u01cc\3\2\2\2\u0083"+
		"\u01d3\3\2\2\2\u0085\u01d7\3\2\2\2\u0087\u0088\7c\2\2\u0088\u0089\7u\2"+
		"\2\u0089\u008a\7o\2\2\u008a\4\3\2\2\2\u008b\u008c\7e\2\2\u008c\u008d\7"+
		"n\2\2\u008d\u008e\7c\2\2\u008e\u008f\7u\2\2\u008f\u0090\7u\2\2\u0090\6"+
		"\3\2\2\2\u0091\u0092\7g\2\2\u0092\u0093\7z\2\2\u0093\u0094\7v\2\2\u0094"+
		"\u0095\7g\2\2\u0095\u0096\7p\2\2\u0096\u0097\7f\2\2\u0097\u0098\7u\2\2"+
		"\u0098\b\3\2\2\2\u0099\u009a\7g\2\2\u009a\u009b\7n\2\2\u009b\u009c\7u"+
		"\2\2\u009c\u009d\7g\2\2\u009d\n\3\2\2\2\u009e\u009f\7h\2\2\u009f\u00a0"+
		"\7c\2\2\u00a0\u00a1\7n\2\2\u00a1\u00a2\7u\2\2\u00a2\u00a3\7g\2\2\u00a3"+
		"\f\3\2\2\2\u00a4\u00a5\7k\2\2\u00a5\u00a6\7h\2\2\u00a6\16\3\2\2\2\u00a7"+
		"\u00a8\7k\2\2\u00a8\u00a9\7p\2\2\u00a9\u00aa\7u\2\2\u00aa\u00ab\7v\2\2"+
		"\u00ab\u00ac\7c\2\2\u00ac\u00ad\7p\2\2\u00ad\u00ae\7e\2\2\u00ae\u00af"+
		"\7g\2\2\u00af\u00b0\7q\2\2\u00b0\u00b1\7h\2\2\u00b1\20\3\2\2\2\u00b2\u00b3"+
		"\7p\2\2\u00b3\u00b4\7g\2\2\u00b4\u00b5\7y\2\2\u00b5\22\3\2\2\2\u00b6\u00b7"+
		"\7p\2\2\u00b7\u00b8\7w\2\2\u00b8\u00b9\7n\2\2\u00b9\u00ba\7n\2\2\u00ba"+
		"\24\3\2\2\2\u00bb\u00bc\7t\2\2\u00bc\u00bd\7g\2\2\u00bd\u00be\7c\2\2\u00be"+
		"\u00bf\7f\2\2\u00bf\u00c0\7K\2\2\u00c0\u00c1\7p\2\2\u00c1\u00c2\7v\2\2"+
		"\u00c2\26\3\2\2\2\u00c3\u00c4\7t\2\2\u00c4\u00c5\7g\2\2\u00c5\u00c6\7"+
		"c\2\2\u00c6\u00c7\7f\2\2\u00c7\u00c8\7H\2\2\u00c8\u00c9\7n\2\2\u00c9\u00ca"+
		"\7q\2\2\u00ca\u00cb\7c\2\2\u00cb\u00cc\7v\2\2\u00cc\30\3\2\2\2\u00cd\u00ce"+
		"\7r\2\2\u00ce\u00cf\7t\2\2\u00cf\u00d0\7k\2\2\u00d0\u00d1\7p\2\2\u00d1"+
		"\u00d2\7v\2\2\u00d2\32\3\2\2\2\u00d3\u00d4\7r\2\2\u00d4\u00d5\7t\2\2\u00d5"+
		"\u00d6\7k\2\2\u00d6\u00d7\7p\2\2\u00d7\u00d8\7v\2\2\u00d8\u00d9\7n\2\2"+
		"\u00d9\u00da\7p\2\2\u00da\34\3\2\2\2\u00db\u00dc\7r\2\2\u00dc\u00dd\7"+
		"t\2\2\u00dd\u00de\7k\2\2\u00de\u00df\7p\2\2\u00df\u00e0\7v\2\2\u00e0\u00e1"+
		"\7n\2\2\u00e1\u00e2\7p\2\2\u00e2\u00e3\7z\2\2\u00e3\36\3\2\2\2\u00e4\u00e5"+
		"\7r\2\2\u00e5\u00e6\7t\2\2\u00e6\u00e7\7k\2\2\u00e7\u00e8\7p\2\2\u00e8"+
		"\u00e9\7v\2\2\u00e9\u00ea\7z\2\2\u00ea \3\2\2\2\u00eb\u00ec\7r\2\2\u00ec"+
		"\u00ed\7t\2\2\u00ed\u00ee\7q\2\2\u00ee\u00ef\7v\2\2\u00ef\u00f0\7g\2\2"+
		"\u00f0\u00f1\7e\2\2\u00f1\u00f2\7v\2\2\u00f2\u00f3\7g\2\2\u00f3\u00f4"+
		"\7f\2\2\u00f4\"\3\2\2\2\u00f5\u00f6\7t\2\2\u00f6\u00f7\7g\2\2\u00f7\u00f8"+
		"\7v\2\2\u00f8\u00f9\7w\2\2\u00f9\u00fa\7t\2\2\u00fa\u00fb\7p\2\2\u00fb"+
		"$\3\2\2\2\u00fc\u00fd\7v\2\2\u00fd\u00fe\7j\2\2\u00fe\u00ff\7k\2\2\u00ff"+
		"\u0100\7u\2\2\u0100&\3\2\2\2\u0101\u0102\7v\2\2\u0102\u0103\7t\2\2\u0103"+
		"\u0104\7w\2\2\u0104\u0105\7g\2\2\u0105(\3\2\2\2\u0106\u0107\7y\2\2\u0107"+
		"\u0108\7j\2\2\u0108\u0109\7k\2\2\u0109\u010a\7n\2\2\u010a\u010b\7g\2\2"+
		"\u010b*\3\2\2\2\u010c\u010d\t\2\2\2\u010d,\3\2\2\2\u010e\u010f\4\62;\2"+
		"\u010f.\3\2\2\2\u0110\u0113\5+\26\2\u0111\u0113\t\3\2\2\u0112\u0110\3"+
		"\2\2\2\u0112\u0111\3\2\2\2\u0113\u0119\3\2\2\2\u0114\u0118\5+\26\2\u0115"+
		"\u0118\5-\27\2\u0116\u0118\t\3\2\2\u0117\u0114\3\2\2\2\u0117\u0115\3\2"+
		"\2\2\u0117\u0116\3\2\2\2\u0118\u011b\3\2\2\2\u0119\u0117\3\2\2\2\u0119"+
		"\u011a\3\2\2\2\u011a\60\3\2\2\2\u011b\u0119\3\2\2\2\u011c\u011d\7>\2\2"+
		"\u011d\62\3\2\2\2\u011e\u011f\7@\2\2\u011f\64\3\2\2\2\u0120\u0121\7?\2"+
		"\2\u0121\66\3\2\2\2\u0122\u0123\7-\2\2\u01238\3\2\2\2\u0124\u0125\7/\2"+
		"\2\u0125:\3\2\2\2\u0126\u0127\7,\2\2\u0127<\3\2\2\2\u0128\u0129\7\61\2"+
		"\2\u0129>\3\2\2\2\u012a\u012b\7\'\2\2\u012b@\3\2\2\2\u012c\u012d\7\60"+
		"\2\2\u012dB\3\2\2\2\u012e\u012f\7.\2\2\u012fD\3\2\2\2\u0130\u0131\7*\2"+
		"\2\u0131F\3\2\2\2\u0132\u0133\7+\2\2\u0133H\3\2\2\2\u0134\u0135\7}\2\2"+
		"\u0135J\3\2\2\2\u0136\u0137\7\177\2\2\u0137L\3\2\2\2\u0138\u0139\7#\2"+
		"\2\u0139N\3\2\2\2\u013a\u013b\7=\2\2\u013bP\3\2\2\2\u013c\u013d\7?\2\2"+
		"\u013d\u013e\7?\2\2\u013eR\3\2\2\2\u013f\u0140\7#\2\2\u0140\u0141\7?\2"+
		"\2\u0141T\3\2\2\2\u0142\u0143\7@\2\2\u0143\u0144\7?\2\2\u0144V\3\2\2\2"+
		"\u0145\u0146\7>\2\2\u0146\u0147\7?\2\2\u0147X\3\2\2\2\u0148\u0149\7(\2"+
		"\2\u0149\u014a\7(\2\2\u014aZ\3\2\2\2\u014b\u014c\7~\2\2\u014c\u014d\7"+
		"~\2\2\u014d\\\3\2\2\2\u014e\u014f\4\63;\2\u014f^\3\2\2\2\u0150\u0159\7"+
		"\62\2\2\u0151\u0155\5]/\2\u0152\u0154\5-\27\2\u0153\u0152\3\2\2\2\u0154"+
		"\u0157\3\2\2\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0159\3\2"+
		"\2\2\u0157\u0155\3\2\2\2\u0158\u0150\3\2\2\2\u0158\u0151\3\2\2\2\u0159"+
		"`\3\2\2\2\u015a\u015c\5-\27\2\u015b\u015a\3\2\2\2\u015c\u015d\3\2\2\2"+
		"\u015d\u015b\3\2\2\2\u015d\u015e\3\2\2\2\u015eb\3\2\2\2\u015f\u0162\t"+
		"\4\2\2\u0160\u0162\3\2\2\2\u0161\u015f\3\2\2\2\u0161\u0160\3\2\2\2\u0162"+
		"d\3\2\2\2\u0163\u0164\t\5\2\2\u0164\u0165\5c\62\2\u0165\u0166\5a\61\2"+
		"\u0166f\3\2\2\2\u0167\u0168\5a\61\2\u0168\u0169\7\60\2\2\u0169\u016a\5"+
		"a\61\2\u016ah\3\2\2\2\u016b\u0170\5g\64\2\u016c\u016d\5g\64\2\u016d\u016e"+
		"\5e\63\2\u016e\u0170\3\2\2\2\u016f\u016b\3\2\2\2\u016f\u016c\3\2\2\2\u0170"+
		"\u0173\3\2\2\2\u0171\u0174\t\6\2\2\u0172\u0174\3\2\2\2\u0173\u0171\3\2"+
		"\2\2\u0173\u0172\3\2\2\2\u0174j\3\2\2\2\u0175\u0176\t\7\2\2\u0176l\3\2"+
		"\2\2\u0177\u0179\5k\66\2\u0178\u0177\3\2\2\2\u0179\u017a\3\2\2\2\u017a"+
		"\u0178\3\2\2\2\u017a\u017b\3\2\2\2\u017bn\3\2\2\2\u017c\u017d\7\62\2\2"+
		"\u017d\u0181\7z\2\2\u017e\u017f\7\62\2\2\u017f\u0181\7Z\2\2\u0180\u017c"+
		"\3\2\2\2\u0180\u017e\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0183\5m\67\2\u0183"+
		"\u0184\7\60\2\2\u0184\u0185\5m\67\2\u0185\u0186\t\b\2\2\u0186\u0187\5"+
		"c\62\2\u0187\u018a\5a\61\2\u0188\u018b\t\6\2\2\u0189\u018b\3\2\2\2\u018a"+
		"\u0188\3\2\2\2\u018a\u0189\3\2\2\2\u018bp\3\2\2\2\u018c\u018f\5i\65\2"+
		"\u018d\u018f\5o8\2\u018e\u018c\3\2\2\2\u018e\u018d\3\2\2\2\u018fr\3\2"+
		"\2\2\u0190\u0191\n\t\2\2\u0191t\3\2\2\2\u0192\u0193\7\f\2\2\u0193v\3\2"+
		"\2\2\u0194\u019c\7$\2\2\u0195\u019b\5s:\2\u0196\u0197\7^\2\2\u0197\u019b"+
		"\7$\2\2\u0198\u0199\7^\2\2\u0199\u019b\7^\2\2\u019a\u0195\3\2\2\2\u019a"+
		"\u0196\3\2\2\2\u019a\u0198\3\2\2\2\u019b\u019e\3\2\2\2\u019c\u019a\3\2"+
		"\2\2\u019c\u019d\3\2\2\2\u019d\u019f\3\2\2\2\u019e\u019c\3\2\2\2\u019f"+
		"\u01a0\7$\2\2\u01a0x\3\2\2\2\u01a1\u01aa\7$\2\2\u01a2\u01a9\5s:\2\u01a3"+
		"\u01a9\5u;\2\u01a4\u01a5\7^\2\2\u01a5\u01a9\7$\2\2\u01a6\u01a7\7^\2\2"+
		"\u01a7\u01a9\7^\2\2\u01a8\u01a2\3\2\2\2\u01a8\u01a3\3\2\2\2\u01a8\u01a4"+
		"\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a9\u01ac\3\2\2\2\u01aa\u01a8\3\2\2\2\u01aa"+
		"\u01ab\3\2\2\2\u01ab\u01ad\3\2\2\2\u01ac\u01aa\3\2\2\2\u01ad\u01ae\7$"+
		"\2\2\u01aez\3\2\2\2\u01af\u01b0\7\61\2\2\u01b0\u01b1\7,\2\2\u01b1\u01b5"+
		"\3\2\2\2\u01b2\u01b4\13\2\2\2\u01b3\u01b2\3\2\2\2\u01b4\u01b7\3\2\2\2"+
		"\u01b5\u01b6\3\2\2\2\u01b5\u01b3\3\2\2\2\u01b6\u01b8\3\2\2\2\u01b7\u01b5"+
		"\3\2\2\2\u01b8\u01b9\7,\2\2\u01b9\u01ba\7\61\2\2\u01ba|\3\2\2\2\u01bb"+
		"\u01bc\7\61\2\2\u01bc\u01bd\7\61\2\2\u01bd\u01c1\3\2\2\2\u01be\u01c0\n"+
		"\n\2\2\u01bf\u01be\3\2\2\2\u01c0\u01c3\3\2\2\2\u01c1\u01bf\3\2\2\2\u01c1"+
		"\u01c2\3\2\2\2\u01c2~\3\2\2\2\u01c3\u01c1\3\2\2\2\u01c4\u01c7\5{>\2\u01c5"+
		"\u01c7\5}?\2\u01c6\u01c4\3\2\2\2\u01c6\u01c5\3\2\2\2\u01c7\u0080\3\2\2"+
		"\2\u01c8\u01cd\t\13\2\2\u01c9\u01cd\5u;\2\u01ca\u01cd\7\17\2\2\u01cb\u01cd"+
		"\5\177@\2\u01cc\u01c8\3\2\2\2\u01cc\u01c9\3\2\2\2\u01cc\u01ca\3\2\2\2"+
		"\u01cc\u01cb\3\2\2\2\u01cd\u01ce\3\2\2\2\u01ce\u01cf\bA\2\2\u01cf\u0082"+
		"\3\2\2\2\u01d0\u01d4\5+\26\2\u01d1\u01d4\5-\27\2\u01d2\u01d4\t\f\2\2\u01d3"+
		"\u01d0\3\2\2\2\u01d3\u01d1\3\2\2\2\u01d3\u01d2\3\2\2\2\u01d4\u01d5\3\2"+
		"\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u0084\3\2\2\2\u01d7"+
		"\u01d8\7%\2\2\u01d8\u01d9\7k\2\2\u01d9\u01da\7p\2\2\u01da\u01db\7e\2\2"+
		"\u01db\u01dc\7n\2\2\u01dc\u01dd\7w\2\2\u01dd\u01de\7f\2\2\u01de\u01df"+
		"\7g\2\2\u01df\u01e3\3\2\2\2\u01e0\u01e2\7\"\2\2\u01e1\u01e0\3\2\2\2\u01e2"+
		"\u01e5\3\2\2\2\u01e3\u01e1\3\2\2\2\u01e3\u01e4\3\2\2\2\u01e4\u01e6\3\2"+
		"\2\2\u01e5\u01e3\3\2\2\2\u01e6\u01e7\7$\2\2\u01e7\u01e8\5\u0083B\2\u01e8"+
		"\u01e9\7$\2\2\u01e9\u01ea\bC\3\2\u01ea\u0086\3\2\2\2\33\2\u0112\u0117"+
		"\u0119\u0155\u0158\u015d\u0161\u016f\u0173\u017a\u0180\u018a\u018e\u019a"+
		"\u019c\u01a8\u01aa\u01b5\u01c1\u01c6\u01cc\u01d3\u01d5\u01e3\4\3A\2\3"+
		"C\3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}