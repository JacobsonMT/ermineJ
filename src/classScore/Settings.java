package classScore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import baseCode.util.FileTools;

/**
 * <hr>
 * <p>
 * Copyright (c) 2004 Columbia University
 * 
 * @author Kiran Keshav
 * @author Homin Lee
 * @author Will Braynen
 * @version $Id$
 */

public class Settings {
   private Properties properties;
   private String pref_file;
   private String classFile;
   private String annotFile;
   private String rawFile;
   private String dataFolder;
   private String classFolder;
   private String scoreFile;
   private int maxClassSize = 100;
   private int minClassSize = 8;
   private int iterations = 10000;
   private int scorecol = 2;
   private int geneRepTreatment = BEST_PVAL;
   private int rawScoreMethod = MEAN_METHOD;
   private int analysisMethod = RESAMP;
   private int quantile = 50;
   private int mtc = BENJAMINIHOCHBERG; // multiple test correction
   private boolean doLog = true;
   private double pValThreshold = 0.001;
   private boolean alwaysUseEmpirical;

   public static final int BEST_PVAL = 1;
   public static final int MEAN_PVAL = 2;
   
   public static final int MEAN_METHOD = 0;
   public static final int QUANTILE_METHOD = 1;
   public static final int MEAN_ABOVE_QUANTILE_METHOD = 2;
   
   public static final int ORA = 0;
   public static final int RESAMP = 1;
   public static final int CORR = 2;
   public static final int ROC = 3;
   
   public static final int BONFERONNI = 0;
   public static final int WESTFALLYOUNG = 1;
   public static final int BENJAMINIHOCHBERG = 2;


   public Settings() {
      this( "" );
   }

   /**
    * Creates settings object
    * 
    * @param filename name of preferences file to read
    */
   public Settings( String filename ) {
      classFile = "";
      annotFile = "";
      rawFile = "";
      scoreFile = "";
      properties = new Properties();

      if ( dataFolder == null && !this.determineDataDirectory() ) {
         return;
      }

      pref_file = filename;
      classFolder = new String( dataFolder
            + System.getProperty( "file.separator" ) + "genesets" );

      if ( !FileTools.testDir( classFolder ) ) {
         new File( classFolder ).mkdir(); // todo should test success and do something about it.
      }

      if ( pref_file.compareTo( "" ) == 0 )
            pref_file = dataFolder + System.getProperty( "file.separator" )
                  + "ClassScore.preferences";

      try {
         File fi = new File( pref_file );
         if ( fi.canRead() ) {
            InputStream f = new FileInputStream( pref_file );
            properties.load( f );
            f.close();
            if ( properties.containsKey( "scoreFile" ) )
                  scoreFile = properties.getProperty( "scoreFile" );
            if ( properties.containsKey( "classFile" ) )
                  classFile = properties.getProperty( "classFile" );
            if ( properties.containsKey( "annotFile" ) )
                  annotFile = properties.getProperty( "annotFile" );
            if ( properties.containsKey( "rawFile" ) )
                  rawFile = properties.getProperty( "rawFile" );
            if ( properties.containsKey( "dataFolder" ) )
                  this.dataFolder = properties.getProperty( "dataFolder" );
            if ( properties.containsKey( "classFolder" ) )
                  this.classFolder = properties.getProperty( "classFolder" );
            if ( properties.containsKey( "maxClassSize" ) )
                  maxClassSize = Integer.valueOf(
                        properties.getProperty( "maxClassSize" ) ).intValue();
            if ( properties.containsKey( "minClassSize" ) )
                  minClassSize = Integer.valueOf(
                        properties.getProperty( "minClassSize" ) ).intValue();
            if ( properties.containsKey( "iterations" ) )
                  iterations = Integer.valueOf(
                        properties.getProperty( "iterations" ) ).intValue();
            if ( properties.containsKey( "scorecol" ) )
                  scorecol = Integer.valueOf(
                        properties.getProperty( "scorecol" ) ).intValue();
            if ( properties.containsKey( "geneRepTreatment" ) )
                  geneRepTreatment = Integer.valueOf(
                        properties.getProperty( "geneRepTreatment" ) )
                        .intValue();
            if ( properties.containsKey( "rawScoreMethod" ) )
                  rawScoreMethod = Integer.valueOf(
                        properties.getProperty( "rawScoreMethod" ) ).intValue();
            if ( properties.containsKey( "analysisMethod" ) )
                  analysisMethod = Integer.valueOf(
                        properties.getProperty( "analysisMethod" ) ).intValue();
            if ( properties.containsKey( "quantile" ) )
                  quantile = Integer.valueOf(
                        properties.getProperty( "quantile" ) ).intValue();
            if ( properties.containsKey( "doLog" ) )
                  doLog = Boolean.valueOf( properties.getProperty( "doLog" ) )
                        .booleanValue();
            if ( properties.containsKey( "pValThreshold" ) )
                  pValThreshold = Double.valueOf(
                        properties.getProperty( "pValThreshold" ) )
                        .doubleValue();
            if ( properties.containsKey( "useEmpirical" ) )
                  alwaysUseEmpirical = Boolean.valueOf(
                        properties.getProperty( "useEmpirical" ) )
                        .booleanValue();
            if ( properties.containsKey( "mtc" ) )
                  mtc = Integer.valueOf( properties.getProperty( "mtc" ) )
                        .intValue();
         }
      } catch ( IOException ex ) {
         //    System.err.println( "Could not find preferences file. Will probably attempt to create a new one." ); // no
         // big
         // deal.
      }
   }

