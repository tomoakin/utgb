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
// CompactFASTATest.java
// Since: 2010/03/12
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.util.sequence;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xerial.util.FileResource;
import org.xerial.util.log.Logger;

public class CompactFASTATest {

	private static Logger _logger = Logger.getLogger(CompactFASTATest.class);
	static String workDir = "target";

	@BeforeClass
	public static void init() throws Exception {
		CompactFASTAGenerator g = new CompactFASTAGenerator();
		g.packFASTA(FileResource.find(CompactFASTATest.class, "sample.fa"));
	}

	@AfterClass
	public static void finish() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void sequence() throws Exception {
		CompactFASTA c = new CompactFASTA("target/sample.fa");
		GenomeSequence seq = c.getSequence("chr1", 0, 100);
		assertEquals("NNNNNNACGGATTCTTGCTATATANTTACTTACCCGTAGTCTAGAGATCTTTCCAATATCGTCT", seq.toString());
	}

}
