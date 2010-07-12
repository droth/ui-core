package li.droth.ceofhack.model;

import java.util.List;
import java.util.Map;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface CommandInstance extends Command {
    public CommandModel getModel();

    public boolean validate();

    public boolean setArguments(DataTypeInstance dataTypeInstance);

    public Map<DataTypeModel, DataTypeInstance> getArguments();

    public List<DataTypeInstance> getOrderedContents();
}
