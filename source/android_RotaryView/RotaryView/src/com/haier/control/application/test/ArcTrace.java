package com.haier.control.application.test;

import android.graphics.Point;

public class ArcTrace {

	private double width;
	private double w;
	private double height;
	private double arc;
	private double arcDegree;

	private double cos;
	private double sin;
	private double radius;
	private Point translatePoint;

	public ArcTrace(int w, int h) {
		this.w = width = w;
		height = h;
		if (width >= h / 2) {
			this.w = height / 2;
		}
		compute();
	}

	public double getRadius() {
		return radius;
	}

	public double getSinArc() {
		return sin;
	}

	public double getCosArc() {
		return cos;
	}

	public Point getDevicePoint(double arcDegree) {
		double tempArc = arcDegree * Math.PI / 180;
		double x2 = radius * Math.cos(tempArc);
		double y2 = radius * Math.sin(tempArc);
		int x = (int) (x2 * Math.cos(arc) + y2 * Math.sin(arc) + translatePoint.x);
		int y = (int) (y2 * Math.cos(arc) - x2 * Math.sin(arc) + translatePoint.y);
		Point p = new Point(x, y);
		return p;
	}

	private void compute() {
		radius = (4 * w * w + height * height) / (8 * w);
		sin = ((double) height) / 2 / radius;
		cos = (radius - w) / radius;
		arc = Math.asin(sin);
		arcDegree = arc / Math.PI * 180;

		translatePoint = new Point();
		int x = -(int) (getRadius() * cos);
		int y = (int) (getRadius() * sin);
		translatePoint.set(x, y);
	}

	public double getArcDegree() {
		return 2 * arcDegree;
	}

	// private Point getTranslatePoint() {
	// return translatePoint;
	// }

	public static void main(String[] args) {
		ArcTrace at = new ArcTrace(100, 200);
		System.out.println("getArc=" + at.getArcDegree());
		System.out.println("getRadius=" + at.getRadius());
		System.out.println("getSinArc=" + at.getSinArc());
		System.out.println("getCosArc=" + at.getCosArc());
		System.out.println("sin + cos="
				+ (at.getSinArc() * at.getSinArc() + at.getCosArc()
						* at.getCosArc()));
		System.out.println(" current point===" + at.getDevicePoint(0));
		System.out.println(" current point===" + at.getDevicePoint(178));

		System.out.println("----------------------------------");

		at = new ArcTrace(6, 200);
		System.out.println("getArc=" + at.getArcDegree());
		System.out.println("getRadius=" + at.getRadius());
		System.out.println("getSinArc=" + at.getSinArc());
		System.out.println("getCosArc=" + at.getCosArc());
		System.out.println("sin + cos="
				+ (at.getSinArc() * at.getSinArc() + at.getCosArc()
						* at.getCosArc()));
	}
}
