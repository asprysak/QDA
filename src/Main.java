import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.util.ShapeUtilities;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import net.miginfocom.swing.MigLayout;

/** 
 * Klasa główna rysująca okno programu, przeprowadzająca wszystkie obliczenia,
 * wyświetlająca wyniki i wykres.
 * 
 * @author Anna Sprysak
 */
public class Main {

	DataSet dataset1 = new DataSet();
	DataSet dataset2 = new DataSet();
	MainFrame frame = new MainFrame();
	JButton refershButton = new JButton("Next step");
	JButton clearButton = new JButton("Clear");
	JMenuItem addPoint = new JMenuItem("Add a point");
	PanelChart chartPanel = new PanelChart();
	int counter = 0;
	int pointCounter = 1;
	float xPoint = 0;
	float yPoint = 0;
	Color col1 = new Color(0, 0, 100);
	Color col2 = new Color(150, 0, 0);

	ExtendedTextField s00Field1 = new ExtendedTextField(" ");
	ExtendedTextField s01Field1 = new ExtendedTextField(" ");
	ExtendedTextField s10Field1 = new ExtendedTextField(" ");
	ExtendedTextField s11Field1 = new ExtendedTextField(" ");

	ExtendedTextField s00Field2 = new ExtendedTextField(" ");
	ExtendedTextField s01Field2 = new ExtendedTextField(" ");
	ExtendedTextField s10Field2 = new ExtendedTextField(" ");
	ExtendedTextField s11Field2 = new ExtendedTextField(" ");

	ExtendedTextField s2od00Field = new ExtendedTextField(" ");
	ExtendedTextField s2od01Field = new ExtendedTextField(" ");
	ExtendedTextField s2od10Field = new ExtendedTextField(" ");
	ExtendedTextField s2od11Field = new ExtendedTextField(" ");

	ExtendedTextField s1od00Field = new ExtendedTextField(" ");
	ExtendedTextField s1od01Field = new ExtendedTextField(" ");
	ExtendedTextField s1od10Field = new ExtendedTextField(" ");
	ExtendedTextField s1od11Field = new ExtendedTextField(" ");

	JTextField aField = new JTextField(" ");
	JTextField bField = new JTextField(" ");
	JTextField cField = new JTextField(" ");
	JTextField dField = new JTextField(" ");
	JTextField eField = new JTextField(" ");
	JTextField fField = new JTextField(" ");

	double A = 0;
	double B = 0;
	double C = 0;
	double D = 0;
	double E = 0;
	double F1 = 0;
	double F2 = 0;

