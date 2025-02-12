package org.firstinspires.ftc.teamcode.Detection;

import android.graphics.Canvas;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@Config
public class PropDetectionRedClose implements VisionProcessor {

    public int detection = 1;
    //centru
    public static int rightRectX1 = 700, rightRectY1 = 330;
    public static int rightRectX2 = 800, rightRectY2 = 530;

    public static double rightThresh = 1200000;
    public double rightSum = 0;
    //dreapta
    public static int middleRectX1 = 300, middleRectY1 = 330;
    public static int middleRectX2 = 400, middleRectY2 = 530;

    public static double middleThresh = 900000;
    public double middleSum = 0;

    public static int redLowH = 110, redLowS = 160, redLowV = 0;
    public static int redHighH = 125, redHighS = 255, redHighV = 255;

    Mat workingMat = new Mat();

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, workingMat, Imgproc.COLOR_BGR2HSV);

        Rect rightRect = new Rect(new Point(rightRectX1, rightRectY1), new Point(rightRectX2, rightRectY2));
        Rect middleRect = new Rect(new Point(middleRectX1, middleRectY1), new Point(middleRectX2, middleRectY2));

        Scalar lowThresh = new Scalar(redLowH, redLowS, redLowV);
        Scalar highThresh = new Scalar(redHighH, redHighS, redHighV);

        Core.inRange(workingMat, lowThresh, highThresh, workingMat);

        rightSum = Core.sumElems(workingMat.submat(rightRect)).val[0];
        middleSum = Core.sumElems(workingMat.submat(middleRect)).val[0];

        Imgproc.rectangle(frame, rightRect, new Scalar(0,255,0), 5);
        Imgproc.rectangle(frame, middleRect, new Scalar(0,255,0), 5);

        if(rightSum > rightThresh)
            detection = 3;
        else if (middleSum > middleThresh)
            detection = 2;
        else detection = 1;

//        workingMat.copyTo(frame);

        workingMat.release();

        return detection;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext){

  }
}
