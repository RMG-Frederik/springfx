/*
 * Copyright 2018 CodeCrafting.net
 *
 * Licensed under the GNU GENERAL PUBLIC LICENSE, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.codecrafting.springfx.animation;

import javafx.animation.Interpolator;

public class EaseInterpolator
{
	//BASIC Interpolators
	public static final Interpolator LINEAR = Interpolator.LINEAR;
	public static final Interpolator EASE = Interpolator.SPLINE(0.250, 0.100, 0.250, 1.000);
	public static final Interpolator EASE_IN = Interpolator.EASE_IN;
	public static final Interpolator EASE_OUT = Interpolator.EASE_OUT;
	public static final Interpolator EASE_IN_OUT = Interpolator.EASE_BOTH;
	
	//Penner Equations Interpolators (IN)
	public static final Interpolator EASE_IN_QUAD = Interpolator.SPLINE(0.550, 0.085, 0.680, 0.530);
	public static final Interpolator EASE_IN_CUBIC = Interpolator.SPLINE(0.550, 0.055, 0.675, 0.190);
	public static final Interpolator EASE_IN_QUART = Interpolator.SPLINE(0.895, 0.030, 0.685, 0.220);
	public static final Interpolator EASE_IN_QUINT = Interpolator.SPLINE(0.755, 0.050, 0.855, 0.060);
	public static final Interpolator EASE_IN_SINE = Interpolator.SPLINE(0.470, 0.000, 0.745, 0.715);
	public static final Interpolator EASE_IN_EXPO = Interpolator.SPLINE(0.950, 0.050, 0.795, 0.035);
	public static final Interpolator EASE_IN_CIRC = Interpolator.SPLINE(0.600, 0.040, 0.980, 0.335);
	//public static final Interpolator EASE_IN_BACK = Interpolator.SPLINE(0.600, -0.280, 0.735, 0.045);
	
	//Penner Equations Interpolators (OUT)
	public static final Interpolator EASE_OUT_QUAD = Interpolator.SPLINE(0.250, 0.460, 0.450, 0.940);
	public static final Interpolator EASE_OUT_CUBIC = Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000);
	public static final Interpolator EASE_OUT_QUART = Interpolator.SPLINE(0.165, 0.840, 0.440, 1.000);
	public static final Interpolator EASE_OUT_QUINT = Interpolator.SPLINE(0.230, 1.000, 0.320, 1.000);
	public static final Interpolator EASE_OUT_SINE = Interpolator.SPLINE(0.390, 0.575, 0.565, 1.000);
	public static final Interpolator EASE_OUT_EXPO = Interpolator.SPLINE(0.190, 1.000, 0.220, 1.000);
	public static final Interpolator EASE_OUT_CIRC = Interpolator.SPLINE(0.075, 0.820, 0.165, 1.000);
	//public static final Interpolator EASE_OUT_BACK = Interpolator.SPLINE(0.175, 0.885, 0.320, 1.275);
	
	//Penner Equations Interpolators (IN_OUT)
	public static final Interpolator EASE_IN_OUT_QUAD = Interpolator.SPLINE(0.455, 0.030, 0.515, 0.955);
	public static final Interpolator EASE_IN_OUT_CUBIC = Interpolator.SPLINE(0.645, 0.045, 0.355, 1.000);
	public static final Interpolator EASE_IN_OUT_QUART = Interpolator.SPLINE(0.770, 0.000, 0.175, 1.000);
	public static final Interpolator EASE_IN_OUT_QUINT = Interpolator.SPLINE(0.860, 0.000, 0.070, 1.000);
	public static final Interpolator EASE_IN_OUT_SINE = Interpolator.SPLINE(0.445, 0.050, 0.550, 0.950);
	public static final Interpolator EASE_IN_OUT_EXPO = Interpolator.SPLINE(1.000, 0.000, 0.000, 1.000);
	public static final Interpolator EASE_IN_OUT_CIRC = Interpolator.SPLINE(0.785, 0.135, 0.150, 0.860);
	//public static final Interpolator EASE_IN_OUT_BACK = Interpolator.SPLINE(0.680, -0.550, 0.265, 1.550);
	
	public static Interpolator getCubicBezierInterpolator(double x1, double y1, double x2, double y2)
	{
		return Interpolator.SPLINE(x1, y1, x2, y2);
	}
}
