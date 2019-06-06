package com.knooppuntnet.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.knooppuntnet.domain.pdf.Instruction;
import com.knooppuntnet.domain.pdf.Path;

import java.io.IOException;
import java.util.Iterator;

public class MapMatchingDeserializer extends JsonDeserializer<Path> {

	@Override
	public Path deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);

		Path path = new Path();
		path.setTime(node.get(0).get("time").asLong());

		createInstruction(node, path);

		return path;
	}

	private void createInstruction(JsonNode node, Path path) {
		for (JsonNode tag : node.get(0).get("instructions")) {
			Instruction instruction = new Instruction();
			instruction.setSign(tag.get("sign").asInt());
			instruction.setText(tag.get("text").asText());
			instruction.setStreetName(tag.get("street_name").asText());
			instruction.setDistance(tag.get("distance").asLong());

			path.addInstruction(instruction);
		}
	}
}
