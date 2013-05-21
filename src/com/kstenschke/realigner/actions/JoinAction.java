/*
 * Copyright 2012-2013 Kay Stenschke
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

package com.kstenschke.realigner.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.IconLoader;
import com.kstenschke.realigner.Preferences;
import com.kstenschke.realigner.TextualHelper;

import javax.swing.*;
import java.util.List;


/**
 * Implode / Explode Action
 */
public class JoinAction extends AnAction {

	/**
	 * Disable when no project open
	 *
	 * @param   event   Action system event
	 */
	public void update(AnActionEvent event) {
		boolean enabled = false;

		final Project project = event.getData(PlatformDataKeys.PROJECT);
		Editor editor = event.getData(PlatformDataKeys.EDITOR);
		if (project != null && editor != null) {
			SelectionModel selectionModel = editor.getSelectionModel();
			boolean hasSelection = selectionModel.hasSelection();
			if (hasSelection) {
				final Document document = editor.getDocument();

				int lineNumberSelStart = document.getLineNumber(selectionModel.getSelectionStart());
				int lineNumberSelEnd = document.getLineNumber(selectionModel.getSelectionEnd());

				if (lineNumberSelEnd > lineNumberSelStart) {
					enabled = true;
				}
			}
		}

		event.getPresentation().setEnabled(enabled);
	}

	/**
	 * Perform implode / explode
	 *
	 * @param   event   Action system event
	 */
	public void actionPerformed(final AnActionEvent event) {

		final Project currentProject = event.getData(PlatformDataKeys.PROJECT);
		//Project currentProject = (Project) event.getDataContext().getData(DataConstants.PROJECT);

		CommandProcessor.getInstance().executeCommand(currentProject, new Runnable() {
			public void run() {

				ApplicationManager.getApplication().runWriteAction(new Runnable() {
					public void run() {
						Editor editor = event.getData(PlatformDataKeys.EDITOR);
						//Editor editor = (Editor) event.getDataContext().getData(DataConstants.EDITOR);

						if (editor != null) {
							boolean cannotJoin = false;

							SelectionModel selectionModel = editor.getSelectionModel();
							boolean hasSelection = selectionModel.hasSelection();

							if (hasSelection) {
								int offsetStart = selectionModel.getSelectionStart();
								int offsetEnd = selectionModel.getSelectionEnd();

								final Document document = editor.getDocument();
								//CharSequence editorText = document.getCharsSequence();
								//int caretOffset = editor.getCaretModel().getOffset();

								int lineNumberSelStart = document.getLineNumber(offsetStart);
								int lineNumberSelEnd = document.getLineNumber(offsetEnd);

								if (document.getLineStartOffset(lineNumberSelEnd) == offsetEnd) {
									lineNumberSelEnd--;
								}

								if (lineNumberSelEnd > lineNumberSelStart) {
									// Join selected lines
									String glue = Messages.showInputDialog(
											  currentProject,
											  "Enter Glue (Optional)", "Join Lines With Glue",
											  IconLoader.getIcon("/com/kstenschke/realigner/resources/images/arrow-join.png"),
											  Preferences.getJoinGlue(), null
									);

									if (glue != null) {
										Preferences.saveJoinProperties(glue);

										List<String> linesList = TextualHelper.extractLines(document, lineNumberSelStart, lineNumberSelEnd);
										String linesStr = "";
										int amountLines = linesList.size();
										for (int i = 0; i < amountLines; i++) {
											linesStr = linesStr + linesList.get(i) + (i < (amountLines - 1) ? glue : "");
										}

										// Remove newlines
										String joinedLines = linesStr.replaceAll("(\\n)+", "");

										// Replace the full lines with themselves joined
										offsetStart = document.getLineStartOffset(lineNumberSelStart);
										offsetEnd = document.getLineEndOffset(lineNumberSelEnd);

										document.replaceString(offsetStart, offsetEnd, joinedLines);
									}
								} else {
									cannotJoin = true;
								}
							} else {
								cannotJoin = true;
							}

							// No selection or only one line of selection? Display resp. message
							if (cannotJoin) {
								JOptionPane.showMessageDialog(editor.getComponent(), "Please select lines to be joined.");
							}
						}
					}
				});

			}
		}, "Join Lines with Glue", UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION);
	}

}
