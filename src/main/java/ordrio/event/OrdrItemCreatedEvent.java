package ordrio.event;

import io.resx.core.event.SourcedEvent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static ordrio.Constants.ORDR_CREATED_EVENT_ADDRESS;
import static ordrio.Constants.ORDR_ITEM_CREATED_EVENT_ADDRESS;

@Getter
@Setter
public class OrdrItemCreatedEvent extends SourcedEvent {
	private String name;
	private String ordrId;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal singlePrice = BigDecimal.ZERO;

	public OrdrItemCreatedEvent() {
		super(ORDR_ITEM_CREATED_EVENT_ADDRESS, null);
	}

	public OrdrItemCreatedEvent(String id, String name, String ordrId, BigDecimal amount, BigDecimal singlePrice) {
		super(ORDR_ITEM_CREATED_EVENT_ADDRESS, id);
		this.name = name;
		this.ordrId = ordrId;
		this.amount = amount;
		this.singlePrice = singlePrice;
	}
}
