package org.usfirst.frc.team1089.vision;

import edu.wpi.cscore.*;
import edu.wpi.first.networktables.NetworkTable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

/**
 * Creates a thread capable of processing images from a camera and getting contours.
 *
 * This class also contains a local counter for the number of threads,
 * and handles assigning the ports to the needed {@link edu.wpi.cscore.MjpegServer MjpegServer}s
 */
public class VisionTask implements Runnable {
    private final CvSource RESTREAM_SOURCE;
    private final CvSink IMG_SINK;
    private final Mat IMG;
    private final NetworkTable TABLE;
    private final Pipeline<Mat, ArrayList<MatOfPoint>> PIPELINE;
    private final ArrayList<MatOfPoint> CONTOURS;

    /**
     * Creates a new {@code VisionTask}
     * @param restreamSource  the CvSource to output marked up mats for restreaming
     * @param imgSink         the CvSink to pull mats from
     * @param contourPipeline the pipeline to use for processing
     * @param table           the NetworkTable to deploy values to
     */
    public VisionTask(CvSource restreamSource, CvSink imgSink, Pipeline<Mat, ArrayList<MatOfPoint>> contourPipeline, NetworkTable table) {
        RESTREAM_SOURCE = restreamSource;
        IMG_SINK = imgSink;
        PIPELINE = contourPipeline;
        TABLE = table;

        IMG = new Mat();
        CONTOURS = new ArrayList<MatOfPoint>();
    }

    public void run() {
        while (!Thread.interrupted()) {
            // Grab a frame. If it has a frame time of 0, the request timed out.
            // So we just skip the frame and continue instead.
            if (IMG_SINK.grabFrame(IMG) == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + IMG_SINK.getError());
                continue;
            }
            // Process the image.
            PIPELINE.process(IMG);
            }

            // Regardless of whether or not we see anything, we still want to see
            // what we consider the center of the screen.

            // Pass values to network table

            // then release the resources within the mat.

            RESTREAM_SOURCE.putFrame(IMG);
            IMG.release();
            RESTREAM_SOURCE.free();
            IMG_SINK.free();
    }
}