   /**
    * Creates settings object
    * 
    * @param settings - settings object to copy
    */
   public Settings( Settings settings ) {
      classFile = settings.getClassFile();
      annotFile = settings.getAnnotFile();
      rawFile = settings.getRawFile();
      dataFolder = settings.getDataFolder();
      classFolder = settings.getClassFolder();
      scoreFile = settings.getScoreFile();
      maxClassSize = settings.getMaxClassSize();
      minClassSize = settings.getMinClassSize();
      iterations = settings.getIterations();
      scorecol = settings.getScorecol();
      geneRepTreatment = settings.getGeneRepTreatment();
      rawScoreMethod = settings.getRawScoreMethod();
      analysisMethod = settings.getAnalysisMethod();
      quantile = settings.getQuantile();
      doLog = settings.getDoLog();
      pValThreshold = settings.getPValThreshold();
      alwaysUseEmpirical = settings.getAlwaysUseEmpirical();
      pref_file = settings.getPrefFile();
      mtc = settings.getMtc();
      properties = new Properties();
   }

   /**
    * Writes setting values to file.
    */
   public void writePrefs() throws IOException {

      if ( pref_file == null || pref_file.length() == 0 ) {
         return;
      }
      properties.setProperty( "scoreFile", scoreFile );
      properties.setProperty( "classFile", classFile );
      properties.setProperty( "annotFile", annotFile );
      properties.setProperty( "rawFile", rawFile );
      properties.setProperty( "dataFolder", dataFolder );
      properties.setProperty( "classFolder", classFolder );
      properties.setProperty( "maxClassSize", String.valueOf( maxClassSize ) );
      properties.setProperty( "minClassSize", String.valueOf( minClassSize ) );
      properties.setProperty( "iterations", String.valueOf( iterations ) );
      properties.setProperty( "scorecol", String.valueOf( scorecol ) );
      properties.setProperty( "geneRepTreatment", String
            .valueOf( geneRepTreatment ) );
      properties
            .setProperty( "rawScoreMethod", String.valueOf( rawScoreMethod ) );
      properties.setProperty( "mtc", String.valueOf( mtc ) );
      properties
            .setProperty( "analysisMethod", String.valueOf( analysisMethod ) );
      properties.setProperty( "quantile", String.valueOf( quantile ) );
      properties.setProperty( "doLog", String.valueOf( doLog ) );
      properties.setProperty( "pValThreshold", String.valueOf( pValThreshold ) );
      properties.setProperty( "useEmpirical", String
            .valueOf( alwaysUseEmpirical ) );
      OutputStream f = new FileOutputStream( pref_file );
      properties.store( f, "" );
      f.close();
      pref_file=null;//keshav
   }

   public String toString() {

      return properties.toString();
   }

