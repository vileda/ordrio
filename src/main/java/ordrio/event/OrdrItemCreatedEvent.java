package ordrio.event;

import io.resx.core.event.SourcedEvent;
import lombok.Getter;
import lombok.Setter;

import static ordrio.Constants.ORDR_CREATED_EVENT_ADDRESS;
import static ordrio.Constants.ORDR_ITEM_CREATED_EVENT_ADDRESS;

@Getter
@Setter
public class OrdrItemCreatedEvent extends SourcedEvent {
	private String name;
	private String ordrId;

	public OrdrItemCreatedEvent() {
		super(ORDR_ITEM_CREATED_EVENT_ADDRESS, null);
	}

	public OrdrItemCreatedEvent(String id, String name, String ordrId) {
		super(ORDR_ITEM_CREATED_EVENT_ADDRESS, id);
		this.name = name;
		this.ordrId = ordrId;
	}
}
