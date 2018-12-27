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

public class AnnounceTextField{

    private TextField announce_field;
    private String start_message = "messages from CorpusDraw will appear here";

    public AnnounceTextField() {
	announce_field = new TextField(start_message); 
        announce_field.setEditable(false); } 

    public TextField getTextField() {
	return announce_field; }
			      
    public void put(String err_str) {
	announce_field.setText(err_str); }

    public void clear() {
	announce_field.setText(""); }
}

