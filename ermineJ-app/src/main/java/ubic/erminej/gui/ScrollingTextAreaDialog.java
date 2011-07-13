/*
 * The baseCode project
 * 
 * Copyright (c) 2006 University of British Columbia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ubic.erminej.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author pavlidis
 * @version $Id$
 */
public class ScrollingTextAreaDialog extends JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = -8888590244789533769L;
    private JTextArea ta;

    public void setEditable( boolean b ) {
        ta.setEditable( b );
    }

    public void setCaretPosition( int position ) {
        ta.setCaretPosition( position );
    }

    public ScrollingTextAreaDialog( Frame owner, String title, boolean modal ) {
        super( owner, title, modal );
        jbInit();
        pack();
    }

    /**
     * @param text
     */
    public void setText( String text ) {
        ta.setText( text );
        validate();
    }

    private void jbInit() {
        JPanel pan = new JPanel( new BorderLayout() );
        pan.setPreferredSize( new Dimension( 650, 500 ) );
        this.getContentPane().add( pan );
        ta = new JTextArea( 60, 80 );
        ta.setBackground( Color.WHITE );

        Font oldFont = ta.getFont();
        Font newFont = new Font( oldFont.getFontName(), oldFont.getStyle(), 10 /* points */);
        ta.setFont( newFont );
        JScrollPane sp = new JScrollPane( ta );
        pan.add( sp, BorderLayout.CENTER );

        pan.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        pack();
    }
}
