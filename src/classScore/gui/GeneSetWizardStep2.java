package classScore.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;

import baseCode.bio.geneset.GeneAnnotations;
import baseCode.gui.WizardStep;
import baseCode.gui.table.TableSorter;
import classScore.data.NewGeneSet;
import java.awt.event.*;
import javax.swing.*;

/**
 * <hr>
 * <p>
 * Copyright (c) 2004 Columbia University
 *
 * @author Homin K Lee
 * @version $Id$
 */

public class GeneSetWizardStep2 extends WizardStep {

   private GeneSetWizard wiz;
   private GeneAnnotations geneData;
   private JLabel countLabel;
   private JTable probeTable;
   private JTable newClassTable;
   private AbstractTableModel ncTableModel;
   private NewGeneSet newGeneSet;
   private JTextField searchTextField;

   private final static int COL0WIDTH = 80;
   private final static int COL1WIDTH = 80;
   private final static int COL2WIDTH = 200;

   public GeneSetWizardStep2( GeneSetWizard wiz, GeneAnnotations geneData,
         NewGeneSet newGeneSet ) {
      super( wiz );
      this.wiz = wiz;
      this.geneData = geneData;
      this.newGeneSet = newGeneSet;
      wiz.clearStatus();
      geneData.resetSelectedProbes();
      populateTables();
   }

   //Component initialization
   protected void jbInit() {
      BorderLayout borderLayout2 = new BorderLayout();
      this.setLayout( borderLayout2 );
      JPanel step2Panel;

      JPanel topPanel = new JPanel();
      // countLabel = new JLabel();
      JLabel jLabel1 = new JLabel();
      JLabel jLabel2 = new JLabel();
      jLabel1.setPreferredSize(new Dimension(250, 15));
      jLabel1.setText("Available Probes");
      jLabel2.setPreferredSize(new Dimension(250, 15));
      jLabel2.setText("Probes in gene set");
      showStatus( "Number of Probes selected: 0" );
      //  topPanel.add( countLabel );
      topPanel.add(jLabel1, null);
      topPanel.add(jLabel2, null);

      step2Panel = new JPanel();
      BorderLayout borderLayout1 = new BorderLayout();
      step2Panel.setLayout( borderLayout1 );

      JPanel centerPanel = new JPanel();
      GridLayout gridLayout1 = new GridLayout();
      centerPanel.setLayout( gridLayout1 );
      JScrollPane probeScrollPane;
      JScrollPane newClassScrollPane;
      probeTable = new JTable();
      probeTable.getTableHeader().setReorderingAllowed( false );
      probeScrollPane = new JScrollPane( probeTable );
      probeScrollPane.setPreferredSize( new Dimension( 250, 150 ) );
      newClassTable = new JTable();
      newClassTable.getTableHeader().setReorderingAllowed( false );
      newClassScrollPane = new JScrollPane( newClassTable );
      newClassScrollPane.setPreferredSize( new Dimension( 250, 150 ) );
      centerPanel.add( probeScrollPane, null );
      centerPanel.add( newClassScrollPane, null );

      JPanel bottomPanel = new JPanel();
      bottomPanel.setPreferredSize( new Dimension( 300, 50 ) );

      JButton searchButton = new JButton();
      searchButton.setText( "Find" );
      searchButton
            .addActionListener( new GeneSetWizardStep2_searchButton_actionAdapter(
                  this ) );

      searchTextField = new JTextField();
      searchTextField.setPreferredSize( new Dimension( 80, 19 ) );
      searchTextField
            .addKeyListener( new GeneSetWizardStep2_searchText_keyAdapter( this ) );
      searchTextField
            .addActionListener( new GeneSetWizardStep2_searchTextField_actionAdapter(
                  this ) );
      JButton addButton = new JButton();
      addButton.setSelected( false );
      addButton.setText( "Add >" );
      addButton
            .addActionListener( new GeneSetWizardStep2_addButton_actionAdapter(
                  this ) );
      JButton deleteButton = new JButton();
      deleteButton.setSelected( false );
      deleteButton.setText( "Delete" );
      deleteButton
            .addActionListener( new GeneSetWizardStep2_delete_actionPerformed_actionAdapter(
                  this ) );

      bottomPanel.add( searchButton );
      //    bottomPanel.add(searchLabel, null);
      bottomPanel.add( searchTextField );
      bottomPanel.add( addButton, null );
      bottomPanel.add( deleteButton, null );
      step2Panel.add( topPanel, BorderLayout.NORTH );
      step2Panel.add( centerPanel, BorderLayout.CENTER );
      step2Panel.add( bottomPanel, BorderLayout.SOUTH );

      this.addHelp( "<html><b>Set up the gene set</b><br>"
            + "Add or remove probes/genes using the buttons below the table. "
            + "To find a specific gene use the 'find' tool." );
      this.addMain( step2Panel );
   }

