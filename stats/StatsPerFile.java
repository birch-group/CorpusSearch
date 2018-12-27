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
 *Stores statistics for each file.
  <DL><DD>name of file<BR>
  <DD>hits per file<BR>
  <DD>tokens containing hits per file<BR>
  <DD>total number of tokens per file<BR>
*/
public class StatsPerFile {

    private String file_name;
    private String file_ID;
    private int hits;
    private int tokens;
    private int total;

    public StatsPerFile (String full_file_name) {
	this.file_name = full_file_name;
        this.file_ID = this.cleanUp(full_file_name);
        this.hits = 0;
        this.tokens = 0;
        this.total = 0; }

    private String cleanUp (String file_name) {
        String clean_name;

	clean_name = file_name;
	if (clean_name.equals("")) {
	    return clean_name; }
	if (file_name.lastIndexOf('/') > 0) {
	    clean_name =
		file_name.substring(file_name.lastIndexOf('/') + 1); }
        return clean_name; }

    public void hitsAdd (int x) {
        hits += x;
        return; }

    public void tokensAdd1 () {
        tokens += 1;
        return; }

    public void totalAdd1 () {
        total += 1;
        return; }

    public String getFileID () {
        return(file_ID); }

    public int getHits () {
        return(hits); }

    public int getTokens () {
        return(tokens); }

    public int getTotal () {
        return(total); }

    public void PrintToPrintWriter (PrintWriter outer) {
        String file_name;

        outer.println("  source file, hits/tokens/total");
        file_name = this.getFileID();
        outer.print("  " + file_name);
        if (file_name.length() < 6)
	    outer.print("\t");
        if (file_name.length() < 14)
	    outer.print("\t");
        outer.print("\t" + this.getHits() + "/");
        outer.print(this.getTokens() + "/");
        outer.println(this.getTotal());
	return; }

    public void PrintToSysOut () {
        String file_name;

        System.out.println("  source file, hits/tokens/total");
        file_name = this.getFileID();
        System.out.print("  " + file_name);
        if (file_name.length() < 6)
	    System.out.print("\t");
        if (file_name.length() < 14)
	    System.out.print("\t");
        System.out.print("\t" + this.getHits() + "/");
        System.out.print(this.getTokens() + "/");
        System.out.println(this.getTotal());
	return; }

    public void PrintToSystemErr() {

	System.err.println("  source file, hits/tokens/total");
        file_name = this.getFileID();
        System.err.print("  " + file_name);
        if (file_name.length() < 6)
            System.err.print("\t");
        if (file_name.length() < 14)
            System.err.print("\t");
        System.err.print("\t" + this.getHits() + "/");
        System.err.print(this.getTokens() + "/");
        System.err.println(this.getTotal());
        return; }

} 



