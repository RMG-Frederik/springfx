/*
 * Copyright 2018 Lucas Lara Marotta
 * Copyright 2018-2019 The SpringFX Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.animation;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class AnimationBuilder 
{
	public static final Duration DEFAULT_DURATION = Duration.millis(1000);
	public static final Duration DEFAULT_DELAY = Duration.ZERO;
	public static final Interpolator DEFAULT_INTERPOLATOR = EaseInterpolator.LINEAR;
	public static final double DEFAULT_PERCENTAGE = 0.35;
	
	private Duration delay;
	private double percentage;
	private Interpolator interpolator;
	private final TransitionBuilder transitionBuilder = new TransitionBuilder(DEFAULT_DURATION);
	
	public AnimationBuilder()
	{
		delay = DEFAULT_DELAY;
		percentage = DEFAULT_PERCENTAGE;
		interpolator = DEFAULT_INTERPOLATOR;
	}
	
	public AnimationBuilder(Duration duration)
	{
		this(duration, DEFAULT_DELAY);
	}
	
	public AnimationBuilder(Duration duration, Duration delay)
	{
		transitionBuilder.setDuration(duration);
		percentage = DEFAULT_PERCENTAGE;
		interpolator = DEFAULT_INTERPOLATOR;
		this.delay = delay;
	}
	
	public AnimationBuilder(double duration)
	{
		this(duration, DEFAULT_DELAY.toSeconds());
	}
	
	public AnimationBuilder(double duration, double delay)
	{
		transitionBuilder.setDuration(Duration.seconds(duration));
		percentage = DEFAULT_PERCENTAGE;
		interpolator = DEFAULT_INTERPOLATOR;
		this.delay = Duration.seconds(delay);
	}
	
	public TransitionBuilder getTransitionBuilder()
	{
		return transitionBuilder;
	}
	
	public Duration getDuration()
	{
		return transitionBuilder.getDuration();
	}
	
	public AnimationBuilder duration(final Duration duration)
	{
		transitionBuilder.setDuration(duration);
		return this;
	}
	
	public AnimationBuilder duration(final double duration)
	{
		transitionBuilder.setDuration(Duration.seconds(duration));
		return this;
	}
	
	public AnimationBuilder delay(final Duration delay)
	{
		this.delay = delay;
		return this;
	}
	
	public AnimationBuilder delay(final double delay)
	{
		this.delay = Duration.seconds(delay);
		return this;
	}
	
	public AnimationBuilder percentage(double percentage)
	{
		this.percentage = percentage;
		return this;
	}
	
	public AnimationBuilder interpolator(final Interpolator interpolator)
	{
		this.interpolator = interpolator;
		return this;
	}
	
	public AnimationNode custom(Transition custom)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().add(custom);
		return new AnimationNode(transition);
	}
	
	/* ================================================================== */
	//	FADE ANIMATIONS
	/* ================================================================== */
	
	public AnimationNode fadeIn(final Node node)
	{
		return custom(transitionBuilder.getFadeTransition(node, 0, 1));
	}
	
	public AnimationNode fadeOut(final Node node)
	{
		return custom(transitionBuilder.getFadeTransition(node, 1, 0));
	}
	
	public AnimationNode fadeInUp(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 0, 1), transitionBuilder.getSlideTransitionPerc(node, 0, -percentage, 0, 0));
		return new AnimationNode(transition);
	}
	
	public AnimationNode fadeInDown(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 0, 1), transitionBuilder.getSlideTransitionPerc(node, 0, percentage, 0, 0));
		return new AnimationNode(transition);
	}
	
	public AnimationNode fadeInRight(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 0, 1), transitionBuilder.getSlideTransitionPerc(node, percentage, 0, 0, 0));
		return new AnimationNode(transition);		
	}
	
	public AnimationNode fadeInLeft(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 0, 1), transitionBuilder.getSlideTransitionPerc(node, -percentage, 0, 0, 0));
		return new AnimationNode(transition);		
	}
	
	public AnimationNode fadeOutUp(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 1, 0), transitionBuilder.getSlideTransitionPerc(node, 0, 0, 0, -percentage));
		return new AnimationNode(transition);
	}
	
	public AnimationNode fadeOutDown(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 1, 0), transitionBuilder.getSlideTransitionPerc(node, 0, 0, 0, percentage));
		return new AnimationNode(transition);
	}
	
	public AnimationNode fadeOutRight(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 1, 0), transitionBuilder.getSlideTransitionPerc(node, 0, 0, percentage, 0));
		return new AnimationNode(transition);		
	}
	
	public AnimationNode fadeOutLeft(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 1, 0), transitionBuilder.getSlideTransitionPerc(node, 0, 0, -percentage, 0));
		return new AnimationNode(transition);		
	}
	
	/* ================================================================== */
	//	SCALE ANIMATIONS
	/* ================================================================== */	
	
	public AnimationNode scaleUp(final Node node)
	{
		return custom(transitionBuilder.getScaleTransition(node, percentage, percentage, 1, 1));
	}
	
	public AnimationNode scaleDown(final Node node)
	{
		return custom(transitionBuilder.getScaleTransition(node, 1, 1, percentage, percentage));
	}
	
	public AnimationNode zoomIn(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 0, 1), transitionBuilder.getScaleTransition(node, percentage, percentage, 1, 1));
		return new AnimationNode(transition);
	}
	
	public AnimationNode zoomOut(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		transition.getChildren().addAll(transitionBuilder.getFadeTransition(node, 1, 0), transitionBuilder.getScaleTransition(node, 1, 1, percentage, percentage));
		return new AnimationNode(transition);
	}
	
	/* ================================================================== */
	//	SKEW ANIMATIONS
	/* ================================================================== */
	
	public AnimationNode skew(final Node node, double fromXAngle, double fromYAngle, double toXAngle, double toYAngle)
	{
		return custom(transitionBuilder.getSkewTransiotion(node, fromXAngle, fromYAngle, toXAngle, toYAngle));
	}
	
	/* ================================================================== */
	//	SIZE ANIMATIONS
	/* ================================================================== */
	
	public AnimationNode sizeTo(final Region region, double toWidth, double toHeight)
	{
		return custom(transitionBuilder.getSizeTransitionTo(region, toWidth, toHeight));
	}
	
	/* ================================================================== */
	//	SLIDE ANIMATIONS
	/* ================================================================== */
	
	public AnimationNode slideInUp(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, percentage, 0, 0));
	}
	
	public AnimationNode slideInDown(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, -percentage, 0, 0));
	}
	
	public AnimationNode slideInRight(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, percentage, 0, 0, 0));
	}
	
	public AnimationNode slideInLeft(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, -percentage, 0, 0, 0));
	}
	
	public AnimationNode slideOutUp(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, 0, 0, -percentage));
	}
	
	public AnimationNode slideOutDown(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, 0, 0, percentage));
	}
	
	public AnimationNode slideOutRight(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, 0, -percentage, 0));
	}
	
	public AnimationNode slideOutLeft(final Node node)
	{
		return custom(transitionBuilder.getSlideTransitionPerc(node, 0, 0, percentage, 0));
	}
	
	/* ================================================================== */
	//	SPECIAL ANIMATIONS
	/* ================================================================== */
	
	public AnimationNode flash(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		FadeTransition flash = transitionBuilder.getFadeTransition(node, 0, 1);
		flash.setCycleCount(5);
		flash.setAutoReverse(true);
		transition.getChildren().add(flash);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode loop(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		RotateTransition loop = transitionBuilder.getRotateTransition(node, 360);
		loop.setInterpolator(EaseInterpolator.LINEAR);
		loop.setCycleCount(Transition.INDEFINITE);
		transition.getChildren().add(loop);
		return new AnimationNode(transition);
	}

	public AnimationNode pulse(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		ScaleTransition pulse = transitionBuilder.getScaleTransition(node, 1, 1, 1.1, 1.1);
		pulse.setCycleCount(4);
		pulse.setAutoReverse(true);
		transition.getChildren().add(pulse);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode rubberBand(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		Duration curDuration = transitionBuilder.getDuration();
		transitionBuilder.setDuration(Duration.millis(curDuration.toMillis()/6));
		ScaleTransition t1 = transitionBuilder.getScaleTransition(node, 1, 1, 1.25, 0.75);
		ScaleTransition t2 = transitionBuilder.getScaleTransition(node, 1.25, 0.75, 0.75, 1.25);
		ScaleTransition t3 = transitionBuilder.getScaleTransition(node, 0.75, 1.25, 1.15, 0.85);
		ScaleTransition t4 = transitionBuilder.getScaleTransition(node, 1.15, 0.85, 0.95, 1.05);
		ScaleTransition t5 = transitionBuilder.getScaleTransition(node, 0.95, 1.05, 1.05, 0.95);
		ScaleTransition t6 = transitionBuilder.getScaleTransition(node, 1.05, 0.95, 1, 1);
		transitionBuilder.setDuration(curDuration);
		sequential.getChildren().addAll(t1, t2, t3, t4, t5, t6);
		transition.getChildren().addAll(sequential);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode shake(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		Duration curDuration = transitionBuilder.getDuration();
		transitionBuilder.setDuration(Duration.millis(curDuration.toMillis()/16.667));
		TranslateTransition t1 = transitionBuilder.getSlideTransition(node, 0, 0, 10, 0);
		TranslateTransition t2 = transitionBuilder.getSlideTransition(node, 10, 0, 0, 0);
		TranslateTransition t3 = transitionBuilder.getSlideTransition(node, 0, 0, -10, 0);
		TranslateTransition t4 = transitionBuilder.getSlideTransition(node, -10, 0, 0, 0);
		transitionBuilder.setDuration(curDuration);
		sequential.getChildren().addAll(t1, t2, t3, t4);
		transition.getChildren().addAll(sequential);
		transition.setCycleCount(3);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}

	public AnimationNode swing(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		Duration curDuration = transitionBuilder.getDuration();
		transitionBuilder.setDuration(Duration.millis(curDuration.toMillis()/5));
		RotateTransition t1 = transitionBuilder.getRotateTransition(node, 15);
		RotateTransition t2 = transitionBuilder.getRotateTransition(node, -10);
		RotateTransition t3 = transitionBuilder.getRotateTransition(node, 5);
		RotateTransition t4 = transitionBuilder.getRotateTransition(node, -5);
		RotateTransition t5 = transitionBuilder.getRotateTransition(node, 0);
		transitionBuilder.setDuration(curDuration);
		sequential.getChildren().addAll(t1, t2, t3, t4, t5);
		transition.getChildren().addAll(sequential);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode tada(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		Duration curDuration = transitionBuilder.getDuration();
		transitionBuilder.setDuration(Duration.millis(curDuration.toMillis()/10));
		ParallelTransition t1 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1, 1, 0.9, 0.9), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t2 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 0.9, 0.9, 0.9, 0.9), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t3 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 0.9, 0.9, 1.1, 1.1), transitionBuilder.getRotateTransition(node, 3));
		ParallelTransition t4 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t5 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, 3));
		ParallelTransition t6 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t7 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, 3));
		ParallelTransition t8 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t9 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.1, 1.1), transitionBuilder.getRotateTransition(node, 3));
		ParallelTransition t10 = new ParallelTransition(transitionBuilder.getScaleTransition(node, 1.1, 1.1, 1.0, 1.0), transitionBuilder.getRotateTransition(node, 0));
		transitionBuilder.setDuration(curDuration);
		sequential.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
		transition.getChildren().addAll(sequential);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode wobble(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		Duration curDuration = transitionBuilder.getDuration();
		transitionBuilder.setDuration(Duration.millis(curDuration.toMillis()/6));
		ParallelTransition t1 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, 0, 0, -0.25, 0.0), transitionBuilder.getRotateTransition(node, -5));
		ParallelTransition t2 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, -0.25, 0, 0.20, 0.0), transitionBuilder.getRotateTransition(node, 3));
		ParallelTransition t3 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, 0.20, 0.0, -0.15, 0.0), transitionBuilder.getRotateTransition(node, -3));
		ParallelTransition t4 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, -0.15, 0.0, 0.10, 0.0), transitionBuilder.getRotateTransition(node, 2));
		ParallelTransition t5 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, 0.10, 0.0, -0.05, 0.0), transitionBuilder.getRotateTransition(node, -1));
		ParallelTransition t6 = new ParallelTransition(transitionBuilder.getSlideTransitionPerc(node, -0.05, 0.0, 0.0, 0.0), transitionBuilder.getRotateTransition(node, 0));
		transitionBuilder.setDuration(curDuration);
		sequential.getChildren().addAll(t1, t2, t3, t4, t5, t6);
		transition.getChildren().addAll(sequential);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	public AnimationNode jello(final Node node)
	{
		ParallelTransition transition = getBaseTransition();
		SequentialTransition sequential = new SequentialTransition();
		final Duration curDuration = transitionBuilder.getDuration();
		
		SkewTransition t1 = transitionBuilder.getSkewTransiotion(node, 0.0, 0.0, -12.5, -12.5);
		t1.durationProperty().set(curDuration.multiply(0.222));
		SkewTransition t2 = transitionBuilder.getSkewTransiotion(node, -12.5, -12.5, 6.25, 6.25);
		t2.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t3 = transitionBuilder.getSkewTransiotion(node, 6.25, 6.25, -3.125, -3.125);
		t3.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t4 = transitionBuilder.getSkewTransiotion(node, -3.125, -3.125, 1.5625, 1.5625);
		t4.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t5 = transitionBuilder.getSkewTransiotion(node, 1.5625, 1.5625, -0.78125, -0.78125);
		t5.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t6 = transitionBuilder.getSkewTransiotion(node, -0.78125, -0.78125, 0.390625, 0.390625);
		t6.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t7 = transitionBuilder.getSkewTransiotion(node, 0.390625, 0.390625, -0.1953125, -0.1953125);
		t7.durationProperty().set(curDuration.multiply(0.1111));
		SkewTransition t8 = transitionBuilder.getSkewTransiotion(node, -0.1953125, -0.1953125, 0.0, 0.0);
		t8.durationProperty().set(curDuration.multiply(0.1134));
		
		sequential.getChildren().addAll(t1, t2, t3, t4, t5, t6, t7, t8);
		transition.getChildren().addAll(sequential);
		transition.setInterpolator(DEFAULT_INTERPOLATOR);
		return new AnimationNode(transition);
	}
	
	private ParallelTransition getBaseTransition()
	{
		ParallelTransition transition = new ParallelTransition();
		transition.setInterpolator(interpolator);
		transition.setDelay(delay);
		return transition;
	}
	
}
