package ordrio.event;

import io.resx.core.event.DistributedEvent;
import lombok.Getter;
import lombok.Setter;
import ordrio.aggregate.Ordr;

import static ordrio.Constants.UPDATE_ORDR_EVENT_ADDRESS;

@Getter
@Setter
public class UpdateOrdrEvent extends DistributedEvent {
	public UpdateOrdrEvent() {
		super(UPDATE_ORDR_EVENT_ADDRESS);
	}
}
