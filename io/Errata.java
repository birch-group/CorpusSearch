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

*/
package io;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;

public class Errata {
    protected PrintWriter out_fox;

    protected void GenericError(String error_message) {
	System.out.println("");
	System.out.println("WARNING!:  " + error_message);
	FileNameAndLine();
	ChunkToSystemErr(); 
        GenericErrorToOutput(out_fox, error_message);}

    public void GenericErrorToOutput(PrintWriter out_fox, 
				     String error_message) {
	if (!IoInfo.getErrorsToOutput()) { return; }
	try {
	    out_fox.println("");
	    out_fox.println("/* WARNING!:  " + error_message);
	    FileNameAndLineToOutput(out_fox);
	    ChunkToOutput(out_fox);
	    out_fox.println("*/");
	    out_fox.println(""); }
	catch (Exception e) {
	    System.out.println("Could not print error message to output."); }
	finally { return; } }

    public void CorpusError() {
	GenericError("Stuck here:  "); }

    public void MissingWrapperError() {
	GenericError("missing wrapper: "); }

    public void MissingSynLabelError() {
	GenericError("missing syntactic label: "); }

    public void DegenerateLeafError(String label) {
	GenericError("Degenerate leaf:  " + label); }

    public void ExtraLabelError(String label) {
        GenericError("extra label: " + label); }

    public void MissingPOSLabelError(String text) {
	GenericError("missing POS label for this text:  " + text); }

    public void EmptyTreeError() {
	GenericError("Empty tree before end of file."); }

    public void FragError() {
	GenericError("Fragment at end of file:  "); }

    public void ExtraTagsError(String label) {
	GenericError("extra tags:  " + label); }

    public void StrayWordError(String label) {
	GenericError("stray word:  " + label); }

    public void FileNameAndLine() {
	System.out.println("");
        System.out.print("    " + (TBToRix.infile).FILE_NAME);
	System.out.println(", line " + (TBToRix.infile).LINE_NUMBER);
        System.out.println(""); }

    public void FileNameAndLineToOutput(PrintWriter out_fox) {
	try {
	    out_fox.println("");
	    out_fox.print("    " + (TBToRix.infile).FILE_NAME); 
	    out_fox.println(", line " + (TBToRix.infile).LINE_NUMBER);
	    out_fox.println(""); }
	catch (Exception e) {
	    System.out.println("Could not print FileNameAndLine to output."); }
	finally { return; } }

    public void ChunkToSystemErr() {
	System.out.println("Last chunk read:  ");
	(TBToRix.infile).ChunkToSystemErr();
	System.out.println(""); }

    public void ChunkToOutput(PrintWriter out_fox) {
	try {
	    out_fox.println("last chunk read: ");
	    out_fox.print((TBToRix.infile).getChunk());
	    out_fox.println(""); }
	catch (Exception e) {
	    System.out.println("Could not print Chunk to output."); }
	finally { return; } }

} 
