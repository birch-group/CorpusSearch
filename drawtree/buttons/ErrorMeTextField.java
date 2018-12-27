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


public class ErrorMeTextField {

    private TextField error_field;
    private String start_message = "messages from CorpusDraw will appear here";

    public ErrorMeTextField() {
	error_field = new TextField(start_message); 
        error_field.setEditable(false); } 

    public TextField getTextField() {
	return error_field; }
			      
    public void error_put(String err_str) {
	try {
	    //error_field.requestFocus();
	    error_field.setText(err_str); }
	catch (Exception e) {e.printStackTrace(); }
	finally { return; }}

    public void willDo(String func_name) {
	error_field.setText(func_name); }

    public void willDo (String func_name, String to_func) {
	error_field.setText(func_name + ":  " + to_func); }

    public void willDo (String func_name, String to_func, String to_func2) {
	error_field.setText(func_name + ":  " 
			    + to_func + ", " + to_func2); }

    public void cant (String func_name, String to_func) {
	error_field.setText("cannot " + func_name + ":  " + to_func); } 

    public void cant (String func_name, String to_func, String to_func2) {
	error_field.setText("cannot " + func_name + ":  " 
			    + to_func + ", " + to_func2); }

    public void clear() {
	error_field.setText(""); }
}

