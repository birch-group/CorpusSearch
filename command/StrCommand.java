//  Copyright 2010 Beth Randall

/*********************************
This file is part of CorpusSearch.

CORPUSSEARCH is free software: you can redistribute it and/or modify
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

package command;

import java.util.*;
import java.io.*;


import basicinfo.*;

public class StrCommand extends CommandInfo{

    // String command has this form:
    // HTMLQ(subquery1)AND(subquery2)NODEQIP*COMMANDQcommand1COMMANDQcommand2


    private boolean is_HTMLQ, is_STDOUTQ, is_FILEQ;
    private Vector my_commands;
    private String orig_string;

    public StrCommand() {
	is_HTMLQ = false;
	is_STDOUTQ = false;
	is_FILEQ = false;
    }

    public boolean initStrCommand(String maybe) {
	if (maybe.startsWith("HTMLQ")) {
	    orig_string = maybe;
	    HTMLQuery(maybe); 
	    is_HTMLQ = true;
	    return true; }
	if (maybe.startsWith("STDOUTQ")) {
	    orig_string = maybe;
	    STDOUTQuery(maybe);
	    is_STDOUTQ = true;
	    return true; }
	if (maybe.startsWith("FILEQ")) {
	    orig_string = maybe;
	    FILEQuery(maybe);
	    is_FILEQ = true;
	    return true; }
	return false;
    }

    public boolean isFILEQ() {
	return is_FILEQ; }

    public boolean isSTDOUTQ() {
	return is_STDOUTQ; }

    public boolean isHTMLQ() {
	return is_HTMLQ; }

    public void HTMLQuery(String html_q) {
	String clean;

	try {
	    clean = removePrefix("HTMLQ", html_q);
	    clean = getCommands(clean);
	    clean = setNode(clean);
	    clean = clearCrap(clean);
	    CommandInfo.query = clean;
	    CommandInfo.orig_query = clean;
	    CommandInfo.has_command_file = false;
	    BasicCommands.setOutputFormat("HTML"); }
	catch(Exception e) { e.printStackTrace(); }
	finally { return; }}

    public void STDOUTQuery(String stdout_q) {
	String clean;

	try {
	    clean = removePrefix("STDOUTQ", stdout_q);
	    clean = getCommands(clean);
	    clean = setNode(clean);
	    clean = clearCrap(clean);
	    CommandInfo.query = clean;
	    CommandInfo.orig_query = clean;
	    CommandInfo.has_command_file = false;
	    BasicCommands.setOutputFormat("STDOUT"); }
	catch(Exception e) { e.printStackTrace(); }
	finally { return; }}

    public void FILEQuery(String file_q) {
	String clean;

	try {
	    clean = removePrefix("FILEQ", file_q);
	    clean = getCommands(clean);
	    clean = setNode(clean);
	    clean = clearCrap(clean);
	    CommandInfo.query = clean;
	    CommandInfo.orig_query = clean;
	    CommandInfo.has_command_file = false;
	    BasicCommands.setOutputFormat("FILEQ"); }
	catch(Exception e) { e.printStackTrace(); }
	finally { return; }}

    public String removePrefix(String prefix, String crappy) {

	String fixless = crappy.substring(prefix.length());
	return fixless; }


    public String setNode(String line_query) {
	String node_marker = "NODEQ", node_stuff, clean;
	int mark_dex;

	mark_dex = line_query.indexOf(node_marker);
	if (mark_dex < 0) {
	    CommandInfo.node = "$ROOT";
	    return line_query; }
	node_stuff = line_query.substring(mark_dex);
	clean = line_query.substring(0, mark_dex);
	node_stuff = node_stuff.substring(node_marker.length());
	CommandInfo.node = node_stuff;
	return clean; }
	
    public String clearCrap(String crappy) {
	String orig, clean;
	int end_fill, fill_dex = 1;

	clean = crappy;
	clear_crap: while (fill_dex >= 0) {
	    fill_dex = clean.indexOf("(___");
	    if (fill_dex < 0) { break clear_crap; }
	    end_fill = clean.indexOf("___)");
	    end_fill += 4; // get to the end of "___)".
	    if (clean.substring(end_fill).startsWith("AND")) {
		end_fill += 3; }
	    orig = clean;
	    clean = orig.substring(0, fill_dex); 
	    clean += orig.substring(end_fill); }
	// remove trailing AND.
	if (clean.endsWith("AND")) {
	clean = clean.substring(0, clean.lastIndexOf("AND")); }
	//System.out.println("clean:  " + clean);
	return clean; }

    private String getCommands(String in_str) {
	int command_dex = 0;
	String one_command, out_str;

	out_str = in_str;
	my_commands = new Vector();
	command_dex = out_str.lastIndexOf("COMMANDQ");
	while (command_dex >= 0) {
	    one_command = out_str.substring(command_dex);
	    one_command = one_command.substring("COMMANDQ".length());
	    //System.err.println("got one command:  " + one_command);
	    my_commands.addElement(one_command);
	    out_str = out_str.substring(0, command_dex); 
	    command_dex = out_str.lastIndexOf("COMMANDQ"); }
	runCommands();
	return out_str; }

    private void runCommands() {
	int j;

	for (j = 0; j < my_commands.size(); j++) {
	    BasicStrCommands.Store((String)my_commands.elementAt(j)); }
	return; }


} 