   /**
    * Figure out where the data directory should go.
    * 
    * @return
    */
   public boolean determineDataDirectory() {
      dataFolder = System.getProperty( "user.dir" ); // directory from which we are running the software. This is not
      // platform independent so we fall back on the user home directory.

      dataFolder = dataFolder.substring( 0, dataFolder.lastIndexOf( System
            .getProperty( "file.separator" ) ) ); // up one level.

      dataFolder = dataFolder + System.getProperty( "file.separator" )
            + "ermineJ.data";

      if ( !FileTools.testDir( dataFolder ) ) {
         dataFolder = System.getProperty( "user.home" )
               + System.getProperty( "file.separator" ) + "ermineJ.data";

         if ( !FileTools.testDir( dataFolder ) ) {

            // try to make it in the user's home directory.
            return ( new File( dataFolder ) ).mkdir();
         }
      }

      return true;
   }

   /**
    * Returns setting values.
    */
   public String getClassFile() {
      return classFile;
   }

   public String getAnnotFile() {
      return annotFile;
   }

   public String getRawFile() {
      return rawFile;
   }

   public String getDataFolder() {
      return dataFolder;
   }

   public String getClassFolder() {
      return classFolder;
   }

   public String getScoreFile() {
      return scoreFile;
   }

   public int getMaxClassSize() {
      return maxClassSize;
   }

   public int getMinClassSize() {
      return minClassSize;
   }

   public int getIterations() {
      return iterations;
   }

   public int getScorecol() {
      return scorecol;
   }

   public int getGeneRepTreatment() {
      return geneRepTreatment;
   }

   public int getRawScoreMethod() {
      return rawScoreMethod;
   }

   public int getAnalysisMethod() {
      return analysisMethod;
   }

   public int getQuantile() {
      return quantile;
   }

   public boolean getDoLog() {
      return doLog;
   }

   public double getPValThreshold() {
      return pValThreshold;
   }

   public String getPrefFile() {
      return pref_file;
   }

   /**
    * Sets setting values.
    */
   public void setClassFile( String val ) {
      classFile = val;
   }

   public void setAnnotFile( String val ) {
      annotFile = val;
   }

   public void setRawFile( String val ) {
      rawFile = val;
   }

   public void setDataFolder( String val ) {
      dataFolder = val;
   }

   public void setClassFolder( String val ) {
      classFolder = val;
   }

   public void setScoreFile( String val ) {
      scoreFile = val;
   }

   public void setMaxClassSize( int val ) {
      maxClassSize = val;
   }

   public void setMinClassSize( int val ) {
      minClassSize = val;
   }

   public void setIterations( int val ) {
      iterations = val;
   }

   public void setScorecol( int val ) {
      scorecol = val;
   }

   public void setGeneRepTreatment( int val ) {
      geneRepTreatment = val;
   }

   public void setRawScoreMethod( int val ) {
      rawScoreMethod = val;
   }

   public void setAnalysisMethod( int val ) {
      analysisMethod = val;
   }

   public void setQuantile( int val ) {
      quantile = val;
   }

   public void setDoLog( boolean val ) {
      doLog = val;
   }

   public void setPValThreshold( double val ) {
      pValThreshold = val;
   }

   public void setPrefFile( String val ) {
      pref_file = val;
   }

   public boolean getUseWeights() {
      if ( geneRepTreatment == MEAN_PVAL || geneRepTreatment == BEST_PVAL )
            return true;

      return false;
   }

   public String getGroupMethodString() {
      if ( geneRepTreatment == MEAN_PVAL )
         return "MEAN_PVAL";
      else if ( geneRepTreatment == BEST_PVAL )
         return "BEST_PVAL";
      else
         return "MEAN_PVAL"; // dummy. It won't be used.
   }

   public int getGroupMethod() {
      return geneRepTreatment;
   }

   public int getClassScoreMethod() {
      return rawScoreMethod;

   }

   public String getClassScoreMethodString() {
      if ( rawScoreMethod == MEAN_METHOD ) {
         return "Mean";
      }
      return "Quantile"; // note that quantile is hard-coded to be 50 for the
      // gui.

   }

   /**
    * @return
    */
   public boolean getUseLog() {
      return doLog;
   }

   /**
    * @return
    */
   public boolean getAlwaysUseEmpirical() {
      return alwaysUseEmpirical;
   }

   /**
    * @param b
    */
   public void setAlwaysUseEmpirical( boolean b ) {
      alwaysUseEmpirical = b;
   }

   /**
    * @return Returns the mtc.
    */
   public int getMtc() {
      return mtc;
   }
   /**
    * @param mtc The mtc to set.
    */
   public void setMtc( int mtc ) {
      this.mtc = mtc;
   }
}

