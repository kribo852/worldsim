package worldsim.app;

import java.lang.FunctionalInterface;
import java.awt.Graphics;

@FunctionalInterface
public interface CursorDepiction {

	public void depictConsumer(double cursorPositionx, double cursorPositiony, Graphics g);

}