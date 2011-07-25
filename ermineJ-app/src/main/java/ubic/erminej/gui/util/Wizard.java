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
package ubic.erminej.gui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ubic.basecode.util.StatusViewer;
import ubic.erminej.gui.StatusJlabel;

/**
 * Simple "wizard" implementation. To use, call the "addStep" method with a new WizardStep as an argument. Actions must
 * be defined for the "back", "cancel", "finish" and "next" buttons.
 * 
 * @author pavlidis
 * @author Homin Lee
 * @version $Id$
 */
public abstract class Wizard extends JDialog {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected JPanel mainPanel;
    protected JPanel BottomPanel = new JPanel();
    protected JPanel BottomPanelWrap = new JPanel();
    protected JLabel jLabelStatus = new JLabel();
    protected JPanel jPanelStatus = new JPanel();

    protected JButton nextButton = new JButton();
    protected JButton backButton = new JButton();
    protected JButton cancelButton = new JButton();
    protected JButton finishButton = new JButton();

    protected JFrame callingframe;
    List<WizardStep> steps = new Vector<WizardStep>();
    private StatusViewer statusMessenger;

    public Wizard( JFrame callingframe, int width, int height ) {
        this.callingframe = callingframe;
        setModal( true );
        jbInit( width, height );
    }

    /**
     * Make the status bar empty.
     */
    public void clearStatus() {
        statusMessenger.clear();
    }

    /**
     * Disable the "finish" button, indicating the user has some steps to do yet.
     */
    public void setFinishDisabled() {
        this.finishButton.setEnabled( false );
    }

    /**
     * Enable the "finish" button, indicating the user can get out of the wizard at this stage.
     */
    public void setFinishEnabled() {
        this.finishButton.setEnabled( true );
    }

    /**
     * Print an error message to the status bar.
     * 
     * @param a
     */
    public void showError( String a ) {
        statusMessenger.showError( a );
    }

    /**
     * Print a message to the status bar.
     * 
     * @param a
     */
    public void showStatus( String a ) {
        statusMessenger.showStatus( a );
    }

    public void showWizard() {
        Dimension dlgSize = getPreferredSize();
        Dimension frmSize = callingframe.getSize();
        Point loc = callingframe.getLocation();
        setLocation( ( frmSize.width - dlgSize.width ) / 2 + loc.x, ( frmSize.height - dlgSize.height ) / 2 + loc.y );
        pack();
        nextButton.requestFocusInWindow();
        this.setVisible( true );
    }

    protected void addStep( WizardStep panel ) {
        steps.add( panel );
    }

    protected void addStep( WizardStep panel, boolean first ) {
        this.addStep( panel );
        if ( first ) mainPanel.add( steps.get( 0 ), BorderLayout.CENTER );
    }

    /**
     * Define what happens when the 'back' button is pressed
     * 
     * @param e
     */
    protected abstract void backButton_actionPerformed( ActionEvent e );

    /**
     * Define what happens when the 'cancel' button is pressed.
     * 
     * @param e
     */
    protected abstract void cancelButton_actionPerformed( ActionEvent e );

    /**
     * Define what happens when the 'finish' button is pressed.
     * 
     * @param e
     */
    protected abstract void finishEditing( ActionEvent e );

    /**
     * Define what happens when the 'next' button is pressed
     * 
     * @param e
     */
    protected abstract void nextButton_actionPerformed( ActionEvent e );

    // Component initialization
    private void jbInit( int width, int height ) {
        setResizable( true );
        mainPanel = ( JPanel ) this.getContentPane();
        mainPanel.setPreferredSize( new Dimension( width, height ) );
        mainPanel.setLayout( new BorderLayout() );

        // holds the buttons and the status bar.
        BottomPanelWrap.setLayout( new BorderLayout() );

        // bottom buttons/////////////////////////////////////////////////////////
        BottomPanel.setPreferredSize( new Dimension( width, 40 ) );
        nextButton.setText( "Next >" );
        nextButton.addActionListener( new Wizard_nextButton_actionAdapter( this ) );
        nextButton.setMnemonic( 'n' );
        backButton.setText( "< Back" );
        backButton.addActionListener( new Wizard_backButton_actionAdapter( this ) );
        backButton.setEnabled( false );
        backButton.setMnemonic( 'b' );
        cancelButton.setText( "Cancel" );
        cancelButton.addActionListener( new Wizard_cancelButton_actionAdapter( this ) );
        cancelButton.setMnemonic( 'c' );
        finishButton.setText( "Finish" );
        finishButton.setMnemonic( 'f' );
        finishButton.addActionListener( new Wizard_finishButton_actionAdapter( this ) );
        BottomPanel.add( cancelButton, null );
        BottomPanel.add( backButton, null );
        BottomPanel.add( nextButton, null );
        BottomPanel.add( finishButton, null );

        // status bar
        jPanelStatus.setBorder( BorderFactory.createEtchedBorder() );
        jPanelStatus.setLayout( new BorderLayout() );
        jLabelStatus.setFont( new java.awt.Font( "Dialog", 0, 11 ) );
        jLabelStatus.setPreferredSize( new Dimension( width - 40, 19 ) );
        jLabelStatus.setHorizontalAlignment( SwingConstants.LEFT );
        jPanelStatus.add( jLabelStatus, BorderLayout.WEST );
        statusMessenger = new StatusJlabel( jLabelStatus );

        BottomPanelWrap.add( BottomPanel, BorderLayout.NORTH );
        BottomPanelWrap.add( jPanelStatus, BorderLayout.SOUTH );

        mainPanel.add( BottomPanelWrap, BorderLayout.SOUTH );

    }
}

class Wizard_backButton_actionAdapter implements java.awt.event.ActionListener {
    Wizard adaptee;

    Wizard_backButton_actionAdapter( Wizard adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.backButton_actionPerformed( e );
    }
}

class Wizard_cancelButton_actionAdapter implements java.awt.event.ActionListener {
    Wizard adaptee;

    Wizard_cancelButton_actionAdapter( Wizard adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.cancelButton_actionPerformed( e );
    }
}

class Wizard_finishButton_actionAdapter implements java.awt.event.ActionListener {
    Wizard adaptee;

    Wizard_finishButton_actionAdapter( Wizard adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.finishEditing( e );
    }
}

class Wizard_nextButton_actionAdapter implements java.awt.event.ActionListener {
    Wizard adaptee;

    Wizard_nextButton_actionAdapter( Wizard adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.nextButton_actionPerformed( e );
    }
}
