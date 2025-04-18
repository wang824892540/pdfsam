/*
 * This file is part of the PDF Split And Merge source code
 * Created on 18 gen 2016
 * Copyright 2017 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui.components.tool;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.pdfsam.eventstudio.annotation.EventListener;
import org.pdfsam.eventstudio.annotation.EventStation;
import org.pdfsam.model.tool.TaskExecutionRequest;
import org.pdfsam.model.tool.ToolBound;
import org.sejda.model.notification.event.PercentageOfWorkDoneChangedEvent;
import org.sejda.model.notification.event.TaskExecutionCompletedEvent;
import org.sejda.model.notification.event.TaskExecutionFailedEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.pdfsam.eventstudio.StaticStudio.eventStudio;
import static org.pdfsam.i18n.I18nContext.i18n;

/**
 * Horizontal buttons panel shown in the footer
 *
 * @author Andrea Vacondio
 */
public class Footer extends HBox implements ToolBound {

    private final ProgressBar bar = new ProgressBar(0);
    private final Label statusLabel = new Label();
    private final TaskFailedButton failed = new TaskFailedButton();
    private final OpenButton openButton;
    private final RunButton runButton;
    private String ownerModule;

    public Footer(RunButton runButton, OpenButton openButton, String ownerModule) {
        this.ownerModule = defaultString(ownerModule);
        this.openButton = openButton;
        this.runButton = runButton;
        this.getStyleClass().addAll("footer-pane");
        this.statusLabel.getStyleClass().add("status-label");
        this.statusLabel.setVisible(false);
        this.bar.setMaxWidth(Double.MAX_VALUE);
        this.bar.getStyleClass().add("pdfsam-footer-bar");
        this.statusLabel.setMaxHeight(Double.MAX_VALUE);
        VBox progressPane = new VBox(statusLabel, bar);
        progressPane.getStyleClass().add("progress-pane");
        VBox.setVgrow(statusLabel, Priority.ALWAYS);
        HBox.setHgrow(bar, Priority.ALWAYS);
        HBox.setHgrow(progressPane, Priority.ALWAYS);
        this.failed.setVisible(false);
        StackPane buttons = new StackPane(failed, openButton);
        buttons.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(runButton, buttons, progressPane);
        eventStudio().add(TaskExecutionRequest.class, e -> {
            if (e.toolId().equals(ownerModule)) {
                failed.setVisible(false);
                openButton.setVisible(false);
                statusLabel.setVisible(true);
                statusLabel.setText(i18n().tr("Requested"));
                bar.setProgress(0);
            }
        });
        eventStudio().addAnnotatedListeners(this);
    }

    public OpenButton openButton() {
        return openButton;
    }

    public RunButton runButton() {
        return runButton;
    }

    @Override
    @EventStation
    public String toolBinding() {
        return ownerModule;
    }

    @EventListener
    public void onTaskCompleted(TaskExecutionCompletedEvent event) {
        failed.setVisible(false);
        openButton.setVisible(true);
        statusLabel.setText(i18n().tr("Completed"));
        bar.setProgress(1);
    }

    @EventListener
    public void onTaskFailed(TaskExecutionFailedEvent event) {
        openButton.setVisible(false);
        failed.setVisible(true);
        statusLabel.setText(i18n().tr("Failed"));
    }

    @EventListener
    public void onProgress(PercentageOfWorkDoneChangedEvent event) {
        Platform.runLater(() -> {
            statusLabel.setText(i18n().tr("Running"));
            if (event.isUndetermined()) {
                bar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            } else {
                bar.setProgress(event.getPercentage().divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).doubleValue());
                statusLabel.setText(i18n().tr("Running {0}%", Integer.toString(event.getPercentage().intValue())));
            }
        });
    }
}
