package com.knooppuntnet.domain.pdf;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.knooppuntnet.serializer.MapMatchingDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = MapMatchingDeserializer.class)
public class Path {

	private long time;
	private List<Instruction> instructionList = new ArrayList<>();

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<Instruction> getInstructionList() {
		return instructionList;
	}

	public void setInstructionList(List<Instruction> instructionList) {
		this.instructionList = instructionList;
	}

	public void addInstruction(Instruction instruction) {
		this.instructionList.add(instruction);
	}
}
