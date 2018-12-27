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

/*
	Beth Randall:  July 2001
	Check.java
	contains methods to check whether input files can be 
	read, and whether output files are accessible.
*/
package csearch;

import java.io.*;
import java.util.*;

/**
 * contains methods to check whether input files can be read, and
 * whether output files are accessible.
 */
public class Check extends CorpusSearch {

    /**
     * returns parent directory of input File.
     */
    public static File parent(File f) {
        String dirname = f.getParent();
        if (dirname == null) {
            if (f.isAbsolute()) return new File(File.separator);
            else return new File(System.getProperty("user.dir")); }
        return new File(dirname);
    } 

    /**
     * checks whether query file exists and can be read.
     */
    public static void QueryFile (File command_file) {
        try {
            if (!command_file.exists()
                    || !command_file.isFile()) {
                System.out.println("ERROR! no such file: " + command_name);
                System.out.println("");
		InFace.GetQueryFile();
	        return; }
            if (!command_file.canRead()) {
                System.out.println("ERROR! " + command_name + " is unreadable: ");
                System.out.println("");
                InFace.GetQueryFile(); 
		return; }
        } 
        catch (Exception e) {
            System.out.println("In Check:  QueryFile:  ");
            System.out.println(e.getMessage()); }
        finally { return; }
    } 

} 


