package ubic.erminej.data;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import ubic.basecode.util.RegressionTesting;

import junit.framework.TestCase;
import ubic.basecode.bio.geneset.GeneAnnotations;
import ubic.erminej.Settings;
import ubic.erminej.data.GeneScores;

/**
 * <hr>
 * <p>
 * Copyright (c) 2006 University of British Columbia
 * 
 * @author pavlidis
 * @version $Id$
 */
public class GeneScoreReaderTest extends TestCase {
    InputStream is = null;
    InputStream ism = null;
    GeneScores test = null;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        Settings s = new Settings();

        ism = GeneScoreReaderTest.class.getResourceAsStream( "/data/test.an.txt" );

        is = GeneScoreReaderTest.class.getResourceAsStream( "/data/test.scores.txt" );

        GeneAnnotations g = new GeneAnnotations( ism, null, null, null );

        test = new GeneScores( is, s, null, g );
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        ism.close();
        is.close();
    }

    public void testGet_probe_ids() {
        String[] expectedReturn = new String[] { "ProbeA", "ProbeB", "ProbeC", "ProbeD", "ProbeE", "ProbeF", "ProbeG",
                "ProbeH" };

        String[] actualReturn = test.getProbeIds();
        assertTrue( RegressionTesting.containsSame( expectedReturn, actualReturn ) );
    }

    public void testGetPvalues() {
        double[] expectedReturn = new double[] { -Math.log10( 0.01 ), -Math.log10( 0.01 ), -Math.log10( 0.02 ),
                -Math.log10( 0.1 ), -Math.log10( 0.1 ), -Math.log10( 0.1 ), -Math.log10( 0.1 ), -Math.log10( 0.1 ) };
        double[] actualReturn = test.getPvalues();
        assertTrue( RegressionTesting.closeEnough( expectedReturn, actualReturn, 0.00001 ) );
    }

    public void testGet_numpvals() {
        int expectedReturn = 8;
        int actualReturn = test.getNumGeneScores();
        assertEquals( "return value", expectedReturn, actualReturn );
    }

    /*
     * Class under test for Map getGeneToPvalMap()
     */
    public void testGetGeneToPvalMap() {
        Set expectedReturn = new HashSet();
        expectedReturn.add( "GeneA" );
        expectedReturn.add( "GeneB" );
        expectedReturn.add( "GeneC" );
        expectedReturn.add( "GeneD" );
        expectedReturn.add( "GeneE" ); // not really a good test...

        Set actualReturn = test.getGeneToPvalMap().keySet();

        System.err.println( actualReturn );

        assertTrue( RegressionTesting.containsSame( expectedReturn, actualReturn ) );
    }

}