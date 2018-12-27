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
import print.*;
import syntree.*;
import javax.swing.*;

public class ToolView implements WindowListener {

    private static Frame myFrame;
    private static Graphics grappa;
    private static GridBagConstraints c;    
    private static GridBagLayout gridbag;
    public static TreeView treeve;
    private static Font fonto;
    private Rectangle bounds;
    private Panel function_panel, display_panel, text_panel;
    private static UrText urt = new UrText();

    public ToolView() {
	int griddexx = -1;

	try{
	myFrame = new Frame ("CorpusDraw") ;
	fonto = myFrame.getFont();
	bounds = getVBounds();
	//myFrame.setResizable(true);
	c = new GridBagConstraints();
	gridbag = new GridBagLayout();
	myFrame.setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;
	GridBagLayout function_panelbag = new GridBagLayout();	
	function_panel = new Panel(function_panelbag); 
	c.weightx = 0.0; c.weighty = 0.0;
	c.gridy = 0;

	// back button.
	MyEvents.bb = new BackButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.bb.getButton(), c);

	// next button.
	MyEvents.nb = new NextButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.nb.getButton(), c);

	// goto button.
	MyEvents.gtb = new GoToButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.gtb.getButton(), c);

	// reset button.
	MyEvents.rsb = new ResetButton();
	griddexx += 1; c.gridx = griddexx;
	function_panel.add(MyEvents.rsb.getButton(), c);

	// undo button.
	MyEvents.ub = new UndoButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.ub.getButton(), c);

	// redo button.
	MyEvents.rb = new RedoButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.rb.getButton(), c);

	// replace label button.
	MyEvents.rlb = new ReplaceLabelButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.rlb.getButton(), c);

	// add internal node button.
        MyEvents.ainb = new AddInternalNodeButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.ainb.getButton(), c);

	// delete button.
	MyEvents.db = new DeleteButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.db.getButton(),  c);

	// raze button.
	MyEvents.rzb = new RazeButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.rzb.getButton(),  c);

	// move to button.
	MyEvents.mtb = new MoveToButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.mtb.getButton(), c);

	// coindex button.
	MyEvents.cib = new CoIndexButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.cib.getButton(), c);

	//leaf before button.
	MyEvents.lbb = new LeafBeforeButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.lbb.getButton(), c);

	//leaf after button.
	MyEvents.lab = new LeafAfterButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.lab.getButton(), c);

	// trace before button.
	MyEvents.tbb = new TraceBeforeButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.tbb.getButton(), c);

	// trace after button.
	MyEvents.tab = new TraceAfterButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.tab.getButton(), c);

	// merge previous button.
	MyEvents.mpb = new MergePreviousButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.mpb.getButton(), c);

	// merge following button.
	MyEvents.mfb = new MergeFollowingButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.mfb.getButton(), c); 

	// split button.
	MyEvents.sb = new SplitButton();
	griddexx += 1; c.gridx = griddexx; 
	function_panel.add(MyEvents.sb.getButton(), c);


	// add function_panel to myFrame.
	c.gridx = 0; c.gridy = 0;
	myFrame.add(function_panel, c);

	GridBagLayout text_panelbag = new GridBagLayout();	
	text_panel = new Panel(text_panelbag);
	griddexx = -1;
	c = new GridBagConstraints();
	c.weightx = 0.0; c.weighty = 0.0;

	// inputtext field.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.intf = new InputTextField();
	MyEvents.intf.setInputText("your input here");
	MyEvents.intf.getTextField().setColumns(30);
	text_panel.add(MyEvents.intf.getTextField(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	text_panel.add(MyEvents.blb.getButton(), c);

	// error text field.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.errtf = new ErrorMeTextField();
	MyEvents.warn("error messages here");
	MyEvents.errtf.getTextField().setColumns(40);
	text_panel.add(MyEvents.errtf.getTextField(), c);

	// blank button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.blb = new BlankButton();
	text_panel.add(MyEvents.blb.getButton(), c);

	// announce text field.
	griddexx += 1; c.gridx = griddexx;	
	MyEvents.anntf = new AnnounceTextField();
	MyEvents.anntf.put("file name");
	MyEvents.anntf.getTextField().setColumns(20);
	text_panel.add(MyEvents.anntf.getTextField(), c);

	// blank button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.blb = new BlankButton();
	text_panel.add(MyEvents.blb.getButton(), c);

	// number text field.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.numtf = new NumTextField();
	MyEvents.numtf.getTextField().setColumns(5);
	text_panel.add(MyEvents.numtf.getTextField(), c);

	c.gridx = 0; c.gridy = 1;
	myFrame.add(text_panel, c);

	GridBagLayout display_panelbag = new GridBagLayout();	
	display_panel = new Panel(display_panelbag);

	c = new GridBagConstraints();
	c.weightx = 0.0; c.weighty = 0.0;
	c.fill = GridBagConstraints.BOTH;
	griddexx = -1;

	// save button.
	MyEvents.svb = new SaveButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.svb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// shrink button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.shb = new ShrinkButton();
	display_panel.add(MyEvents.shb.getButton(), c);

	// swell button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.swb = new SwellButton();
	display_panel.add(MyEvents.swb.getButton(), c);

	// show only button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.sob = new ShowOnlyButton();
	display_panel.add(MyEvents.sob.getButton(), c);

	// show all button
	griddexx += 1; c.gridx = griddexx;
	MyEvents.sab = new ShowAllButton();
	display_panel.add(MyEvents.sab.getButton(), c);

	// show only list button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.solb = new ShowOnlyListButton();
	display_panel.add(MyEvents.solb.getButton(), c);

	// collapse button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.cb = new CollapseButton();
	display_panel.add(MyEvents.cb.getButton(), c);

	// expand button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.eb = new ExpandButton();
	display_panel.add(MyEvents.eb.getButton(), c);

	// expand all button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.eab = new ExpandAllButton();
	display_panel.add(MyEvents.eab.getButton(), c);

	// collapse list button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.clb = new CollapseListButton();
	display_panel.add(MyEvents.clb.getButton(), c);

	// collapse clear button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.ccb = new CollapseClearButton();
	display_panel.add(MyEvents.ccb.getButton(), c);

	// help button.
	griddexx += 1; c.gridx = griddexx;
	MyEvents.hb = new HelpButton();
	display_panel.add(MyEvents.hb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// blank button.
	MyEvents.blb = new BlankButton();
	griddexx += 1; c.gridx = griddexx;
	display_panel.add(MyEvents.blb.getButton(), c);

	// quit file button.	
	MyEvents.qfb = new QuitFileButton();
	griddexx += 1; c.gridx = griddexx; 
	display_panel.add(MyEvents.qfb.getButton(), c);

	// quit button.
	MyEvents.qb = new QuitMeButton();
	griddexx += 1; c.gridx = griddexx; 
	display_panel.add(MyEvents.qb.getButton(), c);
	
	c.gridx = 0; c.gridy = 2;
	myFrame.add(display_panel, c);

	// urtext area.
	c.gridx = 0; c.gridy = 3;
	c.gridwidth = GridBagConstraints.REMAINDER;
	MyEvents.urg = new UrTextArea();
	MyEvents.urg.getTextArea().setRows(5);
	myFrame.add(MyEvents.urg.getTextArea(), c);
	// query field.

	if (CorpusDraw.hasQuery()) {
	    c.gridy = 4;
	    MyEvents.qta = new QueryTextArea();
	    MyEvents.qta.getTextArea().setRows(2);
	    MyEvents.qta.put(CorpusDraw.query); 
	    myFrame.add(MyEvents.qta.getTextArea(), c); }

	// graphic tree window.
	treeve = new TreeView();
	c.gridx = 0; c.gridy = 4;
	if (CorpusDraw.hasQuery()) {
	    c.gridy = 5; }
	c.weightx = 1.0; c.weighty = 1.0;
	//	c.gridwidth = GridBagConstraints.REMAINDER;
	//treeve.getScrollPane().setPreferredSize(new Dimension(1000, 1000));
	myFrame.add(treeve.getScrollPane(), c);

	myFrame.pack();
	myFrame.setBackground(ToolColors.TOOL_BACKGROUND);
	myFrame.addWindowListener(this);
	myFrame.setSize(bounds.width, bounds.height);
	myFrame.show();
	}
	catch (Exception e) {
	    e.printStackTrace(); }
    }

    public int getFrameHt() {
	return (myFrame.getHeight()); }

    public int getFrameWdth() {
	return (myFrame.getWidth()); }

    public void removeWindowListener() {
	myFrame.removeWindowListener(this); }

    public void addWindowListener() {
	myFrame.addWindowListener(this); }

    public Rectangle getVBounds() {
	Rectangle virtualBounds = new Rectangle();
	GraphicsEnvironment ge = GraphicsEnvironment.
	    getLocalGraphicsEnvironment();
	GraphicsDevice[] gs = ge.getScreenDevices();
	for (int j = 0; j < gs.length; j++) {
	    GraphicsDevice gd = gs[j];
	    GraphicsConfiguration[] gc = 
		gd.getConfigurations();
	    for (int i = 0; i < gc.length; i++) {
		virtualBounds = virtualBounds.union(gc[i].getBounds()); }
	}
	return virtualBounds; }

    public Frame getFrame() {
	return myFrame; }
    
    public int getHeight() {
	return bounds.height; }

    public int getWidth() {
	return bounds.width; }

    public void windowClosed(WindowEvent e) {
	//DrawLoop.finishFile();
	System.exit(1); }

    public void windowClosing(WindowEvent e) {
	//DrawLoop.finishFile();
	System.exit(1); }

    public void windowOpened(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}

    public void windowGainedFocus(WindowEvent e) {}

    public void windowLostFocus(WindowEvent e) {}

    public void windowStateChanged(WindowEvent e) {}

}

