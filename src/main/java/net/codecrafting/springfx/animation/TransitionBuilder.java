package net.codecrafting.springfx.animation;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class TransitionBuilder 
{
	private Duration duration;
	
	public TransitionBuilder(Duration duration)
	{
		this.duration = duration;
	}
	
	public Duration getDuration() 
	{
		return duration;
	}

	public void setDuration(Duration duration) 
	{
		this.duration = duration;
	}

	public TranslateTransition getSlideTransition(final Node node, double fromX, double fromY, double toX, double toY)
	{
		TranslateTransition slide = new TranslateTransition(duration, node);
		slide.setFromX(fromX);
		slide.setFromY(fromY);
		slide.setToX(toX);
		slide.setToY(toY);
		return slide;
	}
	
	public TranslateTransition getSlideTransitionPerc(final Node node, double fromX, double fromY, double toX, double toY)
	{
		double w,h;
		TranslateTransition slide = new TranslateTransition(duration, node);
		if(!node.isResizable()) {
			w = node.getBoundsInLocal().getWidth();
			h = node.getBoundsInLocal().getHeight();
		} else {
			w = node.getLayoutBounds().getWidth();
			h = node.getLayoutBounds().getHeight();
		}
		if(w == 0) {
			w = 250;
			fromX = 1;
			toX = 1;
		}
		if(h == 0) {
			h = 250;
			fromY = 1;
			toY = 1;
		}
		slide.setFromX(fromX*w);
		slide.setFromY(fromY*h);
		slide.setToX(toX*w);
		slide.setToY(toY*h);
		return slide;		
	}
	
	public FadeTransition getFadeTransition(final Node node, double start, double end)
	{
		FadeTransition fade = new FadeTransition(duration, node);
		fade.setToValue(end);
		fade.setFromValue(start);
        return fade;
	}
	
	public ScaleTransition getScaleTransition(final Node node, double fromX, double fromY, double toX, double toY)
	{
		ScaleTransition scale = new ScaleTransition(duration, node);
		scale.setFromX(fromX);
		scale.setFromY(fromY);
		scale.setToX(toX);
		scale.setToY(toY);
		return scale;
	}
	
	public ScaleTransition getScaleTransitionPerc(final Node node, double fromX, double fromY, double toX, double toY)
	{
		double w,h;
		ScaleTransition scale = new ScaleTransition(duration, node);
		if(!node.isResizable()) {
			w = node.getBoundsInLocal().getWidth();
			h = node.getBoundsInLocal().getHeight();
		} else {
			w = node.getLayoutBounds().getWidth();
			h = node.getLayoutBounds().getHeight();	
		}
		if(w == 0) {
			w = 250;
			fromX = 1;
			toX = 1;
		}
		if(h == 0) {
			h = 250;
			fromY = 1;
			toY = 1;
		}
		scale.setFromX(fromX*w);
		scale.setFromY(fromY*h);
		scale.setToX(toX*w);
		scale.setToY(toY*h);
		return scale;
	}
	
	public SizeTransition getSizeTransitionTo(final Region region, double toWidth, double toHeight)
	{
		double w,h;
		if(!region.isResizable()) {
			w = region.getBoundsInLocal().getWidth();
			h = region.getBoundsInLocal().getHeight();
		} else {
			w = region.getLayoutBounds().getWidth();
			h = region.getLayoutBounds().getHeight();	
		}
		if(w == 0) w = 250;
		if(h == 0) h = 250;
		return getSizeTransition(region, w, toWidth, h, toHeight);
	}	
	
	public SizeTransition getSizeTransition(final Region region, double fromWidth, double toWidth, double fromHeight, double toHeight)
	{
		SizeTransition size = new SizeTransition(duration, region);
		size.setFromWidth(fromWidth);
		size.setFromHeight(fromHeight);
		size.setToWidth(toWidth);
		size.setToHeight(toHeight);
		return size;
	}
	
	public RotateTransition getRotateTransition(final Node node, double angle)
	{
		return getRotateTransition(node, angle, new Point3D(0, 0, 1));
	}
	
	public RotateTransition getRotateTransition(final Node node, double angle, Point3D axis)
	{
		RotateTransition rotate = new RotateTransition(duration, node);
		rotate.setToAngle(angle);
		rotate.setAxis(axis);
		return rotate;
	}
	
	public SkewTransition getSkewTransiotion(final Node node, double fromXAngle, double fromYAngle, double toXAngle, double toYAngle)
	{
		SkewTransition skew = new SkewTransition(duration, node);
		skew.setFromXAngle(fromXAngle);
		skew.setFromYAngle(fromYAngle);
		skew.setToXAngle(toXAngle);
		skew.setToYAngle(toYAngle);
		return skew;		
	}
}
