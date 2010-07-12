package li.droth.ceofhack.model;

import java.util.Properties;

/**
 * <p/>
 *
 * @author danielroth
 */
public class ValidatorFactory {

    public static DataTypeValidator createInstance(Properties properties, String type, String name) throws IllegalDefinedValidator {
        if (type.equalsIgnoreCase("Regex")) {
            return new RegexValidator(properties, name);
        } else if (type.equalsIgnoreCase("Generator")) {
            return new GeneratorValidator(properties, name);
        }

        return null;
    }
}
