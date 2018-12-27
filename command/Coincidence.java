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

package command;

import java.io.*;
import java.util.*;

public class Coincidence {

    private boolean coincide;
    private int first;
    private int second;

    public Coincidence () {
	coincide = false;
	first = -1;
	second = -1;
    }

    public Coincidence (boolean coinc, int one, int two) {
	coincide = coinc;
	first = one;
	second = two;
    }

    public boolean getCoincide () {
	return coincide;
    }

    public int getFirst () {
	return first;
    }

    public int getSecond () {
	return second;
    }

    public void PrintToSystemErr() {
	System.err.println(coincide +":  " + first + ", " + second);
	return;
    }

}
