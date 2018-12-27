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
import java.io.*;
import basicinfo.*;
import print.*;
import io.*;

public class CorpusTags {

    protected boolean has_tags;
    protected boolean has_error;
    private String tags_file_name;
    private UberList ustle;

    public CorpusTags() {}

    public CorpusTags(String source_file_name) {
	has_error = false;
	has_tags = getTags(source_file_name); 
	try {
	    if (has_tags) {
		ustle = new UberList();
	        has_error = !ustle.makeUberList(this.tags_file_name);
	        if (has_error) { has_tags = false; } } }
	catch (Exception e) {
	    System.err.print("WARNING! unable to process ");
	    System.err.println(tags_file_name + " as tags file.");
	    has_tags = false;
	    has_error = true; }
	finally { return; } }

    public void setHasErrorTrue() {
	has_error = true; }

    public boolean hasError() {
	return has_error; }

    public boolean getTags(String source_file_name) {
	File tags_dir, source_file;
	String dirname, file_name = "BULLWINKLE";
	
	try {
	    source_file = new File(source_file_name);
	    dirname = source_file.getParent();
	    tags_dir = new File(dirname);
	    if (dirname != null) {
		file_name = searchDir(tags_dir);
		if (file_name.endsWith(".tag")) {
		    this.tags_file_name = file_name;
		    //this.tags_file = new File(tags_file_name);
		    return true; } }
	    tags_dir = new File(System.getProperty("user.dir"));
	    if (dirname != null) {
		file_name = searchDir(tags_dir);
		if (file_name.endsWith(".tag")) {
		    this.tags_file_name = file_name;
		    //this.tags_file = new File(tags_file_name);
		    return true; } }
	}
	catch (Exception e) {
	    System.err.println("WARNING:  could not search for tags file:  ");
	    System.err.print("query file, source file(s), and ");
	    System.err.print("the CorpusDraw program should be stored in ");
	    System.err.println("3 separate directories.");
	    //e.printStackTrace(); 
	    return false; }
	finally { return false; } } 
    
    private String searchDir(File tags_dir) {
	String one_found = "not_found", tags_name;
	String[] dir_list;
	int j;

	dir_list = tags_dir.list();
	for (j = 0; j < dir_list.length; j++) {
	    one_found = dir_list[j];
	    if (one_found.endsWith(".tag")) {
		one_found = tags_dir.separator + one_found;
		one_found = tags_dir.getAbsolutePath() + one_found;
		return one_found; } }
	return one_found; }

    public boolean hasTags() {
	return has_tags; }

    public String getTagsFileName() {
	return tags_file_name; }

    public String getShortFileName() {
	String short_name;
	int last_dex;

	last_dex = tags_file_name.lastIndexOf("/");
	if (last_dex < 0 ) {
	    return tags_file_name; }
	short_name = tags_file_name.substring(last_dex + 1);
	return short_name; }
	

    public boolean legitSynTag(String taggle) {
	return (ustle.legitSynTag(taggle)); }

    public boolean legitPOSTag(String taggle) {
	return (ustle.legitPOSTag(taggle)); }

    public boolean legitCatTag(String taggle) {
	return (ustle.legitCatTag(taggle)); }


    public void PrintToSystemErr() {
	ustle.PrintToSystemErr(); }

}

    






