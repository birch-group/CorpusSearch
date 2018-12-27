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


import java.util.*;
import java.io.*;

public class IoInfo {
    public static CommList commie;
    public static LineCommList linie;
    protected static String corpus_encoding = "UTF-8";
    public static String default_encoding = "UTF-8";
    protected static boolean errors_to_output = false;
    protected static String input_format = "PTB";

    public static String getInputFormat() {
	return(input_format); }

    public static void setInputFormat(String format) {
	input_format = format; }

    public static void newComments() {
	commie = new CommList();
	linie = new LineCommList(); }

    public static String getCorpusEncoding() {
	return corpus_encoding; }

    public static void setCorpusEncoding (String codpiece) {
	corpus_encoding = codpiece; 
        if (corpus_encoding.equals("ISO-LATIN-1")) {
	    corpus_encoding = "ISO-8859-1"; } }

    public static void setCorpusEncoding() { // set to default.
	corpus_encoding = default_encoding; }

    public static void setErrorsToOutput(boolean urk) {
	errors_to_output = urk; }

    public static boolean getErrorsToOutput() {
	return errors_to_output; }

}
