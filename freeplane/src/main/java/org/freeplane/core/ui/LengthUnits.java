package org.freeplane.core.ui;

import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.Convertible;
import org.freeplane.core.util.Quantity;

public enum LengthUnits implements Convertible{
/*
+---------+-------------+---------------+
| px      | Pixels      | Varies        | 
+---------+-------------+---------------+
| in      | Inches      | 1             | 
+---------+-------------+---------------+
| mm      | Millimeters | 25.4          | 
+---------+-------------+---------------+
| cm      | Centimeters | 2.54          | 
+---------+-------------+---------------+
| pt      | Points      | 72            | 
+---------+-------------+---------------+
		
 */
		px(1d), 
		in(UITools.getScreenResolution()), 
		mm(UITools.getScreenResolution() / 25.4), 
		cm(UITools.getScreenResolution() / 2.54),
		pt(UITools.getScreenResolution() / 72.0);
		
		LengthUnits(double factor){
			this.factor = factor;
			
		}
		final private double factor;
		@Override
		public double factor() {
			return factor;
		}
		
		static public Quantity<LengthUnits> pixelsInPt(double value){
			return new Quantity<LengthUnits>(value, px).in(pt);
		}
		
		static public Quantity<LengthUnits> fromStringInPt(String value){
			return Quantity.fromString(value, px).in(pt);
		}
	}
