package words.ast;

import java.util.HashSet;

import words.environment.WordsAction;
import words.environment.WordsEnvironment;
import words.environment.WordsObject;
import words.environment.WordsSay;
import words.exceptions.WordsRuntimeException;

public class INodeSaysPredicate extends INode {
	public INodeSaysPredicate(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(WordsEnvironment environment) throws WordsRuntimeException {
		ASTValue subject = children.get(0).eval(environment);
		ASTValue objectAlias = children.get(1).eval(environment);
		ASTValue sayStatement = children.get(2).eval(environment);
		
		HashSet<WordsObject> objectsToCheck = new HashSet<WordsObject>();
		
		// If subject is a string, then we are looking at a class name
		if (subject.type.equals(ASTValue.ValueType.STRING)) {
			objectsToCheck = environment.getObjectsByClass(subject.stringValue);
		} else if (subject.type.equals(ASTValue.ValueType.OBJ)) {
			objectsToCheck.add(subject.objValue);
		}
		
		for (WordsObject object : objectsToCheck) {
			WordsAction lastAction = object.getLastAction();
			if (lastAction instanceof WordsSay && object.getCurrentMessage().equals(sayStatement.stringValue)) {
				return new ASTValue(true);
				//TODO: Aliasing, statement list evaluation
			}
		}
		return new ASTValue(false);
	}
}