   public boolean isReady() {
      if ( newGeneSet.getProbes().size() == 0 ) {
         return false;
      }

      return true;
   }

   void delete_actionPerformed( ActionEvent e ) {
      int n = newClassTable.getSelectedRowCount();
      int[] rows = newClassTable.getSelectedRows();
      for ( int i = 0; i < n; i++ ) {
         newGeneSet.getProbes().remove(
               newClassTable.getValueAt( rows[i] - i, 0 ) );
      }
      ncTableModel.fireTableDataChanged();
      updateCountLabel();
   }

   void addButton_actionPerformed( ActionEvent e ) {
      int n = probeTable.getSelectedRowCount();
      int[] rows = probeTable.getSelectedRows();
      for ( int i = 0; i < n; i++ ) {
         //newGeneSet.probes.add(probeTable.getValueAt(rows[i], 0)); (for just
         // deleting probes)
         String newGene;
         if ( ( newGene = geneData.getProbeGeneName( ( String ) probeTable
               .getValueAt( rows[i], 0 ) ) ) != null ) {
            addGene( newGene );
         }
      }
      HashSet noDupes = new HashSet( newGeneSet.getProbes() );
      newGeneSet.getProbes().clear();
      newGeneSet.getProbes().addAll( noDupes );
      ncTableModel.fireTableDataChanged();
      updateCountLabel();
   }

   void editorProbe_actionPerformed( ChangeEvent e ) {
      String newProbe = ( String ) ( ( DefaultCellEditor ) e.getSource() )
            .getCellEditorValue();
      String newGene;
      if ( ( newGene = geneData.getProbeGeneName( newProbe ) ) != null ) {
         addGene( newGene );
      } else {
         showError( "Probe " + newProbe + " does not exist." );
      }
   }

   void editorGene_actionPerformed( ChangeEvent e ) {
      String newGene = ( String ) ( ( DefaultCellEditor ) e.getSource() )
            .getCellEditorValue();
      addGene( newGene );
   }

   void addGene( String gene ) {
      ArrayList probelist = geneData.getGeneProbeList( gene );
      if ( probelist != null ) {
         newGeneSet.getProbes().addAll( probelist );
         ncTableModel.fireTableDataChanged();
         updateCountLabel();
      } else {
         showError( "Gene " + gene + " does not exist." );
      }
   }

   public void updateCountLabel() {
      showStatus( "Number of Probes selected: " + newGeneSet.getProbes().size() );
   }

   private void populateTables() {
      ProbeTableModel model = new ProbeTableModel( geneData );
      TableSorter sorter = new TableSorter( model );
      probeTable.setModel( sorter );
      sorter.setTableHeader( probeTable.getTableHeader() );
      probeTable.getColumnModel().getColumn( 0 ).setPreferredWidth( COL0WIDTH );
      probeTable.getColumnModel().getColumn( 1 ).setPreferredWidth( COL1WIDTH );
      probeTable.getColumnModel().getColumn( 2 ).setPreferredWidth( COL2WIDTH );

      ncTableModel = newGeneSet.toTableModel( false );
      newClassTable.setModel( ncTableModel );
      newClassTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 40 );
      newClassTable.getColumnModel().getColumn( 1 ).setPreferredWidth( 40 );

