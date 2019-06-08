package kpn.planner.domain.pdf;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import kpn.planner.serializer.MapMatchingDeserializer;

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
