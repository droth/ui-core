package li.droth.ceofhack.model;

import java.util.List;

/**
 * <p/>
 *
 * @author danielroth
 */
public interface CommandModel extends Command {
    public boolean validate(CommandInstance instance);

    public List<DataTypeModel> getDataTypes();

    public CommandInstance getInstance();

    public List<CommandModel> responses();

    public void addResponse(CommandModel commandModel);
}
