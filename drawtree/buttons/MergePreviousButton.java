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

public class MergePreviousButton implements ActionListener {

    private Button merge_prev;
    private boolean PRESSED;

    public MergePreviousButton() {
	merge_prev = new Button("<--Merge");
	merge_prev.setForeground(MyColors.white); 
	merge_prev.addActionListener(this); 
        merge_prev.setBackground(MyColors.violet); }

    public Button getButton() {
	return merge_prev; }

    public void resetButton() {
	PRESSED = false; }

    public boolean getPressed() {
	return PRESSED; }

    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == merge_prev) {
	    PRESSED = true; }}

}
