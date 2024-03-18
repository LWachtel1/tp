package byteceps.processing;


import byteceps.activities.Exercise;
import byteceps.commands.Parser;
import byteceps.errors.Exceptions;
import byteceps.ui.UserInterface;

import java.util.HashMap;

public class ExerciseManager extends ActivityManager {
    @Override
    public void execute(Parser parser) throws Exceptions.InvalidInput,
            Exceptions.ErrorAddingActivity, Exceptions.ActivityExistsException,
            Exceptions.ActivityDoesNotExists {
        if (parser.getAction().isEmpty()) {
            throw new Exceptions.InvalidInput("No action specified");
        }
        Exercise newExercise;
        Exercise retrievedExercise;

        switch (parser.getAction()) {
        case "add":
            newExercise = processAddExercise(parser);
            add(newExercise);
            UserInterface.printMessage(String.format(
                    "Added Exercise: %s", newExercise.getActivityName()
            ));
            break;
        case "delete":
            retrievedExercise = retrieveExercise(parser);
            delete(retrievedExercise);
            UserInterface.printMessage(String.format(
                    "Deleted Exercise: %s", retrievedExercise.getActivityName()
            ));
            break;
        case "edit":
            String newExerciseName = processEditExercise(parser);
            UserInterface.printMessage(String.format(
                    "Edited Exercise from %s to %s", parser.getActionParameter(), newExerciseName
            ));
            break;
        case "list":
            list();
            break;
        default:
            throw new IllegalStateException("Unexpected value: " + parser.getAction());
        }
    }

    //@@author V4vern
    public Exercise processAddExercise(Parser parser) throws Exceptions.InvalidInput {
        String exerciseName = parser.getActionParameter();
        if (exerciseName.isEmpty()) {
            throw new Exceptions.InvalidInput("Exercise name cannot be empty");
        }
        return new Exercise(exerciseName);
    }

    //@@author V4vern
    public Exercise retrieveExercise(Parser parser) throws Exceptions.ActivityDoesNotExists {
        String exerciseName = parser.getActionParameter();
        return (Exercise) retrieve(exerciseName);
    }

    public String processEditExercise(Parser parser) throws Exceptions.InvalidInput, Exceptions.ActivityDoesNotExists {
        HashMap<String, String> additionalArguments = parser.getAdditionalArguments();
        if (!additionalArguments.containsKey("to")) {
            throw new Exceptions.InvalidInput("Edit command not complete");
        }
        String newExerciseName = additionalArguments.get("to");
        Exercise retrievedExercise = retrieveExercise(parser);
        retrievedExercise.editExerciseName(newExerciseName);

        return newExerciseName;
    }
    @Override
    public String getActivityType(boolean plural) {
        return plural ? "Exercises" : "Exercise";
    }

}
