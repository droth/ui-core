package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public class BasicDataTypeModel implements DataTypeModel {
    private String name;
    private DataTypeValidator dataTypeValidator;

    public BasicDataTypeModel(String name, DataTypeValidator validator) {
        this.name = name;
        this.dataTypeValidator = validator;
    }

    @Override
    public boolean validate(DataTypeInstance dataTypeInstance) {
        return dataTypeValidator.validate(dataTypeInstance);
    }

    @Override
    public DataTypeInstance getInstance() {
        return new BasicDataTypeInstance(this);
    }

    @Override
    public CeofString extractFromJunk(String junk) {
        return dataTypeValidator.extractValidPart(junk);
    }

    @Override
    public String getName() {
        return name;
    }
}
