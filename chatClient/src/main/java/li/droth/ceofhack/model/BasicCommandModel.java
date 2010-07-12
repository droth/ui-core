package li.droth.ceofhack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 *
 * @author danielroth
 */
public class BasicCommandModel implements CommandModel {
    private String displayName;
    private String socketName;

    private List<DataTypeModel> validDataTypeModels;
    private List<CommandModel> responses;

    public BasicCommandModel(String displayName, String socketName, List<DataTypeModel> vaDataTypeModels) {
        this.displayName = displayName;
        this.socketName = socketName;
        this.validDataTypeModels = vaDataTypeModels;
        responses = new ArrayList<CommandModel>();

    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getSocketName() {
        return socketName;
    }

    @Override
    public boolean validate(CommandInstance instance) {
        List<DataTypeInstance> list = instance.getOrderedContents();
        if (list == null && validDataTypeModels != null) {
            return false;
        } else if (validDataTypeModels == null) {
            return true;
        } else if (list.size() != validDataTypeModels.size()) {
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null)
                return false;
            else if (!validDataTypeModels.get(i).equals(list.get(i).getModel())) {
                return false;
            } else if (!list.get(i).validate()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<DataTypeModel> getDataTypes() {
        return validDataTypeModels;
    }

    @Override
    public CommandInstance getInstance() {
        return new BasicCommandInstance(this);
    }

    @Override
    public List<CommandModel> responses() {
        return responses;
    }

    @Override
    public void addResponse(CommandModel commandModel) {
        responses.add(commandModel);
    }

}
