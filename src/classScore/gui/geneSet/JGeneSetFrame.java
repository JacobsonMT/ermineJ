package classScore.gui.geneSet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;

import baseCode.bio.geneset.GeneAnnotations;
import baseCode.dataStructure.matrix.DenseDoubleMatrix2DNamed;
import baseCode.gui.ColorMap;
import baseCode.gui.GuiUtil;
import baseCode.gui.JGradientBar;
import baseCode.gui.JLinkLabel;
import baseCode.gui.JMatrixDisplay;
import baseCode.gui.table.JBarGraphCellRenderer;
import baseCode.gui.table.JMatrixCellRenderer;
import baseCode.gui.table.JVerticalHeaderRenderer;
import baseCode.gui.table.TableSorter;
import baseCode.io.reader.DoubleMatrixReader;
import baseCode.util.FileTools;
import classScore.GeneSetPvalRun;
import classScore.Settings;
import classScore.data.GeneSetResult;
import classScore.gui.JHistViewer;

/**
 * <hr>
 * <p>
 * Copyright (c) 2004 Columbia University
 * 
 * @author Kiran Keshav
 * @author Will Braynen
 * @version $Id$
 */
public class JGeneSetFrame extends JFrame {
    private static final int COLOR_RANGE_SLIDER_MIN = 1;
    private static final int COLOR_RANGE_SLIDER_RESOLUTION = 12;

    private static final int DEFAULT_WIDTH_MATRIXDISPLAY_COLUMN = 6;
    private static final String GUI_PREFS = "GUI.prefs";
    private static final String INCLUDEEVERYTHING = "savedata.includeEverything";
    private static final String INCLUDELABELS = "includeImageLabels";
    private static final String MATRIXCOLUMNWIDTH = "ColumnWidth";
    private static final int MAX_WIDTH_MATRIXDISPLAY_COLUMN = 19;
    private static final int MIN_WIDTH_MATRIXDISPLAY_COLUMN = 1;
    private static final int NORMALIZED_COLOR_RANGE_MAX = 12; // [-6,6] standard deviations out
    private static final int PREFERRED_WIDTH_DESCRIPTION_COLUMN = 300;
    private static final int PREFERRED_WIDTH_GENENAME_COLUMN = 75;
    private static final int PREFERRED_WIDTH_PROBEID_COLUMN = 75;
    private static final int PREFERRED_WIDTH_PVALUE_COLUMN = 75;
    private static final int PREFERRED_WIDTH_PVALUEBAR_COLUMN = 75;
    private static final String SAVESTARTPATH = "StartPath";

    private static final String WINDOWHEIGHT = "WindowHeight";
    private static final String WINDOWWIDTH = "WindowWidth";

    public JMatrixDisplay m_matrixDisplay = null;
    private GeneSetPvalRun analysisResults;
    private GeneSetResult classResults;
    private int height;
    private boolean includeEverything = true;

    private boolean includeLabels = true; // whether when saving data we include the row/column labels.
    private int matrixColumnWidth; // how wide the color image columns are.
    private String pref_file;
    private Properties properties;

    private Settings settings;
    private int width;
    protected BorderLayout borderLayout1 = new BorderLayout();
    protected JTable m_table = new JTable();
    protected JScrollPane m_tableScrollPane = new JScrollPane();
    protected JToolBar m_toolbar = new JToolBar();
    JDataFileChooser fileChooser = null;
    JImageFileChooser imageChooser = null;
    JMenu m_analysisMenu = new JMenu();
    JRadioButtonMenuItem m_blackbodyColormapMenuItem = new JRadioButtonMenuItem();
    JLabel m_cellWidthLabel = new JLabel();

    JSlider m_cellWidthSlider = new JSlider();
    JLabel m_colorRangeLabel = new JLabel();
    JSlider m_colorRangeSlider = new JSlider();
    JMenu m_fileMenu = new JMenu();
    JGradientBar m_gradientBar = new JGradientBar();
    JRadioButtonMenuItem m_greenredColormapMenuItem = new JRadioButtonMenuItem();
    /** controls the width of the cells in the matrix display */
    JMenuBar m_menuBar = new JMenuBar();
    DecimalFormat m_nf = new DecimalFormat( "0.##E0" );
    JCheckBoxMenuItem m_normalizeMenuItem = new JCheckBoxMenuItem();
    HashMap m_pvaluesOrdinalPosition = new HashMap();
    JMenuItem m_saveDataMenuItem = new JMenuItem();
    JMenuItem m_saveImageMenuItem = new JMenuItem();
    JLabel m_spacerLabel = new JLabel();
    JMenuItem m_viewHistMenuItem = new JMenuItem();
    JMenu m_viewMenu = new JMenu();

