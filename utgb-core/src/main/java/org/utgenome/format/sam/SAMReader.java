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
// SAMReader.java
// Since: Dec 3, 2009
//
// $URL$ 
// $Author$
//--------------------------------------
package org.utgenome.format.sam;

import java.io.InputStream;

import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMRecord;

/**
 * SAM File reader
 * 
 * @author leo
 * 
 */
public class SAMReader {

	public static Iterable<SAMRecord> getSAMRecord(InputStream samFile) {
		return new SAMFileReader(samFile);
	}
}
