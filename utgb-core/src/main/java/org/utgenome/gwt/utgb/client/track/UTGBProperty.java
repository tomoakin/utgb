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
// GenomeBrowser Project
//
// UTGBProperty.java
// Since: Jun 14, 2007
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.gwt.utgb.client.track;

/**
 * Common properties shared in a track group
 * 
 * @author leo
 * 
 */
public interface UTGBProperty {
	public static final String SEQUENCE_SIZE = "sequence_size";
	public static final String SPECIES = "species";
	public static final String REVISION = "revision";
	public static final String TARGET = "target";
	public static final String GROUP = "group";
	public static final String DB_GROUP = "dbGroup";
	public static final String DB_NAME = "dbName";

	public static final String[] coordinateParameters = { SPECIES, REVISION, TARGET, GROUP, DB_GROUP, DB_NAME };

}
