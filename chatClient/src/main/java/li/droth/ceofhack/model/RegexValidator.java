package li.droth.ceofhack.model;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p/>
 *
 * @author danielroth
 */
public class RegexValidator implements DataTypeValidator {
    private Pattern pattern;
    private Pattern extract;
    private int length = 0;

    public RegexValidator(Properties properties, String name) throws IllegalDefinedValidator {
        Object pattern = properties.get(name + ".pattern");
        if (pattern == null)
            throw new IllegalDefinedValidator("Pattern is missing");
        else {
            this.pattern = Pattern.compile(pattern.toString());
            extract = Pattern.compile("[ ]?(" + pattern.toString() + ").*");
            Object length = properties.get(name + ".length");
            if (length != null)
                this.length = Integer.parseInt(length.toString());
        }
    }

    @Override
    public boolean validate(DataTypeInstance instance) {
        return pattern.matcher(instance.getContent()).matches();
    }

    @Override
    public CeofString extractValidPart(String input) {
        Matcher extractMatcher = extract.matcher(input);
        if (extractMatcher.find()) {
            String extract = extractMatcher.group(1);
            if (length != 0) {
                StringBuilder builder = new StringBuilder(extract);
                for (int i = extract.length(); i < length; i++) {
                    builder.append("\0");
                }
                return new CeofString(builder.toString(), extract.length());
            }
            return new CeofString(extract, extract.length());

        }
        return null;
    }
}
