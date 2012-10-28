/*
 * Copyright 2012 Kay Stenschke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kstenschke.realigner.resources.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.kstenschke.realigner.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WrapOptions extends JDialog {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JTextField textFieldPrefix;
	private JTextField textFieldPostfix;
	private JCheckBox escapeSingleQuotesCheckBox;
	private JCheckBox escapeDoubleQuotesCheckBox;
	private JCheckBox escapeBackslashesCheckBox;
	private JCheckBox removeBlankLines;
	private JPanel panelQuickWrapButtons;
	private JPanel panelWrapButtonsContainer;

	public Boolean clickedOk	= false;



	/**
	 * Wrap Options constructor
	 */
	public WrapOptions() {
		clickedOk	= false;

		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

			// Init quick wrap buttons from stored wrap button item configs, or hide resp. sub panel
		if( !Settings.areWrapButtonsConfigured() ) {
			panelQuickWrapButtons.setVisible(false);
		} else {
				// Add quick wrap buttons
			Object[] buttonsLabels	= Settings.getWrapButtonItemsLabels();

				// Cleanup wrap buttons panel, set layout: grid with a row per quick wrap button
			panelWrapButtonsContainer.removeAll();
			panelWrapButtonsContainer.setLayout(new GridLayoutManager(buttonsLabels.length, 1, new Insets(0, 0, 0, 0), 0, 0, true, false));

			for( int i = 0; i < buttonsLabels.length; i++ ) {
				JButton wrapButton	= new javax.swing.JButton( buttonsLabels[i].toString() );
				panelWrapButtonsContainer.add(wrapButton, new GridConstraints(i, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false) );
				wrapButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

					}
				});
			}
			panelWrapButtonsContainer.revalidate();

			panelQuickWrapButtons.setVisible(true);
		}


			// Setup button action listeners
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onOK();
			}
		});
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		});

// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				onCancel();
			}
		});

// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}



	/**
	 * Handle click ok event
	 */
	private void onOK() {
		clickedOk	= true;
		dispose();
	}



	/**
	 * Handle click cancel event
	 */
	private void onCancel() {
		clickedOk	= false;
		dispose();
	}



	/**
	 * Get wrap LHS
	 *
	 * @return	String
	 */
	public String getTextFieldPrefix() {
		return textFieldPrefix.getText();
	}


	/**
	 * Set wrap LHS
	 */
	public void setTextFieldPrefix(String prefix) {
		textFieldPrefix.setText(prefix);
	}



	/**
	 * Get wrap RHS
	 *
	 * @return	String
	 */
	public String getTextFieldPostfix() {
		return textFieldPostfix.getText();
	}



	/**
	 * Set wrap LHS
	 */
	public void setTextFieldPostfix(String postfix) {
		textFieldPostfix.setText(postfix);
	}



	/**
	 * Check whether wrapped single quotes are selected to be escaped
	 *
	 * @return	Boolean.
	 */
	public Boolean isSelectedEscapeSingleQuotes() {
		return escapeSingleQuotesCheckBox.isSelected();
	}

	public void setSelectedEscapeSingleQuotes(Boolean setSelected) {
		escapeSingleQuotesCheckBox.setSelected(setSelected);
	}




	/**
	 * Check whether wrapped double quotes are selected to be escaped
	 *
	 * @return	Boolean.
	 */
	public Boolean isSelectedEscapeDoubleQuotes() {
		return escapeDoubleQuotesCheckBox.isSelected();
	}

	public void setSelectedEscapeDoubleQuotes(Boolean setSelected) {
		escapeDoubleQuotesCheckBox.setSelected(setSelected);
	}



	/**
	 * Check whether wrapped double quotes are selected to be escaped
	 *
	 * @return	Boolean.
	 */
	public Boolean isSelectedEscapeBackslashes() {
		return escapeBackslashesCheckBox.isSelected();
	}

	public void setSelectedEscapeBackslashes(Boolean setSelected) {
		escapeBackslashesCheckBox.setSelected(setSelected);
	}

	public void setSelectedRemoveBlankLines(Boolean setSelected) {
		removeBlankLines.setSelected(setSelected);
	}


	public Boolean isSelectedRemoveBlankLines() {
		return removeBlankLines.isSelected();
	}

	public void setRemoveBlankLinesVisible(Boolean isVisible) {
		removeBlankLines.setVisible(isVisible);
	}



	/**
	 * @param	args	Arguments
	 */
	public static void main(String[] args) {
		WrapOptions dialog = new WrapOptions();

		dialog.pack();
		dialog.setVisible(true);
		System.exit(0);
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
	}

}
