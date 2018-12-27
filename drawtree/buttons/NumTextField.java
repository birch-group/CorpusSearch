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

public class NumTextField{

    private TextField num_field;
    private String start_message = "0000";

    public NumTextField() {
	num_field = new TextField(start_message); 
        num_field.setEditable(false); } 

    public TextField getTextField() {
	return num_field; }
			      
    public void put(String err_str) {
	num_field.setText(err_str); }

    public void put (int num) {
	String num_str = padSpaces(num);
	num_str += num;
	this.put(num_str);
    }
    
    public String padSpaces(int num) {
	if (num > 0 && num < 10) {
	    return("      "); }
	if (num < 100) {
	    return("     "); }
	if (num < 1000) {
	    return("    "); }
	if (num < 10000) {
	    return("   "); }
	return(""); }

    public void clear() {
	num_field.setText(""); }
}

