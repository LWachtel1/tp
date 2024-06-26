package byteceps.validators;

import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.strings.CommandStrings;
import byteceps.ui.strings.ManagerStrings;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class WeeklyProgramValidator extends Validator {
    //@@author pqienso
    public static String validateCommand(Parser parser) throws Exceptions.InvalidInput {
        assert parser != null : "Parser must not be null";
        String commandAction = parser.getAction();
        assert commandAction != null : "Command action must not be null";
        if (commandAction.isEmpty()) {
            throw new Exceptions.InvalidInput("No action specified");
        }

        switch (commandAction) {
        case CommandStrings.ACTION_ASSIGN:
            validateAssignAction(parser);
            break;
        case CommandStrings.ACTION_CLEAR:
            validateClearAction(parser);
            break;
        case CommandStrings.ACTION_TODAY:
            validateTodayAction(parser);
            break;
        case CommandStrings.ACTION_LOG:
            validateLogAction(parser);
            break;
        case CommandStrings.ACTION_LIST:
            validateListAction(parser);
            break;
        case CommandStrings.ACTION_HISTORY:
            validateHistoryAction(parser);
            break;
        default:
            throw new Exceptions.InvalidInput(String.format(ManagerStrings.UNEXPECTED_ACTION, commandAction));
        }

        return commandAction;
    }
    //@@author joshualeejunyi
    private static void validateAssignAction(Parser parser) throws Exceptions.InvalidInput {
        String workoutName = parser.getActionParameter();
        String day = parser.getAdditionalArguments(CommandStrings.ARG_TO);
        if (hasNoInput(workoutName) || hasNoInput(day)) {
            throw new Exceptions.InvalidInput(ManagerStrings.INCOMPLETE_PROGRAM_ASSIGN);
        }
        validateNumAdditionalArgs(1, 1, parser);
    }

    private static void validateClearAction(Parser parser) throws Exceptions.InvalidInput {
        validateNumAdditionalArgs(0, 0, parser);
    }

    private static void validateLogAction(Parser parser) throws Exceptions.InvalidInput {
        String weight = parser.getAdditionalArguments(CommandStrings.ARG_WEIGHT);
        String reps = parser.getAdditionalArguments(CommandStrings.ARG_REPS);
        String sets = parser.getAdditionalArguments(CommandStrings.ARG_SETS);
        String exerciseName = parser.getActionParameter();
        String date = parser.getAdditionalArguments(CommandStrings.ARG_DATE);

        if (hasNoInput(weight) || hasNoInput(reps) || hasNoInput(sets) || hasNoInput(exerciseName)) {
            throw new Exceptions.InvalidInput(ManagerStrings.LOG_INCOMPLETE);
        }

        if(!hasNoInput(date)) {
            try {
                LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                throw new Exceptions.InvalidInput(ManagerStrings.INVALID_DATE_ENTERED);
            }
        }
        validateWeightsRepsSets(sets, weight, reps);
        validateNumAdditionalArgs(3, 4, parser);
    }

    private static void validateWeightsRepsSets(String sets, String weight, String reps)
            throws Exceptions.InvalidInput {
        int setsInt = 0;
        try {
            setsInt = Integer.parseInt(sets);
            if (setsInt < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new Exceptions.InvalidInput(ManagerStrings.INVALID_REPS_SETS);
        }

        int weightsCount = weight.split(" ").length;
        int repsCount = reps.split(" ").length;

        // Validate sets against weights
        if (weightsCount != setsInt) {
            throw new Exceptions.InvalidInput(String.format(ManagerStrings.INVALID_WEIGHTS_SETS_MISMATCH,
                    weightsCount, setsInt));
        }

        // Validate sets against repetitions
        if (repsCount != setsInt) {
            throw new Exceptions.InvalidInput(String.format(ManagerStrings.INVALID_REPS_SETS_MISMATCH,
                    repsCount, setsInt));
        }
    }

    private static void validateTodayAction(Parser parser) throws Exceptions.InvalidInput {
        validateNumAdditionalArgs(0, 0, parser);
    }

    private static void validateHistoryAction(Parser parser) throws Exceptions.InvalidInput {
        validateNumAdditionalArgs(0, 0, parser);
    }
}
