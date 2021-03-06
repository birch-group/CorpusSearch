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
import java.util.*;
import syntree.*;
import basicinfo.*;
import javax.swing.*;
import search_result.*;

/**
DrawGraphicTree takes as input a ChangeGraphicTree, where the x and
y coordinates of each graphic node have been worked out.  */

public class DrawGraphicTree extends TreeCanvas {

    public static Graphics my_graphite;
    protected static int Y_BUFFER = 16;

    public  static void drawTree (ChangeGraphicTree cgt, Graphics graphite) {

	my_graphite = graphite;
	CorpusDraw.toole.treeve.treece.setSizeCurrTree();
	if (cgt.isEmpty()) { return; }
	cgt.setCollapseList(DrawLoop.getCollapseList());
	cgt.setCollapsedBits();
	if (!cgt.willCollapse()) {
	    bottomUp(cgt);  return; }
	else {
	    bottomUpCollapse(cgt); return; } }

    protected  static void bottomUpCollapse (ChangeGraphicTree cgt) {
	int i,k, end_collapse;
	Node leaf;
	GraphicNode gleaf;
  
	// first, draw text leaves of tree.
	tree_loop:  for (i = 0; i < cgt.gt_size(); i++) {
	    gleaf = cgt.graphicNodeAt(i);
	    leaf = gleaf.getNode();
	    if (cgt.IsLeafText(leaf)) {
		if (cgt.isCollapsed(i)) {
		    // advance i past the collapsed subtree.
		    end_collapse = cgt.getCollapsedEnd(leaf);
		    i = end_collapse;
		    continue tree_loop; }
		else { drawGNodeString(cgt, gleaf); }}}
	for (k = cgt.getMaxDepth(); k >= 0; k --) {
	    subBottomCollapse(cgt, k); }
	//	cgt.PrintToSystemErr();
    }

    protected  static void bottomUp (ChangeGraphicTree cgt) {
	int i, k;
	Node leaf;
	GraphicNode gleaf;

	// first, draw text leaves of tree.
	for (i = 0; i < cgt.gt_size(); i++) {
	    gleaf = cgt.graphicNodeAt(i);
	    leaf = gleaf.getNode();
	    if (cgt.IsLeafText(leaf)) {
		drawGNodeString(cgt, gleaf); } }
	for (k = cgt.getMaxDepth(); k >= 0; k --) {
	    subBottom(cgt, k); }
	//	cgt.PrintToSystemErr();
    }
	    
    protected  static void subBottom(ChangeGraphicTree cgt, int depth) {
	int i;
	Node depth_node = new Node("NULL");
	GraphicNode gnode;
	Vector layer;

	layer = (cgt.getChangeTree()).getAllNodesforDepth(depth);
	layer_loop: for (i = 0; i < layer.size(); i++) {
	    depth_node = (Node)layer.elementAt(i); 
	    if ((cgt.getChangeTree()).IsLeafText(depth_node)) {
		continue layer_loop;  }
 	    gnode = cgt.getGNode(depth_node);
 	    drawGStringLines(cgt, gnode); } }

     protected  static void subBottomCollapse(ChangeGraphicTree cgt, 
					     int depth) {
 	int i, j, k, y_end;
 	GraphicNode gmom, gdepth;
 	Node first_kid, last_kid, depth_node = new Node("NULL");
 	Vector layer, depth_daughters;

 	layer = (cgt.getChangeTree()).getAllNodesforDepth(depth);
 	layer_loop: for (i = 0; i < layer.size(); i++) {
 	    depth_node = (Node)layer.elementAt(i);
 	    // is depth_node the root of a collapsed subtree?
 	    if (cgt.isCollapseSubRoot(depth_node)) {
		gmom = cgt.getGNode(depth_node);
		drawGStringLinesCollapseRoot(cgt, gmom);
 		continue layer_loop; }
 	    if ((cgt.getChangeTree()).IsLeafText(depth_node) || 
 		cgt.isCollapsed(depth_node.getIndex_int())) { 
 		continue layer_loop; }
	    gdepth = cgt.getGNode(depth_node);
 	    drawGStringLines(cgt, gdepth);
 	}
     }

