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
package ubic.erminej.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ubic.basecode.gui.JLinkLabel;

/**
 * Displays 'about' information for the software.
 * 
 * @author Kiran Keshav
 * @author Paul Pavlidis
 * @version $Id$
 */
public class AboutBox extends JDialog implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 3482331365640848322L;
    private static Log log = LogFactory.getLog( AboutBox.class.getName() );
    /**
     * 
     */
    private static final int TOTAL_HEIGHT = 550;
    /**
     * 
     */
    private static final int PREFERRED_WIDTH = 450;
    private String VERSION = "2.1";
    private final static String COPYRIGHT = "Copyright (c) University of British Columbia";
    private static final String SOFTWARENAME = "ermineJ";

    JPanel mainPanel = new JPanel();
    JPanel centerPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel blurbsPanel = new JPanel();
    JButton button1 = new JButton();
    JLabel labelAuthors = new JLabel();
    JLinkLabel labelHomepage = new JLinkLabel();
    JLabel imageLabel = new JLabel();
    JLabel label1 = new JLabel();
    JLabel versionLabel = new JLabel();
    JLabel copyrightLabel = new JLabel();
    ImageIcon image1;

    JTextPane licensePanel = new JTextPane();

    public AboutBox( Frame parent ) {
        super( parent );
        enableEvents( AWTEvent.WINDOW_EVENT_MASK );
        try {
            jbInit();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        setModal( true );
        setResizable( true );
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = getPreferredSize();
        setLocation( ( screenSize.width - dlgSize.width ) / 2, ( screenSize.height - dlgSize.height ) / 2 );
        pack();
        this.setVisible( true );
    }

    // Component initialization
    private void jbInit() throws Exception {

        this.getContentPane().setBackground( Color.white );
        this.setResizable( false );
        this.setTitle( "About " + SOFTWARENAME );

        getVersion();

        mainPanel.setLayout( new BorderLayout() );
        mainPanel.setBackground( Color.white );
        mainPanel.setPreferredSize( new Dimension( PREFERRED_WIDTH, TOTAL_HEIGHT ) );
        mainPanel.setRequestFocusEnabled( true );
        mainPanel.setVerifyInputWhenFocusTarget( true );

        centerPanel.setBackground( Color.white );

        imageLabel.setDebugGraphicsOptions( 0 );
        imageLabel.setHorizontalAlignment( SwingConstants.CENTER );
        imageLabel.setHorizontalTextPosition( SwingConstants.CENTER );
        imageLabel.setIcon( new ImageIcon( GeneSetScoreFrame.class.getResource( "resources/logo1small.gif" ) ) );
        imageLabel.setIconTextGap( 0 );

        versionLabel.setBackground( Color.white );
        versionLabel.setFont( new java.awt.Font( "Dialog", 1, 11 ) );
        versionLabel.setPreferredSize( new Dimension( PREFERRED_WIDTH, 30 ) );
        versionLabel.setHorizontalAlignment( SwingConstants.CENTER );
        versionLabel.setHorizontalTextPosition( SwingConstants.LEFT );
        versionLabel.setText( "Version " + VERSION );

        copyrightLabel.setPreferredSize( new Dimension( PREFERRED_WIDTH, 30 ) );
        copyrightLabel.setHorizontalAlignment( SwingConstants.CENTER );
        copyrightLabel.setText( COPYRIGHT );

        labelAuthors.setPreferredSize( new Dimension( PREFERRED_WIDTH, 60 ) );
        labelAuthors.setHorizontalAlignment( SwingConstants.CENTER );
        labelAuthors.setHorizontalTextPosition( SwingConstants.CENTER );
        labelAuthors.setText( "By: Paul Pavlidis, Homin Lee, Will Braynen, and Kiran Keshav." );

        labelHomepage.setHorizontalAlignment( SwingConstants.CENTER );
        labelHomepage.setHorizontalTextPosition( SwingConstants.CENTER );
        String homepageURL = "http://microarray.cu-genome.org/ermineJ/";
        labelHomepage.setText( homepageURL );
        // labelHomepage.setURL( homepageURL );
        labelHomepage.setPreferredSize( new Dimension( PREFERRED_WIDTH, 20 ) );

        blurbsPanel.setLayout( new FlowLayout() );
        blurbsPanel.setBackground( Color.white );
        blurbsPanel.setOpaque( true );
        blurbsPanel.setPreferredSize( new Dimension( PREFERRED_WIDTH, 180 ) );
        blurbsPanel.setRequestFocusEnabled( true );
        blurbsPanel.add( versionLabel, null );
        blurbsPanel.add( copyrightLabel, null );
        blurbsPanel.add( labelAuthors, null );
        blurbsPanel.add( labelHomepage, null );

        licensePanel.setBackground( Color.white );
        licensePanel.setAlignmentX( ( float ) 0.5 );
        licensePanel.setPreferredSize( new Dimension( PREFERRED_WIDTH, 200 ) );
        licensePanel.setDisabledTextColor( Color.black );
        licensePanel.setEditable( false );
        licensePanel.setMargin( new Insets( 10, 10, 10, 10 ) );
        licensePanel.setContentType( "text/html" );
        licensePanel
                .setText( "<p>ErmineJ is licensed under the Apache Public License.</p><p>Direct questions about ermineJ to "
                        + "Kelsey Hamer: kelsey@bioinformatics.ubc.ca</p><p>If you use this software for your work, please cite: "
                        // + " Pavlidis, P., "
                        // + "Lewis, D.P., and Noble, W.S. (2002) Exploring gene expression data"
                        // + " with class scores. Proceedings of the Pacific Symposium on Biocomputing"
                        // + " 7. pp 474-485." +
                        + "Lee HK., Braynen W., Keshav K. and Pavlidis P. (2005)"
                        + " ErmineJ: Tool for functional analysis of gene expression data sets. BMC Bioinformatics 6:269"
                        + "</p></html>" );

        centerPanel.add( blurbsPanel, BorderLayout.NORTH );
        centerPanel.add( licensePanel, BorderLayout.CENTER );

        button1.setText( "Ok" );
        button1.addActionListener( this );

        buttonPanel.setBorder( BorderFactory.createEtchedBorder() );
        buttonPanel.add( button1, null );
        mainPanel.add( imageLabel, BorderLayout.NORTH );
        mainPanel.add( centerPanel, BorderLayout.CENTER );
        mainPanel.add( buttonPanel, BorderLayout.SOUTH );

        this.getContentPane().add( mainPanel, BorderLayout.CENTER );

    }

    /**
     * @throws IOException
     */
    private void getVersion() {
        try {
            VERSION = ( new BufferedReader( new InputStreamReader( new BufferedInputStream( this.getClass()
                    .getResourceAsStream( "resources/version" ) ) ) ) ).readLine();
        } catch ( Exception e ) {
            log.error( "Could not determine version number" );
        }
    }

    // Overridden so we can exit when window is closed
    protected void processWindowEvent( WindowEvent e ) {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING ) {
            cancel();
        }
        super.processWindowEvent( e );
    }

    // Close the dialog
    void cancel() {
        dispose();
    }

    // Close the dialog on a button event
    public void actionPerformed( ActionEvent e ) {
        if ( e.getSource() == button1 ) {
            cancel();
        }
    }

}