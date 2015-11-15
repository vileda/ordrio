package ordrio.aggregate;

import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrdrItem extends Aggregate {
	private String id;
	private String ordrId;
	private String name;
	private BigDecimal amount = BigDecimal.ZERO;
	private BigDecimal singlePrice = BigDecimal.ZERO;
	private BigDecimal totalPrice = BigDecimal.ZERO;
}
