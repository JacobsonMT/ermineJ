/*
 * The baseCode project
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
package ubic.erminej.gui.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * <p>
 * DataFileFilter class.
 * </p>
 *
 * @author Will Braynen
 * @version $Id$
 */
public class DataFileFilter extends FileFilter {

    /** {@inheritDoc} */
    @Override
    public boolean accept( File f ) {

        if ( f.isDirectory() ) {
            return true;
        }

        /*
         * This used to filter on the file ending, but causes too many problems.
         */
        return true;

    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return "Plain or compressed text files";
    }
}
