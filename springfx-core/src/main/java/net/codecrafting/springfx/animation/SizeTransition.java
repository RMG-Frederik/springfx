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

import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public final class SizeTransition extends Transition 
{
    /**
     * The target node of this {@code Transition}.
     * <p>
     * It is not possible to change the target {@code node} of a running
     * {@code SizeTransition}. If the value of {@code node} is changed for a
     * running {@code SizeTransition}, the animation has to be stopped and
     * started again to pick up the new value.
     */
    private ObjectProperty<Region> region;
    private static final Region DEFAULT_REGION = null;
    private Region cachedRegion;
    
    private double startW = Double.NaN;
    private double startH = Double.NaN;
    private double deltaW = Double.NaN;
    private double deltaH = Double.NaN;
    
    /**
     * The duration of this {@code SizeTransition}.
     * <p>
     * It is not possible to change the {@code duration} of a running
     * {@code SizeTransition}. If the value of {@code duration} is changed for a
     * running {@code SizeTransition}, the animation has to be stopped and
     * started again to pick up the new value.
     * <p>
     * Note: While the unit of {@code duration} is a millisecond, the
     * granularity depends on the underlying operating system and will in
     * general be larger. For example animations on desktop systems usually run
     * with a maximum of 60fps which gives a granularity of ~17 ms.
     *
     * Setting duration to value lower than {@link Duration#ZERO} will result
     * in {@link IllegalArgumentException}.
     *
     * @defaultValue 400ms
     */
    private ObjectProperty<Duration> duration;
    private static final Duration DEFAULT_DURATION = Duration.millis(400);
    
    /**
     * Specifies the start width value for this {@code SizeTransition}.
     * <p>
     * It is not possible to change {@code fromWidth} of a running
     * {@code SizeTransition}. If the value of {@code fromAngle} is changed
     * for a running {@code SizeTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty fromWidth;
    private static final double DEFAULT_FROM_WIDTH = Double.NaN;
    
    /**
     * Specifies the start height value for this {@code SizeTransition}.
     * <p>
     * It is not possible to change {@code fromHeight} of a running
     * {@code SizeTransition}. If the value of {@code Angle} is changed
     * for a running {@code SizeTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty fromHeight;
    private static final double DEFAULT_FROM_HEIGHT = Double.NaN;
    
    /**
     * Specifies the stop width value for this {@code SizeTransition}.
     * <p>
     * It is not possible to change {@code toWidth} of a running
     * {@code SizeTransition}. If the value of {@code toXAngle} is changed
     * for a running {@code SizeTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty toWidth;
    private static final double DEFAULT_TO_WIDTH = Double.NaN;
    
    /**
     * Specifies the stop height value for this {@code SizeTransition}.
     * <p>
     * It is not possible to change {@code toHeight} of a running
     * {@code SizeTransition}. If the value of {@code toYAngle} is changed
     * for a running {@code SizeTransition}, the animation has to be stopped
     * and started again to pick up the new value.
     *
     * @defaultValue {@code Double.NaN}
     */
    private DoubleProperty toHeight;
    private static final double DEFAULT_TO_HEIGHT = Double.NaN;
	
	public SizeTransition()
	{
		this(DEFAULT_DURATION, null);
	}
	
	public SizeTransition(Duration duration)
	{
		this(duration, null);
	}
    
	public SizeTransition(Duration duration, Region node)
	{
        setDuration(duration);
        setRegion(node);
        setCycleDuration(duration);		
	}
	
    public final void setRegion(Region value) 
    {
        if ((region != null) || (value != null /* DEFAULT_NODE */)) {
            nodeProperty().set(value);
        }
    }

    public final Region getRegion() 
    {
        return (region == null)? DEFAULT_REGION : region.get();
    }

    public final ObjectProperty<Region> nodeProperty() 
    {
        if (region == null) {
        	region = new SimpleObjectProperty<Region>(this, "region", DEFAULT_REGION);
        }
        return region;
    }
    
    public final void setDuration(Duration value) 
    {
        if ((duration != null) || (!DEFAULT_DURATION.equals(value))) {
            durationProperty().set(value);
        }
    }

    public final Duration getDuration() 
    {
        return (duration == null)? DEFAULT_DURATION : duration.get();
    }
	
    public final ObjectProperty<Duration> durationProperty() 
    {
        if (duration == null) {
            duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION) {

                @Override
                public void invalidated() {
                    try {
                        setCycleDuration(getDuration());
                    } catch (IllegalArgumentException e) {
                        if (isBound()) {
                            unbind();
                        }
                        set(getCycleDuration());
                        throw e;
                    }
                }

                @Override
                public Object getBean() {
                    return SizeTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }
        return duration;
    }
    
    public final void setToWidth(double value) 
    {
        if ((toWidth != null) || (!Double.isNaN(value))) {
            toWidthProperty().set(value);
        }
    }

    public final double getToWidth() 
    {
        return (toWidth == null)? DEFAULT_TO_WIDTH : toWidth.get();
    }

    public final DoubleProperty toWidthProperty() 
    {
        if (toWidth == null) {
        	toWidth = new SimpleDoubleProperty(this, "toWidth", DEFAULT_TO_WIDTH);
        }
        return toWidth;
    }
    
    public final void setFromWidth(double value) 
    {
        if ((fromWidth != null) || (!Double.isNaN(value))) {
        	fromWidthProperty().set(value);
        }
    }

    public final double getFromWidth() 
    {
        return (fromWidth == null)? DEFAULT_FROM_WIDTH : fromWidth.get();
    }

    public final DoubleProperty fromWidthProperty() 
    {
        if (fromWidth == null) {
        	fromWidth = new SimpleDoubleProperty(this, "fromWidth", DEFAULT_FROM_WIDTH);
        }
        return fromWidth;
    }
   
    public final double getToHeight() 
    {
        return (toHeight == null)? DEFAULT_TO_HEIGHT : toHeight.get();
    }
    
    public final void setToHeight(double value) 
    {
        if ((toHeight != null) || (!Double.isNaN(value))) {
        	toHeightProperty().set(value);
        }
    }

    public final DoubleProperty toHeightProperty() 
    {
        if (toHeight == null) {
        	toHeight = new SimpleDoubleProperty(this, "toHeight", DEFAULT_TO_HEIGHT);
        }
        return toHeight;
    }
    
    public final void setFromHeight(double value) 
    {
        if ((fromHeight != null) || (!Double.isNaN(value))) {
        	fromHeightProperty().set(value);
        }
    }

    public final double getFromHeight() 
    {
        return (fromHeight == null)? DEFAULT_FROM_HEIGHT : fromHeight.get();
    }

    public final DoubleProperty fromHeightProperty() 
    {
        if (fromHeight == null) {
        	fromHeight = new SimpleDoubleProperty(this, "fromHeight", DEFAULT_FROM_HEIGHT);
        }
        return fromHeight;
    }
	
    private Region getTargetRegion() 
    {
        final Region region = getRegion();
        return region;
    }
	
    /**
     * {@inheritDoc}
     * 
     * In case of any from/to sizes is (NaN) the pref sizes will be used instead. 
     * In case of pref sizes are {@link Region#USE_COMPUTED_SIZE} the region bounds will be used.
     */
	@Override
	protected void interpolate(double frac) 
	{
		if(cachedRegion == null) {
	        cachedRegion = getTargetRegion();	
		}
		
        double _fromWidth = getFromWidth();
        double _toWidth = getToWidth();
        double _fromHeight = getFromHeight();
        double _toHeight = getToHeight();
        
        if(Double.isNaN(startW)) {
            startW = _fromWidth;
            if(Double.isNaN(_fromWidth)) {
            	if(cachedRegion.getPrefWidth() == Region.USE_COMPUTED_SIZE) {
            		startW = (! cachedRegion.isResizable()) ? cachedRegion.getBoundsInLocal().getWidth() : cachedRegion.getLayoutBounds().getWidth();
            	} else {
            		startW = cachedRegion.getPrefWidth();
            	}
            }	
        }
        
        if(Double.isNaN(startH)) {
            startH = _fromHeight;
            if(Double.isNaN(_fromHeight)) {
            	if(cachedRegion.getPrefHeight() == Region.USE_COMPUTED_SIZE) {
            		startH = (! cachedRegion.isResizable()) ? cachedRegion.getBoundsInLocal().getHeight() : cachedRegion.getLayoutBounds().getHeight();
            	} else {
            		startH = cachedRegion.getPrefHeight();
            	}
            }	
        }
        
        if(Double.isNaN(deltaW)) {
            deltaW = _toWidth - startW;
            if(Double.isNaN(_toWidth)) {
            	if(cachedRegion.getPrefWidth() == Region.USE_COMPUTED_SIZE) {
            		deltaW = (! cachedRegion.isResizable()) ? cachedRegion.getBoundsInLocal().getWidth() - startW : cachedRegion.getLayoutBounds().getWidth() - startW;
            	} else {
            		deltaW = cachedRegion.getPrefWidth() - startW;
            	}
            }	
        }
        
        if(Double.isNaN(deltaH)) {
            deltaH = _toHeight - startH;
            if(Double.isNaN(_toHeight)) {
            	if(cachedRegion.getPrefHeight() == Region.USE_COMPUTED_SIZE) {
            		deltaH = (! cachedRegion.isResizable()) ? cachedRegion.getBoundsInLocal().getHeight() - startH : cachedRegion.getLayoutBounds().getHeight() - startH;
            	} else {
            		deltaH = cachedRegion.getPrefHeight() - startH;
            	}
            }	
        }
      
        cachedRegion.setPrefSize(startW + frac * deltaW, startH + frac * deltaH);
	}
}
