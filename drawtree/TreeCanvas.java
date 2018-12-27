//  Copyright 2010 Beth Randall

/*********************************
This file is part of CorpusSearch.

CorpusSearch is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CorpusSearch is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with CorpusSearch.  If not, see <http://www.gnu.org/licenses/>.
************************************/

package drawtree;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import javax.swing.*;
import search_result.*;

public class TreeCanvas extends Canvas implements MouseListener,KeyListener{

    protected Graphics graphite;
    protected Font fonto;
    protected FontMetrics font_met;
    protected GraphicNode grunt1;
    protected Node nodal;
    protected String font_name, labell, message;
    protected TreeTextField my_text_field;
    protected boolean ctrl_pressed, esc_pressed, shift_pressed, tab_pressed;
    protected boolean enter_pressed, getting_text, getting_number, did_it;
    protected boolean new_trace_POS, new_trace_text, will_coindex;
    protected boolean found_error, will_delete, ctrl_x;
    protected int num, must_undo, num_clicks;

    public TreeCanvas() {
	Init(); }

    public void addTree() {
	MyEvents.setUrText(CorpusDraw.currTree());
	MyEvents.anntf.put(this.makeFileString());
	MyEvents.numtf.put(this.makeSentNum());
	initBooleans();
	graphite = this.getGraphics();
	this.setSizeCurrTree();
	CorpusDraw.toole.treeve.initScrollBars();
	paint(graphite); }

    private void initBooleans() {
	esc_pressed = false; ctrl_pressed = false; shift_pressed = false;
        tab_pressed = false; enter_pressed = false; 
	getting_text = false; getting_number = false; ctrl_x = false;
        will_delete = false; found_error = false; must_undo = 0;
        num_clicks = 0; }


    // this is needed for the shameless hack in DrawLoop.
    public void correctUrText() {
	//MyEvents.setUrText(CorpusDraw.currTree()); }
    }

    public void setSizeCurrTree() {
	this.setSize(CorpusDraw.currTree().getOptWidth(),
		     CorpusDraw.currTree().getOptHeight());
	// printSizeToSystemErr();
    }

    private void printSizeToSystemErr() {
	System.err.print("opt width, height, my width, height:  ");
	System.err.print(CorpusDraw.currTree().getOptWidth() + ", ");
	System.err.print(CorpusDraw.currTree().getOptHeight() + ", ");
	System.err.println(this.getWidth() + ", " + this.getHeight()); }

    private String makeFileString() {
	String file_str;

	try {
	    file_str = CorpusDraw.currTree().getShortFileName(); }
	catch (Exception e) {
	    file_str = "UNKNOWN"; }
	return file_str; }

    private int makeSentNum() {
	int num = 0;
	try {
	    num = CorpusDraw.currTree().getChangeTree().getNumID(); }
	catch (Exception e) {
	    num = -1; }
	return num; }

    public void resetTree() {
	initBooleans();
	CorpusDraw.currTree().setBulletNode();
	//CorpusDraw.toole.treeve.moveScrollBars(CorpusDraw.currTree()); 
	//CorpusDraw.toole.treeve.moveScrollBars();
	paint(graphite); }

    private void Init() {
	
	graphite = this.getGraphics();
	//graphite.setFont(new Font("Dialog", 12, 0));
	//fonto = graphite.getFont();
	this.setBackground(ToolColors.BACKGROUND);
	this.addMouseListener(this);
	this.addKeyListener(this);
	this.requestFocusInWindow();
	// my_text_field = new TreeTextField();
	//this.setSize(CorpusDraw.MIN_WDTH, CorpusDraw.MIN_HT);
	initBooleans(); }

    public void paint (Graphics graphite) {
	graphite = this.getGraphics();
	//fonto = graphite.getFont(); 
	clearMyRect(graphite);
	DrawGraphicTree.drawTree(CorpusDraw.currTree(), graphite); 
        if (getting_number || getting_text) { my_text_field.refresh(); } }

    public void clearMyRect(Graphics graphite) {
	graphite.clearRect(0, 0, CorpusDraw.MIN_WDTH, CorpusDraw.MIN_HT); }

