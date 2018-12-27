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


public class MyEvents {

    public static UrTextArea urg;  public static InputTextField intf;
    public static ErrorMeTextField errtf;
    public static AnnounceTextField anntf; public static NumTextField numtf;
    public static QueryTextArea qta;
    public static HelpButton hb; public static BackButton bb;
    public static NextButton nb; public static GoToButton gtb;
    public static ResetButton rsb;
    public static UndoButton ub; public static RedoButton rb;
    public static DeleteButton db; public static ReplaceLabelButton rlb;
    public static RazeButton rzb;
    public static AddInternalNodeButton ainb; public static CoIndexButton cib;
    public static LeafBeforeButton lbb; public static LeafAfterButton lab;
    public static MoveToButton mtb; public static MergePreviousButton mpb;
    public static TraceBeforeButton tbb; public static TraceAfterButton tab;
    public static MergeFollowingButton mfb; public static SplitButton sb;
    public static SaveButton svb; public static QuitFileButton qfb;    
    public static QuitMeButton qb; public static BlankButton blb, blb2;
    public static ShrinkButton shb; public static SwellButton swb;
    public static ShowOnlyButton sob; public static ShowAllButton sab;
    public static ShowOnlyListButton solb; public static CollapseButton cb;
    public static ExpandButton eb; public static ExpandAllButton eab;
    public static CollapseListButton clb; 
    public static CollapseClearButton ccb;
    public static UrText urt = new UrText();
    private static  boolean next = false, prev = false;

    public static void setUrText(ChangeGraphicTree cgt) {
	urg.ur_put(urt.toString(cgt.getChangeTree())); }
  
    public static void clearTextFields() {
	errtf.clear();
	intf.clear(); }

    public static boolean interrupt() {
	return(interrupt("hope")); }

    public static void warn(String admonish) {
	//System.err.println("will warn: " + admonish);
	errtf.error_put(admonish); }

    public static boolean nextToken() { return next; }

    public static boolean prevToken() {return prev; }

    public static void setNextToken() { next = true; }

    public static void setPrevToken() { prev = true; }
    
    public static void resetPrevToken() { prev = false; }
    
    public static void resetNextToken() { next = false; }

    public static boolean willReset() {
	return (rsb.getPressed()); }

    public static void clearButtons() {
	bb.resetButton();  nb.resetButton();  ub.resetButton();
	rb.resetButton();  db.resetButton();  rlb.resetButton();
	ainb.resetButton(); cib.resetButton();  lbb.resetButton();
	lab.resetButton();  mtb.resetButton();  mpb.resetButton();
	mfb.resetButton();  sb.resetButton();   qfb.resetButton();
	qb.resetButton();   sob.resetButton();  sab.resetButton();
	solb.resetButton(); cb.resetButton();  eb.resetButton();  
	eab.resetButton();  clb.resetButton(); ccb.resetButton();
	tbb.resetButton(); tab.resetButton();
	rsb.resetButton(); rzb.resetButton();
        next = false; prev = false; }

    public static boolean interrupt(String to_interrupt) {
	boolean response;

	if (rsb.getPressed()) {
	    clearButtons(); return true; }
	if (bb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { bb.resetButton(); }
	    return response; } 
	if (nb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { nb.resetButton(); }
	    return response; }
	if (ub.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { ub.resetButton(); }
	    return response; }
	if (rb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { rb.resetButton(); }
	    return response; }
	if (rzb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { rzb.resetButton(); }
	    return response; }
	if (db.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { db.resetButton(); }
	    return response; }
	if (rlb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { rlb.resetButton(); }
	    return response; }
	if (ainb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { ainb.resetButton(); }
	    return response; }
	if (cib.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { cib.resetButton(); }
	    return response; }
	if (lbb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { lbb.resetButton(); }
	    return response; }
	if (lab.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { lab.resetButton(); }
	    return response; }
	if (mtb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { mtb.resetButton(); }
	    return response; }
	if (mpb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { mpb.resetButton(); }
	    return response; }
	if (mfb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { mfb.resetButton(); }
	    return response; }
	if (sb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { sb.resetButton(); }
	    return response; }
	if (qfb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { qfb.resetButton(); }
	    return response; }
	if (qb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { qb.resetButton(); }
	    return response; }
	if (sob.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { sob.resetButton(); }
	    return response; }
	if (sab.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { sab.resetButton(); }
	    return response; }
	if (solb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { solb.resetButton(); }
	    return response; }
    	if (cb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { cb.resetButton(); }
	    return response; }
        if (eb.getPressed()) {
	    response = myAlert(to_interrupt);
	    if (!response) { eb.resetButton(); }
	    return response; }
	if (eab.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { eab.resetButton(); }
	    return response; }
	if (clb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { clb.resetButton(); }
	    return response; }
	if (ccb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { ccb.resetButton(); }
	    return response; }
	if (tbb.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { tbb.resetButton(); }
	    return response; }
	if (tab.getPressed()) { 
	    response = myAlert(to_interrupt);
	    if (!response) { tab.resetButton(); }
	    return response; }
	return false;
    }	


    private static boolean myAlert(String to_interrupt) {
	Dialog myDialog;
	TextField alert_text;
	GridBagConstraints alert_c;
	GridBagLayout alert_gridbag, button_gridbag;
	YesButton yesb;
	NoMeButton nob;
	Button blank = new Button("");
	Panel button_panel;
	boolean hack;

	alert_text = new TextField("  abandon " + to_interrupt + "?"); 
	alert_text.setBackground(ToolColors.HIGHLIGHT1);
	myDialog = new Dialog(CorpusDraw.toole.getFrame(), "Interrupt");
	myDialog.setBackground(ToolColors.HIGHLIGHT1);
	blank.setBackground(ToolColors.HIGHLIGHT1);
	alert_c = new GridBagConstraints();
	alert_gridbag = new GridBagLayout();
	myDialog.setLayout(alert_gridbag);
	alert_c.fill = GridBagConstraints.BOTH;
	alert_c.gridx = 0; alert_c.gridy = 0;
	myDialog.add(alert_text, alert_c);
	button_gridbag = new GridBagLayout();
	button_panel = new Panel(button_gridbag);
	alert_c  = new GridBagConstraints();
	alert_c.gridx = 0; alert_c.gridy = 0;
	button_panel.add(blank, alert_c);
	alert_c.gridx = 0; alert_c.gridy = 1;
	yesb = new YesButton();
	button_panel.add(yesb.getButton(), alert_c);
	alert_c.gridx = 0; alert_c.gridy = 2;
	blank = new Button();
	blank.setBackground(ToolColors.HIGHLIGHT1);
	button_panel.add(blank, alert_c);
	alert_c.gridx =  0; alert_c.gridy = 3;
	nob = new NoMeButton();
	button_panel.add(nob.getButton(), alert_c);
	alert_c.gridx = 0; alert_c.gridy = 1;
	myDialog.add(button_panel, alert_c);
	myDialog.setSize(300, 300);
	myDialog.show();

	while(true) {
	    // the following System.err.print makes the
	    // dialog box work, for some mysterious reason.
	    // Delete at your peril -- necessary hack.
	    System.err.print("");
	    if (nob.getPressed()) {
		nob.resetButton();
		myDialog.hide();
		return false; }
	    if (yesb.getPressed()) {
		yesb.resetButton();
		myDialog.hide();
		return true; } }
    }


}
