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

//package drawtree;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;
import java.applet.Applet;
import basicinfo.*;
import java.util.*;
//import drawtree.*;
import csearch.*;

public class CSApplet extends Applet
		 implements ActionListener {

   JLabel text;
   JButton button;
   JPanel panel;
   private boolean _clickMeMode = true;

   public void init(){
     setLayout(new BorderLayout(1, 2));
     setBackground(Color.white);
     
     //text = new JLabel("I'm a little wolverine");     
     //button = new JButton("Click Me");
     //button.addActionListener(this);
     add("Center", text);
     //add("South", button);
     //CorpusDraw.tree_buff = new TreeBuffer();
     //CorpusDraw.source_list = new Vector();
     //CorpusDraw.has_query = false;
     //CorpusDraw.show_only = false;
     //CorpusDraw.copy_corpus = false;
     //CorpusDraw.max_ht = 0; CorpusDraw.max_wdth = 0;  
     //CorpusDraw.source_list.addElement("corpus_draw/cmmalory.m4.psd"); 
     //CorpusDraw.toole = new ToolView();
     //DrawMeat.CrankThrough(CorpusDraw.source_list, CorpusDraw.toole); 
     
   }

  public void start(){
     System.out.println("Applet starting.");
  }

  public void stop(){
     System.out.println("Applet stopping.");
  }

  public void destroy(){
     System.out.println("Destroy method called.");
  }

   public void actionPerformed(ActionEvent event){
        Object source = event.getSource();
        if (_clickMeMode) {
          text.setText("Button Clicked");
          button.setText("Click Again");
          _clickMeMode = false;
        } else {
          text.setText("I'm a Simple Program");
          button.setText("Click Me");
          _clickMeMode = true;
        }
   }
}