     protected  static void drawMomLines (ChangeGraphicTree cgt, 
					 GraphicNode gmom) {
 	Vector daughters;
 	Node nodal, mom;
 	GraphicNode gnode;
 	int i, mom_x, mom_y, daught_x, daught_y;

	mom = new Node("NULL");
	try {
	    my_graphite.setColor(ToolColors.LINE);
	    mom = gmom.getNode();
	    if (mom.IsNullNode() || mom.getIndex_int() == 0) { return; }
	    if (cgt.sparse.getRootNode().equals(mom)) {
		drawMomRootLines(cgt, gmom);
	        return; }
	    daughters = (cgt.getChangeTree()).GetDaughters(mom);
	    mom_x = cgt.getMiddleX(gmom);
	    mom_y = gmom.getYEnd() + Y_BUFFER;
	    for (i = 0; i < daughters.size(); i++) {
		nodal = (Node)daughters.elementAt(i);
		gnode = cgt.graphicNodeFor(nodal);
		if (daughters.size() == 1) { daught_x = mom_x; }
		else { daught_x = cgt.getMiddleX(gnode); }
		daught_y = gnode.getYEnd();
		my_graphite.drawLine(mom_x, mom_y, daught_x, daught_y); } }
	catch (Exception e) {
	    //e.printStackTrace();
	    System.err.println("will print cgt:  ");
	    cgt.PrintToSystemErr(mom.getIndex_int()-5, mom.getIndex_int()+5);
	    System.err.println("in DrawGraphicTree.drawMomLines:  ");
	    System.err.println("gmom:  " + gmom.getNode().toString()); 
	    System.exit(1); }
	finally { return; } }

    //draw special "bent" lines from root to root's daughters.
     protected  static void drawMomRootLines (ChangeGraphicTree cgt, 
					 GraphicNode gmom) {
 	Vector daughters;
 	Node nodal, mom;
 	GraphicNode gnode;
 	int i, mom_x, mom_y, daught_x, daught_y;

	try {
	    my_graphite.setColor(ToolColors.LINE);
	    mom = gmom.getNode();
	    if (mom.IsNullNode() || mom.getIndex_int() == 0) { return; }
	    daughters = (cgt.getChangeTree()).GetDaughters(mom);
	    mom_x = cgt.getMiddleX(gmom);
	    mom_y = gmom.getYEnd() + Y_BUFFER;
	    for (i = 0; i < daughters.size(); i++) {
		nodal = (Node)daughters.elementAt(i);
		gnode = cgt.getGNode(nodal);
		if (daughters.size() == 1) {
		    daught_x = mom_x; }
		else {
		    daught_x = cgt.getMiddleX(gnode); }
		daught_y = gnode.getYEnd();
		// first, draw vertical line.
		my_graphite.drawLine(daught_x, mom_y, daught_x, daught_y);
		my_graphite.drawLine(daught_x, mom_y, mom_x, mom_y);
		//my_graphite.drawLine(mom_x, mom_y, daught_x, daught_y); 
	    } }
	catch (Exception e) {
	    System.err.println("in DrawGraphicTree.drawMomLines:  ");
	    System.exit(1); }
	finally { return; } }

    protected static void drawGCircle (ChangeGraphicTree cgt, 
				      GraphicNode gmom) {
 	Node mom;
 	int mom_x, mom_y;

	my_graphite.setColor(ToolColors.COLLAPSE);
	mom = gmom.getNode();
 	mom_x = cgt.getMiddleX(gmom);
 	mom_y = gmom.getYEnd() + Y_BUFFER;
	my_graphite.drawOval(mom_x, mom_y, 8, 8);
	return; }
	

     protected  static Node getOnlyAncestor(ChangeTree sparse, Node nodal, 
					   Node stop) {
 	Node anc = nodal, best = nodal;

 	while (!anc.IsNullNode() && !anc.equals(stop)) {
 	    anc = sparse.GetMother(anc);
 	    if (anc.IsNullNode() || anc.equals(stop)) {
 		return best; }
 	    if (CorpusDraw.show_list.hasMatch(anc)) {
 		best = anc; } }
 	return anc; }

    protected  static void drawGStringLines(ChangeGraphicTree cgt, 
					   GraphicNode gnode) {
 	drawGNodeString(cgt, gnode); 
 	drawMomLines(cgt, gnode); }

