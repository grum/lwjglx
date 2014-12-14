package org.lwjglx.input;

import org.lwjgl.glfw.GLFW;
import org.lwjglx.LWJGLException;
import org.lwjglx.Sys;
import org.lwjglx.opengl.Display;

public class Mouse {
	
	private static boolean grabbed = false;
	
	private static int lastX = 0;
	private static int lastY = 0;
	
	private static int latestX = 0;
	private static int latestY = 0;
	
	private static int x = 0;
	private static int y = 0;
	
	private static int maxEvents = 32;
	
	private static int[] buttonEvents = new int[maxEvents];
	private static boolean[] buttonEventStates = new boolean[maxEvents];
	private static int[] xEvents = new int[maxEvents];
	private static int[] yEvents = new int[maxEvents];
	private static int[] lastxEvents = new int[maxEvents];
	private static int[] lastyEvents = new int[maxEvents];
	private static long[] nanoTimeEvents = new long[maxEvents];
	
	private static int eventCount = 0;
	private static int currentEventPos = -1;
	private static int nextEventPos = 0;
	
	private static boolean clipPostionToDisplay = true;
	
	public static void addMoveEvent(double mouseX, double mouseY) {
		eventCount++;
		if (eventCount > maxEvents) eventCount = maxEvents;
		
		latestX = (int)mouseX;
		latestY = Display.getHeight() - (int)mouseY;
		
		lastxEvents[nextEventPos] = xEvents[nextEventPos];
		lastyEvents[nextEventPos] = yEvents[nextEventPos];
		
		xEvents[nextEventPos] = latestX;
		yEvents[nextEventPos] = latestY;
		
		buttonEvents[nextEventPos] = -1;
		buttonEventStates[nextEventPos] = false;
		
		nanoTimeEvents[nextEventPos] = Sys.getNanoTime();
		
		nextEventPos++;
		if (nextEventPos == maxEvents) nextEventPos = 0;
		
		if (currentEventPos == nextEventPos) currentEventPos++;
		if (currentEventPos == maxEvents) currentEventPos = 0;
	}
	
	public static void addButtonEvent(int button, boolean pressed) {
		eventCount++;
		if (eventCount > maxEvents) eventCount = maxEvents;
		
		lastxEvents[nextEventPos] = xEvents[nextEventPos];
		lastyEvents[nextEventPos] = yEvents[nextEventPos];
		
		xEvents[nextEventPos] = latestX;
		yEvents[nextEventPos] = latestY;
		
		buttonEvents[nextEventPos] = button;
		buttonEventStates[nextEventPos] = pressed;
		
		nanoTimeEvents[nextEventPos] = Sys.getNanoTime();
		
		nextEventPos++;
		if (nextEventPos == maxEvents) nextEventPos = 0;
		
		if (currentEventPos == nextEventPos) currentEventPos++;
		if (currentEventPos == maxEvents) currentEventPos = 0;
	}
	
	public static void poll() {
		lastX = x;
		lastY = y;
		
		if (!grabbed && clipPostionToDisplay) {
			if (latestX < 0) latestX = 0;
			if (latestY < 0) latestY = 0;
			if (latestX > Display.getWidth()-1) latestX = Display.getWidth() - 1;
			if (latestY > Display.getHeight()-1) latestY = Display.getHeight() - 1;
		}
		
		x = latestX;
		y = latestY;
	}
	
	public static void create() throws LWJGLException {
		
	}
	
	public static boolean isCreated() {
		return Display.isCreated();
	}
	
	public static void setGrabbed(boolean grab) {
		GLFW.glfwSetInputMode(Display.getWindow(), 
							  GLFW.GLFW_CURSOR, 
							  grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
		grabbed = grab;
	}
	
	public static boolean isGrabbed() {
		return grabbed;
	}
	
	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(Display.getWindow(), button) == GLFW.GLFW_PRESS;
	}
	
	public static boolean next() {
		if (eventCount == 0) return false;
		
		eventCount--;
		currentEventPos++;
		if (currentEventPos == maxEvents) currentEventPos = 0;
		
		return true;
	}
	
	public static int getEventX() {
		return xEvents[currentEventPos];
	}
	
	public static int getEventY() {
		return yEvents[currentEventPos];
	}
	
	public static int getEventDX() {
		return xEvents[currentEventPos] - lastxEvents[currentEventPos];
	}
	
	public static int getEventDY() {
		return yEvents[currentEventPos] - lastyEvents[currentEventPos];
	}
	
	public static long getEventNanoseconds() {
		return nanoTimeEvents[currentEventPos];
	}
	
	public static int getEventButton() {
		return buttonEvents[currentEventPos];
	}
	
	public static boolean getEventButtonState() {
		return buttonEventStates[currentEventPos];
	}
	
	public static int getEventDWheel() {
		return 0; // TODO
	}
		
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static int getDX() {
		return x - lastX;
	}
	
	public static int getDY() {
		return y - lastY;
	}
	
	public static int getDWheel() {
		// TODO
		return 0;
	}
	
	public static int getButtonCount() {
		return 8; // max mouse buttons supported by GLFW
	}
	
	public static void setClipMouseCoordinatesToWindow(boolean clip) {
		clipPostionToDisplay = clip;
	}
	
	public static void setCursorPosition(int new_x, int new_y) {
		GLFW.glfwSetCursorPos(Display.getWindow(), new_x, new_y);
	}
	
	public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
		// TODO
		System.out.println("TODO: Implement Mouse.setNativeCursor(Cursor)");
		return null;
	}
	
	public static void destroy() {
		
	}

}
