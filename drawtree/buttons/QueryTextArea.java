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


public class QueryTextArea {

    private TextArea query_area;
    private String start_message = "query will appear here";

    public QueryTextArea() {
	query_area = new TextArea(start_message, 1, 700); 
        query_area.setEditable(false); } 

    public TextArea getTextArea() {
	return query_area; }
			      
    public void put(String err_str) {
	query_area.setText(err_str); }

    public void clear() {
	query_area.setText(""); }
}