     protected static void drawGStringLinesCollapseRoot(ChangeGraphicTree cgt, 
							GraphicNode gnode) {

 	//gnode.PrintToSystemErr();
 	if (CorpusDraw.show_only && !CorpusDraw.show_list.isEmpty() &&
 	    !CorpusDraw.show_list.hasMatch(gnode.getLabel())) {
 		return; }
	drawCollGNodeString(cgt, gnode); 
 	//drawTriangle(cgt, gnode); }
	drawGCircle(cgt, gnode); }

    protected  static void drawGNodeString(ChangeGraphicTree cgt, 
					  GraphicNode gnode) {
	try {
	    if (gnode.isHighlighted1()) { drawHighlight1(gnode); }
	    if (gnode.isHighlighted2()) { drawHighlight2(gnode); }
	    if (cgt.getsBullet(gnode)) { drawBullet(cgt, gnode); }
	    my_graphite.setColor(ToolColors.TEXT);
	    if (!cgt.justInits()) {
		my_graphite.drawString(gnode.getLabel(), 
				       gnode.getXStart(), 
				       gnode.getYStart()); }
	    else {
		my_graphite.drawString(gnode.getLabel().substring(0, 1),
				       gnode.getXStart(),
				       gnode.getYStart()); }
	    if (cgt.isSelected(gnode)) { drawSelect(cgt, gnode); } }
        catch (Exception e) { e.printStackTrace(); }
        finally { return; } }

    protected  static void drawCollGNodeString(ChangeGraphicTree cgt, 
					  GraphicNode gnode) {
	try {
	    if (gnode.isHighlighted1()) { drawHighlight1(gnode); }
	    if (gnode.isHighlighted2()) { drawHighlight2(gnode); }
	    if (cgt.getsBullet(gnode)) { drawBullet(cgt, gnode); }
	    my_graphite.setColor(ToolColors.COLLAPSE);
	    my_graphite.drawString(gnode.getLabel(),
				   gnode.getXStart(),
				   gnode.getYStart()); 
	    if (cgt.isSelected(gnode)) { drawSelect(cgt, gnode); } }
        catch (Exception e) { e.printStackTrace(); }
        finally { return; } }


    protected static void whichSelect(ChangeGraphicTree cgt,
				      GraphicNode gnode) {
	if (cgt.isSelected(gnode)) {
	    drawSelect(cgt, gnode); return; }
	drawUnSelect(cgt, gnode); return; }

    protected  static void drawSelect(ChangeGraphicTree cgt, 
				     GraphicNode gnode) {
	my_graphite.setColor(ToolColors.SELECT); 
	drawSelectRectangle(cgt, gnode); }

   
    
    protected static void drawUnSelect(ChangeGraphicTree cgt, 
				       GraphicNode gnode) {
	my_graphite.setColor(ToolColors.BACKGROUND); 
	drawSelectRectangle(cgt, gnode); }

    protected static void drawSelectRectangle(ChangeGraphicTree cgt,
					      GraphicNode gnode) {

	my_graphite.drawRect(gnode.getXStart() - 2, gnode.getYEnd(),
			     gnode.getXEnd() - gnode.getXStart() + 2, 
			     gnode.getYStart() - gnode.getYEnd()); }


    protected  static void drawHighlight1(GraphicNode gnode) {
	my_graphite.setColor(ToolColors.HIGHLIGHT1); 
	fillHighlightRect(gnode); }

    protected  static void drawHighlight2(GraphicNode gnode) {
	my_graphite.setColor(ToolColors.HIGHLIGHT2); 
	fillHighlightRect(gnode); }

    protected static void fillHighlightRect(GraphicNode gnode) {
	my_graphite.fillRect(gnode.getXStart() - 2, gnode.getYEnd() + 1, 
			  gnode.getXEnd() - gnode.getXStart() + 2, 
			  gnode.getYStart() - gnode.getYEnd() - 1); }

    protected static void drawBullet(ChangeGraphicTree cgt, 
				     GraphicNode gnode) {
	my_graphite.setColor(ToolColors.BULLET);
	subBullet(cgt, gnode); }

    protected static void subBullet(ChangeGraphicTree cgt, 
				    GraphicNode gnode) {
	my_graphite.fillOval(cgt.getMiddleX(gnode), 
			     gnode.getYEnd() - 8, 6, 6); } 

    protected static void clearBullet(ChangeGraphicTree cgt, 
				      GraphicNode gnode) {
	my_graphite.setColor(ToolColors.BACKGROUND);
	subBullet(cgt, gnode); }
}

