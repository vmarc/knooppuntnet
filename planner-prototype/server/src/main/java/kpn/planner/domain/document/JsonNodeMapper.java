package kpn.planner.domain.document;

import java.util.List;

public class JsonNodeMapper {

	private List<String> key;
	private List<Long> values;

	public JsonNodeMapper() {
	}

	public List<String> getKey() {
		return key;
	}

	public void setKey(List<String> key) {
		this.key = key;
	}

	public List<Long> getValues() {
		return values;
	}

	public void setValues(List<Long> values) {
		this.values = values;
	}
}
