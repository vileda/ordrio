package ordrio.event;

import io.resx.core.event.SourcedEvent;
import lombok.Getter;
import lombok.Setter;

import static ordrio.Constants.ORDR_CREATED_EVENT_ADDRESS;

@Getter
@Setter
public class OrdrCreatedEvent extends SourcedEvent {
	private String name;

	public OrdrCreatedEvent() {
		super(ORDR_CREATED_EVENT_ADDRESS, null);
	}

	public OrdrCreatedEvent(String id, String name) {
		super(ORDR_CREATED_EVENT_ADDRESS, id);
		this.name = name;
	}
}
