/*
 * The ermineJ project
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
package ubic.erminej.gui.geneset;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ubic.basecode.util.StatusViewer;

import ubic.basecode.gui.StatusJlabel;
import ubic.erminej.Settings;

/**
 * FIXME this should extend jDialog, implement actionlistener, and be modal.
 * 
 * @author pavlidis
 * @version $Id$
 */
public class GeneUrlDialog extends JFrame {
    private static final Log log = LogFactory.getLog( GeneUrlDialog.class );
    private static final int MAINWIDTH = 550;
    private JPanel mainPanel;
    private Dimension dlgSize = new Dimension( MAINWIDTH, 100 );
    private JPanel bottomPanel = new JPanel();
    private JButton cancelButton = new JButton();
    private JButton setButton = new JButton();
    private JPanel centerPanel = new JPanel();
    private JTextField urlTextField;
    private JLabel jLabelStatus = new JLabel();
    private JPanel jPanelStatus = new JPanel();
    private JPanel BottomPanelWrap = new JPanel();
    private StatusViewer statusMessenger;
    private boolean hasOld = false;
    private Settings settings;
    private final GeneSetTableModel tableModel;
    private boolean firstTime = true;

    public GeneUrlDialog( JFrame parent, Settings settings, GeneSetTableModel model ) {
        this.settings = settings;
        this.tableModel = model;
        // this.setModal( true );
        try {
            jbInit();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation( ( screenSize.width - dlgSize.width ) / 2, ( screenSize.height - dlgSize.height ) / 2 );
            pack();
            urlTextField.requestFocusInWindow();
            show();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    private void jbInit() {
        setResizable( true );
        mainPanel = ( JPanel ) this.getContentPane();
        mainPanel.setPreferredSize( new Dimension( MAINWIDTH, 150 ) );
        mainPanel.setLayout( new BorderLayout() );

        centerPanel.setPreferredSize( new Dimension( 200, 50 ) );

        urlTextField = new JTextField();
        initializeFieldText();
        urlTextField.setPreferredSize( new Dimension( 500, 19 ) );
        urlTextField.addMouseListener( new SetTextMouseButton_actionAdapter( this ) );
        centerPanel.add( urlTextField, null );

        bottomPanel.setPreferredSize( new Dimension( 200, 40 ) );

        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new CancelButton_actionAdapter( this ) );

        setButton.setText( "Save" );
        setButton.addActionListener( new SetButton_actionAdapter( this ) );
        bottomPanel.add( setButton, null );

        bottomPanel.add( cancelButton, null );

        // status bar
        jPanelStatus.setBorder( BorderFactory.createEtchedBorder() );
        jLabelStatus.setFont( new java.awt.Font( "Dialog", 0, 11 ) );
        jLabelStatus.setPreferredSize( new Dimension( MAINWIDTH - 40, 19 ) );
        jLabelStatus.setHorizontalAlignment( SwingConstants.LEFT );
        jPanelStatus.add( jLabelStatus, null );
        statusMessenger = new StatusJlabel( jLabelStatus );
        BottomPanelWrap.setLayout( new BorderLayout() );
        BottomPanelWrap.add( bottomPanel, BorderLayout.NORTH );
        BottomPanelWrap.add( jPanelStatus, BorderLayout.SOUTH );

        mainPanel.add( centerPanel, BorderLayout.NORTH );
        mainPanel.add( BottomPanelWrap, BorderLayout.SOUTH );
        this.setTitle( "Set the url used to create links for genes." );

    }

    /**
     * 
     */
    private void initializeFieldText() {
        if ( this.settings == null ) {
            try {
                this.settings = new Settings();
            } catch ( IOException e ) {
                throw new RuntimeException( e );
            }
        }
        if ( this.settings.getConfig().containsKey( Settings.GENE_URL_BASE ) ) {
            String oldUrlBase = this.settings.getConfig().getString( Settings.GENE_URL_BASE );
            log.debug( "Found url base " + oldUrlBase );
            urlTextField.setText( oldUrlBase );
            hasOld = true;
        }
    }

    void cancelButton_actionPerformed() {
        dispose();
    }

    void setActionPerformed() {
        String candidateUrlBase = urlTextField.getText().trim();

        if ( candidateUrlBase.length() == 0 ) {
            statusMessenger.showError( "URL must not be blank." );
            return;
        }

        if ( candidateUrlBase.indexOf( "@@" ) < 0 ) {
            statusMessenger.showError( "URL must contain the string '@@' for substitution with the gene name" );
            return;
        }

        if ( candidateUrlBase.indexOf( " " ) >= 0 ) {
            statusMessenger.showError( "URL should not contain spaces" );
            return;
        }

        settings.getConfig().setProperty( Settings.GENE_URL_BASE, candidateUrlBase );
        tableModel.configure();
        dispose();
    }

    /**
     * 
     */
    public void urlMouseActionAdapter() {
        if ( !hasOld && firstTime ) this.urlTextField.setText( "" );
        firstTime = false;
    }

}

class SetButton_actionAdapter implements java.awt.event.ActionListener {
    GeneUrlDialog adaptee;

    SetButton_actionAdapter( GeneUrlDialog adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.setActionPerformed();
    }
}

class CancelButton_actionAdapter implements java.awt.event.ActionListener {
    GeneUrlDialog adaptee;

    CancelButton_actionAdapter( GeneUrlDialog adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.cancelButton_actionPerformed();
    }
}

class SetTextMouseButton_actionAdapter implements MouseListener {

    GeneUrlDialog adaptee;

    /**
     * @param adaptee
     */
    public SetTextMouseButton_actionAdapter( GeneUrlDialog adaptee ) {
        super();
        this.adaptee = adaptee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked( MouseEvent e ) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered( MouseEvent e ) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited( MouseEvent e ) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed( MouseEvent e ) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased( MouseEvent e ) {
        adaptee.urlMouseActionAdapter();
    }

}