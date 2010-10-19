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
// NucleotideVariationTest.java
// Since: 2010/10/13
//
//--------------------------------------
package org.utgenome.util.sv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.utgenome.util.sv.GeneticVariation.VariationType;
import org.xerial.util.log.Logger;

public class NucleotideVariationTest {

	private static Logger _logger = Logger.getLogger(NucleotideVariationTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void toSilk() throws Exception {
		GeneticVariation snv = new GeneticVariation(VariationType.PointMutation, "chr1", 3, 4, "A");
		_logger.info(snv);

	}

}