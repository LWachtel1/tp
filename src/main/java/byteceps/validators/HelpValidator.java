package byteceps.validators;


import byteceps.errors.Exceptions;
import byteceps.ui.strings.HelpStrings;
import byteceps.commands.Parser;



public class HelpValidator {

    public HelpValidator() {

    }

    public String validateExecute(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        assert parser.getAction() != null : "Command action must not be null";

        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput(HelpStrings.NO_COMMAND_EXCEPTION);
        }

        if (parser.hasAdditionalArguments()) {
            throw new Exceptions.InvalidInput(HelpStrings.ADDITIONAL_ARGUMENTS_EXCEPTION);
        }
        String command = parser.getAction();
        return command;
    }

    public boolean validateShow(Parser parser){
        boolean isEmptyFlag = parser.getActionParameter().isEmpty();
        return isEmptyFlag;
    }

}