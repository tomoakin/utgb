/*--------------------------------------------------------------------------
 *  Copyright 2009 utgenome.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// utgb-core Project
//
// WeaverCanvas.java
// Since: 2010/04/23
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.ribbon.client.ui;

import org.utgenome.gwt.utgb.client.ui.FixedWidthLabel;
import org.utgenome.gwt.utgb.client.ui.RoundCornerFrame;
import org.utgenome.gwt.widget.client.Style;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Multi layer canvas for drawing genome data
 * 
 * @author leo
 * 
 */
public class WeaverCanvas extends Composite {

	private final AbsolutePanel mainPanel;
	private final GWTCanvas canvas1;
	private final GWTCanvas canvas2;

	public WeaverCanvas() {

		int width = 800;
		int height = 500;
		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(width, height);
		canvas1 = new GWTCanvas(width, height);
		canvas2 = new GWTCanvas(width, height);

		//		CanvasGradient gr = canvas1.createRadialGradient(45, 45, 10, 52, 50, 30);
		//		gr.addColorStop(0, new Color(0xA7, 0XD3, 0x0C));
		//		gr.addColorStop(0.9f, new Color(0x01, 0x9F, 0x62, 0f));
		//		gr.addColorStop(1, new Color("rgba(1,159,98,0)"));
		//canvas1.setFillStyle(gr);
		canvas1.setFillStyle(new Color(200, 0, 0, 0.7f));
		canvas1.fillRect(0, 0, 150, 150);

		canvas2.setFillStyle(new Color(100, 200, 250));
		canvas2.fillRect(30, 20, 200, 150);

		mainPanel.add(canvas2, 0, 0);
		mainPanel.add(canvas1, 0, 0);

		RoundCornerFrame f = new RoundCornerFrame("3E5A77", 0.8f, 4);
		FixedWidthLabel l = new FixedWidthLabel("Hello World. Nice to meet you. Welcome to UTGB Toolkit", 200);
		Style.fontSize(l, 12);
		Style.fontColor(l, "white");
		//Style.semiTransparentBackground(l, "3E5A77", 0.8f);
		f.setWidget(l);

		//		l.setPixelSize(200, 19);
		//		//l.setStyleName("label");
		//		DOM.setStyleAttribute(l.getElement(), "backgroundImage", "url(utgb-core/transparent?color=3E5A77&opacity=0.7)");
		//		DOM.setStyleAttribute(l.getElement(), "textOverflow", "ellipsis");
		//		DOM.setStyleAttribute(l.getElement(), "overflow", "hidden");
		//		DOM.setStyleAttribute(l.getElement(), "whiteSpace", "nowrap");
		//		DOM.setStyleAttribute(l.getElement(), "display", "block");
		//DOM.setStyleAttribute(l.getElement(), "color", "white");

		mainPanel.add(f, 10, 10);

		initWidget(mainPanel);
	}

	private int pos = 0;

	public void move() {

		pos += 2;

		//Style.set(w, cssPropertyName, value)
		mainPanel.add(canvas1, pos, pos);

	}

	public void fade() {

	}

}
