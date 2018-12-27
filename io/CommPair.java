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

public class CommPair extends CommList {
    private String begin = "BESSIE";
    private String end = "SMITH";

    public CommPair (String beggin, String ennd) {
	this.begin = beggin;
	this.end = ennd; }

    public String getBegin() { return begin; }

    public String getEnd() { return end; }

    public void PrintToSystemErr() {
	System.err.println("begin:  " + this.begin + ", end:  " + this.end); }

}