    public void update (Graphics graphite) {
	super.update(graphite); }

    public void setCanvasBackground(Color hue) {
	this.setBackground(hue); }

    public void setEndOfFile() {
	clearMyRect(graphite);
	graphite.setColor(ToolColors.WARNING);
	graphite.drawString("END OF FILE", 100, 100); }


    public void draw_show_only() {
	if (CorpusDraw.show_list.isEmpty()) {
	    this.set_show_only(); return; }
	CorpusDraw.show_only = true;
	this.resetTree(); }

    public void draw_show_all() {
	CorpusDraw.show_only = false;
	this.resetTree(); }

    public void set_show_only() {
	CorpusDraw.currTree().showOnlyList();
	CorpusDraw.show_list = new ArgList(CorpusDraw.show_str);
	CorpusDraw.show_only = true;
	this.resetTree(); }

    public void mouseClicked(MouseEvent e) {
	int x, y;
	GraphicNode grunt; 

	x = e.getX();
	y = e.getY();
	//System.err.println("x, y: " + x + ", " + y);
	num_clicks = e.getClickCount();
	if (num_clicks == 2) {
	    grunt = CorpusDraw.currTree().getNodeforCoords(x, y);
	    if (!grunt.IsNullGNode()) {
		CorpusDraw.currTree().addToSelected(grunt);
		replaceLabelCanvas(); return; }}
    }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
	int x, y;
	GraphicNode grunt;

	this.requestFocusInWindow();
	//MyEvents.clearTextFields();
	x = e.getX();
	y = e.getY();
	//System.err.println("x, y:  " + x + ", " + y);
	grunt = CorpusDraw.currTree().getNodeforCoords(x, y);  
	if (grunt.IsNullGNode()) {
	    if (e.getButton() == e.BUTTON1 && 
		!getting_text && !getting_number) {
		CorpusDraw.currTree().clearSelected();
		resetTree(); return; }
	    if (e.getButton() == e.BUTTON3 && 
		!getting_text && !getting_number) {
		if (shift_pressed) {
		    CorpusDraw.currTree().redo();
		    resetTree(); return; }
		else {
		    CorpusDraw.currTree().undo();
		    resetTree(); return; }}}
	if (num_clicks != 2) {
	    CorpusDraw.currTree().toggleSelected(grunt); }
	DrawGraphicTree.whichSelect(CorpusDraw.currTree(), grunt);
	// BUTTON1 is left-click, is default mouse click (trackpad).
	if (e.getButton() == e.BUTTON1 && 
	    !getting_text && !getting_number) {
	    if (ctrl_pressed && shift_pressed) {
		CorpusDraw.currTree().delete_node();
		resetTree(); return; }
	    if (shift_pressed && !ctrl_pressed) {
		replaceLabelCanvas(); resetTree(); return; }
	    if (ctrl_pressed && !shift_pressed) {
		addNodeCanvas(); return;}}
	
