package ordrio.aggregate;

import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;
import ordrio.event.OrdrCreatedEvent;
import ordrio.event.OrdrItemCreatedEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Ordr extends Aggregate {
	private String id;
	private String name;
	private List<OrdrItem> items;

	public Ordr() {
		items = new ArrayList<>();
	}

	public void on(OrdrCreatedEvent event) {
		id = event.getId();
		name = event.getName();
	}

	public void on(OrdrItemCreatedEvent event) {
		OrdrItem ordrItem = new OrdrItem();
		ordrItem.setId(event.getId());
		ordrItem.setOrdrId(event.getOrdrId());
		ordrItem.setName(event.getName());
		items.add(ordrItem);
	}
}
