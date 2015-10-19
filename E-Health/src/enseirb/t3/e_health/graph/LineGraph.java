package enseirb.t3.e_health.graph;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class LineGraph {

	private GraphicalView view;
	private TimeSeries dataset;
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYSeriesRenderer renderer = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	private float textSize = 40;
	private float titleTextSize = 60;
	private int window = 9;

	public LineGraph(String dataname) {
		String xTitle = "Time";
		String yTitle = null;
		String chartTitle = null;

		switch (dataname) {
		case "A":
			yTitle = "mL/s";
			chartTitle = "Airflow";
			break;
		case "B":
			yTitle = "Bpm";
			chartTitle = "Pouls";
			break;
		case "C":
			yTitle = "µS";
			chartTitle = "Conductance";
			break;
		case "O":
			yTitle = "%";
			chartTitle = "SPO2";
			break;
		case "P":
			yTitle = "Position";
			chartTitle = "Position";
			break;
		case "R":
			yTitle = "Ohm";
			chartTitle = "Resistance";
			break;
		case "T":
			yTitle = "°C";
			chartTitle = "Temperature";
			break;
		default:
			break;
		}
		
		dataset = new TimeSeries(chartTitle + " " + yTitle);
		
		mDataset.addSeries(dataset);

		renderer.setColor(Color.RED);
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setLineWidth(3);
		renderer.setDisplayChartValues(true);
		mRenderer.setXLabels(window);
		mRenderer.setChartTitle(chartTitle);
		mRenderer.setAxisTitleTextSize(titleTextSize);
		mRenderer.setChartTitleTextSize(titleTextSize);
		mRenderer.setXTitle(xTitle);
		mRenderer.setYTitle(yTitle);
		mRenderer.setLabelsColor(Color.rgb(44, 196, 196));
		mRenderer.setLegendTextSize(textSize);
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setAxisTitleTextSize(textSize);
		mRenderer.setLabelsTextSize(textSize);
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setLegendHeight(50);
	    mRenderer.setMargins(new int[]{0, 70, 80, 70});
		mRenderer.addSeriesRenderer(renderer);
	}

	public GraphicalView getView(Context context) {
		view = ChartFactory.getTimeChartView(context, mDataset, mRenderer,
				"H:mm:ss");
		return view;
	}

	public XYMultipleSeriesRenderer getmRenderer() {
		return mRenderer;
	}

	public XYMultipleSeriesDataset getDataset() {
		return mDataset;
	}

	public void addNewPoint(Point p) {
		dataset.add(p.getX(), p.getY());
	}

	public void removePoint(int i) {
		dataset.remove(i);
	}
	public void setWindow(int window){
		this.window=window;
	}
}
