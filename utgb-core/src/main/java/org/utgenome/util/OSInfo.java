/*--------------------------------------------------------------------------
 *  Copyright 2010 utgenome.org
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
// OSInfo.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.util;

/**
 * Utilities for retrieving OS information
 * 
 * @author leo
 * 
 */
public class OSInfo {

	public static int availableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static long availaableFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

}
