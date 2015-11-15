package ordrio.command;

import io.resx.core.command.Command;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static ordrio.Constants.CREATE_ORDR_COMMAND_ADDRESS;
import static ordrio.Constants.CREATE_ORDR_ITEM_COMMAND_ADDRESS;

@Getter
@Setter
public class CreateOrdrItemCommand extends Command {
	private String id;
	private String ordrId;
	private String name;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal singlePrice = BigDecimal.ZERO;
	private BigDecimal totalPrice = BigDecimal.ZERO;

	public CreateOrdrItemCommand() {
		super(CREATE_ORDR_ITEM_COMMAND_ADDRESS);
	}
}
