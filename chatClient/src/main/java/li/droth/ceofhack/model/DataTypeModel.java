package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface DataTypeModel extends DataType {
    public boolean validate(DataTypeInstance dataTypeInstance);

    public DataTypeInstance getInstance();

    public CeofString extractFromJunk(String junk);

}
