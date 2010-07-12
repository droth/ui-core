package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface DataTypeValidator {
    public boolean validate(DataTypeInstance instance);

    public CeofString extractValidPart(String input);
}
