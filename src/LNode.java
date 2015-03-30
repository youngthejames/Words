/**
 * An abstract syntax tree leaf node.
 */
public class LNode extends AST {
	public boolean hasNoVal;
	public Direction direction;
	public double num;
	public String string;
	
	public LNode(ASTType type, Direction.Type d) {
		super(type);
		this.direction = new Direction(d);
	}
	
	public LNode(ASTType type) {
		super(type);
		this.hasNoVal = true;
	}
	
	public LNode(ASTType type, double n) {
		super(type);
		this.num = n;
	}
	
	public LNode(ASTType type, String s) {
		super(type);
		
		if (type == ASTType.STRING) {
			// Remove leading and trailing " characters
			// Do not do a full string replace, since some " characters may be escaped
			if (s.startsWith("\""))
					s = s.substring(1, s.length());
			
			if (s.endsWith("\""))
				s = s.substring(0, s.length() - 1);
			
			// Replace supported escape sequences
			s = s.replace("\\\n", "\n");
			s = s.replace("\\\\", "\\");
			s = s.replace("\\\"", "\"");
			
			this.string = s;
		} else if (type == ASTType.IDENTIFIER) {
			this.string = s;
		} else if (type == ASTType.REFERENCE) {
			this.string = s.replace("'s", "");
		}
	}
	
	private String valueAsString() {
		if (type == ASTType.DIRECTION)
			return direction.toString();
		else if (type == ASTType.NOTHING || type == ASTType.NOW)
			return "true";
		else if (type == ASTType.NUM)
			return Double.toString(num);
		else if (type == ASTType.STRING || type == ASTType.IDENTIFIER || type == ASTType.REFERENCE)
			return string;
		
		return "";
	}
	
	public void dump(int level) {
		for (int i = 0; i < level; i++)
			System.err.printf("  ");
		
		System.err.println(this.type.toString() + ": " + valueAsString());
	}
	
	@Override
	public String toString() {
		return "[" + type.toString() + ": " + valueAsString() + "]";
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public Value eval(WordsEnvironment environment) {
		switch (this.type) {
			case STRING:
			case REFERENCE:
			case IDENTIFIER:
				return new Value(this.string);
			case NUM:
				return new Value(this.num);
			case DIRECTION:
				return new Value(this.direction);
			case NOTHING:
				return new Value(ValueType.NOTHING);
			case NOW:
				return new Value(ValueType.NOW);
		}
		return null;
	}
}