	Main() {

		frame.setTitle("QDA");
		frame.controlPanel.setLayout(new MigLayout(""));
		frame.graphicPanel.add(chartPanel);
		frame.validate();
		refershButton.setEnabled(false);
		addPoint.setEnabled(false);


		refershButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (counter == 0) {
					chartPanel = new PanelChart(dataset1, dataset2);
					frame.graphicPanel.removeAll();
					frame.graphicPanel.add(chartPanel);
					chartPanel.revalidate();
					frame.graphicPanel.revalidate();
					frame.repaint();
				}

				float n1 = dataset1.getN();
				float n2 = dataset2.getN();

				double[][] S1 = new double[2][2];

				for (int i = 0; i < n1; i++) {
					S1[0][0] += Math.pow(
							(dataset1.data[0][i] - dataset1.getXMean()), 2);
					S1[1][0] += (dataset1.data[0][i] - dataset1.getXMean())	* (dataset1.data[1][i] - dataset1.getYMean());
					S1[1][1] += Math.pow((dataset1.data[1][i] - dataset1.getYMean()), 2);
				}
				S1[0][1] = S1[1][0];
				for (int i = 0; i < S1[0].length; i++) {
					for (int j = 0; j < S1.length; j++) {
						S1[j][i] = S1[j][i] / (n1 - 1);
					}
				}
				
				s00Field1.setText(String.format("%.3f", S1[0][0]));
				s01Field1.setText(String.format("%.3f", S1[0][1]));
				s10Field1.setText(String.format("%.3f", S1[1][0]));
				s11Field1.setText(String.format("%.3f", S1[1][1]));

				double[][] S2 = new double[2][2];

				for (int i = 0; i < n2; i++) {
					S2[0][0] += Math.pow((dataset2.data[0][i] - dataset2.getXMean()), 2);
					S2[1][0] += (dataset2.data[0][i] - dataset2.getXMean())	* (dataset2.data[1][i] - dataset2.getYMean());
					S2[0][1] = S2[1][0];
					S2[1][1] += Math.pow((dataset2.data[1][i] - dataset2.getYMean()), 2);
				}
				for (int i = 0; i < S2[0].length; i++) {
					for (int j = 0; j < S2.length; j++) {
						S2[j][i] = S2[j][i] / (n2 - 1);
					}
				}
				s00Field2.setText(String.format("%.3f", S2[0][0]));
				s01Field2.setText(String.format("%.3f", S2[0][1]));
				s10Field2.setText(String.format("%.3f", S2[1][0]));
				s11Field2.setText(String.format("%.3f", S2[1][1]));

				double[][] S1od = new double[2][2];
				double det = S1[0][0] * S1[1][1] - S1[0][1] * S1[1][0];

				S1od[0][0] = S1[1][1] / det;
				S1od[0][1] = -S1[0][1] / det;
				S1od[1][0] = -S1[1][0] / det;
				S1od[1][1] = S1[0][0] / det;

				s1od00Field.setText(String.format("%.3f", S1od[0][0]));
				s1od01Field.setText(String.format("%.3f", S1od[0][1]));
				s1od10Field.setText(String.format("%.3f", S1od[1][0]));
				s1od11Field.setText(String.format("%.3f", S1od[1][1]));

				double[][] S2od = new double[2][2];
				double det2 = S2[0][0] * S2[1][1] - S2[0][1] * S2[1][0];

				S2od[0][0] = S2[1][1] / det2;
				S2od[0][1] = -S2[0][1] / det2;
				S2od[1][0] = -S2[1][0] / det2;
				S2od[1][1] = S2[0][0] / det2;

				s2od00Field.setText(String.format("%.3f", S2od[0][0]));
				s2od01Field.setText(String.format("%.3f", S2od[0][1]));
				s2od10Field.setText(String.format("%.3f", S2od[1][0]));
				s2od11Field.setText(String.format("%.3f", S2od[1][1]));

				A = -(S1od[0][0] - S2od[0][0]) / 2;
				B = -(S1od[1][1] - S2od[1][1]) / 2;
				C = -(S1od[1][0] - S2od[1][0] + S1od[0][1] - S2od[0][1]) / 2;
				F1 = Math.log(n1 / n2) + Math.log(Math.abs(det2 / det)) / 2
						+ S2od[0][0] * dataset2.getXMean()
						* dataset2.getXMean() / 2 + S2od[0][1]
						* dataset2.getXMean() * dataset2.getYMean()
						+ S2od[1][1] * dataset2.getYMean()
						* dataset2.getYMean() / 2;
				F2 = -(S1od[0][0] * dataset1.getXMean()
						* dataset1.getXMean() / 2 + S1od[0][1]
						* dataset1.getXMean() * dataset1.getYMean() + S1od[1][1]
						* dataset1.getYMean() * dataset1.getYMean() / 2);
				D = S1od[0][0] * dataset1.getXMean() + S1od[1][0]
						* dataset1.getYMean() - S2od[0][0]
						* dataset2.getXMean() - S2od[1][0]
						* dataset2.getYMean();
				E = (S1od[0][1] * dataset1.getXMean() + S1od[1][1]
						* dataset1.getYMean() - S2od[0][1]
						* dataset2.getXMean() - S2od[1][1]
						* dataset2.getYMean());

				aField.setText(String.format("%.3f", A));
				bField.setText(String.format("%.3f", B));
				cField.setText(String.format("%.3f", C));
				dField.setText(String.format("%.3f", D));
				eField.setText(String.format("%.3f", E));
				fField.setText(String.format("%.3f", (F1 + F2)));

				Matrix m1 = new Matrix(S1);
				Matrix m2 = new Matrix(S2);
				EigenvalueDecomposition val1 = m1.eig();
				EigenvalueDecomposition val2 = m2.eig();
				double[] real1 = val1.getRealEigenvalues();
				Matrix vec1 = val1.getV();
				double[] real2 = val2.getRealEigenvalues();
				Matrix vec2 = val2.getV();

				int ind1 = 0;
				double max1 = real1[0];
				for (int i = 0; i < real1.length; i++) {
					if (real1[i] > max1) {
						max1 = real1[i];
						ind1 = i;
					}
				}

				double min1 = real1[0];
				for (int i = 0; i < real1.length; i++) {
					if (real1[i] < min1) {
						min1 = real1[i];
					}
				}

				int ind2 = 0;
				double max2 = real2[0];
				for (int i = 0; i < real2.length; i++) {
					if (real2[i] > max2) {
						max2 = real2[i];
						ind2 = i;
					}
				}
				
				double min2 = real2[0];
				for (int i = 0; i < real2.length; i++) {
					if (real2[i] < min2) {
						min2 = real2[i];
					}
				}

				double[][] vec1Array = vec1.getArray();
				double x1 = vec1Array[0][ind1];
				double y1 = vec1Array[1][ind1];
				double ar1 = Math.atan(y1 / x1);

				double[][] vec2Array = vec2.getArray();
				double x2 = vec2Array[0][ind2];
				double y2 = vec2Array[1][ind2];
				double ar2 = Math.atan(y2 / x2);

				float r1x = (float) Math.sqrt(Math.abs(max1));
				float r1y = (float) Math.sqrt(Math.abs(min1));
				float r2x = (float) Math.sqrt(Math.abs(max2));
				float r2y = (float) Math.sqrt(Math.abs(min2));

				if (counter == 1) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (Math.sqrt(2.2914) * r1x),
							(float) (Math.sqrt(2.2914) * r1y), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (Math.sqrt(2.2914) * r2x),
							(float) (Math.sqrt(2.2914) * r2y), col2, ar2);
				}
				if (counter == 2) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (Math.sqrt(6.1582) * r1x),
							(float) (Math.sqrt(6.1582) * r1y), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (Math.sqrt(6.1582) * r2x),
							(float) (Math.sqrt(6.1582) * r2y), col2, ar2);
				}
				if (counter == 3) {
					chartPanel.addElipse(dataset1.getXMean(),
							dataset1.getYMean(),
							(float) (Math.sqrt(11.6182) * r1x),
							(float) (Math.sqrt(11.6182) * r1y), col1, ar1);
					chartPanel.addElipse(dataset2.getXMean(),
							dataset2.getYMean(),
							(float) (Math.sqrt(11.6182) * r2x),
							(float) (Math.sqrt(11.6182) * r2y), col2, ar2);
				}
				if (counter == 4) {
					NumberAxis domainAxis = (NumberAxis) ((XYPlot) chartPanel.chart
							.getPlot()).getRangeAxis();
					domainAxis.setAutoRange(false);

					float minX = dataset1.data[0][0];
					float maxX = dataset1.data[0][0];
					float miny = dataset1.data[1][0];
					float maxy = dataset1.data[1][0];
					for (int j = 0; j < dataset1.data[0].length; j++) {
						if (dataset1.data[0][j] < minX) {
							minX = dataset1.data[0][j];
						}
						if (dataset1.data[0][j] > maxX) {
							maxX = dataset1.data[0][j];
						}
					}
					for (int j = 0; j < dataset2.data[0].length; j++) {
						if (dataset2.data[0][j] < minX) {
							minX = dataset2.data[0][j];
						}
						if (dataset2.data[0][j] > maxX) {
							maxX = dataset2.data[0][j];
						}
					}
					for (int j = 0; j < dataset1.data[0].length; j++) {
						if (dataset1.data[1][j] < miny) {
							miny = dataset1.data[1][j];
						}
						if (dataset1.data[1][j] > maxy) {
							maxy = dataset1.data[1][j];
						}
					}

					double[] curveX = new double[100000];
					double[] curveY = new double[100000];
					double dx = ((maxX + 1) - (minX - 1)) / 100000;
					curveX[0] = minX - dx - 1;
					for (int i = 1; i < 100000; i++) {
						curveX[i] = curveX[i - 1] + dx;
					}
					int valid1 = 0;

					List<Double> listX1 = new ArrayList<Double>();
					List<Double> listY1 = new ArrayList<Double>();
					for (int i = 0; i < 100000; i++) {
						double delta = Math.pow(C * curveX[i] + E, 2)
								- 4
								* B
								* (A * curveX[i] * curveX[i] + D
										* curveX[i] + F1 + F2);
						if (delta >= 0) {
							curveY[i] = (-C * curveX[i] - E - Math
									.sqrt(delta)) / (2 * B);
							listX1.add(curveX[i]);
							listY1.add(curveY[i]);
							valid1++;
						}
					}
					double[] curveYNew = new double[valid1];
					double[] curveXNew = new double[valid1];
					Iterator<Double> iterX1 = listX1.iterator();
					Iterator<Double> iterY1 = listY1.iterator();
					for (int j = 0; j < curveYNew.length; j++) {
						if (iterX1.hasNext()) {
							curveXNew[j] = iterX1.next();

						}
					}
					for (int j = 0; j < curveYNew.length; j++) {
						if (iterY1.hasNext()) {
							curveYNew[j] = iterY1.next();
						}
					}

					List<Double> listX2 = new ArrayList<Double>();
					List<Double> listY2 = new ArrayList<Double>();

					double[] curveX2 = curveX.clone();
					double[] curveY2 = new double[100000];
					int valid2 = 0;
					for (int i = 0; i < 100000; i++) {
						double delta = Math.pow(C * curveX2[i] + E, 2)
								- 4
								* B
								* (A * curveX2[i] * curveX2[i] + D
										* curveX2[i] + F1 + F2);
						if (delta >= 0) {
							curveY2[i] = (-C * curveX2[i] - E + Math
									.sqrt(delta)) / (2 * B);
							listX2.add(curveX2[i]);
							listY2.add(curveY2[i]);
							valid2++;
						}
					}

					double[] curveY2New = new double[valid2];
					double[] curveX2New = new double[valid2];
					Iterator<Double> iterX2 = listX2.iterator();
					Iterator<Double> iterY2 = listY2.iterator();
					for (int j = 0; j < curveY2New.length; j++) {
						if (iterX2.hasNext()) {
							curveX2New[j] = iterX2.next();
						}
					}
					for (int j = 0; j < curveY2New.length; j++) {
						if (iterY2.hasNext()) {
							curveY2New[j] = iterY2.next();
						}
					}
					double[][] curve1 = { curveXNew, curveYNew };
					double[][] curve2 = { curveX2New, curveY2New };

					XYSeries xyseriesPara = new XYSeries("Curve");
					for (int i = 0; i < curve1[0].length; i++) {
						xyseriesPara.add(curve1[0][i], curve1[1][i]);
					}

					chartPanel.xyseriescollection.addSeries(xyseriesPara);
					chartPanel.xylineandshaperenderer.setSeriesStroke(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							new BasicStroke(1.5f));
					chartPanel.xylineandshaperenderer.setSeriesShape(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							ShapeUtilities.createDiamond(0.5f));
					chartPanel.xylineandshaperenderer.setSeriesLinesVisible(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							false);
					chartPanel.xylineandshaperenderer.setSeriesShapesVisible(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							true);
					chartPanel.xylineandshaperenderer.setSeriesPaint(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							Color.magenta);
					chartPanel.xylineandshaperenderer.setSeriesOutlinePaint(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							Color.magenta);

					XYSeries xyseriesPara2 = new XYSeries("Curve2");
					for (int i = 0; i < curve2[0].length; i++) {
						xyseriesPara2.add(curve2[0][i], curve2[1][i]);
					}
					chartPanel.xyseriescollection.addSeries(xyseriesPara2);
					LegendItemCollection legend = chartPanel.chart.getPlot()
							.getLegendItems();
					LegendItemCollection newLegend = new LegendItemCollection();

					for (int i = 0; i < chartPanel.xyseriescollection
							.getSeriesCount() - 1; i++) {
						newLegend.add(legend.get(i));
					}

					((XYPlot) chartPanel.chart.getPlot())
							.setFixedLegendItems(newLegend);
					chartPanel.xylineandshaperenderer.setSeriesStroke(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							new BasicStroke(1.5f));
					chartPanel.xylineandshaperenderer.setSeriesShape(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							ShapeUtilities.createDiamond(0.5f));
					chartPanel.xylineandshaperenderer.setSeriesOutlinePaint(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							Color.magenta);
					chartPanel.xylineandshaperenderer.setSeriesLinesVisible(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							false);
					chartPanel.xylineandshaperenderer.setSeriesShapesVisible(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							true);
					chartPanel.xylineandshaperenderer.setSeriesPaint(
							chartPanel.xyseriescollection.getSeriesCount() - 1,
							Color.magenta);
					addPoint.setEnabled(true);

				}
				counter++;

				if (counter == 5) {
					refershButton.setEnabled(false);
					counter = 0;
				}
			}
		});

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				counter = 0;
				chartPanel.removeAll();
				frame.graphicPanel.updateUI();
				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}
				addPoint.setEnabled(false);
				
				s00Field1.setText(" ");
				s01Field1.setText(" ");
				s10Field1.setText(" ");
				s11Field1.setText(" ");

				s00Field2.setText(" ");
				s01Field2.setText(" ");
				s10Field2.setText(" ");
				s11Field2.setText(" ");

				s2od00Field.setText(" ");
				s2od01Field.setText(" ");
				s2od10Field.setText(" ");
				s2od11Field.setText(" ");

				s1od00Field.setText(" ");
				s1od01Field.setText(" ");
				s1od10Field.setText(" ");
				s1od11Field.setText(" ");

				aField.setText(" ");
				bField.setText(" ");
				cField.setText(" ");
				dField.setText(" ");
				eField.setText(" ");
				fField.setText(" ");

				pointCounter = 1;

			}
		});

		frame.controlPanel.add(this.refershButton,"width 110:110:110, wrap");
		frame.controlPanel.add(this.clearButton, "width 110:110:110, wrap");

		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		JMenu file = new JMenu("Options");
		bar.add(file);

		addPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDialog pointchooser = new JDialog();
				pointchooser.setTitle("Add a point");
				int dialogWidth = 150;
				int dialogHeight = 130;
				int screenWidth = frame.getToolkit().getScreenSize().width;
				int screenHeight = frame.getToolkit().getScreenSize().height;
				pointchooser.setLocation((screenWidth - dialogWidth) / 2,
						(screenHeight - dialogHeight) / 2);
				pointchooser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				pointchooser.setSize(dialogWidth, dialogHeight);
				pointchooser.setLayout(new MigLayout(""));
				JSpinner spinX = new JSpinner();
				SpinnerModel xModel = new SpinnerNumberModel(0, -100, 100, 0.1);
				xModel.setValue(0.1);
				spinX.setModel(xModel);
				JSpinner spinY = new JSpinner();
				SpinnerModel yModel = new SpinnerNumberModel(0, -100, 100, 0.1);
				yModel.setValue(0.1);
				spinY.setModel(yModel);

				String[] labels = { "x", "y" };
				pointchooser.add(new JLabel(labels[0]));
				pointchooser.add(spinX, "wrap");
				pointchooser.add(new JLabel(labels[1]));
				pointchooser.add(spinY, "wrap");

				JButton okButton = new JButton("OK");
				pointchooser.add(okButton);
				JButton cancelButton = new JButton("Cancel");
				pointchooser.add(cancelButton);

				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						xPoint = ((Number) spinX.getValue()).floatValue();
						yPoint = ((Number) spinY.getValue()).floatValue();
						XYSeries series = new XYSeries("Point " + pointCounter);
						series.add(xPoint, yPoint);
						chartPanel.xyseriescollection.addSeries(series);
						Color c = new Color(0);

						double del = A * xPoint * xPoint
								+ B * yPoint * yPoint + C
								* xPoint * yPoint + E * yPoint + D
								* xPoint + F1 + F2;

						if (del > 0) {
							c = (Color) chartPanel.xylineandshaperenderer
									.getSeriesPaint(0);

						}
						if (del < 0) {
							c = (Color) chartPanel.xylineandshaperenderer
									.getSeriesPaint(1);
						}

						chartPanel.xylineandshaperenderer
								.setSeriesLinesVisible(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1, false);
						chartPanel.xylineandshaperenderer
								.setSeriesShapesVisible(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1, true);
						chartPanel.xylineandshaperenderer.setSeriesPaint(
								chartPanel.xyseriescollection.getSeriesCount() - 1,
								c);
						chartPanel.xylineandshaperenderer.setSeriesShape(
								chartPanel.xyseriescollection.getSeriesCount() - 1,
								ShapeUtilities.createDiagonalCross(3, 3));
						chartPanel.xylineandshaperenderer
								.setSeriesOutlinePaint(
										chartPanel.xyseriescollection
												.getSeriesCount() - 1,
										Color.white);
						chartPanel.validate();
						pointCounter++;
						pointchooser.dispose();
					}
				});
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						pointchooser.dispose();

					}
				});
				pointchooser.setVisible(true);
			}
		});
		file.add(addPoint);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exit);

		JMenu dataSet1Option = new JMenu("Dataset 1");
		JMenu dataSet2Option = new JMenu("Dataset 2");
		bar.add(dataSet1Option);
		bar.add(dataSet2Option);
		JMenuItem loadItem = new JMenuItem("Load from a file");

		loadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataset1.loadFromFile();
				chartPanel.revalidate();
				chartPanel.revalidate();
				frame.validate();
				frame.repaint();
				chartPanel.validate();
				chartPanel.repaint();

				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}

			}
		});

		JMenuItem generateItem = new JMenuItem("Generate");
		generateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset1.generate(dataset1, dataset2, refershButton);
			}
		});

		JMenuItem loadItem2 = new JMenuItem("Load from a file");
		loadItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataset2.loadFromFile();
				frame.graphicPanel.revalidate();
				frame.repaint();
				chartPanel.revalidate();
				if (dataset1.data.length > 0 && dataset2.data.length > 0) {
					refershButton.setEnabled(true);
				}
			}
		});

		JMenuItem generateItem2 = new JMenuItem("Generate");
		generateItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataset2.generate(dataset1, dataset2, refershButton);

			}
		});

		dataSet1Option.add(loadItem);
		dataSet1Option.add(generateItem);
		dataSet2Option.add(loadItem2);
		dataSet2Option.add(generateItem2);

		JLabel s1Label = new JLabel("<html>S<sub>1</sub>=</html>");
		frame.controlPanel.add(s1Label, "wrap");
		frame.controlPanel.add(s00Field1);
		frame.controlPanel.add(s01Field1, "wrap");
		frame.controlPanel.add(s10Field1);
		frame.controlPanel.add(s11Field1, "wrap");

		JLabel s2Label = new JLabel("<html>S<sub>2</sub>=</html>");
		frame.controlPanel.add(s2Label, "wrap");
		frame.controlPanel.add(s00Field2);
		frame.controlPanel.add(s01Field2, "wrap");
		frame.controlPanel.add(s10Field2);
		frame.controlPanel.add(s11Field2, "wrap");

		JLabel wodLabel = new JLabel("<html>S<sub>1</sub><sup>-1</sup>=</html>");
		frame.controlPanel.add(wodLabel, "wrap");
		frame.controlPanel.add(s1od00Field);
		frame.controlPanel.add(s1od01Field, "wrap");
		frame.controlPanel.add(s1od10Field);
		frame.controlPanel.add(s1od11Field, "wrap");

		JLabel wLabel = new JLabel("<html>S<sub>2</sub><sup>-1</sup>=</html>");
		frame.controlPanel.add(wLabel, "wrap");
		frame.controlPanel.add(s2od00Field);
		frame.controlPanel.add(s2od01Field, "wrap");
		frame.controlPanel.add(s2od10Field);
		frame.controlPanel.add(s2od11Field, "wrap");

		JLabel eqLabel = new JLabel(
				"<html>Ax<sup>2</sup>+By<sup>2</sup>+Cxy+Dx+Ey+F=0</html> ");
		frame.controlPanel.add(eqLabel, "span 2 1, wrap");

		aField.setColumns(5);
		aField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel aLabel = new JLabel("A =");
		frame.controlPanel.add(aLabel);
		frame.controlPanel.add(aField, "wrap");

		bField.setColumns(5);
		bField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel bLabel = new JLabel("B =");
		frame.controlPanel.add(bLabel);
		frame.controlPanel.add(bField, "wrap");

		cField.setColumns(5);
		cField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel cLabel = new JLabel("C =");
		frame.controlPanel.add(cLabel);
		frame.controlPanel.add(cField, "wrap");

		dField.setColumns(5);
		dField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel dLabel = new JLabel("D =");
		frame.controlPanel.add(dLabel);
		frame.controlPanel.add(dField, "wrap");

		eField.setColumns(5);
		eField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel eLabel = new JLabel("E =");
		frame.controlPanel.add(eLabel);
		frame.controlPanel.add(eField, "wrap");

		fField.setColumns(5);
		fField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel fLabel = new JLabel("F =");
		frame.controlPanel.add(fLabel);
		frame.controlPanel.add(fField, "wrap");
		frame.validate();

	}

	public static void main(String[] args) throws IOException {

		new Main();

	}
}
