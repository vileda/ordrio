package ordrio.aggregate;

import io.resx.core.Aggregate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdrItem extends Aggregate {
	private String id;
	private String ordrId;
	private String name;
}
