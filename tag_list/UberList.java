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
import basicinfo.*;

public class UberList {

    private UberPOSList upos;
    private UberSynList usyn;
    private UberEmptiesList uempty; 
    private String pos_dividers = "+|-";
    private String syn_dividers = "-|=";
    private String trace_dividers = "-";
    private String empt_str = "0|" + "\\" + "**" ;
    protected Vector syn_divide, pos_divide, trace_divide;
    protected ArgList empties;


    public UberList() {
	upos = new UberPOSList();
	usyn = new UberSynList(); 
	uempty = new UberEmptiesList();
	syn_divide = PipeList.makeCharacterList(syn_dividers);
        pos_divide = PipeList.makeCharacterList(pos_dividers);
	trace_divide = PipeList.makeCharacterList(trace_dividers);
        empties = new ArgList(empt_str); }

    public int sizePOS() {
	return upos.size(); }

    public int sizeSyn() {
	return usyn.size(); }

    public UberPOSList getPOSList() {
	return upos; }

    public UberSynList getSynList() {
	return usyn; }

    public UberEmptiesList getEmptyList() {
	return uempty; }

    public ArgList getEmpties() {
	return empties; }

    public Vector getSynDivide() {
	return syn_divide; }

    public Vector getPOSDivide() {
	return pos_divide; }

    public Vector getTraceDivide() {
	return trace_divide; }

    public String getSynDivideStr() {
	return syn_dividers; }

    public String getPOSDivideStr() {
	return pos_dividers; }

    public String getTraceDivideStr() {
	return trace_dividers; }

    public void addFileList(FileList flist) {
	upos.addFileList(flist.getPOSList());
	usyn.addFileList(flist.getSynList());
        uempty.addFileList(flist.getEmptiesList()); }

    public void Sort() {
	upos.Sort();
	usyn.Sort(); }


    public void PrintToSystemErr() {
	System.err.println("UberList:  ");
	upos.PrintToSystemErr();
	usyn.PrintToSystemErr();
	return; }

    public void PrintToSystemErr(int start, int end) {
	System.err.println("UberList:  ");
	upos.PrintToSystemErr(start, end);
	usyn.PrintToSystemErr(start, end);
	return; }

} 
