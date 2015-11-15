package ordrio.aggregate;

import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;
import ordrio.event.OrdrItemCreatedEvent;

@Getter
@Setter
public class OrdrItem extends Aggregate {
	private String id;
	private String ordrId;
	private String name;

	public void on(OrdrItemCreatedEvent event) {
		id = event.getId();
		ordrId = event.getOrdrId();
		name = event.getName();
	}
}
