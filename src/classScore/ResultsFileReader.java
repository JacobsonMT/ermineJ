package classScore;

import java.io.*;
import java.util.*;
import classScore.data.*;
import classScore.gui.GeneSetScoreStatus;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Homin Lee
 * @version $Id$
 */

public class ResultsFileReader {

   private Map results;

   public ResultsFileReader( String filename, GeneSetScoreStatus messenger ) throws NumberFormatException,
         IOException {
      results = new LinkedHashMap();

      File infile = new File( filename );
      if ( !infile.exists() || !infile.canRead() ) {
         throw new IOException( "Could not read " + filename );
      }

      FileInputStream fis = new FileInputStream( filename );
      BufferedInputStream bis = new BufferedInputStream( fis );
      BufferedReader dis = new BufferedReader( new InputStreamReader( bis ) );
      
      messenger.setStatus( "Loading analysis..." );
      String line;
      //line = dis.readLine(); // ditch the header.
      while ( ( line = dis.readLine() ) != null ) {
         StringTokenizer st = new StringTokenizer( line, "\t" );
         String firstword = st.nextToken();
         if ( firstword.compareTo( "!" ) == 0 ) {
            // todo mungedname might not be here.
            String classMungedName = st.nextToken();
            String className = st.nextToken();
            String classId = st.nextToken();
            int size = Integer.parseInt( st.nextToken() );
            int effsize = Integer.parseInt( st.nextToken() );
            double score = Double.parseDouble( st.nextToken() );
            double pval = Double.parseDouble( st.nextToken() );
            GeneSetResult c = new GeneSetResult( classId, className, size,
                  effsize, score, pval );
            results.put( classId, c );
         }
      }
     messenger.setStatus( results.size() + " class results read from file" );
   }

   public Map getResults() {
      return results;
   }

}