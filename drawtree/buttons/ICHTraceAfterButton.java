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

public class ICHTraceAfterButton implements ActionListener {

    private Button ich_trace_after;
    private boolean PRESSED;

    public ICHTraceAfterButton() {
	ich_trace_after = new Button("*ICH*-->");
	ich_trace_after.addActionListener(this); 
        ich_trace_after.setBackground(MyColors.dark_pink); }

    public Button getButton() {
	return ich_trace_after; }

    public void resetButton() {
	PRESSED = false; }

    public boolean getPressed() {
	return PRESSED; }

    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == ich_trace_after) {
	    PRESSED = true; }}

}
