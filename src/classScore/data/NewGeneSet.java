package classScore.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import baseCode.gui.GuiUtil;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author Homin K Lee
 * @version $Id$
 */

public class NewGeneSet {
   private GeneAnnotations geneData;
   private String id;
   private String desc;
   private ArrayList probes;
   private boolean modifiedGS=false;

   public NewGeneSet( GeneAnnotations geneData ) {
      this.geneData = geneData;
      id = new String( "" );
      desc = new String( "" );
      probes = new ArrayList();
   }

   public void setModified(boolean val) { modifiedGS=val; }
   public boolean modified() { return modifiedGS; }

   public void clear() {
      id = "";
      desc = "";
      probes.clear();
   }

   public AbstractTableModel toTableModel( boolean editable ) {
      final boolean finalized = editable;

      return new AbstractTableModel() {

         private String[] columnNames = { "Probe", "Gene", "Description" };

         public String getColumnName( int i ) {
            return columnNames[i];
         }

         public int getRowCount() {
            int windowrows;
            if ( finalized ) {
               windowrows = 16;
            } else {
               windowrows = 13;
            }
            int extra = 1;
            if ( probes.size() < windowrows ) {
               extra = windowrows - probes.size();
            }
            return probes.size() + extra;
         }

         public int getColumnCount() {
            return 3;
         }

         public Object getValueAt( int r, int c ) {
            if ( r < probes.size() ) {
               String probeid = ( String ) probes.get( r );
               switch ( c ) {
                  case 0:
                     return probeid;
                  case 1:
                     return geneData.getProbeGeneName( probeid );
                  case 2:
                     return geneData.getProbeDescription( probeid );
                  default:
                     return null;
               }
            }
            return null;
         }

         public boolean isCellEditable( int r, int c ) {
            if ( !finalized && ( c == 0 || c == 1 ) ) {
               return true;
            }
            return false;

         }
      };
   }

   public void loadClassFile( String file ) {
      clear();
      File infile = new File( file );
      if ( !infile.exists() || !infile.canRead() ) {
         GuiUtil.error( "Could not find file: " + file );
      } else {
         try {
            FileInputStream fis = new FileInputStream( file );
            BufferedInputStream bis = new BufferedInputStream( fis );
            BufferedReader dis = new BufferedReader(
                  new InputStreamReader( bis ) );
            String row;
            ArrayList genes = new ArrayList();
            String type = new String( "" );
            int filetype = 0;
            while ( ( row = dis.readLine() ) != null ) {
               if ( type.compareTo( "" ) == 0 ) {
                  type = row;
                  if ( type.compareTo( "probe" ) == 0 ) {
                     filetype = 0;
                  } else if ( type.compareTo( "gene" ) == 0 ) {
                     filetype = 1;
                  }
               } else if ( id.compareTo( "" ) == 0 ) {
                  id = row;
               } else if ( desc.compareTo( "" ) == 0 ) {
                  desc = row;
               } else {
                  if ( filetype == 0 ) {
                     probes.add( row );
                  } else if ( filetype == 1 ) {
                     genes.add( row );
                  }
               }
            }
            dis.close();
            if ( filetype == 1 ) {
               HashSet probeSet = new HashSet();
               for ( Iterator it = genes.iterator(); it.hasNext(); ) {
                  probeSet.addAll( geneData.getGeneProbeList( ( String ) it
                        .next() ) );
               }
               probes = new ArrayList( probeSet );
            }
         } catch ( IOException ioe ) {
            GuiUtil.error( "Could not find file: " + file );
         }
      }

   }

   public static String getFileName( String folder, String id ) {
      return new String( folder + File.separatorChar + id + "-class.txt" );
   }

   public void saveClass( String folder, int type ) {
      try {
         String fileid = id.replace( ':', '-' );
         String filedesc = desc.replace( '\n', ' ' );
         String filetype = ( type == 0 ) ? "probe" : "gene";
         BufferedWriter out = new BufferedWriter( new FileWriter( getFileName(
               folder, fileid ), false ) );
         out.write( filetype + "\n" );
         out.write( id + "\n" );
         out.write( filedesc + "\n" );
         for ( Iterator it = probes.iterator(); it.hasNext(); ) {
            out.write( ( String ) it.next() + "\n" );
         }
         out.close();
      } catch ( IOException e ) {
         System.err
               .println( "There was an IO error while printing the results: "
                     + e );
      }
   }

   public static HashMap getClassFileInfo( String file ) {
      HashMap cinfo = new HashMap();
      File infile = new File( file );
      if ( !infile.exists() || !infile.canRead() ) {
         System.err.println( "Could not find file: " + file );
      } else {
         try {
            FileInputStream fis = new FileInputStream( file );
            BufferedInputStream bis = new BufferedInputStream( fis );
            BufferedReader dis = new BufferedReader(
                  new InputStreamReader( bis ) );
            String row;
            ArrayList members = new ArrayList();
            while ( ( row = dis.readLine() ) != null ) {
               if ( !cinfo.containsKey( "type" ) ) {
                  cinfo.put( "type", row );
               } else if ( !cinfo.containsKey( "id" ) ) {
                  cinfo.put( "id", row );
               } else if ( !cinfo.containsKey( "desc" ) ) {
                  cinfo.put( "desc", row );
               } else {
                  members.add( row );
               }
            }
            cinfo.put( "members", members );
            dis.close();
         } catch ( IOException ioe ) {
            System.err.println( "Could not find file: " + ioe );
         }
      }
      return cinfo;
   }

   public void addToMaps( GONames goData ) {
      geneData.addClass( id, this.getProbes() );
      goData.addClass( id, desc );
      geneData.sortGeneSets();
   }

   /**
    * Redefine a class. The "real" version of the class is modified to look like this one.
    */
   public void modifyClass( GONames goData ) {
      geneData.modifyClass( id, probes );
      goData.modifyClass( id, desc );
   }

   public void setId( String val ) {
      id = val;
   }

   public void setDesc( String val ) {
      desc = val;
   }

   public void setProbes( ArrayList val ) {
      probes = val;
   }

   public String getId() {
      return id;
   }

   public String getDesc() {
      return desc;
   }

   public ArrayList getProbes() {
      return probes;
   }

}
