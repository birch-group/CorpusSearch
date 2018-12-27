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

public class TreeView extends CorpusDraw {

    private ScrollPane treeScroll;
    protected TreeCanvas treece;
    int curr_scroll_ht, curr_scroll_wd;

    public TreeView() {
	Init(); }

    private void Init() {
	treece = new TreeCanvas();
	treeScroll = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
	treeScroll.add(treece);
	treeScroll.setWheelScrollingEnabled(true);
	this.moveScrollBars();
	return; }

    public ScrollPane getScrollPane() {
	return treeScroll; }

    public void scrollToRoot() {
	scrollToGNode(CorpusDraw.currTree().getRootGNode()); }

    public void scrollToGNode(GraphicNode gnode) {
	CorpusDraw.currTree().setScrollWordDex(gnode);
	this.moveScrollBars(CorpusDraw.currTree().getWordScrollWdth(),
			    CorpusDraw.currTree().getWordScrollHt()); 
    }

    public Node scrollToWord(int word_dex, String select_word) {
	Node to_node;

	to_node = CorpusDraw.currTree().
	    setScrollWordDex(word_dex, select_word);
	this.moveScrollBars(CorpusDraw.currTree().getWordScrollWdth(),
			    CorpusDraw.currTree().getWordScrollHt()); 
	return to_node; }

    public int getHeight() {
	return (this.getHeight()); }

    public void initScrollBars() {
	this.moveScrollBars(0, 300000); }

    public void moveScrollBars() {
	this.moveScrollBars(curr_scroll_wd, curr_scroll_ht); }
    
    public void moveScrollBars(int my_width, int my_height) {
	this.curr_scroll_ht = my_height;
	this.curr_scroll_wd = my_width;
	treeScroll.setScrollPosition(my_width, my_height); }

    public void moveScrollBars(ChangeGraphicTree cgt) {
	this.curr_scroll_ht = cgt.getScrollHt();
	this.curr_scroll_wd = cgt.getScrollHt();
	this.moveScrollBars(this.curr_scroll_wd, this.curr_scroll_ht); }
}
