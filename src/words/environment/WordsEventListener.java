package words.environment;

import words.ast.AST;
import words.ast.ASTValue;
import words.environment.WordsEnvironment;
import words.exceptions.WordsProgramException;
import words.exceptions.WordsRuntimeException;

public class WordsEventListener {
	private AST predicate;
	private AST statementList;
	private boolean temporary;

	public WordsEventListener(AST predicate, AST statementList, boolean temporary) {
		this.predicate = predicate;
		this.statementList = statementList;
		this.temporary = temporary;
	}
	
	public boolean execute(WordsEnvironment environment) throws WordsProgramException {
		ASTValue predicateValue;
		try {
			predicateValue = predicate.eval(environment);
		} catch (WordsRuntimeException e) {
			throw new WordsProgramException(predicate, e);
		}

		// currently restricted to boolean predicate
		assert predicateValue.type == ASTValue.ValueType.BOOLEAN : "Predicate has type " + predicateValue.type.toString();

		if (predicateValue.booleanValue == true) {
			try {
				statementList.eval(environment);
			} catch (WordsRuntimeException e) {
				throw new WordsProgramException(statementList, e);
			}
		} else {
			if (temporary) {
				return false;
			}
		}

		return true;
	}
	
}
