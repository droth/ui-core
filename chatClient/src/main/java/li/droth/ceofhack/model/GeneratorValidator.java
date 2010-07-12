package li.droth.ceofhack.model;

import java.util.*;
import java.util.regex.Pattern;

/**
 * <p/>
 *
 * @author danielroth
 */
public class GeneratorValidator implements DataTypeValidator{
    private Pattern pattern;
    private int length;
    private Random rnd;

    private List<Character> characters = new ArrayList<Character>();

    GeneratorValidator(Properties properties, String name) throws IllegalDefinedValidator {
        Object pattern = properties.get(name + ".pattern");
        if (pattern == null)
            throw new IllegalDefinedValidator("Pattern is missing");
        else {
            this.pattern = Pattern.compile("[ ]?(" + pattern.toString() + ").*");
            Object length = properties.get(name + ".length");
            this.length = Integer.parseInt(length.toString());
            Object ranges = properties.get(name + ".ranges");
            StringTokenizer tokenizer = new StringTokenizer(ranges.toString(), " ");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.length() == 1)
                    characters.add(token.charAt(0));
                else if (token.length() == 3) {
                    for (int i = token.charAt(0); i <= token.charAt(2); i++)
                        characters.add((char) i);
                }

            }

        }
        rnd = new Random(System.currentTimeMillis());
    }
    @Override
    public boolean validate(DataTypeInstance instance) {
        return pattern.matcher(instance.getContent()).matches();
    }

    @Override
    public CeofString extractValidPart(String input) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < length; i++) {
            builder.append(characters.get(rnd.nextInt(characters.size())));
        }
        return new CeofString(builder.toString(), 0);
    }
}