      showStatus( "Available probes: " + geneData.selectedProbes() );
   }

   /**
    * do a search.
    */
   public void searchButton_actionPerformed_adapter( ActionEvent e ) {
      find();
   }

   void searchTextField_actionPerformed( ActionEvent e ) {
      find();
   }

   void find() {
      String searchOn = searchTextField.getText();

      if ( searchOn.equals( "" ) ) {
         geneData.resetSelectedProbes();
      } else {
         geneData.selectProbes( searchOn );
      }
      populateTables();
   }
}

class GeneSetWizardStep2_delete_actionPerformed_actionAdapter implements
      java.awt.event.ActionListener {
   GeneSetWizardStep2 adaptee;

   GeneSetWizardStep2_delete_actionPerformed_actionAdapter(
         GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void actionPerformed( ActionEvent e ) {
      adaptee.delete_actionPerformed( e );
   }
}

class GeneSetWizardStep2_addButton_actionAdapter implements
      java.awt.event.ActionListener {
   GeneSetWizardStep2 adaptee;

   GeneSetWizardStep2_addButton_actionAdapter( GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void actionPerformed( ActionEvent e ) {
      adaptee.addButton_actionPerformed( e );
   }
}

class GeneSetWizardStep2_editorProbeAdaptor implements CellEditorListener {
   GeneSetWizardStep2 adaptee;

   GeneSetWizardStep2_editorProbeAdaptor( GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void editingStopped( ChangeEvent e ) {
      adaptee.editorProbe_actionPerformed( e );
   }

   public void editingCanceled( ChangeEvent e ) {
      editingCanceled( e );
   }
}

class GeneSetWizardStep2_editorGeneAdaptor implements CellEditorListener {
   GeneSetWizardStep2 adaptee;

   GeneSetWizardStep2_editorGeneAdaptor( GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void editingStopped( ChangeEvent e ) {
      adaptee.editorGene_actionPerformed( e );
   }

   public void editingCanceled( ChangeEvent e ) {
      editingCanceled( e );
   }
}

// hitting enter in search also activates it.

class GeneSetWizardStep2_searchText_actionAdapter implements ActionListener {
   GeneSetWizardStep2 adaptee;

   public GeneSetWizardStep2_searchText_actionAdapter(
         GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void actionPerformed( ActionEvent e ) {
      adaptee.searchButton_actionPerformed_adapter( e );
   }
}

// respond to typing in the search field.
// todo 3.0 Stub: incremental search trigger would go here.

class GeneSetWizardStep2_searchText_keyAdapter implements KeyListener {

   GeneSetWizardStep2 adaptee;

   public GeneSetWizardStep2_searchText_keyAdapter( GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void keyPressed( KeyEvent e ) {
   }

   public void keyReleased( KeyEvent e ) {
   }

   public void keyTyped( KeyEvent e ) {
   }

}

class ProbeTableModel extends AbstractTableModel {
   GeneAnnotations geneData;
   private String[] columnNames = {
         "Probe", "Gene", "Description"
   };

   public ProbeTableModel( GeneAnnotations geneData ) {
      this.geneData = geneData;
   }

   public String getColumnName( int i ) {
      return columnNames[i];
   }

   public int getColumnCount() {
      return 3;
   }

   public int getRowCount() {
      return geneData.getSelectedProbes().size();
   }

   public Object getValueAt( int i, int j ) {

      String probeid = ( String ) geneData.getSelectedProbes().get( i );
      switch ( j ) {
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
}
// respond to search request.

class GeneSetWizardStep2_searchButton_actionAdapter implements ActionListener {
   GeneSetWizardStep2 adaptee;

   public GeneSetWizardStep2_searchButton_actionAdapter(
         GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void actionPerformed( ActionEvent e ) {
      adaptee.searchButton_actionPerformed_adapter( e );
   }

}

class GeneSetWizardStep2_searchTextField_actionAdapter implements
      java.awt.event.ActionListener {
   GeneSetWizardStep2 adaptee;

   GeneSetWizardStep2_searchTextField_actionAdapter( GeneSetWizardStep2 adaptee ) {
      this.adaptee = adaptee;
   }

   public void actionPerformed( ActionEvent e ) {
      adaptee.searchTextField_actionPerformed( e );
   }
}
