package edu.gui.frame;

import edu.dbframework.parse.beans.TableItem;
import edu.gui.table.DataTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class ChartDialog extends JDialog {

    JPanel topPanel;

    JComboBox tableComboBox;

    public ChartDialog() {
        this.getContentPane().setLayout(new BorderLayout());
        setTitle("Charts");
        setBounds(150, 150, 640, 480);
        init();
    }

    private void init() {
        DataTableModel model = (DataTableModel) MainFrame.table.getModel();
        tableComboBox = new JComboBox(model.getColumnNames().toArray());

        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(tableComboBox);

        this.getContentPane().add(topPanel, BorderLayout.NORTH);

/*        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        JFreeChart chart = ChartFactory.createBarChart("Comparison between Salesman",
                "Salesman", "Profit", dataset, PlotOrientation.VERTICAL,
                false, true, false);

        ChartPanel panel = new ChartPanel(chart);
        this.getContentPane().add(panel, BorderLayout.CENTER);*/
    }

}
