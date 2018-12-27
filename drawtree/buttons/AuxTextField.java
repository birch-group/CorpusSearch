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


public class AuxTextField {

    private TextField aux_field;
    private String start_message = "query will appear here";

    public AuxTextField(int tool_width) {
	aux_field = new TextField(start_message, tool_width/2);
	aux_field.setEditable(false); }

    public AuxTextField() {
	aux_field = new TextField(start_message); 
        aux_field.setEditable(false); } 

    public TextField getTextField() {
	return aux_field; }
			      
    public void put(String err_str) {
	aux_field.setText(err_str); }

    public void clear() {
	aux_field.setText(""); }
}

