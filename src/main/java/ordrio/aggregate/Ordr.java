package ordrio.aggregate;

import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;
import ordrio.event.OrdrCreatedEvent;
import ordrio.event.OrdrItemCreatedEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Ordr extends Aggregate {
	private String id;
	private String name;
	private List<OrdrItem> items;
	private BigDecimal totalPrice;

	public Ordr() {
		items = new ArrayList<>();
	}

	public void on(OrdrCreatedEvent event) {
		id = event.getId();
		name = event.getName();
		totalPrice = BigDecimal.ZERO;
	}

	public void on(OrdrItemCreatedEvent event) {
		OrdrItem ordrItem = new OrdrItem();
		ordrItem.setId(event.getId());
		ordrItem.setOrdrId(event.getOrdrId());
		ordrItem.setAmount(event.getAmount());
		ordrItem.setSinglePrice(event.getSinglePrice());
		ordrItem.setName(event.getName());
		ordrItem.setTotalPrice(ordrItem.getSinglePrice()
						.multiply(ordrItem.getAmount()));
		totalPrice = totalPrice.add(ordrItem.getTotalPrice());
		items.add(ordrItem);
	}
}
