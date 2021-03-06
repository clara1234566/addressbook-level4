//@@author rajdeepsh

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;

/**
 * Creates a group in the AddressBook.
 */
public class CreateGroupCommand extends Command {

    public static final String COMMAND_WORD = "creategroup";
    public static final String COMMAND_WORD_2 = "cg";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a new group in the address book. "
            + "Parameters: "
            + PREFIX_NAME + "GROUP_NAME "
            + PREFIX_GROUP_LOCATION + "GROUP_LOCATION "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "TUT[01] "
            + PREFIX_GROUP_LOCATION + "E1-06-01 "
            + PREFIX_TAG + "MA1501";

    public static final String MESSAGE_SUCCESS = "New group added: %1$s";
    public static final String MESSAGE_DUPLICATE_GROUP = "This group already exists in the student management system";
    public static final String LOG_DUPLICATE_GROUP = "Duplicate group has been detected";
    public static final String LOG_COMMIT = "Version Committed";
    public static final String LOG_COMMAND_SUCCESS = "Group has been created";

    private static final Logger logger = LogsCenter.getLogger(CreateGroupCommand.class);

    private final Group toCreate;
    private boolean shouldCommit;

    /**
     * Receives group needed for creating.
     *
     * @param group Group to create.
     */
    public CreateGroupCommand(Group group) {
        requireNonNull(group);
        this.toCreate = group;
        this.shouldCommit = true;
    }

    /**
     * Creates a group.
     *
     * @param model {@code Model} which the command should operate on.
     * @param history {@code CommandHistory} which the command should operate on.
     * @return Successful command result.
     * @throws CommandException If duplicate group found.
     */
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.hasGroup(toCreate)) {
            logger.log(Level.WARNING, LOG_DUPLICATE_GROUP);
            throw new CommandException(MESSAGE_DUPLICATE_GROUP);
        }
        model.createGroup(toCreate);
        logger.log(Level.INFO, LOG_COMMAND_SUCCESS);
        if (shouldCommit) {
            model.commitAddressBook();
            logger.log(Level.INFO, LOG_COMMIT);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, toCreate));
    }

    /**
     * Returns true if the objects are the same.
     *
     * @param other Object to compare with.
     * @return Result of comparison.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CreateGroupCommand // instanceof handles nulls
                && toCreate.equals(((CreateGroupCommand) other).toCreate));
    }

    /**
     * Sets flag for committing.
     *
     * @param shouldCommit Committing flag.
     */
    public void setShouldCommit(boolean shouldCommit) {
        this.shouldCommit = shouldCommit;
    }
}
