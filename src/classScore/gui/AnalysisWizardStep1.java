package classScore.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import baseCode.gui.WizardStep;
import classScore.Settings;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version $Id$
 */

public class AnalysisWizardStep1 extends WizardStep
{
   AnalysisWizard wiz;
   Settings settings;
   JRadioButton oraButton;
   JRadioButton resampButton;
   JRadioButton corrButton;

   public AnalysisWizardStep1(AnalysisWizard wiz, Settings settings)
   {
      super(wiz);
      this.wiz=wiz;
      this.settings=settings;
      setValues();
   }

   //Component initialization
   protected void jbInit(){
      JPanel step1Panel = new JPanel();
      JPanel jPanel4 = new JPanel();
      JLabel jLabel8 = new JLabel();
      JPanel jPanel5 = new JPanel();
      ButtonGroup buttonGroup1 = new ButtonGroup();
      oraButton = new JRadioButton();
      resampButton = new JRadioButton();
      corrButton = new JRadioButton();
      JPanel jPanel12 = new JPanel();
      JLabel jLabel4 = new JLabel();
      JLabel jLabel5 = new JLabel();
      JLabel jLabel9 = new JLabel();
      step1Panel.setPreferredSize(new Dimension(550, 120));
      jPanel4.setBorder(BorderFactory.createEtchedBorder());
      jLabel8.setText("Choose the method of analysis:");
      jLabel8.setPreferredSize(new Dimension(274, 17));
      step1Panel.add(jLabel8, null);
      jPanel5.setPreferredSize(new Dimension(80, 80));
      oraButton.setText("ORA");
      oraButton.setBorder(BorderFactory.createLineBorder(Color.black));
      oraButton.setPreferredSize(new Dimension(75, 17));
      oraButton.addActionListener(new AnalysisWizardStep1_oraButton_actionAdapter(this));
      buttonGroup1.add(oraButton);
      jPanel5.add(oraButton, null);
      resampButton.setText("Resampling");
      resampButton.setSelected(true);
      resampButton.setPreferredSize(new Dimension(75, 17));
      resampButton.setBorder(BorderFactory.createLineBorder(Color.black));
      resampButton.addActionListener(new AnalysisWizardStep1_resampButton_actionAdapter(this));
      buttonGroup1.add(resampButton);
      jPanel5.add(resampButton, null);
      corrButton.setText("Correlation");
      corrButton.setPreferredSize(new Dimension(75, 17));
      corrButton.setBorder(BorderFactory.createLineBorder(Color.black));
      corrButton.addActionListener(new AnalysisWizardStep1_corrButton_actionAdapter(this));
      buttonGroup1.add(corrButton);
      jPanel5.add(corrButton, null);
      jPanel4.add(jPanel5, null);
      jPanel12.setPreferredSize(new Dimension(175, 80));
      jLabel9.setText("- Description of ORA analysis");
      jLabel9.setPreferredSize(new Dimension(172, 17));
      jPanel12.add(jLabel9, null);
      jLabel4.setText("- Description of resampling analysis");
      jLabel4.setPreferredSize(new Dimension(172, 17));
      jPanel12.add(jLabel4, null);
      jLabel5.setText("- Description of correlation analysis");
      jLabel5.setPreferredSize(new Dimension(172, 17));
      jPanel12.add(jLabel5, null);
      jPanel4.add(jPanel12, null);
      step1Panel.add(jPanel4, null);

      this.addHelp("<html>This is a place holder.<br>"+
                   "Blah, blah, blah, blah, blah.");
      this.addMain(step1Panel);
   }

   public boolean isReady() { return true; }

   void corrButton_actionPerformed(ActionEvent e) {
      wiz.setAnalysisType(2);
   }

   void resampButton_actionPerformed(ActionEvent e) {
      wiz.setAnalysisType(1);
   }

   void oraButton_actionPerformed(ActionEvent e) {
      wiz.setAnalysisType(0);
   }

   private void setValues() {
      if(settings.getAnalysisMethod()==Settings.ORA)
         oraButton.setSelected(true);
      else if(settings.getAnalysisMethod()==Settings.RESAMP)
         resampButton.setSelected(true);
      else if(settings.getAnalysisMethod()==Settings.CORR)
         corrButton.setSelected(true);
   }

   public void saveValues(){
      if(oraButton.isSelected()) {
         settings.setAnalysisMethod(Settings.ORA);
      } else if (resampButton.isSelected()) {
         settings.setAnalysisMethod(Settings.RESAMP);
      } else if (corrButton.isSelected()) {
         settings.setAnalysisMethod(Settings.CORR);
      }
   }
}

class AnalysisWizardStep1_oraButton_actionAdapter implements java.awt.event.
        ActionListener {
   AnalysisWizardStep1 adaptee;

   AnalysisWizardStep1_oraButton_actionAdapter(AnalysisWizardStep1 adaptee) {
      this.adaptee = adaptee;
   }

   public void actionPerformed(ActionEvent e) {
      adaptee.oraButton_actionPerformed(e);
   }
}

class AnalysisWizardStep1_corrButton_actionAdapter implements java.awt.event.
        ActionListener {
   AnalysisWizardStep1 adaptee;

   AnalysisWizardStep1_corrButton_actionAdapter(AnalysisWizardStep1 adaptee) {
      this.adaptee = adaptee;
   }

   public void actionPerformed(ActionEvent e) {
      adaptee.corrButton_actionPerformed(e);
   }
}

class AnalysisWizardStep1_resampButton_actionAdapter implements java.awt.event.
        ActionListener {
   AnalysisWizardStep1 adaptee;

   AnalysisWizardStep1_resampButton_actionAdapter(AnalysisWizardStep1 adaptee) {
      this.adaptee = adaptee;
   }

   public void actionPerformed(ActionEvent e) {
      adaptee.resampButton_actionPerformed(e);
   }
}