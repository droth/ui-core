package li.droth.ceofhack.model;

import li.droth.ceofhack.util.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <p/>
 *
 * @author danielroth
 */
public class UI2CeofTranslator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UI2CeofTranslator.class);
    private Map<String, CommandModel> displayNameToModel;
    private Map<String, CommandModel> socketNameToModel;
    private Map<String, DataTypeModel> dataTypeNameToModel;
    private Map<String, DataTypeValidator> dataTyeValidatorNameToValidator;

    private Properties cmdProperties;
    private Properties modelProperties;


    private static UI2CeofTranslator instance;

    private UI2CeofTranslator() {
        //singleton
        super();
        displayNameToModel = new HashMap<String, CommandModel>();
        socketNameToModel = new HashMap<String, CommandModel>();
        dataTypeNameToModel = new HashMap<String, DataTypeModel>();
        dataTyeValidatorNameToValidator = new HashMap<String, DataTypeValidator>();
        init();
    }

    public static UI2CeofTranslator getInstance() {
        if (instance == null)
            instance = new UI2CeofTranslator();
        return instance;
    }

    private void init() {
        //TODO load properties
        cmdProperties = PropertiesReader.readProperties(getClass(), "/cmd.properties");
        modelProperties = PropertiesReader.readProperties(getClass(), "/model.properties");

        Enumeration names = cmdProperties.propertyNames();
        for (; names.hasMoreElements();) {
            Object element = names.nextElement();
            if (element.toString().indexOf(".") == -1 && findCommand(element.toString()) == null) {
                //command found
                LOGGER.debug("Loading cmd {} ", element);
                try {
                    loadCommand(element.toString());
                    LOGGER.debug("Loaded cmd {} ", element);
                } catch (IllegalDefinedCommand illegalDefinedCommand) {
                    LOGGER.warn("Failed to load command {} the error was {}", element, illegalDefinedCommand);
                }

            }

        }
        loadResponses();
    }

    private void loadResponses() {
        for (String cmdName : displayNameToModel.keySet()) {
            Object response = cmdProperties.get(cmdName + ".response");
            if (response != null) {
                CommandModel model = displayNameToModel.get(cmdName);
                StringTokenizer tokenizer = new StringTokenizer(response.toString(), " ", false);
                while (tokenizer.hasMoreTokens()) {
                    String responseName = tokenizer.nextToken();
                    CommandModel responseModel = findCommand(responseName);
                    if (responseModel != null) {
                        model.addResponse(responseModel);
                        LOGGER.debug("Command {} has response {}", model.getDisplayName(), responseModel.getDisplayName());
                    } else {
                        LOGGER.debug("Command {} has invalid response {}, ignoreing it", model.getDisplayName(), responseName);
                    }
                }
            } else {
                LOGGER.debug("Command {} has no responses", cmdName);
            }
        }
    }

    private void loadCommand(String name) throws IllegalDefinedCommand {
        String socketName = cmdProperties.get(name).toString();
        List<DataTypeModel> list = new ArrayList<DataTypeModel>();
        Object syntax = cmdProperties.get(name + ".syntax");
        if (syntax == null) {
            throw new IllegalDefinedCommand("Syntax is missing");
        }
        StringTokenizer tokenizer = new StringTokenizer(syntax.toString(), " ", false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (findDataType(token) == null) {
                LOGGER.debug("Loading DataType {} ", token);
                try {
                    loadDataType(token);
                    LOGGER.debug("Loaded DataType {} ", token);
                } catch (IllegalDefinedDataType e) {
                    LOGGER.warn("Failed to load DataType {} the error was {}", token, e);
                    throw new IllegalDefinedCommand(e);
                }
            }
            list.add(findDataType(token));
        }
        name = name.toLowerCase();
        socketName = socketName.toLowerCase();
        BasicCommandModel commandModel = new BasicCommandModel(name, socketName, list);

        displayNameToModel.put(name, commandModel);
        socketNameToModel.put(socketName, commandModel);

    }

    private void loadDataType(String dataType) throws IllegalDefinedDataType {
        Object name = modelProperties.get(dataType);
        if (name == null) {
            throw new IllegalDefinedDataType("Datatype is not defined");
        }
        Object validatorType = modelProperties.get(dataType + ".validatorName");
        if (validatorType == null) {
            throw new IllegalDefinedDataType("Validator is missing");
        }
        if (findValidator(validatorType.toString()) == null) {
            LOGGER.debug("Loading ValidatorType {} ", validatorType);
            try {
                loadValidator(validatorType.toString());
                LOGGER.debug("Laoded validator {} ", validatorType);
            } catch (IllegalDefinedValidator e) {
                LOGGER.warn("Failed to load validator {} the error was {}", validatorType, e);
                throw new IllegalDefinedDataType(e);
            }
        }

        BasicDataTypeModel model = new BasicDataTypeModel(dataType, findValidator(validatorType.toString()));
        dataTypeNameToModel.put(dataType, model);
    }

    private void loadValidator(String name) throws IllegalDefinedValidator {
        Object type = modelProperties.get(name + ".type");
        if (type == null) {
            throw new IllegalDefinedValidator("Type is missing");
        }
        DataTypeValidator validator = ValidatorFactory.createInstance(modelProperties, type.toString(), name);
        dataTyeValidatorNameToValidator.put(name, validator);
    }


    public DataTypeValidator findValidator(String name) {
        if (dataTyeValidatorNameToValidator.containsKey(name))
            return dataTyeValidatorNameToValidator.get(name);
        return null;
    }


    public CommandModel findCommand(String commandName) {
        commandName = commandName.toLowerCase();
        if (displayNameToModel.containsKey(commandName))
            return displayNameToModel.get(commandName);
        else if (socketNameToModel.containsKey(commandName))
            return socketNameToModel.get(commandName);
        return null;
    }

    public DataTypeModel findDataType(String name) {
        if (dataTypeNameToModel.containsKey(name))
            return dataTypeNameToModel.get(name);
        return null;
    }

    private class IllegalDefinedCommand extends Exception {
        public IllegalDefinedCommand(String s) {
            super(s);
        }

        public IllegalDefinedCommand(IllegalDefinedDataType e) {
            super(e);
        }
    }

    private class IllegalDefinedDataType extends Exception {
        public IllegalDefinedDataType(String s) {
            super(s);
        }

        public IllegalDefinedDataType(IllegalDefinedValidator e) {
            super(e);
        }
    }

    private CeofString extractCommand(String commandString) {
        if (commandString.startsWith("/")) {
            //to external (space is the separator)
            if (commandString.indexOf(" ") != -1)
                return new CeofString(commandString.substring(1, commandString.indexOf(" ")), commandString.indexOf(" "));
            else
                return new CeofString(commandString.substring(1, commandString.length()), commandString.length());
        } else {
            if (commandString.length() >= 4)
                return new CeofString(commandString.substring(0, 4), 4);
            return new CeofString(commandString, commandString.length());
        }
    }

    public CommandInstance buildCommandInstance(String command) {
        CeofString cmd = extractCommand(command);
        command = command.substring(cmd.getLength());
        CommandModel model = findCommand(cmd.getValue());
        if (model == null) {
            LOGGER.error("Invalid command {}", cmd.getValue());
            return null;
        }
        List<DataTypeModel> list = model.getDataTypes();

        CommandInstance instance = model.getInstance();

        for (DataTypeModel dataTypeModel : list) {
            DataTypeInstance dataTypeInstance = dataTypeModel.getInstance();
            CeofString content = dataTypeModel.extractFromJunk(command);
            if (content == null) {
                LOGGER.error("Invalid args in command {} {}", cmd.getValue(), command);
                return null;
            }

            dataTypeInstance.setContent(content.getValue());
            instance.setArguments(dataTypeInstance);
            if (command.length() > 0) {
                if (command.charAt(0) == ' ')
                    command = command.substring(content.getLength() + 1);
                else
                    command = command.substring(content.getLength());
            }
        }

        return instance;
    }

    public CommandInstance buildCommandInstanceFromCeof(String command) {
        CeofString cmd = extractCommand(command);
        command = command.substring(cmd.getLength());
        CommandModel model = findCommand(cmd.getValue());
        if (model == null) {
            LOGGER.error("Invalid command {}", cmd);
            return null;
        }
        List<DataTypeModel> list = model.getDataTypes();

        CommandInstance instance = model.getInstance();

        for (DataTypeModel dataTypeModel : list) {
            DataTypeInstance dataTypeInstance = dataTypeModel.getInstance();
            if (dataTypeInstance.getModel().getName().equalsIgnoreCase("ID")) {
                CeofString content = dataTypeModel.extractFromJunk(command);
                if (content == null) {
                    LOGGER.error("Invalid args in command {} {}", cmd.getValue(), command);
                    return null;
                }

                dataTypeInstance.setContent(content.getValue());
                instance.setArguments(dataTypeInstance);
                command = command.substring(6);
            } else {
                CeofString content = dataTypeModel.extractFromJunk(command);
                if (content == null) {
                    LOGGER.error("Invalid args in command {} {}", cmd.getValue(), command);
                    return null;
                }

                dataTypeInstance.setContent(content.getValue());
                instance.setArguments(dataTypeInstance);
                if (command.length() > 0) {
                    if (command.charAt(0) == ' ')
                        command = command.substring(content.getLength() + 1);
                    else
                        command = command.substring(content.getLength());
                }
            }
        }

        return instance;
    }

}
