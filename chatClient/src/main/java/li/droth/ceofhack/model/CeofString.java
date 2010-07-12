package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public class CeofString {
    private String value;
    private int length;

    public CeofString(String value, int length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public int getLength(){
        return length;
    }

}
