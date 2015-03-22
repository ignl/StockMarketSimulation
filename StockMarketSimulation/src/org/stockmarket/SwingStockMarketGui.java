package org.stockmarket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.cellular.gui.WorldGui;
import org.cellular.twodimensional.XYCoordinates;
import org.cellular.world.World;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;
import org.stockmarket.StockMarketWorld.Candle;

public class SwingStockMarketGui implements WorldGui<Action> {


    private boolean init = false;

    private JFrame mainFrame;

    private List<OHLCDataItem> data;

    private Date date = new Date();

    private JButton[][] cells;

    private JFreeChart chart;

    @Override
    public void showWorld(final World<Action> world) {
        final StockMarketWorld stockMarket = (StockMarketWorld) world;
        final int height = stockMarket.getHeight();
        final int lenght = stockMarket.getLenght();
        if (!init) {
            initGui(stockMarket);
            init = true;
        } else {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < lenght; x++) {
                    final MarketParticipant marketParticipant = (MarketParticipant) stockMarket.getCell(new XYCoordinates(x, y));
                    final Action value = marketParticipant.getValue();
                    if (value == Action.SELL) {
                        cells[x][y].setBackground(Color.RED);
                    } else if (value == Action.BUY) {
                        cells[x][y].setBackground(Color.GREEN);
                    } else {
                        cells[x][y].setBackground(Color.GRAY);
                    }
                    cells[x][y].setText("<html>Capital: " + marketParticipant.getTotalCapital() + " <br/>" + "Shares: "
                            + marketParticipant.getSharesHold() + "</html>");
                }
            }
            chart.getXYPlot().setDataset(addNewCandleAndGetDataSet(stockMarket.getLastCandle()));
        }

    }

    private void initGui(final StockMarketWorld stockMarket) {
        final int height = stockMarket.getHeight();
        final int lenght = stockMarket.getLenght();
        cells = new JButton[lenght][height];
        mainFrame = new JFrame("Stock market simulation");
        mainFrame.setLayout(new GridLayout(2, 1));
        mainFrame.setResizable(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Toolkit kit = Toolkit.getDefaultToolkit();
        final Insets insets = kit.getScreenInsets(mainFrame.getGraphicsConfiguration());
        final Dimension screen = kit.getScreenSize();
        mainFrame.setSize((int) (screen.getWidth() - insets.left - insets.right), (int) (screen.getHeight() - insets.top - insets.bottom));
        mainFrame.setLocation(insets.left, insets.top);

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.setVisible(true);

        // add cells as a buttons
        final JPanel cellsBoard = new JPanel(new GridLayout(height, lenght));

        cellsBoard.setBorder(new LineBorder(Color.BLACK));
        mainFrame.add(cellsBoard);

        // add buttons
        final Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < lenght; x++) {
                final MarketParticipant marketParticipant = (MarketParticipant) stockMarket.getCell(new XYCoordinates(x, y));
                final Action value = marketParticipant.getValue();
                final JButton b = new JButton();
                b.setMargin(buttonMargin);
                final ImageIcon icon = new ImageIcon(new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                
                if (value == Action.SELL) {
                    b.setBackground(Color.RED);
                } else if (value == Action.BUY) {
                    b.setBackground(Color.GREEN);
                } else {
                    b.setBackground(Color.GRAY);
                }
                
                cells[x][y] = b;
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < lenght; x++) {
                cellsBoard.add(cells[y][x]);
            }
        }

        // add chart
        final OHLCDataset ds = createDataset();
        chart = ChartFactory.createCandlestickChart("Market chart", "Iteration", "Value", ds, true);
        chart.setBackgroundPaint(Color.WHITE);

        final XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE); // light yellow = new Color(0xffffe0)
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setDomainPannable(true);
        final NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
        numberAxis.setAutoRangeIncludesZero(false);

        final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");
        dateAxis.setDateFormatOverride(formatter);

        ((CandlestickRenderer) plot.getRenderer()).setDrawVolume(false);
        ((CandlestickRenderer) plot.getRenderer()).setAutoWidthMethod(1);

        final ChartPanel cp = new ChartPanel(chart);
        mainFrame.getContentPane().add(cp);
    }

    private OHLCDataset createDataset() {
        data = new ArrayList<OHLCDataItem>();
        final OHLCDataItem[] dataArray = new OHLCDataItem[] {};
        final OHLCDataset dataset = new DefaultOHLCDataset("Market", dataArray);
        return dataset;
    }

    private OHLCDataset addNewCandleAndGetDataSet(final Candle candle) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        date = cal.getTime();
        data.add(new OHLCDataItem(date, candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose(), 0));

        final OHLCDataItem[] dataArray = data.toArray(new OHLCDataItem[data.size()]);
        final OHLCDataset dataset = new DefaultOHLCDataset("Market", dataArray);
        return dataset;
    }
}
