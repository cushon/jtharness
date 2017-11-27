/*
 * $Id$
 *
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.sun.javatest.tool.jthelp;

import com.sun.javatest.tool.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JTHelpProgressBar extends Component implements PropertyChangeListener {

    private JProgressBar progressBar;
    private SwingWorker task;
    private JDialog frame;
    private JPanel сontentPane;
    private UIFactory uif;


    public JTHelpProgressBar(SwingWorker progressTask) {
        uif = new UIFactory(this, null);

        task = progressTask;
        сontentPane = new JPanel(new BorderLayout());
        JLabel waitText = uif.createLabel("help.wait");
        progressBar = uif.createProgressBar("help.progress", JProgressBar.HORIZONTAL);

        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel();
        panel.add(waitText);
        panel.add(progressBar);

        сontentPane.add(panel, BorderLayout.PAGE_START);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            final int progress = (Integer) evt.getNewValue();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    progressBar.setValue(progress);
                }
            });
        }
        if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    frame.dispose();
                }
            });
        }
    }


    public void createAndShowGUI() {

        frame = new JDialog();
        frame.setModal(true);
        frame.setUndecorated(true);
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(uif.getI18NColor("help.progress")) );

        сontentPane.setOpaque(true);
        frame.setContentPane(сontentPane);
        frame.pack();

        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - frame.getWidth()) / 2;
        final int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        task.addPropertyChangeListener(JTHelpProgressBar.this);
        task.execute();

        frame.setVisible(true);

    }

}