package li.droth.ceofhack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 *
 * @author danielroth
 */
public class BasicCommandInstance implements CommandInstance {
    private CommandModel delegate;
    private Map<DataTypeModel, DataTypeInstance> contents;

    protected BasicCommandInstance(CommandModel command) {
        delegate = command;
        contents = new HashMap<DataTypeModel, DataTypeInstance>();

    }

    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    @Override
    public String getSocketName() {
        return delegate.getSocketName();
    }

    @Override
    public CommandModel getModel() {
        return delegate;
    }

    @Override
    public boolean validate() {
        return delegate.validate(this);
    }

    @Override
    public boolean setArguments(DataTypeInstance dataTypeInstance) {
        if (delegate.getDataTypes().contains(dataTypeInstance.getModel())) {
            if (dataTypeInstance.getModel().validate(dataTypeInstance)) {
                contents.put(dataTypeInstance.getModel(), dataTypeInstance);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<DataTypeModel, DataTypeInstance> getArguments() {
        return contents;
    }

    @Override
    public List<DataTypeInstance> getOrderedContents() {
        List<DataTypeInstance> list = new ArrayList<DataTypeInstance>();
        for (DataTypeModel dataTypeModel : delegate.getDataTypes()) {
            list.add(contents.get(dataTypeModel));
        }
        return list;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("/").append(getDisplayName());
        for (DataTypeInstance dataTypeInstance : getOrderedContents()) {
            builder.append(" ").append(dataTypeInstance.getContent());
        }
        return builder.toString();

    }

}
