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
import print.*;
import syntree.*;


public class MySave {
    public static int willSave() {
	Dialog myDialog;
	TextField alert_text;
	GridBagConstraints alert_c;
	GridBagLayout alert_gridbag, button_gridbag;
	YesSaveButton yesb;
	NoSaveButton nob;
	DontQuitButton dqb;
	Button blank = new Button("");
	Panel button_panel;
	boolean hack;

	alert_text = new TextField(" save before quitting?"); 
	alert_text.setBackground(ToolColors.HIGHLIGHT1);
	myDialog = new Dialog(CorpusDraw.toole.getFrame(), "Save?");
	myDialog.setBackground(ToolColors.HIGHLIGHT1);
	blank.setBackground(ToolColors.HIGHLIGHT1);
	alert_c = new GridBagConstraints();
	alert_gridbag = new GridBagLayout();
	myDialog.setLayout(alert_gridbag);
	alert_c.fill = GridBagConstraints.BOTH;
	alert_c.gridx = 0; alert_c.gridy = 0;
	myDialog.add(alert_text, alert_c);
	button_gridbag = new GridBagLayout();
	button_panel = new Panel(button_gridbag);
	alert_c  = new GridBagConstraints();
	alert_c.gridx = 0; alert_c.gridy = 0;
	button_panel.add(blank, alert_c);
	alert_c.gridx = 0; alert_c.gridy = 1;
	yesb = new YesSaveButton();
	button_panel.add(yesb.getButton(), alert_c);
	alert_c.gridx = 0; alert_c.gridy = 2;
	blank = new Button();
	blank.setBackground(ToolColors.HIGHLIGHT1);
	button_panel.add(blank, alert_c);
	alert_c.gridx =  0; alert_c.gridy = 3;
	nob = new NoSaveButton();
	button_panel.add(nob.getButton(), alert_c);
	alert_c.gridx = 0; alert_c.gridy = 4;
	blank = new Button();
	blank.setBackground(ToolColors.HIGHLIGHT1);
	button_panel.add(blank, alert_c);
	alert_c.gridx = 0; alert_c.gridy = 5;
	dqb = new DontQuitButton();
	button_panel.add(dqb.getButton(), alert_c);
	alert_c.gridx = 0; alert_c.gridy = 1;
	myDialog.add(button_panel, alert_c);
	myDialog.setSize(300, 300);
	myDialog.show();

	while(true) {
	    // the following System.err.print makes the
	    // dialog box work, for some mysterious reason.
	    // Delete at your peril -- necessary hack.
	    System.err.print("");
	    if (nob.getPressed()) {
		nob.resetButton();
		myDialog.hide();
		return 0; }
	    if (yesb.getPressed()) {
		yesb.resetButton();
		myDialog.hide();
		return 1; } 
	    if (dqb.getPressed()) {
		dqb.resetButton();
		myDialog.hide();
		return -1; } }
    }


}
