package com.knooppuntnet.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.knooppuntnet.domain.poi.Poi;
import com.knooppuntnet.domain.poi.TagInformation;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Optional;

public class PoiDeserializer extends JsonDeserializer<Poi> {

	private static final String URL = "http://upload.wikimedia.org/wikipedia/commons/";

	@Override
	public Poi deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);

		Poi poi = new Poi();

		for (JsonNode layerNode: node.get("layers"))
			poi.setLayer(layerNode.asText());

		poi.setElementId(node.get("elementId").asLong());
		poi.setElementType(node.get("elementType").asText());
		poi.setLatitude(node.get("latitude").asDouble());
		poi.setLongitude(node.get("longitude").asDouble());

		for (JsonNode tag : node.get("tags"))
			poi.addTagInformation(new TagInformation(tag.get(0).asText(), tag.get(1).asText()));

		Optional<TagInformation> information = poi.getTags().stream().filter(tag -> tag.getKey().equals("image")).findAny();
		if(information.isPresent() && information.get().getValue().contains("File")) {
			parseFileName(information.get());
		}

		return poi;
	}

	private void parseFileName(TagInformation information) {
		String parseableFileName = information.getValue().replace(" ", "_");

		String fileName = parseableFileName.contains("File%") ?
				parseableFileName.substring(parseableFileName.indexOf("File%3A") + 7) : parseableFileName.substring(parseableFileName.indexOf("File:") + 5);

		information.setValue(md5HexUrl(fileName));
	}

	private String md5HexUrl(String fileName) {
		String messageDigest = DigestUtils.md5Hex(fileName.replace(" ", "_"));
		String hash1 = messageDigest.substring(0, 1);
		String hash2 = messageDigest.substring(0, 2);

		return URL + hash1 + "/" + hash2 + "/" + fileName;
	}
}
