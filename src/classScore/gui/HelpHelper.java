package classScore.gui;

import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractButton;

import classScore.ResourceAnchor;

/**
 * Makes it easier to add help access wherever we want To use this, you can do the following, for example for a menu
 * item.
 * 
 * <pre>
 * HelpHelper hh = new HelpHelper();
 * hh.initHelp( helpMenuItem );
 * </pre>
 * 
 * <hr>
 * <p>
 * Copyright (c) 2004 Columbia University
 * 
 * @author Paul Pavlidis
 * @version $Id$
 */
public class HelpHelper {

    // JavaHelp
    private HelpBroker m_helpBroker = null;

    /**
     * Initializes JavaHelp by creating HelpSet and HelpBroker objects and attaching an action listener an
     * AbstractButton
     * 
     * @param c an AbstractButton (typically a JButton or JMenuItem) which will respond to help requests.
     * @return true if successful
     */
    public boolean initHelp( AbstractButton c ) {

        // Create HelpSet and HelpBroker objects
        HelpSet hs = getHelpSet( "classScore/main.hs" );
        if ( hs != null ) {
            m_helpBroker = hs.createHelpBroker();
            // Assign help to components
            CSH.setHelpIDString( c, "top" );
            c.addActionListener( new CSH.DisplayHelpFromSource( m_helpBroker ) );
            return true;
        }
        // GuiUtil.error( "Couldn't load help" );
        System.err.println( "Couldn't load help" );
        return false;
    }

    /**
     * Finds the helpset file and creates a HelpSet object.
     * 
     * @param helpsetFilename filename of the *.hs file relative to the classpath
     * @return the help set object created from the file; if the file was not loaded for whatever reason, returns null.
     */
    private HelpSet getHelpSet( String helpsetFilename ) {
        HelpSet hs = null;
        try {
            ClassLoader cl = ResourceAnchor.class.getClassLoader();
            URL hsURL = HelpSet.findHelpSet( cl, helpsetFilename );
            hs = new HelpSet( cl, hsURL );
        } catch ( Exception e ) {
            System.err.println( "HelpSet: " + e.getMessage() );
            System.err.println( "HelpSet: " + helpsetFilename + " not found" );
            e.printStackTrace();
        }
        return hs;
    }

}