    /**
     * @param res
     * @param run
     * @param probeIDs an array of probe ID's that has some order; the actual order is arbitrary, as long as it is some
     *        order.
     * @param pvalues a map of probeID's to p values.
     * @param geneData holds gene names and descriptions which can be retrieved by probe ID.
     * @param settings <code>getRawFile()</code> should return the microarray file which contains the microarray data
     *        for the probe ID's contained in <code>probeIDs</code>.
     */
    public JGeneSetFrame( List probeIDs, Map pvalues, GeneAnnotations geneData, Settings settings, GeneSetPvalRun run,
            GeneSetResult res ) {
        try {
            String filename = settings.getRawFile();
            this.settings = settings;
            this.analysisResults = run;
            this.classResults = res;

            createDetailsTable( probeIDs, pvalues, geneData, filename );
            initChoosers();

            jbInit();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * @param e
     */
    public void closeWindow_actionPerformed( WindowEvent e ) {

        try {
            writePrefs();
        } catch ( IOException e1 ) {
            e1.printStackTrace();
        }
        this.dispose();
    }

    private void createDetailsTable( List probeIDs, Map pvalues, GeneAnnotations geneData, String filename ) {

        readPrefs();

        //
        // Create a matrix display
        //
        // create a probe set from probeIDs
        HashSet probesInGeneSet = new HashSet();
        for ( int i = 0; i < probeIDs.size(); i++ ) {
            probesInGeneSet.add( probeIDs.get( i ) );
        }

        // Read the matrix data
        DoubleMatrixReader matrixReader = new DoubleMatrixReader();
        DenseDoubleMatrix2DNamed matrix = null;
        // keshav
        if ( filename.length() == 0 ) {
            filename = "dummy";
        }
        if ( !filename.equals( "dummy" ) ) {// keshav(was filename.length() > 0, now filename != dummy)
            try {
                matrix = ( DenseDoubleMatrix2DNamed ) matrixReader.read( filename, probesInGeneSet );
            } catch ( IOException e ) {
                GuiUtil.error( "Unable to load raw microarray data from file " + filename + "\n"
                        + "Please make sure this file exists and the filename and directory path are correct,\n"
                        + "and that it is a valid raw data file (tab-delimited).\n" );
            }
        }
        /*
         * //if ( matrix.rows() == 0 ) { if ( matrix == null ) {//added (use row above) GuiUtil .error( "None of the
         * probes in this gene set were found in your data file." ); }
         */
        // if ( matrix.rows() != probesInGeneSet.size() ) {
        if ( matrix == null ) {// added (use row above)
            System.err.println( "Not all the probes in this gene set were in the data file." );
        }

        // create the matrix display
        if ( matrix != null ) {
            m_matrixDisplay = new JMatrixDisplay( matrix );
            m_matrixDisplay.setStandardizedEnabled( true );
        }

        //
        // Create the rest of the table
        //

        GeneSetTableModel tableModel = new GeneSetTableModel( m_matrixDisplay, probeIDs, pvalues,
                m_pvaluesOrdinalPosition, geneData, m_nf, settings );
        TableSorter sorter = new TableSorter( tableModel, m_matrixDisplay );
        m_table.setModel( sorter );
        sorter.setTableHeader( m_table.getTableHeader() );

        //
        // Set up the matrix display part of the table
        //

        // Make the columns in the matrix display not too wide (cell-size)
        // and set a custom cell renderer
        JMatrixCellRenderer matrixCellRenderer = new JMatrixCellRenderer( m_matrixDisplay ); // create one instance
        // that will be used to
        // draw each cell

        JVerticalHeaderRenderer verticalHeaderRenderer = new JVerticalHeaderRenderer(); // create only one instance

        int matrixColumnCount = ( m_matrixDisplay != null ) ? m_matrixDisplay.getColumnCount() : 0;

        // Set each column width and renderer.
        for ( int i = 0; i < matrixColumnCount; i++ ) {
            TableColumn col = m_table.getColumnModel().getColumn( i );
            col.setMinWidth( MIN_WIDTH_MATRIXDISPLAY_COLUMN );
            col.setMaxWidth( MAX_WIDTH_MATRIXDISPLAY_COLUMN );
            col.setCellRenderer( matrixCellRenderer );
            col.setHeaderRenderer( verticalHeaderRenderer );
        }

        if ( matrix != null ) // keshav added if (but not the guts)
            resizeMatrixColumns( matrixColumnWidth );

        //
        // Set up the rest of the table
        //
        TableColumn col;

        // probe ID
        col = m_table.getColumnModel().getColumn( matrixColumnCount + 0 );
        col.setPreferredWidth( PREFERRED_WIDTH_PROBEID_COLUMN );

        // p value
        col = m_table.getColumnModel().getColumn( matrixColumnCount + 1 );
        col.setPreferredWidth( PREFERRED_WIDTH_PVALUE_COLUMN );

        // p value bar
        col = m_table.getColumnModel().getColumn( matrixColumnCount + 2 );
        col.setPreferredWidth( PREFERRED_WIDTH_PVALUEBAR_COLUMN );
        col.setCellRenderer( new JBarGraphCellRenderer() );// todo

        // name
        col = m_table.getColumnModel().getColumn( matrixColumnCount + 3 );
        col.setPreferredWidth( PREFERRED_WIDTH_GENENAME_COLUMN );

        // description
        col = m_table.getColumnModel().getColumn( matrixColumnCount + 4 );
        col.setPreferredWidth( PREFERRED_WIDTH_DESCRIPTION_COLUMN );

        // Sort initially by the pvalue column
        if ( settings.getBigIsBetter() ) {
            sorter.setSortingStatus( matrixColumnCount + 1, TableSorter.DESCENDING );
        } else {
            sorter.setSortingStatus( matrixColumnCount + 1, TableSorter.ASCENDING );
        }

        // For the pvalue bar graph we need to know the ordinal position of each
        // pvalue in our list of pvalues, and now is the perfect time because
        // the table is sorted by pvalues
        for ( int i = 0; i < m_table.getRowCount(); i++ ) {
            String probeID = ( String ) m_table.getValueAt( i, matrixColumnCount + 0 ); // probeIDs.get( i );
            m_pvaluesOrdinalPosition.put( probeID, new Integer( i ) );
        }

        // Save the dimensions of the table just in case
        int totalWidth = matrixColumnCount * matrixColumnWidth + PREFERRED_WIDTH_PROBEID_COLUMN
                + PREFERRED_WIDTH_PVALUE_COLUMN + PREFERRED_WIDTH_GENENAME_COLUMN + PREFERRED_WIDTH_DESCRIPTION_COLUMN;
        int totalheight = m_table.getPreferredScrollableViewportSize().height;

        Dimension d = new Dimension( totalWidth, totalheight );
        m_table.setSize( d );

    } // end createDetailsTable

    private String getProbeID( int row ) {
        int offset = m_matrixDisplay.getColumnCount(); // matrix display ends
        return ( String ) m_table.getValueAt( row, offset + 0 );
    }

    /**
     * 
     */
    private void initChoosers() {
        if ( m_matrixDisplay == null ) return;

        imageChooser = new JImageFileChooser( this.includeLabels, m_matrixDisplay.getStandardizedEnabled() );
        fileChooser = new JDataFileChooser( this.includeEverything, m_matrixDisplay.getStandardizedEnabled() );
        readPathPrefs();
    }

    private void initColorRangeWidget() {

        // init the slider
        m_colorRangeSlider.setMinimum( COLOR_RANGE_SLIDER_MIN );
        m_colorRangeSlider.setMaximum( COLOR_RANGE_SLIDER_RESOLUTION );

        double rangeMax;
        boolean normalized = m_matrixDisplay.getStandardizedEnabled();
        if ( normalized ) {
            rangeMax = NORMALIZED_COLOR_RANGE_MAX;
        } else {
            rangeMax = m_matrixDisplay.getMax() - m_matrixDisplay.getMin();
        }
        double zoomFactor = COLOR_RANGE_SLIDER_RESOLUTION / rangeMax;
        m_colorRangeSlider.setValue( ( int ) ( m_matrixDisplay.getDisplayRange() * zoomFactor ) );

        // init gradient bar
        double min = m_matrixDisplay.getDisplayMin();
        double max = m_matrixDisplay.getDisplayMax();
        m_gradientBar.setLabels( min, max );
    }

    private void jbInit() throws Exception {

        setSize( width, height );
        setResizable( false );
        setLocation( 200, 100 );
        getContentPane().setLayout( borderLayout1 );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );

        // Listener for window closing events.
        this.addWindowListener( new JGeneSetFrame_windowListenerAdapter( this ) );

        // Enable the horizontal scroll bar
        m_table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // Prevent user from moving tables around
        m_table.getTableHeader().setReorderingAllowed( false );

        // For html links on gene names
        m_table.addMouseListener( new JGeneSetFrame_m_mouseAdapter( this ) );
        m_table.addMouseMotionListener( new JGeneSetFrame_m_mouseMotionListener( this ) );

        // change the cursor to a hand over a header
        m_table.getTableHeader().addMouseListener( new JGeneSetFrameTableHeader_mouseAdapterCursorChanger( this ) );

        // Make sure the matrix display doesn't have a grid separating color cells.
        m_table.setIntercellSpacing( new Dimension( 0, 0 ) );

        // The rest of the table (text and value) should have a light gray grid
        m_table.setGridColor( Color.lightGray );

        // add a viewport with a table inside it
        m_toolbar.setFloatable( false );
        this.setJMenuBar( m_menuBar );

        m_fileMenu.setText( "File" );
        m_greenredColormapMenuItem.setSelected( false );
        m_greenredColormapMenuItem.setText( "Green-Red" );
        m_greenredColormapMenuItem
                .addActionListener( new JGeneSetFrame_m_greenredColormapMenuItem_actionAdapter( this ) );
        m_greenredColormapMenuItem
                .addActionListener( new JGeneSetFrame_m_greenredColormapMenuItem_actionAdapter( this ) );

        m_viewMenu.setText( "View" );
        m_blackbodyColormapMenuItem.setSelected( true );
        m_blackbodyColormapMenuItem.setText( "Blackbody" );
        m_blackbodyColormapMenuItem.addActionListener( new JGeneSetFrame_m_blackbodyColormapMenuItem_actionAdapter(
                this ) );
        m_blackbodyColormapMenuItem.addActionListener( new JGeneSetFrame_m_blackbodyColormapMenuItem_actionAdapter(
                this ) );
        m_saveImageMenuItem.setActionCommand( "SaveImage" );
        m_saveImageMenuItem.setText( "Save Image..." );
        m_saveImageMenuItem.addActionListener( new JGeneSetFrame_m_saveImageMenuItem_actionAdapter( this ) );
        m_normalizeMenuItem.setText( "Normalize" );
        m_normalizeMenuItem.addActionListener( new JGeneSetFrame_m_normalizeMenuItem_actionAdapter( this ) );

        m_analysisMenu.setText( "Analysis" );

        m_viewHistMenuItem.setActionCommand( "View Distribution" );
        m_viewHistMenuItem.setText( "View distribution" );
        m_viewHistMenuItem.addActionListener( new JGeneSetFrame_m_viewHistMenuItem_actionAdapter( this ) );

        m_viewMenu.add( m_normalizeMenuItem );
        m_viewMenu.addSeparator();
        m_viewMenu.add( m_greenredColormapMenuItem );
        m_viewMenu.add( m_blackbodyColormapMenuItem );
        m_fileMenu.add( m_saveImageMenuItem );
        m_fileMenu.add( m_saveDataMenuItem );
        m_analysisMenu.add( m_viewHistMenuItem );

        m_cellWidthSlider.setInverted( false );
        m_cellWidthSlider.setMajorTickSpacing( 0 );
        m_cellWidthSlider.setMaximum( MAX_WIDTH_MATRIXDISPLAY_COLUMN );
        m_cellWidthSlider.setMinimum( MIN_WIDTH_MATRIXDISPLAY_COLUMN );
        m_cellWidthSlider.setValue( matrixColumnWidth );
        m_cellWidthSlider.setMinorTickSpacing( 3 );
        m_cellWidthSlider.setPaintLabels( false );
        m_cellWidthSlider.setPaintTicks( true );
        m_cellWidthSlider.setMaximumSize( new Dimension( 90, 24 ) );
        m_cellWidthSlider.setPreferredSize( new Dimension( 90, 24 ) );
        m_cellWidthSlider.addChangeListener( new JGeneSetFrame_m_cellWidthSlider_changeAdapter( this ) );
        this.setResizable( true );
        m_cellWidthLabel.setText( "Cell Width:" );
        m_spacerLabel.setText( "    " );
        m_colorRangeLabel.setText( "Color Range:" );

        m_gradientBar.setMaximumSize( new Dimension( 200, 30 ) );
        m_gradientBar.setPreferredSize( new Dimension( 120, 30 ) );

        if ( m_matrixDisplay != null ) {
            m_gradientBar.setColorMap( m_matrixDisplay.getColorMap() );
            initColorRangeWidget();
        }

        m_colorRangeSlider.setMaximumSize( new Dimension( 90, 24 ) );
        m_colorRangeSlider.setPreferredSize( new Dimension( 90, 24 ) );
        m_colorRangeSlider.addChangeListener( new JGeneSetFrame_m_colorRangeSlider_changeAdapter( this ) );
        m_saveDataMenuItem.setActionCommand( "SaveData" );
        m_saveDataMenuItem.setText( "Save Data..." );
        m_saveDataMenuItem.addActionListener( new JGeneSetFrame_m_saveDataMenuItem_actionAdapter( this ) );
        m_tableScrollPane.getViewport().add( m_table, null );

        // Reposition the table inside the scrollpane
        int x = m_table.getSize().width; // should probably subtract the size of the viewport, but it gets trimmed
        // anyway,
        // so it's okay to be lazy here
        m_tableScrollPane.getViewport().setViewPosition( new Point( x, 0 ) );

        this.getContentPane().add( m_tableScrollPane, BorderLayout.CENTER );
        this.getContentPane().add( m_toolbar, BorderLayout.NORTH );
        m_toolbar.add( m_cellWidthLabel, null );
        m_toolbar.add( m_cellWidthSlider, null );
        m_toolbar.add( m_spacerLabel, null );
        m_toolbar.add( m_colorRangeLabel, null );
        m_toolbar.add( m_colorRangeSlider, null );
        m_toolbar.add( m_gradientBar, null );

        m_menuBar.add( m_fileMenu );
        m_menuBar.add( m_viewMenu );

        m_menuBar.add( m_analysisMenu );

        if ( analysisResults.getHist() == null ) {
            m_analysisMenu.setEnabled( false );
        }

        // Color map menu items (radio button group -- only one can be selected at one time)
        ButtonGroup group = new ButtonGroup();
        group.add( m_greenredColormapMenuItem );
        group.add( m_blackbodyColormapMenuItem );

        m_cellWidthSlider.setPaintTrack( true );
        m_cellWidthSlider.setPaintTicks( false );

        m_nf.setMaximumFractionDigits( 3 );
        if ( m_matrixDisplay != null ) {
            boolean isNormalized = m_matrixDisplay.getStandardizedEnabled();
            m_normalizeMenuItem.setSelected( isNormalized );
        } else {
            // matrixDisplay is null! Disable the menu
            setDisplayMatrixGUIEnabled( false );
        }
    }

    /**
     * 
     *
     */
    private void readPathPrefs() {
        pref_file = settings.getDataFolder() + System.getProperty( "file.separator" ) + GUI_PREFS;
        try {
            File fi = new File( pref_file );
            if ( fi.canRead() ) {
                InputStream f = new FileInputStream( pref_file );
                properties = new Properties();
                properties.load( f );
                f.close();
                if ( properties.containsKey( SAVESTARTPATH ) && m_matrixDisplay != null ) {

                    if ( fileChooser == null || imageChooser == null ) initChoosers();

                    this.fileChooser.setCurrentDirectory( new File( properties.getProperty( SAVESTARTPATH ) ) );
                    this.imageChooser.setCurrentDirectory( new File( properties.getProperty( SAVESTARTPATH ) ) );
                }
            }
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
    }

    /**
     * 
     *
     */
    private void readPrefs() {
        pref_file = settings.getDataFolder() + System.getProperty( "file.separator" ) + GUI_PREFS;
        width = 800;
        height = m_table.getHeight();
        matrixColumnWidth = DEFAULT_WIDTH_MATRIXDISPLAY_COLUMN;

        properties = new Properties();

        try {
            File fi = new File( pref_file );
            if ( fi.canRead() ) {
                InputStream f = new FileInputStream( pref_file );
                properties.load( f );
                f.close();
                if ( properties.containsKey( WINDOWWIDTH ) )
                    width = Integer.parseInt( properties.getProperty( WINDOWWIDTH ) );

                if ( properties.containsKey( WINDOWHEIGHT ) )
                    height = Integer.parseInt( properties.getProperty( WINDOWHEIGHT ) );

                if ( properties.containsKey( MATRIXCOLUMNWIDTH ) )
                    matrixColumnWidth = Integer.parseInt( properties.getProperty( MATRIXCOLUMNWIDTH ) );

                if ( properties.containsKey( INCLUDELABELS ) )
                    this.includeLabels = Boolean.getBoolean( properties.getProperty( INCLUDELABELS ) );

                if ( properties.containsKey( INCLUDEEVERYTHING ) )
                    this.includeEverything = Boolean.getBoolean( properties.getProperty( INCLUDEEVERYTHING ) );

            }
        } catch ( IOException ex ) {
            ex.printStackTrace();
        }
    }

    private void setDisplayMatrixGUIEnabled( boolean enabled ) {

        // the menu
        m_menuBar.setEnabled( enabled );
        m_fileMenu.setEnabled( enabled );
        m_viewMenu.setEnabled( enabled );
        if ( settings.getClassScoreMethod() == Settings.ORA ) {
            m_analysisMenu.setEnabled( false );
        } else {
            m_analysisMenu.setEnabled( true );
        }

        // the toolbar
        m_toolbar.setEnabled( enabled );

        // the sliders
        m_cellWidthSlider.setEnabled( enabled );
        m_cellWidthLabel.setEnabled( enabled );
        m_colorRangeSlider.setEnabled( enabled );
        m_colorRangeLabel.setEnabled( enabled );
        m_gradientBar.setVisible( enabled );
    }

    /**
     * Writes setting values to file.
     */
    private void writePrefs() throws IOException {

        if ( pref_file == null || pref_file.length() == 0 ) {
            System.err.println( "Can't write prefs, no file name" );
            return;
        }

        if ( properties == null ) {
            properties = new Properties();
        }

        properties.setProperty( WINDOWWIDTH, String.valueOf( this.getWidth() ) );
        properties.setProperty( WINDOWHEIGHT, String.valueOf( this.getHeight() ) );
        properties.setProperty( MATRIXCOLUMNWIDTH, String.valueOf( this.matrixColumnWidth ) );
        properties.setProperty( INCLUDELABELS, String.valueOf( this.includeLabels ) );
        properties.setProperty( INCLUDEEVERYTHING, String.valueOf( this.includeEverything ) );
        if ( imageChooser != null )
            properties.setProperty( SAVESTARTPATH, imageChooser.getCurrentDirectory().getAbsolutePath() );

        OutputStream f = new FileOutputStream( pref_file );
        properties.store( f, "" );
        f.close();
    }

    /**
     * Creates new row keys for the JMatrixDisplay object (m_matrixDisplay). You would probably want to call this method
     * to print out the matrix in the order in which it is displayed in the table. In this case, you will want to do
     * something like this: <br>
     * <br>
     * <code>m_matrixDisplay.setRowKeys( getCurrentMatrixDisplayRowOrder() );</code>
     * <p>
     * However, do not forget to call
     * </p>
     * <code>m_matrixDisplay.resetRowKeys()</code>
     * <p>
     * when you are done because the table sorter filter does its own mapping, so the matrix rows have to remain in
     * their original order (or it might not be displayed correctly inside the table).
     */
    protected int[] getCurrentMatrixDisplayRowOrder() {

        int matrixRowCount = m_matrixDisplay.getRowCount();
        int[] rowKeys = new int[matrixRowCount];

        // write out the table, one row at a time
        for ( int r = 0; r < matrixRowCount; r++ ) {
            // for this row: write out matrix values
            String probeID = getProbeID( r );
            rowKeys[r] = m_matrixDisplay.getRowIndexByName( probeID );
        }

        return rowKeys;

    } // end createRowKeys

    protected void saveData( String filename, boolean includeMatrixValues, boolean includeNonMatrix, boolean normalized )
            throws IOException {

        final String NEWLINE = System.getProperty( "line.separator" );

        BufferedWriter out = new BufferedWriter( new FileWriter( filename ) );

        boolean isStandardized = m_matrixDisplay.getStandardizedEnabled();
        m_matrixDisplay.setStandardizedEnabled( normalized );
        {
            int totalRowCount = m_table.getRowCount();
            int totalColumnCount = m_table.getColumnCount();
            int matrixColumnCount = m_matrixDisplay.getColumnCount();

            // write out column names
            if ( includeMatrixValues ) {
                for ( int c = 0; c < matrixColumnCount; c++ ) {
                    String columnName = m_matrixDisplay.getColumnName( c );
                    out.write( columnName + "\t" );
                }
            }

            if ( includeNonMatrix ) {// FIXME - this is not maintainable!
                out.write( "Probe\tScore\tScore\tSymbol\tName" );
            }

            if ( includeMatrixValues || includeNonMatrix ) out.write( NEWLINE );

            // write out the table, one row at a time
            for ( int r = 0; r < totalRowCount; r++ ) {

                if ( includeMatrixValues ) {

                    // for this row: write out matrix values
                    String probeID = getProbeID( r );
                    double[] row = m_matrixDisplay.getRowByName( probeID );
                    for ( int c = 0; c < row.length; c++ ) {
                        out.write( row[c] + "\t" );
                    }
                    m_matrixDisplay.setStandardizedEnabled( isStandardized ); // return to previous state
                }

                if ( includeNonMatrix ) {
                    // for this row: write out the rest of the table
                    for ( int c = matrixColumnCount; c < totalColumnCount; c++ ) {
                        out.write( stripHtml( m_table.getValueAt( r, c ).toString() ) + "\t" );
                    }
                }
                out.write( NEWLINE );
            }
        }
        m_matrixDisplay.setStandardizedEnabled( isStandardized ); // return to previous state

        // close the file
        out.close();

    } // end saveData

    /**
     * Ridiculously simple to remove anchor tag.
     * 
     * @param s
     * @return
     */
    private String stripHtml( String s ) {
        String m = s.replaceAll( "<.+?>", "" );
        m = m.replaceAll( "</.+?>", "" );
        return m;
    }

    protected void saveImage( String filename, boolean includeLabels, boolean normalized ) throws IOException {

        boolean isStandardized = m_matrixDisplay.getStandardizedEnabled();
        m_matrixDisplay.setStandardizedEnabled( normalized );
        m_matrixDisplay.setRowKeys( getCurrentMatrixDisplayRowOrder() );
        try {
            m_matrixDisplay.saveImage( filename, includeLabels, normalized );
        } catch ( IOException e ) {
            // clean up
            m_matrixDisplay.setStandardizedEnabled( isStandardized ); // return to previous state
            m_matrixDisplay.resetRowKeys();
            throw e;
        }

        // clean up
        m_matrixDisplay.setStandardizedEnabled( isStandardized ); // return to previous state
        m_matrixDisplay.resetRowKeys();

    } // end saveImage

    void m_blackbodyColormapMenuItem_actionPerformed( ActionEvent e ) {

        try {
            Color[] colorMap = ColorMap.BLACKBODY_COLORMAP;
            m_matrixDisplay.setColorMap( colorMap );
            m_gradientBar.setColorMap( colorMap );
            m_table.repaint();
        } catch ( Exception ex ) {
        }

    }

    void m_cellWidthSlider_stateChanged( ChangeEvent e ) {
        JSlider source = ( JSlider ) e.getSource();
        int v = source.getValue();
        resizeMatrixColumns( v );
    }

    void m_colorRangeSlider_stateChanged( ChangeEvent e ) {

        JSlider source = ( JSlider ) e.getSource();
        double value = source.getValue();

        double displayMin, displayMax;
        boolean normalized = m_matrixDisplay.getStandardizedEnabled();
        if ( normalized ) {
            double rangeMax = NORMALIZED_COLOR_RANGE_MAX;
            double zoomFactor = COLOR_RANGE_SLIDER_RESOLUTION / rangeMax;
            double range = value / zoomFactor;
            displayMin = -( range / 2 );
            displayMax = +( range / 2 );
        } else {
            double rangeMax = m_matrixDisplay.getMax() - m_matrixDisplay.getMin();
            double zoomFactor = COLOR_RANGE_SLIDER_RESOLUTION / rangeMax;
            double range = value / zoomFactor;
            double midpoint = m_matrixDisplay.getMax() - ( rangeMax / 2 );
            displayMin = midpoint - ( range / 2 );
            displayMax = midpoint + ( range / 2 );
        }

        m_gradientBar.setLabels( displayMin, displayMax );
        m_matrixDisplay.setDisplayRange( displayMin, displayMax );
        m_table.repaint();
    }

    void m_greenredColormapMenuItem_actionPerformed( ActionEvent e ) {

        try {
            Color[] colorMap = ColorMap.GREENRED_COLORMAP;
            m_matrixDisplay.setColorMap( colorMap );
            m_gradientBar.setColorMap( colorMap );
            m_table.repaint();
        } catch ( Exception ex ) {
        }

    }

    void m_normalizeMenuItem_actionPerformed( ActionEvent e ) {

        boolean normalize = m_normalizeMenuItem.isSelected();
        m_matrixDisplay.setStandardizedEnabled( normalize );

        initColorRangeWidget();
        m_table.repaint();
    }

    void m_saveDataMenuItem_actionPerformed( ActionEvent e ) {
        int returnVal = fileChooser.showSaveDialog( this );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {

            File file = fileChooser.getSelectedFile();
            includeEverything = fileChooser.includeEverything();
            boolean normalize = fileChooser.normalized();

            // Make sure the filename has a data extension
            String filename = file.getPath();
            if ( !FileTools.hasDataExtension( filename ) ) {
                filename = FileTools.addDataExtension( filename );
            }
            // Save the values
            try {
                saveData( filename, true, includeEverything, normalize );
            } catch ( IOException ex ) {
                System.err.println( "IOException error saving data to " + filename );
            }
        }
        // else canceled by user
    }

    void m_saveImageMenuItem_actionPerformed( ActionEvent e ) {

        int returnVal = imageChooser.showSaveDialog( this );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {

            File file = imageChooser.getSelectedFile();
            includeLabels = imageChooser.includeLabels();
            boolean normalize = imageChooser.normalized();

            // Make sure the filename has an image extension
            String filename = file.getPath();
            if ( !FileTools.hasImageExtension( filename ) ) {
                filename = FileTools.addImageExtension( filename );
            }
            // Save the color matrix image
            try {
                saveImage( filename, includeLabels, normalize );
            } catch ( IOException ex ) {
                System.err.println( "IOException error saving png to " + filename );
            }
        }
        // else canceled by user
    }

    /**
     * @param e
     */
    void m_viewHistMenuItem_actionPerformed( ActionEvent e ) {
        if ( analysisResults != null ) {
            JHistViewer f = new JHistViewer( analysisResults.getHist(), classResults.getEffectiveSize(), classResults
                    .getScore() );
            f.setTitle( this.getTitle() + " histogram" );
            f.pack();
            f.show();
        }
    }

    /**
     * Adjust the width of every matrix display column
     * 
     * @param desiredCellWidth
     */
    void resizeMatrixColumns( int desiredCellWidth ) {
        if ( desiredCellWidth >= MIN_WIDTH_MATRIXDISPLAY_COLUMN && desiredCellWidth <= MAX_WIDTH_MATRIXDISPLAY_COLUMN ) {

            m_table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

            int matrixColumnCount = m_matrixDisplay.getColumnCount();
            for ( int i = 0; i < matrixColumnCount; i++ ) {
                TableColumn col = m_table.getColumnModel().getColumn( i );
                col.setResizable( false );
                col.setPreferredWidth( desiredCellWidth );
            }
            this.matrixColumnWidth = desiredCellWidth; // copy value into our parameter.
        }
    }

    void table_mouseExited( MouseEvent e ) {
        setCursor( Cursor.getDefaultCursor() );
    }

    void table_mouseMoved( MouseEvent e ) {
        int i = m_table.rowAtPoint( e.getPoint() );
        int j = m_table.columnAtPoint( e.getPoint() );
        if ( i >= 0 && j == m_table.getColumnCount() - 2 && m_table.getValueAt( i, j ) != null ) {
            setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
        } else
            setCursor( Cursor.getDefaultCursor() );
    }

    void table_mouseReleased( MouseEvent e ) {
        if ( SwingUtilities.isLeftMouseButton( e ) ) {
            int i = m_table.getSelectedRow();
            int j = m_table.getSelectedColumn();
            if ( m_table.getValueAt( i, j ) != null && j == m_table.getColumnCount() - 2 ) {
                JLinkLabel geneLink = ( JLinkLabel ) m_table.getValueAt( i, j );
                geneLink.mouseClicked( e );
            }
        }
    }

} // end class JGeneSetFrame

class JGeneSetFrame_m_blackbodyColormapMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_blackbodyColormapMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_blackbodyColormapMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_m_cellWidthSlider_changeAdapter implements javax.swing.event.ChangeListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_cellWidthSlider_changeAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void stateChanged( ChangeEvent e ) {
        adaptee.m_cellWidthSlider_stateChanged( e );
    }
}

class JGeneSetFrame_m_colorRangeSlider_changeAdapter implements javax.swing.event.ChangeListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_colorRangeSlider_changeAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void stateChanged( ChangeEvent e ) {
        adaptee.m_colorRangeSlider_stateChanged( e );
    }
}

