package words.ast;

import words.environment.*;
import words.exceptions.*;

public class INodeQueueCustomAction extends INode {
	public INodeQueueCustomAction(Object... children) {
		super(children);
	}

	@Override
	public ASTValue eval(Environment environment) throws WordsRuntimeException {
		AST referenceObject = children.get(0);
		AST identifier = children.get(1);
		ASTValue actionName = children.get(2).eval(environment);
		//TODO: Handle parameter list (at all)
		AST paramList = children.get(3);
		ASTValue doNow = children.get(4) != null ? children.get(4).eval(environment) : null;
		
		Property property = lookupProperty(environment, referenceObject, identifier);
		if (property.type != Property.PropertyType.OBJECT) {
			throw new InvalidTypeException(ASTValue.Type.OBJ.toString(), property.type.toString());
		}
		WordsObject object = property.objProperty;
				
		CustomAction action = object.getWordsClass().getCustomAction(actionName.stringValue);
		
		if (doNow == null) {
			object.enqueueAction(action);
		} else {
			object.enqueueActionAtFront(action);
		}
		
		return null;
	}
}