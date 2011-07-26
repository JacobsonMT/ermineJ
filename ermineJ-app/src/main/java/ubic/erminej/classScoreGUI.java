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

import javax.swing.UIManager;

import ubic.erminej.gui.MainFrame;
import ubic.erminej.gui.util.GuiUtil;

/**
 * Main for GUI
 * 
 * @author Paul Pavlidis
 * @version $Id$
 */
public class classScoreGUI {
    public static void main( String[] args ) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
            new classScoreGUI( new Settings() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public classScoreGUI( final Settings settings ) {
        MainFrame frame = new MainFrame( settings );
        init( frame );
    }

    private void init( MainFrame frame ) {

        GuiUtil.centerContainer( frame );

        frame.setVisible( true );

    }

}