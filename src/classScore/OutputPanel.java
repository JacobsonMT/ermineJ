package classScore;

import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * @todo make columns start out better sizes
 */

public class OutputPanel extends JScrollPane {
   JTable table;
   OutputTableModel model;

   public OutputPanel(Vector results) {
      try {
         model = new OutputTableModel(results);
         table = new JTable(model);
         this.getViewport().add(table, null);
         table.getColumnModel().getColumn(0).setPreferredWidth(70);
         table.getColumnModel().getColumn(2).setPreferredWidth(50);
         table.getColumnModel().getColumn(3).setPreferredWidth(50);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // public void addClassDetailsListener(ClassDetailsEventListener listener) {
   //   listenerList.add(ClassDetailsEventListener.class, listener);
   // }
   public void addInitialClassData(InitialMaps data) {
      model.addInitialClassData(data);
      table.setModel(model);
      table.revalidate();
   }

   public void addRunData(Map data) {
      model.addRunData(data);
      table.addColumn(new TableColumn(model.getColumnCount() - 3));
      table.addColumn(new TableColumn(model.getColumnCount() - 2));
      table.addColumn(new TableColumn(model.getColumnCount() - 1));
      table.getColumnModel().getColumn(model.getColumnCount() - 3).setPreferredWidth(30);
      table.getColumnModel().getColumn(model.getColumnCount() - 2).setPreferredWidth(30);
      table.getColumnModel().getColumn(model.getColumnCount() - 1).setPreferredWidth(30);
   }

   public void addRun() {
      model.addRun();
      table.addColumn(new TableColumn(model.getColumnCount() - 3));
      table.addColumn(new TableColumn(model.getColumnCount() - 2));
      table.addColumn(new TableColumn(model.getColumnCount() - 1));
      table.getColumnModel().getColumn(model.getColumnCount() - 3).setPreferredWidth(30);
      table.getColumnModel().getColumn(model.getColumnCount() - 2).setPreferredWidth(30);
      table.getColumnModel().getColumn(model.getColumnCount() - 1).setPreferredWidth(30);
   }
}


class OutputTableModel extends AbstractTableModel {
   InitialMaps imaps;
   //Vector results = new Vector();
   Vector results;
   Vector columnNames = new Vector();
   private NumberFormat nf = NumberFormat.getInstance();
   int state = -1;
   int cols_per_run = 3;

   public OutputTableModel(Vector results) {
      this.results=results;
      nf.setMaximumFractionDigits(8);
      columnNames.add("Name");
      columnNames.add("Description");
      columnNames.add("# of Probes");
      columnNames.add("# of Genes");
   }

   public void addInitialClassData(InitialMaps imaps) {
      state = 0;
      this.imaps = imaps;
   }

   public void addRunData(Map result) {
      state++;
      columnNames.add("Run " + state + " Rank");
      columnNames.add("Run " + state + " Score");
      columnNames.add("Run " + state + " Pval");
      results.add(result);
   }

   public void addRun() {
      state++;
      columnNames.add("Run " + state + " Rank");
      columnNames.add("Run " + state + " Score");
      columnNames.add("Run " + state + " Pval");
   }

   public String getColumnName(int i) {return (String) columnNames.get(i);
   }

   public int getColumnCount() {return columnNames.size();
   }

   public int getRowCount() {
      if (state == -1) {
         return 20;
      } else {
         return imaps.numClasses();
      }
   }

   public Object getValueAt(int i, int j) {
      if (state >= 0 && j < 4) {
         String classid = imaps.getClass(i);
         switch (j) {
         case 0:
            return classid;
         case 1:
            return imaps.getClassDesc(classid);
         case 2:
            return Integer.toString(imaps.numProbes(classid));
         case 3:
            return Integer.toString(imaps.numGenes(classid));
         }
      } else if (state > 0) {
         String classid = imaps.getClass(i);
         double runnum = Math.floor((j - 4) / cols_per_run);
         Map data = ((classPvalRun)results.get((int) runnum)).getResults();
         //Map data = (Map)results.get((int) runnum);
         if (data.containsKey(classid)) {
            classresult res = (classresult) data.get(classid);
            if ((j - 4) % cols_per_run == 0) {
               return new Integer(res.getRank());
            } else if ((j - 4) % cols_per_run == 1) {
               return new Double(nf.format(res.getScore()));
            } else {
               return new Double(nf.format(res.getPvalue()));
            }
         } else {
            return "";
         }
      }
      return "";
   }
}
