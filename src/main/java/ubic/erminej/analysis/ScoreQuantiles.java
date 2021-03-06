/*
 * The ermineJ project
 *
 * Copyright (c) 2011 University of British Columbia
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ubic.erminej.analysis;

import org.apache.commons.lang3.ArrayUtils;

import cern.colt.list.DoubleArrayList;
import hep.aida.bin.QuantileBin1D;
import ubic.erminej.SettingsHolder;
import ubic.erminej.data.GeneScores;

/**
 * <p>
 * ScoreQuantiles class.
 * </p>
 *
 * @author paul
 * @version $Id$
 */
public class ScoreQuantiles {

    /**
     * <p>
     * computeQuantiles.
     * </p>
     *
     * @param settings a {@link ubic.erminej.SettingsHolder} object.
     * @param geneScores a {@link ubic.erminej.data.GeneScores} object.
     * @return a {@link hep.aida.bin.QuantileBin1D} object.
     */
    public static QuantileBin1D computeQuantiles( SettingsHolder settings, GeneScores geneScores ) {
        Double[] scores = geneScores.getGeneScores();
        QuantileBin1D scoreQuantiles = new QuantileBin1D( true, scores.length, 0.0, 0.0, 1000,
                new cern.jet.random.engine.DRand() );

        scoreQuantiles.addAllOf( new DoubleArrayList( ArrayUtils.toPrimitive( scores ) ) );

        return scoreQuantiles;
    }
}
