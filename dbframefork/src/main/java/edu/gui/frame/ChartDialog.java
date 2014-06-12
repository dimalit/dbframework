package edu.gui.frame;

import edu.gui.table.DataTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


public class ChartDialog extends JDialog {

    JPanel topPanel;

    JComboBox tableComboBox;
    JTextField columnChartCount;
    JButton drawChartButton;

    public ChartDialog() {
        this.getContentPane().setLayout(new BorderLayout());
        setTitle("Charts");
        setBounds(150, 150, 640, 480);
        init();
    }

    private void init() {
        DataTableModel model = (DataTableModel) MainFrame.table.getModel();
        tableComboBox = new JComboBox(model.getColumnsWithNumbers().toArray());
        columnChartCount = new JTextField();
        columnChartCount.setColumns(5);
        drawChartButton = new JButton("Draw Chart");
        drawChartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                drawChart();
            }
        });

        topPanel = new JPanel(new FlowLayout());
        topPanel.add(tableComboBox);
        topPanel.add(columnChartCount);
        topPanel.add(drawChartButton);

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    private void drawChart() {
        JFreeChart chart = ChartFactory.createBarChart((String) tableComboBox.getSelectedItem(),
                "Intervals", "Counts", createDataset(), PlotOrientation.VERTICAL,
                false, true, false);

        ChartPanel panel = new ChartPanel(chart);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().revalidate();
    }

    private DefaultCategoryDataset createDataset() {
        DataTableModel model = (DataTableModel) MainFrame.table.getModel();
        List<Float> columnSet = new ArrayList<Float>();
        for (String value : model.getData().get(tableComboBox.getSelectedItem())) {
            Float floatVal = new Float(value);
            columnSet.add(floatVal);
        }
        Collections.sort(columnSet);

        Integer chartColumnsCount = null;
        if (columnChartCount.getText().length() > 0) {
            chartColumnsCount = new Integer(columnChartCount.getText());
        } else {
            chartColumnsCount = new Integer(10);
        }

        LinkedHashMap<String, Integer> datasetValues = new LinkedHashMap<String, Integer>();
        float span = (columnSet.get(columnSet.size() - 1) - columnSet.get(0)) / chartColumnsCount;
        int columnSetKey = 0;
        for (int i = 1; i <= chartColumnsCount; i++) {
            if (columnSetKey < columnSet.size()) {
                int countInSpan = 0;
                String chartColumnLabel = "" + columnSet.get(columnSetKey) + " - " + (columnSet.get(columnSetKey) + span);
                float initialSpan = columnSet.get(columnSetKey) + span;
                while (columnSetKey < columnSet.size() && columnSet.get(columnSetKey) < initialSpan) {
                    countInSpan++;
                    columnSetKey++;
                }
                datasetValues.put(chartColumnLabel, countInSpan);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String key : datasetValues.keySet()) {
            dataset.setValue(datasetValues.get(key), "Counts", key);
        }
        return dataset;
    }

}
