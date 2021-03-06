package seedu.recruit.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.recruit.logic.parser.CliSyntax.PREFIX_COMPANY_NAME;
import static seedu.recruit.logic.parser.CliSyntax.PREFIX_EMAIL;

import seedu.recruit.commons.core.EventsCenter;
import seedu.recruit.commons.events.logic.ChangeLogicStateEvent;
import seedu.recruit.commons.events.ui.ShowCompanyBookRequestEvent;
import seedu.recruit.logic.CommandHistory;

import seedu.recruit.logic.parser.Prefix;
import seedu.recruit.model.Model;
import seedu.recruit.model.UserPrefs;

/**
 * Sorts all the companies in the CompanyBook
 */
public class SortCompanyCommand extends Command {

    public static final String COMMAND_WORD = "sortC";

    public static final String MESSAGE_SUCCESS = "Sorted all companies.\n";

    public static final String MESSAGE_TAG_USAGE = "Please sort by using one of the available tags: "
            + "Company Name " + PREFIX_COMPANY_NAME
            + ", Email " + PREFIX_EMAIL
            + " or sort the current order in reverse with r/ \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_COMPANY_NAME;

    private static Prefix prefixToSort;

    public SortCompanyCommand(Prefix prefix) {
        this.prefixToSort = prefix;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history, UserPrefs userPrefs) {
        requireNonNull(model);
        model.sortCompanies(prefixToSort);
        model.commitRecruitBook();

        if (ShortlistCandidateInitializationCommand.isShortlisting()) {
            EventsCenter.getInstance()
                    .post(new ChangeLogicStateEvent(SelectCompanyCommand.COMMAND_LOGIC_STATE_FOR_SHORTLIST));

            return new CommandResult(MESSAGE_SUCCESS
                    + ShortlistCandidateInitializationCommand.MESSAGE_NEXT_STEP
                    + SelectCompanyCommand.MESSAGE_USAGE);
        }

        if (DeleteShortlistedCandidateInitializationCommand.isDeleting()) {
            EventsCenter.getInstance()
                    .post(new ChangeLogicStateEvent(SelectCompanyCommand.COMMAND_LOGIC_STATE_FOR_SHORTLIST_DELETE));

            return new CommandResult(MESSAGE_SUCCESS
                    + DeleteShortlistedCandidateInitializationCommand.MESSAGE_NEXT_STEP
                    + SelectCompanyCommand.MESSAGE_USAGE);
        }

        EventsCenter.getInstance().post(new ShowCompanyBookRequestEvent());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCompanyCommand)) {
            return false;
        }

        // state check
        SortCompanyCommand s = (SortCompanyCommand) other;
        return prefixToSort.equals(s.prefixToSort);
    }

}
