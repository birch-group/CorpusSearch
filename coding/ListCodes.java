/*
  
 */
package coding;

import java.io.*;
import java.util.*;

public class ListCodes {

    private Vector list;

    public ListCodes () {
        this.list = new Vector();
    }

    public void addCodeObj(CodeObj co) {
	list.addElement(co); }

    public CodeObj getCodeObj(int i) {
	return ((CodeObj)list.elementAt(i)) ; }

    public int size() {
	return list.size(); }
    
    public void PrintToSystemErr() {
	CodeObj co;
	System.err.println("ListCodes:  ");
	for (int i = 0; i < this.size(); i++) {
	    co = this.getCodeObj(i);
	    co.PrintToSystemErr();
	}
    }

}// end class LIstCodes






