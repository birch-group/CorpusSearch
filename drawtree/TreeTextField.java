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
//import javax.swing.*;
import search_result.*;

public class TreeTextField extends TreeCanvas {

    protected GraphicNode my_gnode;
    protected String my_string;
    protected Graphics my_graphite;
    protected TreeCursor my_cursor;
    protected FontMetrics fontmet;
    protected Character my_Char;
    protected int my_x, my_y, my_width, my_height, curse_dex;
    protected boolean selected_text;

    public TreeTextField() { my_string = ""; }

    public TreeTextField(GraphicNode in_gnode, Graphics grapho) {
	Init(in_gnode, grapho); }

    public TreeTextField(Graphics grapho) {  // for numerical input, e.g. GoTo.
	selected_text = true;
	my_gnode = CorpusDraw.currTree().getRootGNode();
	my_graphite = grapho;
	my_Char = new Character('Q'); 
        my_string = "";
	my_x = my_gnode.getXStart() - 6;
	my_y = my_gnode.getYEnd() - 200;
	my_height = my_gnode.getYStart() - my_gnode.getYEnd() + 6;
	eraseField();
	my_cursor = new TreeCursor(my_string);
	drawInfo();}

    protected void Init(GraphicNode in_gnode, Graphics grapho) {
	selected_text = true;
	super.requestFocusInWindow();
	my_graphite = grapho;
	my_gnode = in_gnode;
	my_string = my_gnode.getNode().getLabel();
	my_x = my_gnode.getXStart() - 6; 
	my_y = my_gnode.getYEnd();
	my_height = my_gnode.getYStart() - my_gnode.getYEnd() + 6; 
	my_cursor = new TreeCursor(my_string);
	drawInfo();
    }
    
    protected void refresh() { drawInfo(); }

    protected void eraseField() {
	int image_width;

	fontmet = getFontMetrics(my_graphite.getFont());
	image_width = fontmet.stringWidth(my_string);
	my_graphite.setColor(ToolColors.BACKGROUND);
	my_graphite.drawRect(my_x, my_y, image_width + 20, my_height);
	my_graphite.fillRect(my_x, my_y, image_width + 20, my_height); 
    }

    protected int ttfStringWidth(String label) {
	int len;
	fontmet = getFontMetrics(my_graphite.getFont());
	len = fontmet.stringWidth(label);
	//System.err.println("label, len:  " + label + ", " + len);
	return(len); }

   
    protected void drawInfo() {
	int draw_size, image_width, font_met, pre_width;
	String precursor, postcursor;


	//System.err.println("graphics font:  " + (my_graphite.getFont()).getName() + ", " + my_graphite.getFont().getSize() + ", " + my_graphite.getFont().getStyle());
	fontmet = getFontMetrics(my_graphite.getFont());
	image_width = fontmet.stringWidth(my_string);
	//System.err.println("image_width:  " + image_width);
	if (image_width < 12) { image_width = 12; }
	my_graphite.setColor(Color.white);
	if (selected_text) {
	    my_graphite.setColor(ToolColors.SELECT_TEXT); }
	my_graphite.fillRect(my_x, my_y, image_width + 20, my_height); 
	my_graphite.setColor(Color.black);
	my_graphite.drawRect(my_x, my_y, image_width + 20, my_height);
	precursor = my_cursor.getPre(my_string);
	pre_width = fontmet.stringWidth(precursor);
	my_graphite.drawString(precursor, my_x + 6,my_y + 12);
	my_graphite.setColor(Color.red);
	my_graphite.drawString("|", my_x + 6 + pre_width, my_y + 12); 
	my_graphite.setColor(Color.black);
	postcursor = my_cursor.getPost(my_string);
	pre_width = fontmet.stringWidth(precursor + "|");
	my_graphite.drawString(postcursor, my_x + 6 + pre_width, my_y + 12);
    }	


    protected void addDigit (char c) { 
	if ((int)c == 8 || (int)c == 10) { 
	    drawInfo(); return; } // backspace, enter
	eraseField();
	if (selected_text) {
	    my_string = "";
	    selected_text = false; 
	    drawInfo(); }
	if (my_Char.isDigit(c)) { 
	    my_string = my_cursor.addChar(c, my_string); }
	else {MyEvents.warn("not digit:  " + c); return; }
	drawInfo(); }

    protected void addChar (char c) { 
	if ((int)c == 8 || (int)c == 10) { 
	    drawInfo(); return; } // backspace, enter
	eraseField();
	if (selected_text) {
	    my_string = "";
	    selected_text = false; }
	my_string = my_cursor.addChar(c, my_string);
	drawInfo(); }

    protected void deleteChar () {
	eraseField();
	if (selected_text) { 
	  my_string = "";
	  selected_text = false; }
	//	if (my_string.length() == 0) { return; }
	my_string = my_cursor.deleteChar(my_string);
	drawInfo(); }

    protected void arrowRight() {
	if (selected_text) { 
	    selected_text = false;
	    drawInfo(); return; }
	my_cursor.plusOne(my_string);
        drawInfo(); }

    protected void arrowLeft() {
	if (selected_text) {
	    my_cursor.setWhere(0);
	    selected_text = false;
	    drawInfo(); return; }
	my_cursor.minusOne(my_string);
        drawInfo(); }


    protected String getString() { return my_string.trim(); }

    protected String getSynString() { 
	return ((my_string.toUpperCase()).trim()); }

    protected GraphicNode getGNode() { return my_gnode; }

    protected int getNumber() {
	Integer my_Int;
	int my_num = -1;

	try {
	    my_Int = new Integer(my_string);
	    my_num = my_Int.intValue(); }
	catch(Exception e) { my_num =  -1; }
	finally { return my_num; }}

    protected Node getNode() { return my_gnode.getNode(); }

    protected void PrintToSystemErr() {
	System.err.println("my_string: " + my_string); 
    }
}

   
