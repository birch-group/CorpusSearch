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
import java.util.*;
import syntree.*;
import search_result.*;

public class ActionRecord {

    private ChangeGraphicTree tree_1, tree_2;
    private boolean has_tree_2;
    private int undo_next;
    private SentenceResult indices;
    private String action;

    public ActionRecord(){}

    public ActionRecord (String in_action, ChangeGraphicTree in_tree, 
			 int will_undo_next) {
	this.setAction(in_action);
	this.setTree(in_tree); 
        has_tree_2 = false; undo_next = will_undo_next; }
	

    public ActionRecord (String in_action, ChangeGraphicTree in_tree) {
	this.setAction(in_action);
	this.setTree(in_tree); 
        has_tree_2 = false; undo_next = 0; }

    public ActionRecord (String in_action, 
			 ChangeGraphicTree in_tree_1, 
			 ChangeGraphicTree in_tree_2) {
	this.setAction(in_action);
	this.setTree(in_tree_1, in_tree_2);
	has_tree_2 = true; undo_next = 0;  }

    public int unDoNext() { return undo_next; }

    public void setTree(ChangeGraphicTree in_tree) {
	this.tree_1 = in_tree; }

    public void setTree(ChangeGraphicTree in_tree_1, 
			ChangeGraphicTree in_tree_2) {
	this.tree_1 = in_tree_1;
	this.tree_2 = in_tree_2; }
    
    public void setAction(String in_action) {
	this.action = in_action; }

    public String getAction() {
	return this.action; }

    public ChangeGraphicTree getTree1() {
	return this.tree_1; }

    public ChangeGraphicTree getTree2() {
	return this.tree_2; }

    public boolean hasTree2() {
	return has_tree_2; }


}
