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

package buttons;

import java.awt.*;
import java.awt.event.*;

public class InputTextField implements ActionListener {

    private TextField in_field;
    boolean TEXT_CHANGED = false;
    private String start_message = "your input will appear here";
    private String my_text, pos, text, new_label, message;


    public InputTextField(int width) {
	in_field = new TextField(start_message, width);
	in_field.addActionListener(this); }

    public InputTextField() {
	in_field = new TextField(start_message, 10); 
        in_field.addActionListener(this); }

    public TextField getTextField() {
	return in_field; }

    public void setInputText(String str) {
	in_field.setText(str);
	try {
	    in_field.setCaretPosition(str.length()); }
	catch (Exception e) { } }
	    
    public String getPos (){
	return pos; }

    public String getText() {
	return text; }

    public boolean getPosText() {
	int space_dex;

 	wait_loop: while (true) {
 	    if (MyEvents.interrupt("insert leaf after")) {
		MyEvents.lab.resetButton();
		return false; }
 	    if (MyEvents.intf.getChanged()) {
 		new_label = MyEvents.intf.getInputText();
 		//new_label = new_label.toUpperCase();
 		//if (!legitLeaf(new_label)) {
 		//  continue wait_loop; }
 		space_dex = new_label.indexOf(" ");
 		if (space_dex == -1) {
 		    message = "ERROR! leaf must have form:  POS TEXT";
 		    MyEvents.warn(message);
		    MyEvents.intf.resetChanged();
		    MyEvents.intf.setInputText(new_label);
 		    continue wait_loop; }
 		pos = (new_label.substring(0, space_dex)).trim();
 		pos = pos.toUpperCase();
 		text = (new_label.substring(space_dex + 1)).trim();
 		//if (!legitLeaf(pos, text)) {
		//continue wait_loop; }
 		MyEvents.warn("got new trace:  " + new_label);
 		MyEvents.intf.resetChanged();
 	        break; } 
 	    if (MyEvents.ub.getPressed()) {
 		break; } }
	return true; }


    public void requestFocus() {
	in_field.requestFocus(); }
	
    public String getInputText() {
	return my_text; }

    public void clear() {
	in_field.setText(""); }

    public boolean getChanged() {
	return TEXT_CHANGED; }

    public void resetChanged() {
	TEXT_CHANGED = false; }
   
    public void actionPerformed(ActionEvent evt) {
	my_text = in_field.getText();
	in_field.setText("got it!  " + my_text);
	TEXT_CHANGED = true;
    }



}

