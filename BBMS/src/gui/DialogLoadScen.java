/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.io.File;

import utilities.FIO;
import bbms.GlobalFuncs;

/**
 *
 * @author Brian
 */
public class DialogLoadScen extends javax.swing.JDialog {
    
    private boolean overwriteDataFiles = true;

    /**
     * Creates new form DialogNewPop
     */
    public DialogLoadScen(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        Generate_Button = new javax.swing.JButton();
        LinkProb_Field = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        NumHidden_Field = new javax.swing.JTextField();
        NumOutputs_Field = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        NumSensors_Field = new javax.swing.JTextField();
        NumScouts_Field = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        LoadPopFile = new javax.swing.JTextField();
        Load_Button = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        DetailedOutputFileField = new javax.swing.JTextField();
        SummaryOutputFileField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        PercentRunField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        RunPerOrgField = new javax.swing.JTextField();
        Warning_Label = new javax.swing.JLabel();
        OverwriteCheckbox = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        MaxEpochsField = new javax.swing.JTextField();
        PauseEpochCheckbox = new javax.swing.JCheckBox();
        PauseIterCheckbox = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        PrefixOutputField = new javax.swing.JTextField();
        RandCOACheckbox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Scenario");

        Generate_Button.setText("Generate");
        Generate_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Generate_ButtonMouseClicked(evt);
            }
        });

        LinkProb_Field.setColumns(3);
        LinkProb_Field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        LinkProb_Field.setText("0.1");

        jLabel5.setText("Link Prob");

        jLabel1.setText("# Hidden Nodes");

        NumHidden_Field.setColumns(3);
        NumHidden_Field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NumHidden_Field.setText("5");

        NumOutputs_Field.setColumns(1);
        NumOutputs_Field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NumOutputs_Field.setText("1");

        jLabel2.setText("# Outputs");

        jLabel3.setText("# Sensors");

        NumSensors_Field.setColumns(1);
        NumSensors_Field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NumSensors_Field.setText("1");

        NumScouts_Field.setColumns(3);
        NumScouts_Field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        NumScouts_Field.setText("5");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Num Scouts");

        Load_Button.setText("Load");
        Load_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Load_ButtonMouseClicked(evt);
            }
        });

        jLabel6.setText("Load Pop from File");

        DetailedOutputFileField.setText("resultDetails.txt");

        SummaryOutputFileField.setText("resultSummary.txt");

        jLabel7.setText("Detailed Output");

        jLabel8.setText("Summary Output");

        jLabel10.setText("Percent per Run");

        PercentRunField.setText("1.0");

        jLabel11.setText("Runs per Organism");

        RunPerOrgField.setText("10");

        Warning_Label.setText(" ");

        OverwriteCheckbox.setSelected(true);
        OverwriteCheckbox.setText("Overwrite Files");
        OverwriteCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OverwriteCheckboxItemStateChanged(evt);
            }
        });

        jLabel12.setText("Stop After Epochs");

        MaxEpochsField.setText("500");

        PauseEpochCheckbox.setText("Pause at Epoch");
        PauseEpochCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PauseEpochCheckboxItemStateChanged(evt);
            }
        });

        PauseIterCheckbox.setText("Pause at Iter");
        PauseIterCheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                PauseIterCheckboxItemStateChanged(evt);
            }
        });

        jLabel13.setText("Subdir");

        PrefixOutputField.setText(GlobalFuncs.dirPrefix);

        RandCOACheckbox.setSelected(true);
        RandCOACheckbox.setText("Rand COA in Epoch");
        RandCOACheckbox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RandCOACheckboxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(NumScouts_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(NumSensors_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(NumOutputs_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(NumHidden_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(LinkProb_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(Generate_Button))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(PauseIterCheckbox)
                                        .addComponent(PauseEpochCheckbox)))))
                        .addComponent(jLabel6)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(LoadPopFile, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(31, 31, 31)
                            .addComponent(Load_Button)))
                    .addComponent(RandCOACheckbox))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PercentRunField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RunPerOrgField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(DetailedOutputFileField, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                                .addComponent(SummaryOutputFileField))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(Warning_Label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addGap(18, 18, 18)
                                .addComponent(MaxEpochsField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(OverwriteCheckbox, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(PrefixOutputField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NumScouts_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NumSensors_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(DetailedOutputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(NumOutputs_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(Generate_Button))
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NumHidden_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(SummaryOutputFileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PauseIterCheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LinkProb_Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(PauseEpochCheckbox)
                    .addComponent(jLabel13)
                    .addComponent(PrefixOutputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RandCOACheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(OverwriteCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(PercentRunField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(RunPerOrgField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LoadPopFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Load_Button)
                    .addComponent(Warning_Label)
                    .addComponent(jLabel12)
                    .addComponent(MaxEpochsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private boolean ValidateGlobalInputs() {
    	double ppr = Double.parseDouble(PercentRunField.getText());
    	int runsPerOrg = Integer.parseInt(RunPerOrgField.getText());
        int maxE = Integer.parseInt(MaxEpochsField.getText());
    	
    	if (ppr < 0.0 || ppr > 1.0) {
            Warning_Label.setText("ERROR: Percent per run out of bounds.");
            return false;
    	}
    	
    	if (runsPerOrg <= 0) {
            Warning_Label.setText("ERROR: Runs per org out of bounds.");
            return false;
    	}
        
        if (maxE <= 0) {
            Warning_Label.setText("ERROR: Max epochs out of bounds.");
            return false;
        }
    	
    	GlobalFuncs.percentPerRun = ppr;
    	GlobalFuncs.maxRunsPerOrg = runsPerOrg;
        GlobalFuncs.maxEpochs = maxE;
        GlobalFuncs.outputPrefix = PrefixOutputField.getText();
        
        FIO.newDirectory("src/saves/" + GlobalFuncs.outputPrefix);
            	
    	File dOut = FIO.newFile("src/saves/" + GlobalFuncs.outputPrefix + "/" + DetailedOutputFileField.getText());
    	File sOut = FIO.newFile("src/saves/" + GlobalFuncs.outputPrefix + "/" + SummaryOutputFileField.getText());
        File iterOut = FIO.newFile("src/saves/" + GlobalFuncs.outputPrefix + "/" + "detailedIterations.txt");
    	
    	GlobalFuncs.detailedOutput = dOut.toPath();
    	GlobalFuncs.summaryOutput = sOut.toPath();
        GlobalFuncs.fullIterOutput = iterOut.toPath();
        
        if (overwriteDataFiles) {
            FIO.overwriteFile(GlobalFuncs.detailedOutput, "");
            FIO.overwriteFile(GlobalFuncs.summaryOutput, unit.JNEATIntegration.PrintSummaryKey());
            FIO.overwriteFile(GlobalFuncs.fullIterOutput, unit.JNEATIntegration.PrintDetailedIterKey());
            GUI_NB.GCO("Output files overwritten");
        }
        
        GlobalFuncs.newEpoch = true;
    	
    	return true;
    }
    
    private void Generate_ButtonMouseClicked(java.awt.event.MouseEvent evt) {                                             
        int popSize = Integer.parseInt(NumScouts_Field.getText());
        int numSensors = Integer.parseInt(NumSensors_Field.getText());
        int numOutputs = Integer.parseInt(NumOutputs_Field.getText());
        int numHidden = Integer.parseInt(NumHidden_Field.getText());
        double probLink = Double.parseDouble(LinkProb_Field.getText());

        if (popSize < 0) Warning_Label.setText("ERROR: Pop Size < 1");
        else if (numSensors < 1) Warning_Label.setText("ERROR: Num Sensors < 1");
        else if (numOutputs < 1) Warning_Label.setText("ERROR: Num Outputs < 1");
        else if (numHidden < 0) Warning_Label.setText("ERROR: Num Hidden < 1");
        else if (probLink < 0 || probLink > 1) Warning_Label.setText("ERROR: Prob Link not valid percent");
        else if (ValidateGlobalInputs()){
            
            bbms.GlobalFuncs.currentPop = new jneat.Population(Math.max(1, popSize), numSensors, numOutputs, numHidden, false, probLink);
            GUI_NB.GCO("New population added.");            
            GUI_NB.GCO("Running scenario iteration setup.");        
            int subPopSize = GlobalFuncs.currentPop.PopulationSlice(GlobalFuncs.percentPerRun);
            if (popSize == 0) unit.JNEATIntegration.ScenIterationSetup(popSize);    // Key for editing a scenario
            else unit.JNEATIntegration.ScenIterationSetup(subPopSize);
            dispose();
        }    
    }                                            

    private void Load_ButtonMouseClicked(java.awt.event.MouseEvent evt) {                                         
        bbms.GlobalFuncs.tempStr = LoadPopFile.getText();
        if (ValidateGlobalInputs()) {
        	unit.JNEATIntegration.ScenIterationFromFile();
            dispose();
        }        
    }                                        

    private void OverwriteCheckboxItemStateChanged(java.awt.event.ItemEvent evt) {                                                   
        if (evt.getStateChange() == 1) overwriteDataFiles = true;
        else overwriteDataFiles = false;
    }                                                  

    private void PauseEpochCheckboxItemStateChanged(java.awt.event.ItemEvent evt) {                                                    
        if (evt.getStateChange() == 1) GlobalFuncs.pauseNewEpoch = true;
        else GlobalFuncs.pauseNewEpoch = false;
    }                                                   

    private void PauseIterCheckboxItemStateChanged(java.awt.event.ItemEvent evt) {                                                   
        if (evt.getStateChange() == 1) GlobalFuncs.pauseNewIter = true;
        else GlobalFuncs.pauseNewIter = false;
    }                                                  

    private void RandCOACheckboxItemStateChanged(java.awt.event.ItemEvent evt) {                                                 
        if (evt.getStateChange() == 1) GlobalFuncs.randCOAEpoch = true;
        else GlobalFuncs.randCOAEpoch = false;
    }                                                

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogLoadScen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogLoadScen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogLoadScen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogLoadScen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogLoadScen dialog = new DialogLoadScen(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextField DetailedOutputFileField;
    private javax.swing.JButton Generate_Button;
    private javax.swing.JTextField LinkProb_Field;
    private javax.swing.JTextField LoadPopFile;
    private javax.swing.JButton Load_Button;
    private javax.swing.JTextField MaxEpochsField;
    private javax.swing.JTextField NumHidden_Field;
    private javax.swing.JTextField NumOutputs_Field;
    private javax.swing.JTextField NumScouts_Field;
    private javax.swing.JTextField NumSensors_Field;
    private javax.swing.JCheckBox OverwriteCheckbox;
    private javax.swing.JCheckBox PauseEpochCheckbox;
    private javax.swing.JCheckBox PauseIterCheckbox;
    private javax.swing.JTextField PercentRunField;
    private javax.swing.JTextField PrefixOutputField;
    private javax.swing.JCheckBox RandCOACheckbox;
    private javax.swing.JTextField RunPerOrgField;
    private javax.swing.JTextField SummaryOutputFileField;
    private javax.swing.JLabel Warning_Label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration                   
}
