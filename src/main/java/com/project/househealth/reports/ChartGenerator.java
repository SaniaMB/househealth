package com.project.househealth.reports;

import com.lowagie.text.Image;
import com.project.househealth.entity.HealthLog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ChartGenerator {

    public Image createBloodPressureChart(
            List<HealthLog> logs
    ) throws Exception {

        XYSeries systolic =
                new XYSeries("Systolic");

        XYSeries diastolic =
                new XYSeries("Diastolic");

        int x = 1;

        // oldest -> newest
        for (int i = logs.size() - 1; i >= 0; i--) {

            HealthLog log = logs.get(i);

            systolic.add(
                    x,
                    log.getSystolic()
            );

            diastolic.add(
                    x,
                    log.getDiastolic()
            );

            x++;
        }

        XYSeriesCollection dataset =
                new XYSeriesCollection();

        dataset.addSeries(systolic);
        dataset.addSeries(diastolic);

        JFreeChart chart =
                ChartFactory.createXYLineChart(
                        null,
                        null,
                        null,
                        dataset
                );

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot =
                chart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setOutlineVisible(false);

        plot.setDomainGridlinesVisible(false);

        plot.setRangeGridlinesVisible(false);

        plot.setInsets(
                new RectangleInsets(
                        2,
                        2,
                        2,
                        2
                )
        );

        NumberAxis domain =
                (NumberAxis) plot.getDomainAxis();

        domain.setVisible(false);

        NumberAxis range =
                (NumberAxis) plot.getRangeAxis();

        range.setVisible(false);

        XYLineAndShapeRenderer renderer =
                new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(
                0,
                new Color(61,181,98)
        );

        renderer.setSeriesPaint(
                1,
                new Color(37,99,235)
        );

        renderer.setSeriesStroke(
                0,
                new BasicStroke(2.3f)
        );

        renderer.setSeriesStroke(
                1,
                new BasicStroke(2.3f)
        );

        renderer.setSeriesShapesVisible(
                0,
                false
        );

        renderer.setSeriesShapesVisible(
                1,
                false
        );

        plot.setRenderer(renderer);

        BufferedImage image =
                chart.createBufferedImage(
                        260,
                        90
                );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(
                image,
                "png",
                output
        );

        Image pdfImage =
                Image.getInstance(
                        output.toByteArray()
                );

        pdfImage.scaleToFit(
                220,
                80
        );

        return pdfImage;

    }

    public Image createSugarChart(
            List<HealthLog> logs
    ) throws Exception {

        XYSeries sugar =
                new XYSeries("Sugar");

        int x = 1;

        // oldest -> newest
        for (int i = logs.size() - 1; i >= 0; i--) {

            HealthLog log = logs.get(i);

            sugar.add(
                    x,
                    log.getSugarValue()
            );

            x++;
        }

        XYSeriesCollection dataset =
                new XYSeriesCollection();

        dataset.addSeries(sugar);

        JFreeChart chart =
                ChartFactory.createXYLineChart(
                        null,
                        null,
                        null,
                        dataset
                );

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot =
                chart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setOutlineVisible(false);

        plot.setDomainGridlinesVisible(false);

        plot.setRangeGridlinesVisible(false);

        plot.setInsets(
                new RectangleInsets(
                        2,
                        2,
                        2,
                        2
                )
        );

        NumberAxis domain =
                (NumberAxis) plot.getDomainAxis();

        domain.setVisible(false);

        NumberAxis range =
                (NumberAxis) plot.getRangeAxis();

        range.setVisible(false);

        XYLineAndShapeRenderer renderer =
                new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(
                0,
                new Color(61,181,98)
        );

        renderer.setSeriesStroke(
                0,
                new BasicStroke(2.5f)
        );

        renderer.setSeriesShapesVisible(
                0,
                false
        );

        plot.setRenderer(renderer);

        BufferedImage image =
                chart.createBufferedImage(
                        260,
                        90
                );

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        ImageIO.write(
                image,
                "png",
                output
        );

        Image pdfImage =
                Image.getInstance(
                        output.toByteArray()
                );

        pdfImage.scaleToFit(
                220,
                80
        );

        return pdfImage;

    }
}
