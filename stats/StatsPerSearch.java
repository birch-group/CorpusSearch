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

package stats;

import java.util.*;
import java.io.*;

/**
 * Stores statistics for each search.
   <DL><DD>hits per search<BR>
   <DD>tokens containing hits per search<BR>
   <DD>total tokens searched<BR>
*/
public class StatsPerSearch {

    private int hits;
    private int tokens;
    private int total;
    private String command_name;  // name of query file
    private String dest_name;  // name of output file.
    private Vector file_stats;

    public StatsPerSearch () {
	file_stats = new Vector();
        hits = 0; tokens = 0; total = 0; }

    public StatsPerSearch(String command_name, String dest_name) {
	file_stats = new Vector();
	hits = 0; tokens = 0; total = 0;
	this.setCommandName(command_name);
	this.setDestName(dest_name); }

    public void file_statsAdd (StatsPerFile stato) {
	this.hits += stato.getHits();
	this.tokens += stato.getTokens();
	this.total += stato.getTotal();
	file_stats.addElement(stato);
	return; }

    public StatsPerFile getStatsPerFile (int x) {
	return((StatsPerFile)(this.file_stats).elementAt(x)); }

    public int size () {
	return(file_stats.size()); }

    public void hitsAdd (int x) {
        hits += x; }

    public void tokensAdd1 () {
        tokens += 1; }

    public void totalAdd1 () {
        total += 1; }

    public int getHits () {
        return(hits); }

    public int getTokens () {
        return(tokens); }

    public int getTotal () {
        return(total); }

    public void setCommandName(String name) {
        command_name = name; }

    public void setDestName (String name) {
        dest_name = name; }

    public String getCommandName() {
        return(command_name); }

    public String getDestName() {
        return(dest_name); }

    public void PrintToPrintWriter (PrintWriter outer) {
        int i;
        String file_name, tabs_before_stats = "\t";
	StatsPerFile file_info;

        outer.println("source files, hits/tokens/total");
	for (i = 0; i < this.size(); i++) {
	    tabs_before_stats = "\t";
	    file_info = this.getStatsPerFile(i);
	    file_name = file_info.getFileID();
	    outer.print("  " + file_name);
	    if (file_name.length() < 6) {
		tabs_before_stats += "\t"; }
	    if (file_name.length() < 14) {
		tabs_before_stats += "\t"; }
	    outer.print(tabs_before_stats);
	    outer.print(file_info.getHits() + "/");
	    outer.print(file_info.getTokens() + "/");
	    outer.println(file_info.getTotal()); }
	outer.println("whole search, hits/tokens/total");
	outer.print(tabs_before_stats);
	outer.print("\t" + this.getHits() + "/");
	outer.print(this.getTokens() + "/");
	outer.println(this.getTotal());
        return; }

    public void PrintToSysOut () {
        int i;
        String file_name, tabs_before_stats = "\t";
	StatsPerFile file_info;

        System.out.println("source files, hits/tokens/total");
	for (i = 0; i < this.size(); i++) {
	    tabs_before_stats = "\t";
	    file_info = this.getStatsPerFile(i);
	    file_name = file_info.getFileID();
	    System.out.print("  " + file_name);
	    if (file_name.length() < 6) {
		tabs_before_stats += "\t"; }
	    if (file_name.length() < 14) {
		tabs_before_stats += "\t"; }
	    System.out.print(tabs_before_stats);
	    System.out.print(file_info.getHits() + "/");
	    System.out.print(file_info.getTokens() + "/");
	    System.out.println(file_info.getTotal()); }
	System.out.println("whole search, hits/tokens/total");
	System.out.print(tabs_before_stats);
	System.out.print("\t" + this.getHits() + "/");
	System.out.print(this.getTokens() + "/");
	System.out.println(this.getTotal());
        return; }

} 