	    if (e.getButton() == e.BUTTON3 && 
	    !getting_text && !getting_number) {
	    if (!ctrl_pressed && !shift_pressed) { 
		CorpusDraw.currTree().moveTo();
		resetTree(); return;}}
	    if (ctrl_pressed && shift_pressed) {
		traceAfterCanvas(); return; }
	    if (ctrl_pressed && !shift_pressed) {
		traceBeforeCanvas(); return; }
    }

    public void keyPressed(KeyEvent e) {
	GraphicNode grunt;
	Node nodal;

	this.requestFocusInWindow();
	//displayInfo(e, "KEY_PRESSED:  ");
	if (e.getKeyText(e.getKeyCode()).equals("Escape")) {
	    esc_pressed = true; return; }
	// control x.
	if (e.getKeyCode() == 88 && e.getModifiersEx() == 128) {
	    ctrl_x = true; return; }
	// control s.
	if (e.getKeyCode() == 83 && e.getModifiersEx() == 128) {
	    if (ctrl_x) {
		ctrl_x = false;
		DrawLoop.saveFile(); 
		return; } } 
	if (e.getKeyText(e.getKeyCode()).equals("Ctrl")) {
	    ctrl_pressed = true; 
	    return; }
	// Tab isn't actually found by TreeCanvas.
	if (e.getKeyText(e.getKeyCode()).equals("Tab")) {
	    tab_pressed = true; return; }
	if (e.getKeyText(e.getKeyCode()).equals("Shift")) {
	    shift_pressed = true; return; }
	if (e.getKeyText(e.getKeyCode()).equals("Backspace")) {
	    will_delete = true; return; }
	if (e.getKeyText(e.getKeyCode()).equals("Enter")) {
	    if (getting_number) { 
		num = my_text_field.getNumber();
		if (num <= 0) {
		    message = "not a legit number: "+my_text_field.getString();
		    MyEvents.warn(message); }
		else {
		CorpusDraw.currTree().goTo2(my_text_field.getString());
		DrawLoop.goToTree(DrawLoop.getMyTBToRix(),
				  my_text_field.getNumber()); } }
	    if (new_trace_POS) { newTracePOS(); return;}
	    if (new_trace_text) { newTraceText(); return;}
	    if (getting_text) {
		labell = my_text_field.getString();
		did_it = CorpusDraw.currTree().
		    cleanLabel(labell, 
		    my_text_field.getNode());
		if (!did_it) { 
		    found_error = true;
		    this.requestFocusInWindow(); 
		    MyEvents.intf.clear(); return; }
		labell = CorpusDraw.currTree().getMyNewLabel();
		CorpusDraw.currTree().replaceLabel2(labell,
						    my_text_field.getNode(),
						    must_undo); 
		resetTree(); return; }
	    enter_pressed = true; return; }
	// move bullet down.
	//displayInfo(e, "");
	if (e.isActionKey() && e.getKeyText(e.getKeyCode()).equals("Down")) {
	    //System.err.println("will move bullet down:  ");
	    DrawGraphicTree.clearBullet(CorpusDraw.currTree(), 
 			CorpusDraw.currTree().getGraphicBulletNode());
	    CorpusDraw.currTree().moveBulletDown();
	    DrawGraphicTree.drawBullet(CorpusDraw.currTree(),
		       CorpusDraw.currTree().getGraphicBulletNode());
	    return; }
	if (e.isActionKey() && e.getKeyText(e.getKeyCode()).equals("Up")) {
	    DrawGraphicTree.clearBullet(CorpusDraw.currTree(), 
			CorpusDraw.currTree().getGraphicBulletNode());
	    CorpusDraw.currTree().moveBulletUp(); 
	    DrawGraphicTree.drawBullet(CorpusDraw.currTree(),
		       CorpusDraw.currTree().getGraphicBulletNode());
            return; }
	if (e.isActionKey() && e.getKeyText(e.getKeyCode()).equals("Right")) {
	    if (getting_text || getting_number) { 
		my_text_field.arrowRight();
		return; }
	    DrawGraphicTree.clearBullet(CorpusDraw.currTree(), 
			CorpusDraw.currTree().getGraphicBulletNode());
	    CorpusDraw.currTree().moveBulletRight(); 
	    DrawGraphicTree.drawBullet(CorpusDraw.currTree(),
		       CorpusDraw.currTree().getGraphicBulletNode());
            return; }
	if (e.isActionKey() && e.getKeyText(e.getKeyCode()).equals("Left")) {
	    if (getting_text || getting_number) {
		my_text_field.arrowLeft();
		return; }
	    DrawGraphicTree.clearBullet(CorpusDraw.currTree(), 
			CorpusDraw.currTree().getGraphicBulletNode());
	    CorpusDraw.currTree().moveBulletLeft(); 
	    DrawGraphicTree.drawBullet(CorpusDraw.currTree(),
		       CorpusDraw.currTree().getGraphicBulletNode());
            return; }
    }

    public void keyReleased(KeyEvent e) {
	if (!new_trace_POS && !new_trace_text && !getting_text) {
	    if (e.getKeyText(e.getKeyCode()).equals("Ctrl")) {
		ctrl_pressed = false; }}
	if (e.getKeyText(e.getKeyCode()).equals("Escape")) {
	    esc_pressed = false; }
	if (e.getKeyText(e.getKeyCode()).equals("Shift")) {
	    shift_pressed = false; }
    }

    public void keyTyped (KeyEvent e) {
	GraphicNode grunt;
	char c;

	//displayInfo(e, "KEY_TYPED: ");
	this.requestFocusInWindow();
	// dealt with Enter under "KeyPressed".
	//	if (e.getKeyText(e.getKeyCode().equals("Enter"))) { return; }

	if (will_delete) { 
	    my_text_field.deleteChar();
	    will_delete = false;
	    return; }
	if (found_error) {
	    my_text_field.refresh();
	    found_error = false;
	    return; }
	c = e.getKeyChar();

	if (c == ' ') {
	    grunt = CorpusDraw.currTree().getGraphicBulletNode();
	    CorpusDraw.currTree().toggleSelected(grunt);
	    DrawGraphicTree.whichSelect(CorpusDraw.currTree(), grunt);
	    return; }
	if (!esc_pressed && !e.isActionKey() && getting_text) {
	    my_text_field.addChar(c);
	    //my_text_field.PrintToSystemErr();
	    return; }
	if (!esc_pressed && !e.isActionKey() && getting_number) {
	    my_text_field.addDigit(c);
	    return; }
	//if ((int)c == )
	if (esc_pressed) {
	    if (c == 'm') {
		CorpusDraw.currTree().moveTo();
		resetTree(); return; }
	    if (c == 'd') {
		CorpusDraw.currTree().delete_node( );
		resetTree(); return ; }
	    if (c == 'n') { addNodeCanvas(); }
	    if (c == 'r') {
		CorpusDraw.currTree().redo();
		resetTree(); return; }
	    if (c == 'u') {
		CorpusDraw.currTree().undo();
		resetTree(); return; }
	    if (c == 'c') {
		CorpusDraw.currTree().co_index();
		resetTree(); return; }
	    if (c == 'l') { replaceLabelCanvas(); }
	    if (c == 'a') { leafAfterCanvas(); }
	    if (c == 'b') { leafBeforeCanvas(); }
	    if (c == 'x') { traceAfterCanvas(); }
	    if (c == 'y') { traceBeforeCanvas(); }
	    if (c == 'p') {
		CorpusDraw.currTree().mergePrevious();
		resetTree(); return; }
	    if (c == 'f') {
		CorpusDraw.currTree().mergeFollowing();
		resetTree(); return; }
	    if (c == 's') {
		CorpusDraw.currTree().split();
		resetTree(); return; }
	    if (c == 'g') {
		goToCanvas(); }
	    if (c == '1') {
 		MyEvents.setNextToken(); resetTree(); return;}
	    if (c == '2') {
 		MyEvents.setPrevToken(); resetTree(); return;}
	}
	
    }

    private void goToCanvas() {
	GraphicNode grunt;

	CorpusDraw.toole.treeve.scrollToRoot(); 
	this.requestFocusInWindow();
	getting_number = true;
	graphite = this.getGraphics();
	my_text_field = new TreeTextField(graphite);
	MyEvents.warn("goto:  type number in window:");
	esc_pressed = false; }
    
    private void traceAfterCanvas() {
	GraphicNode grunt;

	enter_pressed = false;
	grunt = CorpusDraw.currTree().blankTraceAfter();
	if (grunt.IsNullGNode()) { resetTree(); return; }
	resetTree();
	my_text_field = new TreeTextField(grunt, graphite);
        getting_text = true;
	new_trace_POS = true;
	will_coindex = true; }

    private void traceBeforeCanvas() {
	GraphicNode grunt;
		
	enter_pressed = false;
	grunt = CorpusDraw.currTree().blankTraceBefore();
	if (grunt.IsNullGNode()) { resetTree(); return; }
	resetTree();
	my_text_field = new TreeTextField(grunt, graphite);
        getting_text = true;
	new_trace_POS = true;
	will_coindex = true; }

    private void leafAfterCanvas () {
	GraphicNode grunt;

	grunt = CorpusDraw.currTree().blankLeafAfter();
	if (grunt.IsNullGNode()) { resetTree(); return; }
	resetTree();
	my_text_field = new TreeTextField(grunt, graphite);
	getting_text = true;
	new_trace_POS = true;
	esc_pressed = false; }

    private void leafBeforeCanvas(){
	GraphicNode grunt;

	grunt = CorpusDraw.currTree().blankLeafBefore();
	if (grunt.IsNullGNode()) { resetTree(); return; }
	resetTree();
	my_text_field = new TreeTextField(grunt, graphite);
	getting_text = true;
	new_trace_POS = true;
	esc_pressed = false; }

    private void addNodeCanvas() {
	GraphicNode grunt;
	
	grunt = CorpusDraw.currTree().addBlankSynNode();
	if (grunt.IsNullGNode()) { resetTree(); return; }
	resetTree();
	must_undo = 1;
	getting_text = true;
	my_text_field = new TreeTextField(grunt, graphite);
	esc_pressed = false; }


    private void replaceLabelCanvas() {
	GraphicNode grunt;

	did_it = CorpusDraw.currTree().replaceLabel0();
	if (!did_it) { resetTree(); return; }
	getting_text = true;
	grunt = CorpusDraw.currTree().popSelected();
	my_text_field = new TreeTextField(grunt, graphite);
	esc_pressed = false; }

    private void newTracePOS() {
	GraphicNode grunt;

	labell = my_text_field.getSynString();
	did_it = CorpusDraw.currTree().legitTracePOS(labell);
	if (!did_it) { 
	    found_error = true;
	    this.requestFocusInWindow(); 
	    MyEvents.intf.clear(); return; }
	//labell = CorpusDraw.currTree().getMyNewLabel();
	CorpusDraw.currTree().replaceLabel2(labell, my_text_field.getNode());
	nodal = CorpusDraw.currTree().sparse.
	    FirstDaughter(my_text_field.getNode());
	grunt = CorpusDraw.currTree().getGNode(nodal);
	resetTree();
	my_text_field = new TreeTextField(grunt, graphite);
	new_trace_POS = false;
	getting_text = true;
	new_trace_text = true;
	//	will_coindex = true;
	return; }

    private void newTraceText() {

	labell = my_text_field.getString();
	did_it = CorpusDraw.currTree().legitEmptyCat(labell);
	if (!did_it) { 
	    found_error = true;
	    this.requestFocusInWindow(); 
	    MyEvents.intf.clear(); return; }
	//labell = CorpusDraw.currTree().getMyNewLabel();
	CorpusDraw.currTree().replaceLabel2(labell, my_text_field.getNode());
	new_trace_text = false;
	if (will_coindex) {
	    nodal = CorpusDraw.currTree().getNewNode(my_text_field.getNode());
		    CorpusDraw.currTree().
			co_index(nodal,
			CorpusDraw.currTree().getUpSNode2(), 3);
		    will_coindex = false; }
	resetTree(); return; }

  protected void displayInfo(KeyEvent e, String s){
      String keyString, modString, locationString, actionString, tmpString;

        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
	    int keyCode = e.getKeyCode();
	    keyString += ":  keyCode = " + keyCode; }
        else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode + " ("
                        + KeyEvent.getKeyText(keyCode) + ")"; }

        int modifiers = e.getModifiersEx();
        modString = "modifiers = " + modifiers;
        tmpString = KeyEvent.getModifiersExText(modifiers);
        if (tmpString.length() > 0) { modString += " (" + tmpString + ")"; }
        else { modString += " (no modifiers)"; }

        actionString = "action key? ";
        if (e.isActionKey()) { actionString += "YES";}
	else { actionString += "NO"; }

        locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        
	System.err.println(s);
	System.err.println("keyString:  " + keyString);
	System.err.println("modString:  " + modString);
	//System.err.println("actionString:  " + actionString);
	//System.err.println("locationString:  " + locationString);
    }

    }

   
