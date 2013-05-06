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
package ubic.erminej;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ubic.erminej.SettingsHolder.Method;
import ubic.erminej.analysis.GeneSetPvalRun;
import ubic.erminej.data.Gene;
import ubic.erminej.data.GeneAnnotations;
import ubic.erminej.data.GeneSet;
import ubic.erminej.data.GeneSetResult;
import ubic.erminej.data.GeneSetTerm;

/**
 * @author pavlidis
 * @version $Id$
 */
public class ResultsPrinter {

    /*
     * File format formalities
     */

    protected static final String RUN_NAME_FIELD_PATTERN = "!# runName=";
    private static final String SYMBOL_SEPARATOR = "|";
    protected static final String RUN_INDICATOR = "!#======";

    private static final String RUN_SEPARATOR = RUN_INDICATOR + " End run =======";

    protected static final String END_OF_SETTINGS_SEPARATOR = "#!----";

    private static final String HEADING = "# ErmineJ results file\n# Created "
            + new Date()
            + "\n# If you use this file in your research, please cite:\n# Lee H.K., Braynen W., Keshav K. and Pavlidis P. (2005) \n# ErmineJ: Tool"
            + " for functional analysis of gene expression data sets. BMC Bioinformatics 6:269\n"
            + "# See http://www.chibi.ubc.ca/ermineJ for more information\n"
            + "# Do not edit this file or you may not be able to reload it into ErmineJ. Edit a copy instead.\n";

    private static Log log = LogFactory.getLog( ResultsPrinter.class.getName() );

    /**
     * @param destFile output file name
     * @param run Analysis run to be saved
     * @param goName GO information
     * @param saveAllGeneNames Whether the output should include all the genes
     */
    public static void write( String destFile, GeneSetPvalRun run, boolean saveAllGeneNames ) throws IOException {

        Writer w = getDestination( destFile );
        printHeading( w );
        printOneResultSet( run, w, saveAllGeneNames, true );
        run.setSavedToFile( true );
        w.close();
    }

    private static void printHeading( Writer w ) throws IOException {
        w.write( HEADING );
    }

    /**
     * Used for saving "projects".
     * 
     * @param path Output path (always clobbered)
     * @param masterSettings
     * @param runsToSave
     */
    public static void write( String path, SettingsHolder masterSettings, Collection<GeneSetPvalRun> runsToSave )
            throws IOException {
        Writer w = null;
        try {
            w = getDestination( path );
            printHeading( w );

            if ( runsToSave.isEmpty() ) {
                // only the settings to consider.
                Settings.writeAnalysisSettings( masterSettings, w );
                w.close();
                return;
            }

            for ( GeneSetPvalRun run : runsToSave ) {
                printOneResultSet( run, w, masterSettings.getSaveAllGenesInOutput(), true );
                run.setSavedToFile( true );
            }
        } catch ( IOException e ) {
            throw e;
        } finally {
            if ( w != null ) w.close();
        }

    }

