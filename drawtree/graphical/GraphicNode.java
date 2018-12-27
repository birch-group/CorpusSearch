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
import syntree.*;

public class GraphicNode {

    public boolean PRESSED = false;
    private Node myNode;
    private String label;
    private int index, x_start, x_end, y_start, y_end;
    private boolean collapsed, collapsed_root;
    private boolean highlight1, highlight2;

    public GraphicNode(String nullski) {
	myNode = new Node("NULL");
        Init(myNode); }

    public GraphicNode(Node nodal) {
	Init(nodal); }

    public GraphicNode (Node nodal, int in_x_start, int in_x_end, 
			int in_y_start, int in_y_end) {
	this.x_start = in_x_start;
	this.x_end = in_x_end;
	this.y_start = in_y_start;
	this.y_end = in_y_end;
	Init(nodal); }

    public GraphicNode(Node nodal, int in_y_start, int in_y_end) {
	this.y_start = in_y_start;
	this.y_end = in_y_end;
	Init(nodal); }

    private void Init(Node nodal) {
	myNode = nodal;
	label = myNode.getLabel();
	index = myNode.getIndex_int(); 
	collapsed = false;
	collapsed_root = false; }
    //        highlight = false; }

    public boolean isHighlighted1() {
	return this.highlight1; }

    public void setHighlight1(boolean value) {
	this.highlight1 = value; }

    public boolean isHighlighted2() {
	return this.highlight2; }

    public void setHighlight2(boolean value) {
	this.highlight2 = value; }

    public void setCollapsed(boolean value) {
	collapsed = value; }

    public boolean getCollapsed() {
	return collapsed; }

    public void setCollapsedRoot(boolean value) {
	collapsed_root = value; }

    public boolean getCollapsedRoot() {
	return collapsed_root; }

    public boolean equals (GraphicNode other) {
	return ((this.getNode()).equals(other.getNode())); }

    public boolean equals (Node other) {
	return ((this.getNode()).equals(other)); }

    public boolean IsNullGNode() { return myNode.IsNullNode(); }

    public void setXStart (int x) { x_start = x; }

    public void setYStart (int y) { y_start = y; }

    public void setXEnd (int x) { x_end = x; }

    public void setYEnd (int y) { y_end = y; }

    public void setYs (int start, int end) {
	y_start = start;
	y_end = end; }

    public void setXs (int start, int end) {
	x_start = start;
	x_end = end; }

    public int getXStart () { return x_start; }

    public int getYStart () { return y_start; }

    public int getXEnd () { return x_end; }

    public int getYEnd () { return y_end; }

    public boolean isInX (int coord) {
	if (x_start <= coord && coord <= x_end) {
	    return true; }
	return false; }

    public boolean isInY (int coord) {
	if (y_start <= coord && coord <= y_end) {
	    return true; }
	return false; }   

    public boolean isInNode (int coord) {
	if (!isInX(coord)) {
	    return false; }
	if (!isInY(coord)) {
	    return false; }
	return true; }
 
    public int compareToX(int x) {
	if (x < x_start) {
	    return -1; }
	if (x <= x_end) {
	    return 0; }
	else {
	    return 1; } }

    // remember, y_start is greater than y_end!
    public int compareToY(int y) {
	if (y > y_start) {
	    return -1; }
	if (y >= y_end) {
	    return 0; }
	else {
	    return 1; } }

    public boolean sameY(GraphicNode other_gnode) {
	if (this.getYStart() == other_gnode.getYStart()) {
	    return true; }
	return false; }

    // the penumbra is added to make it slightly easier to select nodes.
    public boolean containsCoords(int x, int y) {
	int penumbra = 15;

	if (x < x_start - penumbra) { return false; }
	if (x > x_end + penumbra) { return false; }
	if (y > y_start + penumbra) { return false; }
	if (y < y_end - penumbra) { return false; }
	else { return true; } }

    public Node getNode() {
	return myNode; }

    public String getLabel() {
	return (this.label); }

    public int getIndex() {
	return (this.index); }

    public void shiftLeft(int factor) {
	int new_x_start, new_x_end;

	new_x_start = this.getXStart() - factor;
	this.setXStart(new_x_start);
	new_x_end = this.getXEnd() - factor;
	this.setXEnd(new_x_end); } 

    public void shiftRight(int factor) {
	int new_x_start, new_x_end;

	new_x_start = this.getXStart() + factor;
	this.setXStart(new_x_start);
	new_x_end = this.getXEnd() + factor;
	this.setXEnd(new_x_end); } 

    public void shiftUp(int factor) {
	int new_y_start, new_y_end;
	
	new_y_start = this.getYStart() - factor;
	this.setYStart(new_y_start);
	new_y_end = this.getYEnd() - factor;
	this.setYEnd(new_y_end); } 


    public void shiftDown(int factor) {
	int new_y_start, new_y_end;
	    
	new_y_start = this.getYStart() + factor;
	this.setYStart(new_y_start);
	new_y_end = this.getYEnd() + factor;
	this.setYEnd(new_y_end); } 


    public String toString() {
	String groan;
	groan = myNode.toString() + ", " + x_start + ", " + x_end;
	return groan; }

    public void PrintToSystemErr() {
	System.err.print(myNode.toString() + ", " + x_start + ", " + x_end);
	System.err.println(", " + y_start + ", " + y_end);
        //System.err.print(", high1:  " + this.highlight1);
        //System.err.println(", high2:  " + this.highlight2); 
    }

}
