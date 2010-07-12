package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface DataTypeInstance extends DataType {
    public DataTypeModel getModel();

    public boolean validate();

    public String getContent();

    public void setContent(String content);
}
