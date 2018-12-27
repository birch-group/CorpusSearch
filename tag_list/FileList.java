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

package tag_list;

import java.util.*;
//import syntree.*;
import basicinfo.*;

public class FileList {

    private FilePOSList fpos;
    private FileSynList fsyn; 
    private FileEmptiesList femp;

    public FileList() {
	fpos = new FilePOSList();
	fsyn = new FileSynList();
        femp = new FileEmptiesList(); }

    public void addSentenceList(SentenceList per_sentence) {
	fpos.addSentenceList(per_sentence); 
	fsyn.addSentenceList(per_sentence);
        femp.addSentenceList(per_sentence); }

    public FilePOSList getPOSList() {
	return fpos; }

    public FileSynList getSynList() {
	return fsyn; }

    public FileEmptiesList getEmptiesList() {
	return femp; }

    public boolean isEmpty() {
	if (fpos.isEmpty() && fsyn.isEmpty() && femp.isEmpty()) {
	    return true; }
	return false; }


    public void PrintToSystemErr() {
	fpos.PrintToSystemErr();
	fsyn.PrintToSystemErr(); 
        femp.PrintToSystemErr(); }

    public void PrintToSystemErr(int start, int end) {
	fpos.PrintToSystemErr(start, end);
	fsyn.PrintToSystemErr(start, end); 
        femp.PrintToSystemErr(start, end); }
}
