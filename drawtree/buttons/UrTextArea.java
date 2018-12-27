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

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class UrTextArea implements MouseListener {

    private TextArea ur_area;
    private int my_select, my_word_dex;
    private String my_sel_str, ur_string;
    boolean got_selected;

    public UrTextArea() {
	ur_area = new TextArea("Urtext will appear here", 2, 700);
	//ur_area.addMouseListener(this);
        ur_area.setEditable(false); 
        my_select = -3;
        my_sel_str = "XIANGQI";
        got_selected = false; } 

    public TextArea getTextArea() {
	return ur_area; }

    public boolean isSelected() {
	return got_selected; }

    public void resetSelected() {
	got_selected = false; }

    public void ur_put(String ur_str) {
	ur_area.setText(ur_str);
        this.ur_string = ur_str; }

    // translates selected index to index of word.
    public int getWordDex() {
	int num_spaces;

	num_spaces = this.countSpacesBefore(my_select);
	return num_spaces; }

    public String getSelectWord() {
	int space_before, space_after;
	String select_word = my_sel_str;
	Character chuck = new Character('Q');
	
	try {
	    if (my_select >= ur_string.length()) {
		my_select = ur_string.length() - 10; }
	    if (chuck.isWhitespace(ur_string.charAt(my_select))) {
		space_before = my_select; }
	    else {
		space_before = lastWhiteDex(ur_string, my_select, chuck); }
	    space_after = nextWhiteDex(ur_string, my_select + 1, chuck);
	    // deal with selected ID string.
	    if (space_after < 0) {
		space_before = lastWhiteDex(ur_string, space_before - 1, 
					    chuck);
		space_after = ur_string.length(); }
	    select_word = ur_string.substring(space_before + 1, space_after); }
	catch (Exception e) {
	    
	    e.printStackTrace();
	    System.exit(1); }
	finally { return select_word; } } 

    public int lastWhiteDex(String ur_string, int from, 
			    Character chuck) {
	int j;

	for (j = from; j >= 0; j --) {
	    if (chuck.isWhitespace(ur_string.charAt(j))) {
		return j; } }
	return -1; }

    public int nextWhiteDex(String ur_string, int from,
			    Character chuck) {
	int j;

	for (j = from; j < ur_string.length(); j ++) {
	    if (chuck.isWhitespace(ur_string.charAt(j))) {
		return j; } }
	return -1; }

    public int countSpacesBefore (int dex) {
	int num_spaces = 0, j = 0;

	while (j < dex && j >= 0) {
	    j = ur_string.indexOf(" ", j + 1);
	    if (j > 0) { num_spaces += 1; } }
	return (num_spaces - 1); }

    public void mouseClicked(MouseEvent e) {
	my_select = ur_area.getSelectionStart();
	my_sel_str = ur_area.getSelectedText();
	got_selected = true;
	my_word_dex = this.getWordDex(); }

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
	//	my_select = ur_area.getSelectionStart();
	//my_sel_str = ur_area.getSelectedText();
	//got_selected = true;
	//my_word_dex = this.getWordDex(); }
    }

    public void actionPerformed(ActionEvent evt) {
    }
}

