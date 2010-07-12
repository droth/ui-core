package li.droth.ceofhack.model;

/**
 * <p/>
 *
 * @author danielroth
 */
public class BasicDataTypeInstance implements DataTypeInstance{
    private DataTypeModel delegate;
    private String content;

    public BasicDataTypeInstance(DataTypeModel dataTypeModel) {
        this.delegate = dataTypeModel;
    }
    @Override
    public DataTypeModel getModel() {
        return delegate;
    }

    @Override
    public boolean validate() {
        return delegate.validate(this);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }
}
