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

import javax.swing.JApplet;
import java.awt.Graphics;

public class HelloWorld extends JApplet {
    public void paint(Graphics g) {
        g.drawRect(0, 0, 
                   getSize().width - 1,
                   getSize().height - 1);
        g.drawString("Hello Wombat!", 5, 15);
    }
}
