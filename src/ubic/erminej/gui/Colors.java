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

import java.awt.Color;

/**
 * @author pavlidis
 * @version $Id$
 */
public class Colors {
    static final Color goParent = Color.LIGHT_GRAY;
    static final Color goChild = Color.YELLOW;

    static final Color LIGHTRED1 = new Color( 240, 100, 100 ); // dark
    static final Color LIGHTRED2 = new Color( 240, 140, 140 );
    static final Color LIGHTRED3 = new Color( 240, 180, 180 );
    static final Color LIGHTRED4 = new Color( 240, 200, 200 );
    static final Color LIGHTRED5 = new Color( 240, 220, 220 ); // light

    static final Color GREY1 = new Color( 100, 100, 100 ); // dark
    static final Color GREY2 = new Color( 140, 140, 140 );
    static final Color GREY3 = new Color( 189, 180, 180 );
    static final Color GREY4 = new Color( 200, 200, 200 );
    static final Color GREY5 = new Color( 220, 220, 220 ); // light

    static final Color LIGHTGREEN1 = new Color( 140, 240, 140 ); // dark
    static final Color LIGHTGREEN2 = new Color( 160, 240, 160 );
    static final Color LIGHTGREEN3 = new Color( 180, 240, 180 );
    static final Color LIGHTGREEN4 = new Color( 200, 240, 200 );
    static final Color LIGHTGREEN5 = new Color( 220, 240, 220 ); // light

    static final Color PINK = new Color( 220, 160, 220 );
    static final Color LIGHTYELLOW = new Color( 255, 239, 142 );

    /**
     * @param pvalCorr
     * @return
     */
    public static Color chooseBackgroundColorForPvalue( double pvalCorr ) {
        Color bgColor = null;
        if ( pvalCorr < 0.001 ) {
            bgColor = Colors.LIGHTGREEN2;
        } else if ( pvalCorr < 0.01 ) {
            bgColor = Colors.LIGHTGREEN3;
        } else if ( pvalCorr < 0.05 ) {
            bgColor = Colors.LIGHTGREEN4;
        } else if ( pvalCorr < 0.1 ) {
            bgColor = Colors.LIGHTGREEN5;
        } else {
            bgColor = Color.WHITE;
        }
        return bgColor;
    }
}