    /**
     * Print the settings and results (but not the heading)
     * 
     * @param sort Sort the results so the best class (by score pvalue) is listed first.
     */
    private static void printOneResultSet( GeneSetPvalRun resultRun, Writer out, boolean saveAllGeneNames, boolean sort )
            throws IOException {

        if ( resultRun == null ) {
            log.warn( "No results to print" );
            return;
        }

        out.write( "# Start output of analysis run: " + resultRun.getName() + "\n" );
        // followed by settings.
        out.write( "# Settings\n" );
        Settings.writeAnalysisSettings( resultRun.getSettings(), out );

        /*
         * Add meta-information about the run. Treat it like a setting.
         */
        out.write( String.format( "multifunctionalityCorrelation=%.4f\n", resultRun.getMultifunctionalityCorrelation() ) );

        if ( resultRun.getSettings().getClassScoreMethod().equals( Method.ORA ) ) {
            out.write( String.format( "multifunctionalityEnrichment=%.4f\n",
                    resultRun.getMultifunctionalityEnrichment() ) );
            out.write( String.format( "multifunctionalityEnrichmentPvalue=%.4g\n",
                    resultRun.getMultifunctionalityEnrichmentPvalue() ) );
            out.write( String.format( "numAboveThreshold=%d\n", resultRun.getNumAboveThreshold() ) );
        }

        out.write( END_OF_SETTINGS_SEPARATOR + "\n" );
        out.write( RUN_NAME_FIELD_PATTERN + resultRun.getName() + "\n" );

        Map<GeneSetTerm, GeneSetResult> results = resultRun.getResults();
        GeneAnnotations geneData = resultRun.getGeneData();

        boolean first = true;
        if ( sort ) {

            List<GeneSetResult> sortedResults = new ArrayList<GeneSetResult>( results.values() );
            Collections.sort( sortedResults );

            for ( GeneSetResult res : sortedResults ) {
                if ( first ) {
                    first = false;
                    res.printHeadings( out, "\tSame as" + "\tGeneMembers" );
                }
                print( out, saveAllGeneNames, geneData, res );
            }
        } else {
            // output them in alphabetical order. This is useful for testing.
            List<GeneSetTerm> c = new ArrayList<GeneSetTerm>( results.keySet() );
            Collections.sort( c );

            for ( GeneSetTerm t : c ) {
                GeneSetResult res = results.get( t );
                if ( first ) {
                    first = false;
                    res.printHeadings( out, "\tSame as" + "\tGenesMembers" );
                }
                print( out, saveAllGeneNames, geneData, res );
            }
        }

        out.write( RUN_SEPARATOR );

    }

    /**
     * @param out
     * @param res
     * @throws IOException
     */
    private static void print( Writer out, boolean saveAllGeneNames, GeneAnnotations geneData, GeneSetResult res )
            throws IOException {
        res.print( out, "\t" + formatRedundantAndSimilar( geneData, res.getGeneSetId() ) + "\t"
                + ( saveAllGeneNames ? formatGeneNames( geneData, res.getGeneSetId() ) : "" ) + "\t" );
    }

    private static Writer getDestination( String destFile ) throws IOException {
        Writer out;
        if ( destFile == null ) {
            log.debug( "Writing results to STDOUT" );
            out = new BufferedWriter( new PrintWriter( System.out ) );
        } else {
            log.info( "Writing results to " + destFile );
            out = new BufferedWriter( new FileWriter( destFile, false ) ); // NOT APPENDING.
        }
        return out;
    }

    /**
     * @param className
     * @return
     */
    private static String formatGeneNames( GeneAnnotations geneData, GeneSetTerm className ) {
        if ( className == null ) return "";
        Collection<Gene> genes = geneData.getGeneSetGenes( className );
        if ( genes == null || genes.size() == 0 ) return "";
        List<Gene> sortedGenes = new ArrayList<Gene>( genes );
        Collections.sort( sortedGenes );
        StringBuffer buf = new StringBuffer();
        for ( Iterator<Gene> iter = sortedGenes.iterator(); iter.hasNext(); ) {
            Gene gene = iter.next();
            buf.append( gene.getSymbol() + SYMBOL_SEPARATOR );
        }
        return buf.toString();
    }

    /**
     * Set up the string the way I want it.
     * 
     * @param classid String
     * @return String
     */
    private static String formatRedundantAndSimilar( GeneAnnotations geneData, GeneSetTerm classid ) {
        Collection<GeneSet> redund = geneData.findGeneSet( classid ).getRedundantGroups();
        String return_value = "";
        if ( redund.isEmpty() ) {
            return return_value;
        }

        for ( GeneSet nextid : redund ) {
            return_value = return_value + nextid.getId() + SYMBOL_SEPARATOR + nextid.getName() + ", ";
        }

        return return_value;

    }

}