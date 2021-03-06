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

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ubic.erminej.analysis.GeneSetPvalRun;
import ubic.erminej.data.GeneAnnotations;
import ubic.erminej.data.GeneScores;

/**
 * Simple API to run ermineJ analyses, using Strings as the initial representations.
 *
 * @author Paul Pavlidis
 * @version $Id$
 */
public class ClassScoreSimple {

    // List of genes corresponding to the elements. Indicates the Many-to-one mapping of
    // elements to
    // genes.
    private List<String> genes = null;

    // List of Collections of go terms for the elements.
    private List<Collection<String>> goAssociations = null;

    // List of identifiers to be analyzed
    private List<String> elements = null;

    private GeneSetPvalRun results;

    private Settings settings = null;

    /**
     * Note that these Lists must all be in the same order with respect to the elements.
     *
     * @param elements List of identifiers to be analyzed
     * @param genes List of genes corresponding to the elements. Indicates the Many-to-one mapping of elements to genes.
     * @param goAssociations List of Collections of go terms for the elements.
     */
    public ClassScoreSimple( List<String> elements, List<String> genes, List<Collection<String>> goAssociations ) {
        this.elements = elements;
        this.genes = genes;
        this.goAssociations = goAssociations;

        try {
            settings = new Settings( false );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        settings.setQuantile( 50 );
        settings.setMtc( SettingsHolder.MultiTestCorrMethod.FDR );

    }

    /**
     * Gene the resulting gene set pvalue for a given id.
     *
     * @param id The id of the gene set, e.g,. GO:0000232
     * @return -1 if the id is not in the results. Otherwise, the pvalue for the gene set.
     */
    public double getGeneSetPvalue( String id ) {
        if ( results == null ) throw new IllegalStateException( "You must call 'run' before you can get results" );

        if ( !results.getResults().containsKey( id ) ) return -1;

        return results.getResults().get( id ).getPvalue();
    }

    /**
     * Run an analysis using the current configuration.
     *
     * @param geneScores a {@link java.util.List} object.
     */
    public void run( List<Double> geneScores ) {
        GeneAnnotations geneData = new GeneAnnotations( elements, genes, goAssociations );
        GeneScores scores = new GeneScores( elements, geneScores, geneData, settings );
        results = new GeneSetPvalRun( settings, scores );
    }

    /**
     * Indicate that in the original gene scores, whether big values are better. If your inputs are p-values this should
     * be set to false. If you are using fold-changes, set to true.
     *
     * @param b a boolean.
     */
    public void setBigGeneScoreIsBetter( boolean b ) {
        this.settings.setBigIsBetter( b );
    }

    /**
     * Set the type of anlaysis to run. ORA is over-representation analysis.
     *
     * @param val either ClassScoreSimple.ORA or ClassScoreSimple.RESAMPLING or ROC
     */
    public void setClassScoreMethod( int val ) {
        switch ( val ) {
            case 0:
                settings.setClassScoreMethod( SettingsHolder.Method.ORA ); // could be precision-recall.
                break;
            case 1:
                settings.setClassScoreMethod( SettingsHolder.Method.GSR );
                break;
            case 2:
                settings.setClassScoreMethod( SettingsHolder.Method.CORR );
                break;
            case 3:
                settings.setClassScoreMethod( SettingsHolder.Method.ROC );
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * How to handle situations when more than one probe corresponds to the same gene.
     *
     * @param val either BEST_GENE_SCORE or MEAN_GENE_SCORE
     */
    public void setGeneReplicateTreatment( int val ) {
        switch ( val ) {
            case 1:
                settings.setGeneRepTreatment( SettingsHolder.MultiElementHandling.BEST );

                break;
            case 2:
                settings.setGeneRepTreatment( SettingsHolder.MultiElementHandling.MEAN );

                break;
            default:
                throw new IllegalArgumentException();
        }

    }

    /**
     * Set the method to be used to summarize gene sets during resampling analysis. This is ignored otherwise.
     *
     * @param val Either GeneScoreMethod.MEAN, MEDIAN, MEAN_ABOVE_QUANTILE or PRECISIONRECALL
     */
    public void setGeneScoreSummaryMethod( int val ) {
        switch ( val ) {
            case 0:
                settings.setGeneSetResamplingScoreMethod( SettingsHolder.GeneScoreMethod.MEAN );
                break;
            case 1:
                settings.setGeneSetResamplingScoreMethod( SettingsHolder.GeneScoreMethod.QUANTILE );
                break;
            case 2:
                settings.setGeneSetResamplingScoreMethod( SettingsHolder.GeneScoreMethod.MEAN_ABOVE_QUANTILE );
                break;
            case 3:
                settings.setGeneSetResamplingScoreMethod( SettingsHolder.GeneScoreMethod.PRECISIONRECALL );
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Set the threshold to use for ORA analysis. This is ignored otherwise.
     *
     * @param val a double.
     */
    public void setGeneScoreThreshold( double val ) {
        this.settings.setGeneScoreThreshold( val );
    }

    /**
     * The number of iterations to be used during resampling. This is ignored for ORA analysis.
     *
     * @param val a int.
     */
    public void setIterations( int val ) {
        if ( val < 1 ) throw new IllegalArgumentException( "Value must be positive" );
        this.settings.setIterations( val );
    }

    /**
     * Set to true if your inputs are p-values.
     *
     * @param val If true, your gene scores will be transformed by -log base 10.
     */
    public void setLogTransformGeneScores( boolean val ) {
        this.settings.setDoLog( val );
    }

    /**
     * The maximum gene set size to be considered.
     *
     * @param val a int.
     */
    public void setMaxGeneSetSize( int val ) {
        if ( val < 1 ) throw new IllegalArgumentException( "Value must be positive" );
        this.settings.setMaxClassSize( val );
    }

    /**
     * The minimum gene set size to be considered.
     *
     * @param val a int.
     */
    public void setMinGeneSetSize( int val ) {
        if ( val < 1 ) throw new IllegalArgumentException( "Value must be positive" );
        this.settings.setMinClassSize( val );
    }

}
