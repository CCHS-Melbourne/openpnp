/*
 * Copyright (C) 2011 Jason von Nieda <jason@vonnieda.org>
 * 
 * This file is part of OpenPnP.
 * 
 * OpenPnP is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * OpenPnP is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with OpenPnP. If not, see
 * <http://www.gnu.org/licenses/>.
 * 
 * For more information about OpenPnP visit http://openpnp.org
 */

package org.openpnp.machine.reference.feeder.wizards;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.openpnp.gui.components.ComponentDecorators;
import org.openpnp.gui.support.ActuatorsComboBoxModel;
import org.openpnp.gui.support.DoubleConverter;
import org.openpnp.machine.reference.feeder.ReferenceAutoFeeder;
import org.openpnp.machine.reference.feeder.ReferenceAutoFeeder.ActuatorType;
import org.openpnp.model.Configuration;
import org.openpnp.spi.Actuator;
import org.openpnp.util.UiUtils;
import org.pmw.tinylog.Logger;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JComboBox;

public class ReferenceAutoFeederConfigurationWizard
        extends AbstractReferenceFeederConfigurationWizard {
    private final ReferenceAutoFeeder feeder;
    private JComboBox comboBoxFeedActuator;
    private JTextField actuatorValue;
    private JComboBox comboBoxPostPickActuator;
    private JTextField postPickActuatorValue;
    private JComboBox actuatorType;
    private JComboBox postPickActuatorType;
    private JButton btnTestFeedActuator;
    private JButton btnTestPostPickActuator;

    public ReferenceAutoFeederConfigurationWizard(ReferenceAutoFeeder feeder) {
        super(feeder);
        this.feeder = feeder;

        JPanel panelActuator = new JPanel();
        panelActuator.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
                "Actuators", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        contentPanel.add(panelActuator);
        panelActuator.setLayout(new FormLayout(new ColumnSpec[] {
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,
                FormSpecs.RELATED_GAP_COLSPEC,
                FormSpecs.DEFAULT_COLSPEC,},
            new RowSpec[] {
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,
                FormSpecs.RELATED_GAP_ROWSPEC,
                FormSpecs.DEFAULT_ROWSPEC,}));

        JLabel lblActuator = new JLabel("Actuator");
        panelActuator.add(lblActuator, "4, 2, left, default");

        JLabel lblActuatorType = new JLabel("Actuator Type");
        panelActuator.add(lblActuatorType, "6, 2, left, default");

        JLabel lblActuatorValue = new JLabel("Actuator Value");
        panelActuator.add(lblActuatorValue, "8, 2, left, default");

        JLabel lblFeed = new JLabel("Feed");
        panelActuator.add(lblFeed, "2, 4, right, default");
        
        comboBoxFeedActuator = new JComboBox();
        comboBoxFeedActuator.setModel(new ActuatorsComboBoxModel(Configuration.get().getMachine()));
        panelActuator.add(comboBoxFeedActuator, "4, 4, fill, default");
        
        actuatorType = new JComboBox(ActuatorType.values());
        panelActuator.add(actuatorType, "6, 4, fill, default");

        actuatorValue = new JTextField();
        panelActuator.add(actuatorValue, "8, 4");
        actuatorValue.setColumns(10);
        
        JLabel lblForBoolean = new JLabel("For Boolean: 1 = True, 0 = False");
        panelActuator.add(lblForBoolean, "10, 4");

        btnTestFeedActuator = new JButton(testFeedActuatorAction);
        panelActuator.add(btnTestFeedActuator, "12, 4");

        JLabel lblPostPick = new JLabel("Post Pick");
        panelActuator.add(lblPostPick, "2, 6, right, default");
        
        comboBoxPostPickActuator = new JComboBox();
        comboBoxPostPickActuator.setModel(new ActuatorsComboBoxModel(Configuration.get().getMachine()));
        panelActuator.add(comboBoxPostPickActuator, "4, 6, fill, default");
        
        postPickActuatorType = new JComboBox(ActuatorType.values());
        panelActuator.add(postPickActuatorType, "6, 6, fill, default");

        postPickActuatorValue = new JTextField();
        postPickActuatorValue.setColumns(10);
        panelActuator.add(postPickActuatorValue, "8, 6");
        
        JLabel label = new JLabel("For Boolean: 1 = True, 0 = False");
        panelActuator.add(label, "10, 6");

        btnTestPostPickActuator = new JButton(testPostPickActuatorAction);
        panelActuator.add(btnTestPostPickActuator, "12, 6");
    }

    @Override
    public void createBindings() {
        super.createBindings();

        DoubleConverter doubleConverter =
                new DoubleConverter(Configuration.get().getLengthDisplayFormat());

        addWrappedBinding(feeder, "actuatorName", comboBoxFeedActuator, "selectedItem");
        addWrappedBinding(feeder, "actuatorType", actuatorType, "selectedItem");
        addWrappedBinding(feeder, "actuatorValue", actuatorValue, "text", doubleConverter);
        
        addWrappedBinding(feeder, "postPickActuatorName", comboBoxPostPickActuator, "selectedItem");
        addWrappedBinding(feeder, "postPickActuatorType", postPickActuatorType, "selectedItem");
        addWrappedBinding(feeder, "postPickActuatorValue", postPickActuatorValue, "text", doubleConverter);
        
        ComponentDecorators.decorateWithAutoSelect(actuatorValue);
        ComponentDecorators.decorateWithAutoSelect(postPickActuatorValue);
    }

    private Action testFeedActuatorAction = new AbstractAction("Test feed") {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			UiUtils.messageBoxOnException(() -> {
				if (feeder.getActuatorName() == null || feeder.getActuatorName().equals("")) {
					Logger.warn("No actuatorName specified for feeder {}.", feeder.getName());
					return;
				}
				Actuator actuator = Configuration.get().getMachine().getActuatorByName(feeder.getActuatorName());

				if (actuator == null) {
					throw new Exception("Feed failed. Unable to find an actuator named " + feeder.getActuatorName());
				}
				if (feeder.getActuatorType() == ActuatorType.Boolean) {
					actuator.actuate(feeder.getActuatorValue() != 0);
				} else {
					actuator.actuate(feeder.getActuatorValue());
				}
			});
		}
    };

    private Action testPostPickActuatorAction = new AbstractAction("Test post pick") {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			UiUtils.messageBoxOnException(() -> {
				if (feeder.getPostPickActuatorName() == null || feeder.getPostPickActuatorName().equals("")) {
					Logger.warn("No postPickActuatorName specified for feeder {}.", feeder.getName());
					return;
				}
				Actuator actuator = Configuration.get().getMachine()
						.getActuatorByName(feeder.getPostPickActuatorName());

				if (actuator == null) {
					throw new Exception(
							"Feed failed. Unable to find an actuator named " + feeder.getPostPickActuatorName());
				}
				if (feeder.getPostPickActuatorType() == ActuatorType.Boolean) {
					actuator.actuate(feeder.getPostPickActuatorValue() != 0);
				} else {
					actuator.actuate(feeder.getPostPickActuatorValue());
				}
			});
		}
    };
}
