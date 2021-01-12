package fr.ensimag.deca.tree;


/**
 * read...() statement.
 *
 * @author gl10
 * @date 01/01/2021
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }



	@Override
	public boolean isIntLiteral() {
		return false;
	}


	@Override
	public boolean isFloatLiteral() {
		return false;
	}


	@Override
	public boolean isBooleanLiteral() {
		return false;
	}


	@Override
	public boolean isIdentifier() {
		return false;
	}

	

}
