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

public class HelpButton implements ActionListener {

    private Button help;
    private boolean PRESSED;
    private Dialog myDialog;

    public HelpButton() {
	help = new Button("Help");
	help.addActionListener(this); 
        help.setBackground(MyColors.pale_orange); }  

    public Button getButton() {
	return help; }

    public void resetButton() {
	PRESSED = false; }

    public boolean getPressed() {
	return PRESSED; }

    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == help) {
	    PRESSED = true;

	int di_height = 500, di_width = 200;
	TextField alert_text;
	TextArea strokes;
	GridBagConstraints alert_c;
	GridBagLayout alert_gridbag, button_gridbag;
	YesButton yesb;
	NoMeButton nob;
	Button blank = new Button("");
	Panel button_panel;
	boolean will_close;

	strokes = new TextArea(30, 20);
	addStrokesList(strokes);
	myDialog = new Dialog(CorpusDraw.toole.getFrame(), "Help");
	myDialog.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    myDialog.hide(); }});
	myDialog.setBackground(ToolColors.HIGHLIGHT1);
	blank.setBackground(ToolColors.HIGHLIGHT1);
	strokes.setBackground(ToolColors.HIGHLIGHT1);
	alert_c = new GridBagConstraints();
	alert_gridbag = new GridBagLayout();
	myDialog.setLayout(alert_gridbag);
	alert_c.fill = GridBagConstraints.BOTH;
	alert_c.gridx = 0; alert_c.gridy = 0;
	alert_c.anchor = GridBagConstraints.LINE_START;
	myDialog.add(strokes, alert_c);
	myDialog.setSize(di_width, di_height);
	myDialog.setModal(false);
	myDialog.show();
	}

    }

    private void addStrokesList(TextArea strokes) {
	strokes.append("\n");
	strokes.append("ESC-u    undo\n");
	strokes.append("ESC-r    redo\n");
	strokes.append("ESC-d    delete\n");
	strokes.append("ESC-m    move to\n");
	strokes.append("ESC-n    add node\n");
	strokes.append("ESC-c    co-index\n");
        strokes.append("ESC-l    replace label\n");
        strokes.append("ESC-a    leaf after\n");
        strokes.append("ESC-b    leaf before\n");
        strokes.append("ESC-p    merge previous\n");
        strokes.append("ESC-f    merge following\n");
        strokes.append("ESC-s    split\n"); }



}


