package worldsim.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import java.lang.Runnable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@NoArgsConstructor
public class Toolbox {

	List<Tool> tools;
	int index = 0;

	public void initRegister (Tool... tools) {
		this.tools = Arrays.asList(tools);
	}

	
	public void cycleForward() {
		index = (index + 1)%tools.size();
	}

	public String getCurrentName() {
		return tools.get(index).getName();
	} 

	public void runCurrentAction() {
		tools.get(index).getAction().run();
	}

	public CursorDepiction getCurrentCursorDepiction() {
		return tools.get(index).getCursordepiction();
	}

	@Getter
	@AllArgsConstructor
	static class Tool{
		String name;
		Runnable action;
		CursorDepiction cursordepiction;
	}

}
