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
package revise;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class TaskList {

    private Vector tasks;
    private Vector query_curlies;

    public TaskList() {
	query_curlies = new Vector();
	tasks = new Vector(); } 

    // input curlies vector: [ , , ,{1}, , ,{2}]
    // query_curlies: [-1, -1, -1, 1, -1, -1, 2]
    public void SetCurlers(Vector curlies) {
        Integer dex, null_Int = new Integer(-1);
        String curler;

        for (int i = 0; i < curlies.size(); i++) {
            curler = (String)curlies.elementAt(i);
            if (curler.startsWith("{")) {
                dex = new Integer(curler.substring(1, curler.length()-1));
                query_curlies.addElement(dex); }
            else {
                query_curlies.addElement(null_Int); } }
	this.InstallCurlers();
    }

    public void InstallCurlers() {
	OneTask one;

	for (int i = 0; i < this.size(); i++) {
	    one = this.taskAt(i);
	    one.InstallQueryDexes(query_curlies); }
	return; }

    public void addTask(OneTask one) {
	tasks.addElement(one); }

    public int size() {
	return tasks.size(); }

    public OneTask taskAt(int i) {
	return ((OneTask)tasks.elementAt(i)); }

    public void PrintToSystemErr() {
	OneTask one;
	int i;

	System.err.println("TaskList:  ");
	for (i = 0; i < this.size(); i++) {
	    one = this.taskAt(i);
	    System.err.println(i + ".)  ");
	    one.PrintToSystemErr(); }
	//	System.err.println("CurlyList:  ");
	//for (i = 0; i < query_curlies.size(); i++) {
	//  System.err.print(i + ".)  ");
	//  System.err.println((Integer)query_curlies.elementAt(i)); }
	System.err.println("");
    } // end method PrintToSystemErr()

    public static void main (String[] args) {
	return;
    }

} // end class TaskList.java
