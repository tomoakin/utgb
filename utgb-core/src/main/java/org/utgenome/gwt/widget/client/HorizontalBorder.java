/*--------------------------------------------------------------------------
 *  Copyright 2007 utgenome.org
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
// Border.java
// Since: 2007/11/29
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.widget.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;

/**
 * Horizontal Border for {@link FrameBorder}
 * 
 * @author leo
 * 
 */
public class HorizontalBorder extends Composite {
	public static final int UPPER = 0 << 1;
	public static final int LOWER = 1 << 1;

	protected static final int LEFT = 0;
	protected static final int RIGHT = 1;

	private final FlexTable layoutFrame = new FlexTable();
	private final Label left = new Label();
	private final Label center = new Label();
	private final Label right = new Label();

	private int cornerRadius;

	/**
	 * @param cornerRadius
	 * @param type
	 *            HorizontalBorder.UPPER or {@link HorizontalBorder}.LOWER
	 * @param borderFlag
	 *            bit vector or {@link FrameBorder}.{NORTH, EAST, SOUTH, WEST}
	 * @param color
	 */
	public HorizontalBorder(int cornerRadius, int type, int borderFlag, String color) {
		this.cornerRadius = cornerRadius;
		assert (type == UPPER || type == LOWER);

		setupWidget(type, borderFlag, color);
		initWidget(layoutFrame);
	}

	protected void setupWidget(int type, int borderFlag, String color) {
		int index = 0;
		if ((borderFlag & FrameBorder.WEST) != 0)
			layoutFrame.setWidget(0, index++, left);
		layoutFrame.setWidget(0, index, center);
		layoutFrame.getCellFormatter().setWidth(0, index, "100%");
		index++;
		if ((borderFlag & FrameBorder.EAST) != 0)
			layoutFrame.setWidget(0, index++, right);

		layoutFrame.setCellPadding(0);
		layoutFrame.setCellSpacing(0);
		layoutFrame.setHeight(cornerRadius + "px");

		left.setPixelSize(cornerRadius, cornerRadius);
		right.setPixelSize(cornerRadius, cornerRadius);

		Style.backgroundColor(center, color);
		Style.fontSize(center, 0);
		center.setSize("100%", cornerRadius + "px");
		// layoutFrame.setBorderWidth(1);

		setCorner(left, type | LEFT, cornerRadius, color);
		setCorner(right, type | RIGHT, cornerRadius, color);

	}

	private void setCorner(Label label, int positionType, int cornerRadius, String color) {
		assert (positionType >= 0 && positionType <= 4);

		int backgroundXPos = (positionType & RIGHT) != 0 ? -cornerRadius : 0;
		int backgroundYPos = (positionType & LOWER) != 0 ? -cornerRadius : 0;

		Style.backgroundImage(label, "theme/default/rc" + cornerRadius + ((positionType & LOWER) != 0 ? "d" : "") + ".png");
		Style.backgroundNoRepeat(label);
		Style.backgroundPosition(label, backgroundXPos + "px " + backgroundYPos + "px");
		Style.overflowHidden(label);
		Style.fontSize(label, 0);

		label.setPixelSize(cornerRadius, cornerRadius);

	}

	public void setWidth(int width) {
		this.setWidth(width + "px");
	}

}