class JGeneSetFrame_m_greenredColormapMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_greenredColormapMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_greenredColormapMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_m_mouseAdapter extends java.awt.event.MouseAdapter {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_mouseAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void mouseEntered( MouseEvent e ) {
        // adaptee.table_mouseEntered(e);
    }

    public void mouseExited( MouseEvent e ) {
        adaptee.table_mouseExited( e );
    }

    public void mouseReleased( MouseEvent e ) {
        adaptee.table_mouseReleased( e );
    }

}

class JGeneSetFrame_m_mouseMotionListener implements java.awt.event.MouseMotionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_mouseMotionListener( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void mouseDragged( MouseEvent e ) {
    }

    public void mouseMoved( MouseEvent e ) {
        adaptee.table_mouseMoved( e );
    }
}

class JGeneSetFrame_m_normalizeMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_normalizeMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_normalizeMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_m_saveDataMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_saveDataMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_saveDataMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_m_saveImageMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_saveImageMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_saveImageMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_m_viewHistMenuItem_actionAdapter implements java.awt.event.ActionListener {
    JGeneSetFrame adaptee;

    JGeneSetFrame_m_viewHistMenuItem_actionAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void actionPerformed( ActionEvent e ) {
        adaptee.m_viewHistMenuItem_actionPerformed( e );
    }
}

class JGeneSetFrame_windowListenerAdapter extends java.awt.event.WindowAdapter {
    JGeneSetFrame adaptee;

    JGeneSetFrame_windowListenerAdapter( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void windowClosing( WindowEvent e ) {
        adaptee.closeWindow_actionPerformed( e );
    }
}

class JGeneSetFrameTableHeader_mouseAdapterCursorChanger extends java.awt.event.MouseAdapter {
    JGeneSetFrame adaptee;

    JGeneSetFrameTableHeader_mouseAdapterCursorChanger( JGeneSetFrame adaptee ) {
        this.adaptee = adaptee;
    }

    public void mouseEntered( MouseEvent e ) {
        adaptee.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    }

    public void mouseExited( MouseEvent e ) {
        adaptee.setCursor( Cursor.getDefaultCursor() );
    }

}
