package ubic.basecode.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import ubic.basecode.graphics.ColorMap;
import ubic.basecode.graphics.MatrixDisplay;

/**
 * This is an example of how you'd display a microarray.
 * 
 * @author Will Braynen
 * @version $Id$
 */
public class MatrixDisplayApp {
    // Main method: args[0] can contain the name of the data file
    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if ( args.length > 1 ) {
            new MatrixDisplayApp( args[0], args[1] );
        } else {
            System.err.println( "Please specify dataFilename and outPngFilename by passing them as program arguments" );
        }
    }

    boolean packFrame = false;

    // Construct the application
    public MatrixDisplayApp( String inDataFilename, String outPngFilename ) {

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout( new BorderLayout() );
        frame.setSize( new Dimension( 600, 550 ) );
        frame.setTitle( "Eisen Plot" );

        //
        // Here is an example of how you'd display a matrix of doubles
        // visually with colors
        //
        MatrixDisplay matrixDisplay = null;
        try {
            matrixDisplay = new MatrixDisplay( inDataFilename );
        } catch ( java.io.IOException e ) {
            System.err.println( "Unable to open file " + inDataFilename );
            return;
        }

        matrixDisplay.setLabelsVisible( true );

        try {
            boolean showLabels = true;

            matrixDisplay.saveImage( outPngFilename, showLabels );
        } catch ( java.io.IOException e ) {
            System.err.println( "Unable to save screenshot to file " + outPngFilename );
            return;
        }

        frame.getContentPane().add( matrixDisplay, BorderLayout.CENTER );

        // Validate frames that have preset sizes
        // Pack frames that have useful preferred size info, e.g. from their
        // layout
        if ( packFrame ) {
            frame.pack();
        } else {
            frame.validate();
        }
        // Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if ( frameSize.height > screenSize.height ) {
            frameSize.height = screenSize.height;
        }
        if ( frameSize.width > screenSize.width ) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation( ( screenSize.width - frameSize.width ) / 2, ( screenSize.height - frameSize.height ) / 2 );
        frame.setVisible( true );

        matrixDisplay.setStandardizedEnabled( true );

        // use the green-red color map
        try {
            matrixDisplay.setColorMap( ColorMap.GREENRED_COLORMAP );
        } catch ( IllegalArgumentException e ) {
            e.printStackTrace();
        }

        matrixDisplay.setStandardizedEnabled( false );

        // for ( int i = 0; i < 5; i++ ) {
        // try {
        // Thread.sleep( 1000 );
        // isShowingStandardized = !isShowingStandardized;
        // matrixDisplay.setStandardizedEnabled( isShowingStandardized );
        // matrixDisplay.repaint();
        // } catch ( InterruptedException e ) {
        // }
        // }
    }
}