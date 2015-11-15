package ordrio.command;

import io.resx.core.command.Command;
import lombok.Getter;
import lombok.Setter;

import static ordrio.Constants.CREATE_ORDR_COMMAND_ADDRESS;

@Getter
@Setter
public class CreateOrdrCommand extends Command {
	private String id;
	private String name;

	public CreateOrdrCommand() {
		super(CREATE_ORDR_COMMAND_ADDRESS);
	}
}
